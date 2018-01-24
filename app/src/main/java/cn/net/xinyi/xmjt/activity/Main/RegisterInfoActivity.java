package cn.net.xinyi.xmjt.activity.Main;
/**
 * 注册个人资料的输入
 * */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppConfig;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;

public class RegisterInfoActivity extends BaseActivity2 implements OnClickListener {
    private static final String TAG = "RegisterInfoActivity";
    //密码
    @BindView(id = R.id.edt_UsrPwd)
    private EditText etPassword;
    //密码确认
    @BindView(id = R.id.edt_UsrPwdRe)
    private EditText etPasswordConfirm;
    //注册
    @BindView(id = R.id.btn_Register,click = true)
    private Button btn_Register;
    //分局单位
    @BindView(id = R.id.spin_fj,click = true)
    private TextView tv_Fj;
    //派出所
    @BindView(id = R.id.spin_pcs,click = true)
    private TextView tv_Pcs;
    //警务室
    @BindView(id = R.id.spin_jws,click = true)
    private TextView tv_Jws ;
    //身份标识
    @BindView(id = R.id.spin_sfbs,click = true)
    private TextView tv_sfbs ;
    //手机号码-也作为用户名
    @BindView(id = R.id.tv_phone)
    private TextView tv_phone;
    //警号
    @BindView(id = R.id.et_jhnumber)
    private EditText et_jhnumber;
    //名字
    @BindView(id = R.id.et_username)
    private EditText et_username;
    //街道
    @BindView(id = R.id.tv_jd,click = true)
    private TextView tv_jd;
    //社区
    @BindView(id = R.id.tv_sq,click = true)
    private TextView tv_sq;
    //网格
    @BindView(id = R.id.tv_wangge,click = true)
    private TextView tv_wangge;

