package cn.net.xinyi.xmjt.v527.presentation.home.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.xinyi_tech.comm.util.ImageLoaderUtils;
import com.xinyi_tech.comm.util.ToastyUtil;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.MineActivity;
import cn.net.xinyi.xmjt.activity.ZHFK.KQ.KQAty;
import cn.net.xinyi.xmjt.v527.manager.ModelItemManager;
import cn.net.xinyi.xmjt.v527.presentation.home.model.PersonInfo;

import static cn.net.xinyi.xmjt.api.ApiHttpClient.IMAGE_HOST;


/**
 * Created by Fracesuit on 2017/6/29.
 */

public class PersonInfoAdapter extends DelegateAdapter.Adapter<PersonInfoAdapter.HeaderHolder> {
    private PersonInfo prsonInfo;
    private LayoutHelper mLayoutHelper;
    private Activity activity;

    public PersonInfoAdapter(Activity activity, LayoutHelper layoutHelper, PersonInfo prsonInfo) {
        this.prsonInfo = prsonInfo;
        this.activity = activity;
        this.mLayoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public HeaderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_person_info, parent, false));
    }

    @Override
    public void onBindViewHolder(HeaderHolder holder, int position) {
        holder.superTextView.setLeftTextIsBold(true)
                .setLeftString(prsonInfo.getName())
                .setLeftBottomString(prsonInfo.getDeptInfo())
        ;
        holder.superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(activity, MineActivity.class);
            }
        });

        if (ModelItemManager.canLook(0, 2, 4, 6, 8, 10)) {
            holder.llqd.setVisibility(View.VISIBLE);
            holder.llqd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.startActivity(activity, KQAty.class);
                }
            });
       /*     holder.superTextView.setRightIcon(R.drawable.icon_qidndao)
                    .setRightImageViewClickListener(new SuperTextView.OnRightImageViewClickListener() {
                        @Override
                        public void onClickListener(ImageView imageView) {

                        }
                    });*/
        } else {
            holder.llqd.setVisibility(View.GONE);
        }
        holder.pm.setText(prsonInfo.getPm());
        holder.jf.setText(prsonInfo.getJf());
        holder.lljf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastyUtil.warningShort("开发中");
            }
        });
        holder.llpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastyUtil.warningShort("开发中");
            }
        });
        final ImageView leftIconIV = holder.superTextView.getLeftIconIV();
        if (prsonInfo.getHeadImg() != null) {
            ImageLoaderUtils.showImage(leftIconIV, IMAGE_HOST + "/user/" + prsonInfo.getHeadImg());
        } else {
            ImageLoaderUtils.showImage(leftIconIV, R.drawable.app_icon);
        }

        holder.ll_jfpm.setVisibility(ModelItemManager.canLook(0, 2, 4, 6, 8, 10) ? View.VISIBLE : View.GONE);

//PersonActivity
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        SuperTextView superTextView;
        TextView jf;
        TextView pm;
        View ll_jfpm;
        View lljf;
        View llpm;
        View llqd;

        HeaderHolder(View view) {
            super(view);
            superTextView = (SuperTextView) view.findViewById(R.id.stv_info);
            jf = (TextView) view.findViewById(R.id.tv_jf);
            pm = (TextView) view.findViewById(R.id.tv_pm);
            ll_jfpm = view.findViewById(R.id.ll_jfpm);
            lljf = view.findViewById(R.id.lljf);
            llpm = view.findViewById(R.id.llpm);
            llqd = view.findViewById(R.id.llqd);
        }
    }
}



