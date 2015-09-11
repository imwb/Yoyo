package wb.com.yoyo.Manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import wb.com.yoyo.Model.LoginConfig;
import wb.com.yoyo.R;
import wb.com.yoyo.comm.Constant;

/**
 * sharedPerfrences 管理类
 * Created by wb on 15/9/10.
 */
public class SPManager {

    private SharedPreferences sp;
    private Context context;

    public SPManager(Context context) {

        this.context=context;
        sp=context.getSharedPreferences(Constant.LOGIN_SET, Activity.MODE_PRIVATE);
    }

    public LoginConfig getLoginConfig(){


        Resources res = context.getResources();
        LoginConfig config=new LoginConfig();

        //getString(String name,String defoult)
        String username = sp.getString(Constant.USERNAME, null);
        String password = sp.getString(Constant.PASSWORD,null);
        String xmpp_host = sp.getString(Constant.XMPP_HOST, res.getString(R.string.xmpp_host));
        int xmpp_port = sp.getInt(Constant.XMPP_PORT, res.getInteger(R.integer.xmpp_port));
        boolean isRemember = sp.getBoolean(Constant.IS_REMEMBER, res.getBoolean(R.bool.is_remember));
        boolean isAutoLogin = sp.getBoolean(Constant.IS_AUTOLOGIN,res.getBoolean(R.bool.is_autologin));
        boolean isFirstStart = sp.getBoolean(Constant.IS_FIRSTSTART,true);
        String avatarpath = sp.getString(Constant.AVATAR_PATH, null);

        config.setUsername(username);
        config.setPassword(password);
        config.setXmppHost(xmpp_host);
        config.setXmppPort(xmpp_port);
        config.setIsRemember(isRemember);
        config.setIsAutoLogin(isAutoLogin);
        config.setIsFirstStart(isFirstStart);
        config.setAvatarpath(avatarpath);

        return config;
    }

    public void saveLoginConfig(LoginConfig loginConfig) {

        sp.edit().putString(Constant.XMPP_HOST, loginConfig.getXmppHost()).commit();
        sp.edit().putInt(Constant.XMPP_PORT, loginConfig.getXmppPort()).commit();
        sp.edit().putString(Constant.USERNAME,loginConfig.getUsername()).commit();
        sp.edit().putString(Constant.PASSWORD,loginConfig.getPassword()).commit();
        sp.edit().putBoolean(Constant.IS_AUTOLOGIN,loginConfig.isAutoLogin()).commit();
        sp.edit().putBoolean(Constant.IS_FIRSTSTART,loginConfig.isFirstStart()).commit();
        sp.edit().putBoolean(Constant.IS_REMEMBER,loginConfig.isRemember()).commit();
        sp.edit().putString(Constant.AVATAR_PATH,loginConfig.getAvatarpath()).commit();

    }
}
