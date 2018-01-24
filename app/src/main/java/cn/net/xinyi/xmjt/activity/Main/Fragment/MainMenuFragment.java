package cn.net.xinyi.xmjt.activity.Main.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.CollectionInfoActivity;
import cn.net.xinyi.xmjt.activity.Collection.CollecManageAty;
import cn.net.xinyi.xmjt.activity.Collection.CollecTotalAty;
import cn.net.xinyi.xmjt.activity.Collection.House.HouseCheckActivity;
import cn.net.xinyi.xmjt.activity.Collection.InfoCheck.InfoCheckMainAty;
import cn.net.xinyi.xmjt.activity.Collection.Person.PerReturnAty;
import cn.net.xinyi.xmjt.activity.Collection.SafetyManage.SafetyMainAty;
import cn.net.xinyi.xmjt.activity.Collection.TSYY.TsyyMainAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCMainAty;
import cn.net.xinyi.xmjt.activity.Main.MainMenuAdp;
import cn.net.xinyi.xmjt.activity.Main.Setting.SalaryMainAty;
import cn.net.xinyi.xmjt.activity.Plate.PlateMainMenuActivity;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutyStartAty;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.ModelItem;
import cn.net.xinyi.xmjt.service.BaiduTraceService;
import cn.net.xinyi.xmjt.utils.BaiduTraceFacade;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.UI;
import cn.net.xinyi.xmjt.utils.zxing.activity.CaptureActivity;

/**
 * Created by zhiren.zhang on 2016/8/19.
 */
