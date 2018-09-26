package com.bithaw.zbt.utils;

import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.util.SafeEncoder;

/**
 * @Auther: lijy
 * @Date: 2018/7/12 11:42
 * @Description:
 */
@Component
public class RedisComponent {

    private static final Logger logger = LoggerFactory.getLogger(RedisComponent.class);

    private RedisSerializer serializer;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 写入缓存
     */
    public void set(final String key, final Object value) {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.set(SafeEncoder.encode(key), serialize(value));
            return null;
        });
    }

    /**
     * 写入缓存
     */
    public void setString(final String key, final String value) {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.set(SafeEncoder.encode(key), SafeEncoder.encode(value));
            return null;
        });
    }

    /**
     * 写入缓存设置时效时间
     */
    public void setex(final String key, Object value, Long expireTime) {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.setEx(SafeEncoder.encode(key), expireTime, serialize(value));
            return null;
        });
    }

    public void setexString(final String key, String value, Long expireTime) {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.setEx(SafeEncoder.encode(key), expireTime, SafeEncoder.encode(value));
            return null;
        });
    }

    public boolean setnx(final String key, Object value) {
        return (boolean) redisTemplate.execute((RedisCallback<Boolean>)redisConnection -> redisConnection.setNX(SafeEncoder.encode(key), serialize(value)));
    }

    public boolean setnxString(final String key, String value) {
        return (boolean) redisTemplate.execute((RedisCallback<Boolean>)redisConnection -> redisConnection.setNX(SafeEncoder.encode(key), SafeEncoder.encode(value)));
    }

    public void expire(final String key, Long expire){
        redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.expire(SafeEncoder.encode(key), expire);
            return null;
        });
    }

    public <T> T get(final String key, Class<T> clazz) {
        return (T) redisTemplate.execute((RedisCallback<T>) redisConnection -> {
            byte[] bs = redisConnection.get(SafeEncoder.encode(key));
            return deserialize(bs, clazz);
        });
    }

    public void del(String key) {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            return redisConnection.del(SafeEncoder.encode(key));
        });
    }

    public void lpush(final String key, final Object value) {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.lPush(SafeEncoder.encode(key), serialize(value));
            return null;
        });
    }

    RedisSerializer getSerializer() {
        if (this.serializer == null) {
            synchronized (this) {
                if (this.serializer == null) {
                    // 为了向下兼容默认,如果没有提供序列化器,默认使用,json序列化
                    serializer = new JsonSerializer();
                    logger.info(
                        "RedisComponent [" + this.toString() + "] is done! serializer:" + serializer
                            .toString());
                }
            }
        }

        return serializer;
    }

    public void setSerializer(RedisSerializer serializer) {
        this.serializer = serializer;
    }

    private byte[] serialize(Object object) {
        return getSerializer().serialize(object);
    }

    private <T> T deserialize(byte[] byteArray, Class<T> c) {
        return getSerializer().deserialize(byteArray, c);
    }

    private <E> List<E> deserializeForList(byte[] byteArray, Class<E> elementC) {
        return getSerializer().deserializeForList(byteArray, elementC);
    }

    /**
     * 通过SETNX试图获取一个锁
     *
     * @param expire 存活时间(秒)
     */
    public boolean acquire(final String key, final long expire) {
        return (boolean) redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            boolean success = false;
            try {
                long value = System.currentTimeMillis() + expire * 1000 + 1;
                // 通过setnx获取一个lock
                success = redisConnection
                    .setNX(SafeEncoder.encode(key), SafeEncoder.encode(String.valueOf(value)));
                // setnx成功，则成功获取一个锁
                if (success) {
                    if (expire > 0) {
                        try {
                            redisConnection.expire(SafeEncoder.encode(key), expire);
                        } catch (Throwable e) {
                            logger.error("", e);
                        }
                    }
                    success = true;
                }
                // setnx失败，说明锁仍然被其他对象保持，检查其是否已经超时
                else {
                    // 当前锁过期时间
                    long oldValue = deserialize(redisConnection.get(SafeEncoder.encode(key)),
                        Long.class);
                    // 超时
                    if (oldValue < System.currentTimeMillis()) {
                        // 查看是否有并发
                        String oldValueAgain = deserialize(redisConnection
                            .getSet(SafeEncoder.encode(key),
                                SafeEncoder.encode(String.valueOf(value))), String.class);
                        // 获取锁成功
                        if (Long.valueOf(oldValueAgain) == oldValue) {
                            if (expire > 0) {
                                try {
                                    redisConnection.expire(SafeEncoder.encode(key), expire);
                                } catch (Throwable e) {
                                    logger.error("", e);
                                }
                            }
                            success = true;
                        } else {
                            // 已被其他进程捷足先登了
                            success = false;
                        }
                    } else {
                        // 未超时，则直接返回失败
                        success = false;
                    }
                }
            } catch (Throwable e) {
                logger.error("", e);
            }
            return success;
        });
    }

    private final static Pattern LCK_TIME = Pattern.compile("\\d+");

    /**
     * 释放锁
     */
    public void release(final String key) {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            try {
                String lckUUID = deserialize(redisConnection.get(SafeEncoder.encode(key)),
                    String.class);
                if (lckUUID == null || !LCK_TIME.matcher(lckUUID).find()) {
                    return null;
                }
                Long getValue = Long.parseLong(lckUUID);
                // 避免删除非自己获取得到的锁
                if (System.currentTimeMillis() < getValue) {
                    redisConnection.del(SafeEncoder.encode(key));
                }
            } catch (Exception e) {
                logger.error("", e);
            }
            return null;
        });
    }
}
