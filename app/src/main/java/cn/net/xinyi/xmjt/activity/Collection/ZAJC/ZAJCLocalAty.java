package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.CollectDBUtils;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;
import cn.net.xinyi.xmjt.utils.FileUtils;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;

/**
 * Created by hao.zhou on 2016/2/25.
 */
public class ZAJCLocalAty extends BaseActivity implements View.OnClickListener {
    /**listview**/
    @BindView(id = R.id.lv_store)
    private ListView lv_store;
    /**没有数据显示空布局**/
    @BindView(id = R.id.ll_empty_data)
    private LinearLayout ll_nodata;
    private ZAJCLocalAdp adapter;
    /**上传**/
    @BindView(id = R.id.btn_upload,click = true)
    private Button btn_upload;
    private int networkType;
    private List<ZAJCModle> ZAJCInfos;
    private ProgressDialog mProgressDialog ;
    private ZAJCJKModle jkInfo;
    private ZAJCXFModle xfInfo;
    private List<ZAJCCYZModle> cyyModles;
    private List<String> filePath=new ArrayList<String>();
    private List<String> fileName=new ArrayList<String>();
    private int uploadCount=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_store_info_manage);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getString(R.string.zamanage_manage));
        getActionBar().setHomeButtonEnabled(true);
        /***控件绑定**/
        AnnotateManager.initBindView(this);
        /***获得本地存储的数据
         * listview展示
         * */
        getLocalData();
        mProgressDialog = new ProgressDialog(this);

    }


    private void getLocalData() {
        initItemClick();
        try {
            ZAJCInfos= DBHelperNew.getInstance(this).getZAJCDao().queryForAll();
            /**
             * 如果没有数据  显示空布局
             *   有数据  adapter
             * **/
            if (ZAJCInfos.size()==0){
                ll_nodata.setVisibility(View.VISIBLE);
                lv_store.setVisibility(View.GONE);
                btn_upload.setVisibility(View.GONE);
            }else {
                adapter=new ZAJCLocalAdp(lv_store,ZAJCInfos,R.layout.aty_store_info_manage_item,ZAJCLocalAty.this);
                lv_store.setAdapter(adapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void initItemClick() {
        lv_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ZAJCLocalAty.this, ZAJCEditorAty.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GeneralUtils.Info, ZAJCInfos.get(position));
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
                if (networkType != 1) {
                    BaseUtil.showDialog("只允许在 WIFI 网络下上传", ZAJCLocalAty.this);
                    break;
                }



//                //没有连接wifi提示，是否继续上传
//                if (networkType != 1) {
//                   diaoShow(getResources().getString(R.string.upload_no_wifi_tips));
//                    break;
//                }

                //显示上传的对话框
                diaoShow(getResources().getString(R.string.upload_store_tips));
                break;
        }
    }


    private void diaoShow(String message) {
        AlertDialog dialog = new AlertDialog.Builder(ZAJCLocalAty.this).
                setTitle("提示").setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
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
                                                dialog.dismiss();
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0://上传前检测
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
                    BaseUtil.showDialog("本次上传了" + msg.arg1 + "条采集信息",ZAJCLocalAty.this);
                    getLocalData();
                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！",ZAJCLocalAty.this);
                    break;

                case 4://上传检测完成
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到当前APP 版本过低，请回到民警通主菜单点击【系统设置】-【APP 更新】，按提示升级APP 后重新上传！", ZAJCLocalAty.this);
                    } else {
                        //开始上传采集信息
                        startUploadThread();
                    }
                    break;

                case 5://上传检测失败
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到你当前的WIFI网络无法连接互联网或不稳定，无法上传，请检查网络正常后重新上传！", ZAJCLocalAty.this);
                    } else {
                        BaseUtil.showDialog("检测失败！", ZAJCLocalAty.this);
                    }
                    break;

                case 7://没有wifi
                    mProgressDialog.cancel();
                    BaseUtil.showDialog(getResources().getString(R.string.upload_no_wifi_tips), ZAJCLocalAty.this);
                    break;

            }
        }
    };
    private void startUploadThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = ZAJCInfos.size();
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadImage(ZAJCInfos);
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
    void uploadImage(List<ZAJCModle> list) {
        uploadCount = 0;
        for (int i = 0; i < list.size(); i++) {
            final ZAJCModle mInfo = list.get(i);
            /**根据关联id查询出数据库从业者信息**/
            cyyModles= CollectDBUtils.getCYZData(ZAJCLocalAty.this, mInfo.getGLID());
            /**根据关联id查询出数据库监控信息**/
            jkInfo= CollectDBUtils.getJKData(ZAJCLocalAty.this, mInfo.getGLID());
            /**根据关联id查询出数据库消防信息**/
            xfInfo= CollectDBUtils.getXFData(ZAJCLocalAty.this, mInfo.getGLID());
            for(ZAJCCYZModle cyzInfo:cyyModles) {
                if (!StringUtils.isEmpty(cyzInfo.getIV_CYZQSZ())&& FileUtils.fileIsExists(cyzInfo.getIV_CYZQSZ())) {
                    String path=cyzInfo.getIV_CYZQSZ();
                    filePath.add(path);
                    fileName.add(BaseUtil.getFileNameNoEx(path));
                }
            }
            if (null!=jkInfo&&!StringUtils.isEmpty(jkInfo.getIV_JKPMT())&& FileUtils.fileIsExists(jkInfo.getIV_JKPMT())) {
                String path = jkInfo.getIV_JKPMT();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            if (null!=jkInfo&&!StringUtils.isEmpty(jkInfo.getIV_SXTQJT())&& FileUtils.fileIsExists(jkInfo.getIV_SXTQJT())) {
                String path = jkInfo.getIV_SXTQJT();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            if (null!=xfInfo&&!StringUtils.isEmpty(xfInfo.getIV_XFQJT())&& FileUtils.fileIsExists(xfInfo.getIV_XFQJT())) {
                String path = xfInfo.getIV_XFQJT() ;
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            if (((AppContext) getApplication()).getNetworkType()!=1){
                Message msg = new Message();
                msg.what = 7;
                handler.sendMessage(msg);
                /**判断数据是不是为空  然后判断有没有照片**/
            } else  if(filePath.size()!=0 || !StringUtils.isEmpty(mInfo.getIV_MPHQJT())
                    ||!StringUtils.isEmpty(mInfo.getIV_DMQJT())||!StringUtils.isEmpty(mInfo.getIV_YYZZ())){
                if (!StringUtils.isEmpty(mInfo.getIV_MPHQJT())&& FileUtils.fileIsExists(mInfo.getIV_MPHQJT())) {
                    String path = mInfo.getIV_MPHQJT();
                    filePath.add(path);
                    fileName.add(BaseUtil.getFileNameNoEx(path));
                }

                if (!StringUtils.isEmpty(mInfo.getIV_DMQJT())&& FileUtils.fileIsExists(mInfo.getIV_DMQJT())) {
                    String path = mInfo.getIV_DMQJT();
                    filePath.add(path);
                    fileName.add(BaseUtil.getFileNameNoEx(path));
                }

                if (!StringUtils.isEmpty(mInfo.getIV_YYZZ())&& FileUtils.fileIsExists(mInfo.getIV_YYZZ())) {
                    String path = mInfo.getIV_YYZZ();
                    filePath.add(path);
                    fileName.add(BaseUtil.getFileNameNoEx(path));
                }
                if (!StringUtils.isEmpty(mInfo.getIV_QT())&& FileUtils.fileIsExists(mInfo.getIV_QT())) {
                    String path = mInfo.getIV_QT();
                    filePath.add(path);
                    fileName.add(BaseUtil.getFileNameNoEx(path));
                }
                if (!StringUtils.isEmpty(mInfo.getIV_HBPW())&& FileUtils.fileIsExists(mInfo.getIV_HBPW())) {
                    String path = mInfo.getIV_HBPW();
                    filePath.add(path);
                    fileName.add(BaseUtil.getFileNameNoEx(path));
                }
                if (!StringUtils.isEmpty(mInfo.getIV_TZHY())&& FileUtils.fileIsExists(mInfo.getIV_TZHY())) {
                    String path = mInfo.getIV_TZHY();
                    filePath.add(path);
                    fileName.add(BaseUtil.getFileNameNoEx(path));
                }
                if (!StringUtils.isEmpty(mInfo.getIV_XFYS())&& FileUtils.fileIsExists(mInfo.getIV_XFYS())) {
                    String path = mInfo.getIV_XFYS();
                    filePath.add(path);
                    fileName.add(BaseUtil.getFileNameNoEx(path));
                }

                //上传图片
                ApiResource.uploadZAJCImage(filePath, fileName, new AsyncHttpResponseHandler() {
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
            } else {
                uploadInfo(mInfo);
            }
        }
    }

    //同步上传采集数据到服务端
    public void uploadInfo(final ZAJCModle info) {
        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        /***治安基础信息*/
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        /***消防信息*/
        JSONObject jo1= JSON.parseObject(JSON.toJSONString(xfInfo));
        /***监控信息*/
        JSONObject jo2=JSON.parseObject(JSON.toJSONString(jkInfo));

        /**治安基础*/
        if (jo != null) {
            jo.remove(ZAJCModle.sZAJCID);
            jo.remove("gLID");
            jo.remove("cJFL");

            if (StringUtils.isEmpty(info.getLB())) {
                /**
                 /**
                 * @parm info.getLB
                 * 根据类别中文获取字典编码的值**/
                String lb=zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.XXCJ_ZAJCFL,info.getLB());
                jo.put("lB", lb);

                /**
                 * @param lb
                 * 根据类别父编码(lb)获取全部列表
                 * 根据@param info.getCJFL()
                 * 获得采集分类
                 * **/
                String type=zdUtils.getZdlbAndFzdbmAndZdzToZdbm(GeneralUtils.XXCJ_ZAJCFL_JT,lb,info.getCJFL());
                jo.put("cJFL", type);
            }else {
                /**根据zdz获取zdbm**/
                String CJFL= zdUtils.getZdlbAndZdbmToZdz(GeneralUtils.XXCJ_ZAJCFL,info.getCJFL())==null?info.getCJFL():zdUtils.getZdlbAndZdbmToZdz(GeneralUtils.XXCJ_ZAJCFL,info.getCJFL());
                jo.put("cJFL", CJFL);
            }

            jo.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
            jo.put("CJDW", AppContext.instance.getLoginInfo().getOrgancode());

            if (StringUtils.isEmpty(info.getIV_MPHQJT())) {
                jo.remove(ZAJCModle.sIV_MPHQJT);
                jo.put(ZAJCModle.sIV_MPHQJT, BaseUtil.getFileName(info.getIV_MPHQJT()));
            }

            if (StringUtils.isEmpty(info.getIV_DMQJT())) {
                jo.remove(ZAJCModle.sIV_DMQJT);
                jo.put(ZAJCModle.sIV_DMQJT, BaseUtil.getFileName(info.getIV_DMQJT()));
            }
            if (StringUtils.isEmpty(info.getIV_YYZZ())) {
                jo.remove(ZAJCModle.sIV_YYZZ);
                jo.put(ZAJCModle.sIV_YYZZ, BaseUtil.getFileName(info.getIV_YYZZ()));
            }
            if (StringUtils.isEmpty(info.getIV_QT())) {
                jo.remove(ZAJCModle.sIV_QT);
                jo.put(ZAJCModle.sIV_QT, BaseUtil.getFileName(info.getIV_QT()));
            }
            if (StringUtils.isEmpty(info.getIV_TZHY() )) {
                jo.remove(ZAJCModle.sIV_TZHY);
                jo.put(ZAJCModle.sIV_TZHY, BaseUtil.getFileName(info.getIV_TZHY()));
            }
            if (StringUtils.isEmpty(info.getIV_XFYS())) {
                jo.remove(ZAJCModle.sIV_XFYS);
                jo.put(ZAJCModle.sIV_XFYS, BaseUtil.getFileName(info.getIV_XFYS()));
            }
            if (StringUtils.isEmpty(info.getIV_HBPW())) {
                jo.remove(ZAJCModle.sIV_HBPW);
                jo.put(ZAJCModle.sIV_HBPW, BaseUtil.getFileName(info.getIV_HBPW()));
            }
            jsonObject.put("ZAJCXX", jo.toString());
        }

        /**消防*/
        if (jo1 != null) {
            jo1.remove("gLID");
            if (xfInfo.getIV_XFQJT() != null) {
                jo1.remove(ZAJCXFModle.sIV_XFQJT);
                jo1.put(ZAJCXFModle.sIV_XFQJT, BaseUtil.getFileName(xfInfo.getIV_XFQJT()));
            }
            jsonObject.put("XFXX", jo1.toString());
        }
        /**监控*/
        if (jo2 != null) {
            jo2.remove("jKID");
            jo2.remove("gLID");
            if (jkInfo.getIV_JKPMT() != null) {
                jo2.remove(ZAJCJKModle.sIV_JKPMT);
                jo2.put(ZAJCJKModle.sIV_JKPMT, BaseUtil.getFileName(jkInfo.getIV_JKPMT()));
            }

            if (jkInfo.getIV_SXTQJT() != null) {
                jo2.remove(ZAJCJKModle.sIV_SXTQJT);
                jo2.put(ZAJCJKModle.sIV_SXTQJT, BaseUtil.getFileName(jkInfo.getIV_SXTQJT()));
            }
            jsonObject.put("JKXX", jo2.toString());

        }
        /**从业者*/
        for(ZAJCCYZModle cyzInfo:cyyModles) {
            JSONObject jo3 = JSON.parseObject(JSON.toJSONString(cyzInfo));
            jo3.remove("cYZID");
            jo3.remove("gLID");
            if (cyzInfo.getIV_CYZQSZ() != null) {
                jo3.remove(ZAJCCYZModle.sIV_CYZQSZ);
                jo3.put(ZAJCCYZModle.sIV_CYZQSZ, BaseUtil.getFileName(cyzInfo.getIV_CYZQSZ()));
            }
            jsonArray.add(jo3);
        }

        jsonObject.put("CYZ",jsonArray.toString());
        /**转成json*/
        String json = jsonObject.toString();
        ApiResource.addZAJCInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);

                if (!result.isEmpty() && result.startsWith("true")) {
                    uploadCount++;
                    /**删除采集本地照片**/
                    CollectDBUtils.DeleteZALocal(ZAJCLocalAty.this, cyyModles, jkInfo,xfInfo,info);
                    mProgressDialog.incrementProgressBy(1);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            getLocalData();
        }
    }


    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            ZAJCLocalAty.this.finish();
        }
        // super.dispatchKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }


    /***activity退出*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ZAJCLocalAty.this.finish();
                break;
            default:
                break;
        }
        return true;
    }

}