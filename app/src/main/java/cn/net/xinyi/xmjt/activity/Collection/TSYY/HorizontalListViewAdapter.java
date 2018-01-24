package cn.net.xinyi.xmjt.activity.Collection.TSYY;

/**
 * Created by zhouhao on 2016/12/30.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.xinyi.xmjt.R;

public class HorizontalListViewAdapter extends BaseAdapter{
    private JSONArray jsonArray;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context context, JSONArray jsonArray){
        this.mContext = context;
        this.jsonArray = jsonArray;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return jsonArray.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.ll_onetext, null);
            holder.mTitle=(TextView)convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        JSONObject jo=(JSONObject)jsonArray.get(position);
        if (jo.size()>0){
            holder.mTitle.setText(jo.getString("SJ")+"\n("+jo.getString("XQ")+")");
        }
        if(position == selectIndex){
            convertView.setSelected(true);
        }else{
            convertView.setSelected(false);
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle ;
    }
    public void setSelectIndex(int i){
        selectIndex = i;
    }
}