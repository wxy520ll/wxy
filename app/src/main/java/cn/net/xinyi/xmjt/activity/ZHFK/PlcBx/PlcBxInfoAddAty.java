package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
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
 * Created by hao.zhou on 2016/5/6.
 */
public class PlcBxInfoAddAty extends BaseWithLocActivity implements View.OnClickListener {

    //岗亭类型
    @BindView(id = R.id.tv_type, click = true)
    TextView mPlcBxType;

    //街道
    @BindView(id = R.id.et_street, click = true)
    TextView mStreet;

    //统一编号
    @BindView(id = R.id.et_unionNo)
    EditText mUnionNo;


    //建设需求
    @BindView(id = R.id.et_demand, click = true)
    TextView mDemand;


    //领头部门
    @BindView(id = R.id.et_initiator, click = true)
    TextView mInitiator;


    //所属派出所
    @BindView(id = R.id.et_police_station,click = true)
    TextView mPlcSttn;


    //完成时间
    @BindView(id = R.id.et_finish_time,click = true)
    TextView mFinishTime;


    //值守单位
    @BindView(id = R.id.et_watchOver_department,click = true)
    EditText watchOverDprtmt;

    //联系人
    @BindView(id = R.id.et_linkman)
    EditText mLinkMan;

    //手机号码
    @BindView(id = R.id.et_phoneno)
    EditText mPhoneNo;

    //手持终端类型
    @BindView(id = R.id.et_interphone_type, click = true)
    TextView mInterphoneType;

    //排班规则
    @BindView(id = R.id.tv_worktime,click = true)
    TextView mWorkTime;

    //手持终端号码
    @BindView(id = R.id.et_interphoneno)
    EditText mInterPhoneNo;

    //手动定位
    @BindView(id = R.id.tv_sddw, click = true)
    TextView mManualLoc;

    /**手动定位**/
    @BindView(id = R.id.tv_sxwz,click = true)
    private TextView tv_sxwz;

    //地址
    @BindView(id = R.id.tv_zb)
    EditText mAddress;

    //经纬度
    @BindView(id = R.id.et_latLngt, click = true)
    TextView mTvLatLngt;

    //上传
    @BindView(id = R.id.btn_plc_bx_update, click = true)
    TextView mUpdate;

    //照片1
    @BindView(id = R.id.iv_zp1, click = true)
    ImageView iv_zp1;

    //照片2
    @BindView(id = R.id.iv_zp2, click = true)
    ImageView iv_zp2;

    LocaionInfo mLocation;

    PoliceBoxModle mPb;

