package wb.com.yoyo.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import wb.com.yoyo.Model.ChartHisBean;
import wb.com.yoyo.Model.IMMessage;
import wb.com.yoyo.Model.Notice;
import wb.com.yoyo.Model.User;
import wb.com.yoyo.Utils.StringUtil;

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

    public long savaIMMessage(IMMessage msg) {

        ContentValues contentValues = new ContentValues();
        if (StringUtil.notEmpty(msg.getContent())) {
            contentValues.put("content", StringUtil.doEmpty(msg.getContent()));
        }
        if (StringUtil.notEmpty(msg.getFromSubJid())) {
            contentValues.put("msg_from",
                    StringUtil.doEmpty(msg.getFromSubJid()));
        }
        contentValues.put("msg_type", msg.getMsgType());
        contentValues.put("msg_time", msg.getTime());

        return st.insert("im_msg_his", contentValues);


    }

    public List<IMMessage> getMessageListByFrom(String fromUser, int pageNum, int pageSize) {
        if (StringUtil.empty(fromUser)) {
            return null;
        }

        int fromIndex = (pageNum - 1) * pageSize;

        List<IMMessage> list = st.queryForList(
                new SQLiteTemplate.RowMapper<IMMessage>() {
                    @Override
                    public IMMessage mapRow(Cursor cursor, int index) {
                        IMMessage msg = new IMMessage();
                        msg.setContent(cursor.getString(cursor
                                .getColumnIndex("content")));
                        msg.setFromSubJid(cursor.getString(cursor
                                .getColumnIndex("msg_from")));
                        msg.setMsgType(cursor.getInt(cursor
                                .getColumnIndex("msg_type")));
                        msg.setTime(cursor.getString(cursor
                                .getColumnIndex("msg_time")));
                        return msg;
                    }
                },
                "select content,msg_from, msg_type,msg_time from im_msg_his where msg_from=? order by msg_time desc limit ? , ? ",
                new String[] { "" + fromUser, "" + fromIndex, "" + pageSize });
        return list;
    }

    public User getUserByJid(String jid) {
        return  st.queryForObject(
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
        }
                , "select * from im_contacts where jid = ?"
                ,
                new String[]{jid});
    }

    public List<ChartHisBean> getRecentContactsWithLadtMsg() {

        List<ChartHisBean> list = st
                .queryForList(
                        new SQLiteTemplate.RowMapper<ChartHisBean>() {

                            @Override
                            public ChartHisBean mapRow(Cursor cursor, int index) {
                                ChartHisBean notice = new ChartHisBean();
                                notice.setId(cursor.getString(cursor
                                        .getColumnIndex("_id")));
                                notice.setContent(cursor.getString(cursor
                                        .getColumnIndex("content")));
                                notice.setFrom(cursor.getString(cursor
                                        .getColumnIndex("msg_from")));
                                notice.setNoticeTime(cursor.getString(cursor
                                        .getColumnIndex("msg_time")));
                                notice.setMegtype(cursor.getInt(cursor.
                                        getColumnIndex("msg_type")));
                                return notice;
                            }
                        },
                        "select m.[_id],m.[content],m.[msg_time],m.msg_from,m.msg_type from im_msg_his  m join (select msg_from,max(msg_time) as time from im_msg_his group by msg_from) as tem  on  tem.time=m.msg_time and tem.msg_from=m.msg_from ",
                        null);
        for (ChartHisBean b : list) {
            int count = st
                    .getCount(
                            "select _id from im_notice where status=? and type=? and notice_from=?",
                            new String[] { "" + Notice.UNREAD,
                                    "" + Notice.CHAT_MSG, b.getFrom() });
            b.setNoticeSum(count);
        }
        return list;

    }
}
