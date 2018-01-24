package cn.net.xinyi.xmjt.v527.presentation.task.csxc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allen.library.SuperButton;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.comm.util.ToolbarUtils;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.ZajcEntity;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.adapter.ZajcPreAdapter;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.CsxcModel;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.ZajcModel;
import cn.net.xinyi.xmjt.v527.presentation.task.presenter.RwPresenter;

import static cn.net.xinyi.xmjt.api.ApiHttpClient.IMAGE_HOST;

public class ZajcPreActivity extends BaseListActivity<ZajcPreAdapter, ZajcEntity, RwPresenter> {

    CsxcModel csxcModel;
    private String jljcId;
    private ZajcModel checkresult;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        csxcModel = (CsxcModel) getIntent().getSerializableExtra("model");
        jljcId = getIntent().getStringExtra("jljcId");

        super.onCreateAfter(savedInstanceState);
        addButtonAtLast();
        ToolbarUtils.with(this, toolbar).setSupportBack(true).setTitle("场所巡查", false).build();
    }


    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder()
                .isEnableLoadMore(false)
                .isEnableRefresh(false)
                .isDivided(false)
                .build();
    }

    @Override
    protected ZajcPreAdapter getAdapter() {
        return new ZajcPreAdapter(this);
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        if (csxcModel != null) {
            mPresenter.getJcxmByHylx(csxcModel.getCscode(), requestCode);
        } else {
            mPresenter.getCheckResultById(jljcId, requestCode);
        }
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (csxcModel != null) {
            super.doParseData(requestCode, data);
        } else {
            JSONObject jsonObject = (JSONObject) data;
            final List<ZajcEntity> detail = JSON.parseArray(jsonObject.getString("DETAIL"), ZajcEntity.class);
            checkresult = JSON.parseObject(jsonObject.getString("CHECKRESULT"), ZajcModel.class);
            checkresult.setJczp(IMAGE_HOST + checkresult.getJczp());
            checkresult.setQmzp(IMAGE_HOST + checkresult.getQmzp());
            super.doParseData(requestCode, detail);
        }
    }

    @Override
    protected RwPresenter getPresenter() {
        return new RwPresenter();
    }

    private void addButtonAtLast() {
        final SuperButton superButton = new SuperButton(Utils.getApp());
        superButton.setShapeCornersRadius(0)
                .setShapeSelectorNormalColor(ResUtils.getColor(R.color.comm_blue))
                .setShapeSelectorPressedColor(ResUtils.getColor(R.color.line))
                .setShapeUseSelector(true)
                .setUseShape();

        superButton.setTextColor(ResUtils.getColor(R.color.comm_white));
        superButton.setTextSize(16);
        superButton.setText("下一步");
        superButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (csxcModel != null) {
                    final Intent intent = new Intent(ZajcPreActivity.this, ZajcActivity.class);
                    intent.putExtra("selectCodes", getCodes());
                    intent.putExtra("taskId", csxcModel.getId());
                    intent.putExtra("csId", csxcModel.getCsId());
                    ActivityUtils.startActivity(intent);
                } else {
                    final Intent intent = new Intent(ZajcPreActivity.this, ZajcActivity.class);
                    intent.putExtra("checkresult", checkresult);
                    ActivityUtils.startActivity(intent);
                }
            }
        });

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(45));
        baselistContainer.addView(superButton, layoutParams);
    }


    private String getCodes() {
        final List<ZajcEntity> data = baseListAdapter.getData();
        final StringBuilder stringBuilder = new StringBuilder();
        for (ZajcEntity d : data) {
            stringBuilder.append(d.getCIID() + "," + d.getDEFAULTCHECK() + ";");
        }
        return stringBuilder.toString();
    }


}
