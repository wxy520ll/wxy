package cn.net.xinyi.xmjt.activity.Collection.House;

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
public class RankHousePCSAdapter extends BaseAdapter {
    private JSONArray data=null;
    private Context context;
    public RankHousePCSAdapter(Context context, JSONArray data) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_house_rank_organ_item,null);
            viewHolde.tv_rank=(TextView)convertView.findViewById(R.id.tv_rank);
            viewHolde.tv_company=(TextView)convertView.findViewById(R.id.tv_company);
            viewHolde.tv_sf=(TextView)convertView.findViewById(R.id.tv_sf);
            viewHolde.tv_zf=(TextView)convertView.findViewById(R.id.tv_zf);
            viewHolde.tv_jf=(TextView)convertView.findViewById(R.id.tv_jf);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }

        String rank = String.valueOf(position+1);
        String pcs =((JSONObject)data.get(position)).getString("ZDZ");
        int CJZS = Integer.parseInt(((JSONObject) data.get(position)).getString("CJZS"));
        int JFZS = Integer.parseInt(((JSONObject) data.get(position)).getString("JFZS"));
        int ZFZS = Integer.parseInt(((JSONObject) data.get(position)).getString("ZFZS"));

        viewHolde.tv_rank.setText(rank);
        viewHolde.tv_company.setText(pcs);
        viewHolde.tv_sf.setText(String.valueOf(CJZS));
        viewHolde.tv_zf.setText(String.valueOf(ZFZS));
        viewHolde.tv_jf.setText(String.valueOf(JFZS));
        return convertView;
    }


    class  ViewHolde{
        TextView tv_rank;
        TextView tv_company;
        TextView tv_sf;
        TextView tv_zf;
        TextView tv_jf;
    }
}