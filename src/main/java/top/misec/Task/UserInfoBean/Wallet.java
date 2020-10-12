package top.misec.Task.UserInfoBean;

/**
 * Auto-generated
 * @author Junzhou Liu
 * @create 2020/10/11 4:21
 */
public class Wallet {

    private long mid;
    private int bcoin_balance;
    private int coupon_balance;
    private int coupon_due_time;

    public void setMid(long mid) {
        this.mid = mid;
    }

    public long getMid() {
        return mid;
    }

    public void setBcoin_balance(int bcoin_balance) {
        this.bcoin_balance = bcoin_balance;
    }

    public int getBcoin_balance() {
        return bcoin_balance;
    }

    public void setCoupon_balance(int coupon_balance) {
        this.coupon_balance = coupon_balance;
    }

    public int getCoupon_balance() {
        return coupon_balance;
    }

    public void setCoupon_due_time(int coupon_due_time) {
        this.coupon_due_time = coupon_due_time;
    }

    public int getCoupon_due_time() {
        return coupon_due_time;
    }

}