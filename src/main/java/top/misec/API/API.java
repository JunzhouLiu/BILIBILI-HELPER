package top.misec.API;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 3:40
 */
public class API {
    public static String LOGIN = "https://api.bilibili.com/x/web-interface/nav";
    public static String Manga = "https://manga.bilibili.com/twirp/activity.v1.Activity/ClockIn";
    public static String AvShare = "https://api.bilibili.com/x/web-interface/share/add";
    public static String CoinAdd = "https://api.bilibili.com/x/web-interface/coin/add";
    public static String isCoin = "https://api.bilibili.com/x/web-interface/archive/coins";
    public static String getRegionRanking = "http://api.bilibili.com/x/web-interface/ranking/region";
    public static String reward = "http://api.bilibili.com/x/member/web/exp/reward";
    //还需要投几个币
    public static String needCoin = "https://www.bilibili.com/plus/account/exp.php";
    //硬币换银瓜子
    public static String silver2coin = "https://api.live.bilibili.com/pay/v1/Exchange/silver2coin";
    //状态
    public static String getSilver2coinStatus = "https://api.live.bilibili.com/pay/v1/Exchange/getStatus";

    public static String videoHeartbeat = "http://api.bilibili.com/x/click-interface/web/heartbeat";

    public static String getCoinBalance = "https://account.bilibili.com/site/getCoin";//查询主站硬币数量

    public static String autoCharge = "http://api.bilibili.com/x/ugcpay/trade/elec/pay/quick";

    public static String chargeComment = "http://api.bilibili.com/x/ugcpay/trade/elec/message";
}
