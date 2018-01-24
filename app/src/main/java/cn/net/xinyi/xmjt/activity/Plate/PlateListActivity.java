package cn.net.xinyi.xmjt.activity.Plate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Plate.Adapter.PlateListAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.PlateInfoModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.FileUtils;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.View.BaseSwipeListViewListener;
import cn.net.xinyi.xmjt.utils.View.SettingsManager;
import cn.net.xinyi.xmjt.utils.View.SwipeListView;

public class PlateListActivity extends FragmentActivity implements
        OnItemClickListener {

    public static final String KEY_PLATE_INFO = "PLATE_INFO";
    public static final String KEY_PLATE_IMAGE = "PLATE_IMAGE";

    private static final String TAG = "PlateListActivity";
    private SwipeListView lv_PlateList;
    private ViewStub emptyView;

    List<PlateInfoModle> plateList;
    private PlateListAdapter mAdapter;
    private View mBtn_upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_list);

        initResource();
        listViewShow();

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(R.string.title_plate_manage);
        getActionBar().setHomeButtonEnabled(true);
        mBtn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPlate();
            }
        });
    }

    private void uploadPlate() {
        //检测本地是否有未上传的车牌
        if (plateList == null || plateList.size() == 0) {
            BaseUtil.showDialog("没有需要上传的车牌！", this);
            return;
        }
        //检测是否连接wifi网络
        int networkType = ((AppContext) getApplication()).getNetworkType();
        if (networkType == 0) {
            BaseUtil.showDialog("当前无可用的网络连接，无法上传", this);
            return;
        }
        //只允许在 WIFI 下上传
        if (networkType != 1) {
            BaseUtil.showDialog("只允许在 WIFI 网络下上传", this);
            return;
        }
        //开始上传
        diaoShow();
    }

    int noImageCount = 0;
    int uploadCount = 0;
    private ProgressDialog mProgressDialog = null;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://上传前检测
                    mProgressDialog = new ProgressDialog(PlateListActivity.this);
                    mProgressDialog.setTitle("上传前检测");
                    mProgressDialog.setMessage("检测网络情况及APP是否最新版本");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:// 上传进度条显示
                    int count = msg.arg1;
                    String msgText = "本机共有 " + count + " 个车牌需要上传";
                    mProgressDialog = new ProgressDialog(PlateListActivity.this);
                    mProgressDialog.setProgress(count);
                    mProgressDialog.setMax(count);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("车牌上传中");
                    mProgressDialog.setMessage(msgText);
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 2:// 上传之后
                    mProgressDialog.cancel();
                    if (noImageCount > 0) {
                        BaseUtil.showDialog("当前" + noImageCount + "条数据上传失败，请检查图片是否丢失", PlateListActivity.this);
                    } else {
                        BaseUtil.showDialog("本次成功上传了" + msg.arg1 + "条采集信息", PlateListActivity.this);
                        listViewShow();
                    }
                    break;
                case 3:// 上传失败
                    mProgressDialog.cancel();
                    if (noImageCount > 0) {
                        BaseUtil.showDialog("当前" + noImageCount + "条数据上传失败，请检查图片是否丢失", PlateListActivity.this);
                    } else {
                        BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", PlateListActivity.this);
                    }
                    break;

                case 4://上传检测完成
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到当前APP 版本过低，请回到民警通主菜单点击【系统设置】-【APP 更新】，按提示升级APP 后重新上传！", PlateListActivity.this);
                    } else {
                        startUploadThread();
                    }
                    break;

                case 5://上传检测失败
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到你当前的WIFI网络无法连接互联网或不稳定，无法上传，请检查网络正常后重新上传！", PlateListActivity.this);
                    } else {
                        BaseUtil.showDialog("检测失败！", PlateListActivity.this);
                    }
                    break;


                case 6:// 上传进度条显示
                    int count1 = msg.arg1;
                    String msgText1 = "本机共有 " + count1 + " 个数据需要更新";
                    mProgressDialog = new ProgressDialog(PlateListActivity.this);
                    mProgressDialog.setProgress(count1);
                    mProgressDialog.setMax(count1);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("数据上传中");
                    mProgressDialog.setMessage(msgText1);
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 7:// 上传之后
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("本次更新了" + msg.arg1 + "条数据", PlateListActivity.this);
                    break;
                case 8:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("更新失败，请稍候重试！", PlateListActivity.this);
                    break;


                default:
                    break;
            }
        }
    };

    private void diaoShow() {
        AlertDialog dialog = new AlertDialog.Builder(this).
                setTitle(getString(R.string.tips)).setMessage("确定上传车牌？")
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);

                        //检查APP是否最新版本及网络是否连通
                        new Thread() {
                            @Override
                            public void run() {
                                ApiResource.getVersionByAppid(AppContext.APP_ID, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        Message msg = new Message();
                                        try {
                                            String result = new String(bytes);
                                            if (result != null && result.trim() != "") {
                                                //获取服务器端版本号
                                                JSONObject jo_v = JSONObject.parseObject(result);
                                                int newVersionCode = Integer.parseInt(jo_v.getString(GeneralUtils.BUILDNUMBER));

                                                //获取当前版本号
                                                PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                                                int curVersionCode = info.versionCode;

                                                if (newVersionCode > curVersionCode) {
                                                    //有新版本
                                                    msg.arg1 = 0;
                                                } else {
                                                    msg.arg1 = 1;
                                                }
                                                msg.what = 4;
                                                handler.sendMessage(msg);
                                            } else {
                                                onFailure(i, headers, bytes, null);
                                            }
                                        } catch (Exception e1) {
                                            onFailure(i, headers, bytes, null);
                                        }
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                        Message msg = new Message();
                                        msg.what = 5;
                                        if (i == 0) {
                                            //检测超时或当前网络不能连接到互联网
                                            msg.arg1 = 0;
                                        } else {
                                            msg.arg1 = 1;
                                        }
                                        handler.sendMessage(msg);
                                    }
                                });
                            }
                        }.start();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                        dialog.cancel();
                    }
                }).create();
        dialog.show();

    }

    private void startUploadThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = plateList.size();
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadPlateImage(plateList);
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

    //同步上传车牌图片到服务端
    void uploadPlateImage(List<PlateInfoModle> list) {
        uploadCount = 0;
        noImageCount = 0;

        for (int i = 0; i < list.size(); i++) {
            final PlateInfoModle mPlateInfo = list.get(i);
            if (!StringUtils.isEmpty(mPlateInfo.getPlateImage()) && FileUtils.fileIsExists(mPlateInfo.getPlateImage())) {
                String filePath = mPlateInfo.getPlateImage();
                String fileName = BaseUtil.getFileNameNoEx(filePath);
                //上传车牌图片
                ApiResource.uploadCapturePlateImage(fileName, filePath, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                        String result = new String(bytes);
                        if (i == 200 && result != null && result.startsWith("true")) {
                            uploadPlateInfo(mPlateInfo);
                        } else {
                            onFailure(i, headers, bytes, null);
                        }
                    }

                    @Override
                    public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                        if (bytes != null) {
                            String result = new String(bytes);
                            Log.d(TAG, result);
                        }
                    }
                });
            } else {
                noImageCount++;
            }
        }
    }

    //同步上传车牌数据到服务端
    public void uploadPlateInfo(final PlateInfoModle info) {

        String pcs = AppContext.instance.getLoginInfo().getPcs();
        String jws = AppContext.instance.getLoginInfo().getJws();

        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("id");
            jo.remove("plateImage");
            jo.remove("plateThumb");
            jo.put("plateImage", BaseUtil.getFileName(info.getPlateImage()));
            jo.put("pcs", pcs);
            jo.put("jws", jws);
        }

        String json = jo.toJSONString();
        //保存上传日志
        //AppConfig.saveUploadLog(json);

        ApiResource.addCapturePlate(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);

                if (!result.isEmpty() && result.startsWith("true")) {
                    //已上传记数+1；
                    uploadCount++;
                    //删除本地数据库中的记录
                    boolean flag = delPlateInfo(info.getId());

                    if (flag) {
                        //删除本地图片
                        File plateImage = new File(info.getPlateImage());
                        File plateImageThub = new File(info.getPlateThumb());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                        if (plateImageThub.exists()) {
                            plateImageThub.delete();
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
                    Log.d(TAG, result);
                }
            }
        });
    }

    private void initResource() {
        lv_PlateList = (SwipeListView) findViewById(R.id.plate_listview);
        emptyView = (ViewStub) findViewById(R.id.empty_view);
        mBtn_upload = findViewById(R.id.btn_upload);
        lv_PlateList.setEmptyView(emptyView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 设置车牌列表显示内容
     */
    private void listViewShow() {
        plateList = this.getLocalData();
        if (plateList == null || plateList.size() == 0) {
            mBtn_upload.setVisibility(View.GONE);
        }
        mAdapter = new PlateListAdapter(PlateListActivity.this,
                plateList, lv_PlateList);

        setListViewStyle();

        lv_PlateList.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d",
                        position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
                Intent mIntent = new Intent(PlateListActivity.this,
                        PlateDetailViewActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(KEY_PLATE_INFO, plateList.get(position));
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    delPlateInfo(position);
                    plateList.remove(position);
                }
                //plateList = getJKSLocalData();
                mAdapter.notifyDataSetChanged();
            }
        });


        lv_PlateList.setAdapter(mAdapter);
        // lv_PlateList.setOnItemClickListener(this);
    }


    /**
     * 删除本地数据库中已上传车牌
     *
     * @param id
     * @return
     */
    private boolean delPlateInfo(int id) {
        boolean mFlag = false;
        DBOperation dbo = new DBOperation(this);
        mFlag = dbo.delPlateInfo(id);
        dbo.clossDb();

        return mFlag;
    }

    /*public boolean delPlateInfo(int position) {
        boolean mFlag = false;
        DBOperation dbo = new DBOperation(this);
        int id = plateList.get(position).getId();
        String imagePath = plateList.get(position).getPlateImage();
        String thumbPath = plateList.get(position).getPlateThumb();
        mFlag = dbo.delPlateInfo(id);
        dbo.clossDb();

        //删除本地图片
        if (mFlag) {
            File plateImage = new File(imagePath);
            File plateImageThub = new File(thumbPath);
            if (plateImage.exists()) {
                plateImage.delete();
            }
            if (plateImageThub.exists()) {
                plateImageThub.delete();
            }
        }

        return mFlag;
    }*/

    /**
     * 设置listview的样式；
     */
    private void setListViewStyle() {
        SettingsManager settings = SettingsManager.getInstance();
        settings.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
        settings.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
        settings.setSwipeAnimationTime(0);
        settings.setSwipeOffsetLeft(300);
        settings.setSwipeOffsetRight(0);

        lv_PlateList.setSwipeMode(settings.getSwipeMode());
        lv_PlateList.setSwipeActionLeft(settings.getSwipeActionLeft());
        lv_PlateList.setSwipeActionRight(settings.getSwipeActionRight());
        lv_PlateList.setOffsetLeft(convertDpToPixel(settings
                .getSwipeOffsetLeft()));
        lv_PlateList.setOffsetRight(convertDpToPixel(settings
                .getSwipeOffsetRight()));
        lv_PlateList.setAnimationTime(settings.getSwipeAnimationTime());
        lv_PlateList.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }

    /**
     * 像素转换
     *
     * @param dp
     * @return
     */
    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    /**
     * 获取本地数据库保存的车牌数据
     *
     * @return
     */
    private List<PlateInfoModle> getLocalData() {
        String account = AppContext.instance.getLoginInfo().getUsername();
        String polno = AppContext.instance.getLoginInfo().getPoliceno();
        if (account.isEmpty())
            return null;

        DBOperation dbo = new DBOperation(this);
        List<PlateInfoModle> list = dbo.getPlateInfoList(account, polno);
        dbo.clossDb();
        return list;
    }

    public String uploadToServer(PlateInfoModle plateInfo) {

        return "";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent mIntent = new Intent(PlateListActivity.this,
                PlateDetailViewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(KEY_PLATE_INFO, plateList.get(position));
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }
}
