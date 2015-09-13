package wb.com.yoyo.Manager;

import android.content.Context;

import java.util.List;

import wb.com.yoyo.DB.DBManager;
import wb.com.yoyo.Model.IMMessage;

/**
 * Created by wb on 15/9/12.
 */
public class MessageManager {

    private static MessageManager messageManager = null;
    private static DBManager dbmanager = null;

    private MessageManager(Context context) {
        SPManager spManager=new SPManager(context);
        String databaseName = spManager.getLoginConfig().getUsername();
        dbmanager = DBManager.getInstance(context, databaseName);
    }

    public static MessageManager getInstance(Context context) {

        if (messageManager == null) {
            messageManager = new MessageManager(context);
        }
        return messageManager;
    }

    public void saveIMMessage(IMMessage newMessage) {

        dbmanager.savaIMMessage(newMessage);
    }

    public List<IMMessage> getMessageListByFrom(String from, int pageNum, int pageSize) {
        return dbmanager.getMessageListByFrom(from,pageNum,pageSize);
    }
}
