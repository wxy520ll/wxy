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
public class DutyTotalSecondAdp extends BaseAdapter {

//    public XLTJAdp(AbsListView view, JSONArray datas, int itemLayoutId, Context context) {
//        super(view, datas, itemLayoutId, context);
//    }
//
//    @Override
//    public void convert(AdapterHolder helper, JSONArray item, boolean isScrolling) {
//        helper.setText(R.id.tv_pcs,((JSONObject)item.get(helper.getPosition())).getString(""));
//        helper.setText(R.id.tv_xdzs,((JSONObject)item.get(helper.getPosition())).getString(""));
//        helper.setText(R.id.tv_sgs,((JSONObject)item.get(helper.getPosition())).getString(""));
//        helper.setText(R.id.tv_kyjl,((JSONObject)item.get(helper.getPosition())).getString(""));
//    }

    private JSONArray data=null;
    private Context context;
    public DutyTotalSecondAdp(Context context, JSONArray data) {
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
            viewHolde.tv_cjrs.setVisibility(View.GONE);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }
//        CJRS:处警人数
//        PCSMC:派出所名称
//        PCSBM:派出所
//        XLDS:巡逻段数
//        XLRS:巡逻人数
        String PCSMC =((JSONObject)data.get(position)).getString("ORGNAME");
        String XLDS =((JSONObject)data.get(position)).getString("XDSL");
        String XLRS =((JSONObject)data.get(position)).getString("XLRS");
        String DISTANCE =((JSONObject)data.get(position)).getString("TOTALDISTANCE");
        viewHolde.tv_pcs.setText(PCSMC);
        viewHolde.tv_sgs.setText(XLDS== null ? "0"  : XLDS);
        viewHolde.tv_kyjl.setText(DISTANCE== null ? "0" : DISTANCE);
        viewHolde.tv_xdzs.setText(XLRS== null ? "0" : XLRS);
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
