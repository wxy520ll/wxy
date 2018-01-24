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
public class PlcBxTJXQAdp extends BaseAdapter {

    private JSONArray data=null;
    private Context context;
    public PlcBxTJXQAdp(Context context, JSONArray data) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolde viewHolde=null;
        if (convertView==null){
            viewHolde=new ViewHolde();
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_duty_pcs_rank_iten,null);
            viewHolde.tv_name=(TextView)convertView.findViewById(R.id.tv_pcs);
            viewHolde.tv_type=(TextView)convertView.findViewById(R.id.tv_xdzs);
            viewHolde.tv_zsry=(TextView)convertView.findViewById(R.id.tv_sgs);
            viewHolde.tv_zhsj=(TextView)convertView.findViewById(R.id.tv_kyjl);
            viewHolde.tv_cjrs=(TextView)convertView.findViewById(R.id.tv_cjrs);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }

//        ADDRESS:地址，
//        GTID:岗亭id
//        GTZSID:岗亭值守id
//        NAME:值守人名称
//        TYPE:岗亭类型
//        UNIFIEDNO:岗亭统一编号
//        YH:值守人电话（账号名）
//        ZHLX:最后操作类型  1开始值守2结束值守3离岗报备4擅自离岗
//        ZHSJ:最后操作时间


        String UNIFIEDNO =((JSONObject)data.get(position)).getString("UNIFIEDNO");
        String NAME =((JSONObject)data.get(position)).getString("NAME");
        String YH =((JSONObject)data.get(position)).getString("YH");
        String ZHSJ =((JSONObject)data.get(position)).getString("ZHSJ");
        String ZHLX =((JSONObject)data.get(position)).getString("ZHLX");

        //当前有人值守的情况
        String L_NAME =((JSONObject)data.get(position)).getString("L_NAME");
        String L_YH =((JSONObject)data.get(position)).getString("L_YH");
        String L_ZHSJ =((JSONObject)data.get(position)).getString("L_ZHSJ");
        String L_ZHLX =((JSONObject)data.get(position)).getString("L_LX");

        viewHolde.tv_name.setText(UNIFIEDNO);
        if (null!=L_NAME||null!=L_ZHLX){//当前有人值守的情况
            viewHolde.tv_zsry.setText(L_NAME+"("+L_YH+")");
            viewHolde.tv_zhsj.setText(L_ZHSJ.substring(0,L_ZHSJ.lastIndexOf(".")));
            if ("2" .equals(L_ZHLX)){
                viewHolde.tv_type.setText(context.getResources().getString(R.string.ending_work_plc_bx));
                viewHolde.tv_type.setTextColor(context.getResources().getColor(R.color.bbutton_danger));
            }else if ("3" .equals(L_ZHLX)){
                viewHolde.tv_type.setText(context.getResources().getString(R.string.bb_work_plc_bx));
                viewHolde.tv_type.setTextColor(context.getResources().getColor(R.color.bbutton_danger));
            }else if ("4" .equals(L_ZHLX)){
                viewHolde.tv_type.setText(context.getResources().getString(R.string.sz_work_plc_bx));
                viewHolde.tv_type.setTextColor(context.getResources().getColor(R.color.bbutton_danger));
            }
        }else {
            if (null == ZHLX||null == NAME){//历史无人值守
                viewHolde.tv_zhsj.setText("");
                viewHolde.tv_zsry.setText("");
                viewHolde.tv_type.setText("无人值守");
                viewHolde.tv_type.setTextColor(context.getResources().getColor(R.color.bbutton_danger));
            } else {//历史有人值守
                viewHolde.tv_zsry.setText(NAME+"("+YH+")");
                viewHolde.tv_zhsj.setText(ZHSJ.substring(0,ZHSJ.lastIndexOf(".")));
                viewHolde.tv_type.setText("值守中");
                viewHolde.tv_type.setTextColor(context.getResources().getColor(R.color.text_black));
            }
        }

        viewHolde.tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPeopleClickListener!=null){
                    onPeopleClickListener.onFlightRults(view,data.getJSONObject(position));
                }
            }
        });

        viewHolde.tv_zhsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPeopleClickListener!=null){
                    onPeopleClickListener.onPlcBxHistory(view,data.getJSONObject(position));
                }
            }
        });

        viewHolde.tv_cjrs.setVisibility(View.GONE);
        return convertView;
    }



    class  ViewHolde{
        TextView tv_name;//岗亭名称
        TextView tv_type;//当前状态
        TextView tv_zsry;//值守人员
        TextView tv_zhsj;//最后操作时间
        TextView tv_cjrs;
    }
    /**
     * 巡逻点击事件
     */
    public interface OnPeopleClickListener {
        void onFlightRults(View view, JSONObject jsonObject);
        void onPlcBxHistory(View view, JSONObject jsonObject);
    }

    public void setOnPeopleClickListener(OnPeopleClickListener onPeopleClickListener) {
        this.onPeopleClickListener = onPeopleClickListener;
    }
    private OnPeopleClickListener onPeopleClickListener;
}
