package cn.net.xinyi.xmjt.activity.Main.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.MainMenuAdp;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutyMainMenuAty;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutySettingMainAty;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutyStartAty;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.TotalAty;
import cn.net.xinyi.xmjt.activity.ZHFK.KQ.KQAty;
import cn.net.xinyi.xmjt.activity.ZHFK.KQ.WorkCheckAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxListAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxZSAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcDstrbtMpAty;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.ModelItem;
import cn.net.xinyi.xmjt.model.PlcBxWorkLog;
import cn.net.xinyi.xmjt.model.PoliceBoxModle;
import cn.net.xinyi.xmjt.model.UserPlcBxInfo;
import cn.net.xinyi.xmjt.model.XinyiLatLng;
import cn.net.xinyi.xmjt.service.BaiduTraceService;
import cn.net.xinyi.xmjt.utils.BaiduTraceFacade;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;


/**
 * Created by zhiren.zhang on 2016/8/19.
 */
public class MainDutyFragment extends BaseNewFragment {
    private static final String TAG = "MainDutyFragment";

    //public static MainDutyActivity instance;
    private View view;
    private LinearLayout.LayoutParams params;
    private GridView gridview;
    private MainMenuAdp mainMenuAdp;
    private ImageView iv_bg; //巡逻状态
    private List<PoliceBoxModle> list;
    XLReceiver mXlReceiver;

