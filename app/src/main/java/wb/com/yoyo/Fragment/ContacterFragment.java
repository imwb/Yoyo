package wb.com.yoyo.Fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smackx.packet.VCard;

import java.io.File;
import java.util.List;

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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewGroup viewGroup=(ViewGroup) inflater.inflate(R.layout.contact_item, null);
		ImageView iv_user_pic=(ImageView) viewGroup.findViewById(R.id.iv_user_pic);
		TextView nickname=(TextView) viewGroup.findViewById(R.id.tv_user_nickname);
		User user=list.get(arg0);

		String path = user.getAvatar_path();
		Bitmap bitmap=UserManager.getUserAvaterFromSD(path);

		if(bitmap!=null)
			iv_user_pic.setImageBitmap(bitmap);

		nickname.setText(user.getName());
		nickname.setTag(user);

		return viewGroup;
	}

}