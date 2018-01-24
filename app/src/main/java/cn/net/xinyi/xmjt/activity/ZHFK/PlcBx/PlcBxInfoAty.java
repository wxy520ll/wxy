package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseWithLocActivity;
import cn.net.xinyi.xmjt.model.DutyFlightRulesModle;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.PoliceBoxModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by studyjun on 2016/4/20.
 */
public class PlcBxInfoAty extends BaseWithLocActivity implements View.OnClickListener {


    //岗亭类型
    @BindView(id = R.id.tv_type,click = true)
    TextView mPlcBxType;

    //街道
    @BindView(id = R.id.et_street,click = true)
    TextView mStreet;

    //统一编号
    @BindView(id = R.id.et_unionNo)
    TextView mUnionNo;


    //建设需求
    @BindView(id = R.id.et_demand)
    TextView mDemand;


    //领头部门
    @BindView(id = R.id.et_initiator)
    TextView mInitiator;


    //所属派出所
    @BindView(id = R.id.et_police_station)
    Spinner mPlcSttn;


    //完成时间
    @BindView(id = R.id.et_finish_time,click = false)
    TextView mFinishTime;


    //值守单位
    @BindView(id = R.id.et_watchOver_department,click = true )
    EditText watchOverDprtmt;

    //联系人
    @BindView(id = R.id.et_linkman)
    EditText mLinkMan;

    //手机号码
    @BindView(id = R.id.et_phoneno)
    EditText mPhoneNo;

    //手持终端类型
    @BindView(id = R.id.et_interphone_type,click = true)
    TextView mInterphoneType;


    //勤务模式
    @BindView(id = R.id.tv_worktime,click = true)
    TextView mWorkTime;

    //手持终端号码
    @BindView(id = R.id.et_interphoneno)
    TextView mInterPhoneNo;

    //手动定位
    @BindView(id = R.id.tv_sddw, click = true)
    TextView mManualLoc;

    //手动定位
    @BindView(id = R.id.tv_hqwz, click = true)
    TextView tv_hqwz;

    //地址
    @BindView(id = R.id.tv_zb, click = true)
    EditText mAddress;

    //经纬度
    @BindView(id = R.id.et_latLngt, click = true)
    TextView mTvLatLngt;

    //更新
    @BindView(id = R.id.btn_plc_bx_update, click = true)
    Button mUpdate;

    //删除
    @BindView(id = R.id.btn_del, click = true)
    Button btn_del;

    //照片1
    @BindView(id = R.id.iv_zp1, click = true)
    ImageView iv_zp1;

    //照片2
    @BindView(id = R.id.iv_zp2, click = true)
    ImageView iv_zp2;

    LocaionInfo mLocation;

    PoliceBoxModle mPb;
    ArrayAdapter mPlcSttndapter;
    ArrayAdapter watchOverDprtmtAdapter;
    /***ImageView拍照标识*/
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private String path2;

