package top.misec.utils;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 20:49
 */
public class HelpUtil {
    private static final String TABLE = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";
    private static final HashMap<String, Integer> MP = new HashMap<>();
    private static final HashMap<Integer, String> MP2 = new HashMap<>();
    static int[] ss = {11, 10, 3, 8, 4, 6, 2, 9, 5, 7};
    static long xor = 177451812;
    static long add = 8728348608L;

    public static void main(String[] args) {

    }

    public static long power(int a, int b) {
        long power = 1;
        for (int c = 0; c < b; c++) {
            power *= a;
        }

        return power;
    }

    public static String bv2av(String s) {
        long r = 0;
        for (int i = 0; i < 58; i++) {
            String s1 = TABLE.substring(i, i + 1);
            MP.put(s1, i);
        }
        for (int i = 0; i < 6; i++) {
            r = r + MP.get(s.substring(ss[i], ss[i] + 1)) * power(58, i);
        }
        return ((r - add) ^ xor) + "";
    }

    public static String av2bv(String st) {
        long s = Long.parseLong(st.split("av")[1]);
        StringBuilder sb = new StringBuilder("BV1  4 1 7  ");
        s = (s ^ xor) + add;
        for (int i = 0; i < 58; i++) {
            String s1 = TABLE.substring(i, i + 1);
            MP2.put(i, s1);
        }
        for (int i = 0; i < 6; i++) {
            String r = MP2.get((int) (s / power(58, i) % 58));
            sb.replace(ss[i], ss[i] + 1, r);
        }
        return sb.toString();
    }

    public static String userNameEncode(String userName) {
        int s1 = userName.length() / 2, s2 = (s1 + 1) / 2;
        return userName.substring(0, s2) + String.join("", Collections.nCopies(s1, "*")) +
                userName.substring(s1 + s2);
    }

}
