package com.umeng.simplify.ui.views;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;

import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.db.ctrl.impl.DatabaseAPI;
import com.umeng.comm.core.imageloader.utils.BitmapDecoder;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.ImageResponse;
import com.umeng.comm.core.utils.DeviceUtils;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.comm.core.utils.ToastMsg;
import com.umeng.common.ui.dialogs.CustomCommomDialog;
import com.umeng.common.ui.widgets.ClipImageLayout;

/**
 * Created by wangfei on 16/5/6.
 */
public class UploadImageDialog extends Dialog implements View.OnClickListener {

    private ClipImageLayout mClipImageLayout;
    private Uri mImgUri = null;
    private OnClickSaveListener mSaveListener;
    private String getUrl;
    /**
     * 构造函数
     *
     * @param context
     */
    public UploadImageDialog(Context context, Uri uri, int theme) {
        super(context, theme);
        this.mImgUri = uri;
        init();
    }

    /**
     * 初始化相关资源跟设置点击事件</br>
     */
    private void init() {
        int layout = ResFinder.getLayout("umeng_commm_pic_clip");
        int imageLayoutId = ResFinder.getId("umeng_comm_clip_layout");
        setContentView(layout);
        mClipImageLayout = (ClipImageLayout) findViewById(imageLayoutId);
        int backResId = ResFinder.getId("umeng_comm_clip_back");
        int saveResId = ResFinder.getId("umeng_comm_clip_save");
        findViewById(backResId).setOnClickListener(this);
        findViewById(saveResId).setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        loadImage();
    }
    public void setCloseListener(){

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        int backResId = ResFinder.getId("umeng_comm_clip_back");
        int saveResId = ResFinder.getId("umeng_comm_clip_save");
        if (id == backResId) {
            dismiss();
        } else if (id == saveResId) {
            clipImage();
        }
    }

    /**
     * 剪切图片并执行回调</br>
     */
    private void clipImage() {
        Bitmap bitmap = mClipImageLayout.clip();
//        mSaveListener.onClickSave(bitmap);
        // 更新用户头像
        updateUserPortrait(bitmap);
    }

    /**
     * 更新用户头像
     */
    private void updateUserPortrait(final Bitmap bmp) {

        final Dialog progressDialog = new CustomCommomDialog(getContext(),ResFinder.getString("umeng_comm_update_user_icon"));

        progressDialog.setCanceledOnTouchOutside(false);

        if (!DeviceUtils.isNetworkAvailable(getContext())){
            ToastMsg.showShortMsgByResName("umeng_comm_update_icon_failed");
            return;
        }
        progressDialog.show();
        CommunityFactory.getCommSDK(getContext()).uploadImage(bmp,
                new Listeners.SimpleFetchListener<ImageResponse>() {

                    @Override
                    public void onStart() {


                    }



                    @Override
                    public void onComplete(ImageResponse response) {
                        progressDialog.dismiss();

                        if (response != null && response.errCode == ErrorCode.NO_ERROR) {
                           getUrl = response.result.originImageUrl;
                            mSaveListener.onClickSave(bmp,getUrl);
                        } else {
                            ToastMsg.showShortMsgByResName("umeng_comm_update_icon_failed");
                        }
                    }

                });
    }

    private void syncUserIconUrlToDB(CommUser user) {
        DatabaseAPI.getInstance().getUserDBAPI().saveUserInfoToDB(user);
    }

    /**
     * 设置点击保存时的回调</br>
     *
     * @param listener
     */
    public void setOnClickSaveListener(OnClickSaveListener listener) {
        this.mSaveListener = listener;
    }

    /**
     * 根据uri加载图片</br>
     */
    private void loadImage() {
        BitmapDecoder decoder = getBitmapDecoder();
        mClipImageLayout.setImageDrawable(decoder.decodeBitmap(Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT));
    }

    private BitmapDecoder getBitmapDecoder() {
        return new BitmapDecoder() {

            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                final ContentResolver contentResolver = getContext().getContentResolver();
                try {
                    return BitmapFactory.decodeStream(contentResolver
                            .openInputStream(mImgUri), null, options);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    /**
     * 点击保存按钮时的回调
     */
    public interface OnClickSaveListener {
        void onClickSave(Bitmap bitmap, String url);
    }

}
