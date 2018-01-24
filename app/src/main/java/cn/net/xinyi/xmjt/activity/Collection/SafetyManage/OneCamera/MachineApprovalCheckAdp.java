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
public class MachineApprovalCheckAdp extends BaseAdapter {


    private JSONArray data=null;
    private Context context;

    public MachineApprovalCheckAdp(Context context, JSONArray data) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.aty_machine_approval_check_adp,null);
            viewHolde.tv_jfmc=(TextView)convertView.findViewById(R.id.tv_jfmc);
            viewHolde.tv_sqdw=(TextView)convertView.findViewById(R.id.tv_sqdw);
            viewHolde.tv_sqsj=(TextView)convertView.findViewById(R.id.tv_sqsj);
            viewHolde.tv_sqr=(TextView)convertView.findViewById(R.id.tv_sqr);
            viewHolde.tv_zt=(TextView)convertView.findViewById(R.id.tv_zt);
            viewHolde.tv_shry=(TextView)convertView.findViewById(R.id.tv_shry);
            viewHolde.tv_sglx=(TextView)convertView.findViewById(R.id.tv_sglx);
            viewHolde.tv_wcsj=(TextView)convertView.findViewById(R.id.tv_wcsj);
            convertView.setTag(viewHolde);
        }else {
            viewHolde= (ViewHolde) convertView.getTag();
        }
        viewHolde.tv_jfmc.setText("机房名称："+((JSONObject)data.get(position)).getString("MC")+"("+((JSONObject)data.get(position)).getString("ZDZ")+")");
        viewHolde.tv_sqdw.setText("申请单位："+((JSONObject)data.get(position)).getString("COMPANYNAME"));
        viewHolde.tv_sqr.setText("申请人员："+((JSONObject)data.get(position)).getString("SQRYNAME")+"("+((JSONObject)data.get(position)).getString("SJHM")+")");
        viewHolde.tv_sqsj.setText("申请时间："+((JSONObject)data.get(position)).getString("SQSJ"));
        viewHolde.tv_shry.setText("审批人："+((JSONObject)data.get(position)).getString("SHRYNAME")+"("+((JSONObject)data.get(position)).getString("SHRY")+")");
        viewHolde.tv_sglx.setText("施工类型："+((JSONObject)data.get(position)).getString("TYPE"));
        viewHolde.tv_wcsj.setText("完成时间："+((JSONObject)data.get(position)).getString("SGWCSJ"));

        if (Integer.parseInt(((JSONObject)data.get(position)).getString("SHJG"))==0){
            viewHolde.tv_zt.setText("审核状态：未审核");
            viewHolde.tv_zt.setTextColor(context.getResources().getColor(R.color.lucky_text_red));
        } else if (Integer.parseInt(((JSONObject)data.get(position)).getString("SHJG"))==3){
            viewHolde.tv_zt.setText("审核状态：申请过期");
            viewHolde.tv_zt.setTextColor(context.getResources().getColor(R.color.lucky_text_red));
        } else {
            String bz=((JSONObject)data.get(position)).getString("BZ");
            viewHolde.tv_zt.setText("审核状态："+(bz== null? "审核通过":"未通过"+bz));
            viewHolde.tv_zt.setTextColor(context.getResources().getColor(R.color.blue));
        }
        return convertView;
    }

    class  ViewHolde{
        TextView tv_jfmc;
        TextView tv_sqdw;
        TextView tv_sqr;
        TextView tv_sqsj;
        TextView tv_zt;
        TextView tv_shry;
        TextView tv_sglx;
        TextView tv_wcsj;
    }

}
