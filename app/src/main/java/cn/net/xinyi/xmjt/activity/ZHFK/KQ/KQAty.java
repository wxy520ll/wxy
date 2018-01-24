package cn.net.xinyi.xmjt.activity.ZHFK.KQ;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseWithLocActivity;
import cn.net.xinyi.xmjt.model.KQModle;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/4/7.
 */
public class KQAty extends BaseWithLocActivity implements View.OnClickListener{
    /**
     * 年月日
     **/
    @BindView(id = R.id.tv_year)
    private TextView tv_year;
    /**
     *时分秒
     **/
    @BindView(id = R.id.tv_date)
    private TextView  tv_date;
    /**
     * 星期
     **/
    @BindView(id = R.id.tv_week)
    private TextView tv_week;
    /**
     * 地址
     **/
    @BindView(id = R.id.et_dz)
    private EditText et_dz;
    /**
     *坐标
     **/
    @BindView(id = R.id.tv_zb)
    private TextView tv_zb;
    /**
     *手动定位
     **/
    @BindView(id = R.id.tv_sddw, click = true)
    private TextView tv_sddw;
    /**
     * 照片1
     **/
    @BindView(id = R.id.iv_zp1, click = true)
    private ImageView iv_zp1;
    /**
     * 上班
     **/
    @BindView(id = R.id.btn_sb, click = true)
    private Button btn_sb;
    /**
     * 下班
     **/
    @BindView(id = R.id.btn_xb, click = true)
    private Button btn_xb;
    /**
     * listview
     **/
    @BindView(id = R.id.ll_all)
    private LinearLayout ll_all;
    /**
     * 营业执照
     **/
    @BindView(id = R.id.iv_yyzz, click = true)
    private ImageView iv_yyzz;
    private Thread myThread = null;
    /**纬度**/
    private double bWd;
    /**经度**/
    private double bJD;
    /***定位类型*/
    private int map_type;
    private boolean flag = false;
    /***ImageView*/
    private int i_flag;
    /***数据库存储地址**/
    private String path1;
    private String w_start="上班";
    private String w_end="下班";
    private LocaionInfo mLocation;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_kq);
        /***控件绑定**/
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getString(R.string.kq));
        getActionBar().setHomeButtonEnabled(true);
        initData();

    }

    private void initData() {
        /**系统时间?**/
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        //转换 年月日
        format=new SimpleDateFormat("yyyy年MM月dd日");
        tv_year.setText(format.format(date));
        //转换 星期
        format=new SimpleDateFormat("EEEE");
        tv_week.setText(format.format(date));
        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sddw:
                Intent intent=new Intent(this,PickupMapActivity.class);
                startActivityForResult(intent,PickupMapActivity.MAP_PICK_UP);
                break;

            case R.id.iv_zp1:
                cameraPhoto();
                break;

            case R.id.btn_sb:
                setData(w_start);
                break;

            case R.id.btn_xb:
                setData(w_end);
                break;
        }
    }

    private void setData(String text) {
        if (path1==null){
            BaseUtil.showDialog("请上传一张图片",this);
        } else  if (TextUtils.isEmpty(et_dz.getText().toString())) {
            BaseUtil.showDialog("当前地址不能为空", this);
        } else {
            KQModle kqModle=new KQModle();
            kqModle.setYEAR(tv_year.getText().toString());
            kqModle.setDATA(tv_date.getText().toString());
            kqModle.setWEEK(tv_week.getText().toString());
            kqModle.setDZ(et_dz.getText().toString());
            kqModle.setLX(text);
            kqModle.setJD(bJD);
            kqModle.setWD(bWd);
            kqModle.setIV_ZP(path1);
            kqModle.setLOCTYPE(map_type);
            kqModle.setCJSJ(new BaseDataUtils().getNowData());
            //开始上传考勤信息
            startUploadThread(kqModle);
        }
    }


    private void startUploadThread(final KQModle Info) {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadImage(Info);
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

    //同步上传图片到服务端
    void uploadImage(final KQModle mInfo) {
        if (mInfo.getIV_ZP() != null) {
            String path= mInfo.getIV_ZP();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }

        //上传图片
        ApiResource.uploadKQImage(filePath, fileName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                String result = new String(bytes);
                if (i == 200 && result != null && result.startsWith("true")) {
                    fileName.clear();
                    filePath.clear();
                    uploadInfo(mInfo);
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

    //同步上传采集数据到服务端
    public void uploadInfo(final KQModle info) {
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("id");
            jo.remove("iV_ZP");
            jo.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
            jo.put("CJDW", AppContext.instance.getLoginInfo().getOrgancode());
            if (info.getIV_ZP() != null) {
                jo.put("iV_ZP", BaseUtil.getFileName(info.getIV_ZP()));
            }

        }

        String json = jo.toJSONString();

        ApiResource.addKQInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                uploadCount++;
                if (!result.isEmpty() && result.startsWith("true")) {
                    if (info.getIV_ZP() != null) {
                        File plateImage = new File(info.getIV_ZP());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }

                    mProgressDialog.incrementProgressBy(1);
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


    private ProgressDialog mProgressDialog;
    private int uploadCount=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    mProgressDialog = new ProgressDialog(KQAty.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("上传");
                    mProgressDialog.setMessage("数据正在上传中...");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 2:// 上传之后
                    mProgressDialog.cancel();
                    showToast("考勤成功！");
                    showActivity( KQListAty.class);
                    KQAty.this.finish();
                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", KQAty.this);
                    break;

                case 4://上传检测完成
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到当前APP 版本过低，请回到民警通主菜单点击【系统设置】-【APP 更新】，按提示升级APP 后重新上传！", KQAty.this);
                    }
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0
                && imagePath != null) {
            path1 = imagePath;
            iv_zp1.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
        }else  if (PickupMapActivity.MAP_PICK_UP == requestCode) { //地图选点
            if (Activity.RESULT_OK == resultCode) {
                LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
                onReceiveLoc(info, true, null);
            }
        }
    }



    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = (hours >= 10 ? hours:"0"+hours) + ":"+ (minutes >= 10 ? minutes:"0"+minutes) +":" + (seconds >= 10 ? seconds:"0"+seconds) ;
                    tv_date.setText(curTime);
                }catch(Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try{
                    doWork();
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }


    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess) {
            mLocation = location;
            /**获得纬度信息**/
            bWd=mLocation.getLat();
            /**获得经度信息信息**/
            bJD=mLocation.getLongt();
            /**获得地址信息**/
            et_dz.setText(mLocation.getAddress());
            /**百度坐标，经纬度展现**/
            tv_zb.setText("(" + bWd + "," + bJD + ")");
            map_type = mLocation.getLoctype();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            /**列表**/
            case R.id.action_list:
                showActivity(KQListAty.class,1);
                break;
            default:
                break;
        }
        return true;
    }
}