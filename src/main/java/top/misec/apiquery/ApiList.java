package top.misec.apiquery;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 3:40
 */
public interface ApiList {

    String ServerPush = "https://sc.ftqq.com/";
    String LOGIN = "https://api.bilibili.com/x/web-interface/nav";
    String Manga = "https://manga.bilibili.com/twirp/activity.v1.Activity/ClockIn";
    String AvShare = "https://api.bilibili.com/x/web-interface/share/add";
    String CoinAdd = "https://api.bilibili.com/x/web-interface/coin/add";
    String isCoin = "https://api.bilibili.com/x/web-interface/archive/coins";
    String getRegionRanking = "https://api.bilibili.com/x/web-interface/ranking/region";
    String reward = "https://api.bilibili.com/x/member/web/exp/reward";

    /**
     * 查询获取已获取的投币经验
     */
    String needCoin = "https://www.bilibili.com/plus/account/exp.php";

    String needCoinNew = "https://api.bilibili.com/x/web-interface/coin/today/exp";

    /**
     * 硬币换银瓜子
     */
    String silver2coin = "https://api.live.bilibili.com/pay/v1/Exchange/silver2coin";

    /**
     * 查询银瓜子兑换状态
     */
    String getSilver2coinStatus = "https://api.live.bilibili.com/pay/v1/Exchange/getStatus";

    /**
     * 上报观看进度
     */
    String videoHeartbeat = "https://api.bilibili.com/x/click-interface/web/heartbeat";

    /**
     * 查询主站硬币余额
     */
    String getCoinBalance = "https://account.bilibili.com/site/getCoin";

    /**
     * 充电请求
     */
    String autoCharge = "https://api.bilibili.com/x/ugcpay/web/v2/trade/elec/pay/quick";

    /**
     * 充电留言
     */
    String chargeComment = "https://api.bilibili.com/x/ugcpay/trade/elec/message";


    String chargeQuery = "https://api.bilibili.com/x/ugcpay/web/v2/trade/elec/panel";

    String queryUserName = "https://api.bilibili.com/x/space/acc/info";

    /**
     * 领取大会员福利
     */
    String vipPrivilegeReceive = "https://api.bilibili.com/x/vip/privilege/receive";

    /**
     * 领取大会员漫画福利
     */
    String mangaGetVipReward = "https://manga.bilibili.com/twirp/user.v1.User/GetVipReward";
    /**
     * 直播签到
     */
    String liveCheckin = "https://api.live.bilibili.com/xlive/web-ucenter/v1/sign/DoSign";

    String queryDynamicNew = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new";

    String videoView = "https://api.bilibili.com/x/web-interface/view";

    /**
     *
     */
    String mangaRead = "https://manga.bilibili.com/twirp/bookshelf.v1.Bookshelf/AddHistory";
}
