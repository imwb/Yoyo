package wb.com.yoyo.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smackx.packet.VCard;

import java.io.File;
import java.util.List;

import wb.com.yoyo.Activity.ChatActivity;
import wb.com.yoyo.Manager.ContactsManager;
import wb.com.yoyo.Manager.UserManager;
import wb.com.yoyo.Model.User;
import wb.com.yoyo.R;



public class ContacterFragment extends Fragment {

	private ListView listView;
	private ViewGroup vg;
	private ContactsManager contactsManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_contacters, null);
		listView=(ListView) view.findViewById(R.id.lv_contacters);
		vg=(ViewGroup) view.findViewById(R.id.layout_add);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		contactsManager=new ContactsManager(getActivity());
		AllContacterAdapter adapter = new AllContacterAdapter(contactsManager.getContactsFromDB(),getActivity());
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User user= (User) view.findViewById(R.id.tv_user_nickname).getTag();
				createChat(user);
			}
		});
	}

	private void createChat(User user) {
		Intent intent=new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("to",user.getJID());
		startActivity(intent);
	}
}
class AllContacterAdapter extends BaseAdapter {

	private List<User> list;
	private LayoutInflater inflater;
	private Context context;

	public AllContacterAdapter(List<User> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 *listview 现调用 getCount 得到listview 要显示的记录数 ，在调用getView绘制每条记录
	 */
	@Override

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.contact_item,null);
			viewHolder.iv_userpic= (ImageView) convertView.findViewById(R.id.iv_user_pic);
			viewHolder.nickname= (TextView) convertView.findViewById(R.id.tv_user_nickname);
			convertView.setTag(viewHolder);
		}else {
			viewHolder= (ViewHolder) convertView.getTag();
		}

		User user=list.get(position);

		viewHolder.nickname.setText(user.getName());
		viewHolder.iv_userpic.setImageBitmap(UserManager.getUserAvaterFromSDBypath(user.getAvatar_path()));

		viewHolder.nickname.setTag(user);

		return convertView;
	}



	/**
	 * adaper 优化 viewHolder
	 */
	static class ViewHolder{

		ImageView iv_userpic;
		TextView nickname;

	}
}