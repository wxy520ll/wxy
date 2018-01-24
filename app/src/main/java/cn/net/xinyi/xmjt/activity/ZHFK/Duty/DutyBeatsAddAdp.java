package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.model.DutyFlightsModle;

/**
 * Created by hao.zhou on 2016/7/1.
 */
public class DutyBeatsAddAdp extends BaseAdapter {


    private JSONArray data=null;
    private Context context;

    public DutyBeatsAddAdp(Context context, JSONArray data) {
        this.context=context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolde viewHolde=null;
        if (convertView==null){
            viewHolde=new ViewHolde();
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_duty_beats_add_adp,null);
            viewHolde.tv_name=(TextView)convertView.findViewById(R.id.tv_duty_name);
            viewHolde.tv_duty=(TextView)convertView.findViewById(R.id.tv_duty);
            viewHolde.tv_gz=(TextView)convertView.findViewById(R.id.tv_gz);
            convertView.setTag(viewHolde);
        }else {
            viewHolde= (ViewHolde) convertView.getTag();
        }
        viewHolde.tv_name.setText("巡段名称："+((JSONObject)data.get(position)).getString("BID_NAME"));
        viewHolde.tv_duty.setText("排班名称："+((JSONObject)data.get(position)).getString("FRNAME"));
        if (null !=((JSONObject)data.get(position)).getString("DUTY_FLIGHTS_LIST")){
            List<DutyFlightsModle> infos=JSON.parseArray(((JSONObject)data.get(position)).getString("DUTY_FLIGHTS_LIST"),DutyFlightsModle.class);
            viewHolde.tv_gz.setText("");
            for (DutyFlightsModle info:infos){
                viewHolde.tv_gz.append(info.getFLIGHT_NAME()+"("+info.getSTART_TIME()+"至"+info.getEND_TIME()+",共"+info.getLENGTH()+"小时);\n");
            }
        }else {
            viewHolde.tv_gz.setText("未设置排班规则！");
        }
        return convertView;
    }

    class  ViewHolde{
        TextView tv_name;//巡段名称
        TextView tv_duty;//规则名称
        TextView tv_gz;//规则

    }




//    public DutyBeatsAddAdp(AbsListView view, Collection<DutyBeatsModle> mDatas, int itemLayoutId, Context mContext) {
//        super(view, mDatas, itemLayoutId, mContext);
//    }
//
//    @Override
//    public void convert(AdapterHolder helper, DutyBeatsModle item, boolean isScrolling) {
//        helper.setText(R.id.tv_xh,""+(helper.getPosition()+1));
//        helper.setText(R.id.tv_name,item.getBID_NAME()+"("+item.getBEAT_DESCRIBE()+")");
//    }
}
