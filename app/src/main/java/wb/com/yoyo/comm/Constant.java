package wb.com.yoyo.comm;

/**
 * Created by wb on 15/9/10.
 */
public class Constant {
    /**
     * 服务器的配置
     */
    public static final String LOGIN_SET = "login_set";// 登录设置
    public static final String USERNAME = "username";// 账户
    public static final String PASSWORD = "password";// 密码
    public static final String XMPP_HOST = "xmpp_host";// 地址
    public static final String XMPP_PORT = "xmpp_port";// 端口
    public static final String XMPP_SEIVICE_NAME = "xmpp_service_name";// 服务名
    public static final String IS_AUTOLOGIN = "isAutoLogin";// 是否自动登录
    public static final String IS_REMEMBER = "isRemember";// 是否记住账户密码
    public static final String IS_FIRSTSTART = "isFirstStart";// 是否首次启动
    public static final String AVATAR_PATH = "avatar_path";//头像路径

    /**
     * 登录提示
     */
    public static final int LOGIN_SECCESS = 0;// 成功
    public static final int HAS_NEW_VERSION = 1;// 发现新版本
    public static final int IS_NEW_VERSION = 2;// 当前版本为最新
    public static final int LOGIN_ERROR_ACCOUNT_PASS = 3;// 账号或者密码错误
    public static final int SERVER_UNAVAILABLE = 4;// 无法连接到服务器
    public static final int LOGIN_ERROR = 5;// 连接失败
    public static final String XMPP_CONNECTION_CLOSED = "xmpp_connection_closed";// 连接中断
    public static final String LOGIN = "login"; // 登录
    public static final String RELOGIN = "relogin"; // 重新登录

    /**
     * 精确到毫秒
     */
//	public static final String MS_FORMART = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String MS_FORMART = "MM-dd HH:mm:ss";

    /**
     * 文件类型
     */
    public static final String  FILETYPE_IMAGE="image";
    public static final String  FILETYPE_VOICE="voice";

    /**
     * 收到好友邀请请求
     */
    public static final String ROSTER_SUBSCRIPTION = "roster.subscribe";
    public static final String ROSTER_SUB_FROM = "roster.subscribe.from";
    public static final String NOTICE_ID = "notice.id";

    public static final String NEW_MESSAGE_ACTION = "roster.newmessage";
}
