package wb.com.yoyo.Manager;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.packet.VCard;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import wb.com.yoyo.Model.User;
import wb.com.yoyo.Utils.BitmapUtils;
import wb.com.yoyo.Utils.StringUtil;

/**
 * 用户管理类
 * Created by wb on 15/9/10.
 */
public class UserManager {
    public static UserManager manager;



    /**
     * 保存头像
     * @param jid
     * @return path
     */
    public static String saveAvatar(String jid,Context context){

        File filesDir=context.getFilesDir();
        System.out.println("-----"+jid);
        System.out.println(StringUtil.getUserNameByJid(jid));
        File file=new File(filesDir, StringUtil.getUserNameByJid(jid));
        InputStream is=getUserImageIS(jid);
        BufferedOutputStream bos=null;
        if(is!=null) {
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                byte[] bytes = new byte[128];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                    bos.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null)
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return file.getPath();
        }else {
            return null;
        }
    }
    public static InputStream getUserImageIS(String jid) {
        XMPPConnection connection = XmppManager.getConnection();
        InputStream ic = null;
        try {
            System.out.println("获取用户头像信息: " + jid);
            VCard vcard = new VCard();
            System.out.println(connection);
            vcard.load(connection, jid);
            System.out.println("vcard"+vcard);
            if (vcard == null || vcard.getAvatar() == null) {
                return null;
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    vcard.getAvatar());
            return bais;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ic;
    }


    public static Bitmap getUserAvaterFromSDBypath(String path) {

        Bitmap bitmap=BitmapUtils.getBitmapByOption(path, 50, 50);

        return bitmap;
    }
    public static Bitmap getUserAvaterFromSDByJID(String jid,Context context) {

        ContactsManager contactsManager=new ContactsManager(context);
        User user=contactsManager.getUserFromDB(jid);
        Bitmap bitmap=BitmapUtils.getBitmapByOption(user.getAvatar_path(), 80, 80);

        return bitmap;
    }

    public static Bitmap getMyAvarta( Context context) {
        SPManager spManager=new SPManager(context);
        String path=spManager.getLoginConfig().getAvatarpath();
        return BitmapUtils.getBitmapByOption(path,80,80);
    }
}
