package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;

import java.util.Collection;
import java.util.HashMap;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.MachineFowModle;

/**
 * Created by hao.zhou on 2016/3/20.
 */
public class MachineFollowAdp extends BaseListAdp<MachineFowModle> {

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;

    private  Collection<MachineFowModle> mDatas;
    private String FELLOWLIST;


    public MachineFollowAdp(AbsListView view, Collection<MachineFowModle> mDatas, int itemLayoutId, Context mContext, String FELLOWLIST) {
        super(view, mDatas, itemLayoutId, mContext);
        this.mDatas=mDatas;
        this.FELLOWLIST=FELLOWLIST;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();  // 初始化数据  默认为不选中的状态
    }

    private void initDate() {
        for(int i=0; i<mDatas.size();i++) {
            getIsSelected().put(i,false);
        }
    }

    @Override
    public void convert(final AdapterHolder helper, MachineFowModle item, boolean isScrolling) {
        helper.setText(R.id.tv_xm, item.getXM()+"("+item.getSJHM()+")");
        helper.setText(R.id.tv_sfz, item.getSFZH());
        CheckBox checkBox2= helper.getView(R.id.checkBox2);

        if (null!=FELLOWLIST&&FELLOWLIST.contains(""+item.getID())){
            checkBox2.setChecked(true);
            isSelected.put(helper.getPosition(), true);
            setIsSelected(isSelected);
        }

        // 监听checkBox并根据原来的状态来设置新的状态
        checkBox2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isSelected.get(helper.getPosition())) {
                    isSelected.put(helper.getPosition(), false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(helper.getPosition(), true);
                    setIsSelected(isSelected);
                }
            }
        });
        // 根据isSelected来设置checkbox的选中状况
        checkBox2.setChecked(getIsSelected().get(helper.getPosition()));
    }



    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        MachineFollowAdp.isSelected = isSelected;
    }

}



