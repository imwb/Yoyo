package wb.com.yoyo.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.util.Calendar;

import wb.com.yoyo.Manager.MessageManager;
import wb.com.yoyo.Manager.NoticeManager;
import wb.com.yoyo.Manager.XmppManager;
import wb.com.yoyo.Model.IMMessage;
import wb.com.yoyo.Model.Notice;
import wb.com.yoyo.Utils.DateUtil;
import wb.com.yoyo.comm.Constant;

/**
 * 聊天服务
 * Created by wb on 15/9/10.
 */
public class IMChatService extends Service{

    private Context context;
    @Override
    public void onCreate() {

        super.onCreate();
        context=this;
        addChatListener();
    }

    private void addChatListener() {


            XMPPConnection conn = XmppManager.getConnection();
            conn.addPacketListener(pListener, new MessageTypeFilter(
                    Message.Type.chat));


    }
    PacketListener pListener = new PacketListener() {

        @Override
        public void processPacket(Packet arg0) {

            Message message = (Message) arg0;
            if (message != null && message.getBody() != null
                    && !message.getBody().equals("null")) {
                IMMessage msg = new IMMessage();
                String time = DateUtil.date2Str(Calendar.getInstance(),
                        Constant.MS_FORMART);
                msg.setTime(time);
                msg.setContent(message.getBody());
                if (Message.Type.error == message.getType()) {
                    msg.setType(IMMessage.ERROR);
                } else {
                    msg.setType(IMMessage.SUCCESS);
                }

                String from = message.getFrom().split("/")[0];
                msg.setFromSubJid(from);

                Notice notice = new Notice();
                notice.setTitle("会话信息");
                notice.setNoticeType(Notice.CHAT_MSG);
                notice.setContent(message.getBody());
                notice.setFrom(from);
                notice.setStatus(Notice.UNREAD);
                notice.setNoticeTime(time);

                NoticeManager noticeManager = NoticeManager
                        .getInstance(context);


                // 历史记录
                IMMessage newMessage = new IMMessage();
                newMessage.setMsgType(0);
                newMessage.setFromSubJid(from);
                newMessage.setContent(message.getBody());
                newMessage.setTime(time);
                MessageManager.getInstance(context).saveIMMessage(newMessage);

                Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);

                intent.putExtra(IMMessage.IMMESSAGE_KEY, msg);

                sendBroadcast(intent);

            }

        }

    };

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
