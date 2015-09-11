package wb.com.yoyo.Activity;

import android.app.ProgressDialog;

import wb.com.yoyo.Model.LoginConfig;

/**
 * activitySupport 接口
 * Created by wb on 15/9/10.
 */
public interface IActivitySupport {

    /**
     * 获取 application
     * @return
     */
    public abstract YoyoApp getMyApplication();

    /**
     * 停止服务
     */
    public abstract void stopService();

    /**
     * 验证网络
     */
    public abstract boolean validateInternet();

    /**
     * 验证是否连接网络
     * @return
     */
    public abstract boolean hasInternetConnection();

    /**
     * 退出应用.
     */
    public abstract void isExit();

    /**
     * 判断GPS是否已经开启.
     */
    public abstract boolean hasLocationGPS();

    /**
     * 检查内存卡.
     */
    public abstract void checkMemoryCard();

    /**
     * 显示toast
     * @param text 内容
     * @param longint 时间
     */
    public abstract void showToast(String text, int longint);

    public abstract void showToast(String text);
    /**
     * 获取进度条.
     * @return
     */
    public abstract ProgressDialog getProgressDialog();

    /**
     *
     * 获取用户配置.
     * @param loginConfig
     */
    public LoginConfig getLoginConfig();

}
