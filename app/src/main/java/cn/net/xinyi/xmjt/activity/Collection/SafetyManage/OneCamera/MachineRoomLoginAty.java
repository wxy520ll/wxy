package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.MachineModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.UI;

public class MachineRoomLoginAty extends BaseActivity2  implements View.OnClickListener{
    //单位
    @BindView(id = R.id.tv_pcs,click = true)
    private TextView tv_pcs;
    //机房
    @BindView(id = R.id.tv_jf,click = true)
    private TextView tv_jf;
    //管理员
    @BindView(id = R.id.ll_gly)
    private LinearLayout ll_gly;
    //管理员
    @BindView(id = R.id.tv_gly)
    private TextView tv_gly;

    //登录机房时间
    @BindView(id = R.id.tv_logintime)
    private TextView tv_logintime;
    //登录机房时间
    @BindView(id = R.id.ll_logintime)
    private LinearLayout ll_logintime;

    //身份证照片
    @BindView(id = R.id.iv_sfz,click = true)
    private ImageView iv_sfz;

    //机房照片
    @BindView(id = R.id.iv_jf,click = true)
    private ImageView iv_jf;

    //开始进入
    @BindView(id = R.id.btn_ks,click = true)
    private Button btn_ks;

    private List<MachineModle> machineRooms;
    private int roomId;
    private JSONObject jo;

