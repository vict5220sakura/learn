import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月19日  下午5:06:42
 * @version   V 1.0
 */

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月19日 下午5:06:42
 * @version  V 1.0
 */
public class test {
	public static void main(String[] args) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1537319670181L));
		System.out.println(date);
	}
}
