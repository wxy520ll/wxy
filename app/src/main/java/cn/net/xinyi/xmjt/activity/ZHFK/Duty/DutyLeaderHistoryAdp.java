package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.xinyi.xmjt.R;

/**
 * Created by hao.zhou on 2015/8/28.
 */
public class DutyLeaderHistoryAdp extends BaseAdapter {

    private JSONArray data=null;
    private Context context;
    public DutyLeaderHistoryAdp(Context context, JSONArray data) {
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
        ViewHolde holder=null;
        if (convertView==null){
            holder=new ViewHolde();
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_duty_history_item,null);
            holder.tv_pcs = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_dh);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_dk);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_year);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_dz);
            holder.tv_ksxl= (TextView) convertView.findViewById(R.id.tv_ksxl);
            holder.tv_ksxl.setVisibility(View.GONE);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolde) convertView.getTag();
        }
        String XZHM =((JSONObject)data.get(position)).getString("XZHM");
        String JSSJ =((JSONObject)data.get(position)).getString("JSSJ");
        String KSSJ =((JSONObject)data.get(position)).getString("KSSJ");
        String DZ =((JSONObject)data.get(position)).getString("KSDZ");
        String XZXM =((JSONObject)data.get(position)).getString("NAME");
        String PCS =((JSONObject)data.get(position)).getString("ZDZ");
        holder.tv_pcs.setText(PCS);
        holder.tv_name.setText(XZXM);
        holder.tv_phone.setText(XZHM);
        holder.tv_time.setText(JSSJ == null ? KSSJ:KSSJ+"  至  "+JSSJ);
        holder.tv_address.setText(DZ);
        return convertView;
    }



    class  ViewHolde{
        public TextView tv_pcs;//所属单位
        public TextView tv_name;//巡长名字
        public TextView tv_phone;//巡长电话
        public TextView tv_time;//巡长检查时间
        public TextView tv_address;//巡长地址
        public TextView tv_ksxl;//开始巡逻
    }
}
