/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月18日  上午9:38:03
 * @version   V 1.0
 */
package localtest.zbtgiveout;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月18日 上午9:38:03
 * @version  V 1.0
 */
@Slf4j
public class TestGiveOut {
	public static void main(String[] args) {
		BigDecimal all = new BigDecimal("1000000000");
		BigDecimal divisor = new BigDecimal("0.0005");
		BigDecimal giveOutAll = BigDecimal.ZERO;
		int x = 1 ;
		for(int i = 0 ; i < 500 ; i++){
			BigDecimal giveOut = all.multiply(divisor).setScale(6, BigDecimal.ROUND_HALF_UP);
			giveOutAll = giveOutAll.add(giveOut);
			all = all.subtract(giveOut);
			log.info("第{}天,发放{}zbt,剩余{}zbt",x,giveOut.toPlainString(),all.toPlainString());
			x++;
		}
		log.info("共发放 {} zbt,剩余 {} zbt,二者之和为 : {}",giveOutAll.toPlainString(),all.toPlainString(),giveOutAll.add(all).toPlainString());
	}
}
