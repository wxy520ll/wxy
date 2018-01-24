package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

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
 * Created by hao.zhou on 2016/7/1.
 */
public class MachineRoomAdp extends BaseAdapter {


    private JSONArray data=null;
    private Context context;

    public MachineRoomAdp(Context context, JSONArray data) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_cj_organ_item,null);
            viewHolde.tv_ssdw=(TextView)convertView.findViewById(R.id.rank);
            viewHolde.tv_jfs=(TextView)convertView.findViewById(R.id.company);
            viewHolde.tv_glys=(TextView)convertView.findViewById(R.id.uploaded_number);
            convertView.setTag(viewHolde);
        }else {
            viewHolde= (ViewHolde) convertView.getTag();
        }
        viewHolde.tv_ssdw.setText(((JSONObject)data.get(position)).getString("ZDZ"));
        viewHolde.tv_jfs.setText(((JSONObject)data.get(position)).getString("JFZS"));
        viewHolde.tv_glys.setText(((JSONObject)data.get(position)).getString("JFGLYZS"));
        return convertView;
    }

    class  ViewHolde{
        TextView tv_ssdw;
        TextView tv_jfs;
        TextView tv_glys;

    }




//    public DutyBeatsAddAdp(AbsListView view, Collection<DutyBeatsModle> mDatas, int itemLayoutId, Context mContext) {
//        super(view, mDatas, itemLayoutId, mContext);
//    }
//
//    @Override
//    public void convert(AdapterHolder helper, DutyBeatsModle item, boolean isScrolling) {
//        helper.setText(R.id.tv_xh,""+(helper.getPosition()+1));
//        helper.setText(R.id.tv_name,item.getBID_NAME()+"("+item.getBEAT_DESCRIBE()+")");
//    }
}
