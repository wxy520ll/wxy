package cn.net.xinyi.xmjt.v527.presentation.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xinyi_tech.comm.base.BaseFragment;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.comm.util.ToolbarUtils;

import org.apache.http.Header;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.CollectionInfoActivity;
import cn.net.xinyi.xmjt.activity.Collection.CollecManageAty;
import cn.net.xinyi.xmjt.activity.Collection.CollecTotalAty;
import cn.net.xinyi.xmjt.activity.Collection.InfoCheck.InfoCheckMainAty;
import cn.net.xinyi.xmjt.activity.Collection.SafetyManage.SafetyMainAty;
import cn.net.xinyi.xmjt.activity.Collection.TSYY.TsyyMainAty;
import cn.net.xinyi.xmjt.activity.Main.Setting.SalaryMainAty;
import cn.net.xinyi.xmjt.activity.Plate.CameraActivity;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutyBeatsListAty;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutyPoliceAty;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutySettingMainAty;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutyStartAty;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.TotalAty;
import cn.net.xinyi.xmjt.activity.ZHFK.KQ.WorkCheckAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxListAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxZSAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcDstrbtMpAty;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.DutyOperationModle;
import cn.net.xinyi.xmjt.model.PlcBxWorkLog;
import cn.net.xinyi.xmjt.model.PoliceBoxModle;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.model.UserPlcBxInfo;
import cn.net.xinyi.xmjt.model.XinyiLatLng;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.zxing.activity.CaptureActivity;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.GzcxEntity;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.JqdtEntity;
import cn.net.xinyi.xmjt.v527.manager.MainItemModel;
import cn.net.xinyi.xmjt.v527.manager.ModelItemManager;
import cn.net.xinyi.xmjt.v527.presentation.MainPresenter;
import cn.net.xinyi.xmjt.v527.presentation.home.adapter.GzcxAdapter;
import cn.net.xinyi.xmjt.v527.presentation.home.adapter.ItemModelAdapter;
import cn.net.xinyi.xmjt.v527.presentation.home.adapter.JgdtAdapter;
import cn.net.xinyi.xmjt.v527.presentation.home.adapter.PersonInfoAdapter;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj.RycjPreActivity;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.ZacsListActivity;
import cn.net.xinyi.xmjt.v527.presentation.home.model.PersonInfo;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.CsxcActivity;
import cn.net.xinyi.xmjt.v527.util.DialogUtils;
import cn.net.xinyi.xmjt.v527.util.TimeUtils2;

import static android.content.Context.MODE_PRIVATE;
import static cn.net.xinyi.xmjt.utils.BaiduTraceFacade.userInfo;
import static com.alibaba.sdk.android.util.CommonUtils.toast;

/**
 * Created by Fracesuit on 2017/12/20.
 */

public class HomeFragment extends BaseFragment<MainPresenter> {

    public static final int NET_GZCX = 0;
    public static final int NET_JQDT = 1;
    public static final int NET_INIT = 2;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    final UserInfo loginInfo = AppContext.instance.getLoginInfo();
    private GzcxAdapter gzcxAdapter;
    private JgdtAdapter jgdtAdapter;

