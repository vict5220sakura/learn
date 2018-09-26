package localtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.utils.RedisComponent;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class RedisLockTest {
	@Autowired
    private RedisComponent redisComponent;
	
	@Test
	public void testLock(){
		if(redisComponent.acquire("ADDRESS_NONCE:0x763b78bc83ef328a252b25b56ba2a7fe49774b72", 10L)){
			System.out.println("成功");
		}else{
			System.out.println("失败");
		}
	}
}
