package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.IdCard.SharedPreferencesHelper;
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;
import cn.net.xinyi.xmjt.weiget.TipsDialog;

/**
 * Created by hao.zhou on 2016/2/22.
 */
public class ZAJCMainAty extends BaseActivity2 implements View.OnClickListener {

    @BindView(id = R.id.list )
    private ExpandableListView expandableListView ;
    //搜书文本框
    @BindView(id = R.id.atv_search )
    private AutoCompleteTextView atv_search ;
    //搜索按钮
    @BindView(id = R.id.tv_search ,click = true)
    private TextView tv_search ;
    //搜索取消
    @BindView(id = R.id.tv_cancel ,click = true)
    private TextView tv_cancel ;
    //空布局
    @BindView(id = R.id.ll_empty_data )
    private LinearLayout ll_empty_data ;
    //搜索listview
    @BindView(id = R.id.lv_search )
    private ListView  lv_search ;
    @BindView(id = R.id.fl_content )
    private FrameLayout fl_content ;
    // 这个数组是用来存储一级item的点击次数的，根据点击次数设置一级标签的选中、为选中状态
    private int[] group_checked = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    // 用来标识是否设置二?item背景色为绿色,初始值为-1既为选中状态
    private int child_groupId = -1;
    private int child_childId = -1;
    // 一级标签上的标题数据源
    private String[] group_title_arry ;
    // 子视图显示文字
    private String[][] child_text_array ;
    // 一级标签上的状态图片数据源
    int[] group_state_array = new int[] { R.drawable.ic_item_goto_right_tip,R.drawable.ic_item_goto_boot };
    // 一级标签上的logo图片数据源
    int[] group_logo_array = new int[] { R.drawable.dr_store, R.drawable.dr_ems, R.drawable.dr_hotel,
            R.drawable.dr_netbar,R.drawable.dr_recl, R.drawable.dr_server,
            R.drawable.dr_company,R.drawable.dr_other, R.drawable.dr_gw , R.drawable.dr_nb };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_cjitem);
        showDialog();
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        /**数据库读取 一、二级列表数据*/
        initData();
        if (!SharedPreferencesUtil.getBoolean(getActivity(),"iscolltips",false)){
            SharedPreferencesUtil.putBoolean(getActivity(),"iscolltips",true);
            BaseUtil.showDialog(getString(R.string.coll_tips),getActivity());
        }
    }

    private void initData() {
        Map<String,String> mapAayys= zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_ZAJCFL);
        /**获取采集分类一级数据**/
        group_title_arry = mapAayys.values().toArray(new String[mapAayys.values().size()]);
        Map<String,String> map1=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"1");
        Map<String,String> map2=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"2");
        Map<String,String> map3=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"3");
        Map<String,String> map4=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"4");
        Map<String,String> map5=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"5");
        Map<String,String> map6=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"6");
        Map<String,String> map7=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"7");
        Map<String,String> map8=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"8");
        Map<String,String> map9=  zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"9");
        Map<String,String> map10= zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT,"10");

        /**子视图显示文字**/
        child_text_array = new String[][] {
                map1.values().toArray(new String[map1.values().size()]),//店铺
                map2.values().toArray(new String[map2.values().size()]),//寄递物流
                map3.values().toArray(new String[map3.values().size()]),//旅业
                map4.values().toArray(new String[map4.values().size()]),//网吧
                map5.values().toArray(new String[map5.values().size()]),//二手回收
                map6.values().toArray(new String[map6.values().size()]),//休闲娱乐服务
                map7.values().toArray(new String[map7.values().size()]),//其它
                map8.values().toArray(new String[map8.values().size()]),//企业
                map9.values().toArray(new String[map9.values().size()]),//涉危爆场所
                map10.values().toArray(new String[map10.values().size()]),//重点内保单位
        };
        initView();
    }

    private void initView() {
        // 设置默认图标为不显示状态
        expandableListView.setGroupIndicator(null);
        // 为列表绑定数据源
        expandableListView.setAdapter(adapter);
        // 设置一级item点击的监听器
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                group_checked[groupPosition] = group_checked[groupPosition] + 1;
                // 刷新界面
                ((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
                return false;
            }
        });

        // 设置二级item点击的监听器
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {


            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // 将被点击的一丶二级标签的位置记录下来
                child_groupId = groupPosition;
                child_childId = childPosition;
                // 刷新界面
                ((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
                return false;
            }
        });

        // 设置二级item点击的监听器
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //检测是否连接网络
                int  networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), ZAJCMainAty.this);
                }else {
                    Intent intent=new Intent(ZAJCMainAty.this, ZAJCAty.class);
                    intent.putExtra("lb",group_title_arry[groupPosition]);
                    intent.putExtra("cjfl",child_text_array[groupPosition][childPosition]);
                    startActivity(intent);
                }
                return false;
            }
        });
    }



    final ExpandableListAdapter adapter = new BaseExpandableListAdapter() {

        // 重写ExpandableListAdapter中的各个方法
        /**
         * 获取一级标签总数
         */
        @Override
        public int getGroupCount() {
            return group_title_arry.length;
        }

        /**
         * 获取一级标签内容
         */
        @Override
        public Object getGroup(int groupPosition) {
            return group_title_arry[groupPosition];
        }

        /**
         * 获取一级标签的ID
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * 获取一级标签下二级标签的总数
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return child_text_array[groupPosition].length;
        }

        /**
         * 获取一级标签下二级标签的内容
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child_text_array[groupPosition][childPosition];
        }

        /**
         * 获取二级标签的ID
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /**
         * 指定位置相应的组视图
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }
        /**
         * 对一级标签进行设置
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // 为视图对象指定布局
            convertView = RelativeLayout.inflate(
                    getBaseContext(), R.layout.group, null);
            /**
             * 声明视图上要显示的控件
             */
            // 新建一个ImageView对象，用来显示一级标签上的logo图片
            ImageView group_logo = (ImageView) convertView
                    .findViewById(R.id.group_logo);
            // 新建一个TextView对象，用来显示一级标签上的标题信息
            TextView group_title = (TextView) convertView
                    .findViewById(R.id.group_title);
            // 新建一个ImageView对象，根据用户点击来标识一级标签的选中状态
            ImageView group_state = (ImageView) convertView
                    .findViewById(R.id.group_state);
            /**
             * 设置相应控件的内容
             */
            // 设置要显示的图片
            group_logo.setBackgroundResource(group_logo_array[groupPosition]);
            // 设置标题上的文本信息
            group_title.setText(group_title_arry[groupPosition]);

            if(group_checked[groupPosition] % 2 == 1){
                // 设置默认的图片是选中状态
                group_state.setBackgroundResource(group_state_array[1]);
            }else{
                for(int test : group_checked){
                    if(test == 0 || test % 2 == 0){
                        // 设置默认的图片是未选中状态
                        group_state.setBackgroundResource(group_state_array[0]);
                    }
                }
            }
            // 返回一个布局对象
            return convertView;
        }

        /**
         * 对一级标签下的二级标签进行设置
         */
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            // 为视图对象指定布局
            convertView = RelativeLayout.inflate(
                    getBaseContext(), R.layout.child, null);
            /**
             * 声明视图上要显示的控件
             */
            // 新建一个TextView对象，用来显示具体内容
            TextView child_text = (TextView) convertView
                    .findViewById(R.id.child_text);
            /**
             * 设置相应控件的内容
             */
            // 设置要显示的文本信息
            child_text.setText(child_text_array[groupPosition][childPosition]);
            // 判断item的位置是否相同，如相同，则表示为选中状态，更改其背景颜色，如不相同，则设置背景色为白色
            if (child_groupId == groupPosition
                    && child_childId == childPosition) {
                // 设置背景色为绿色
                convertView.setBackgroundColor(Color.BLUE);
            } else {
                // 设置背景色为白色
                convertView.setBackgroundColor(Color.WHITE);
            }
            // 返回一个布局对象
            return convertView;
        }

        /**
         * 当选择子节点的时候，调用该方法
         */
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    };


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.zamanage);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_search:
                /**控制连续点击*/
                if (BaseDataUtils.isFastClick())
                    break;
                if (TextUtils.isEmpty(atv_search.getText().toString())){
                    toast("请输入关键字！");
                    break;
                }
                showLoadding();
                getSearchData(atv_search.getText().toString());
                break;
            case R.id.tv_cancel:
                /**控制连续点击*/
                if (BaseDataUtils.isFastClick())
                    break;
                lv_search.setVisibility(View.GONE);
                ll_empty_data.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                break;

        }
    }

    private void getSearchData(String search) {
        JSONObject jo=new JSONObject();
        jo.put("MC",search);
        String json=jo.toJSONString();
        ApiResource.getZASerchList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    List<ZAJCModle> zajc =  JSON.parseArray(result,ZAJCModle.class);
                    if (zajc.size()>0){
                        lv_search.setVisibility(View.VISIBLE);
                        tv_cancel.setVisibility(View.VISIBLE);
                        ll_empty_data.setVisibility(View.GONE);
                        ZAJCSearchAdp mAdapter=new ZAJCSearchAdp(lv_search,zajc,R.layout.aty_zaserach_item,ZAJCMainAty.this);
                        lv_search.setAdapter(mAdapter);
                    }else {
                        lv_search.setVisibility(View.GONE);
                        tv_cancel.setVisibility(View.VISIBLE);
                        ll_empty_data.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    onFailure(i,headers,bytes,e);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                lv_search.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.VISIBLE);
                ll_empty_data.setVisibility(View.VISIBLE);
                toast("查询失败");
            }
        });
    }



    public void showDialog(){
        if (StringUtil.isEmpty(SharedPreferencesHelper.getString(this,"key",""))){
            TipsDialog tipsDialog=new TipsDialog(this,R.style.my_dialog);
            tipsDialog.show();
        }
    }
}