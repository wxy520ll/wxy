package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinyi_tech.comm.util.ToastyUtil;

import org.apache.http.Header;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.MachineFowModle;
import cn.net.xinyi.xmjt.model.MachineModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;

public class MachineApprovalAty extends BaseActivity2 implements View.OnClickListener {
    //单位
    @BindView(id = R.id.tv_pcs, click = true)
    private TextView tv_pcs;
    //管理员
    @BindView(id = R.id.ll_gly)
    private LinearLayout ll_gly;
    //管理员
    @BindView(id = R.id.tv_gly)
    private TextView tv_gly;
    //申请人姓名
    @BindView(id = R.id.tv_sq_xm)
    private TextView tv_sq_xm;
    //申请人电话
    @BindView(id = R.id.tv_sq_dh)
    private TextView tv_sq_dh;
    //申请人公司
    @BindView(id = R.id.tv_sq_gs)
    private TextView tv_sq_gs;
    //机房类型
    @BindView(id = R.id.tv_room_type, click = true)
    private TextView tv_room_type;
    //审批人员
    @BindView(id = R.id.tv_check_person, click = true)
    private TextView tv_check_person;
    //完工时间
    @BindView(id = R.id.tv_sgwcsj, click = true)
    private TextView tv_sgwcsj;
    //同行人员
    @BindView(id = R.id.lv_follow_person)
    private ListView lv_follow_person;
    //增加同行人员
    @BindView(id = R.id.iv_addfollow, click = true)
    private ImageView iv_addfollow;
    //手持身份证照片
    @BindView(id = R.id.iv_sfz, click = true)
    private ImageView iv_sfz;
    //机房申请
    @BindView(id = R.id.btn_jf, click = true)
    private Button btn_jf;
    //取消机房申请
    @BindView(id = R.id.btn_qx, click = true)
    private Button btn_qx;
    int roomId = 0;
    private List<MachineModle> machineRooms;
    private List<MachineFowModle> machineFowModles;
    private Map<String, String> map;
    private Map<String, String> getMapData;
    private String path1;
    private int uploadCount;
    private TimePickerView pvTime;
    private ArrayList<MachineFowModle> mLists;
    private AlertDialog alertDialog;
    private List<String> organs;
    private String pcs;
    private List<String> modles;
    private ListView list_item_1;
    private ListView list_item_2;
    private View myAddView;
    private String FELLOWLIST;
    private Integer ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_machine_approval);
        AnnotateManager.initBindView(this);  //控件绑定
        initDialog();
        initLocalData();
        getMachineFollowData();
        getAllMachineroomDept();
        initTimePickerView();
    }

    private void initLocalData() {
        machineFowModles = new ArrayList<>();
        tv_sq_xm.setText(userInfo.getName());
        tv_sq_dh.setText(userInfo.getUsername());
        tv_sq_gs.setText(userInfo.getCompanyname());

        if (null != getIntent().getSerializableExtra("DATA")) {
            getMapData = (Map<String, String>) getIntent().getSerializableExtra("DATA");
            btn_jf.setText(getString(R.string.commit_agin));
            if (getMapData.get("SHJG").equals("0")) {
                btn_qx.setVisibility(View.VISIBLE);
            }
            tv_pcs.setText(getMapData.get("MC"));//所属单位机房名字
            tv_sgwcsj.setText(getMapData.get("SGWCSJ"));//完成时间
            tv_room_type.setText(getMapData.get("TYPE"));//机房类型
            tv_check_person.setText(getMapData.get("SHRYNAME"));//核查人员
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/fellow/" + getMapData.get("SCJFCRZ"), iv_sfz);
            //机房ID
            Object operType = getMapData.get("ROOMID");
            if (operType instanceof String) {
                roomId = Integer.valueOf((String) operType);
            } else {
                roomId = ((Integer) operType);
            }
            //数据ID
            Object operID = getMapData.get("ID");
            if (operID instanceof String) {
                ID = Integer.valueOf((String) operID);
            } else {
                ID = ((Integer) operID);
            }
            //查询管理员号码
            if (getMapData.get("TYPE").equals("建设施工")) {
                queryAdmin(2, "1");
            } else {
                queryAdmin(2, "0");
            }
            //同行人员拼接字段
            FELLOWLIST = getMapData.get("FELLOWLIST");
            //根据机房id获取管理员信息
            machinePerson(roomId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            getMachineFollowData();
        } else if (requestCode == CAMERA_INTENT_REQUEST
                && resultCode != 0 && imagePath != null) {
            path1 = imagePath;
            iv_sfz.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
        }
    }

    private void getMachineFollowData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("MANAGER_SJHM", userInfo.getUsername());
        ApiResource.MachineRoomFellowList(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (machineFowModles != null) {
                    machineFowModles.clear();
                }
                machineFowModles = JSON.parseArray(result, MachineFowModle.class);
                /**初始化adapter**/
                initAdapter();
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }

    private void getAllMachineroomDept() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("", "");
        ApiResource.getAllMachineroomDept(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                organs = new ArrayList<String>();
                JSONArray js = JSON.parseArray(result);
                if (js.size() != 0) {
                    for (int j = 0; j < js.size(); j++) {
                        organs.add(((JSONObject) js.get(j)).getString("ZDZ"));
                    }
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取单位数据失败");
            }
        });
    }

    /**
     * 初始化adapter
     **/
    private void initAdapter() {
        if (machineFowModles.size() != 0) {
            lv_follow_person.setVisibility(View.VISIBLE);
            MachineFollowAdp mAdapter = new MachineFollowAdp(lv_follow_person, machineFowModles, R.layout.aty_machine_follow_item, MachineApprovalAty.this, FELLOWLIST);
            lv_follow_person.setAdapter(mAdapter);
            lv_follow_person.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MachineApprovalAty.this, MachineFollowAty.class);
                    intent.putExtra("DATA", machineFowModles.get(position));
                    startActivityForResult(intent, 1001);
                }
            });
        } else {
            lv_follow_person.setVisibility(View.GONE);
        }
    }

    private void initTimePickerView() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tv_sgwcsj.setText(DateUtil.date2String(date, "yyyy-MM-dd HH:mm:SS"));
            }
        });
    }


    private void showDialogAlert() {
        if (null != alertDialog) {
            ((FrameLayout) myAddView.getParent()).removeView(myAddView);
            try {
                Field field = alertDialog.getClass()
                        .getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                // 将mShowing变量设为false，表示对话框已关闭
                field.set(alertDialog, true);
                alertDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //弹出框
        alertDialog = new AlertDialog.Builder(MachineApprovalAty.this)
                .setView(myAddView).show();
    }


    //初始化自定义添加巡段AlertDialog
    private void initDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        myAddView = layoutInflater.inflate(R.layout.aty_listview_two, null);
        list_item_1 = (ListView) myAddView.findViewById(R.id.list_item_1);
        list_item_2 = (ListView) myAddView.findViewById(R.id.list_item_2);
        list_item_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pcs = organs.get(position);
                requestData();
            }
        });
        list_item_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                tv_pcs.setText(modles.get(position) + "(" + pcs + ")");
                for (MachineModle modle : machineRooms) {
                    if (modle.getMC().equals(modles.get(position))) {
                        roomId = modle.getROOMID();
                    }
                }
                machinePerson(roomId);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (BaseDataUtils.isFastClick()) {

        } else {
            switch (v.getId()) {
                case R.id.tv_sgwcsj:
                    pvTime.show();
                    break;

                case R.id.iv_sfz:
                    cameraPhoto();
                    break;

                case R.id.iv_addfollow:
                    Intent intent = new Intent(this, MachineFollowAty.class);
                    startActivityForResult(intent, 1001);
                    break;

                case R.id.tv_check_person:
                    if (tv_room_type.getText().toString().equals("建设施工")) {
                        queryAdmin(1, "1");
                    } else if (tv_room_type.getText().toString().equals("巡检维护")) {
                        queryAdmin(1, "0");
                    } else {
                        UI.toast(this, "请选择施工类型！");
                    }
                    break;

                case R.id.tv_room_type:
                    String[] type = new String[]{"建设施工", "巡检维护"};
                    BaseDataUtils.showAlertDialog(MachineApprovalAty.this, type, tv_room_type);
                    tv_check_person.setText("");
                    break;

                case R.id.btn_jf://机房申请确认
                    mLists = new ArrayList<>();
                    //将选中的同行人员 添加到新的集合
                    for (int i = 0; i < machineFowModles.size(); i++) {
                        if (MachineFollowAdp.getIsSelected().get(i).equals(true)) {

//                            if (null != machineFowModles.get(i).getGXSJ()//随行人员照片12小时有效
//                                    && DateUtil.minuteBetween(DateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"), machineFowModles.get(i).getGXSJ()) <= 12 * 60){
                            mLists.add(machineFowModles.get(i));
//                            }else {
//                                UI.toast(MachineApprovalAty.this,"同行人员"+machineFowModles.get(i).getXM()+"的手持身份证照片过期，请重新拍摄！");
//                                return;
//                            }
                        }
                    }

                    String msg = "";
                    if (TextUtils.isEmpty(tv_pcs.getText().toString())) {
                        msg = "请选择施工机房";
                    } else if (TextUtils.isEmpty(tv_room_type.getText().toString())) {
                        msg = "请选择施工类型";
                    } else if (TextUtils.isEmpty(tv_check_person.getText().toString())) {
                        msg = "请选择审批人";
                    } else if (TextUtils.isEmpty(tv_sgwcsj.getText().toString())) {
                        msg = "请选择施工完成时间";
                    } else try {
                        if (!TextUtils.isEmpty(tv_sgwcsj.getText().toString()) && DateUtil.minuteBetween(DateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"), tv_sgwcsj.getText().toString()) >= 0) {
                            msg = "施工完成时间不能小于当前时间！";
                        } else if ((btn_jf.getText().toString() != (getResources().getString(R.string.commit_agin)) && null == path1)) {
                            msg = "手持机房出入证照片不能为空";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (getMapKey(tv_check_person.getText().toString()) == null || StringUtils.isEmpty((String) getMapKey(tv_check_person.getText().toString()))) {
                        ToastyUtil.warningShort("无法获取审批人的电话号码,请重选");
                        return;
                    }
                    if (!msg.isEmpty()) {
                        UI.toast(MachineApprovalAty.this, msg);
                    } else {
                        startUploadThread();
                    }
                    break;

                case R.id.tv_pcs:
                    if (null != organs && organs.size() > 0) {
                        list_item_1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, organs));
                        showDialogAlert();
                    } else {
                        getAllMachineroomDept();
                    }
                    break;

                case R.id.btn_qx:
                    updateData();
                    break;

            }
        }
    }

    private void queryAdmin(final int type, String s) {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("JFLB", s);
        ApiResource.getUserByJflb(requestJson.toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result != null && result.length() > 5) {
                    JSONArray js = JSON.parseArray(result);
                    map = new HashMap<String, String>();
                    map.clear();
                    if (js.size() != 0) {
                        for (int j = 0; j < js.size(); j++) {
                            map.put(((JSONObject) js.get(j)).getString("USERNAME"), ((JSONObject) js.get(j)).getString("NAME"));
                        }
                        if (type == 1) {
                            BaseDataUtils.showAlertDialog(MachineApprovalAty.this, map.values(), tv_check_person);
                        }
                    }
                } else {
                    UI.toast(MachineApprovalAty.this, "该类型没有审批人");
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                UI.toast(MachineApprovalAty.this, "获取数据失败");
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
        if (path1 != null) {
            filePath.add(path1);
            fileName.add(BaseUtil.getFileNameNoEx(path1));
            //上传图片
            ApiResource.upMachineRoomFellow(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
//                        if (btn_jf.getText().toString().equals(getString(R.string.update))){
//                            updateData();//更新
//                        }else {
                        uploadData(); //添加
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
        } else if (btn_jf.getText().toString().equals(getString(R.string.commit_agin))) {
            uploadData(); //添加
        }
    }

    private void uploadData() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("SHJG", "0");
        requestJson.put("ROOMID", roomId);
        requestJson.put("SJHM", userInfo.getUsername());
        requestJson.put("SGWCSJ", tv_sgwcsj.getText().toString());
        requestJson.put("TYPE", tv_room_type.getText().toString());
        requestJson.put("FELLOWLIST", mLists.size() > 0 ? getFelloeList() : "");

        requestJson.put("SHRY", getMapKey(tv_check_person.getText().toString()));
        requestJson.put("BZ", "");
        if (null != path1) {
            requestJson.put("SCJFCRZ", new BaseDataUtils().getNowYearAndMonthAndDay() + "/" + BaseUtil.getFileName(path1));
        } else {
            requestJson.put("SCJFCRZ", getMapData.get("SCJFCRZ"));
        }
        ApiResource.MachineCheckAdd(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty()) {
                    sResult = result;
                    uploadCount++;
                    if (null != path1) {
                        File plateImage = new File(path1);
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                } else {
                    onFailure(i, headers, bytes, null);
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                UI.toast(MachineApprovalAty.this, "数据上传失败");
            }
        });
    }

    private void updateData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("ID", ID);
        requestJson.put("SHJG", "3");
