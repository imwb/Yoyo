package wb.com.yoyo.Activity.Tast;


import java.io.File;
import java.util.List;

import org.jivesoftware.smackx.packet.VCard;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;



import wb.com.yoyo.Activity.ActivitySupport;
import wb.com.yoyo.Activity.MainActivity;
import wb.com.yoyo.Manager.ContactsManager;
import wb.com.yoyo.Manager.SPManager;
import wb.com.yoyo.Manager.UserManager;
import wb.com.yoyo.Manager.XmppManager;
import wb.com.yoyo.Model.LoginConfig;
import wb.com.yoyo.Model.User;
import wb.com.yoyo.R;
import wb.com.yoyo.Service.ContactService;
import wb.com.yoyo.Service.IMChatService;
import wb.com.yoyo.Utils.BitmapUtils;
import wb.com.yoyo.comm.Constant;

/**
 * 登录异步任务类
 * @author wb
 *
 */
public class LoginTask extends AsyncTask<String, Integer, Integer> {
	private ProgressDialog pd;
	private Context context;
	private ActivitySupport activitySupport;
	private LoginConfig loginConfig;

	public LoginTask(Context context, LoginConfig loginConfig) {
		this.loginConfig = loginConfig;
		this.context = context;
		this.activitySupport= (ActivitySupport) context;
		this.pd = activitySupport.getProgressDialog();
	}

	@Override
	protected void onPreExecute() {
		pd.setTitle("请稍等");
		pd.setMessage("正在登录...");
		pd.show();
		super.onPreExecute();
	}
	@Override
	protected Integer doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		XmppManager.init(loginConfig);
		return XmppManager.login(loginConfig);
		
	}
	
	protected void onPostExecute(Integer result) {
		pd.dismiss();
		switch (result) {
		case Constant.LOGIN_SECCESS: // 登录成功
			Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			if (loginConfig.isFirstStart()) {// 如果是首次启动
				intent.setClass(context, MainActivity.class);
				//loginConfig.setIsFirstStart(false);
				ContactsManager contactsManager=new ContactsManager(context);
				contactsManager.clearDB();
				List<User> users=contactsManager.loadContacts();
				contactsManager.savaContacts(users);

			} else {
				loginConfig.setIsFirstStart(true);
				intent.setClass(context, MainActivity.class);
			}
			//intent.setClass(context, StartActivity.class);
			VCard vCard=XmppManager.getMyvCard();
			if(vCard!=null&&vCard.getAvatar()!=null){
				Bitmap bitmap=BitmapFactory.decodeByteArray(vCard.getAvatar(), 0, vCard.getAvatar().length);
				File file= BitmapUtils.getImagePath();
				System.out.println("file..." + file);
				BitmapUtils.savaBitmap(file, loginConfig.getUsername()+".png", bitmap);
//				StaticPara.userName=vCard.getNickName();
				//内存问题？？
				String path=file.getPath()+loginConfig.getUsername()+".png";
				loginConfig.setAvatarpath(path);
				System.out.println(path);
				}
			//loginConfig.setFirstStart(true);

			SPManager spManager=new SPManager(context);
			spManager.saveLoginConfig(loginConfig);// 保存用户配置信息

			startLoadService();
			context.startActivity(intent);
			activitySupport.finish();
			break;
		case Constant.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.message_invalid_username_password),
					Toast.LENGTH_SHORT).show();
			break;
		case Constant.SERVER_UNAVAILABLE:// 服务器连接失败
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.message_server_unavailable),
					Toast.LENGTH_SHORT).show();
			break;
		case Constant.LOGIN_ERROR:// 未知异常
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.unrecoverable_error), Toast.LENGTH_SHORT)
					.show();
			break;
		}
		super.onPostExecute(result);
	}

	private void startLoadService() {
		activitySupport.startService(new Intent(context, IMChatService.class));
		activitySupport.startService(new Intent(context, ContactService.class));
	}


}
