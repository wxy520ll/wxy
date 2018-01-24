package cn.net.xinyi.xmjt.v527.base;

import android.content.DialogInterface;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xinyi_tech.comm.base.BaseActivity;
import com.xinyi_tech.comm.base.BasePresenter;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.activity.Main.LoginActivity;
import cn.net.xinyi.xmjt.api.ApiAsyncHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.AppManager;
import cn.net.xinyi.xmjt.utils.DialogHelper;

/**
 * Created by Fracesuit on 2017/12/23.
 */

public abstract class BaseJwtActivity<P extends BasePresenter> extends BaseActivity<P> {
    public static final int FILE_SELECT_CODE = 0x1;

    @Override
    protected void onResume() {
        super.onResume();
        getSession();//获取当前登录的session,判断是否其他终端登录
    }

    /**
     * USERNAME 获取当前用户的最新session
     * LOGIN_TYPE  类型为手机
     **/
    private void getSession() {
        if (null != AppContext.instance.getLoginInfo().getUsername() && null != ApiAsyncHttpClient.getCookie(AppContext.instance())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USERNAME", AppContext.instance.getLoginInfo().getUsername());
            jsonObject.put("LOGIN_TYPE", "PHONE");
            String json = jsonObject.toJSONString();
            ApiResource.getSessionKey(json, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = subMessage(new String(bytes));
                    String cookie = ApiAsyncHttpClient.getCookie(AppContext.instance());
                    if (i == 200 && result.length() > 4 && cookie != null && !cookie.substring(cookie.lastIndexOf("=") + 1, cookie.length() - 1).equals(result)) {
                        DialogHelper.showAlertDialog(BaseJwtActivity.this, "您的账号当前在其他手机登录，如非本人操作，则密码可能泄露，请至登录界面点击【忘记密码】进行修改！", new DialogHelper.OnOptionClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, Object o) {
                                lodingToLogin();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                }
            });
        }
    }

    // string 返回时 是2个“”，需要进行截取
    private String subMessage(String text) {
        return text.length() > 3 ? text.substring(1, text.length() - 1) : text;
    }

    // 转到登录
    private void lodingToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
        AppManager.getAppManager().finishAllActivity(LoginActivity.class);
    }

}