    private String pcs_num;
    private String jws_num;
    private String jd_bm;
    private String sq_bm;
    private ArrayList<String> data1;
    private int networkType;
    private String fj_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        tv_phone.setText(getIntent().getStringExtra("edt_phone"));
        //tv_phone.setText("18566266510");
    }


    @Override
    public void onClick(View v) {
        if (BaseDataUtils.isFastClick()) {
            UI.toast(this, "请不要频繁点击！");
        } else {
            switch (v.getId()) {
                //选择所属街道
                case R.id.tv_jd:
                    //需要对 已有的网格 社区 集合数据清空
                    tv_sq.setText("");
                    tv_wangge.setText("");

                    final Map<String,String> jdMaps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_JD);
                    new AlertDialog.Builder(this).setItems(jdMaps.values().toArray(new String[jdMaps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_jd.setText(jdMaps.values().toArray(new String[jdMaps.values().size()])[which]);
                                    jd_bm=zdUtils.getMapKey(jdMaps,tv_jd.getText().toString());
                                }
                            }).create().show();

                    break;

                //选择所属社区
                case R.id.tv_sq:
                    tv_wangge.setText("");

                    if (StringUtils.isEmpty(tv_jd.getText().toString().trim())) {
                        BaseUtil.showDialog("所属街道不能为空", RegisterInfoActivity.this);
                    } else if (!tv_jd.getText().toString().trim().isEmpty()) {
                        final Map<String,String> maps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_SQ);
                        new AlertDialog.Builder(this).setItems(maps.values().toArray(new String[maps.values().size()]),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_sq.setText(maps.values().toArray(new String[maps.values().size()])[which]);
                                        sq_bm=zdUtils.getMapKey(maps,tv_sq.getText().toString());
                                    }
                                }).create().show();


                    }
                    break;

                //选择网格数据
                case R.id.tv_wangge:
                    sq_bm = zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_SQ, tv_sq.getText().toString());
                    //判断上级社区是否为空
                    if (tv_sq.getText().toString().trim().isEmpty()) {
                        BaseUtil.showDialog("所属社区不能为空", RegisterInfoActivity.this);
                        //根据所选的社区，匹配下属的网格信息
                    } else if (sq_bm != null && !tv_sq.getText().toString().trim().isEmpty()) {
                        Intent intent = new Intent(RegisterInfoActivity.this, ChooseWangGeActivity.class);
                        intent.putExtra(GeneralUtils.WangGe, sq_bm);
                        startActivityForResult(intent, 100);
                    }
                    break;

                //身份标识
                case R.id.spin_sfbs:
                    final Map<String,String> sfbsMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_RYSF);
                    new AlertDialog.Builder(this).setItems(sfbsMaps.values().toArray(new String[sfbsMaps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_sfbs.setText(sfbsMaps.values().toArray(new String[sfbsMaps.values().size()])[which]);
                                }
                            }).create().show();
                    break;

                //选择分局
                case R.id.spin_fj:
                    //需要清空派出所、警务室信息，以免数据重复
                    tv_Pcs.setText("");
                    tv_Jws.setText("");
                    final Map<String,String> fjMaps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_FJ);
                    new AlertDialog.Builder(this).setItems(fjMaps.values().toArray(new String[fjMaps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_Fj.setText(fjMaps.values().toArray(new String[fjMaps.values().size()])[which]);
                                    fj_num=zdUtils.getMapKey(fjMaps,tv_Fj.getText().toString());
                                }
                            }).create().show();

                    break;

                //选择派出所
                case R.id.spin_pcs:
                    //需要清空警务室信息，以免数据重复
                    tv_Jws.setText("");
                    if (tv_Fj.getText().toString().trim().isEmpty()||fj_num==null) {
                        BaseUtil.showDialog("请选择所属分局", RegisterInfoActivity.this);
                    } else {
                        final Map<String,String> pcsMaps=zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_PCS,fj_num);
                        new AlertDialog.Builder(this).setItems(pcsMaps.values().toArray(new String[pcsMaps.values().size()]),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_Pcs.setText(pcsMaps.values().toArray(new String[pcsMaps.values().size()])[which]);
                                        pcs_num=zdUtils.getMapKey(pcsMaps,tv_Pcs.getText().toString());
                                    }
                                }).create().show();
                    }
                    break;

                //警务室的选择
                case R.id.spin_jws:
                    //需要清空警务室信息，以免数据重复
                    tv_Jws.setText("");
                    if (tv_Pcs.getText().toString().trim().isEmpty()||pcs_num==null) {
                        BaseUtil.showDialog("派出所不能为空", RegisterInfoActivity.this);
                    } else  {
                        final Map<String,String> jwsMaps=zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_JWS,pcs_num);
                        jwsMaps.put("11","其他");
                        new AlertDialog.Builder(this).setItems(jwsMaps.values().toArray(new String[jwsMaps.values().size()]),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_Jws.setText(jwsMaps.values().toArray(new String[jwsMaps.values().size()])[which]);
                                        jws_num=zdUtils.getMapKey(jwsMaps,tv_Jws.getText().toString());
                                    }
                                }).create().show();
                    }
                    break;

                //注册
                case R.id.btn_Register:
                    //检测是否连接网络
                    if (((AppContext) getApplication()).getNetworkType() == 0) {
                        BaseUtil.showDialog("当前无可用的网络连接，将无法注册！", RegisterInfoActivity.this);
                        break;
                    }
                    register();
                    break;
                default:
                    break;
            }
        }
    }





    //注册时的条件判断
    private void register() {
        //检测是否连接网络
        networkType = ((AppContext) getApplication()).getNetworkType();
        //用户名
        String userName = tv_phone.getText().toString().trim();
        //密码
        String passwd = etPassword.getText().toString().trim();
        //密码确认
        String passwdConfirm = etPasswordConfirm.getText().toString().trim();

        String msg = "";
        if (networkType == 0) {
            msg =getString(R.string.network_not_connected);
        }else  if (userName.isEmpty()) {
            msg = getString(R.string.msg_reg_account_null);
        } else if (userName.length() != 11) {
            msg = getString(R.string.msg_reg_account_polno);
        } else if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
            msg = "姓名不能为空";
        } else if (TextUtils.isEmpty(tv_sfbs.getText().toString())) {
            msg = "身份标识不能为空";
        } else if (TextUtils.isEmpty(tv_Pcs.getText().toString().trim())) {
            msg = "必须选择所属派出所";
        } else if (tv_Pcs.getText().toString().contains("所")&&tv_Jws.getText().toString().trim().isEmpty()) {
            msg = "请选择所属警务室，没有可选择其他";
        } else if (tv_sfbs.getText().toString().trim().equals("民警")&&
                (et_jhnumber.getText().toString().trim().isEmpty() ||
                        et_jhnumber.getText().toString().trim().length() != 6)) {
            //判断身份选择是否为民警,需要录入警号
            msg = "请输入正确6位数警号";
        } else if (tv_sfbs.getText().toString().trim().equals("综管员")&&(tv_jd.getText().toString().trim().isEmpty() ||
                tv_sq.getText().toString().trim().isEmpty() ||
                tv_wangge.getText().toString().trim().isEmpty())) {
            msg = "综管员账号必须选择所在的街道、社区、网格";
        } else if (passwd.isEmpty() || passwd.isEmpty()) {
            msg = getResources().getString(R.string.msg_reg_pwd_null);
        } else if (!passwd.equals(passwdConfirm)) {
            msg = getResources().getString(R.string.msg_reg_pwd_confirm_error);
        }

        if(!msg.isEmpty()){
            BaseUtil.showDialog(msg, RegisterInfoActivity.this);
//        }else if(getIntent().getIntExtra("isRegester",2)!=0){
//            saveUserData();
        }else{
            requestData();
        }
    }

    private void requestData() {
        JSONObject jo=new JSONObject();
        jo.put("SJHM",tv_phone.getText().toString());
        String json=jo.toJSONString();
        ApiResource.queryTXL(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                String jo= (String) JSON.parseArray(result).getJSONObject(0).get("JH");
                //非民警身份
                if (jo == null || jo.length()< 6){
                    if (StringUtils.nullToString(tv_sfbs.getText().toString()).equals("民警")){
                        BaseUtil.showDialog("身份标识匹配错误，请重新选择！（如有错误请联系所属派出所管理员）",RegisterInfoActivity.this);
                    }else {
                        saveUserData();
                    }
                    //民警身份
                } else {
                    if (!StringUtils.nullToString(tv_sfbs.getText().toString()).equals("民警")){
                        BaseUtil.showDialog("您选择身份标识错误！",RegisterInfoActivity.this);
                    }else  if (!jo.equals(StringUtils.nullToString(et_jhnumber.getText().toString()))){
                        BaseUtil.showDialog("警号匹配错误，请重新输入！（如有错误请联系所属派出所管理员）！",RegisterInfoActivity.this);
                    }else{
                        saveUserData();
                    }
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("注册失败，请稍后再试");
            }
        });
    }


    /**
     * 保存用户信息
     */
    private void saveUserData() {
        String sf = tv_sfbs.getText().toString().trim();  //身份标识
        String policeno = et_jhnumber.getText().toString().trim();  //警号
        String jd = tv_jd.getText().toString().trim(); //街道
        String sq = tv_sq.getText().toString().trim(); //社区
        String wg = tv_wangge.getText().toString().trim();   //网格

        UserInfo userInfo=new UserInfo();
        //用户名-电话号码
        userInfo.setUsername(tv_phone.getText().toString());
        userInfo.setCellphone(tv_phone.getText().toString());
        //名字
        userInfo.setName(et_username.getText().toString());
        //密码
        userInfo.setPassword(etPassword.getText().toString());
        //分局编码
        userInfo.setFjbm(fj_num);
        //派出所
        userInfo.setOrgancode(pcs_num==null?"":pcs_num);
        //警务室
        userInfo.setJwscode(jws_num!="11"?jws_num:"其他");
        //警务室
        if (jws_num==null){
            userInfo.setJwscode("");
        }else {
            userInfo.setJwscode(!jws_num.equals("11")?jws_num:"其他");
        }

        //所属街道
        userInfo.setSsjd(jd==null?"":jd);
        //所属社区
        userInfo.setSssq(sq==null?"":sq);
        //所属网格
        userInfo.setSswg(wg==null?"":wg);
        //身份识别
        userInfo.setAccounttype(sf);

        if (!policeno.isEmpty()&&sf.equals("民警")) {
            userInfo.setPoliceno(policeno);
        }else {
            userInfo.setPoliceno(" ");
        }
        //是否注册权限  0有  1  没有
        userInfo.setIsregester(getIntent().getIntExtra("isRegester",0));

        String json = JSON.toJSONString(userInfo);

        Message msg = new Message();
        msg.what = 4;
        mHandle.handleMessage(msg);

        ApiResource.getRegisters(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200) {
                        String result = new String(bytes);
                        Message msg = new Message();
                        if (result.equals("\"1\"")) {
                            msg.what = 1;
                        } else if (result.equals("\"-1\"")) {
                            msg.what = -1;
                        } else if (result.equals("\"-2\"")) {
                            msg.what = -2;
                        } else if (result.equals("\"0\"")) {
                            msg.what = 0;
                        }
                        mHandle.sendMessage(msg);
                    }

                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 0;
                mHandle.sendMessage(msg);
            }
        });
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4:
                    showLoadding("注册中...",RegisterInfoActivity.this);
                    break;
                case 1:
                    stopLoading();
                    //本地注册-升级 成功 清除缓存
                    if (getIntent().getFlags()== GeneralUtils.LoginActivity){
                        SharedPreferences sp = AppConfig.getSharedPreferences(RegisterInfoActivity.this);
                        if (sp!=null){
                            AppConfig.CleanCurrentConfig(RegisterInfoActivity.this);
                        }
                        intentLogin(getResources().getString(R.string.modify_success));
                    }else {
                        intentLogin(getResources().getString(R.string.register_success));
                    }

                    break;
                case 0:
                    stopLoading();
                    BaseUtil.showDialog("注册失败", RegisterInfoActivity.this);
                    break;
                case -2:
                    stopLoading();
                    BaseUtil.showDialog("该手机已经注册", RegisterInfoActivity.this);
                    break;
                case -1:
                    stopLoading();
                    BaseUtil.showDialog("注册信息不完整", RegisterInfoActivity.this);
                    break;
                default:
                    break;
            }
        }
    };




    //注册成功跳转注册页面
    private void intentLogin(String msg) {
        DialogHelper.showAlertDialog(this, msg, new DialogHelper.OnOptionClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, Object o) {
                Intent intent = new Intent(RegisterInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                RegisterSmsActivity.instance.finish();
                LoginActivity.instance.finish();
            }
        });
    }


    //ChooseWangGeActivity.class  返回选择的网格信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK&&data!=null){
            data1=data.getStringArrayListExtra(GeneralUtils.RegistWangGe);
            String[] ary=data1.toArray(new String[data1.size()]);
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < ary.length; i++){
                sb. append(ary[i]+",");
            }
            tv_wangge.setText(sb.toString());
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getResources().getString(R.string.title_new_user_reg);
    }
}
