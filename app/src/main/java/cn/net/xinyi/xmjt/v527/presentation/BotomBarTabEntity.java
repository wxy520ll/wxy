package cn.net.xinyi.xmjt.v527.presentation;

import com.flyco.tablayout.listener.CustomTabEntity;

import cn.net.xinyi.xmjt.v527.manager.MainItemModel;

public class BotomBarTabEntity implements CustomTabEntity {
    MainItemModel tab;

    public BotomBarTabEntity(MainItemModel tab) {
        this.tab = tab;
    }

    @Override
    public String getTabTitle() {
        return tab.getTitle();
    }

    @Override
    public int getTabSelectedIcon() {
        return tab.getDrawableId();
    }

    @Override
    public int getTabUnselectedIcon() {
        return tab.getNormalDrawable();
    }
}
