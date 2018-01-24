package cn.net.xinyi.xmjt.v527.presentation.task;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.base.BaseFragment;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.comm.util.ToolbarUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.InfoCheck.InfoCheckMainAty;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.v527.manager.MainItemModel;
import cn.net.xinyi.xmjt.v527.manager.ModelItemManager;
import cn.net.xinyi.xmjt.v527.presentation.MainPresenter;
import cn.net.xinyi.xmjt.v527.presentation.home.adapter.ItemModelAdapter;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.CsxcActivity;

/**
 * Created by Fracesuit on 2017/12/21.
 */

public class RwFragment extends BaseFragment<MainPresenter> {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    final UserInfo loginInfo = AppContext.instance.getLoginInfo();

    @Override
    protected void requestData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home2;
    }


    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }


    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        ToolbarUtils.with(activity, mToolBar).setTitle(ResUtils.getString(R.string.app_fullname), true).build();
    }

    public void initRecyclerView() {
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 100);

        final DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        adapters.add(getItemAdapter(0));
        delegateAdapter.setAdapters(adapters);
    }


    private ItemModelAdapter getItemAdapter(final int startPosition) {
        final List<MainItemModel> list = ModelItemManager.getRwItemByRole();
        GridLayoutHelper zaxcGridLayoutHelper = new GridLayoutHelper(4);
        zaxcGridLayoutHelper.setBgColor(ContextCompat.getColor(activity, R.color.white));
        zaxcGridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                final MainItemModel mainItemModel = list.get(position - startPosition);
                return mainItemModel.getDrawableId() == 0 ? 4 : 1;
            }
        });
        zaxcGridLayoutHelper.setVGap(SizeUtils.dp2px(10));
        zaxcGridLayoutHelper.setAutoExpand(false);
        final ItemModelAdapter itemModelAdapter = new ItemModelAdapter(zaxcGridLayoutHelper, list);
        itemModelAdapter.setOnItemClickListener(new ItemModelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainItemModel imageTitleModel, int position) {
                final MainItemModel mainItemModel = list.get(position);
                final String uniquenessId = mainItemModel.getUniquenessId();
                switch (uniquenessId) {
                    case "200":
                        ActivityUtils.startActivity(activity, CsxcActivity.class);
                        break;//"场所巡查"
                    case "301":
                        ActivityUtils.startActivity(activity, InfoCheckMainAty.class);
                        break;//eck, "信息核查"
                    case "201":
                        Toast.makeText(activity, "待开发", Toast.LENGTH_SHORT).show();
                        break;//"重点人员巡访"
                    case "202":
                        Toast.makeText(activity, "待开发", Toast.LENGTH_SHORT).show();
                        break;//"警情回访"
                }
            }
        });
        return itemModelAdapter;
    }
}
