package cn.net.xinyi.xmjt.activity.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.net.xinyi.xmjt.R;

/**
 * Created by Fracesuit on 2017/12/22.
 */

public class TxlFragment extends BaseNewFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.icon_test_gzrz);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
}
