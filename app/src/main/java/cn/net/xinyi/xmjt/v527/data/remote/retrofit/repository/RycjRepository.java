package cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj.mode.RycjOtherModel;
import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Fracesuit on 2017/12/28.
 */

public class RycjRepository extends UploadRepository {
    public Observable<String> addPersonidentity(final RycjOtherModel rycjOtherModel) {

        final String photo_1 = rycjOtherModel.getPhoto_1();
        final String photo_2 = rycjOtherModel.getPhoto_2();
        final String photo_3 = rycjOtherModel.getPhoto_3();
        return Observable.just(photo_1, photo_2, photo_3)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        if (StringUtils.isEmpty(s)) {
                            return Observable.just(null);
                        }
                        return uploadFileForString("personidentity", s).map(new Func1<String, String>() {
                            @Override
                            public String call(String s) {
                                return "/slry/" + s;
                            }
                        });
                    }
                }).toList()
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        rycjOtherModel.setPhoto_1(strings.get(0));
                        rycjOtherModel.setPhoto_2(strings.get(1));
                        rycjOtherModel.setPhoto_3(strings.get(2));
                        rycjOtherModel.setCjsj(TimeUtils.getNowString(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.getDefault())));
                        rycjOtherModel.setCjyh(AppContext.instance.getLoginInfo().getUsername());
                        return AppContext.apiService.addPersonidentity(rycjOtherModel);
                    }
                })
                ;


    }
}
