package cn.net.xinyi.xmjt.activity.Main.Setting;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.widget.picker.SuperImageView;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.ChooseWangGeActivity;
import cn.net.xinyi.xmjt.activity.Main.LoginActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * Created by hao.zhou on 2015/10/30.
 */
public class ModifyUserInfoActivity extends BaseActivity2 implements View.OnClickListener {

    private ArrayList<String> wg = new ArrayList<String>();
    private ArrayList<String> data1;
    private String pcs_num;
    private String jws_num;
    private String jd_bm;
    private String sq_bm;
    private ProgressDialog mProgressDialog;
    private int networkType;
    private List<String> jwsOrgans = new ArrayList<String>();
    private List<String> sq = new ArrayList<String>();
    //电话-登录用户名
    @BindView(id = R.id.tv_phone)
    TextView tv_phone;
    //名字
    @BindView(id = R.id.et_username)
    EditText et_username;
    //分局
    @BindView(id = R.id.tv_fj, click = true)
    TextView tv_fj;
    //派出所
    @BindView(id = R.id.tv_pcs, click = true)
    TextView tv_pcs;
    //警务室
    @BindView(id = R.id.tv_jws, click = true)
    TextView tv_jws;
    //身份标识
    @BindView(id = R.id.tv_sfbs)
    TextView tv_sfbs;
    //警号
    @BindView(id = R.id.et_jhnumber)
    EditText et_jhnumber;
    //街道
    @BindView(id = R.id.tv_jd, click = true)
    TextView tv_jd;
    //社区
    @BindView(id = R.id.tv_sq, click = true)
    TextView tv_sq;
    //网格
    @BindView(id = R.id.tv_wg, click = true)
    TextView tv_wg;
    @BindView(id = R.id.ll_jh)
    LinearLayout ll_jh;
    @BindView(id = R.id.ll_sfz)
    LinearLayout ll_sfz;
    @BindView(id = R.id.tv_sfzh)
    TextView tv_sfzh;
    @BindView(id = R.id.simg_head)
    SuperImageView simg_head;
    //提交
    @BindView(id = R.id.btn_modeify, click = true)
    Button btn_modeify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_register_infor);
        AnnotateManager.initBindView(this);
        //进度框
        mProgressDialog = new ProgressDialog(this);
        setPoliceData();//获得数据
        simg_head.with(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //提交
            case R.id.btn_modeify:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                String msg = "";
                if (networkType == 0) {
                    msg = "当前无可用的网络连接!";
                } else if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
                    msg = "姓名不能为空";
                } else if (tv_sfbs.getText().toString().trim().isEmpty()) {
                    msg = "身份标识不能为空";
                } else if (tv_pcs.getText().toString().trim().isEmpty()) {
                    msg = "必须选择所属派出所";
                } else if (tv_pcs.getText().toString().contains("所")
                        && !tv_pcs.getText().toString().equals("看守所")
                        && !tv_pcs.getText().toString().equals("戒毒所")
                        && tv_jws.getText().toString().trim().isEmpty()) {
                    msg = "必须选择所属警务室";
                    //判断身份选择是否为民警,需要录入警号
                } else if (tv_sfbs.getText().toString().trim().equals("民警") &&
                        (et_jhnumber.getText().toString().trim().isEmpty() ||
                                et_jhnumber.getText().toString().trim().length() != 6)) {
                    msg = "请输入正确6位数警号";
                } else if (tv_sfbs.getText().toString().trim().equals("综管员") &&
                        (tv_jd.getText().toString().trim().isEmpty() ||
                                tv_sq.getText().toString().trim().isEmpty() ||
                                tv_wg.getText().toString().trim().isEmpty())) {
                    msg = "综管员账号必须选择所在的街道、社区、网格";
                }

                if (!msg.isEmpty()) {
                    BaseUtil.showDialog(msg, ModifyUserInfoActivity.this);
                } else {
                    //用户修改信息提交
                    uploadImage();
                }
                break;

            //选择网格数据
            case R.id.tv_wg:
                //清空数据
                wg.clear();
                //判断上级社区是否为空
                if (tv_sq.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("所属社区不能为空", ModifyUserInfoActivity.this);
                } else if (sq_bm != null && !tv_sq.getText().toString().trim().isEmpty()) {
                    Intent intent = new Intent(ModifyUserInfoActivity.this, ChooseWangGeActivity.class);
                    intent.putExtra(GeneralUtils.WangGe, sq_bm);
                    startActivityForResult(intent, 101);

                }
                break;

            //选择所属社区
            case R.id.tv_sq:
                //需要对 已有的网格 社区 集合数据清空 避免重复添加
                wg.clear();
                sq.clear();
                tv_wg.setText("");

                if (tv_jd.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("所属街道不能为空", ModifyUserInfoActivity.this);
                    //如果街道编码为空，根据街道名称寻找街道编码 来匹配寻找下属社区！
                } else if (jd_bm != null && !tv_jd.getText().toString().trim().isEmpty()) {
                    final Map<String, String> maps = zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_SQ);
                    new AlertDialog.Builder(this).setItems(maps.values().toArray(new String[maps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_sq.setText(maps.values().toArray(new String[maps.values().size()])[which]);
                                    sq_bm = zdUtils.getMapKey(maps, tv_sq.getText().toString());
                                }
                            }).create().show();
                }
                break;


            //选择所属街道
            case R.id.tv_jd:
                //需要对 已有的网格 社区 集合数据清空
                sq.clear();
                wg.clear();
                tv_sq.setText("");
                tv_wg.setText("");

                final Map<String, String> jdMaps = zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_JD);
                new AlertDialog.Builder(this).setItems(jdMaps.values().toArray(new String[jdMaps.values().size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_jd.setText(jdMaps.values().toArray(new String[jdMaps.values().size()])[which]);
                                jd_bm = zdUtils.getMapKey(jdMaps, tv_jd.getText().toString());
                            }
                        }).create().show();
                break;


            //身份标识
            case R.id.tv_sfbs:
                UI.toast(ModifyUserInfoActivity.this, "身份标识不允许修改，请联系所属派出所管理员！");
