package top.misec.Task;

import org.junit.Test;
import top.misec.Login.Verify;
import top.misec.Utils.AvBvConvert;

/**
 * @author Junzhou Liu
 * @create 2020/10/12 20:24
 */
public class DailyTaskTest {

    @Test
    public static void main(String[] args) {
        Verify.verifyInit(args[0], args[1], args[2]);
        System.out.println(AvBvConvert.bv2av("BV1eZ4y1p7GZ"));
    }

}
