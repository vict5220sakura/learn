package localtest;

import java.util.Arrays;

import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;

import com.alibaba.fastjson.JSONObject;

public class SolidityABITest {

	@Test
	public void solidityABI() {
		String ABI = "{\"transferOwnership\": {\"name\": \"transferOwnership\",\"params\": [\"Address\"],\"returnType\": null}}";
		JSONObject jsonObject = JSONObject.parseObject(ABI);
		abiApi("", "",Bool.class);
	}
	
	public <T> T  abiApi(String abi,String functionName,Class<T> t){
		
		return null;
	}
}
