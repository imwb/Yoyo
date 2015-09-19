package wb.com.yoyo.Fragment;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import wb.com.yoyo.Activity.ChatActivity;
import wb.com.yoyo.Manager.ContactsManager;
import wb.com.yoyo.Manager.MessageManager;
import wb.com.yoyo.Manager.UserManager;
import wb.com.yoyo.Model.ChartHisBean;
import wb.com.yoyo.Model.IMMessage;
import wb.com.yoyo.Model.Notice;
import wb.com.yoyo.Model.User;
import wb.com.yoyo.R;
import wb.com.yoyo.comm.Constant;


public class RecentlyContacterFragment extends Fragment{

	private Context context;
	private ListView inviteList = null;
	private RecentChartAdapter noticeAdapter = null;
	private List<ChartHisBean> inviteNotices = new ArrayList<ChartHisBean>();
	private View view;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		view=inflater.inflate(R.layout.fragment_rctcontact, null);

		return 	view;
	}

	private void initView(View view) {
		inviteList = (ListView) view.findViewById(R.id.lv_contacters);
		inviteNotices = MessageManager.getInstance(context)
				.getRecentContactsWithLastMsg();
		noticeAdapter = new RecentChartAdapter(context, inviteNotices);
		inviteList.setAdapter(noticeAdapter);
		noticeAdapter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createChat((User) v.findViewById(R.id.new_content).getTag());
			}
		});
	}

	private void createChat(User user) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra("to", user.getJID());
		startActivity(intent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context=getActivity();
		initView(view);
		IntentFilter filter=new IntentFilter(Constant.NEW_MESSAGE_ACTION);
		ContacterReceiver contacterReceiver=new ContacterReceiver();
		getActivity().registerReceiver(contacterReceiver,filter);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshList();
	}
	public void refreshList() {
		// 刷新notice信息
		inviteNotices = MessageManager.getInstance(context)
				.getRecentContactsWithLastMsg();
		noticeAdapter.setNoticeList(inviteNotices);
		noticeAdapter.notifyDataSetChanged();
	}
	private  class ContacterReceiver extends BroadcastReceiver {


		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("onrecevie..");
			// TODO Auto-generated method stub
			String action=intent.getAction();
			User user = intent.getParcelableExtra(User.userKey);
			Notice notice = (Notice) intent.getSerializableExtra("notice");
			IMMessage imMessage= (IMMessage) intent.getParcelableExtra(IMMessage.IMMESSAGE_KEY);
			if(action.equals(Constant.NEW_MESSAGE_ACTION)){
				System.out.println("msgrecevie...");
				msgReceive(imMessage);
			}
		}

	}
	public void msgReceive(IMMessage imMessage) {
		// TODO Auto-generated method stub
		System.out.println("reflish");
		for(ChartHisBean ch:inviteNotices){
			if(imMessage.getFromSubJid().equals(ch.getFrom())){
				ch.setContent(imMessage.getContent());
				ch.setNoticeTime(imMessage.getTime());
				Integer x = ch.getNoticeSum() == null ? 0 : ch.getNoticeSum();
				ch.setNoticeSum(x + 1);
			}
			noticeAdapter.setNoticeList(inviteNotices);
			noticeAdapter.notifyDataSetChanged();
			//setPaoPao();
		}
	}
}
class RecentChartAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<ChartHisBean> inviteUsers;
	private Context context;
	private View.OnClickListener contacterOnClick;

	public RecentChartAdapter(Context context, List<ChartHisBean> inviteUsers) {
		this.context = context;
		mInflater = LayoutInflater.from(context);

		this.inviteUsers = inviteUsers;
	}

	public void setNoticeList(List<ChartHisBean> inviteUsers) {
		this.inviteUsers = inviteUsers;
	}

	@Override
	public int getCount() {
		return inviteUsers.size();
	}

	@Override
	public Object getItem(int position) {
		return inviteUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChartHisBean notice = inviteUsers.get(position);
		Integer ppCount = notice.getNoticeSum();
		ViewHolderx holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.recent_chart_item, null);
			holder = new ViewHolderx();
			holder.newTitle = (TextView) convertView
					.findViewById(R.id.new_title);
			holder.itemIcon = (ImageView) convertView
					.findViewById(R.id.new_icon);
			holder.newContent = (TextView) convertView
					.findViewById(R.id.new_content);
			holder.newDate = (TextView) convertView.findViewById(R.id.new_date);
			holder.paopao = (TextView) convertView.findViewById(R.id.paopao);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderx) convertView.getTag();
		}
		// connection.getRoster().getEntry(user)
		String jid = notice.getFrom();
		ContactsManager contactsManager=new ContactsManager(context);
		User u = contactsManager.getUserFromDB(jid);
		if (null == u) {
			u = new User();
			u.setJID(jid);
			u.setName(jid);
		}

		holder.newTitle.setText(u.getName());

		Bitmap bitmap= UserManager.getUserAvaterFromSDByJID(jid,context);

		if(bitmap!=null)
			holder.itemIcon.setImageBitmap(bitmap);
		if(notice.getMegtype()==0||notice.getMegtype()==1)
			holder.newContent.setText(notice.getContent());
		if(notice.getMegtype()==2||notice.getMegtype()==3)
			holder.newContent.setText("[图片]");
		if(notice.getMegtype()==4||notice.getMegtype()==5)
			holder.newContent.setText("[语音]");

		holder.newContent.setTag(u);
		holder.newDate.setText(notice.getNoticeTime().substring(0, 14));

		if (ppCount != null && ppCount > 0) {
			holder.paopao.setText(ppCount + "");
			holder.paopao.setVisibility(View.VISIBLE);

		} else {
			holder.paopao.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(contacterOnClick);

		return convertView;
	}

	public class ViewHolderx {
		public ImageView itemIcon;
		public TextView newTitle;
		public TextView newContent;
		public TextView newDate;
		public TextView paopao;

	}


	public void setOnClickListener(View.OnClickListener contacterOnClick) {

		this.contacterOnClick = contacterOnClick;
	}
}
