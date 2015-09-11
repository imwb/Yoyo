package wb.com.yoyo.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 联系人服务
 * Created by wb on 15/9/10.
 */
public class ContactService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