    private  String[] JSXQ=new String[]{"新建","翻新改造","翻新改造（集装）","翻新改造（房屋）"};
    private  String[] DJS=new String[]{"手持","固态"};
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
        setContentView(R.layout.aty_plcbx_add_info);
        AnnotateManager.initBindView(this);
        getRulesData();
        mPb=new PoliceBoxModle();
        checkLength();
        pcsMaps=zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_PCS,userInfo.getFjbm());
        jdMaps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_JD);
    }

    private void checkLength() {
        checkTextLength(mUnionNo,15);
        checkTextLength(mLinkMan,12);
        checkTextLength(mPhoneNo,15);
        checkTextLength(mInterPhoneNo,10);
    }


    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess) {
            mLocation = location;
            mTvLatLngt.setText("("+location.getLat()+","+location.getLongt()+")");
            mAddress.setText(location.getAddress());
            mPb.setLAT(location.getLat());
            mPb.setLNGT(location.getLongt());
        }
    }
    @Override
    public void onClick(View view) {
        if (BaseDataUtils.isFastClick()){
          
        }else {
            switch (view.getId()){
                //岗亭类别
                case R.id.tv_type:
                    new BaseDataUtils().showAlertDialog(this,getResources().getStringArray(R.array.duty_plate_type),mPlcBxType);
                    break;
                //建设需求
                case R.id.et_demand:
                    new BaseDataUtils().showAlertDialog(this,JSXQ,mDemand);
                    break;
                //对讲机
                case R.id.et_interphone_type:
                    new BaseDataUtils().showAlertDialog(this,DJS,mInterphoneType);
                    break;
                //值守状态
                case R.id.et_police_station :
                    new BaseDataUtils().showAlertDialog(this, pcsMaps.values(),mPlcSttn);
                    break;
                //值守单位
                case R.id.et_watchOver_department :
                    new BaseDataUtils().showAlertDialog(this,pcsMaps.values(),watchOverDprtmt);
                    break;
                //所属街道
                case R.id.et_street :
                    new BaseDataUtils().showAlertDialog(this,jdMaps.values(),mStreet);
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
                //领头部门
                case R.id.et_initiator :
                    new BaseDataUtils().showAlertDialog(this,pcsMaps.values(),mInitiator);
                    break;
                //完成时间
                case R.id.et_finish_time:
                    new BaseDataUtils().getData(this,mFinishTime);
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

                case R.id.btn_plc_bx_update:
                    if (!checkForm())
                        return;
                    //开始上传采集信息
                    startUploadThread();
                    break;

                case R.id.tv_sddw:
                    Intent intent=new Intent(this,PickupMapActivity.class);
                    startActivityForResult(intent, PickupMapActivity.MAP_PICK_UP);
                    break;

                case R.id.tv_sxwz:
                    loc();//获取定位
                    break;


            }
        }
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
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        });
    }

    private void postUpdate(){
        ApiResource.postAddPlcBxInfo(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result =new String(bytes);
                JSONObject jo=JSON.parseObject(result);
                int IDs=Integer.parseInt(jo.get("ID").toString());
                if (i == 200 && IDs > 0){
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(PlcBxInfoAddAty.this.getWindow().getDecorView().getWindowToken(), 0);
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
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding();
                    break;

                case 2:// 上传之后
                    stopLoading();
                    new AlertDialog.Builder(PlcBxInfoAddAty.this).setTitle(R.string.tips)
                            .setMessage("上传成功!")
                            .setNegativeButton ("返回",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            setResult(Activity.RESULT_OK);
                                            PlcBxInfoAddAty.this.finish();
                                        }
                                    })
                            .setPositiveButton("继续采集",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent(PlcBxInfoAddAty.this, PlcBxInfoAddAty.class);
                                            startActivity(intent);
                                            PlcBxInfoAddAty.this.finish();
                                        }
                                    }).setCancelable(false).show();

                    break;

                case 3:// 上传失败
                    stopLoading();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", PlcBxInfoAddAty.this);
                    break;

            }
        }
    };

    /**
     *返回拍照
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0 && imagePath != null) {
            /***获得拍照的路径 写入数据库**/
            if (i_flag == 1){
                path1 = imagePath;
                iv_zp1.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 2){
                path2 = imagePath;
                iv_zp2.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        }else  if (PickupMapActivity.MAP_PICK_UP == requestCode) { //地图选点
            if (Activity.RESULT_OK == resultCode) {
                LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
                onReceiveLoc(info, true, null);
            }
        }
    }

    private boolean checkForm() {
        if (TextUtils.isEmpty(mPlcBxType.getText().toString())){
            toast("请选择岗亭类别");
            return false;
        }
        if (TextUtils.isEmpty(mStreet.getText().toString())){
            toast("请选择街道");
            return false;
        }
        if (TextUtils.isEmpty(mUnionNo.getText().toString())){
            toast("统一编号不能为空");
            return false;
        }
//        if (TextUtils.isEmpty(mDemand.getText().toString())){
//            toast("请选择建设需求");
//            return false;
//        }
//        if (TextUtils.isEmpty(mInitiator.getText().toString())){
//            toast("请选择领头部门");
//            return false;
//        }
        if (TextUtils.isEmpty(mPlcSttn.getText().toString())){
            toast("请选择所属派出所");
            return false;
        }
        if (TextUtils.isEmpty(mFinishTime.getText().toString())){
            toast("请选择完成时间");
            return false;
        }
        if (TextUtils.isEmpty(watchOverDprtmt.getText().toString())){
            toast("请选择值守单位");
            return false;
        }
        if (TextUtils.isEmpty(mAddress.getText().toString())){
            toast("地址不能为空");
            return false;
        }
        if (mPb==null||mPb.getLAT()<=0||mPb.getLNGT()<=0){
            toast("经纬度不能为空");
            return false;
        }
        if (TextUtils.isEmpty(mWorkTime.getText().toString())){
            toast("勤务模式不能为空");
            return false;
        }
        if (TextUtils.isEmpty(mLinkMan.getText().toString())){
            toast("联络民警不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mPhoneNo.getText().toString())){
            toast("联络电话不能为空");
            return false;
        }

//        if (TextUtils.isEmpty(mInterphoneType.getText().toString())){
//            toast("对讲机类型不能为空");
//            return false;
//        }
//        if (TextUtils.isEmpty(mInterPhoneNo.getText().toString())){
//            toast("对讲机号码不能为空");
//            return false;
//        }
        if (path1==null || path2==null){
            toast("请上传2张岗亭图片");
            return false;
        }
        return true;

    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_bx_cj);
    }

    public String getRequestJson(){
        if (mLocation!=null){
            mPb.setLAT(mLocation.getLat());
            mPb.setLNGT(mLocation.getLongt());
            mPb.setADDRESS(mAddress.getText().toString());
        }
        mPb.setIMG1(new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        mPb.setIMG2(new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path2));
        mPb.setINTERPHONENO(mInterPhoneNo.getText().toString());
        mPb.setINTERPHONETYPE(mInterphoneType.getText().toString());
        mPb.setPHONENO(mPhoneNo.getText().toString());
        mPb.setLINKMAN(mLinkMan.getText().toString());
        mPb.setWATCHOVERUNIT(watchOverDprtmt.getText().toString());
        mPb.setfINISHTIME(mFinishTime.getText().toString());
        mPb.setWORKTIME(mapRusles.get(mWorkTime.getText().toString().trim()));
        mPb.setPOLICESTATION(mPlcSttn.getText().toString());
        mPb.setINITIATOR(mInitiator.getText().toString());
        mPb.setDEMAND(mDemand.getText().toString());
        mPb.setUNIFIEDNO(mUnionNo.getText().toString());
        mPb.setSTREET(mStreet.getText().toString());
        mPb.setTYPE(mPlcBxType.getText().toString());
        JSONObject jo = JSON.parseObject(JSON.toJSONString(mPb));
        if (jo != null) {
            jo.remove("ID");
        }
        return jo.toJSONString();
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

}