package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
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
 * 查看监控室信息
 * 包含核查部分
 */
public class WatchJKSInfoActivity extends BaseActivity2 implements View.OnClickListener {
    private  TextView tv_result;
    private Button menu_canel;
    private JKSInfoModle jKSInfo;
    public static WatchJKSInfoActivity intence;
    private Intent intent;
    private String WD;
    private String JD;
    private int networkType;
    private View layout;
    private ImageView iv_07;
    private String imagePath;
    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    private Bitmap bitmap;
    private static int image_width = 400;
    private static int image_height = 300;
    private LinearLayout ll_pass_yes, ll_pass_no;
    private TextView tv_resone;
    private EditText et_bz;
    private Button menu_sure;
    private File captureImage;
    int uploadCount = 0;
    int noImageCount = 0;
    private TextView tv_tips;
    private Dialog dialog;
    private String type[] = new String[]{"监控室不存在", "信息录入错误","监控室所在地址或坐标错误"};
    private boolean flag;
    @BindView(id = R.id.tv_remarks)
    TextView tv_BZ;
    //控件初始化
    @BindView(id = R.id.tv_wangge)
    TextView tv_wangge;
    @BindView(id = R.id.tv_collecion_pcs)
    TextView tv_SSPCS;
    @BindView(id = R.id.tv_phone)
    TextView tv_LXDH;
    @BindView(id = R.id.tv_trustee)
    TextView tv_ZRR;
    @BindView(id = R.id.tv_certificates_number)
    TextView tv_CZSGRS;
    @BindView(id = R.id.tv_collection_azdata_jks)
    TextView tv_AZSJ;
    @BindView(id = R.id.tv_camera_num_normal)
    TextView tv_ZCSYSXTSL;
    @BindView(id = R.id.tv_camera_num)
    TextView tv_SXTSL;
    @BindView(id = R.id.tv_block_number)
    TextView tv_LDBH;
    @BindView(id = R.id.tv_collection_position_shoudong)
    TextView tv_SDDW;
    @BindView(id = R.id.tv_collection_posioion_jks)
    TextView tv_JKSWZ;
    @BindView(id = R.id.tv_collection_gps_jks)
    TextView tv_collection_gps_jks;
    @BindView(id = R.id.tv_save_company)
    TextView tv_SPCZCS;
    @BindView(id = R.id.tv_ywfl)
    TextView tv_ywfl;
    @BindView(id = R.id.tv_name)
    TextView tv_JKSMC;
    @BindView(id = R.id.iv_03)
    ImageView iv_ZP3;
    @BindView(id = R.id.iv_02)
    ImageView iv_ZP2;
    @BindView(id = R.id.iv_01)
    ImageView iv_ZP1;
    @BindView(id = R.id.btn_editor,click = true)
    Button btn_editor;
    @BindView(id = R.id.btn_upl,click = true)
    Button btn_del;
    /** *核查不通过-原因提示布局*/
    @BindView(id = R.id.ll_top)
    LinearLayout ll_top;
    /** *核查不通过-原因显示*/
    @BindView(id = R.id.tv_check_faile)
    TextView tv_check_faile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_camear_room_info);
        AnnotateManager.initBindView(this);
        intence = this;
        initViews();
    }



    private void initViews() {
        //监控室信息
        jKSInfo = (JKSInfoModle) getIntent().getSerializableExtra(GeneralUtils.JKSInfo);
        //地图初始化
        networkType = ((AppContext) getApplication()).getNetworkType();

        /** *如果核查的备注说明中包含位置或者地址不对  提示去现场修改更新地址 */
        if (getIntent().getFlags()== GeneralUtils.CheckFaileJKSInfoActivity
                &&jKSInfo.getSHSBSM()!=null
                &&(jKSInfo.getSHSBSM().contains("位置")
                ||jKSInfo.getSHSBSM().contains("地址")
                ||jKSInfo.getSHSBSM().contains("坐标")
                ||jKSInfo.getSHSBYY().contains("地址")
                )){
            BaseUtil.showDialog("监控室位置录入不正确导致核查失败，请至采集点附近修改",WatchJKSInfoActivity.this);
        }
        //上传部分查看
        if (getIntent().getFlags() == GeneralUtils.DownJKSInfoActivity) {
            btn_del.setVisibility(View.GONE);
            //核查
        } else if (getIntent().getFlags() == GeneralUtils.CheckJKSInfo_flag) {
            /** *百度地图请求当前位置信息  */
            new GetLocation(this, handler).startLocation();
            btn_editor.setText(R.string.pass_yes);
            btn_del.setText(R.string.pass_no);
            getActionBar().setTitle("监控室信息核查");
            //监控室核查不通过布局显示
        }else if (getIntent().getFlags() == GeneralUtils.CheckFaileJKSInfoActivity){
            if (jKSInfo.getSHSBSM()!=null){
                tv_check_faile.setText(jKSInfo.getSHSBYY()+","+jKSInfo.getSHSBSM());
            }else {
                tv_check_faile.setText(jKSInfo.getSHSBYY());
            }

            btn_del.setVisibility(View.GONE);
            ll_top.setVisibility(View.VISIBLE);
        }
        initData();

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_editor:
                /** *已上传采集信息  暂未提供修改的功能 */
                if (getIntent().getFlags() == GeneralUtils.DownJKSInfoActivity) {
                    BaseUtil.showDialog("此功能开发中，敬请期待!", WatchJKSInfoActivity.this);
                    /** *核查失败的界面  点击跳转编辑界面 修改信息   */
                }else if (getIntent().getFlags() == GeneralUtils.CheckFaileJKSInfoActivity){
                    intent = new Intent(WatchJKSInfoActivity.this, JKSEditorActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(GeneralUtils.JKSInfo, jKSInfo);
                    intent.putExtras(mBundle);
                    intent.setFlags(GeneralUtils.CheckFaileJKSInfoActivity);
                    startActivity(intent);
                    /** *核查界面   下面2个按钮会显示 通过-不通过  */
                }else if (getIntent().getFlags() == GeneralUtils.CheckJKSInfo_flag) {
                    //弹出框加载
                    popDialogShow(getResources().getString(R.string.check_jks_tips_pass), getResources().getString(R.string.pass_yes));
                    //如果弹出框不为空则进行下一步
                    if (dialog!=null){
                        //通过 和不通过 布局隐藏
                        ll_pass_yes.setVisibility(View.VISIBLE);
                        ll_pass_no.setVisibility(View.GONE);

                        //通过需要上传照片一张
                        iv_07 = (ImageView) layout.findViewById(R.id.iv_07);
                        iv_07.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cameraPhoto();
                            }
                        });

                        menu_sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) { //点击上传
                                if (imagePath == null) {
                                    BaseUtil.showDialog("请上传监控室全景图！", WatchJKSInfoActivity.this);
                                } else {
                                    flag=true;//通过标识 为true
                                    //上传检测，是否联网->获取位置的信息->判断当前位置与采集点距离->弹出框显示
                                    checkIntent(flag);
                                }
                            }
                        });
                    }
                } else {   //缓存 查看页面的跳转
                    intent = new Intent(WatchJKSInfoActivity.this, JKSEditorActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(GeneralUtils.JKSInfo, jKSInfo);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
                break;


            //本地缓存数据：删除
            // 核查页面：不通过
            case R.id.btn_upl:
                if (getIntent().getFlags() == GeneralUtils.CheckJKSInfo_flag) {
                    popDialogShow(null, getResources().getString(R.string.pass_no));//弹出框加载
                    if (dialog!=null){//如果弹出框不为空则进行下一步
                        //通过界面隐藏
                        ll_pass_yes.setVisibility(View.GONE);
                        ll_pass_no.setVisibility(View.VISIBLE);
                        //备注
                        et_bz = (EditText) layout.findViewById(R.id.et_bz);
                        //选择不通过的理由
                        tv_resone = (TextView) layout.findViewById(R.id.tv_resone);
                        tv_resone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(WatchJKSInfoActivity.this).setItems(type,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tv_resone.setText(type[which]);
                                            }
                                        }).create().show();
                            }
                        });
                        menu_sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//提交不通过
                                if (tv_resone.getText().toString().trim().isEmpty()) {
                                    BaseUtil.showDialog("请选择不通过的原因", WatchJKSInfoActivity.this);
                                } else {
                                    //标识为false
                                    // flag=false;
                                    //上传检测，是否联网->获取位置的信息->判断当前位置与采集点距离->弹出框显示
                                    // checkIntent(flag);
                                    /***以上更改核查的机制   不需要判断距离*/
                                    uploadFaileThread();
                                }
                            }
                        });}
                } else {
                    DialogHelper.showAlertDialog(getString(R.string.msg_delete_confirm), WatchJKSInfoActivity.this, new DialogHelper.OnOptionClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, Object o) {
                            delCarInfo(which);
                            Intent intent1 = new Intent(WatchJKSInfoActivity.this, ManageJKSInfoActivity.class);
                            setResult(1);
                            startActivity(intent1);
                            ManageJKSInfoActivity.intence.finish();
                            WatchJKSInfoActivity.this.finish();
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
            BaseUtil.showDialog("当前无可用的网络连接，请选择附近网络良好处上传", WatchJKSInfoActivity.this);
        }else {
            //判断获得位置是否成功
            if (JD == null || WD == null) {
                BaseUtil.showDialog(getResources().getString(R.string.position_faile), WatchJKSInfoActivity.this);
                // 判断是否在500米范围内
            }else if (DistanceUtil.getDistance(new LatLng(Double.parseDouble(jKSInfo.getWD()),
                    Double.parseDouble(jKSInfo.getJD())), new LatLng(Double.parseDouble(WD),
                    Double.parseDouble(JD)))>500){
                BaseUtil.showDialog("请到采集点附近核查！", WatchJKSInfoActivity.this);
            }else if (flag==true){  //上传通过核查监控室信息
                UploadHCInfo();
//            }else if(flag==false){//上传不通过核查监控室信息
//                uploadFaileThread();
            }
        }
    }

    //弹出框的大小设置
    private void popDialogShow(String tips,String restul) {
        dialog=new popDialog(WatchJKSInfoActivity.this);
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
        //提示拍照
        tv_tips.setText(tips);
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

    //自定义dialog，核查弹出框
    class  popDialog extends Dialog {
        public popDialog(Context context) {
            super(context,R.style.iphone_progress_dialog);
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout=inflater.inflate(R.layout.check_jks_result, null);
            //通过界面
            ll_pass_yes=(LinearLayout)layout.findViewById(R.id.ll_pass_yes);
            //不通过界面
            ll_pass_no=(LinearLayout)layout.findViewById(R.id.ll_pass_no);
            //上传
            menu_sure=(Button)layout.findViewById(R.id.menu_sure);
            //取消
            menu_canel=(Button)layout.findViewById(R.id.menu_canel);
            tv_tips=(TextView)layout.findViewById(R.id.tv_tips);
            tv_result=(TextView)layout.findViewById(R.id.tv_result);
            //dialog添加视图
            setContentView(layout);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }


    //加载数据  查看
    private void initData() {
        if (getIntent().getFlags() == GeneralUtils.CheckJKSInfo_flag
                ||getIntent().getFlags() == GeneralUtils.DownJKSInfoActivity
                ||getIntent().getFlags() == GeneralUtils.CheckFaileJKSInfoActivity) {
            //转换的时间yy-MM-dd
            String date=ImageUtils.ImageLoad(jKSInfo.getSCSJ());
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/jks/" + date + "/" + jKSInfo.getZP1(), iv_ZP1);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/jks/" + date + "/" + jKSInfo.getZP2(), iv_ZP2);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/jks/" + date + "/" + jKSInfo.getZP3(), iv_ZP3);
        }else {
            if (jKSInfo.getZP1() != null) {
                iv_ZP1.setImageBitmap(ImageUtils.decodeFile(jKSInfo.getZP1()));
            }
            if (jKSInfo.getZP2() != null) {
                iv_ZP2.setImageBitmap(ImageUtils.decodeFile(jKSInfo.getZP2()));
            }
            if (jKSInfo.getZP3() != null) {
                iv_ZP3.setImageBitmap(ImageUtils.decodeFile(jKSInfo.getZP3()));
            }
        }
        tv_collection_gps_jks.setText("(" + jKSInfo.getWD() + "," + jKSInfo.getJD() + ")");
        tv_BZ.setText(jKSInfo.getBZ());
        tv_wangge.setText(jKSInfo.getSSWG());
        tv_LXDH.setText(jKSInfo.getLXDH());
        tv_ZRR.setText(jKSInfo.getZRR());
        tv_CZSGRS.setText(String.valueOf(jKSInfo.getCZSGRS()));
        tv_SXTSL.setText(String.valueOf(jKSInfo.getSXTSL()));
        tv_ZCSYSXTSL.setText(String.valueOf(jKSInfo.getZCSYSXTSL()));
        tv_LDBH.setText(jKSInfo.getLDBH());
        tv_JKSMC.setText(jKSInfo.getJKSMC());
        tv_SSPCS.setText(jKSInfo.getSSPCS());
        tv_AZSJ.setText(jKSInfo.getAZSJ());
        tv_JKSWZ.setText(jKSInfo.getJKSWZ());
        tv_SPCZCS.setText(jKSInfo.getSPCZCS());
        tv_ywfl.setText(zdUtils.getZdlbAndZdbmToZdz(GeneralUtils.XXCJ_JKSYWFL,jKSInfo.getJKSYWFLBM()));
    }

    //删除数据库信息
    public boolean delCarInfo(int position) {
        boolean mFlag = false;
        DBOperation dbo = new DBOperation(this);
        int id = jKSInfo.getId();
        mFlag = dbo.delCameraRoomInfo(id);
        dbo.clossDb();
        if (mFlag) {
            if (jKSInfo.getZP1() != null) {
                //删除本地图片1
                File plateImage = new File(jKSInfo.getZP1());
                if (plateImage.exists()) {
                    plateImage.delete();
                }
            }

            if (jKSInfo.getZP2() != null) {
                //删除本地图片2
                File plateImage = new File(jKSInfo.getZP2());
                if (plateImage.exists()) {
                    plateImage.delete();
                }
            }

            if (jKSInfo.getZP3() != null) {
                //删除本地图片3
                File plateImage = new File(jKSInfo.getZP3());
                if (plateImage.exists()) {
                    plateImage.delete();
                }
            }
        }
        return mFlag;
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
        jo.put("JKSBH", jKSInfo.getJKSBH());
        jo.put("SHYH", AppContext.instance.getLoginInfo().getUsername());
        jo.put("SHZT", "2");
        jo.put("SHSBYY", tv_resone.getText().toString().trim());
        jo.put("SHSBSM", et_bz.getText().toString().trim());
        String json = jo.toJSONString();

        ApiResource.CheckJKSInfo(json, new AsyncHttpResponseHandler() {
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

    //启一个线程  用于上传成功的信息
    private void UploadHCInfo() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                //上传通过 核查的图片
                uploadCheckImage();
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

    //同步上传审核监控室图片到服务端
    void uploadCheckImage() {
        uploadCount = 0;

        filePath.add(imagePath);
        fileName.add(BaseUtil.getFileNameNoEx(imagePath));

        //上传审核监控室图片
        ApiResource.uploadCameraRoomImage(filePath, fileName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                String result = new String(bytes);
                if (i == 200 && result != null && result.startsWith("true")) {
                    fileName.clear();
                    filePath.clear();
                    uploadCheckInfo();
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                }
            }
        });
    }

    //同步上传核查数据
    public void uploadCheckInfo() {
        //json处理
        JSONObject jo = new JSONObject();
        jo.put("SHZB_JD",JD);
        jo.put("SHZB_WD",WD);
        jo.put("JKSBH",jKSInfo.getJKSBH());
        jo.put("SHYH", AppContext.instance.getLoginInfo().getUsername());
        jo.put("SHZP", BaseUtil.getFileName(imagePath));
        jo.put("SHZT", 1);
        String json = jo.toJSONString();

        ApiResource.CheckJKSInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty() && result.startsWith("true")) {
                    //已上传记数+1；
                    uploadCount++;
                    mProgressDialog.incrementProgressBy(1);
                    mProgressDialog.cancel();

                    if (imagePath != null) {
                        //删除本地图片1
                        File plateImage = new File(imagePath);
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
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
                    mProgressDialog = new ProgressDialog(WatchJKSInfoActivity.this);
                    mProgressDialog.setTitle("上传前检测");
                    mProgressDialog.setMessage("检测网络情况及APP是否最新版本");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 1:// 上传进度条显示
                    int count = msg.arg1;
                    mProgressDialog = new ProgressDialog(WatchJKSInfoActivity.this);
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
                    new AlertDialog.Builder(WatchJKSInfoActivity.this)
                            .setTitle("提示")
                            .setMessage("上传核查信息成功")
                            .setNegativeButton ("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent=new Intent();
                                            setResult(GeneralUtils.WatchJKSInfo, intent);
                                            WatchJKSInfoActivity.this.finish();
                                            //  CheckJKSInfoActivity.intence.finish();
                                        }
                                    }).show();

                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", WatchJKSInfoActivity.this);
                    break;

                case -100:
                    location = (BDLocation) msg.obj;
                    WD="" + location.getLatitude();
                    JD="" + location.getLongitude();
                    break;

            }
        }};


    /**
     * 图片的相关处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST &&resultCode!=0&& imagePath != null) {
            getCaptureImageFile();
            showImgs(imagePath, "0");
        }
    }

    private void getCaptureImageFile() {
        Bitmap source_bitmap = ImageUtils.decodeFile(imagePath);
        if (source_bitmap.getWidth() > source_bitmap.getHeight()) {
            bitmap = ThumbnailUtils.extractThumbnail(source_bitmap,
                    image_width, image_height);
        } else {
            bitmap = ThumbnailUtils.extractThumbnail(source_bitmap,
                    image_height, image_width);
        }
        source_bitmap.recycle();
        bitmap.recycle();
    }

    /**
     * 展示选择的图片showMapView
     */
    private void showImgs(String path, String orientation) {
        Bitmap bit = ImageUtils.compressImageByPixel(path);
        int angle = 0;
        if (orientation != null && !"".equals(orientation)) {
            angle = Integer.parseInt(orientation);
        }
        if (angle != 0) {
            // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
            Matrix m = new Matrix();
            int width = bit.getWidth();
            int height = bit.getHeight();
            m.setRotate(angle); // 旋转angle度
            bit = Bitmap.createBitmap(bit, 0, 0, width, height, m, true);// 从新生成图片
        }
        iv_07.setImageBitmap(bit);
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.watch_jks);
    }
}