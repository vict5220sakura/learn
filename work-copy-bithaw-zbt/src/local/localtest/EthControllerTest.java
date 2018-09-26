package localtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bithaw.zbt.BithawZBTTradeApplication;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes=BithawZBTTradeApplication.class)
@WebAppConfiguration
@ContextConfiguration
public class EthControllerTest {
    
	@Autowired
    private WebApplicationContext context;

	@Value("${eth-admin.gas.price}")
	private String gasPrice;
	
	@Test
	public void gasPrice(){
		log.info("gas测试 : " + gasPrice);
	}
	
    private MockMvc mvc;
    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }
    
    @Test
    public void testEthController() throws Exception {
        RequestBuilder request = null;
        //路径
        request = post("/eth/newZbtAddress")
                //参数
                .content("{\"password\":\"123\"}").contentType(MediaType.APPLICATION_JSON)
                //接受的数据类型
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request)
                //状态吗是否相等
               .andExpect(status().isOk())
                //得到的信息是否与特定字段匹配
                .andExpect(content().string("1"))
                //输出信息
                .andDo(print());
    }
    @Test
    public void testEthControllerNewAccount() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/newAccount")
    			//参数
    			.content("{\"password\":\"123456\"}").contentType(MediaType.APPLICATION_JSON)
    			//接受的数据类型
    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    }
    
    @Test
    public void testEthControllerGetAccounts() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = get("/eth/getAccount")
    			//参数
    			//.content("{\"password\":\"123456\"}").contentType(MediaType.APPLICATION_JSON)
    			//接受的数据类型
    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    }
    @Test
    public void testEthControllerNewWallet() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/newWallet")
    			//参数
    			.content("{\"password\":\"123456\"}").contentType(MediaType.APPLICATION_JSON)
    			//接受的数据类型
    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    }
    
    @Test
    public void testEthControllerGetCredentialsByMnemonic() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/newWallet")
    			//参数
    			.content("{\"password\":\"123456\"}").contentType(MediaType.APPLICATION_JSON)
    			//接受的数据类型
    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    }
    
    @Test
    public void testEthControllerNewZbtAddress() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/newZbtAddress")
    			//参数
    			.param("password","123")
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
    			//接受的数据类型
    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    /**
     * 提币
     * @throws Exception
     */
    @Test
    public void withdraw() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/withdraw")
    			//参数
    			.param("address","0x237edb43e3eeb2f5600cdd569cda4949b6c1766f")
    			.param("password","123")
    			.param("toAddress","0xa654850cde5bad2d43e394ceb6d66b8da761dd31")
    			.param("amount","100")
    			.param("contractAddress","0x4a1bb066ad3952226e83afe8bddba4cb4dc36a56")
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
    			//接受的数据类型
    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    /**
     * zbt充值
     * @throws Exception
     */
    @Test
    public void zbtPay() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/withdraw")
    			//参数
    			.param("address","0xa654850cde5bad2d43e394ceb6d66b8da761dd31")
    			.param("password","123")
    			.param("toAddress","0x237edb43e3eeb2f5600cdd569cda4949b6c1766f")
    			.param("amount","100")
    			.param("contractAddress","0x4a1bb066ad3952226e83afe8bddba4cb4dc36a56")
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
    			//接受的数据类型
    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    /**
     * 创建zbt账户
     * @throws Exception
     */
    @Test
    public void newZbtAccount() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/newZbtAddress")
    			//参数
    			.param("password","123")
    			.contentType(MediaType.MULTIPART_FORM_DATA)
    			//接受的数据类型
    			.accept(MediaType.TEXT_EVENT_STREAM_VALUE);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    /**
     * 创建zbt账户
     * @throws Exception
     */
    @Test
    public void tradeStatus() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/tradeStatus")
    			//参数
    			.param("transactionHash","0xff1bdac70229d7d951a3d825a423e921263c22fce315d5b803b28dde59ddcd5e")
    			.contentType(MediaType.MULTIPART_FORM_DATA)
    			//接受的数据类型
    			.accept(MediaType.TEXT_EVENT_STREAM_VALUE);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    @Test
    public void zbtTrade() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/zbtTrade")
    			//参数
    			.param("fromAddress","0x763b78bc83ef328a252b25b56ba2a7fe49774b72")
    			.param("mnemonic","idea hire door road hybrid business steak victory kangaroo notice actress motion")
    			.param("password","123")
    			.param("toAddress","0x00B8eB7D9eb0d47924e68b56A2F6599ef844B617")
    			.param("amount","0.000001")
    			.param("solidityAddress","0xb00ecbd39b5138f9eb7680205f565848b3699742")
    			.param("gasPrice","1")
    			.param("nonce","");
//    			.contentType(MediaType.MULTIPART_FORM_DATA)
    			//接受的数据类型
//    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    @Test
    public void ethTrade() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/ethTrade")
    			//参数
    			.param("fromAddress","0x763b78bc83ef328a252b25b56ba2a7fe49774b72")
    			.param("mnemonic","idea hire door road hybrid business steak victory kangaroo notice actress motion")
    			.param("password","123")
    			.param("toAddress","0x00B8eB7D9eb0d47924e68b56A2F6599ef844B617")
    			.param("amount",  "0.001")
    			.param("gasPrice","6")
    			.param("nonce","3")
    			.contentType(MediaType.MULTIPART_FORM_DATA)
    			//接受的数据类型
    			.accept(MediaType.APPLICATION_JSON);
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    @Test
    public void getEth() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/getEth")
    			//参数
//    			.param("address","0xa654850cde5bad2d43e394ceb6d66b8da761dd31");
    			.param("address","0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    @Test
    public void getZbt() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/getZbt")
    			//参数
    			.param("address","0x763b78bc83ef328a252b25b56ba2a7fe49774b72")
//    			.param("address","0x763b78bc83ef328a252b25b56ba2a7fe49774b72")
    			.param("solidityAddress","0xb00ecbd39b5138f9eb7680205f565848b3699742");
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    @Test
    public void getTradeStatus() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/getTradeStatus")
    			//参数
    			.param("transactionHash","0xd9fc2e434c8785b855664826ea4e73ab3da07a3f8cbae32a62b80037e4bcc0b9");
//    			.param("address","0x763b78bc83ef328a252b25b56ba2a7fe49774b72")
//    			.param("solidityAddress","0xb00ecbd39b5138f9eb7680205f565848b3699742");
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    @Test
    public void getTradeFee() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/getTradeFee")
    			//参数
    			.param("transactionHash","0x27888e627834e455a705d442fcacc665577a4bba1a04a51272a735e034cb9e1d");
//    			.param("address","0x763b78bc83ef328a252b25b56ba2a7fe49774b72")
//    			.param("solidityAddress","0xb00ecbd39b5138f9eb7680205f565848b3699742");
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    @Test
    public void getNonce() throws Exception {
    	RequestBuilder request = null;
    	//路径
    	request = post("/eth/getNonce")
    			//参数
    			.param("address","0x763b78BC83Ef328A252b25b56bA2A7Fe49774b72");
//    			.param("address","0x763b78bc83ef328a252b25b56ba2a7fe49774b72")
//    			.param("solidityAddress","0xb00ecbd39b5138f9eb7680205f565848b3699742");
    	mvc.perform(request)
    	//状态吗是否相等
    	.andExpect(status().isOk())
    	//得到的信息是否与特定字段匹配
    	//.andExpect(content().string("1"))
    	//输出信息
    	.andDo(print());
    	//0xaef48dae0928bb9652c349be594f1188aceb9764
    }
    
    
    public static void main(String[] args) {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1533034780000L)));
	}
    
    
}
