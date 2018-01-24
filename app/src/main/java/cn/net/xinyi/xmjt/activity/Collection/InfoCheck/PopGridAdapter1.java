package cn.net.xinyi.xmjt.activity.Collection.InfoCheck;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;


/**
 * Created by $ wxy on 2017/6/3.
 * 从右滑出的popwindow
 */

public class PopGridAdapter1 extends BaseAdapter {

    private Context context;
    private List<String> models=new ArrayList<>();

    public PopGridAdapter1(Context context, List<String> models) {
        this.context = context;
        this.models.clear();
        this.models.addAll(models);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= View.inflate(context, R.layout.gridview_item,null);
        TextView textView= (TextView) view.findViewById(R.id.state2);
        textView.setText(models.get(position));
        return view;
    }

}
