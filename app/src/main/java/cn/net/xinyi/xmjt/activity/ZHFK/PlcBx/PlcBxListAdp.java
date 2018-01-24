package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.model.DutyFlightsModle;

/**
 * Created by hao.zhou on 2016/4/11.
 */
public class PlcBxListAdp extends BaseAdapter {
//
//    public PlcBxListAdp(AbsListView view, List<PoliceBoxModle> mDatas, int itemLayoutId, Context mContext) {
//        super(view, mDatas, itemLayoutId, mContext);
//    }
//
//    @Override
//    public void convert(AdapterHolder helper, final PoliceBoxModle item, boolean isScrolling) {
//        helper.setText(R.id.plc_xh,"序号："+String.valueOf(positions+1));
//        helper.setText(R.id.plc_bx_unionNo, item.getUNIFIEDNO());
//        helper.setText(R.id.plc_bx_address, item.getADDRESS());
//        helper.setText(R.id.plc_bx_linkMan, (TextUtils.isEmpty(item.getLINKMAN())?"":item.getLINKMAN()) + " " +(TextUtils.isEmpty(item.getPHONENO())?"":item.getPHONENO()) );
//    }

    private JSONArray data=null;
    private Context context;

    public PlcBxListAdp(Context context, JSONArray data) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_plcbx_item,null);
            viewHolde.plc_xh=(TextView)convertView.findViewById(R.id.plc_xh);
            viewHolde.plc_bx_unionNo=(TextView)convertView.findViewById(R.id.plc_bx_unionNo);
            viewHolde.plc_bx_address=(TextView)convertView.findViewById(R.id.plc_bx_address);
            viewHolde.plc_bx_linkMan=(TextView)convertView.findViewById(R.id.plc_bx_linkMan);
            viewHolde.tv_duty=(TextView)convertView.findViewById(R.id.tv_duty);
            viewHolde.tv_gz=(TextView)convertView.findViewById(R.id.tv_gz);
            convertView.setTag(viewHolde);
        }else {
            viewHolde= (ViewHolde) convertView.getTag();
        }
        viewHolde.plc_xh.setText("序号："+String.valueOf(position+1));
        viewHolde.plc_bx_unionNo.setText(((JSONObject)data.get(position)).getString("UNIFIEDNO"));
        viewHolde.plc_bx_address.setText(((JSONObject)data.get(position)).getString("ADDRESS"));
        String sLINKMAN=((JSONObject)data.get(position)).getString("LINKMAN");
        String sPHONENO=((JSONObject)data.get(position)).getString("PHONENO");
        viewHolde.plc_bx_linkMan.setText((sLINKMAN==null?"":sLINKMAN) +"("+(sPHONENO==null?"":sPHONENO)+")");
        viewHolde.tv_duty.setText("排班名称："+((JSONObject)data.get(position)).getString("FRNAME"));
        if (null !=((JSONObject)data.get(position)).getString("DUTY_FLIGHTS_LIST")){
            List<DutyFlightsModle> infos= JSON.parseArray(((JSONObject)data.get(position)).getString("DUTY_FLIGHTS_LIST"),DutyFlightsModle.class);
            viewHolde.tv_gz.setText("");
            for (DutyFlightsModle info:infos){
                viewHolde.tv_gz.append(info.getFLIGHT_NAME()+"("+info.getSTART_TIME()+"至"+info.getEND_TIME()+",共"+info.getLENGTH()+"小时);\n");
            }
        }else {
            viewHolde.tv_gz.setText("未设置排班规则！");
        }
        return convertView;
    }

    class  ViewHolde{
        TextView plc_xh;//序号
        TextView plc_bx_unionNo;//唯一编号
        TextView plc_bx_address;//地址
        TextView plc_bx_linkMan;//联系人和电话
        TextView tv_duty;//排班规则
        TextView tv_gz;//勤务班次

    }


    //        helper.setText(R.id.plc_xh,"序号："+String.valueOf(positions+1));
//        helper.setText(R.id.plc_bx_unionNo, item.getUNIFIEDNO());
//        helper.setText(R.id.plc_bx_address, item.getADDRESS());
//        helper.setText(R.id.plc_bx_linkMan, (TextUtils.isEmpty(item.getLINKMAN())?"":item.getLINKMAN()) + " " +(TextUtils.isEmpty(item.getPHONENO())?"":item.getPHONENO()) );

}
