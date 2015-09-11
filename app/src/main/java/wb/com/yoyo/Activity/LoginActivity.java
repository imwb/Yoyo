package wb.com.yoyo.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import wb.com.yoyo.Activity.Tast.LoginTask;
import wb.com.yoyo.Manager.SPManager;
import wb.com.yoyo.Manager.XmppManager;
import wb.com.yoyo.Model.LoginConfig;
import wb.com.yoyo.R;
import wb.com.yoyo.Utils.BitmapUtils;
import wb.com.yoyo.Utils.StringUtil;
import wb.com.yoyo.Utils.ValidateUtil;

/**
 * 登录
 * Created by wb on 15/9/10.
 */
public class LoginActivity extends ActivitySupport{

    private EditText edt_username, edt_pwd;
    private CheckBox rememberCb, autologinCb;
    private Button btn_login = null;
    private Button btn_reg = null;
    private LoginConfig loginConfig;
    private TextView tx_setconfig,tv_reg;
    private ImageView avatar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_login);
        context=this;

        initViews();
        initRrg();
        initSet();

    }


    private void initViews() {
        loginConfig=getLoginConfig();

        if(loginConfig.isAutoLogin()==true){
            LoginTask loginTask = new LoginTask(context, loginConfig);
            loginTask.execute();
        }



        edt_pwd=(EditText) findViewById(R.id.et_login_password);
        edt_username=(EditText) findViewById(R.id.et_login_name);
        rememberCb=(CheckBox) findViewById(R.id.cb_remember);
        autologinCb=(CheckBox) findViewById(R.id.cb_autologin);
        btn_login=(Button) findViewById(R.id.but_login);
        tx_setconfig=(TextView) findViewById(R.id.tv_logconfig);
        tv_reg=(TextView) findViewById(R.id.tv_reg);
        avatar= (ImageView) findViewById(R.id.iv_avatar);

        edt_username.setText(loginConfig.getUsername());
        if(loginConfig.isRemember()){
            edt_pwd.setText(loginConfig.getPassword());
        }
        Bitmap bitmap= BitmapUtils.
                getBitmapByOption(BitmapUtils.getImagePath() + "/" + loginConfig.getUsername() + ".png", 80, 80);
        if(bitmap!=null)
            avatar.setImageBitmap(bitmap);


        rememberCb.setChecked(loginConfig.isRemember());
        rememberCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (!isChecked)
                    autologinCb.setChecked(false);
            }
        });
        autologinCb.setChecked(loginConfig.isAutoLogin());

        //登录按钮
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(checkData()&&validateInternet()){
                    String username = edt_username.getText().toString();
                    String password = edt_pwd.getText().toString();
                    loginConfig.setUsername(username);
                    loginConfig.setPassword(password);
                    // 先记录下各组件的目前状态,登录成功后才保存
                    loginConfig.setPassword(password);
                    loginConfig.setUsername(username);
                    loginConfig.setIsRemember(rememberCb.isChecked());
                    loginConfig.setIsAutoLogin(autologinCb.isChecked());
                    LoginTask loginTask = new LoginTask(LoginActivity.this,
                            loginConfig);
                    loginTask.execute();
                }
            }
        });

    }
    private void initSet() {
        //点击配置信息
        tx_setconfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final EditText xmppHostText = new EditText(context);
                xmppHostText.setText(loginConfig.getXmppHost());
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setTitle("服务器设置")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(xmppHostText)
                        .setMessage("请设置服务器IP地址")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        String xmppHost = StringUtil
                                                .doEmpty(xmppHostText.getText()
                                                        .toString());
                                        loginConfig.setXmppHost(xmppHost);
                                        XmppManager.getInstance().init(
                                                loginConfig);
                                        new SPManager(context)
                                                .saveLoginConfig(loginConfig);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();// 取消弹出框
                                    }
                                }).create().show();

            }
        });
    }

    private void initRrg() {
        tv_reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(context, ResginActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     *
     * 登录校验.
     *
     */
    private boolean checkData() {
        boolean checked = false;
        checked = (!ValidateUtil.isEmpty(edt_username, "登录名") && !ValidateUtil
                .isEmpty(edt_pwd, "密码"));
        return checked;
    }
    @Override
    public void onBackPressed() {
        isExit();
    }

}
