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
public class RankJDUserAdapter extends BaseAdapter {
    private JSONArray data=null;
    private Context context;
    private int days=1;

    public RankJDUserAdapter(Context context, JSONArray data, int days) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.rank_pcs_user_adapter,null);
            viewHolde.ranking=(TextView)convertView.findViewById(R.id.tv_rank);
            viewHolde.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolde.tv_phone=(TextView)convertView.findViewById(R.id.tv_phone);
            viewHolde.tv_completion_num=(TextView)convertView.findViewById(R.id.tv_completion_num);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }

        String rank = String.valueOf(position+1);
        String phone = ((JSONObject)data.get(position)).getString("CJYH");
        String name = ((JSONObject)data.get(position)).getString("NAME");
        int total = Integer.parseInt(((JSONObject) data.get(position)).getString("TOTAL"));

        viewHolde.ranking.setText(rank);
        viewHolde.tv_phone.setText(phone);
        viewHolde.tv_name.setText(name);
        viewHolde.tv_completion_num.setText(String.valueOf(total));
        return convertView;
    }


    class  ViewHolde{
        TextView ranking;
        TextView tv_name;
        TextView tv_phone;
        TextView tv_completion_num;
    }
}