    private Map<String,String> mapRusles=new HashMap<String, String>();
    private List<String> sRuleList=new ArrayList<String>();
    private Map<String, String> pcsMaps;
    private Map<String, String> jdMaps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_plc_bx_info);
        AnnotateManager.initBindView(this);
        TypeUtils.compatibleWithJavaBean=true; //开启json大写
        pcsMaps=zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_PCS,userInfo.getFjbm());
        jdMaps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_JD);
        getPlateData();
        getRulesData();
    }

    private void getPlateData() {
        JSONObject jo=new JSONObject();
        jo.put("ID",getIntent().getStringExtra("data"));
        jo.put("ISDISPLAYDEL",1);
        String json=jo.toJSONString();
        ApiResource.getGtList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    List<PoliceBoxModle>  mPbs = JSON.parseArray(result,PoliceBoxModle.class );
                    if (mPbs != null&&mPbs.size()>0) {
                        mPb=mPbs.get(0);
                        bindData(mPb);
                    }
                } catch (JSONException e) {
                    onFailure(i,headers,bytes,e);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }


    private void bindData(PoliceBoxModle pb) {

        mPlcSttndapter = new ArrayAdapter(this,R.layout.simple_spinner_dropdown_item);
        mPlcSttndapter.addAll(pcsMaps.values());

        mPlcSttn.setAdapter(mPlcSttndapter);

        mPlcBxType.setText(pb.getTYPE());
        mStreet.setText(pb.getSTREET());
        mUnionNo.setText(pb.getUNIFIEDNO());
        mDemand.setText(pb.getDEMAND());
        mInitiator.setText(pb.getINITIATOR());

        for (int i=0;i<mPlcSttndapter.getCount();i++){
            if(mPlcSttndapter.getItem(i).equals(pb.getPOLICESTATION())){
                mPlcSttn.setSelection(i);
                break;
            }
        }
        watchOverDprtmt.setText(pb.getWATCHOVERUNIT());
        mFinishTime.setText(pb.getfINISHTIME());
        mLinkMan.setText(pb.getLINKMAN());
        mPhoneNo.setText(pb.getPHONENO());
        mInterphoneType.setText(pb.getINTERPHONETYPE());
        mWorkTime.setText(pb.getFRNAME());
        mInterPhoneNo.setText(pb.getINTERPHONENO());
        mAddress.setText(pb.getADDRESS());
        if (pb.getLAT()>0)
            mTvLatLngt.setText("("+pb.getLAT()+","+pb.getLNGT()+")");

        if (pb.getIMG1()!=null){
            iv_zp1.setImageResource(R.drawable.loading_pic);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/gt/" + pb.getIMG1(), iv_zp1);
        }
        if (pb.getIMG2()!=null){
            iv_zp2.setImageResource(R.drawable.loading_pic);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/gt/" + pb.getIMG2(), iv_zp2);
        }
    }

    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess) {
            mLocation = location;
            mTvLatLngt.setText("("+location.getLat()+","+location.getLongt()+")");
            mPb.setLAT(location.getLat());
            mPb.setLNGT(location.getLongt());
        }
    }

    @Override
    public void onClick(View v) {
        if (BaseDataUtils.isFastClick()){

        }else {
            switch (v.getId()) {
                //岗亭类别
                case R.id.tv_type:
                    new BaseDataUtils().showAlertDialog(this, getResources().getStringArray(R.array.duty_plate_type),mPlcBxType);
                    break;
                case R.id.et_street:

                    new BaseDataUtils().showAlertDialog(this,jdMaps.values(),mStreet);
                    break;
                case R.id.et_interphone_type:
                    new BaseDataUtils().showAlertDialog(this,new String[]{"手持","固定"},mInterphoneType);
                    break;
                case R.id.tv_worktime:
                    if (((AppContext) getApplication()).getNetworkType() == 0) {
                        toast(getString(R.string.network_not_connected));
                    } else if (mapRusles.size()<=0&&sRuleList.size()<=0){
                        getRulesData();
                    }else {
                        BaseDataUtils.showAlertDialog(this, sRuleList,mWorkTime);
                    }
                    break;
                case R.id.tv_sddw:
                    Intent intent=new Intent(PlcBxInfoAty.this,PickupMapActivity.class);
                    startActivityForResult(intent,PickupMapActivity.MAP_PICK_UP);
                    break;

                case R.id.tv_hqwz:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("获取位置").setMessage("请确定您当前所在位置为："+mUnionNo.getText().toString()).setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loc();//获取定位
                        }
                    }).show();
                    break;

                case R.id.et_finish_time:
                    DatePickerDialog dialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mFinishTime.setText(year+"-"+monthOfYear+1+"-"+dayOfMonth);
                        }
                    }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                    break;
                case R.id.btn_plc_bx_update:
                    if (!checkForm())
                        return;
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setTitle("提交").setMessage("确定当前所在位置为"+mUnionNo.getText().toString()+"，并提交修改后的岗亭信息！").setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (path1==null && path2==null){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        postUpdate();
                                    }
                                }).start();
                            }else {
                                //开始上传采集信息
                                startUploadThread();
                            }
                        }
                    }).show();

                    break;
                //照片1
                case R.id.iv_zp1:
                    i_flag = 1;
                    cameraPhoto();
                    break;
                //照片2
                case R.id.iv_zp2:
                    i_flag = 2;
                    cameraPhoto();
                    break;

                case R.id.et_watchOver_department:
                    new BaseDataUtils().showAlertDialog(this,pcsMaps.values(),watchOverDprtmt);
                    break;

                case R.id.btn_del:
                    new AlertDialog.Builder(this).setTitle(getString(R.string.tips))
                            .setMessage("确定删除岗亭信息！")
                            .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delInfo(mPb.getID());
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                    break;
            }
        }
    }
    private String getReques() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("DEPT_ID", userInfo.getOrgancode());
        requestJson.put("RULE_DUTY", "02");
        requestJson.put("BEATS_SET", "01");//过滤未设置勤务班次的规则
        return requestJson.toJSONString();
    }

    private void getRulesData() {
        showLoadding();
        ApiResource.getDutyFlightReulsList(getReques(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if (i == 200 && result.length()>3){
                    try {
                        List<DutyFlightRulesModle> Ruleslist = JSON.parseArray(result, DutyFlightRulesModle.class);
                        if (Ruleslist.size()>0){
                            for (DutyFlightRulesModle info:Ruleslist){
                                sRuleList.add(info.getRULE_NAME());
                                mapRusles.put(info.getRULE_NAME(),info.getID());
                            }
                        }
                    } catch (JSONException e){
                        toast("获取数据失败");
                    }
                }else {
                    toast(getString(R.string.duty_flight_data));
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("获取数据失败");
            }
        });
    }

    /**
     * 物理删除岗亭
     * @string id 数据ID
     * @string DELRY 执行删除操作人员
     * **/
    private void delInfo(int id) {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ID",id);
        jo.put("DELRY",userInfo.getUsername());
        ApiResource.delGTInfo(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    /**弹出框删除成功**/
                    setDialog(PlcBxInfoAty.this,getString(R.string.del_yes));
                }else {
                    toast("删除岗亭失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                // toast(throwable.getMessage());
                toast("删除岗亭失败！");
                stopLoading();
            }
        });
    }


    private void startUploadThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                // 将数据上传到服务器
                uploadImage();
            }
        }.start();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding();
                    break;
                case 2:// 更新成功
                    stopLoading();
                    /**弹出框更新成功**/
                    setDialog(PlcBxInfoAty.this,"更新成功");
                    break;
                case 3:// 更新失败
                    toast("更新失败");
                    stopLoading();
                    break;
            }
        }
    };

    //同步上传图片到服务端
    void uploadImage() {
        if (path1 != null) {
            filePath.add(path1);
            fileName.add(BaseUtil.getFileNameNoEx(path1));
        }

        if (path2 != null) {
            filePath.add(path2);
            fileName.add(BaseUtil.getFileNameNoEx(path2));
        }

        //上传图片
        ApiResource.uploadGTImage(filePath, fileName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                String result = new String(bytes);
                if (i == 200 && result != null && result.startsWith("true")) {
                    fileName.clear();
                    filePath.clear();
                    postUpdate();
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                if (bytes != null) {
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private void postUpdate(){
        ApiResource.postUpdatePlcBxInfo(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result =new String(bytes);
                if ("true".equals(result)){
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(PlcBxInfoAty.this.getWindow().getDecorView().getWindowToken(), 0);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        });
    }

    private boolean checkForm() {
        if (mPb==null||mPb.getLAT()<=0||mPb.getLNGT()<=0){
            toast("经纬度不能为空");
            return false;
        }
        if (TextUtils.isEmpty(mLinkMan.getText().toString())){
            toast("责任人不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mPhoneNo.getText().toString())){
            toast("电话不能为空");
            return false;
        }
        if (TextUtils.isEmpty(mWorkTime.getText().toString())){
            toast("勤务模式不能为空");
            return false;
        }
        if (mPb.getIMG1()==null&&(path1==null || path2==null)){
            toast("请上传2张岗亭图片");
            return false;
        }
//        if (TextUtils.isEmpty(mInterPhoneNo.getText().toString())){
//            toast("对讲机号码不能为空");
//            return false;
//        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PickupMapActivity.MAP_PICK_UP == requestCode) { //地图选点
            if (Activity.RESULT_OK == resultCode) {
                LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
                onReceiveLoc(info, true, null);
            }
        }else if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0 && imagePath != null) {
            /***获得拍照的路径 写入数据库**/
            if (i_flag == 1){
                path1 = imagePath;
                iv_zp1.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 2){
                path2 = imagePath;
                iv_zp2.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_bx_info);
    }

    public String getRequestJson(){
        if (mLocation!=null){
            mPb.setLAT(mLocation.getLat());
            mPb.setLNGT(mLocation.getLongt());
        }
        if (path1!=null){
            mPb.setIMG1(new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        }
        if (path2!=null){
            mPb.setIMG2(new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path2));
        }
        mPb.setADDRESS(mAddress.getText().toString());
        mPb.setINTERPHONENO(mInterPhoneNo.getText().toString());
        mPb.setINTERPHONETYPE(mInterphoneType.getText().toString());
        mPb.setPHONENO(mPhoneNo.getText().toString());
        mPb.setLINKMAN(mLinkMan.getText().toString());
        //值守派出所
        mPb.setWATCHOVERUNIT(watchOverDprtmt.getText().toString());
        mPb.setfINISHTIME(mFinishTime.getText().toString());
        if (mPlcSttndapter.getCount()>0){//当存在派出所列表
            mPb.setPOLICESTATION(mPlcSttndapter.getItem(mPlcSttn.getSelectedItemPosition()).toString());
        }
        mPb.setWORKTIME(mapRusles.get(mWorkTime.getText().toString().trim()));
        mPb.setINITIATOR(mInitiator.getText().toString());
        mPb.setDEMAND(mDemand.getText().toString());
        mPb.setUNIFIEDNO(mUnionNo.getText().toString());
        mPb.setSTREET(mStreet.getText().toString());
        mPb.setTYPE(mPlcBxType.getText().toString());
        return JSON.toJSONString(mPb);
    }

    @Override
    public boolean needAutoLoc() {
        return false;
    }
}