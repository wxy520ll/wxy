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
public class LpnCountAdapter extends BaseAdapter {
    private JSONArray data=null;
    private Context context;

    public LpnCountAdapter(Context context, JSONArray data) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_suboffice_adapter,null);
            viewHolde.tv_company_name=(TextView)convertView.findViewById(R.id.tv_company_name);
            viewHolde.tv_update_number=(TextView)convertView.findViewById(R.id.tv_update_number);
            viewHolde.tv_completed_number=(TextView)convertView.findViewById(R.id.tv_completed_number);
            viewHolde.tv_completion_rate=(TextView)convertView.findViewById(R.id.tv_completion_rate);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }

        String pcs =((JSONObject)data.get(position)).getString("PCS");
        String rank = String.valueOf(position+1);
        int total = Integer.parseInt(((JSONObject) data.get(position)).getString("TOTAL"));
        int amounts = Integer.parseInt(((JSONObject) data.get(position)).getString("AMOUNTS"));
        int amountsOfDay = Integer.parseInt(((JSONObject) data.get(position)).getString("AMOUNTSOFDAY"));
        String organType = ((JSONObject)data.get(position)).getString("ORGANTYPE");

        viewHolde.tv_company_name.setText(pcs);
        viewHolde.tv_completed_number.setText(String.valueOf(total));
        viewHolde.tv_update_number.setText(String.valueOf(amounts));
        viewHolde.tv_completion_rate.setText(String.valueOf(total*100/(amounts))+"%");
        return convertView;
    }


    class  ViewHolde{
        TextView tv_company_name;
        TextView tv_update_number;
        TextView tv_completed_number;
        TextView tv_completion_rate;
    }
}
