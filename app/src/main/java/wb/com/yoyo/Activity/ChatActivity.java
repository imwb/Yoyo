package wb.com.yoyo.Activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import wb.com.yoyo.Manager.ContactsManager;
import wb.com.yoyo.Manager.MessageManager;
import wb.com.yoyo.Manager.UserManager;
import wb.com.yoyo.Manager.XmppManager;
import wb.com.yoyo.Model.IMMessage;
import wb.com.yoyo.Model.SoundMeter;
import wb.com.yoyo.Model.User;
import wb.com.yoyo.R;
import wb.com.yoyo.Utils.BitmapUtils;
import wb.com.yoyo.Utils.DateUtil;
import wb.com.yoyo.Utils.StringUtil;
import wb.com.yoyo.comm.Constant;

/**
 * Created by wb on 15/9/12.
 */
public class ChatActivity extends ActivitySupport {

    private Chat chat = null;
    private List<IMMessage> message_pool = null;
    private String to;// 聊天人
    private User user;
    private String fileto;
    private static int pageSize = 10;

    private MessageListAdapter adapter = null;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private ImageView titleBack;
    private TextView tvChatTitle;

    private EditText messageInput = null;
    private Button messageSendBtn = null;
    private ListView listView;
    private ImageButton picsendButton;
    private boolean btn_vocie = false;

    private SoundMeter sound;
    //发送文字布局
    private RelativeLayout mBottom;
    private ImageView chatting_mode_btn;
    private String voiceName;
    private long startVoiceT, endVoiceT;
    private TextView mBtnRcd;

