package com.bithaw.zbt.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Description 线程池配置类,用于注入线程池方便执行一部操作 在需要异步执行的方法上配置@Async就可以异步执行了 进阶
 *              https://www.cnblogs.com/yw0219/p/8810956.html
 * @author WangWei
 * @date 2018年8月24日 上午11:04:53
 * @version V 1.0
 */
@Configuration
@EnableAsync
public class ThreadPoolAsyncConfig {

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(6);
		// 设置最大线程数
		executor.setMaxPoolSize(12);
		// 设置队列容量
		executor.setQueueCapacity(20);
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(60);
		// 设置默认线程名称
		executor.setThreadNamePrefix("ThreadTaskPool-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}
}