public class MainMenuFragment extends BaseNewFragment {
    private static final String TAG = "MainMenuFragment";
    private GridView gridview;
    private MainMenuAdp mainMenuAdp;
    XLReceiver mXlReceiver;
    private ImageView iv_bg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaiduTraceFacade.init(getContext());
        IntentFilter filter = new IntentFilter(DutyStartAty.XL_STATUS_ACTION); //注册巡逻状态改变receiver
        mXlReceiver = new XLReceiver();
        activity.registerReceiver(mXlReceiver, filter);
        getLastXlGtStatus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.main_menu, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gridview = (GridView) view.findViewById(R.id.main_menu);
        iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        iv_bg.setBackgroundResource(R.drawable.main_cj);
        //初始化GridView
        intMenu();
    }

    void intMenu() {
        List<ModelItem> modelItems = new ArrayList<>();
        if (BaseDataUtils.isCompanyOrOther()!=4){
            if (BaseDataUtils.isCompanyOrOther() > 2&&userInfo.getIsregester()==0) {
                if (!userInfo.getAccounttype().equals("综管员")){
                    modelItems.add(new ModelItem(ModelItem.NAME_CPZS, ModelItem.ID_CPZS, ModelItem.ICON_CPZS));
                }
                modelItems.add(new ModelItem(ModelItem.NAME_ESLDZS, ModelItem.ID_ESLDZS, ModelItem.ICON_ESLDZS));
                modelItems.add(new ModelItem(ModelItem.NAME_SSP, ModelItem.ID_SSP, ModelItem.ICON_SSP));
                if (!userInfo.getAccounttype().equals("综管员")) {
                    modelItems.add(new ModelItem(ModelItem.NAME_ZACJ, ModelItem.ID_ZACJ, ModelItem.ICON_ZACJ));
                    modelItems.add(new ModelItem(ModelItem.NAME_QJCJ, ModelItem.ID_QJCJ, ModelItem.ICON_QJCJ));
                    modelItems.add(new ModelItem(ModelItem.NAME_RYPC, ModelItem.ID_RYPC, ModelItem.ICON_RYPC));
                    modelItems.add(new ModelItem(ModelItem.NAME_XDXT, ModelItem.ID_XDXT, ModelItem.ICON_XDXT));
                    //队员薪资查询
                    modelItems.add(new ModelItem(ModelItem.NAME_SALALY, ModelItem.ID_SALALY, ModelItem.ICON_SALALY));
                    modelItems.add(new ModelItem(ModelItem.NAME_CJTJ, ModelItem.ID_CJTJ, ModelItem.ICON_CJTJ));
                    modelItems.add(new ModelItem(ModelItem.NAME_CJGL, ModelItem.ID_CJGL, ModelItem.ICON_CJGL));
                    modelItems.add(new ModelItem(ModelItem.NAME_XXHC, ModelItem.ID_XXHC, ModelItem.ICON_XXHC));
                }
                //出租屋信息
                //if (BaseDataUtils.isPOLICE()) {
                // modelItems.add(new ModelItem(ModelItem.NAME_RENTALHOUSE, ModelItem.ID_RENTALHOUSE, ModelItem.ICON_RENTALHOUSE));
                //}
            }
            //机房管理
            if (!userInfo.getAccounttype().equals("综管员")) {
                modelItems.add(new ModelItem(ModelItem.NAME_SAFETY_MANAGE, ModelItem.ID_SAFETY_MANAGE, ModelItem.ICON_SAFETY_MANAGE));
            }
        }

        //提审预约  仅供民警使用
        if (userInfo.getAccounttype().equals("民警")||userInfo.getPcs().contains("看守")) {
            modelItems.add(new ModelItem(ModelItem.NAME_TSYY, ModelItem.ID_TSYY, ModelItem.ICON_TSYY));
        }


        if (userInfo.getIsregester()!=0) {//没有注册权限只能使用提审预约功能
            UI.toast( activity,"如需要使用更多功能，请联系所属派出所管理员！");
        }
        mainMenuAdp = new MainMenuAdp(gridview, modelItems, R.layout.item_module, activity);
        mainMenuAdp.setState(BaseListAdp.STATE_LESS_ONE_PAGE);
        // 添加并且显示
        gridview.setAdapter(mainMenuAdp);
        // 添加消息处理
        gridview.setOnItemClickListener(new ItemClickListener());
    }


    // 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,
                                View arg1, int arg2, long arg3) {
            //检测是否连接网络
            int networkType = ((AppContext) activity.getApplication()).getNetworkType();
            if (BaseDataUtils.isFastClick()) {

            } else if (networkType == 0) {
                BaseUtil.showDialog(getString(R.string.network_not_connected), activity);
            }else if (!isOPen(activity)) {//判断是否打开Gps
                isOpenGps(activity);
            } else {
                switch (mainMenuAdp.getItem(arg2).getId()) {

                    case ModelItem.ID_CPZS:
                        openPlate();
                        break;

                    case ModelItem.ID_ESLDZS: //二三类点助手
                        openInformaton();
                        break;

                    case ModelItem.ID_ZACJ://治安基础信息采集
                        showActivity(ZAJCMainAty.class);
                        break;

                    case ModelItem.ID_QJCJ://警情采集
                        new AlertDialog.Builder(getActivity()).setTitle("提示")
                                .setMessage("由于市局接警系统升级后无法提供警情数据,现暂停警情采集工作")
                                .setNegativeButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                              dialog.dismiss();
                                            }
                                        })

                                .setCancelable(false).show();
                        //showActivity(JQCJAty.class);
                        break;

                    case ModelItem.ID_RYPC://人员 盘查
                        showActivity(PerReturnAty.class);
                        break;

                    case ModelItem.ID_CJGL://采集管理
                        showActivity(CollecManageAty.class);
                        break;

                    case ModelItem.ID_CJTJ://采集统计
                        showActivity(CollecTotalAty.class);
                        break;

                    case ModelItem.ID_RENTALHOUSE://出租屋
                        Intent intent = new Intent(activity, CaptureActivity.class);
                        startActivityForResult(intent, 100);
                        break;

                    case ModelItem.ID_SALALY://薪资查询
                        showActivity(SalaryMainAty.class);
                        break;

                    case ModelItem.ID_SAFETY_MANAGE://安全管理
                        showActivity(SafetyMainAty.class);
                        break;

                    case ModelItem.ID_TSYY://提审预约
                        showActivity(TsyyMainAty.class);
                        break;

                    case ModelItem.ID_XXHC://提审预约
                        showActivity(InfoCheckMainAty.class);
                        break;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data && requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String code = bundle.getString("result");
            if (code != null) {
                Intent intent=new Intent(activity, HouseCheckActivity.class);
                intent.putExtra("ldbh",code.substring(code.indexOf("=") + 1).trim());
                // intent.putExtra("code",code);
                startActivity(intent);
                // BaseUtil.showDialog(code.substring(code.indexOf("=") + 1).trim(), activity);
            } else {
                UI.toast(activity, "楼栋二维码识别错误!");
            }
        }
    }

    private void openPlate() {
        //车牌助手
        if (AppContext.instance.getLoginInfo().getAccounttype().equals("综管员")) {
            BaseUtil.showDialog("您没有权限使用此模块！", activity);
        } else {
            showActivity(PlateMainMenuActivity.class);
        }
    }



    private void openInformaton() {
        showActivity(CollectionInfoActivity.class);
    }


    @Override
    public void onDestroy() {
        //结束百度鹰眼服务
        if (new BaseDataUtils().notLeader()) {
            BaiduTraceFacade.stop();
        }
        if (mXlReceiver != null)
            activity.unregisterReceiver(mXlReceiver);
        activity.stopService(new Intent(activity, BaiduTraceService.class));
        super.onDestroy();
    }

    /**
     * 获取用户巡逻状态
     */
    protected void getLastXlGtStatus() {
        if (AppContext.instance().getLoginInfo() != null) {
            JSONObject object = new JSONObject();
            object.put("CJYH", AppContext.instance().getLoginInfo().getUsername());
            ApiResource.getLastDutyOrPlcBxOrKqToType(object.toJSONString(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = new String(bytes);
                    //gtzs 岗亭状态 1开始ed 2结束ed 3离岗ed 4擅自离岗ed 5继续值守ing
                    //kq 上班 下班
                    //xl
                    try {
                        boolean isNeedBaiduTrace = false;
                        JSONObject jsonObject = JSON.parseObject(result);
                        if ((null != jsonObject.getInteger("gtzs")) && (1 == jsonObject.getInteger("gtzs") || 4 == jsonObject.getInteger("gtzs") || 5 == jsonObject.getInteger("gtzs"))) {
                            isNeedBaiduTrace = true; //在值守状态或者擅自离岗都要开启轨迹
                            BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), activity);
                        }
                        if (null != jsonObject.getString("kq") && "上班".equals(jsonObject.getString("kq"))) {
                            isNeedBaiduTrace = true;  //上班状态需要开启轨迹
                        }
                        if (null != jsonObject.getString("xl") && (!DutyStartAty.TYPE_BEATS_END.equals(jsonObject.getString("xl")))) {
                            isNeedBaiduTrace = true; //在开始巡逻中，打卡中都需要开启轨迹
                            BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), activity);
                        }
                        if (isNeedBaiduTrace) {
                            Intent intent = new Intent(activity, BaiduTraceService.class);
                            activity.startService(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        onFailure(i, headers, bytes, e);
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    toast("获取失败,请稍后再试！");
                }
            });
        }
    }

    class XLReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getLastXlGtStatus();
        }
    }


}
