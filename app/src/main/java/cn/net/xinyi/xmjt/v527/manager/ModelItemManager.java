package cn.net.xinyi.xmjt.v527.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.GzrzFragment;
import cn.net.xinyi.xmjt.v527.presentation.home.HomeFragment;
import cn.net.xinyi.xmjt.v527.presentation.lgq.FeedsFragment;
import cn.net.xinyi.xmjt.v527.presentation.task.RwFragment;
import cn.net.xinyi.xmjt.v527.presentation.txl.TxlFragment;

/**
 * Created by Fracesuit on 2017/12/21.
 */

public class ModelItemManager {

    /**
     * 超级管理员	0
     * 分局民警管理员	1
     * 分局辅警(聘员)管理员	2
     * 派出所民警管理员	3
     * 派出所辅警(聘员)管理员	4
     * 分局民警	5
     * 分局辅警(聘员)	6
     * 派出所民警	7
     * 派出所辅警(聘员)	8
     * 综管员	9
     * 保安员	10
     * 管道	11
     * 厂商	12
     */

    private static List<MainItemModel> allModule() {
        ArrayList<MainItemModel> mainItemModels = new ArrayList<>();
        mainItemModels.add(generateImageTitleModel(0, "巡逻勤务", String.valueOf(0)));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_jwxl, "勤务巡逻", String.valueOf(10), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_kdqw, "卡点勤务", String.valueOf(11), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_gzdc, "工作督查", String.valueOf(12), 0, 1, 3, 5, 7));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_dtzs, "地图展示", String.valueOf(13), 0, 1, 2, 3, 4, 5, 6, 7, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_qwsz, "勤务设置", String.valueOf(14), 0, 1, 2, 3, 4));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_qwtj, "勤务统计", String.valueOf(15), 0, 1, 2, 3, 4, 5, 6, 7, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_wcft, "外出防逃", String.valueOf(16), 0, 1, 2, 3, 4, 5, 6, 7, 8));
        mainItemModels.add(generateImageTitleModel(0, "信息采集", String.valueOf(1)));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_clcj, "车辆采集", String.valueOf(100), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_esld, "二三类点", String.valueOf(101), 0, 2, 4, 6, 8, 9));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_zacs, "治安场所", String.valueOf(102), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_rycj, "人员采集", String.valueOf(103), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_xsgl, "线索管理", String.valueOf(104), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_cjtj, "采集统计", String.valueOf(105), 0, 1, 2, 3, 4, 5, 6, 7, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_cjgl, "采集管理", String.valueOf(106), 0, 1, 2, 4, 6, 8));
        mainItemModels.addAll(getRwItem());
        mainItemModels.add(generateImageTitleModel(0, "内部工作", String.valueOf(3)));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_aqgl, "安全管理", String.valueOf(300), 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_tsyy, "提审预约", String.valueOf(302), 0, 3, 5, 7));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_xzcx, "薪资查询", String.valueOf(303), 0, 2, 4, 6, 8, 10));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_jkgl, "健康管理", String.valueOf(304), 0, 2, 4, 6, 8, 10));

        return mainItemModels;
    }


    private static List<MainItemModel> getRwItem() {
        ArrayList<MainItemModel> mainItemModels = new ArrayList<>();
        mainItemModels.add(generateImageTitleModel(0, "治安巡查", String.valueOf(2)));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_xxhc, "信息核查", String.valueOf(301), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_csxc, "场所巡查", String.valueOf(200), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_zfry, "人员巡访", String.valueOf(201), 0, 2, 4, 6, 8));
        mainItemModels.add(generateImageTitleModel(R.drawable.icon_jqhf, "警情回访", String.valueOf(202), 0, 2, 4, 6, 8));
        return mainItemModels;
    }

    private static List<MainItemModel> getTabItem() {
        ArrayList<MainItemModel> mainItemModels = new ArrayList<>();
        String[] mTitles = {"首页", "任务", "通讯录", "龙岗圈", "工作日志"};
        int[] mIconUnselectIds = {R.drawable.icon_tab_home_normal, R.drawable.icon_tab_rw_normal, R.drawable.icon_tab_mine_normal, R.drawable.icon_tab_lgq_normal, R.drawable.icon_tab_gzrz_normal};
        int[] mIconSelectIds = {R.drawable.icon_tab_home_selected, R.drawable.icon_tab_rw_selected, R.drawable.icon_tab_mine_selected, R.drawable.icon_tab_lgq_selected, R.drawable.icon_tab_gzrz_selected};
        int[][] roleids = {{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},//首页
                {0, 2, 4, 8},//任务
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 10},//通讯录
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 10},//龙岗圈
                {0, 2, 4, 8}//工作日志
        };
        Class[] clazzes = new Class[]{HomeFragment.class, RwFragment.class, TxlFragment.class, FeedsFragment.class, GzrzFragment.class};
        for (int i = 0, len = mTitles.length; i < len; i++) {
            final MainItemModel itemModel = generateImageTitleModel(0, mTitles[i], String.valueOf(i), roleids[i]);
            itemModel.setNormalDrawable(mIconUnselectIds[i]);
            itemModel.setDrawableId(mIconSelectIds[i]);
            itemModel.setClazz(clazzes[i]);
            mainItemModels.add(itemModel);
        }

        return mainItemModels;
    }


    private static MainItemModel generateImageTitleModel(int drawableId, String title, String uniquenessId, int... roleIds) {
        MainItemModel model = new MainItemModel(drawableId, title);
        model.setRoleIds(roleIds);
        model.setUniquenessId(uniquenessId);
        return model;
    }

    public static int getRoleTypeByRoleName() {
        UserInfo userInfo = AppContext.instance().getLoginInfo();
        final String accounttype = userInfo.getAccounttype();
        final String roleNmae = userInfo.getRoleNmae();
        if ("超级管理员".equals(roleNmae)) {
            return 0;
        } else if ("分局民警管理员".equals(roleNmae)) {
            return 1;
        } else if ("分局辅警管理员".equals(roleNmae) || "分局聘员管理员".equals(roleNmae)) {
            return 2;
        } else if ("派出所民警管理员".equals(roleNmae)) {
            return 3;
        } else if ("派出所辅警管理员".equals(roleNmae) || "派出所聘员管理员".equals(roleNmae)) {
            return 4;
        } else if ("分局民警".equals(roleNmae)) {
            return 5;
        } else if ("分局辅警".equals(roleNmae) || "分局聘员".equals(roleNmae)) {
            return 6;
        } else if ("派出所民警".equals(roleNmae)) {
            return 7;
        } else if ("派出所辅警".equals(roleNmae) || "派出所聘员".equals(roleNmae)) {
            return 8;
        } else if ("综管员".equals(roleNmae)) {
            return 9;
        } else if ("保安员".equals(accounttype)) {
            return 10;
        } else if ("管道公司".equals(accounttype)) {
            return 11;
        } else if ("厂商".equals(roleNmae)) {
            return 12;
        } else {
            return -1;//没有设置权限
        }
    }


    private static List<MainItemModel> getItemByRole(List<MainItemModel> mainItemModels) {
        final int roleId = getRoleTypeByRoleName();
        ArrayList<MainItemModel> mainModeItems = new ArrayList<>();
        ArrayList<MainItemModel> deteteGroup = new ArrayList<>();

        for (int i = 0, len = mainItemModels.size(); i < len; i++) {
            final MainItemModel roleModel = mainItemModels.get(i);
            if (roleModel.getDrawableId() == 0) {
                if (mainModeItems.size() > 0) {
                    final MainItemModel group = mainModeItems.get(mainModeItems.size() - 1);
                    if (group.getDrawableId() == 0) {
                        deteteGroup.add(group);
                    }
                }
                mainModeItems.add(roleModel);
            } else if ((roleModel.contantRole(roleId))) {
                mainModeItems.add(roleModel);
            }
        }
        Collections.sort(mainModeItems);
        //过滤掉分组(加入分组没有任何数据的时候)
        mainModeItems.removeAll(deteteGroup);

        final int size = mainModeItems.size();
        if (size > 0) {
            if (mainModeItems.get(size - 1).getDrawableId() == 0) {
                mainModeItems.remove(size - 1);
            }
        }


        return mainModeItems;
    }


    //下面是我们需要的
    public static List<MainItemModel> getMainItemByRole() {
        return getItemByRole(allModule());
    }

    public static List<MainItemModel> getRwItemByRole() {
        return getItemByRole(getRwItem());
    }

    //底下的tab
    public static List<MainItemModel> getTabByRole() {
        return getItemByRole(getTabItem());
    }


    public static List<MainItemModel> getOtherByRole() {//工作动态  工作成效
        return getItemByRole(getRwItem());
    }

    public static boolean canLook(Integer... roleIds) {
        final Integer roleTypeByRoleName = ModelItemManager.getRoleTypeByRoleName();
        return Arrays.asList(roleIds).contains(roleTypeByRoleName);
    }

}
