package top.misec.utils;

import java.time.LocalDate;
import java.time.ZoneId;

public final class DateUtil {


    private static final ZoneId DEFAULT_ZONE = ZoneId.of("GMT+8");

    // 获取当日为本月的几号
    public static int getDayOfMonth() {
        return LocalDate.now(DEFAULT_ZONE).getDayOfMonth();
    }

    private DateUtil() {
    }
}
