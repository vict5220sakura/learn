package localtest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Response.Error;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.EthSyncing.Result;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.utils.BytesHexStrTranslate;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Hash;
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class Web3jTest {
	@Autowired
	private Web3j web3j;
	@Autowired
	private Admin admin;
	
	@Value("${web3j.client-address}")
	private String ethRPCAddress;
	
	@Test
	public void ethRPCaddress(){
		log.info("测试 : " + ethRPCAddress);
	}
	/*@Value("${redis.ip}")
	private String redisIpTest;
	
	@Test
	public void test(){
		System.out.println("王玮 测试 " + redisIpTest);
	}*/
	/**
	 * 验证交易是否有效
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Test
	public void checkTrade() throws IOException, InterruptedException, ExecutionException{
		EthTransaction ethTransaction = web3j.ethGetTransactionByHash("0xff1bdac70229d7d951a3d825a423e921263c22fce315d5b803b28dde59ddcd5e").sendAsync().get();
		log.info("测试 : " + (ethTransaction==null));
//		log.info("测试 : " + transaction.getBlockNumber());
		

	}
	
	
	//生成助记词钱包
	@Test
	public void createWallet() throws CipherException, IOException{
		String keyStoreDir = WalletUtils.getDefaultKeyDirectory();
		log.info("生成keyStore文件的默认目录：" + keyStoreDir);
		File file = new File(keyStoreDir);  
		if(!file.exists()){  
		    file.mkdirs();  
		} 
		// 通过密码及keystore目录生成钱包
		Bip39Wallet wallet = WalletUtils.generateBip39Wallet("123",file);
		// keyStore文件名
		log.info(wallet.getFilename());
		// 12个单词的助记词
		log.info(wallet.getMnemonic());
//		2018-07-19 09:52:19.258 [main] INFO  com.bithaw.zbt.service.Web3jTest [Web3jTest.java:64] - 生成keyStore文件的默认目录：C:\Users\Administrator\AppData\Roaming\Ethereum
//		2018-07-19 09:52:19.947 [main] INFO  com.bithaw.zbt.service.Web3jTest [Web3jTest.java:72] - UTC--2018-07-19T01-52-19.925000000Z--fdcb189654b38fc11ebfc9259d2c8767fb5e105a.json
//		2018-07-19 09:52:19.948 [main] INFO  com.bithaw.zbt.service.Web3jTest [Web3jTest.java:74] - rhythm flag salmon seminar eager advice laugh crane wonder silk craft fee
		
		//0xe5c59774f4c108895707fc923479d39bb1b1fadd 测试合约地址
		
//		2018-07-19 10:27:41.455 [main] INFO  com.bithaw.zbt.service.Web3jTest [Web3jTest.java:66] - 生成keyStore文件的默认目录：C:\Users\Administrator\AppData\Roaming\Ethereum
//		2018-07-19 10:28:42.095 [main] INFO  com.bithaw.zbt.service.Web3jTest [Web3jTest.java:74] - UTC--2018-07-19T02-28-42.81000000Z--763b78bc83ef328a252b25b56ba2a7fe49774b72.json
//		2018-07-19 10:29:42.096 [main] INFO  com.bithaw.zbt.service.Web3jTest [Web3jTest.java:76] - idea hire door road hybrid business steak victory kangaroo notice actress motion
	}
	
	/**
	 * web3j 离线签名交易
	 * @throws Exception 
	 */
	@Test
	public void tradeOutline() throws Exception{
		localSendEther("0x763b78bc83ef328a252b25b56ba2a7fe49774b72", "123", "idea hire door road hybrid business steak victory kangaroo notice actress motion", "0xfdcb189654b38fc11ebfc9259d2c8767fb5e105a", new BigInteger("1"));
	}
	
	
	//本地调用keystore文件转账方式
    public void localSendEther(String from,String password,String mnemonic,String to,BigInteger value) throws Exception {
        //加载本地KeyStore文件生成Credentials对象
    	Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        //Credentials credentials = WalletUtils.loadCredentials(password,keyStore);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger gasPrice = new BigInteger("120000000000");
        BigInteger gasLimit = Contract.GAS_LIMIT.divide(new BigInteger("2"));
        //生成RawTransaction交易对象
        RawTransaction rawTransaction  = RawTransaction.createTransaction(nonce,gasPrice,gasLimit,to,value,"abcde123");//可以额外带数据
        //使用Credentials对象对RawTransaction对象进行签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        if(ethSendTransaction.hasError()) {
            String message=ethSendTransaction.getError().getMessage();
            System.out.println("transaction failed,info:"+message);
            log.info("transaction failed,info:"+message);
        }
        else {
            String hash=ethSendTransaction.getTransactionHash();
            EthGetTransactionReceipt send = web3j.ethGetTransactionReceipt(hash).send();
            System.out.println("transaction from "+from+" to "+to+" amount:"+value);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("transaction from "+from+" to "+to+" amount:"+value+" time:"+df.format(new Date()));
        }
    }
    
    /**
     * 离线签名代币转账
     * @throws Exception 
     */
    @Test
    public void tokenTrade() throws Exception{
    	
    	sendTokenOutline("0x763b78bc83ef328a252b25b56ba2a7fe49774b72", "123", "idea hire door road hybrid business steak victory kangaroo notice actress motion", "0xe5c59774f4c108895707fc923479d39bb1b1fadd",//
    			new BigInteger("0"));
    }
    
  //本地调用keystore文件转账方式
    public void sendTokenOutline(String from,String password,String mnemonic,String to,BigInteger value) throws Exception {
    	String data = FunctionEncoder
				.encode(new Function("transfer", Arrays.asList(new Address("0xa654850cde5bad2d43e394ceb6d66b8da761dd31"), new Uint256(new BigInteger("10"))),
						Arrays.asList(new TypeReference<Bool>() {
						})));
        //加载本地KeyStore文件生成Credentials对象
    	Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        //Credentials credentials = WalletUtils.loadCredentials(password,keyStore);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger gasPrice = new BigInteger("120000000000");
        BigInteger gasLimit = new BigDecimal("0.00021")//
        		.multiply(new BigDecimal("1000000000000000000"))//
        		.divide(new BigDecimal(gasPrice.toString()),BigDecimal.ROUND_HALF_UP).toBigInteger();
        //生成RawTransaction交易对象
        RawTransaction rawTransaction  = RawTransaction.createTransaction(nonce,gasPrice,gasLimit,to,value,data);//可以额外带数据
        //使用Credentials对象对RawTransaction对象进行签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        if(ethSendTransaction.hasError()) {
            String message=ethSendTransaction.getError().getMessage();
            System.out.println("transaction failed,info:"+message);
            log.info("transaction failed,info:"+message);
        }
        else {
            String hash=ethSendTransaction.getTransactionHash();
            EthGetTransactionReceipt send = web3j.ethGetTransactionReceipt(hash).send();
            System.out.println("transaction from "+from+" to "+to+" amount:"+value);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("transaction from "+from+" to "+to+" amount:"+value+" time:"+df.format(new Date()));
        }
    }
 
	
	@Test
	public void getNoce() throws InterruptedException, ExecutionException{
		EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount("0x763b78bc83ef328a252b25b56ba2a7fe49774b72", DefaultBlockParameterName.LATEST).sendAsync().get();//获取账户交易信息
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        System.out.println("次数: "+nonce);
	}
	
	@Test
	public void txpool2(){
		
	}
	
	
	
