package cn.net.xinyi.xmjt.activity.Main.Setting;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import java.util.ArrayList;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by mazhongwang on 14/11/25.
 */
public class OfflineMapActivity extends BaseActivity implements MKOfflineMapListener {
    private MKOfflineMap mOffline = null;
    /**
     * 已下载的离线地图信息列表
     */
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private LocalMapAdapter localMapAdapter = null;
    private ArrayList<MKOLSearchRecord> allCityList = null;
    private AllCityMapAdapter cityMapAdapter = null;
    private int defaultCityId = 340;
    private int networkType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        initView();

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("离线地图管理");
        getActionBar().setHomeButtonEnabled(true);

        networkType = ((AppContext) getApplication()).getNetworkType();
        if(networkType == 0){
            BaseUtil.showDialog("当前无可用的网络连接，无法下载或更新离线地图", this);
        } else if(networkType == 1){
            //自动下载深圳的离线地图
            start(defaultCityId);
        }else{
            BaseUtil.showDialog("只允许在 WIFI 网络下载或更新离线地图", this);
        }

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

    private void initView() {
        // 获取所有支持离线地图的城市
        allCityList = mOffline.getHotCityList();
        if(allCityList == null){
            allCityList = new ArrayList<MKOLSearchRecord>();
        }

        ListView allCityListView = (ListView) findViewById(R.id.allcitylist);
        cityMapAdapter = new AllCityMapAdapter();
        allCityListView.setAdapter(cityMapAdapter);

        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }

        ListView localMapListView = (ListView) findViewById(R.id.localmaplist);
        localMapAdapter = new LocalMapAdapter();
        localMapListView.setAdapter(localMapAdapter);

        //开始下载深圳地图
        start(defaultCityId);
    }


    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                    updateDownloadProcess();
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
        }

    }

    /**
     * 更新状态显示
     */
    public void updateDownloadProcess() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        localMapAdapter.notifyDataSetChanged();
    }

    /**
     * 更新状态显示
     */
    public void updateView() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        localMapAdapter.notifyDataSetChanged();

        allCityList = mOffline.getHotCityList();
        if (allCityList == null) {
            allCityList = new ArrayList<MKOLSearchRecord>();
        }
        cityMapAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onPause() {
//        int cityid = Integer.parseInt(cidView.getText().toString());
//        mOffline.pause(cityid);
//        super.onPause();
//    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    @Override
    protected void onDestroy() {
        /**
         * 退出时，销毁离线地图模块
         */
        mOffline.destroy();
        super.onDestroy();
    }


    /**
     * 开始下载
     *
     * @param cityid
     */
    public void start(int cityid) {
        mOffline.start(cityid);
//        Toast.makeText(this, "开始下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
//                .show();
        updateView();
    }

    /**
     * 暂停下载
     *
     * @param cityid
     */
    public void stop(int cityid) {

        mOffline.pause(cityid);
        Toast.makeText(this, "暂停下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
                .show();
        updateView();
    }

    /**
     * 删除离线地图
     *
     * @param cityid
     */
    public void remove(int cityid) {
        mOffline.remove(cityid);
        Toast.makeText(this, "删除离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
                .show();
        updateView();
    }

    public boolean isMapDownload(int cityid){
        boolean mFlag = false;
        for(int i=0; i<localMapList.size(); i++){
            if(localMapList.get(i).cityID == cityid){
                mFlag = true;
                break;
            }
        }
        return  mFlag;
    }

    /**
     * 离线地图管理列表适配器
     */
    public class LocalMapAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return localMapList.size();
        }

        @Override
        public Object getItem(int index) {
            return localMapList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup arg2) {
            MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
            view = View.inflate(OfflineMapActivity.this,
                    R.layout.offline_localmap_list, null);
            initViewItem(view, e);
            return view;
        }

        void initViewItem(View view, final MKOLUpdateElement e) {
            Button remove = (Button) view.findViewById(R.id.remove);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView update = (TextView) view.findViewById(R.id.update);
            TextView ratio = (TextView) view.findViewById(R.id.ratio);
            ratio.setText(e.ratio + "%");
            title.setText(e.cityName);
            if (e.update) {
                update.setText("可更新");
                remove.setText("更新");
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mOffline.remove(e.cityID);
                        mOffline.start(e.cityID);
                        updateView();
                    }
                });

                if(networkType!=1){
                    remove.setEnabled(false);
                }

            } else {
                update.setText("最新");
                remove.setText("删除");
                if(e.cityID == defaultCityId){
                    remove.setEnabled(false);
                }
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mOffline.remove(e.cityID);
                        updateView();
                    }
                });
            }



        }

    }


    /**
     * 离线地图管理列表适配器
     */
    public class AllCityMapAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allCityList.size();
        }

        @Override
        public Object getItem(int index) {
            return allCityList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup arg2) {
            MKOLSearchRecord e = (MKOLSearchRecord) getItem(index);
            view = View.inflate(OfflineMapActivity.this,
                    R.layout.offline_citymap_list, null);
            initViewItem(view, e);
            return view;
        }

        void initViewItem(View view, final MKOLSearchRecord e) {
            Button download = (Button) view.findViewById(R.id.download);
            TextView title = (TextView) view.findViewById(R.id.cityName);
            TextView downloadFlag = (TextView) view.findViewById(R.id.downloadFlag);
            TextView fileSize = (TextView) view.findViewById(R.id.fileSize);
            title.setText(e.cityName);
            fileSize.setText(OfflineMapActivity.this.formatDataSize(e.size));

            if (OfflineMapActivity.this.isMapDownload(e.cityID)) {
                download.setEnabled(false);
                downloadFlag.setVisibility(View.VISIBLE);
            } else {
                download.setEnabled(true);
                downloadFlag.setVisibility(View.INVISIBLE);
            }

            if(networkType!=1){
                download.setEnabled(false);
            }

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    start(e.cityID);
                    updateView();
                }
            });

        }

    }

}