    @Override
    protected void requestData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home2;
    }

    ProgressDialog progressDialog;

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        initToolbar();
        initRecyclerView();
        getCameraInformation();

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载app...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);                    //设置不可点击界面之外的区域让对话框小时
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         //进度条类型
    }

    String currGzcxTime = TimeUtils2.getTime(0);

    public void initRecyclerView() {
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 100);

        final DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        //info
        SingleLayoutHelper layoutHelper = new SingleLayoutHelper();
        layoutHelper.setBgColor(ContextCompat.getColor(activity, R.color.white));

        final PersonInfo personInfo = new PersonInfo();
        personInfo.setName(loginInfo.getName());
        personInfo.setDeptInfo(loginInfo.getPcs() + "-" + loginInfo.getJws());
        personInfo.setHeadImg(loginInfo.getTxzp());
        personInfo.setJf("0");
        personInfo.setPm("0");
        PersonInfoAdapter personInfoAdapter = new PersonInfoAdapter(activity, layoutHelper, personInfo);
        adapters.add(personInfoAdapter);


        int startPosition = 1;
        if (ModelItemManager.canLook(0, 1, 2, 3, 4, 5, 6, 7, 8)) {
            startPosition = 3;
            //警格动态
            SingleLayoutHelper jkdtLayoutHelper = new SingleLayoutHelper();
            jkdtLayoutHelper.setBgColor(ContextCompat.getColor(activity, R.color.white));
            jgdtAdapter = new JgdtAdapter(jkdtLayoutHelper);
            jgdtAdapter.setOnAdapteItemClickListener(new JgdtAdapter.OnAdapteItemClickListener() {
                @Override
                public void onClick(int type, String param) {
                    switch (type) {
                        case 100:
                            final Intent intent = new Intent(activity, JqdtDetailListActivity.class);
                            intent.putExtra("time", param);
                            ActivityUtils.startActivity(intent);
                            break;
                        case 200:
                            DialogUtils.showDialog(activity, "警情动态", param);
                            break;
                    }

                }
            });
            adapters.add(jgdtAdapter);
            //工作成效
            SingleLayoutHelper gzcxLayoutHelper = new SingleLayoutHelper();
            gzcxLayoutHelper.setBgColor(ContextCompat.getColor(activity, R.color.white));
            gzcxAdapter = new GzcxAdapter(gzcxLayoutHelper);
            gzcxAdapter.setOnAdapteItemClickListener(new GzcxAdapter.OnAdapteItemClickListener() {
                @Override
                public void onClick(int type, String param) {
                    switch (type) {
                        case 100:
                            currGzcxTime = param;
                            mPresenter.getGzcx(param, NET_GZCX);
                            break;
                        case 200:
                            final Intent intent = new Intent(activity, GzcxDetailListActivity.class);
                            intent.putExtra("time", currGzcxTime);
                            intent.putExtra("type", param);
                            ActivityUtils.startActivity(intent);
                            break;
                    }
                }
            });
            adapters.add(gzcxAdapter);

            mPresenter.initHomeData(NET_INIT);
        }

        adapters.add(getItemAdapter(startPosition));


        delegateAdapter.setAdapters(adapters);

    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    private void showActivity(Class clazz) {
        ActivityUtils.startActivity(activity, clazz);
    }

    private void initToolbar() {
        ToolbarUtils.with(activity, mToolBar).setTitle(ResUtils.getString(R.string.app_fullname), true)
                .setInflateMenu(R.menu.menu_tab_menu_act)
                .build();
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(activity, CaptureActivity.class);
                intent.putExtra("from", "MainActivity");
                startActivityForResult(intent, 100);
                return true;
            }
        });
    }

    private ItemModelAdapter getItemAdapter(final int startPosition) {
        final List<MainItemModel> list = ModelItemManager.getMainItemByRole();
        GridLayoutHelper zaxcGridLayoutHelper = new GridLayoutHelper(4);
        zaxcGridLayoutHelper.setBgColor(ContextCompat.getColor(activity, R.color.white));
        zaxcGridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                final MainItemModel mainItemModel = list.get(position - startPosition);
                return mainItemModel.getDrawableId() == 0 ? 4 : 1;
            }
        });
        zaxcGridLayoutHelper.setVGap(SizeUtils.dp2px(10));
        zaxcGridLayoutHelper.setAutoExpand(false);
        final ItemModelAdapter itemModelAdapter = new ItemModelAdapter(zaxcGridLayoutHelper, list);
        itemModelAdapter.setOnItemClickListener(new ItemModelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainItemModel imageTitleModel, int position) {
                final MainItemModel mainItemModel = list.get(position);
                final String uniquenessId = mainItemModel.getUniquenessId();
                switch (uniquenessId) {
                    case "10":
                        getUserXlStatus(false);
                        break;//"勤务巡逻"
                    case "11":
                        getUserPlcBxStatus();
                        break;//"卡点勤务"
                    case "12":
                        ActivityUtils.startActivity(activity, WorkCheckAty.class);
                        break;//"工作督查"
                    case "13":
                        ActivityUtils.startActivity(activity, PlcDstrbtMpAty.class);
                        break;//"地图展示"
                    case "14":
                        ActivityUtils.startActivity(activity, DutySettingMainAty.class);
                        break;//"勤务设置"
                    case "15":
                        ActivityUtils.startActivity(activity, TotalAty.class);
                        break;//"勤务统计"
                    case "16":
                        onClickStart();
                        break;// "外出防逃"

                    case "100":
                        cpcj();
                         //showActivity(PlateMainMenuActivity.class);
                        break;////"车辆采集"
                    case "101":
                        showActivity(CollectionInfoActivity.class);
                        break;////"二三类点"
                    case "102":
                        showActivity(ZacsListActivity.class);
                        //showActivity(ZacsMainAty.class);
                        break;////"治安场所"
                    case "103":
                        showActivity(RycjPreActivity.class);
                        break;////"人员采集"
                    case "104":
                        Toast.makeText(activity, "待开发", Toast.LENGTH_SHORT).show();
                        break;////"线索管理"
                    case "105":
                        showActivity(CollecTotalAty.class);
                        break;////"采集统计"
                    case "106":
                        showActivity(CollecManageAty.class);
                        break;////"采集管理"

                    case "200":
                        ActivityUtils.startActivity(activity, CsxcActivity.class);
                        break;//"场所巡查"
                    case "201":
                        Toast.makeText(activity, "待开发", Toast.LENGTH_SHORT).show();
                        break;//"重点人员巡访"
                    case "202":
                        Toast.makeText(activity, "待开发", Toast.LENGTH_SHORT).show();
                        break;//"警情回访"
                    case "300":
                        showActivity(SafetyMainAty.class);
                        break;//_manage, "安全管理"
                    case "301":
                        showActivity(InfoCheckMainAty.class);
                        break;//eck, "信息核查"
                    case "302":
                        showActivity(TsyyMainAty.class);
                        break;//"提审预约"
                    case "303":
                        showActivity(SalaryMainAty.class);
                        break;//"薪资查询"
                    case "304":
                        Toast.makeText(activity, "待开发", Toast.LENGTH_SHORT).show();
                        break;//"健康管理"
                }
            }
        });
        return itemModelAdapter;
    }


    private void cpcj() {
        int networkType = ((AppContext) activity.getApplication()).getNetworkType();
        boolean isGpsOpen = ((AppContext)  activity.getApplication()).isGpsOPen();

        if (networkType == 0) {
            if (isGpsOpen) {
                Toast.makeText(activity, "当前无网络连接，定位方式为GPS定位",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "GPS未启用或搜索不到信号,无法定位采集坐标",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (!((AppContext) activity.getApplication()).checkCameraHardware()) {
            BaseUtil.showDialog("本设备没有摄像头", activity);
        } else if (readIntPreferences("PlateService", "picWidth") != 0 && readIntPreferences("PlateService", "picHeight") != 0
                && readIntPreferences("PlateService", "preWidth") != 0 && readIntPreferences("PlateService", "preHeight") != 0
                && readIntPreferences("PlateService", "preMaxWidth") != 0 && readIntPreferences("PlateService", "preMaxHeight") != 0) {
            showCamera(activity);
        } else {
            BaseUtil.showDialog("采集车牌需要您开启摄像头访问权限，请到设备的【设置】-【权限管理】-【应用程序】-【民警通】界面，勾选【信任此应用程序】，开启权限", activity);
        }
    }
    /**
     * 获取设备下面的硬件信息，基本的拍照分辨率，预览分辨率
     */
    private void getCameraInformation() {
        if (readIntPreferences("PlateService", "picWidth") == 0
                || readIntPreferences("PlateService", "picHeight") == 0
                || readIntPreferences("PlateService", "preWidth") == 0
                || readIntPreferences("PlateService", "preHeight") == 0
                || readIntPreferences("PlateService", "preMaxWidth") == 0
                || readIntPreferences("PlateService", "preMaxHeight") == 0) {

            Camera camera = null;
            int pre_Max_Width = 640;
            int pre_Max_Height = 480;
            final int Max_Width = 2048;
            final int Max_Height = 1536;
            boolean isCatchPicture = false;
            int picWidth = 2048;
            int picHeight = 1536;
            int preWidth = 320;
            int preHeight = 240;
            try {
                camera = Camera.open();
                if (camera != null) {
                    Camera.Parameters parameters = camera.getParameters();
                    List<Camera.Size> previewSizes = parameters
                            .getSupportedPreviewSizes();
                    Camera.Size size;
                    int second_Pre_Width = 0, second_Pre_Height = 0;
                    int length = previewSizes.size();
                    if (length == 1) {
                        size = previewSizes.get(0);
                        pre_Max_Width = size.width;
                        pre_Max_Height = size.height;
                    } else {
                        for (int i = 0; i < length; i++) {
                            size = previewSizes.get(i);
                            if (size.width <= Max_Width
                                    && size.height <= Max_Height) {
                                second_Pre_Width = size.width;
                                second_Pre_Height = size.height;
                                if (pre_Max_Width < second_Pre_Width) {
                                    pre_Max_Width = second_Pre_Width;
                                    pre_Max_Height = second_Pre_Height;
                                }
                            }
                        }
                    }

                    for (int i = 0; i < previewSizes.size(); i++) {
                        if (previewSizes.get(i).width == 640
                                && previewSizes.get(i).height == 480) {
                            preWidth = 640;
                            preHeight = 480;
                            break;
                        }
                        if (previewSizes.get(i).width == 320
                                && previewSizes.get(i).height == 240) {
                            preWidth = 320;
                            preHeight = 240;
                        }
                    }
                    if (preWidth == 0 || preHeight == 0) {
                        if (previewSizes.size() == 1) {
                            preWidth = previewSizes.get(0).width;
                            preHeight = previewSizes.get(0).height;
                        } else {
                            preWidth = previewSizes
                                    .get(previewSizes.size() / 2).width;
                            preHeight = previewSizes
                                    .get(previewSizes.size() / 2).height;
                        }
                    }

                    List<Camera.Size> PictureSizes = parameters
                            .getSupportedPictureSizes();
                    for (int i = 0; i < PictureSizes.size(); i++) {
                        if (PictureSizes.get(i).width == 2048
                                && PictureSizes.get(i).height == 1536) {
                            if (isCatchPicture == true) {
                                break;
                            }
                            isCatchPicture = true;
                            picWidth = 2048;
                            picHeight = 1536;
                        }
                        if (PictureSizes.get(i).width == 1600
                                && PictureSizes.get(i).height == 1200) {
                            isCatchPicture = true;
                            picWidth = 1600;
                            picHeight = 1200;
                        }
                        if (PictureSizes.get(i).width == 1280
                                && PictureSizes.get(i).height == 960) {
                            isCatchPicture = true;
                            picWidth = 1280;
                            picHeight = 960;
                            break;
                        }
                    }
                }

                writeIntPreferences("PlateService", "picWidth", picWidth);
                writeIntPreferences("PlateService", "picHeight", picHeight);
                writeIntPreferences("PlateService", "preWidth", preWidth);
                writeIntPreferences("PlateService", "preHeight", preHeight);
                writeIntPreferences("PlateService", "preMaxWidth",
                        pre_Max_Width);
                writeIntPreferences("PlateService", "preMaxHeight",
                        pre_Max_Height);
            } catch (Exception e) {

            } finally {
                if (camera != null) {
                    try {
                        camera.release();
                        camera = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void writeIntPreferences(String perferencesName, String key,
                                    int value) {
        SharedPreferences preferences = activity.getSharedPreferences(perferencesName,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public int readIntPreferences(String perferencesName, String key) {
        SharedPreferences preferences = activity.getSharedPreferences(perferencesName,
                MODE_PRIVATE);
        int result = preferences.getInt(key, 0);
        return result;
    }

    public void showCamera(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }


    public void onClickStart() {

        PackageInfo packageInfo;//PackageInfo所在包为android.content.pm
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo("com.kedacom.wcyjapp", 0);
            //Intent intent1 = getActivity().getPackageManager().getLaunchIntentForPackage("com.kedacom.wcyjapp");
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            new AlertDialog.Builder(getActivity()).setTitle("提示")
                    .setMessage("当前设备没有安装外逃app，是否进行下载安装。")
                    .setNegativeButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        new DownApkAsyncTask().execute("http://219.134.134.156:8088/xsmws-web/swts.apk");
                                    } catch (Exception e) {
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
        } else {
            System.out.println("已经安装");
            Intent i = new Intent();
            i.setFlags(100);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName("com.kedacom.wcyjapp",
                    "com.kedacom.wcyjapp.SplashActivity");
            i.setComponent(cn);
            startActivity(i);
        }


      /*  try {
            Intent intent1 = getActivity().getPackageManager().getLaunchIntentForPackage("com.kedacom.wcyjapp");
            if (intent1 == null) {
                new AlertDialog.Builder(getActivity()).setTitle("提示")
                        .setMessage("当前设备没有安装外逃app，是否进行下载安装。")
                        .setNegativeButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        try {
                                            new DownApkAsyncTask().execute("http://219.134.134.156:8088/xsmws-web/swts.apk");
                                        } catch (Exception e) {
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
            } else {
                System.out.println("已经安装");
                Intent i = new Intent();
                i.setFlags(100);
                ComponentName cn = new ComponentName("com.kedacom.wcyjapp",
                        "com.kedacom.wcyjapp.SplashActivity");
                i.setComponent(cn);
                startActivity(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }


    public class DownApkAsyncTask extends AsyncTask<String, Integer, String> {
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
            if (file != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                getActivity().startActivity(intent);
            }
        }
    }

    /**
     * 获取用户当前值守状态的请求json
     *
     * @return
     */
    private String getUserInfoJson() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("YH", loginInfo.getUsername());
        return requestJson.toJSONString();
    }

    private DutyOperationModle operInfos; //巡逻状态

    /**
     * 获取用户巡逻状态
     */
    protected void getUserXlStatus(final boolean dutyType) {
        showProgressBar(1, false);
        // showLoadding();
        if (AppContext.instance().getLoginInfo() != null) {
            ApiResource.getLastDutyOperation(userInfo.getUsername(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result.length() > 4) {//
                        hideProgressBar();
                        // stopLoading();
                        operInfos = JSON.parseObject(result, DutyOperationModle.class);
                        try {
                            if (dutyType) {//点击处警
                                if (DutyStartAty.TYPE_BEATS_END.equals(operInfos.getLAST_TYPE())) {
                                    showActivity(DutyPoliceAty.class);//处警
                                } else {
                                    Intent intent = new Intent(activity, DutyPoliceAty.class);
                                    intent.putExtra("data", operInfos.getID());
                                    startActivity(intent);
                                }
                            } else {//点击巡逻
                                if (DutyPoliceAty.TYPE_POLICE_START_A.equals(operInfos.getLAST_TYPE())) {
                                    new AlertDialog.Builder(activity)
                                            .setTitle(R.string.tips)
                                            .setMessage("您当前正在处警中，需结束才能进行巡逻！")
                                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(activity, DutyPoliceAty.class);
                                                    intent.putExtra("data", operInfos.getID());
                                                    startActivity(intent);
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, null)
                                            .setCancelable(false).show();
                                } else if (DutyStartAty.TYPE_BEATS_END.equals(operInfos.getLAST_TYPE())
                                        || (!DutyStartAty.TYPE_BEATS_START.equals(operInfos.getDUTY_OPR_TYPE())
                                        && DutyPoliceAty.TYPE_POLICE_END.equals(operInfos.getLAST_TYPE()))) {
                                    showActivity(DutyBeatsListAty.class);//巡段列表
                                } else {
                                    Intent intent = new Intent(activity, DutyStartAty.class);
                                    intent.putExtra("data", operInfos.getDUTY_BEATS_ID());
                                    startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            onFailure(i, headers, bytes, e);
                        }
                    } else {//没有开始巡逻或处警
                        if (dutyType) {
                            showActivity(DutyPoliceAty.class);//处警
                        } else {
                            showActivity(DutyBeatsListAty.class);//巡段列表
                        }
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    hideProgressBar();
                    toast("获取数据失败");
                }
            });
        }
    }

    UserPlcBxInfo mPlcBxInfo;

    private void getUserPlcBxStatus() {
        showProgressBar(1, true);
        ApiResource.getUserGtZt(getUserInfoJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgressBar();
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
                    hideProgressBar();
                    toast("获取值守状态失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                hideProgressBar();
                toast("获取值守状态失败");
            }
        });
    }

    private List<PoliceBoxModle> list;

    private void getGTData(final int ZSZT, final int GTJLID, final int id) {
        JSONObject jo = new JSONObject();
        jo.put("ID", id);
        jo.put("ISDISPLAYDEL", 1);
        String json = jo.toJSONString();
        ApiResource.getGtList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgressBar();
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
                hideProgressBar();
                toast("获取防控点数据失败");
            }
        });
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == NET_GZCX) {
            gzcxAdapter.update((GzcxEntity) data);
        } else if (requestCode == NET_JQDT) {
            jgdtAdapter.update((List<JqdtEntity>) data);
        } else if (requestCode == NET_INIT) {
            final List<Object> results = (List<Object>) data;
            for (Object o : results) {
                if (o instanceof GzcxEntity) {
                    gzcxAdapter.update((GzcxEntity) o);
                } else {
                    jgdtAdapter.update((List<JqdtEntity>) o);
                }
            }
        }
    }
}


