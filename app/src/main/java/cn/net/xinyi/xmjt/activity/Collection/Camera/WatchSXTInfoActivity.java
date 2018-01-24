package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.SXTInfoModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;
import cn.net.xinyi.xmjt.utils.ImageUtils;


/**
 * Created by hao.zhou on 2015/9/18.
 * 查看摄像头信息-
 * 包含核查部分
 */
public class WatchSXTInfoActivity extends BaseActivity implements View.OnClickListener {

    private  TextView tv_result;
    private SXTInfoModle sXTInfo;
    public static WatchSXTInfoActivity intence;
    private String WD;
    private String JD;
    private int networkType;
    private View layout;
    private String imagePath;
    private LinearLayout ll_pass_yes, ll_pass_no;
    private TextView tv_resone;
    private EditText et_bz;
    private Button menu_sure,menu_canel;
    int uploadCount = 0;
    //摄像头审核  不通过原因
    private String type[] = new String[]{"摄像头不存在", "信息录入错误","摄像头所在地址或坐标错误"};
    private Dialog dialog;
    private boolean flag;

    /**
     * 监控室名称  tv_JKSMC
     * 摄像头类型   tv_SXTLX
     * 是否正常    tv_SFZC
     * 摄像头位置   tv_SXTWZ
     * 摄像头位置坐标  tv_collection_gps_sxt
     * 手动定位   tv_SDDW
     * 楼栋编码   tv_LDBH
     * 摄像头方向  tv_SXTFX
     * */
    @BindView(id = R.id.tv_room_name)
    TextView tv_JKSMC;
    @BindView(id = R.id.tv_collection_type_sxt)
    TextView tv_SXTLX;
    @BindView(id = R.id.tv_collection_zc_sxt)
    TextView tv_SFZC;
    @BindView(id = R.id.tv_collection_posioion_sxt)
    TextView tv_SXTWZ;
    @BindView(id = R.id.tv_collection_gps_sxt)
    TextView tv_collection_gps_sxt;
    @BindView(id = R.id.tv_collection_position_sxt_shoudong)
    TextView tv_SDDW;
    @BindView(id = R.id.tv_block_number)
    TextView tv_LDBH;
    @BindView(id = R.id.tv_collection_fx_sxt)
    TextView tv_SXTFX;
    /**
     * 安装时间  tv_AZSJ
     * 摄像头类别  tv_SXTLB
     * 场所分类    tv_CSFL
     * ATM编号    tv_ATMBH
     * 视频存储期限  tv_SPBCQX
     * 所属环境    tv_SSHJ
     * **/
    @BindView(id = R.id.tv_collection_andata_sxt)
    TextView tv_AZSJ;
    @BindView(id = R.id.tv_collection_lb_sxt)
    TextView tv_SXTLB;
    @BindView(id = R.id.tv_collection_cs_sxt)
    TextView tv_CSFL;
    @BindView(id = R.id.tv_atm_number)
    TextView tv_ATMBH;
    @BindView(id = R.id.tv_collection_savedate_sxt)
    TextView tv_SPBCQX;
    @BindView(id = R.id.tv_collection_hj_sxt)
    TextView tv_SSHJ;

    /**
     * 所属场所  tv_SCCS
     * 建设单位  tv_JSDW
     * 备注      tv_BZ
     * */
    @BindView(id = R.id.tv_company)
    TextView tv_SCCS;
    @BindView(id = R.id.tv_construction_unit)
    TextView tv_JSDW;
    @BindView(id = R.id.tv_remarks)
    TextView tv_BZ;
    /**
     * 照片1.2.3*/
    @BindView(id = R.id.iv_06)
    ImageView iv_ZP3;
    @BindView(id = R.id.iv_05)
    ImageView iv_ZP2;
    @BindView(id = R.id.iv_04)
    ImageView iv_ZP1;

    /**
     * 编辑  btn_editor
     * 删除  btn_del
     * */
    @BindView(id = R.id.btn_editor,click = true)
    Button btn_editor;
    @BindView(id = R.id.btn_upl,click = true)
    Button btn_del;

