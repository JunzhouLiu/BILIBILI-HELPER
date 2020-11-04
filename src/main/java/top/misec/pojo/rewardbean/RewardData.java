package top.misec.pojo.rewardbean;

public class RewardData {

    private boolean login;
    private boolean watch;
    private int coins;
    private boolean share;
    private boolean email;
    private boolean tel;
    private boolean safe_question;
    private boolean identify_card;

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean getLogin() {
        return login;
    }

    public void setWatch(boolean watch) {
        this.watch = watch;
    }

    public boolean getWatch() {
        return watch;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean getShare() {
        return share;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean getEmail() {
        return email;
    }

    public void setTel(boolean tel) {
        this.tel = tel;
    }

    public boolean getTel() {
        return tel;
    }

    public void setSafe_question(boolean safe_question) {
        this.safe_question = safe_question;
    }

    public boolean getSafe_question() {
        return safe_question;
    }

    public void setIdentify_card(boolean identify_card) {
        this.identify_card = identify_card;
    }

    public boolean getIdentify_card() {
        return identify_card;
    }

}