    private int flag = 1;
    private static final int POLL_INTERVAL = 300;


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.NEW_MESSAGE_ACTION.equals(action)) {
                IMMessage message = intent
                        .getParcelableExtra(IMMessage.IMMESSAGE_KEY);
                message_pool.add(message);
                receiveNewMessage(message);
                refreshMessage(message_pool);
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        to=getIntent().getStringExtra("to");

        initChat(to);
        initModelView();
        initRec();
        initPic();

    }

    @Override
    protected void onResume() {
        super.onResume();
        message_pool = MessageManager.getInstance(context)
                .getMessageListByFrom(to, 1, pageSize);
        if (null != message_pool && message_pool.size() > 0)
            Collections.sort(message_pool);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.NEW_MESSAGE_ACTION);
        registerReceiver(receiver, filter);
        // 跟心某人所有通知
      // NoticeManager.getInstance(context).updateStatusByFrom(to, Notice.READ);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
     //   NoticeManager.getInstance(context).updateStatusByFrom(to, Notice.READ);
    }

    public void initChat(String to){
        chat = XmppManager.getConnection().getChatManager().createChat(to, new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {

            }
        });

        ContactsManager contactsManager=new ContactsManager(context);
        user=contactsManager.getUserFromDB(to);

        message_pool = MessageManager.getInstance(context)
                .getMessageListByFrom(to, 1, pageSize);

        listView = (ListView) findViewById(R.id.chat_list);
        listView.setCacheColorHint(0);
        adapter=new MessageListAdapter(this,message_pool,listView);
        listView.setAdapter(adapter);

        messageInput = (EditText) findViewById(R.id.chat_content);
        messageSendBtn = (Button) findViewById(R.id.chat_sendbtn);
        messageSendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                if ("".equals(message)) {
                    showToast("不能为空");
                } else {

                    sendMessage(message);
                    messageInput.setText(" ");
                    closeInput();
                }
            }
        });

        titleBack = (ImageView) findViewById(R.id.title_back);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvChatTitle = (TextView) findViewById(R.id.to_chat_name);
        tvChatTitle.setText(user.getName());
    }



    private void initModelView() {
        chatting_mode_btn = (ImageView) this.findViewById(R.id.ivPopUp);
        mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);
        mBtnRcd = (TextView) findViewById(R.id.btn_rcd);
        //模式切换 语音 文本
        chatting_mode_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (btn_vocie) {
                    mBtnRcd.setVisibility(View.GONE);
                    mBottom.setVisibility(View.VISIBLE);
                    btn_vocie = false;
                    chatting_mode_btn
                            .setImageResource(R.drawable.chatting_setmode_msg_btn);

                } else {
                    mBtnRcd.setVisibility(View.VISIBLE);
                    mBottom.setVisibility(View.GONE);
                    chatting_mode_btn
                            .setImageResource(R.drawable.chatting_setmode_voice_btn);
                    btn_vocie = true;
                }
            }
        });
    }
    private void initRec() {
        sound=new SoundMeter();
        //按住录音
        mBtnRcd.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Long time=null;
                if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                    System.out.println("down");
                    mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
                    startVoiceT = SystemClock.currentThreadTimeMillis();
                    time = System.currentTimeMillis();
                    voiceName = time + ".amr";
                    mBtnRcd.setText("请录音！");
                    sound.start(voiceName);
                    flag = 2;
                } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {
                    mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
                    mBtnRcd.setText("按住说话！");
                    sound.stop();
                    endVoiceT = SystemClock.currentThreadTimeMillis();
                    System.out.println("time" + (endVoiceT - startVoiceT));
                    String name = time + ".amr";
                    sendfile(name, "voice");
                    flag = 1;
                }
                return true;
            }
        });
    }

    private void initPic() {
        picsendButton=(ImageButton) findViewById(R.id.imgBut_addpic);
        picsendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //进入相册
                gotoImages();
            }

        });

    }
    private void gotoImages() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        startActivityForResult(intent, 1);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode==1&&resultCode==-1){
            Uri uri= data.getData();
            String path=getFilePathByUri(uri);
            sendfile(path,"image");
        }
    }
    public String getFilePathByUri(Uri uri) {

        ContentResolver contentResolver= getContentResolver();
        Cursor cursor=contentResolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex("_data"));

        return path;
    }

        protected void sendMessage(String messageContent)  {

            String time = DateUtil.date2Str(Calendar.getInstance(),
                    Constant.MS_FORMART);
            Message message = new Message();
            message.setProperty(IMMessage.KEY_TIME, time);
            message.setBody(messageContent);

            try {
                chat.sendMessage(message);
            } catch (XMPPException e) {
                e.printStackTrace();
            }

            IMMessage newMessage = new IMMessage();
            newMessage.setMsgType(1);
            newMessage.setFromSubJid(chat.getParticipant());
            newMessage.setContent(messageContent);
            newMessage.setTime(time);
            message_pool.add(newMessage);
            MessageManager.getInstance(context).saveIMMessage(newMessage);

            // 刷新视图
            refreshMessage(message_pool);

        }

    private void refreshMessage(List<IMMessage> messages) {

        adapter.refreshList(messages);

    }

    private void receiveNewMessage(IMMessage message) {

    }
    /**
     * 发送文件
     * @param name 文件名
     * @param voice 文件类型
     */
    private void sendfile(String  name, String voice) {

    }

   private class MessageListAdapter extends BaseAdapter {
        private List<IMMessage> items;
        private Context context;
        private ListView adapterList;
        private LayoutInflater inflater;

        public MessageListAdapter(Context context, List<IMMessage> items,
                                  ListView adapterList) {
            this.context = context;
            this.items = items;
            this.adapterList = adapterList;

        }

        public void refreshList(List<IMMessage> items) {
            this.items = items;

            this.notifyDataSetChanged();
            adapterList.setSelection(items.size() - 1);
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final IMMessage message = items.get(position);
            TextView msgView=null;
            ImageView fileView=null;
            if (message.getMsgType() == 0) {
                convertView = this.inflater.inflate(
                        R.layout.formclient_chat_in, null);
            } else if(message.getMsgType() == 1){
                convertView = this.inflater.inflate(
                        R.layout.formclient_chat_out, null);
            }else if(message.getMsgType()==2){
                convertView=this.inflater.inflate(R.layout.formclient_chat_picin, null);
            }else if(message.getMsgType()==3){
                convertView=this.inflater.inflate(R.layout.formclient_chat_picout, null);
            }else if (message.getMsgType()==5) {
                convertView=this.inflater.inflate(R.layout.formclient_chat_voiceout, null);
            }
            if(message.getMsgType()==0||message.getMsgType()==1)
            {	msgView=(TextView) convertView
                    .findViewById(R.id.formclient_row_msg);
                msgView.setText(message.getContent());
            }
            else if(message.getMsgType()==2||message.getMsgType()==3){
                fileView=(ImageView) convertView.findViewById(R.id.formclient_row_picmsg);
                Bitmap bitmap= BitmapUtils.getBitmapByOption(message.getContent(), 200, 200);
                fileView.setImageBitmap(bitmap);
                fileView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent=new Intent(context, ShowPicActivity.class);
                        intent.putExtra("picpath", message.getContent());
                        startActivity(intent);

                    }
                });

            }else if (message.getMsgType()==4||message.getMsgType()==5) {
                final MediaPlayer player=new MediaPlayer();
                final String path=message.getContent();
                final TextView tv_time=(TextView) convertView.findViewById(R.id.tv_time);
                System.out.println(path);
                final ImageView voiceView = (ImageView) convertView.findViewById(R.id.formclient_row_picmsg);
                try {
                    player.setDataSource(path);
                    player.prepare();
                    String timeString=DateUtil.getTimeString(player.getDuration());
                    tv_time.setText(timeString);
                } catch (IllegalArgumentException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IllegalStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                voiceView.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        voiceView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.chatto_voice_playing));
                        player.start();


                    }
                });
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        voiceView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.chatto_voice_playing_f3));

                    }
                });

            }
            TextView useridView = (TextView) convertView
                    .findViewById(R.id.formclient_row_userid);
            TextView dateView = (TextView) convertView
                    .findViewById(R.id.formclient_row_date);
            ImageView avatarView=(ImageView) convertView
                    .findViewById(R.id.from_head);

            if (message.getMsgType() == 0||message.getMsgType() == 2||message.getMsgType() == 4) {
                if (null == user) {
                    useridView.setText(StringUtil.getUserNameByJid(to));
                } else {
                    useridView.setText(user.getName());
                }

            } else {
                useridView.setText("我");
            }
            dateView.setText(message.getTime());

            Bitmap bitmap;
            String dir=BitmapUtils.getImagePath()+"/";

            if(message.getMsgType()==0||message.getMsgType()==2||message.getMsgType()==4){
                bitmap= UserManager.getUserAvaterFromSDByJID(to, context);
            }
            else {
                bitmap=UserManager.getMyAvarta(context);
            }
            avatarView.setImageBitmap(bitmap);
            return convertView;
        }


    }

}