    UserPlcBxInfo mPlcBxInfo;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaiduTraceFacade.init(getContext());
        IntentFilter filter = new IntentFilter(DutyStartAty.XL_STATUS_ACTION); //注册巡逻状态改变receiver
        mXlReceiver = new XLReceiver();
        activity.registerReceiver(mXlReceiver, filter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载app...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);                    //设置不可点击界面之外的区域让对话框小时
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         //进度条类型
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.main_menu, null);
        initView(view);
        getLastXlGtStatus();
        return view;
    }

    private void initView(View view) {
        gridview = (GridView) view.findViewById(R.id.main_menu);
        iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        iv_bg.setBackgroundResource(R.drawable.main_fk);
        //初始化GridView
        intMenu();
    }

    private void intMenu() {
        List<ModelItem> modelItems = new ArrayList<>();
        modelItems.add(new ModelItem(ModelItem.NAME_YGKQ, ModelItem.ID_YGKQ, ModelItem.ICON_YGKQ));
        if (!AppContext.instance.getLoginInfo().getAccounttype().equals("综管员")) {
            modelItems.add(new ModelItem(ModelItem.NAME_XL, ModelItem.ID_XL, ModelItem.ICON_XL));
            modelItems.add(new ModelItem(ModelItem.NAME_GT, ModelItem.ID_GT, ModelItem.ICON_GT));
        }
        //巡逻设置对管理员开发
        if (null != AppContext.instance.getLoginPermis() && AppContext.instance.getLoginPermis().equals("admin")) {
            modelItems.add(new ModelItem(ModelItem.NAME_XLSZ, ModelItem.ID_XLSZ, ModelItem.ICON_XLSZ));
        }
        modelItems.add(new ModelItem(ModelItem.NAME_JLFB, ModelItem.ID_JLFB, ModelItem.ICON_JLFB));
        //模块对民警开发
        if (BaseDataUtils.isPOLICE()) {
            modelItems.add(new ModelItem(ModelItem.NAME_QWDC, ModelItem.ID_QWDC, ModelItem.ICON_QWDC));
        }
        if (!AppContext.instance.getLoginInfo().getAccounttype().equals("综管员")) {
            modelItems.add(new ModelItem(ModelItem.NAME_QWTJ, ModelItem.ID_QWTJ, ModelItem.ICON_QWTJ));
        }
        modelItems.add(new ModelItem(ModelItem.NAME_WCFT, ModelItem.ID_WCFT, ModelItem.ICON_WCFT));
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

            } else if (!isOPen(activity)) {//判断是否打开Gps
                isOpenGps(activity);
            } else {
                switch (mainMenuAdp.getItem(arg2).getId()) {
                    /**员工考勤  **/
                    case ModelItem.ID_YGKQ:
                        showActivity(KQAty.class);
                        break;
                    /**巡逻  **/
                    case ModelItem.ID_XL:
                        showActivity(DutyMainMenuAty.class);
                        break;
                    /**岗亭  **/
                    case ModelItem.ID_GT:
                        //showActivity(PlcBxAty.class);
                        getUserPlcBxStatus();
                        break;

                    /**巡逻设置  **/
                    case ModelItem.ID_XLSZ:
                        showActivity(DutySettingMainAty.class);
                        break;

                    /**警力分布  **/
                    case ModelItem.ID_JLFB:
                        showActivity(PlcDstrbtMpAty.class);
                        break;
                    /**勤务统计 **/
                    case ModelItem.ID_QWTJ:
                        showActivity(TotalAty.class);
                        break;
                    /**勤务督查 **/
                    case ModelItem.ID_QWDC:
                        showActivity(WorkCheckAty.class);
                        break;
                    case ModelItem.ID_WCFT:
                        onClickStart();
                        break;
                    default:
                        break;
                }
            }
        }
    }


    public void onClickStart() {
        // 通过包名获取要跳转的app，创建intent对象
        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.kedacom.wcyjapp");
        // 这里如果intent为空，就说名没有安装要跳转的应用
        if (intent != null) {
            startActivity(intent);
        } else {
            new AlertDialog.Builder(getActivity()).setTitle("提示")
                    .setMessage("当前设备没有安装外逃app，是否进行下载安装。")
                    .setNegativeButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        new DownApkAsyncTask().execute("http://219.134.134.156:8088/xsmws-web/swts.apk");
                                    }catch (Exception e){
                                        progressDialog.dismiss();
                                    }
                                }
                            })
                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }


    public class DownApkAsyncTask extends AsyncTask<String, Integer, String>
    {
        File file;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            try {
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int fileLength = conn.getContentLength();
                bis = new BufferedInputStream(conn.getInputStream());
                String fileName = Environment.getExternalStorageDirectory().getPath() + "/swts.apk";
                file = new File(fileName);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                byte data[] = new byte[4 * 1024];
                long total = 0;
                int count;
                while ((count = bis.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    fos.write(data, 0, count);
                    fos.flush();
                }
                fos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            openFile(file);                 //打开安装apk文件操作
            progressDialog.dismiss();  //关闭对话框
        }
        private void openFile(File file) {
            if (file!=null){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                getActivity().startActivity(intent);
            }
        }
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
                        }
                        if (null != jsonObject.getString("kq") && "上班".equals(jsonObject.getString("kq"))) {
                            isNeedBaiduTrace = true;  //上班状态需要开启轨迹
                        }
                        if ((null != jsonObject.getString("xl")) && (!DutyStartAty.TYPE_BEATS_END.equals(jsonObject.getString("xl")))) {
                            isNeedBaiduTrace = true; //在开始巡逻中，打卡中都需要开启轨迹
                            BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), activity.getApplicationContext());
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


    private void getUserPlcBxStatus() {
        showLoadding();
        ApiResource.getUserGtZt(getUserInfoJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    if ("\"-1\"".equals(result)) {
                        toast("获取用户失败，请重新登录");
                    } else {
                        List<UserPlcBxInfo> userPlcBxInfos = JSON.parseArray(result, UserPlcBxInfo.class);
                        if (userPlcBxInfos.size() > 0) {
                            mPlcBxInfo = userPlcBxInfos.get(0);
                            mPlcBxInfo.setLists(JSON.parseArray(mPlcBxInfo.getDATALIST(), PlcBxWorkLog.class));
                        }

                        if (mPlcBxInfo != null) {
                            if (mPlcBxInfo.getZSZT() == 1 || mPlcBxInfo.getZSZT() == 5) { //值守中
                                Intent intent2 = new Intent(PlcBxAty.PLC_BC_ACTION); //发送广播关闭值守监听
                                intent2.putExtra("type", 1);
                                if (mPlcBxInfo.getDATALIST() != null && mPlcBxInfo.getLists().size() > 0) {
                                    XinyiLatLng ll = new XinyiLatLng(mPlcBxInfo.getLists().get(0).getLAT(), mPlcBxInfo.getLists().get(0).getLNGT());
                                    intent2.putExtra("latLngt", ll);
                                    intent2.putExtra("GTID", mPlcBxInfo.getGTID());
                                    intent2.putExtra("GTZSID", mPlcBxInfo.getID());
                                    getActivity().sendBroadcast(intent2);
                                    getGTData(mPlcBxInfo.getZSZT(), mPlcBxInfo.getID(), mPlcBxInfo.getGTID());
                                }
                            } else if (mPlcBxInfo.getZSZT() == 3 || mPlcBxInfo.getZSZT() == 4) { //0,2,3,4 不在值守
                                getGTData(mPlcBxInfo.getZSZT(), mPlcBxInfo.getID(), mPlcBxInfo.getGTID());
                            } else {
                                Intent intent = new Intent(getActivity(), PlcBxListAty.class);
                                intent.setFlags(1);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), PlcBxListAty.class);
                            intent.setFlags(1);
                            startActivity(intent);
                        }

                    }
                } catch (JSONException e) {
                    stopLoading();
                    toast("获取值守状态失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取值守状态失败");
            }
        });
    }

    /**
     * 获取用户当前值守状态的请求json
     *
     * @return
     */
    private String getUserInfoJson() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("YH", userInfo.getUsername());
        return requestJson.toJSONString();
    }

    private void getGTData(final int ZSZT, final int GTJLID, final int id) {
        JSONObject jo = new JSONObject();
        jo.put("ID", id);
        jo.put("ISDISPLAYDEL", 1);
        String json = jo.toJSONString();
        ApiResource.getGtList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    list = JSON.parseArray(result, PoliceBoxModle.class);
                    if (list != null) {
                        for (PoliceBoxModle p : list) {
                            Intent intent = new Intent(getActivity(), PlcBxZSAty.class);
                            intent.putExtra("data", id);
                            intent.putExtra("id", GTJLID);
                            intent.putExtra("zszt", ZSZT);
                            intent.setFlags(1);
                            startActivity(intent);
                        }
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                } catch (JSONException e) {
                    onFailure(i, headers, bytes, e);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取防控点数据失败");
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mXlReceiver != null) {
            activity.unregisterReceiver(mXlReceiver);
        }
    }

    class XLReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getLastXlGtStatus();
        }
    }
}