    /** *核查不通过-原因提示布局*/
    @BindView(id = R.id.ll_top,click = false)
    LinearLayout ll_top;
    /** *核查不通过-原因显示*/
    @BindView(id = R.id.tv_check_faile,click = false)
    TextView tv_check_faile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_camera_info);
        AnnotateManager.initBindView(this);
        intence=this;
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("摄像头信息展示");
        getActionBar().setHomeButtonEnabled(true);
        //初始化组件
        initViews();
    }

    private void initViews() {
        //摄像头信息
        sXTInfo = (SXTInfoModle) getIntent().getSerializableExtra(GeneralUtils.SXTInfo);
        /** *如果核查的备注说明中包含位置或者地址不对  提示去现场修改更新地址 */
        if (getIntent().getFlags()== GeneralUtils.CheckFaileSXTInfoActivity
                &&sXTInfo.getSHSBSM()!=null
                &&(sXTInfo.getSHSBSM().contains("位置")
                ||sXTInfo.getSHSBSM().contains("地址")
                ||sXTInfo.getSHSBSM().contains("坐标")
                ||sXTInfo.getSHSBYY().contains("地址"))){
            BaseUtil.showDialog("摄像头位置录入不正确导致核查失败，请至采集点附近修改",WatchSXTInfoActivity.this);
        }
        /** *布局复用
         * 1.查看已上传的信息
         * 2.核查已上传的信息
         * 3.核查失败的上传信息查看*/
        /** *已经上传的摄像头列表     隐藏底部删除按钮*/
        if (getIntent().getFlags()== GeneralUtils.DownSXTInfoActivity){
            btn_del.setVisibility(View.GONE);
            /** *核查摄像头信息  */
        }else if (getIntent().getFlags()== GeneralUtils.CheckSXTInfo_flag){
            /** *百度地图请求当前位置信息  */
            new GetLocation(this, handler).startLocation();
            btn_del.setText("不通过");
            btn_editor.setText("通过");
            getActionBar().setTitle("摄像头信息核查");
            /** *查看核查失败的摄像头信息  */
        }else if (getIntent().getFlags() == GeneralUtils.CheckFaileSXTInfoActivity){
            if (sXTInfo.getSHSBSM()!=null){
                tv_check_faile.setText(sXTInfo.getSHSBYY()+","+sXTInfo.getSHSBSM());
            }else {
                tv_check_faile.setText(sXTInfo.getSHSBYY());
            }
            btn_del.setVisibility(View.GONE);
            ll_top.setVisibility(View.VISIBLE);
        }
        //加载数据
        initData();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_editor:
                /** *布局复用
                 * 1.查看已上传的信息
                 * 2.核查失败的上传信息查看  跳转编辑修改界面  Flage 做标识
                 * 3.核查已上传的信息  跳转编辑页面  Flage 做标识
                 *      点击通过或不通过 弹出diaolo  注明原因
                 * */
                /** *已上传采集信息  暂未提供修改的功能 */
                if (getIntent().getFlags()== GeneralUtils.DownSXTInfoActivity) {
                    BaseUtil.showDialog("此功能开发中，敬请期待!", WatchSXTInfoActivity.this);
                    /** *核查失败的界面  点击跳转编辑界面 修改信息   */
                }else if (getIntent().getFlags()== GeneralUtils.CheckFaileSXTInfoActivity){
                    Intent intent = new Intent(WatchSXTInfoActivity.this, SXTEditorActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(GeneralUtils.SXTInfo, sXTInfo);
                    intent.putExtras(mBundle);
                    intent.setFlags(GeneralUtils.CheckFaileSXTInfoActivity);
                    startActivity(intent);
                    /** *核查界面   下面2个按钮会显示 通过-不通过  */
                }else if (getIntent().getFlags() == GeneralUtils.CheckSXTInfo_flag){
                    /** *弹出框加载   传想对应的文字提示*/
                    popDialogShow(getResources().getString(R.string.check_jks_tips_pass),getResources().getString(R.string.pass_yes));
                    /** *如果弹出框不为空则进行下一步*/
                    if (dialog!=null){
                        /** *核查通过  布局不通过隐藏*/
                        ll_pass_yes.setVisibility(View.VISIBLE);
                        ll_pass_no.setVisibility(View.GONE);

                        tv_result.setText(R.string.pass_yes);
                        menu_sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                flag=true;//通过标识 为true
                                //上传检测，是否联网->获取位置的信息->判断当前位置与采集点距离->弹出框显示
                                checkIntent(flag);
                            }
                        });
                    }
                }else {
                    /**
                     *本地缓存  跳转编辑页面  Bundle  摄像头信息
                     */
                    Intent intent = new Intent(WatchSXTInfoActivity.this, SXTEditorActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(GeneralUtils.SXTInfo, sXTInfo);
                    //用于查询监控室下属摄像头
                    // mBundle.putString(GeneralUtils.JKSBH, getIntent().getStringExtra(GeneralUtils.JKSBH));
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
                break;

            //本地缓存数据：删除
            // 核查页面：不通过
            case R.id.btn_upl:
                // 核查-不通过
                if (getIntent().getFlags() == GeneralUtils.CheckSXTInfo_flag){
                    popDialogShow(null, getResources().getString(R.string.pass_no));//弹出框加载
                    //如果弹出框不为空则进行下一步
                    if (dialog!=null){
                        //不通过 -通过布局隐藏
                        ll_pass_yes.setVisibility(View.GONE);
                        ll_pass_no.setVisibility(View.VISIBLE);
                        //备注
                        et_bz = (EditText) layout.findViewById(R.id.et_bz);
                        //选择不通过理由
                        tv_resone = (TextView) layout.findViewById(R.id.tv_resone);
                        tv_resone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(WatchSXTInfoActivity.this);
                                builder.setItems(type,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tv_resone.setText(type[which]);
                                            }
                                        });
                                builder.create().show();
                            }
                        });
                        //提交不通过
                        menu_sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (tv_resone.getText().toString().trim().isEmpty()) {
                                    BaseUtil.showDialog("请选择不通过的原因", WatchSXTInfoActivity.this);
                                } else {
                                    //标识为false
                                    flag=false;
                                    //上传检测，是否联网->获取位置的信息->判断当前位置与采集点距离->弹出框显示
                                    checkIntent(flag);
                                }
                            }
                        });
                    }
                }else {
                    DialogHelper.showAlertDialog(getResources().getString(R.string.msg_delete_confirm),WatchSXTInfoActivity.this, new DialogHelper.OnOptionClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, Object o) {
                            delCarInfo(which);
                            //ManageJKSInfoActivity 地图 直接跳转摄像头编辑页面 进行标识
                            if (getIntent().getFlags()== GeneralUtils.JKSListActivity) {
                                Intent intent1 = new Intent(WatchSXTInfoActivity.this, ManageJKSInfoActivity.class);
                                setResult(1);
                                startActivity(intent1);
                                ManageJKSInfoActivity.intence.finish();
                                WatchSXTInfoActivity.this.finish();
                            } else {
                                Intent intent1 = new Intent(WatchSXTInfoActivity.this, ManageSXTInfoActivity.class);
                                setResult(1);
                                startActivity(intent1);
                                ManageSXTInfoActivity.intence.finish();
                                WatchSXTInfoActivity.this.finish();
                            }
                        }
                    });
                    break;
                }
        }
    }

    //核查上传前检测，是否联网->获取位置的信息->判断当前位置与采集点距离->弹出框显示
    private void checkIntent(boolean flag) {
        //检测是否连接网络
        networkType = ((AppContext) getApplication()).getNetworkType();
        if (networkType == 0) {
            BaseUtil.showDialog("当前无可用的网络连接，请选择附近网络良好处上传", WatchSXTInfoActivity.this);
        }else {
            //判断获得位置是否成功
            if (JD == null || WD == null) {
                BaseUtil.showDialog(getResources().getString(R.string.position_faile), WatchSXTInfoActivity.this);
            }else if (flag==true){  //上传通过核查摄像头信息
                //  UploadHCInfo();
                uploadSuccessThread();
            }else if(flag==false){//上传不通过核查监控室信息
                uploadFaileThread();
            }
        }
    }

    //弹出框的大小设置
    private void popDialogShow(String tips,String restul) {
        dialog=new popDialog(WatchSXTInfoActivity.this);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.CENTER);
        /*
           * 将对话框的大小按屏幕大小的百分比设置
              */
        WindowManager m = getWindowManager();
        // 获取屏幕宽、高用
        Display d = m.getDefaultDisplay();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        // 高度的设置
        p.height =  WindowManager.LayoutParams.WRAP_CONTENT;
        // 宽度设置为屏幕的0.65
        p.width = (int) (d.getHeight() * 0.5);
        dialogWindow.setAttributes(p);
        dialog.setCancelable(false);
        dialog.show();
        //标题 核查通过，不通过
        tv_result.setText(restul);
        //取消监听
        menu_canel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    class  popDialog extends Dialog {
        public popDialog(Context context) {
            super(context,R.style.iphone_progress_dialog);
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout=inflater.inflate(R.layout.check_sxt_result, null);
            ll_pass_yes=(LinearLayout)layout.findViewById(R.id.ll_pass_yes);
            ll_pass_no=(LinearLayout)layout.findViewById(R.id.ll_pass_no);
            menu_sure=(Button)layout.findViewById(R.id.menu_sure);
            menu_canel=(Button)layout.findViewById(R.id.menu_canel);
            tv_result=(TextView)layout.findViewById(R.id.tv_result);
            //dialog添加视图
            setContentView(layout);
        }
    }

    //删除数据库信息
    public boolean delCarInfo(int position) {
        boolean mFlag = false;
        DBOperation dbo = new DBOperation(this);
        int id = sXTInfo.getId();
        mFlag = dbo.delCameraInfo(id);
        dbo.clossDb();
        if (mFlag) {
            if (sXTInfo.getZP1() != null) {
                //删除本地图片1
                File plateImage = new File(sXTInfo.getZP1());
                if (plateImage.exists()) {
                    plateImage.delete();
                }
            }
            if (sXTInfo.getZP2() != null) {
                //删除本地图片2
                File plateImage = new File(sXTInfo.getZP2());
                if (plateImage.exists()) {
                    plateImage.delete();
                }
            }

            if (sXTInfo.getZP3() != null) {
                //删除本地图片3
                File plateImage = new File(sXTInfo.getZP3());
                if (plateImage.exists()) {
                    plateImage.delete();
                }
            }
        }
        return mFlag;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
            System.gc();
    }

    //初始化页面布局
    private void initData() {
        if (getIntent().getFlags() == GeneralUtils.CheckSXTInfo_flag
                ||getIntent().getFlags() == GeneralUtils.DownSXTInfoActivity
                ||getIntent().getFlags() == GeneralUtils.CheckFaileSXTInfoActivity){
            //转换的时间yy-MM-dd
            String date=ImageUtils.ImageLoad(sXTInfo.getSCSJ());
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/sxt/" + date + "/" + sXTInfo.getZP1(), iv_ZP1);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/sxt/" + date + "/" + sXTInfo.getZP2(), iv_ZP2);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/sxt/" + date + "/" + sXTInfo.getZP3(), iv_ZP3);
        }else {
            if (sXTInfo.getZP1() != null) {
                iv_ZP1.setImageBitmap(ImageUtils.decodeFile(sXTInfo.getZP1()));
            }
            if (sXTInfo.getZP2() != null) {
                iv_ZP2.setImageBitmap(ImageUtils.decodeFile(sXTInfo.getZP2()));
            }
            if (sXTInfo.getZP3() != null) {
                iv_ZP3.setImageBitmap(ImageUtils.decodeFile(sXTInfo.getZP3()));
            }
        }
        tv_collection_gps_sxt.setText("(" + sXTInfo.getWD() + "," + sXTInfo.getJD() + ")");
        tv_SXTLX.setText(sXTInfo.getSXTLX());
        tv_SFZC.setText(sXTInfo.getSFZC());
        tv_SXTWZ.setText(sXTInfo.getSXTWZ());
        tv_SXTFX.setText(sXTInfo.getSXTFX());
        tv_AZSJ.setText(sXTInfo.getAZSJ());
        tv_CSFL.setText(sXTInfo.getCSFL());
        tv_SSHJ.setText(sXTInfo.getSSHJ());
        tv_SXTLB.setText(sXTInfo.getSXTLB());
        tv_SPBCQX.setText(sXTInfo.getSPBCQX());
        tv_JKSMC.setText(sXTInfo.getJKSMC());
        tv_LDBH.setText(sXTInfo.getLDBH());
        tv_SCCS.setText(sXTInfo.getSCCS());
        tv_JSDW.setText(sXTInfo.getJSDW());
        tv_BZ.setText(sXTInfo.getBZ());
        tv_ATMBH.setText(sXTInfo.getATMBH());

    }


    //启一个线程  用于上传失败的信息
    private void uploadFaileThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                checkFaileInfo();
                int result = uploadCount;
                if (result > 0) {
                    msg = new Message();
                    msg.what = 2;
                    msg.arg1 = result;
                    handler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //上传 不通过信息
    private void checkFaileInfo() {
        JSONObject jo = new JSONObject();
        jo.put("SHZB_JD", JD);
        jo.put("SHZB_WD", WD);
        jo.put("SXTBH",sXTInfo.getSXTBH());
        jo.put("SHYH", AppContext.instance.getLoginInfo().getUsername());
        jo.put("SHZT", "2");
        jo.put("SHSBYY", tv_resone.getText().toString().trim());
        jo.put("SHSBSM", et_bz.getText().toString().trim());
        String json = jo.toJSONString();
        ApiResource.CheckSXTInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    uploadCount = 0;
                    if (!result.isEmpty() && result.startsWith("true")) {
                        //已上传记数+1；
                        uploadCount++;
                        mProgressDialog.incrementProgressBy(1);
                        mProgressDialog.cancel();

                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);

                    }
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        });
    }


    //启一个线程  用于上传失败的信息
    private void uploadSuccessThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                CheckScuessInfo();
                int result = uploadCount;

                if (result > 0) {
                    msg = new Message();
                    msg.what = 2;
                    msg.arg1 = result;
                    handler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //同步上传核查数据
    public void CheckScuessInfo() {
        //json处理
        JSONObject jo = new JSONObject();
        jo.put("SHZB_JD",JD);
        jo.put("SHZB_WD",WD);
        jo.put("SXTBH",sXTInfo.getSXTBH());
        jo.put("SHYH", AppContext.instance.getLoginInfo().getUsername());
        jo.put("SHZP", BaseUtil.getFileName(imagePath));
        jo.put("SHZT", 1);
        String json = jo.toJSONString();

        ApiResource.CheckSXTInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty() && result.startsWith("true")) {
                    //已上传记数+1；
                    uploadCount++;
                    mProgressDialog.incrementProgressBy(1);
                    mProgressDialog.cancel();
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private ProgressDialog mProgressDialog = null;
    private BDLocation location;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://上传前检测
                    mProgressDialog = new ProgressDialog(WatchSXTInfoActivity.this);
                    mProgressDialog.setTitle("上传前检测");
                    mProgressDialog.setMessage("检测网络情况及APP是否最新版本");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 1:// 上传进度条显示
                    int count = msg.arg1;
                    mProgressDialog = new ProgressDialog(WatchSXTInfoActivity.this);
                    mProgressDialog.setProgress(count);
                    mProgressDialog.setMax(count);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("信息上传中");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 2:// 上传之后
                    mProgressDialog.cancel();
                    dialog.dismiss();
                    new AlertDialog.Builder(WatchSXTInfoActivity.this)
                            .setTitle("提示")
                            .setMessage("上传核查信息成功")
                            .setNegativeButton ("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent=new Intent();
                                            setResult(GeneralUtils.WatchSXTInfo, intent);
                                            WatchSXTInfoActivity.this.finish();
                                        }
                                    }).show();
                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", WatchSXTInfoActivity.this);
                    break;

                case -100:
                    location = (BDLocation) msg.obj;
                    WD="" + location.getLatitude();
                    JD="" + location.getLongitude();
                    break;

            }
        }};

    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 在2级菜单用
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        // super.dispatchKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}