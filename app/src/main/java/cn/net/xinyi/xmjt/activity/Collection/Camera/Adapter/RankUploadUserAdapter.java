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
 * Created by mazhongwang on 15/5/6.
 */
public class RankUploadUserAdapter extends BaseAdapter {
    private JSONArray mRankArray;
    private LayoutInflater mInflater;
    private Context mContext;
    private int flags;

    public RankUploadUserAdapter(Context context, JSONArray rankArray,int flags){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mRankArray = rankArray;
        this.flags=flags;
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
            convertView = mInflater.inflate(R.layout.user_count_list_item,null);
            holder.date = (TextView)convertView.findViewById(R.id.txt_date);
            holder.name = (TextView)convertView.findViewById(R.id.txt_name);
            holder.count = (TextView)convertView.findViewById(R.id.txt_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        String name=null;
        if (flags==1){
            name =((JSONObject)mRankArray.get(position)).getString("SHYH");
        }else {
            name =((JSONObject)mRankArray.get(position)).getString("CJYH");
        }
        String count = ((JSONObject)mRankArray.get(position)).getString("TOTAL");
        String date = ((JSONObject)mRankArray.get(position)).getString("DT");

        holder.date.setText(date);
        holder.name.setText(name);
        holder.count.setText(count);

        return convertView;
    }

    static class ViewHolder{
        public TextView date;
        public TextView name;
        public TextView count;
    }
}