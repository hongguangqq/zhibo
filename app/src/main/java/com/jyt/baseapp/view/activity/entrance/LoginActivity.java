package com.jyt.baseapp.view.activity.entrance;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LoginModel;
import com.jyt.baseapp.model.impl.LoginModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.activity.BaseMCVActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseMCVActivity implements PlatformActionListener,EasyPermissions.PermissionCallbacks  {


    @BindView(R.id.et_login_account)
    EditText mEtAccount;
    @BindView(R.id.et_login_password)
    EditText mEtPassword;
    @BindView(R.id.btn_login_submit)
    Button mBtnSubmit;
    @BindView(R.id.btn_login_register)
    Button mBtnRegister;
    @BindView(R.id.tv_ruler)
    TextView mTvRuler;
    @BindView(R.id.tv_login_repwd)
    TextView mTvRepwd;
    @BindView(R.id.iv_login_qq)
    ImageView mIvQq;
    @BindView(R.id.iv_login_chat)
    ImageView mIvChat;

    private boolean mQOW=true;
    private LoginModel mLoginModel;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(LoginActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();
                    Platform platform = (Platform) msg.obj;
                    final String userId = platform.getDb().getUserId();//获取用户账号
                    String userName = platform.getDb().getUserName();//获取用户名字
                    String userIcon = platform.getDb().getUserIcon();//获取用户头像
                    String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                    //Toast.makeText(LoginActivity.this, "用户名：" + userName + "  性别：" + userGender, Toast.LENGTH_SHORT).show();
                    //将返回的数据发送到服务器，判断是否已经存在该用户，存在则进入主界面，不存在则进入注册流程
                    mLoginModel.LoginByQW(userId, true,new BeanCallback<BaseJson<UserBean>>() {
                        @Override
                        public void response(boolean success, BaseJson<UserBean> response, int id) {
                            if (success){
                                Log.e("@#","success");
                                if (response.getCode()==200){
                                    //直接登录
                                    Const.SaveUser(response.getData(),mLoginModel);
                                    IntentHelper.OpenContentActivity(LoginActivity.this);


                                }else if (response.getCode()==500){
                                    //没有注册
                                    BaseUtil.makeText(response.getMessage());
                                    IntentHelper.OpenPerfectInfoActivityByQW(LoginActivity.this,userId,true);
                                }else if (response.getCode()==300){
                                    //登陆失败
                                    BaseUtil.makeText(response.getMessage());
                                }
                            }
                        }
                    });
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    // 相机权限、多个权限
    private final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.GET_ACCOUNTS
    };
    // 打开相机请求Code，多个权限请求Code
    private final int REQUEST_CODE_PERMISSIONS = 100;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initSetting();

    }

    private void init() {
        HideActionBar();
        setvMainBackground(R.mipmap.bg_entrance);
        mLoginModel = new LoginModelImpl();
        checkPermission();

    }

    private void initSetting(){

    }

    @OnClick(R.id.btn_login_submit)
    public void ToSubmit(){
        String tel = mEtAccount.getText().toString();
        String pwd = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(tel)){
            BaseUtil.makeText("手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            BaseUtil.makeText("密码不能为空");
            return;
        }
        mLoginModel.Login(tel, pwd, new BeanCallback<BaseJson<UserBean>>(this,false,null) {
            @Override
            public void response(boolean success, BaseJson<UserBean> response, int id) {
                if (success){
                    BaseUtil.e(response);
                    if (response.getCode()==200){
                        Const.SaveUser(response.getData(),mLoginModel);
                        BaseUtil.makeText("登录成功");
                        IntentHelper.OpenContentActivity(LoginActivity.this);
                    }else if(response.getCode()==300){
                        BaseUtil.makeText(response.getMessage());
                    }
                }
            }
        });

    }


    @OnClick(R.id.btn_login_register)
    public void OpenRegisterActivity(){
        IntentHelper.OpenRegisterActivity(this);
    }

    @OnClick(R.id.iv_login_qq)
    public void LoginByQQ(){
        mQOW=true;
        BaseUtil.makeText("请等待");
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(false);
        qq.setPlatformActionListener(this);
        if (!qq.isClientValid()) {
            Toast.makeText(this, "QQ未安装,请先安装QQ", Toast.LENGTH_SHORT).show();
        }
        authorize(qq);
    }

    @OnClick(R.id.iv_login_chat)
    public void LoginByChat(){
        mQOW=false;
        BaseUtil.makeText("请等待");
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(this);
        wechat.SSOSetting(false);
        if (!wechat.isClientValid()) {
            Toast.makeText(this, "微信未安装,请先安装微信", Toast.LENGTH_SHORT).show();
        }
        authorize(wechat);
    }





    /**
     * 分享
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 授权
     *
     * @param platform
     */
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        if (platform.isAuthValid()) {  //如果授权就删除授权资料
            platform.removeAccount(true);
        }

        platform.showUser(null); //授权并获取用户信息
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String name = platform.getDb().getUserName();
        String id = platform.getDb().getUserId();
        Log.e("@#","Qwname="+name);
        Log.e("@#","Qwid="+id);
        Message message = Message.obtain();
        message.what = 1;
        message.obj = platform;
        mHandler.sendMessage(message);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e("@#","错误");
        Message message = Message.obtain();
        message.what = 2;
        message.obj = platform;
        mHandler.sendMessage(message);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Message message = Message.obtain();
        message.what = 3;
        message.obj = platform;
        mHandler.sendMessage(message);
    }



    // 自定义申请多个权限
    private void checkPermission() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
                if(!Settings.canDrawOverlays(this)){
                    //没有悬浮窗权限,跳转申请
                    startAppSettings();
                    return;
                }
                if (!TextUtils.isEmpty(Const.getUserID())){
                    IntentHelper.OpenContentActivity(this);
                }
            } else {
                List<String> permissionList = new ArrayList<>();
                for (int i = 0; i < PERMISSIONS.length; i++)
                    if (!EasyPermissions.hasPermissions(this, PERMISSIONS[i])){
                        permissionList.add(PERMISSIONS[i]);
                    }
                EasyPermissions.requestPermissions(this, "App应用需要您赋予权限", REQUEST_CODE_PERMISSIONS, permissionList.toArray(new String[permissionList.size()]));
            }
        }else {
            if (!TextUtils.isEmpty(Const.getUserID())){
                IntentHelper.OpenContentActivity(this);
            }
        }

    }

    //显示对话框提示用户缺少权限
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("帮助");//提示帮助
        builder.setMessage("当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限(包括悬浮框权限)。");

        //如果是拒绝授权，则退出应用
        //退出
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //打开设置，让用户选择打开权限
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();//打开设置
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    //打开系统应用设置(ACTION_APPLICATION_DETAILS_SETTINGS:系统设置权限)
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 0);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            IntentHelper.OpenContentActivity(this);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        showMissingPermissionDialog();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            if(!Settings.canDrawOverlays(this)){
                //没有悬浮窗权限,跳转申请
                startAppSettings();
                return;
            }
            if (!TextUtils.isEmpty(Const.getUserID())){
                IntentHelper.OpenContentActivity(this);
            }
        } else {
            showMissingPermissionDialog();
        }
    }

    /**
     * 双击退出
     */
    private long mPressedTime = 0;
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }
        else{//退出程序
            this.finish();
            System.exit(0);
        }
    }
}