//	public static void main(String[] args) {
//		txpool();
//	}
	
	@Test
	public void test02() throws IOException{
		String result = web3j.shhVersion().send().getResult();
		log.info("测试 : " + result);
	}
	
	@Test
	public void getBlockInfo(){
//		web3j.ethGetBlockByNumber(new DefaultBlockParameter, returnFullTransactionObjects);
	}
	
	/**
	 * 解析指定区块的内容
	 * 输入 区块高度
	 * 返回 区块交易内容实体类
	 */
	@Test
	public void getBlock() throws Exception{
		BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();//当前区块高度
		log.info("测试 当前区块高度 : " + blockNumber);
		Block block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(new BigInteger("6001234")), false).send().getBlock();
//		String block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).send().get
		log.info("测试获取区块内容hash : " + block.getHash());
		log.info("测试获取区块内容logsBlom : " + block.getLogsBloom());
		log.info("测试获取区块内容Timestamp : " + block.getTimestamp());
		Date blockDate = new Date(Long.parseLong(block.getTimestamp().toString()+"000"));
		String blockDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blockDate);
		log.info("测试获取区块内容blockDateStr : " + blockDateStr);
//		log.info("测试获取区块内容getSealFields : 开始");
//		List<String> sealFields = block.getSealFields();
//		for(String s : sealFields){
//			log.info("测试获取区块内容getSealFields : " + s);
//		}
//		log.info("测试获取区块内容getSealFields : 结束");
//		log.info("测试获取区块内容getTransactions : " + "开始");
//		List<TransactionResult> transactions = block.getTransactions();
//		log.info("测试获取区块内容getTransactions 共有 : " + transactions.size() + "笔交易");
//		for(TransactionResult transactionResult : transactions){
//			log.info("测试获取区块内容transactionResult : " + transactionResult);
//			log.info("测试获取区块内容transactionResult : " + transactionResult.get());
//			Optional<Transaction> transaction = web3j.ethGetTransactionByHash((String) transactionResult.get()).send().getTransaction();
//			log.info("测试获取区块内容transactionResult chainId : " + transaction.get().getChainId());
//			log.info("测试获取区块内容transactionResult value : " + transaction.get().getValue());
//			log.info("测试获取区块内容transactionResult input : " + transaction.get().getInput());
//			log.info("测试获取区块内容transactionResult input : " + transaction.get());
//		}
//		log.info("测试获取区块内容getTransactions : " + "结束");
		
		List<TransactionResult> transactions = block.getTransactions();
		log.info("测试获取区块内容getTransactions 共有 : " + transactions.size() + "笔交易");
		for(TransactionResult transactionResult : transactions){
			log.info("测试获取区块内容transactionResult : " + transactionResult.get());
			TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt((String) transactionResult.get()).send().getTransactionReceipt().get();
			log.info("测试获取区块内容isStatusOK : " + transactionReceipt.isStatusOK());
			log.info("测试获取区块内容getContractAddress : " + transactionReceipt.getContractAddress());
			log.info("测试获取区块内容getLogsBloom : " + transactionReceipt.getLogsBloom());
			log.info("测试获取区块内容getStatus : " + transactionReceipt.getStatus());
			log.info("测试获取区块内容getGasUsed : " + transactionReceipt.getGasUsed());
			log.info("测试获取区块内容getLogs : " + transactionReceipt.getLogs());
		}
		log.info("测试获取区块内容getTransactions : " + "结束");
		
	}
	
	@Test
	public void gettransactionResult() throws IOException{
		TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt("0xc831c97e0db956787843e815a1f38817c76789b2f844f0cace71573b31e53513").send().getTransactionReceipt().get();
		Transaction transaction = web3j.ethGetTransactionByHash("0xc831c97e0db956787843e815a1f38817c76789b2f844f0cace71573b31e53513").send().getTransaction().get();
		System.out.println();
	}
	
	@Test
	public void getTradeContentInputData() throws Exception{
		Transaction transaction = web3j.ethGetTransactionByHash("0xc831c97e0db956787843e815a1f38817c76789b2f844f0cace71573b31e53513").send().getTransaction().get();
		TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt("0xc831c97e0db956787843e815a1f38817c76789b2f844f0cace71573b31e53513").send().getTransactionReceipt().get();
		String inputData = transaction.getInput();
		log.info("调用合约参数 : " + inputData);
	}
	
	public static void txpool(){
	
	        // TODO 多个参数时使用例子
	//	      String[] temp = new String[]{"0x12341234"};
	//	      Object[] params = new Object[]{"0x1", "0x2", "0x8888f1f195afa192cfee860698584c030f4c9db1", temp};
	
	        // 密码为123456
	//        Object[] params = new Object[]{"0x763b78BC83Ef328A252b25b56bA2A7Fe49774b72"};
	        String methodName = "eth_accounts";
	//        String methodName = "admin_nodeInfo";
	        try {
	//          JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://192.168.199.73:8544"));
	        	JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://47.52.190.231:8545"));
	            Map<String,String> map = new HashMap<>();
	            map.put("content-type", "application/json");
	            client.setHeaders(map);
	            client.invoke(methodName, null);
	            //System.out.println("测试 : " + mapTxpool.get("pendding"));
	            //System.out.println("ceshi"+object.toString());
	//            JSONObject jsonObject = new JSONObject(object.toString().replaceAll("=", ":"));
	//            System.out.println(jsonObject.toString());
	//            System.out.println(jsonObject.getJSONObject("pending").toString());
	//            System.out.println(jsonObject.getJSONObject("queued").toString());
	        } catch (Throwable throwable) {
	            throwable.printStackTrace();
	        }
		}
	@Test
	public void checkEth() throws IOException{
		String web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
		System.out.println(web3ClientVersion);
	}

	public static void asdasdas() throws IOException, CipherException{
//		Credentials loadCredentials = WalletUtils.loadCredentials("123", new File("D://a"));
//		loadCredentials.getEcKeyPair();
//		String generateMnemonic = MnemonicUtils.generateMnemonic(Hash.sha256(BytesHexStrTranslate.toBytes("980662db116bab2848f4ccf82cde736dc5ae3de320b590dd831ef29cd437dfac")));
//		System.out.println(generateMnemonic);
//		Bip39Wallet wallet = WalletUtils.generateBip39Wallet("123",new File("D://"));
//		loadCredentials.getEcKeyPair().getPrivateKey();
//		String generateMnemonic = MnemonicUtils.generateMnemonic(BytesHexStrTranslate.toBytes("980662db116bab2848f4ccf82cde736dc5ae3de320b590dd831ef29cd437dfac"));
//		MnemonicUtils.generateMnemonic(initialEntropy)
//		System.out.println(generateMnemonic);
		Credentials loadBip39Credentials = WalletUtils.loadBip39Credentials("", "all night deputy movie misery diet news bean ask doll gasp custom");
		String address = loadBip39Credentials.getAddress();
		System.out.println(address);
		
	}

	public static void checksss(){
		Function function = new Function(//
				"burn"//
				, Arrays.asList(new Uint256(new BigDecimal("1").multiply(new BigDecimal("1000000")).toBigInteger()))//
				, Arrays.asList(new TypeReference<Bool>() {
				})//
		);
		org.web3j.protocol.core.methods.request.Transaction transaction = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(//
				"0x00B8eB7D9eb0d47924e68b56A2F6599ef844B617"//
				, "0xb00ecbd39b5138f9eb7680205f565848b3699742"//
				, FunctionEncoder.encode(function)//
		);
		
		System.out.println(FunctionEncoder.encode(function));
		System.out.println(transaction.getData());
	}
	public static void freeze(){
		String data = FunctionEncoder
				.encode(new Function("sell", Arrays.asList(new Address("0x458fc273bfdc87087be7adcea44922e45d8935a5")),
						Arrays.asList()));
		System.out.println(data);
	}
	public void testsss(String a){
		
	}
	public void testsss(int a){
		
	}
	
	/**
	 * 调用合约检查冻结状态
	 */
	@Test
	public void testCheckFreeze() {
		Function function = new Function("frozenAccount"//
				, Arrays.asList(new Address("0x763b78bc83ef328a252b25b56ba2a7fe49774b72"))//
				, Arrays.asList(new TypeReference<Bool>() {
				})//
		);
		org.web3j.protocol.core.methods.request.Transaction transaction = //
				org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(//
						"0x763b78bc83ef328a252b25b56ba2a7fe49774b72"//
						, "0xb00ecbd39b5138f9eb7680205f565848b3699742"//
						, FunctionEncoder.encode(function));

		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			Boolean b = (Boolean) results.get(0).getValue();
			log.info("测试" + b + "返回大小" + results.size() + "类型: " +  results.get(0).getTypeAsString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendAsyncTest() throws IOException, InterruptedException, ExecutionException{
		Web3j web3 = Web3j.build(new HttpService("http://47.52.190.231:8545"));
		CompletableFuture<EthSyncing> sendAsync = web3.ethSyncing().sendAsync();
		System.out.println(sendAsync.toString());
	}

	public static void main(String[] args) throws Exception {
			//0xa9059cbb  解码合约入参
	//		String input = "0xa9059cbb000000000000000000000000358fc273bfdc87087be7adcea44922e45d8935a500000000000000000000000000000000000000000000000000000000000186a0";
	//		if(input.startsWith("0xa9059cbb")){
	//			System.out.println("from : 0x" + input.substring(10 + (64 - 40), 74));
	//			System.out.println("value : " + new BigDecimal(Long.parseLong(input.substring(74, 138), 16)).divide(new BigDecimal("1000000")));
	//		}
			
	//		String hexInt = "00000000000000000000000000000000000000000000000000000000000186a0";
	//		System.out.println(Long.parseLong(hexInt, 16));
			//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1533013500179l)));
			//txpool();
			//freeze();
			//freeze();
			/*Web3j web3 = Web3j.build(new HttpService("http://47.52.190.231:8545"));
			//BigInteger blockNumber = web3.ethBlockNumber().send().getBlockNumber();
			Result string = web3.ethSyncing().send().getResult();
			System.out.println(string.toString());*/
			fugai();
		}

	public static void viewFrozen(){
		String address = "0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b";
		String solidityAddress = "0xb00ecbd39b5138f9eb7680205f565848b3699742";
		Function function = new Function(//
				"frozenAccount"//
				, Arrays.asList(new Address(address))//
				, Arrays.asList( new TypeReference<Bool>() {} )//
		);
		org.web3j.protocol.core.methods.request.Transaction transaction = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(//
				address//
				, solidityAddress//
				, FunctionEncoder.encode(function)//
		);
	}
	
	/**
		 * 手动覆盖交易
		 * @throws Exception 
		 */
		public static void fugai() throws Exception{
			//Web3j web3 = Web3j.build(new HttpService("http://47.52.190.231:8545"));
			String fromAddress = "0x763b78bc83ef328a252b25b56ba2a7fe49774b72";
			String mnemonic = "idea hire door road hybrid business steak victory kangaroo notice actress motion";
			String password = "123";
			String toAddress = "0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b";
			String solidityAddress = "0xb00ecbd39b5138f9eb7680205f565848b3699742";
			BigInteger nonce = new BigInteger("68");
			BigInteger value = new BigInteger("1");
			BigInteger gasPrice = new BigInteger("6000000000");
			BigInteger gasLimit = BigInteger.valueOf(500000L);
			
			String data = FunctionEncoder
					.encode(new Function("transfer", Arrays.asList(new Address(toAddress), new Uint256(value)),
							Arrays.asList(new TypeReference<Bool>() {
							})));
	    	
//			String data = FunctionEncoder.encode(//
//					new Function(//
//							"freezeAccount"//
//							, Arrays.asList(//
//									new Address(toAddress)//
//									, new Bool(false)//
//									)//
//							, Arrays.asList()//
//							)//
//					);
			
			System.out.println("data : " + data);
			
	        //加载助记词+密码生成Credentials对象
	    	Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
	        //生成RawTransaction交易对象
	        RawTransaction rawTransaction  = RawTransaction.createTransaction(//
	        		nonce,gasPrice,gasLimit,solidityAddress,new BigInteger("0"),data);//可以额外带数据
	        
	        //使用Credentials对象对RawTransaction对象进行签名
	        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
	        String hexValue = Numeric.toHexString(signedMessage);
	        System.out.println("签名1 "+hexValue);
	        
	//        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
	//        String transactionHash = ethSendTransaction.getTransactionHash();
	//        System.out.println(transactionHash);
	//        if(ethSendTransaction.hasError()) {
	//            String message=ethSendTransaction.getError().getMessage();
	//            System.out.println("transaction failed,info:"+message);
	//            throw new Exception(message);
	//        }
	//        else {
	//            String hash=ethSendTransaction.getTransactionHash();
	//            EthGetTransactionReceipt send = web3.ethGetTransactionReceipt(hash).send();
	//            System.out.println(send);
	//        }
		}
}
