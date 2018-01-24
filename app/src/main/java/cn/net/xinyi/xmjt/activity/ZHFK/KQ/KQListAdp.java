package cn.net.xinyi.xmjt.activity.ZHFK.KQ;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.KQModle;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/4/11.
 */
public class KQListAdp extends BaseListAdp<KQModle> {
    private UserInfo user;
    private Context context;
    private PopupWindow mpopupWindow;
    private ImageView iv_ck;


    public KQListAdp(AbsListView view, Collection<KQModle> mDatas, int itemLayoutId, Context mContext) {
        super(view, mDatas, itemLayoutId, mContext);
        context = this.mContext;
    }

    @Override
    public void convert(AdapterHolder helper, final KQModle item, boolean isScrolling) {
        user = AppContext.instance.getLoginInfo();
        helper.setText(R.id.tv_name, user.getName());
        helper.setText(R.id.tv_dh, user.getUsername());
        helper.setText(R.id.tv_sj, item.getYEAR() + " " + item.getDATA() + " " + item.getWEEK());
        helper.setText(R.id.tv_dw, user.getPcs());
        helper.setText(R.id.tv_dz, item.getDZ());
        helper.setText(R.id.tv_zt, item.getLX());
        if (item.getLX().contains("下")) {
            helper.getConvertView().findViewById(R.id.tv_zt)
                    .setBackgroundColor(context.getResources().getColor(R.color.background_new_tag));
        }
        iv_ck = (ImageView) helper.getConvertView().findViewById(R.id.iv_ck);
        iv_ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(ImageUtils.ImageLoad(item.getSCSJ()), item.getIV_ZP());
            }
        });
    }


    private void showPopMenu(String scsj, String iv_zp) {
        View view = View.inflate(context,
                R.layout.plate_full_image, null);
        ImageView fullImage = (ImageView) view
                .findViewById(R.id.full_plate_image);

        if (null != iv_zp) {
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/kq/" + scsj + "/" + iv_zp, fullImage);
        }

        fullImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mpopupWindow.dismiss();
            }
        });

        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow(context);
            mpopupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
            mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mpopupWindow.setFocusable(true);
            mpopupWindow.setOutsideTouchable(true);
        }

        mpopupWindow.setContentView(view);
        mpopupWindow.showAtLocation(iv_ck, Gravity.CENTER, 0, 0);
        mpopupWindow.update();

        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(1000);
        view.setAnimation(aa);
    }

}
