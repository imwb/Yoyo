package wb.com.yoyo.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import wb.com.yoyo.Manager.SPManager;
import wb.com.yoyo.Model.LoginConfig;
import wb.com.yoyo.R;
import wb.com.yoyo.Service.ContactService;
import wb.com.yoyo.Service.IMChatService;

/**
 * Created by wb on 15/9/10.
 */
public class ActivitySupport extends FragmentActivity implements IActivitySupport {

    protected YoyoApp application;
    protected Context context;
    private ProgressDialog pd;
    private SPManager spManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        application = (YoyoApp) getApplication();
        pd=new ProgressDialog(context);
        spManager=new SPManager(context);

    }

    @Override
    public YoyoApp getMyApplication() {

        return application;
    }

    @Override
    public void stopService() {

        //停止服务
        Intent contact=new Intent(context, ContactService.class);
        Intent chat=new Intent(context, IMChatService.class);
        context.stopService(contact);
        context.stopService(contact);

    }

    @Override
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
        return false;


    }
    public void openWirelessSet() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder
                .setTitle(R.string.prompt)
                .setMessage(context.getString(R.string.check_connection))
                .setPositiveButton(R.string.menu_settings,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                Intent intent = new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS);
                                context.startActivity(intent);
                            }
                        })
                .setNegativeButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.cancel();
                            }
                        });
        dialogBuilder.show();
    }


    @Override
    public boolean hasInternetConnection() {

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void isExit() {

        AlertDialog builder = new AlertDialog.Builder(context)
                .setTitle("确定退出么？")
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopService();
                        finish();
                        application.exit();
                    }
                })
                .show();

    }

    @Override
    public boolean hasLocationGPS() {
        LocationManager manager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void checkMemoryCard() {

        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage("请检查内存卡")
                    .setPositiveButton(R.string.menu_settings,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent(
                                            Settings.ACTION_SETTINGS);
                                    context.startActivity(intent);
                                }
                            })
                    .setNegativeButton("退出",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    application.exit();
                                }
                            }).create().show();
        }
    }

    @Override
    public void showToast(String text, int longint) {

        Toast.makeText(context,text,longint).show();
    }

    @Override
    public void showToast(String text) {

        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return pd;
    }

    @Override
    public LoginConfig getLoginConfig() {
        return spManager.getLoginConfig();
    }
    /**
     *
     * 关闭键盘事件
     *

     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
