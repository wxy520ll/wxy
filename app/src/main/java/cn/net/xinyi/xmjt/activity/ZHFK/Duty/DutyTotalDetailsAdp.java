package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

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
public class DutyTotalDetailsAdp extends BaseAdapter {

    private JSONArray data=null;
    private Context context;
    public DutyTotalDetailsAdp(Context context, JSONArray data) {
        this.context=context;
        this.data = data;
    }
    private OnPeopleClickListener onPeopleClickListener;
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
            viewHolde.tv_pcs=(TextView)convertView.findViewById(R.id.tv_pcs);
            viewHolde.tv_xdzs=(TextView)convertView.findViewById(R.id.tv_xdzs);
            viewHolde.tv_sgs=(TextView)convertView.findViewById(R.id.tv_sgs);
            viewHolde.tv_kyjl=(TextView)convertView.findViewById(R.id.tv_kyjl);
            viewHolde.tv_cjrs=(TextView)convertView.findViewById(R.id.tv_cjrs);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }

//        CJYH:用户，
//        MZ:巡逻段名称
//        NAME:用户名
//        XLDID:巡逻段id
//        XLDWID:最后巡逻点位ID
//        XLJLID:巡逻记录ID
//        XLTYPE:巡逻类型，1巡逻，2处警
//        XLZT:巡逻状态(1开始巡逻2结束巡逻3打卡4开始离岗报备5结束离岗报备)
//        ZHSJ:最后操作时间

        String MZ =((JSONObject)data.get(position)).getString("MZ");
        String CJYH =((JSONObject)data.get(position)).getString("CJYH");
        String NAME =((JSONObject)data.get(position)).getString("NAME");
        String XLTYPE =((JSONObject)data.get(position)).getString("XLTYPE");
        String XLZT =((JSONObject)data.get(position)).getString("XLZT");
        String ZHSJ =((JSONObject)data.get(position)).getString("ZHSJ");
        //不在巡逻状态下，24小时内巡逻数据
        String L_CJYH =((JSONObject)data.get(position)).getString("L_CJYH");
        String L_NAME =((JSONObject)data.get(position)).getString("L_NAME");
        String L_XLTYPE =((JSONObject)data.get(position)).getString("L_XLTYPE");
        String L_XLZT =((JSONObject)data.get(position)).getString("L_XLZT");
        String L_ZHSJ =((JSONObject)data.get(position)).getString("L_ZHSJ");


        viewHolde.tv_pcs.setText(MZ==null ? "处警":MZ);
        if (null!=XLTYPE){
            viewHolde.tv_xdzs.setText(NAME+"("+CJYH+")");
            viewHolde.tv_cjrs.setText(ZHSJ.substring(0, ZHSJ.lastIndexOf(".")));
            viewHolde.tv_sgs.setTextColor(context.getResources().getColor(R.color.black));
            viewHolde.tv_xdzs.setTextColor(context.getResources().getColor(R.color.black));
            if (XLZT.equals(DutyStartAty.TYPE_BEATS_START_A)) {//巡逻开始
                viewHolde.tv_sgs.setText("开始巡逻");
            }else if (XLZT.equals(DutyStartAty.TYPE_BEATS_END)) {//巡逻结束
                viewHolde.tv_sgs.setText("结束巡逻");
                viewHolde.tv_sgs.setTextColor(context.getResources().getColor(R.color.bbutton_danger));
            }else if (XLZT.equals(DutyStartAty.TYPE_BOXS)) {//签到
                viewHolde.tv_sgs.setText("签到");
            }else if (XLZT.equals(DutyStartAty.TYPE_REPORTS_START_A)) {//报备开始
                viewHolde.tv_sgs.setText("报备中");
            }else if (XLZT.equals(DutyStartAty.TYPE_REPORTS_END)) {//报备结束
                viewHolde.tv_sgs.setText("报备结束");
            }else if (XLZT.equals(DutyPoliceAty.TYPE_POLICE_START_A)) {//处警开始
                viewHolde.tv_sgs.setText("处警中");
            }else if (XLZT.equals(DutyPoliceAty.TYPE_POLICE_END)) {//处警结束
                viewHolde.tv_sgs.setText("处警结束");
            }
        }else if (null!=L_XLTYPE){
            viewHolde.tv_xdzs.setText(L_NAME+"("+L_CJYH+")");
            viewHolde.tv_cjrs.setText(L_ZHSJ.substring(0, L_ZHSJ.lastIndexOf(".")));
            viewHolde.tv_xdzs.setTextColor(context.getResources().getColor(R.color.black));
            viewHolde.tv_sgs.setTextColor(context.getResources().getColor(R.color.black));
            if (L_XLZT.equals(DutyStartAty.TYPE_BEATS_START_A)) {//巡逻开始
                viewHolde.tv_sgs.setText("开始巡逻");
            }else if (L_XLZT.equals(DutyStartAty.TYPE_BEATS_END)) {//巡逻结束
                viewHolde.tv_sgs.setText("结束巡逻");
                viewHolde.tv_sgs.setTextColor(context.getResources().getColor(R.color.bbutton_danger));
            }else if (L_XLZT.equals(DutyStartAty.TYPE_BOXS)) {//签到
                viewHolde.tv_sgs.setText("签到");
            }else if (L_XLZT.equals(DutyStartAty.TYPE_REPORTS_START_A)) {//报备开始
                viewHolde.tv_sgs.setText("报备中");
            }else if (L_XLZT.equals(DutyStartAty.TYPE_REPORTS_END)) {//报备结束
                viewHolde.tv_sgs.setText("报备结束");
            }else if (L_XLZT.equals(DutyPoliceAty.TYPE_POLICE_START_A)) {//处警开始
                viewHolde.tv_sgs.setText("处警中");
            }else if (L_XLZT.equals(DutyPoliceAty.TYPE_POLICE_END)) {//处警结束
                viewHolde.tv_sgs.setText("结束处警");
            }
        }else {
            viewHolde.tv_xdzs.setText("");
            viewHolde.tv_sgs.setText("无人巡逻");
            viewHolde.tv_cjrs.setText("");
            viewHolde.tv_xdzs.setTextColor(context.getResources().getColor(R.color.black));
            viewHolde.tv_sgs.setTextColor(context.getResources().getColor(R.color.bbutton_danger));
        }
        viewHolde.tv_kyjl.setVisibility(View.GONE);
        viewHolde.tv_xdzs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPeopleClickListener!=null){
                    onPeopleClickListener.onClick(v,data.getJSONObject(position));
                }
            }
        });
        viewHolde.tv_pcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPeopleClickListener!=null){
                    onPeopleClickListener.onPointClick(v,data.getJSONObject(position));
                }
            }
        });
        viewHolde.tv_sgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPeopleClickListener!=null){
                    onPeopleClickListener.onFlightRults(v,data.getJSONObject(position));
                }
            }
        });
        return convertView;
    }

    class  ViewHolde{
        TextView tv_pcs;//派出所
        TextView tv_xdzs;//巡段总数
        TextView tv_sgs;//上岗数
        TextView tv_kyjl;//可用警力
        TextView tv_cjrs;//处警人数
    }

    /**
     * 巡逻点击事件
     */
    public interface OnPeopleClickListener {
        void onClick(View view, JSONObject jsonObject);
        void onPointClick(View view, JSONObject jsonObject);
        void onFlightRults(View view, JSONObject jsonObject);
    }

    public void setOnPeopleClickListener(OnPeopleClickListener onPeopleClickListener) {
        this.onPeopleClickListener = onPeopleClickListener;
    }
}
