package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.model.DutyBoxModle;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.YinyanEntity;
import cn.net.xinyi.xmjt.utils.BaiduMapUtil;
import cn.net.xinyi.xmjt.utils.DateUtil;

/**
 * Created by hao.zhou on 2016/4/26.
 */
public class DutyBoxAddAty extends PickupMapActivity implements View.OnClickListener {
    //添加签到点弹出框控件
    private TextView tv_title;//标题
    private Button btn_canel;//取消
    private Button btn_add;//确定
    private TextView tv_zb;  // 坐标
    private EditText et_dz;//地址
    private EditText et_mz;//名字
    private boolean flag;//判断签到点 true为编辑 false为添加
    private View myAddView;//自定义布局
    private AlertDialog alertDialog;
    private int position;//编辑签到箱的位置

    private String address;//百度获取地址
    private double jd;//经度
    private double wd;//纬度

    private View layout;
    private String fID;
    private List<DutyBoxModle> list;
    private View addView;//title自定义布局
    private ListView mListview;
    private RelativeLayout rl_addfoot;
    private LinearLayout ll_empty_data;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypeUtils.compatibleWithJavaBean = true;
        if (1 == getIntent().getFlags()) {//巡逻统计页面
            getContentView().removeView(mAddress);//移除显示当前位置地址图标
            getContentView().removeView(pickView);
            fID=getIntent().getStringExtra("data");
            if (null != fID){
                getXLDWList();
            }
            String extra = getIntent().getStringExtra("extra");
            JSONObject object = JSON.parseObject(extra);
            if (!TextUtils.isEmpty(object.getString("CJYH"))) {
                getEntityRealLoc(object.getString("CJYH"),false);
            }
            if (getIntent().getBooleanExtra("isPeople",false)){
                getEntityRealLoc(object.getString("CJYH"),true);
            }
        } else {//巡逻页面
            fID =  getIntent().getStringExtra("data");
            getXLDWList();
            initDialog();
        }


        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        setMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getExtraInfo() != null && marker.getExtraInfo().getSerializable("data") instanceof DutyBoxModle) {
                    final DutyBoxModle box = (DutyBoxModle) marker.getExtraInfo().getSerializable("data");
                    if (null != box) {
                        //创建InfoWindow展示的view
                        Button button = new Button(getApplicationContext());
                        button.setTextColor(getResources().getColor(R.color.bthumbnail_font));
                        button.setText("地址：" + box.getSIGNBOX_DESCRIBE() + "\n纬度：" + box.getBAIDU_COORDINATE_X() + "\n经度" +box.getBAIDU_COORDINATE_Y());
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        button.setBackgroundResource(R.drawable.map_pickup);
                        //定义用于显示该InfoWindow的坐标点
                        LatLng pt = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                        InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
                        //显示InfoWindow
                        mMap.showInfoWindow(mInfoWindow);
                    }
                } else if (marker.getExtraInfo() != null && marker.getExtraInfo().getSerializable("data") instanceof YinyanEntity) {

                    final YinyanEntity pb = (YinyanEntity) marker.getExtraInfo().getSerializable("data");
                    //创建InfoWindow展示的view
                    Button button = new Button(getApplicationContext());
                    button.setTextColor(getResources().getColor(R.color.bthumbnail_font));
                    String name = pb.getName();
                    String phone = pb.getPhone();
                    if (TextUtils.isEmpty(name)) {
                        name = pb.getRealtime_point().getName();
                    }
                    if (TextUtils.isEmpty(name)) {
                        name = "";
                    }
                    if (TextUtils.isEmpty(phone)) {
                        phone = pb.getRealtime_point().getPhone();
                    }
                    if (TextUtils.isEmpty(phone)) {
                        phone = pb.getEntity_name();
                    }
                    button.setText("" + name + "\n电话" + phone+"\n最后定位时间:"+ DateUtil.date2String(new Date(pb.getRealtime_point().getLoc_time()*1000l),"yyyy-MM-dd HH:mm:ss"));

                    button.setBackgroundResource(R.drawable.map_pickup);
                    //定义用于显示该InfoWindow的坐标点
                    LatLng pt = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
                    //显示InfoWindow
                    mMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });

    }
    //初始化自定义添加巡段AlertDialog
    private void initDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        myAddView = layoutInflater.inflate(R.layout.aty_duty_box_add, null);
        et_mz= (EditText) myAddView.findViewById(R.id.ed_mz);
        et_dz =(EditText) myAddView.findViewById(R.id.et_dz);
        tv_zb= (TextView)myAddView.findViewById(R.id.tv_zb);
        tv_title= (TextView)myAddView.findViewById(R.id.tv_title);
        btn_canel= (Button)myAddView.findViewById(R.id.btn_canel);
        btn_add= (Button)myAddView.findViewById(R.id.btn_add);
        btn_canel.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }
    /**
     * @param markers
     * @param drawableId
     */
    public void setPlcBxMarker(List<DutyBoxModle> markers, int drawableId) {
        if (markers.size() == 0)
            return;
        for (DutyBoxModle pb : markers) {
            if (pb.getBAIDU_COORDINATE_X() > 0.0) {
                LatLng ll = new LatLng(pb.getBAIDU_COORDINATE_X(), pb.getBAIDU_COORDINATE_Y());
                LatLng latLng = new LatLng(ll.latitude, ll.longitude);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(drawableId);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(latLng)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                Overlay overlay = mMap.addOverlay(option);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", pb);
                overlay.setExtraInfo(bundle);
            }
        }
    }

    private void uploadInfo() {
        showLoadding();
        ApiResource.addDutyBoxInfo(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if (i == 200 && result != null ) {
                    clearText();
                    toast("添加签到点成功");
                } else {
                    toast("添加签到点失败");
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(DutyBoxAddAty.this.getWindow().getDecorView().getWindowToken(), 0);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("添加签到点失败！");
                stopLoading();
            }
        });
    }


    public String getRequestJson() {
        DutyBoxModle dutyBox = new DutyBoxModle();
        dutyBox.setSIGNBOX_DESCRIBE(et_dz.getText().toString());
        dutyBox.setBAIDU_COORDINATE_Y(jd);
        dutyBox.setBAIDU_COORDINATE_X(wd);
        dutyBox.setSID_NAME(et_mz.getText().toString());
        JSONObject jo = JSON.parseObject(JSON.toJSONString(dutyBox));
        jo.put("F_BID",fID);
        if (flag)
            jo.put("SID", list.get(position).getSID());
        return jo.toJSONString();
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_box);
    }

    /**
     * 巡逻段下属的签到点列表
     *
     * @return
     */
    private String getPlcSttnList() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("F_BID", fID);
        return requestJson.toJSONString();
    }

    private void getXLDWList() {
        showLoadding();
        ApiResource.getDutyBoxList(getPlcSttnList(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    if (null != list && list.size() >  0){
                        list.clear();
                    }
                    list = JSON.parseArray(result, DutyBoxModle.class);
                    if (list == null || list.size() == 0) {
                        ll_empty_data.setVisibility(View.VISIBLE);
                        mListview.setVisibility(View.GONE);
                    } else {
                        mListview.setVisibility(View.VISIBLE);
                        ll_empty_data.setVisibility(View.GONE);
                        DutyBoxAddAdp mAdapter = new DutyBoxAddAdp(mListview, list, R.layout.aty_dutybox_add, DutyBoxAddAty.this);
                        mListview.setAdapter(mAdapter);
                        setPlcBxMarker(list, R.drawable.nearly_checked_null);
                        /**
                         * 界面复用
                         * getIntent().getFlags()==1  签到点统计点击巡逻段查看
                         * 设置第一个点位为中心点
                         * **/
                        if (1 == getIntent().getFlags()){
                            setCenter(new LatLng(list.get(0).getBAIDU_COORDINATE_X(), list.get(0).getBAIDU_COORDINATE_Y()),15.0f);
                        }
                    }
                } catch (JSONException e) {
                    toast("获取数据失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }

    @Override
    public boolean needAutoLoc() {
        return 1 != getIntent().getFlags();
    }



    //动态加载地图 下部分listView
    @Override
    public void setupFooterView(LinearLayout parent) {
        super.setupFooterView(parent);
        addView = LayoutInflater.from(this).inflate(R.layout.aty_duty_add_foot, parent);
        parent.setVisibility(View.VISIBLE);
        mListview = (ListView) addView.findViewById(R.id.lv_list);
        rl_addfoot = (RelativeLayout) addView.findViewById(R.id.rl_addfoot);
        ll_empty_data = (LinearLayout) addView.findViewById(R.id.ll_empty_data);
        initLongClick();
    }

    private void initLongClick() {
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LatLng ll = new LatLng(list.get(i).getBAIDU_COORDINATE_X(), list.get(i).getBAIDU_COORDINATE_Y());
                setCenter(ll);
            }
        });

        mListview.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                menu.add(0, 0, 0, getString(R.string.editor));
                menu.add(0, 1, 0, getString(R.string.del));
                menu.add(0, 2, 0, getString(R.string.cancel));
            }
        });
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        position = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
        switch (item.getItemId()) {
            case 0:
                setData();
                break;
            case 1:
                new AlertDialog.Builder(DutyBoxAddAty.this).setTitle(getString(R.string.tips))
                        .setMessage("确定删除签到点信息！")
                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delInfo();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            case 2:
                toast(getString(R.string.cancel));
                break;
        }

        return super.onContextItemSelected(item);
    }
    private void setData() {
        flag=true;
        //上传成功后清除控件数据
        et_mz.setText(list.get(position).getSID_NAME());
        et_dz.setText(list.get(position).getSIGNBOX_DESCRIBE());
        tv_zb.setText("("+list.get(position).getBAIDU_COORDINATE_X()+","+list.get(position).getBAIDU_COORDINATE_Y()+")");
        showDialogAlert(getString(R.string.duty_box_editor));
    }

    private void editorInfo() {
        showLoadding();
        ApiResource.updateDutyBoxInfo(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if (i == 200 && result.equals("true")) {
                    clearText();
                    toast("编辑签到点成功");
                } else {
                    toast("编辑签到点失败！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("编辑签到点失败！");
                stopLoading();
            }
        });
    }

    private void delInfo() {
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("SID", list.get(position).getSID());
        ApiResource.delDutyBoxInfo(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if (i == 200 && result.equals("true")) {
                    getXLDWList();
                    toast("删除签到点成功");
                } else {
                    toast("删除签到点失败！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("删除签到点失败！");
                stopLoading();
            }
        });
    }

    /**
     * 点击，当有地址的时候会执行此操作
     *
     * @param loc
     * @return
     */
    @Override
    public boolean onAddressPickEvent(LocaionInfo loc) {
        address = loc.getAddress();
        wd = Double.valueOf(loc.lat);
        jd = Double.valueOf(loc.longt);
        if (address != null) {
            flag=false;
            et_dz.setText(address);
            tv_zb.setText("(" + wd + "," + jd + ")");
            showDialogAlert(getString(R.string.duty_box_add));
        }
        return false;
    }

    private void showDialogAlert(String title) {
        tv_title.setText(title);
        alertDialog=new AlertDialog.Builder(DutyBoxAddAty.this)
                .setView(myAddView)
                .setCancelable(false).show();
    }

    public void getEntityRealLoc(String entityName,final boolean isCenter) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("entity_names", entityName);
        ApiResource.getYinyanEntityList(maps, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject resultJSON = JSON.parseObject(result);
                        if (BaiduMapUtil.isOptionSuceess(result)) {
                            List<YinyanEntity> polices = JSON.parseArray(resultJSON.getString("entities"), YinyanEntity.class);
                            if (polices.size() > 0 && polices.get(0).getRealtime_point() != null) {
                                YinyanEntity p = polices.get(0);
                                LatLng ll = new LatLng(p.getRealtime_point().getLocation().get(1), p.getRealtime_point().getLocation().get(0));
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", p);
                                setMarker(ll, R.drawable.map_jlfb, bundle);
                                if (isCenter && ll.longitude> 0.0)
                                    setCenter(ll);
                            }
                        }
                    } catch (JSONException e) {

                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("error");
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_canel:
                dismissAlert(true);
                ((FrameLayout)myAddView.getParent()).removeView(myAddView);
                break;
            case R.id.btn_add:
                if (TextUtils.isEmpty(et_dz.getText().toString())) {
                    toast("地址不能为空！");
                    dismissAlert(false);//控制点击 diaAlert是否出现
                }else {
                    if (flag){//编辑巡逻段信息
                        editorInfo();
                    }else {//上传巡逻段信息
                        uploadInfo();
                    }
                }

                break;
        }
    }

    private void  clearText(){
        dismissAlert(true);//控制点击 diaAlert是否出现
        ((FrameLayout)myAddView.getParent()).removeView(myAddView);//移除view,否则会报错
        getXLDWList();
        //上传成功后清除控件数据
        et_mz.setText("");
        tv_zb.setText("");
        et_dz.setText("");
    }
    private void dismissAlert(boolean b) {//控制对话框是否关闭
        try {
            Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing" );
            field.setAccessible(true);
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(alertDialog,b);
            alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean needKeepLoc() {
        return true;
    }
}