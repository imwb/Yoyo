package wb.com.yoyo.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import wb.com.yoyo.Model.LoginConfig;

/**
 * Created by wb on 15/9/10.
 */
public class ActivitySupport extends FragmentActivity implements IActivitySupport {

    protected YoyoAppliacation appliacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public YoyoAppliacation getMyApplication() {
        return null;
    }

    @Override
    public void stopService() {

    }

    @Override
    public boolean validateInternet() {
        return false;
    }

    @Override
    public boolean hasInternetConnection() {
        return false;
    }

    @Override
    public void isExit() {

    }

    @Override
    public boolean hasLocationGPS() {
        return false;
    }

    @Override
    public void checkMemoryCard() {

    }

    @Override
    public void showToast(String text, int longint) {

    }

    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }

    @Override
    public LoginConfig getLoginConfig() {
        return null;
    }
}
