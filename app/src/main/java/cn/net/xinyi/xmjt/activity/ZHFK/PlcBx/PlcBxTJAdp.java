package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

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
public class PlcBxTJAdp extends BaseAdapter {

    private JSONArray data=null;
    private Context context;
    public PlcBxTJAdp(Context context, JSONArray data) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_duty_pcs_rank_iten,null);
            viewHolde.tv_pcs=(TextView)convertView.findViewById(R.id.tv_pcs);
            viewHolde.tv_xdzs=(TextView)convertView.findViewById(R.id.tv_xdzs);
            viewHolde.tv_sgs=(TextView)convertView.findViewById(R.id.tv_sgs);
            viewHolde.tv_kyjl=(TextView)convertView.findViewById(R.id.tv_kyjl);
            viewHolde.tv_cjrs=(TextView)convertView.findViewById(R.id.tv_cjrs);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }
//        GTSL:岗亭数量，
//        PCSBM:【派出所编码
//        PCSMC:派出所名称
//        ZGSL:在岗数量
//        ZSRS:值守人事

        String PCSMC =((JSONObject)data.get(position)).getString("PCSMC");
        String GTSL =((JSONObject)data.get(position)).getString("GTSL");
        String ZGSL =((JSONObject)data.get(position)).getString("ZGSL");
        String ZSRS =((JSONObject)data.get(position)).getString("ZSRS");
        viewHolde.tv_xdzs.setText(GTSL == null ? "0" : GTSL );
        viewHolde.tv_pcs.setText(PCSMC);
        viewHolde.tv_kyjl.setText(ZGSL== null ? "0" : ZGSL);
        viewHolde.tv_cjrs.setText(ZSRS== null ? "0" : ZSRS);
        viewHolde.tv_sgs.setVisibility(View.GONE);
        return convertView;
    }



    class  ViewHolde{
        TextView tv_pcs;//派出所
        TextView tv_xdzs;//巡段总数
        TextView tv_sgs;//上岗数
        TextView tv_kyjl;//可用警力
        TextView tv_cjrs;//处警人数
    }
}
