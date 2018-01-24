package cn.net.xinyi.xmjt.weiget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.utils.IdCard.SharedPreferencesHelper;

/**
 * Created by jiajun.wang on 2017/10/12.
 */

public class TipsDialog extends Dialog {
    private Context context;
    public TipsDialog(@NonNull Context context) {
        super(context);
    }

    public TipsDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tips);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        ImageView tip3= (ImageView) findViewById(R.id.tip3);
        ImageView tip4= (ImageView) findViewById(R.id.tip4);
        tip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesHelper.putString(context,"key","value");
                dismiss();
            }
        });

        tip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Window dialogWindow = getWindow();
        WindowManager m = ((Activity)context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.gravity= Gravity.CENTER;
        p.height = (int) (d.getHeight() * 0.7); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);

    }
}
