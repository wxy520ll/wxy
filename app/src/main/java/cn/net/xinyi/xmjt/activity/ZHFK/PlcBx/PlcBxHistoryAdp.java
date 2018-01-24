package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

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
public class PlcBxHistoryAdp extends BaseAdapter {

    private JSONArray data=null;
    private Context context;
    public PlcBxHistoryAdp(Context context, JSONArray data) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_duty_history_item,null);
            viewHolde.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolde.tv_dh=(TextView)convertView.findViewById(R.id.tv_dh);
            viewHolde.tv_ksxl=(TextView)convertView.findViewById(R.id.tv_ksxl);
            viewHolde.tv_year=(TextView)convertView.findViewById(R.id.tv_year);
            viewHolde.tv_dz=(TextView)convertView.findViewById(R.id.tv_dz);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }

        String NAME =((JSONObject)data.get(position)).getString("NAME");
        String YH =((JSONObject)data.get(position)).getString("YH");
        String KSSJ =((JSONObject)data.get(position)).getString("KSSJ");
        String JSSJ =((JSONObject)data.get(position)).getString("JSSJ");

        viewHolde.tv_name.setText(NAME);
        viewHolde.tv_dh.setText(YH);
        viewHolde.tv_ksxl.setText("防控点值守");
        viewHolde.tv_year.setText(JSSJ==null?KSSJ:KSSJ+"至"+JSSJ);
        viewHolde.tv_dz.setVisibility(View.GONE);
        return convertView;
    }



    class  ViewHolde{
        TextView tv_name;//名字
        TextView tv_dh;//电话
        TextView tv_ksxl;//类型（防控点值守）
        TextView tv_year;//时间
        TextView tv_dz;//
    }

}
