package cn.net.xinyi.xmjt.v527.presentation.txl.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class TxlGroup implements MultiItemEntity {
    private String groupName;

    public TxlGroup(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int getItemType() {
        return 2;
    }
}
