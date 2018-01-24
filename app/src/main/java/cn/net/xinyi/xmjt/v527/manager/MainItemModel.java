package cn.net.xinyi.xmjt.v527.manager;

import com.xinyi_tech.comm.model.ImageTitleModel;

/**
 * Created by zhiren.zhang on 2017/9/27.
 */

public class MainItemModel extends ImageTitleModel {
    private int[] roleIds;//属于哪些角色
    private int normalDrawable;

    public MainItemModel(int drawableId, String title) {
        super(drawableId, title);
    }

    public int getNormalDrawable() {
        return normalDrawable;
    }

    public MainItemModel setNormalDrawable(int normalDrawable) {
        this.normalDrawable = normalDrawable;
        return this;
    }

    public int[] getRoleIds() {
        return roleIds;
    }

    public MainItemModel setRoleIds(int... roleIds) {
        this.roleIds = roleIds;
        return this;
    }

    public boolean contantRole(int roleid) {
        for (int id : roleIds) {
            if (id == roleid) {
                return true;
            }
        }
        return false;
    }


}
