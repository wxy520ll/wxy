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
 * Created by mazhongwang on 15/5/6.
 */
public class OrganRankAdapter extends BaseAdapter {
    private JSONArray mRankArray;
    private LayoutInflater mInflater;
    private Context mContext;

    public  OrganRankAdapter(Context context,JSONArray rankArray){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mRankArray = rankArray;
    }

    @Override
    public int getCount() {
        return mRankArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mRankArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.organ_rank_list_item,null);
            holder.rank = (TextView)convertView.findViewById(R.id.txt_rank);
            holder.name = (TextView)convertView.findViewById(R.id.txt_name);
            holder.count = (TextView)convertView.findViewById(R.id.txt_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        String rank = String.valueOf(position+1);
        String name =((JSONObject)mRankArray.get(position)).getString("PCS");
        String count = ((JSONObject)mRankArray.get(position)).getString("TOTAL");

        holder.rank.setText(rank);
        holder.name.setText(name);
        holder.count.setText(count);

        return convertView;
    }

    static class ViewHolder{
        public TextView rank;
        public TextView name;
        public TextView count;
    }
}