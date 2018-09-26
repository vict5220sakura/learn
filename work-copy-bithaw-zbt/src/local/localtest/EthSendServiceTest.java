package localtest;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.service.EthSystemService;

@SpringBootTest(classes = BithawZBTTradeApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EthSendServiceTest {
	@Autowired
	EthSystemService ethSendService;
	
	public static void main(String[] args) {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1534995000147l)));
	}
	
	@Test
	public void getStateTest(){
		int state = ethSendService.getState("5220");
		System.out.println(state);
	}
	
	@Test
	public void etherscanGetAllTXByAddressTest(){
		System.out.println();
	}
}