    private String path1;
    private String path2;
    private int i_flag;
    private int uploadCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_machine_room_login);
        AnnotateManager.initBindView(this);  //控件绑定
        getData();
    }

    private void getData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("CJYH", userInfo.getUsername());
        ApiResource.MachineRoomEnterLast(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    jo= (JSONObject) JSON.parseArray(result).get(0);
                    if (null==jo.getString("JSSJ")&&null!=jo.getString("KSSJ")){
                        tv_pcs.setEnabled(false);
                        tv_jf.setEnabled(false);
                        tv_pcs.setText(jo.getString("ZDZ"));//所属单位
                        tv_jf.setText(jo.getString("MC"));//机房名称
                        ll_logintime.setVisibility(View.VISIBLE);
                        tv_logintime.setText(jo.getString("KSSJ"));
                        btn_ks.setText(getString(R.string.machine_room_quick));
                        btn_ks.setBackgroundColor(getResources().getColor(R.color.bbutton_danger));
                        roomId=Integer.parseInt(jo.getString("ROOMID"));
                        machinePerson(roomId);//获取机房管理员信息
                        ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/machine/"  + jo.getString("SFZZP"), iv_sfz);
                        ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/machine/"  + jo.getString("JFZP"), iv_jf);
                    }else {
                        initLocalData();
                    }
                } else {
                    initLocalData();
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
                initLocalData();
            }
        });
    }

    private void initLocalData() {
        if (null!=getIntent().getSerializableExtra("DATA")){
            Map<String,String> map= (Map<String, String>) getIntent().getSerializableExtra("DATA");
            tv_pcs.setEnabled(false);
            tv_jf.setEnabled(false);
            tv_pcs.setText(map.get("ZDZ"));//所属单位
            tv_jf.setText(map.get("MC"));//机房名称
            Object operType = map.get("ROOMID");
            if(operType instanceof String){
                roomId=Integer.valueOf((String)operType);
            }else{
                roomId=((Integer)operType);
            }
            machinePerson(roomId);//获取机房管理员信息
        }else {
            initTextChangeListen();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ks:
                if (btn_ks.getText().toString().equals(getString(R.string.machine_room_quick))){//离开登记
                    updateInfo();
                }else {//进入登记
                    String msg="";
                    if (TextUtils.isEmpty(tv_pcs.getText().toString())){
                        msg="单位名称不能为空";
                    }else  if (TextUtils.isEmpty(tv_jf.getText().toString())  ){
                        msg="机房名称不能为空";
                    }else  if (null==path1){
                        msg="请上传手持身份证照片一张";
                    }else  if (null==path2){
                        msg="请上传机房门口全景图";
                    }
                    if (!msg.isEmpty()){
                        UI.toast(MachineRoomLoginAty.this,msg);
                    }else {
                        startUploadThread();
                    }
                }
                break;

            case R.id.tv_pcs:
                final Map<String,String> pcsMaps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_PCS);
                BaseDataUtils.showAlertDialog(MachineRoomLoginAty.this, pcsMaps.values(),tv_pcs);
                break;

            case R.id.tv_jf:
                if (tv_pcs.getText().toString().isEmpty()){
                    UI.toast(MachineRoomLoginAty.this,"请选择单位");
                }else if (null==machineRooms||machineRooms.size()<0){
                    UI.toast(MachineRoomLoginAty.this,"该单位没有设置机房！");
                }else {
                    List<String> modles=new ArrayList<>();
                    for (MachineModle modle:machineRooms){
                        modles.add(modle.getMC());
                    }
                    BaseDataUtils.showAlertDialog(MachineRoomLoginAty.this,modles,tv_jf);
                }
                break;

            case R.id.iv_sfz:
                i_flag = 1;
                cameraPhoto();
                break;

            case R.id.iv_jf:
                i_flag = 2;
                cameraPhoto();
                break;
        }
    }




    private void startUploadThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadImage();
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
    void uploadImage() {
        if (path1!= null) {
            filePath.add(path1);
            fileName.add(BaseUtil.getFileNameNoEx(path1));
        }
        if (path2!= null) {
            filePath.add(path2);
            fileName.add(BaseUtil.getFileNameNoEx(path2));
        }
        //上传图片
        ApiResource.upMachineRoomEnter(filePath, fileName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                String result = new String(bytes);
                if (i == 200 && result != null && result.startsWith("true")) {
                    fileName.clear();
                    filePath.clear();
                    uploadInfo();
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
    public void uploadInfo() {
        //json处理
        JSONObject jo = new JSONObject();
        jo.put("CJYH", userInfo.getUsername());
        jo.put("ROOMID", roomId);
        jo.put("SFZZP", new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        jo.put("JFZP", new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path2));
        String json = jo.toJSONString();
        ApiResource.MachineRoomEnterAdd(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                uploadCount++;
                if (!result.isEmpty()) {
                    if (null!=path1) {
                        File plateImage = new File(path1);
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (path2 != null) {
                        File plateImage = new File(path2);
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

    //更新上传采集数据到服务端
    public void updateInfo() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("ID",jo.getString("ID"));
        jsonObject.put("CJYH",jo.getString("CJYH"));
        jsonObject.put("ROOMID",jo.getString("ROOMID"));
        String json = jsonObject.toJSONString();
        ApiResource.MachineRoomEnterUpdate(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (null!=result&&result.startsWith("true")){
                    MachineRoomLoginAty.this.finish();
                    UI.toast(MachineRoomLoginAty.this,"离开机房登记成功");
                }else {
                    onFailure(i,headers,bytes,null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                UI.toast(MachineRoomLoginAty.this,"上传失败，可能当前上传的人数较多，请稍候重试！");
            }
        });
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding("正在上传数据",MachineRoomLoginAty.this);
                    break;

                case 2:// 上传之后
                    stopLoading();
                    getData();
                    UI.toast(MachineRoomLoginAty.this,"进入机房登记成功");
                    break;

                case 3:// 上传失败
                    stopLoading();
                    UI.toast(MachineRoomLoginAty.this,"上传失败，可能当前上传的人数较多，请稍候重试！");
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
            if (i_flag == 1){
                path1 = imagePath;
                iv_sfz.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 2){
                path2 = imagePath;
                iv_jf.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        }
    }
    //获取派出所下属机房
    private void requestData() {
        showLoadding();
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("PCSBM",zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_PCS,tv_pcs.getText().toString()));
        requestJson.put("ISPERSONEXISTS","1");
        ApiResource.MachineRoomPcsMain(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    machineRooms= JSON.parseArray(result,MachineModle.class);
                } else {
                    UI.toast(MachineRoomLoginAty.this,"该单位没有机房数据");
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                UI.toast(MachineRoomLoginAty.this,"获取数据失败");
            }
        });
    }

    //获取管理员信息
    private void machinePerson(int roomId) {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("ROOMID", roomId);
        ApiResource.MachineRoomPersonList(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    ll_gly.setVisibility(View.VISIBLE);
                    List<MachineModle> machineModles= JSON.parseArray(result, MachineModle.class);
                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append("\n");
                    for (MachineModle per:machineModles){
                        stringBuilder.append(per.getNAME()+"("+ per.getUSERNAME()+")"+"\n");
                    }
                    tv_gly.setText(stringBuilder.toString());
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }


    private void initTextChangeListen() {
        tv_pcs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                requestData();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        tv_jf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (MachineModle modle:machineRooms){
                    if (modle.getMC().equals(tv_jf.getText().toString())){
                        roomId=modle.getROOMID();
                    }
                }
                machinePerson(roomId);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.machine_jfdj);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


}
