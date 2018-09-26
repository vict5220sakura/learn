package localtest;

import java.util.Arrays;

import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;


public class OtherApiEthTest {
	
	@Test
	public void testEthCall(){
		String address = "0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b";
		Function function = new Function(//
				"frozenAccount"//
				, Arrays.asList(new Address(address))//
				, Arrays.asList( new TypeReference<Bool>() {} )//
		);
		System.out.println(FunctionEncoder.encode(function));
		
	}
	
	@Test
	public void showAdmin(){
		Function function = new Function(//
				"owner"//
				, Arrays.asList()//
				, Arrays.asList( new TypeReference<Address>() {} )//
		);
		System.out.println(FunctionEncoder.encode(function));
	}
}
