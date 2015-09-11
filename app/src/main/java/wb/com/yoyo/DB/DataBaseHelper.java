package wb.com.yoyo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jivesoftware.smack.packet.RosterPacket;

/**
 * Created by wb on 15/9/11.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name,  int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE [im_msg_his] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR, [msg_from] NVARCHAR, [msg_to] NVARCHAR, [msg_time] TEXT, [msg_type] INTEGER);");
        db.execSQL("CREATE TABLE [im_notice]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [type] INTEGER, [title] NVARCHAR, [content] NVARCHAR, [notice_from] NVARCHAR, [notice_to] NVARCHAR, [notice_time] TEXT, [status] INTEGER);");
        db.execSQL("CREATE TABLE [im_contacts] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "[name] NVARCHAR,[jid] nvarchar,[nickname] nvarchar,[avatar_path] path)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
