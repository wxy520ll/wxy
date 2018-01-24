package cn.net.xinyi.xmjt.activity.Collection.Person;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.PerReturnModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;
import cn.net.xinyi.xmjt.utils.FileUtils;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;

/**
 * Created by hao.zhou on 2016/2/25.
 */
public class PerReturnLocalAty extends BaseActivity2 implements View.OnClickListener{
    /**listview**/
    @BindView(id = R.id.lv_store)
    private ListView lv_pre;
    /**没有数据显示空布局**/
    @BindView(id = R.id.ll_empty_data)
    private LinearLayout ll_nodata;
    private Dao<PerReturnModle, Integer> perRetDao;
    private List<PerReturnModle> preRets;
    private PerReturnLocalAdp adapter;
    /**上传**/
    @BindView(id = R.id.btn_upload,click = true)
    private Button btn_upload;
    private int networkType;
    private ProgressDialog mProgressDialog ;
    private  int uploadCount = 0;
    private int noImageCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_store_info_manage);
        /***控件绑定**/
        AnnotateManager.initBindView(this);
        /***获得本地存储的数据
         * listview展示
         * */
        getLocalData();
    }


    private void getLocalData() {
        initItemClick();
        try {
            perRetDao= DBHelperNew.getInstance(this).getPreReturnDao();
            /**查询数据所有数据**/
            preRets = perRetDao.queryForAll();
            /**
             * 如果没有数据  显示空布局
             *   有数据  adapter
             * **/
            if (preRets.size()==0){
                ll_nodata.setVisibility(View.VISIBLE);
                lv_pre.setVisibility(View.GONE);
                btn_upload.setVisibility(View.GONE);
            }else {
                adapter=new PerReturnLocalAdp(lv_pre,preRets,R.layout.aty_store_info_manage_item,this);
                lv_pre.setAdapter(adapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initItemClick() {
        lv_pre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PerReturnLocalAty.this, PerReturnWatchAty.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GeneralUtils.Info, preRets.get(i));
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法上传", this);
                    break;
                }

                //没有连接wifi提示，是否继续上传
                if (networkType != 1) {
                    diaoShow(getResources().getString(R.string.upload_no_wifi_tips));
                    break;
                }
                //显示上传的对话框
                diaoShow(getResources().getString(R.string.upload_store_tips));
                break;
        }
    }

    private void diaoShow(String message) {
        AlertDialog  dialog = new AlertDialog.Builder(this).
                setTitle("提示").setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null!=mProgressDialog&&mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }



    private void startUploadThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = preRets.size();
                handler.sendMessage(msg);

                // 将数据及视频上传到服务器
                uploadImage(preRets);
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
    void uploadImage(List<PerReturnModle> list) {
        uploadCount = 0;
        noImageCount = 0;
        for (int i = 0; i < list.size(); i++) {
            final PerReturnModle mInfo = list.get(i);
            if (!StringUtils.isEmpty(mInfo.getIV_LYQZZP())&& FileUtils.fileIsExists(preRets.get(i).getIV_LYQZZP())) {
                String path=mInfo.getIV_LYQZZP();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));

                //上传图片
                ApiResource.uploadPerImage(filePath, fileName, new AsyncHttpResponseHandler() {
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

            }else {
                noImageCount++;
                fileName.clear();
                filePath.clear();
            }
        }
    }


    //同步上传采集数据到服务端
    public void uploadInfo(final PerReturnModle info) {
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("id");
            jo.remove("zFDZ");
            jo.remove("hFJG");
            jo.remove("hFLDBM");
            jo.remove(PerReturnModle.sLYQZZP);
            jo.remove(PerReturnModle.sMPHZP);
            jo.remove(PerReturnModle.sQTZP);
            jo.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
            jo.put("CJDW", AppContext.instance.getLoginInfo().getOrgancode());

            if (info.getIV_LYQZZP() != null) {
                jo.put(PerReturnModle.sLYQZZP, BaseUtil.getFileName(info.getIV_LYQZZP()));
            }

        }

        String json = jo.toJSONString();

        ApiResource.addPerInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);

                if (!result.isEmpty() && result.startsWith("true")) {
                    fileName.clear();
                    filePath.clear();
                    //已上传记数+1；
                    uploadCount++;
                    //删除本地数据库中的记录
                    try {
                        perRetDao = DBHelperNew.getInstance(PerReturnLocalAty.this).getPreReturnDao();
                        /**数据库执行删除操作**/
                        perRetDao.deleteById(info.getId());
                        if (info.getIV_LYQZZP() != null) {
                            File plateImage = new File(info.getIV_LYQZZP());
                            if (plateImage.exists()) {
                                plateImage.delete();
                            }
                        }

                        mProgressDialog.incrementProgressBy(1);
                    } catch (Exception e) {
                        BaseUtil.showDialog("删除失败，请稍后再试！", PerReturnLocalAty.this);
                        e.printStackTrace();
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0://上传前检测
                    mProgressDialog = new ProgressDialog(PerReturnLocalAty.this);
                    mProgressDialog.setTitle("上传前检测");
                    mProgressDialog.setMessage("检测网络情况及APP是否最新版本");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 1:// 上传进度条显示
                    mProgressDialog.cancel();
                    int count = msg.arg1;
                    String msgText = "本机共有 " + count + "条采集信息需要上传";
                    mProgressDialog = new ProgressDialog(PerReturnLocalAty.this);
                    mProgressDialog.setProgress(count);
                    mProgressDialog.setMax(count);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("信息上传中");
                    mProgressDialog.setMessage(msgText);
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 2:// 上传之后
                    mProgressDialog.cancel();
                    if (noImageCount>0){
                        BaseUtil.showDialog("当前"+noImageCount+"条数据上传失败，请检查图片是否丢失",PerReturnLocalAty.this);
                    }else {
                        BaseUtil.showDialog("本次成功上传了" + msg.arg1 + "条采集信息",PerReturnLocalAty.this);
                    }
                    getLocalData();
                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    if (noImageCount>0){
                        BaseUtil.showDialog("当前"+noImageCount+"数据上传失败，请检查图片是否丢失",PerReturnLocalAty.this);
                    }else {
                        BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！",PerReturnLocalAty.this);
                    }
                    break;

                case 4://上传检测完成
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到当前APP 版本过低，请回到民警通主菜单点击【系统设置】-【APP 更新】，按提示升级APP 后重新上传！", PerReturnLocalAty.this);
                    } else {
                        //开始上传采集信息
                        startUploadThread();
                    }
                    break;

                case 5://上传检测失败
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到你当前的WIFI网络无法连接互联网或不稳定，无法上传，请检查网络正常后重新上传！", PerReturnLocalAty.this);
                    } else {
                        BaseUtil.showDialog("检测失败！", PerReturnLocalAty.this);
                    }
                    break;

            }
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            getLocalData();
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.PER_RETURN_MANAGE);
    }


}