//                if (ZDXXUtils.sfbs == null) {
//                    tv_sfbs.setText(R.string.msg_null);
//                } else {
//                    new AlertDialog.Builder(this).setItems(ZDXXUtils.sfbs.toArray(new String[ZDXXUtils.sfbs.size()]),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    tv_sfbs.setText(ZDXXUtils.sfbs.toArray(new String[ZDXXUtils.sfbs.size()])[which]);
//                                }
//                            }).create().show();
//                }
                break;           //身份标识
            case R.id.tv_fj:
                UI.toast(ModifyUserInfoActivity.this, "所属分局不允许修改！");
                break;

            //选择派出所
            case R.id.tv_pcs:
                //需要清空警务室信息，以免数据重复
                jwsOrgans.clear();
                tv_jws.setText("");
                jws_num = null;

                final Map<String, String> pcsMaps = zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_PCS, userInfo.getFjbm());
                new AlertDialog.Builder(this).setItems(pcsMaps.values().toArray(new String[pcsMaps.values().size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_pcs.setText(pcsMaps.values().toArray(new String[pcsMaps.values().size()])[which]);
                                pcs_num = zdUtils.getMapKey(pcsMaps, tv_pcs.getText().toString());
                            }
                        }).create().show();
                break;

            //警务室的选择
            case R.id.tv_jws:
                jwsOrgans.clear();
                tv_jws.setText("");
                if (tv_pcs.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("派出所不能为空", ModifyUserInfoActivity.this);
                } else if (pcs_num != null && !tv_pcs.getText().toString().trim().isEmpty()) {
                    final Map<String, String> jwsMaps = zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_JWS, pcs_num);
                    jwsMaps.put("11", "其他");
                    new AlertDialog.Builder(this).setItems(jwsMaps.values().toArray(new String[jwsMaps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_jws.setText(jwsMaps.values().toArray(new String[jwsMaps.values().size()])[which]);
                                    jws_num = zdUtils.getMapKey(jwsMaps, tv_jws.getText().toString());
                                }
                            }).create().show();
                }
                break;

        }
    }


    //同步上传图片到服务端
    void uploadImage() {
        final String path1 = simg_head.getImgPath();
        if (path1 != null) {
            filePath.add(path1);
            fileName.add(BaseUtil.getFileNameNoEx(path1));
            showLoadding("正在上传数据",ModifyUserInfoActivity.this);
            //上传图片
            ApiResource.userUpload(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        saveUserData();
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                    stopLoading();
                }

                @Override
                public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                    if (bytes != null) {
                        String result = new String(bytes);
                    }
                    stopLoading();
                    fileName.clear();
                    filePath.clear();
                    ToastyUtil.errorShort("上传图片失败");
                }
            });
        } else {
            saveUserData();
        }
    }

    /**
     * 保存用户信息
     */
    private void saveUserData() {
        //身份
        String sf = tv_sfbs.getText().toString().trim();
        //警号
        String policeno = et_jhnumber.getText().toString().trim();
        //街道
        String jd = tv_jd.getText().toString().trim();
        //社区
        String sq = tv_sq.getText().toString().trim();
        //网格
        String wg = tv_wg.getText().toString().trim();
        //json处理
        JSONObject jo = new JSONObject();
        //用户名-电话号码
        jo.put("username", tv_phone.getText().toString().trim());
        //密码
        jo.put("password", AppContext.instance.getLoginInfo().getPassword());
        //名字
        jo.put("name", et_username.getText().toString().trim());
        //id值
        jo.put("id", AppContext.instance.getLoginInfo().getId());
        //选填的部分如果为空 传  ""
        //派出所
        if (pcs_num != null) {
            jo.put("organcode", pcs_num);
        } else {
            jo.put("organcode", "");
        }
        jo.put("fjbm", userInfo.getFjbm());

        //警务室
        jo.put("jwscode", jws_num != "11" ? jws_num : "其他");

        //警号
        if (!policeno.isEmpty() && sf.equals("民警")) {
            jo.put("policeno", policeno);
        } else {
            jo.put("policeno", "");
        }

        jo.put("SSJD", jd.isEmpty() ? "" : jd); //街道
        jo.put("SSSQ", sq.isEmpty() ? "" : sq); //社区
        jo.put("SSWG", wg.isEmpty() ? "" : wg); //网格
        jo.put("accounttype", sf.isEmpty() ? "" : sf);//身份


        final String path1 = simg_head.getImgPath();
        if (path1 != null) {
            jo.put("TXZP", new BaseDataUtils().getNowYearAndMonthAndDay() + "/" + BaseUtil.getFileName(path1));//头像
        }


        String json = jo.toJSONString();
        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);

        ApiResource.modifyUserInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        String result = new String(bytes);
                        Message msg = new Message();
                        if (result.equals("\"1\"")) {
                            msg.what = 1;
                            AppContext.instance().cleanLoginInfo();
                        } else {
                            msg.what = 2;
                        }
                        mHandle.sendMessage(msg);
                    } else {
                        onFailure(i, headers, bytes, null);
                    }

                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 2;
                mHandle.sendMessage(msg);
            }
        });
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在提交用户资料更新");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    //修改资料成功将跳转登录页面
                    nextLogin();
                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("修改失败", ModifyUserInfoActivity.this);
                    break;

                default:
                    break;
            }
        }
    };

    private void nextLogin() {
        toast("修改资料成功，请重新登录!");
        AppContext.instance().cleanLoginInfo();
        Intent intent = new Intent(ModifyUserInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    //ChooseWangGeActivity.class  返回选择的网格信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        simg_head.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            data1 = data.getStringArrayListExtra(GeneralUtils.RegistWangGe);
            String[] ary = data1.toArray(new String[data1.size()]);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ary.length; i++) {
                sb.append(ary[i] + ",");
            }
            tv_wg.setText(sb.toString());
        }
    }


    private void setPoliceData() {
        if (userInfo.getAccounttype().equals("民警")) {
            ll_jh.setVisibility(View.VISIBLE);
            ll_sfz.setVisibility(View.GONE);
        } else {
            ll_sfz.setVisibility(View.VISIBLE);
            ll_jh.setVisibility(View.GONE);
            if (!StringUtils.isEmpty(userInfo.getSfzh())) {
                tv_sfzh.setText(userInfo.getSfzh());
            }
        }

        tv_phone.setText(userInfo.getUsername());
        et_username.setText(userInfo.getName());
        tv_fj.setText(userInfo.getFj());
        tv_pcs.setText(userInfo.getPcs());
        tv_jws.setText(userInfo.getJws());
        tv_sfbs.setText(userInfo.getAccounttype());
        et_jhnumber.setText(userInfo.getPoliceno());
        tv_jd.setText(userInfo.getSsjd());
        tv_sq.setText(userInfo.getSssq());
        tv_wg.setText(userInfo.getSswg());
        //如果街道编码为空，根据街道名称寻找街道编码 来匹配寻找下属社区！
        jd_bm = zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_JD, tv_jd.getText().toString().trim());
        //如果社区编码为空，根据社区名称寻找社区编码 来匹配寻找下属网格！
        sq_bm = zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_SQ, tv_sq.getText().toString().trim());
        //如果派出所编码为空，根据派出所名称寻找派出所编码 来匹配寻找下属警务室！
        pcs_num = userInfo.getOrgancode();
        //如果警务室编码为空，根据警务室名称寻找警务室编码
        if (userInfo.getJws().equals("其它") || userInfo.getJws().isEmpty()) {
            jws_num = "11";
        } else {
            jws_num = zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_JWS, userInfo.getJws());
        }
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return "个人信息修改";
    }


}