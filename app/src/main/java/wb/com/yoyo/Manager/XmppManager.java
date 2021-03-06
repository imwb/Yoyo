package wb.com.yoyo.Manager;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import java.util.Collection;

import wb.com.yoyo.Model.LoginConfig;
import wb.com.yoyo.comm.Constant;

/**
 * xmpp 管理类
 * Created by wb on 15/9/10.
 */
public class XmppManager {

    private static XMPPConnection connection;
    private static ConnectionConfiguration connectionConfig;
    private static XmppManager xmppManager;
    protected static VCard myvCard=new VCard();
    private XmppManager() {

    }
    public static XmppManager getInstance() {
        if (xmppManager == null) {
            xmppManager = new XmppManager();
        }
        return xmppManager;
    }

    public static VCard getMyvCard() {

        return myvCard;
    }

    // init
    public static XMPPConnection init(LoginConfig loginConfig) {
        Connection.DEBUG_ENABLED = false;
        ProviderManager pm = ProviderManager.getInstance();
        configure(pm);
        connectionConfig = new ConnectionConfiguration(
                loginConfig.getXmppHost(), loginConfig.getXmppPort(),
                loginConfig.getXmppServiceName());
        connectionConfig.setSASLAuthenticationEnabled(false);// 不使用SASL验证，设置为false
        connectionConfig
                .setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        // 允许自动连接
        connectionConfig.setReconnectionAllowed(false);
        // 允许登陆成功后更新在线状态
        connectionConfig.setSendPresence(true);
        // 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        connection = new XMPPConnection(connectionConfig);

        return connection;
    }

    /**
     *
     * 返回一个有效的xmpp连接,如果无效则返回空.
     *
     * @return
     *
     */
    public static XMPPConnection getConnection() {
        if (connection == null) {
            throw new RuntimeException("请先初始化XMPPConnection连接");
        }
        return connection;
    }

    /**
     *
     * 销毁xmpp连接.
     *
     *
     */
    public static void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public static void configure(ProviderManager pm) {

        // Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private",
                new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time",
                    Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
        }

        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
                new XHTMLExtensionProvider());

        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster",
                new RosterExchangeProvider());
        // Message Events

        // Chat State
        pm.addExtensionProvider("active",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());

        // FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si",
                new StreamInitiationProvider());

        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference",
                new GroupChatInvitation.Provider());
        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
                new DiscoverItemsProvider());
        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
                new DiscoverInfoProvider());
        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
                new MUCUserProvider());
        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
                new MUCAdminProvider());
        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
                new MUCOwnerProvider());
        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay",
                new DelayInformationProvider());
        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version",
                    Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
        }
        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
                new OfflineMessageRequest.Provider());
        // Offline Message Indicator
        pm.addExtensionProvider("offline",
                "http://jabber.org/protocol/offline",
                new OfflineMessageInfo.Provider());
        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup",
                "http://www.jivesoftware.org/protocol/sharedgroup",
                new SharedGroupsInfo.Provider());
        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses",
                "http://jabber.org/protocol/address",
                new MultipleAddressesProvider());

    }

    public static int login(LoginConfig loginConfig){

        String username=loginConfig.getUsername();
        String password=loginConfig.getPassword();
        try {
            //得到连接
            XMPPConnection connection=XmppManager.getConnection();
            connection.connect();
            //登录；
         //   connection.login(username, password, "smack");
            connection.login(username, password);
            //presence 表明用户状态
            connection.sendPacket(new Presence(Presence.Type.available));

            loginConfig.setUsername(username);

            if (loginConfig.isRemember()) {// 保存密码
                loginConfig.setPassword(password);
            } else {
                loginConfig.setPassword("");
            }
            try {
                System.out.println("---loadconnncetion");
                myvCard.load(connection);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            return Constant.LOGIN_SECCESS;
        } catch (XMPPException xee) {
            // TODO Auto-generated catch block
            if (xee instanceof XMPPException) {
                XMPPException xe = (XMPPException) xee;
                final XMPPError error = xe.getXMPPError();
                int errorCode = 0;
                if (error != null) {
                    errorCode = error.getCode();
                }
                if (errorCode == 401) {
                    return Constant.LOGIN_ERROR_ACCOUNT_PASS;
                }else if (errorCode == 403) {
                    return Constant.LOGIN_ERROR_ACCOUNT_PASS;
                } else {
                    return Constant.SERVER_UNAVAILABLE;
                }
            } else {
                return Constant.LOGIN_ERROR;
            }

        }
    }

}
