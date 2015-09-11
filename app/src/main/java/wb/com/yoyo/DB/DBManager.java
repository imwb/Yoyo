package wb.com.yoyo.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import wb.com.yoyo.Model.User;

/**
 * Created by wb on 15/9/11.
 */
public class DBManager {
    private static  DBManager manager;
    private Context context;
    private SQLiteTemplate st;
    private String dbname;
    private SQLiteManager sqLiteManager;
    private static final String CONTABLENAME="im_contacts";
    private DBManager(Context context,String dbname){
        this.context=context;
        this.dbname=dbname;
        sqLiteManager=SQLiteManager.getInstance(context,dbname);
        st=SQLiteTemplate.getInstance(sqLiteManager,false);
    }

    public  static  DBManager getInstance(Context context,String dbname){
        if(manager!=null)
            return manager;
        else
            return new DBManager(context,dbname);
    }

    public void saveContacts(List<User> users){

        for(User user:users){
            ContentValues cv=new ContentValues();

            cv.put("name",user.getName());
            cv.put("jid",user.getJID());
            cv.put("nickname",user.getNickname());
            cv.put("avatar_path",user.getAvatar_path());

            st.insert(CONTABLENAME,cv);
        }
    }
    public  List<User> getAllContacts(){
        List<User> users= st.queryForList(
                new SQLiteTemplate.RowMapper<User>() {

            @Override
            public User mapRow(Cursor cursor, int index) {
                User user=new User();

                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setJID(cursor.getString(cursor.getColumnIndex("jid")));
                user.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
                user.setAvatar_path(cursor.getString(cursor.getColumnIndex("avatar_path")));

                return user;
            }
        },
                "select * from "+CONTABLENAME);
        System.out.println(users);
        return users;
    }

    public void deleteAllContacts() {
        st.deleteAll(CONTABLENAME);
    }
}
