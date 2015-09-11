package wb.com.yoyo.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wb on 15/9/11.
 */
public class SQLiteManager {
    private int version = 1;
    private String databaseName;

    // 本地Context对象
    private Context mContext = null;

    private static SQLiteManager manager = null;

    /**
     * 构造函数
     *
     * @param mContext
     */
    private SQLiteManager(Context mContext) {
        super();
        this.mContext = mContext;

    }

    public static SQLiteManager getInstance(Context mContext, String databaseName) {
        if (null == manager) {
            manager = new SQLiteManager(mContext);
        }
        manager.databaseName = databaseName;
        return manager;
    }

    /**
     * 关闭数据库 注意:当事务成功或者一次性操作完毕时候再关闭
     */
    public void closeDatabase(SQLiteDatabase dataBase, Cursor cursor) {
        if (null != dataBase) {
            dataBase.close();
        }
        if (null != cursor) {
            cursor.close();
        }
    }

    /**
     * 打开数据库 注:SQLiteDatabase资源一旦被关闭,该底层会重新产生一个新的SQLiteDatabase
     */
    public SQLiteDatabase openDatabase() {
        return getDatabaseHelper().getWritableDatabase();
    }

    /**
     * 获取DataBaseHelper
     *
     * @return
     */
    public DataBaseHelper getDatabaseHelper() {
        return new DataBaseHelper(mContext, this.databaseName,this.version);
    }
}
