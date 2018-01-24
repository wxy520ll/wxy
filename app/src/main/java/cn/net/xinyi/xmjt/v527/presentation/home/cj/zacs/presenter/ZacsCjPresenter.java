package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.presenter;

import com.xinyi_tech.comm.base.BasePresenter;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository.ZacsCjRepository;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjQueryCondition;

/**
 * Created by Fracesuit on 2018/1/19.
 */

public class ZacsCjPresenter extends BasePresenter {
    ZacsCjRepository zacsCjRepository = new ZacsCjRepository();

    public void getZajcxxlistByQueryNew(ZacjQueryCondition zacjQueryCondition,int requestCode) {
        execute(zacsCjRepository.getZajcxxlistByQueryNew(zacjQueryCondition), requestCode);
    }
}
