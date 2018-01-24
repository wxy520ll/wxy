package cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter;

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
public class RankPCSAdapter extends BaseAdapter {
    private JSONArray data=null;
    private Context context;
    private int days =1;
    public RankPCSAdapter(Context context, JSONArray data, int days) {
        this.context=context;
        this.data = data;
        this.days = days;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_cj_organ_item,null);
            viewHolde.ranking=(TextView)convertView.findViewById(R.id.rank);
            viewHolde.company=(TextView)convertView.findViewById(R.id.company);
            viewHolde.uploaded_number=(TextView)convertView.findViewById(R.id.uploaded_number);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }

        String rank = String.valueOf(position+1);
        String pcs =((JSONObject)data.get(position)).getString("CJDWMC");
        int total = Integer.parseInt(((JSONObject) data.get(position)).getString("TOTAL"));
        viewHolde.company.setText(pcs);
        viewHolde.ranking.setText(rank);
        viewHolde.uploaded_number.setText(String.valueOf(total));
        return convertView;
    }


    class  ViewHolde{
        TextView ranking;
        TextView company;
        TextView uploaded_number;
    }
}