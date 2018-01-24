package cn.net.xinyi.xmjt.activity.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.HashMap;
import java.util.List;

import cn.net.xinyi.xmjt.R;

/**
 * Created by hao.zhou on 2015/9/18.
 */
public class WangGeAdapter extends BaseAdapter {
    private Context context;
    private List<String> data=null;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;

    public WangGeAdapter(Context context,List<String> data) {
        this.context = context;
        this.data = data;
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }

    private void initDate() {
        for(int i=0; i<data.size();i++) {
            getIsSelected().put(i,false);
        }
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.wangge_item, null);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkBox.setText(data.get(position));

        // 监听checkBox并根据原来的状态来设置新的状态
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }
            }
        });
        // 根据isSelected来设置checkbox的选中状况
        holder.checkBox.setChecked(getIsSelected().get(position));



        return convertView;
    }


    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        WangGeAdapter.isSelected = isSelected;
    }

    class  ViewHolder{
        CheckBox checkBox;
    }

}
