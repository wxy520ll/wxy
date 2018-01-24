package cn.net.xinyi.xmjt.activity.Plate.Adapter;

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
public class LpnOrganRankAdapter extends BaseAdapter {
    private JSONArray data=null;
    private Context context;
    private int days =1;

    public LpnOrganRankAdapter(Context context, JSONArray data, int days) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_policeran_adapter,null);
            viewHolde.ranking=(TextView)convertView.findViewById(R.id.rank);
            viewHolde.company=(TextView)convertView.findViewById(R.id.company);
            viewHolde.completed_number=(TextView)convertView.findViewById(R.id.completed_number);
            viewHolde.uploaded_number=(TextView)convertView.findViewById(R.id.uploaded_number);
            viewHolde.completion_rate=(TextView)convertView.findViewById(R.id.completion_rate);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }

        String rank = String.valueOf(position+1);
        String pcs =((JSONObject)data.get(position)).getString("PCS");
        int total = Integer.parseInt(((JSONObject) data.get(position)).getString("TOTAL"));
        int amounts = Integer.parseInt(((JSONObject) data.get(position)).getString("AMOUNTS"));
        int amountsOfDay = Integer.parseInt(((JSONObject) data.get(position)).getString("AMOUNTSOFDAY"));
        String organType = ((JSONObject)data.get(position)).getString("ORGANTYPE");

        viewHolde.ranking.setText(rank);
        viewHolde.company.setText(pcs);
        viewHolde.completed_number.setText(String.valueOf(amountsOfDay*days));
        viewHolde.uploaded_number.setText(String.valueOf(total));
        viewHolde.completion_rate.setText(String.valueOf(total*100/(amountsOfDay*days))+"%");
        return convertView;
    }


    class  ViewHolde{

        TextView ranking;
        TextView company;
        TextView completed_number;
        TextView uploaded_number;
        TextView completion_rate;
    }
}
