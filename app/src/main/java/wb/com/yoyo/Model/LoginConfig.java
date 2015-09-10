package wb.com.yoyo.Model;

/**
 * 登录信息
 * Created by wb on 15/9/10.
 */
public class LoginConfig {

    private String xmppHost;// 地址
    private Integer xmppPort;// 端口
    private String xmppServiceName;// 服务器名称
    private String username;// 用户名
    private String password;// 密码
    private boolean isRemember;// 是否记住密码
    private boolean isAutoLogin;// 是否自动登录
    private boolean isOnline;// 用户连接成功connection
    private boolean isFirstStart;// 是否首次启动
    private String avatarpath;//头像

    public String getXmppHost() {
        return xmppHost;
    }

    public void setXmppHost(String xmppHost) {
        this.xmppHost = xmppHost;
    }

    public String getAvatarpath() {
        return avatarpath;
    }

    public void setAvatarpath(String avatarpath) {
        this.avatarpath = avatarpath;
    }

    public Integer getXmppPort() {
        return xmppPort;
    }

    public void setXmppPort(Integer xmppPort) {
        this.xmppPort = xmppPort;
    }

    public String getXmppServiceName() {
        return xmppServiceName;
    }

    public void setXmppServiceName(String xmppServiceName) {
        this.xmppServiceName = xmppServiceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public void setIsRemember(boolean isRemember) {
        this.isRemember = isRemember;
    }

    public boolean isAutoLogin() {
        return isAutoLogin;
    }

    public void setIsAutoLogin(boolean isAutoLogin) {
        this.isAutoLogin = isAutoLogin;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isFirstStart() {
        return isFirstStart;
    }

    public void setIsFirstStart(boolean isFirstStart) {
        this.isFirstStart = isFirstStart;
    }

    @Override
    public String toString() {
        return "LoginConfig{" +
                "xmppHost='" + xmppHost + '\'' +
                ", xmppPort=" + xmppPort +
                ", xmppServiceName='" + xmppServiceName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isRemember=" + isRemember +
                ", isAutoLogin=" + isAutoLogin +
                ", isOnline=" + isOnline +
                ", isFirstStart=" + isFirstStart +
                ", avatarpath='" + avatarpath + '\'' +
                '}';
    }
}
