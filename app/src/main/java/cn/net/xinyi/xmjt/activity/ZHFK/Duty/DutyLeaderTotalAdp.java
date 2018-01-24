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
public class DutyLeaderTotalAdp extends BaseAdapter {

    private JSONArray data=null;
    private Context context;
    public DutyLeaderTotalAdp(Context context, JSONArray data) {
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
            viewHolde.tv_pcs=(TextView)convertView.findViewById(R.id.tv_pcs);
            viewHolde.tv_jcsj=(TextView)convertView.findViewById(R.id.tv_xdzs);
            viewHolde.tv_xz=(TextView)convertView.findViewById(R.id.tv_sgs);
            viewHolde.tv_czsj=(TextView)convertView.findViewById(R.id.tv_kyjl);
            viewHolde.tv_cjrs=(TextView)convertView.findViewById(R.id.tv_cjrs);
            convertView.setTag(viewHolde);
        }else{
            viewHolde= (ViewHolde) convertView.getTag();
        }


        //ZDZ 派出所编码
        //XZXM  巡长姓名
        //XZHM巡长号码
        //SCSJ最后一次操作时间
        //ZSJ总时间

        String PCSMC =((JSONObject)data.get(position)).getString("ZDZ");
        String XZXM =((JSONObject)data.get(position)).getString("XZXM");
        String XZHM =((JSONObject)data.get(position)).getString("XZHM");
        String SCSJ =((JSONObject)data.get(position)).getString("SCSJ");
        String ZSJ =((JSONObject)data.get(position)).getString("ZSJ");
        viewHolde.tv_pcs.setText(PCSMC);

        if (null != XZXM){//巡长信息
            viewHolde.tv_xz.setText(XZXM+"("+XZHM+")");
            viewHolde.tv_xz.setTextColor(context.getResources().getColor(R.color.black));
        }else {
            viewHolde.tv_xz.setText("无巡长");
            viewHolde.tv_xz.setTextColor(context.getResources().getColor(R.color.bbutton_danger));
        }

        if (null != SCSJ){//上传时间
            viewHolde.tv_czsj.setText(SCSJ);
        }else {
            viewHolde.tv_czsj.setText("");
        }

        if (null != ZSJ){//检查总时间
            viewHolde.tv_jcsj.setText(ZSJ);
        }else {
            viewHolde.tv_jcsj.setText("");
        }

        viewHolde.tv_czsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPeopleClickListener!=null){
                    onPeopleClickListener.onHistoryClick(v,data.getJSONObject(position));
                }
            }
        });

        viewHolde.tv_cjrs.setVisibility(View.GONE);
        return convertView;
    }



    class  ViewHolde{
        TextView tv_pcs;//派出所
        TextView tv_jcsj;//检查时间
        TextView tv_xz;//巡长
        TextView tv_czsj;//最后一次操作数据
        TextView tv_cjrs;//
    }


    private OnPeopleClickListener onPeopleClickListener;
    /**
     * 巡逻点击事件
     */
    public interface OnPeopleClickListener {
        void onHistoryClick(View view, JSONObject jsonObject);
    }

    public void setOnPeopleClickListener(OnPeopleClickListener onPeopleClickListener) {
        this.onPeopleClickListener = onPeopleClickListener;
    }
}