//        requestJson.put("ROOMID",roomId);
//        requestJson.put("SJHM", userInfo.getUsername());
//        requestJson.put("SGWCSJ",tv_sgwcsj.getText().toString());
//        requestJson.put("TYPE", tv_room_type.getText().toString());
//        requestJson.put("FELLOWLIST",mLists.size() > 0?getFelloeList():"");
//        requestJson.put("SHRY", getMapKey(tv_check_person.getText().toString()));
//        requestJson.put("BZ", "");
//        if (null!=path1){
//            requestJson.put("SCJFCRZ", new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
//        }
        ApiResource.MachineCheckUpdate(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty()) {
//                    if (null!=path1) {
//                        File plateImage = new File(path1);
//                        if (plateImage.exists()) {
//                            plateImage.delete();
//                        }
//                    }
                    stopLoading();
                    setResult(RESULT_OK);
                    UI.toast(MachineApprovalAty.this, "取消成功!");
                    MachineApprovalAty.this.finish();
                } else {
                    onFailure(i, headers, bytes, null);
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                UI.toast(MachineApprovalAty.this, "数据上传失败");
            }
        });
    }


    private String sResult;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:// 上传进度条显示
                    showLoadding("正在上传数据",MachineApprovalAty.this);
                    break;
                case 2:// 上传之后
                    stopLoading();
                    if (sResult.equals("\"-2\"")) {
                        UI.toast(MachineApprovalAty.this, "该机房不能重复发起申请！");
                    } else if (sResult.equals("\"-1\"")) {
                        UI.toast(MachineApprovalAty.this, "上传失败,请稍后再试！");
                    } else {
                        setResult(RESULT_OK);
                        UI.toast(MachineApprovalAty.this, "您的申请资料已经通知管理员，如长时间接收到审核通过消息建议主动联系管理员！");
                        MachineApprovalAty.this.finish();
                    }
                    break;
                case 3:// 上传失败
                    stopLoading();
                    UI.toast(MachineApprovalAty.this, "上传失败，可能当前上传的人数较多，请稍候重试！");
                    break;
            }
        }
    };

    //获取同行人员ID
    private Object getFelloeList() {
        StringBuilder s = new StringBuilder();
        for (MachineFowModle infos : mLists) {
            s.append(infos.getID() + ",");
        }
        return s;
    }

    //根据map的值获取key
    private Object getMapKey(String s) {
        String keyValus = null;
        if (map != null) {
            for (String key : map.keySet()) {
                if (map.get(key).equals(s)) {
                    keyValus = key;
                    break;
                }
            }
        }
        return keyValus;
    }


    //获取派出所下属机房
    private void requestData() {
        showLoadding();
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("PCSBM", zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_PCS, pcs));
        requestJson.put("ISPERSONEXISTS", "1");
        ApiResource.MachineRoomPcsMain(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result != null && result.length() > 5) {
                    machineRooms = JSON.parseArray(result, MachineModle.class);
                    modles = new ArrayList<>();
                    modles.clear();
                    if (machineRooms.size() > 0) {
                        for (MachineModle modle : machineRooms) {
                            modles.add(modle.getMC());
                        }
                        list_item_2.setVisibility(View.VISIBLE);
                        list_item_2.setAdapter(new ArrayAdapter<String>(MachineApprovalAty.this, android.R.layout.simple_list_item_1, modles));
                    }
                } else {
                    UI.toast(MachineApprovalAty.this, "该单位没有机房数据");
                    list_item_2.setVisibility(View.GONE);
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                UI.toast(MachineApprovalAty.this, "获取数据失败");
            }
        });
    }

    //获取管理员信息
    private void machinePerson(int roomId) {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("ROOMID", roomId);
        ApiResource.MachineRoomPersonList(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result != null && result.length() > 5) {
                    ll_gly.setVisibility(View.VISIBLE);
                    List<MachineModle> machineModles = JSON.parseArray(result, MachineModle.class);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("\n");
                    for (MachineModle per : machineModles) {
                        stringBuilder.append(per.getNAME() + "(" + per.getUSERNAME() + ")" + "\n");
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

    @Override
    public String getAtyTitle() {
        return getString(R.string.safety_approval);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            } else {
                this.finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }


}
