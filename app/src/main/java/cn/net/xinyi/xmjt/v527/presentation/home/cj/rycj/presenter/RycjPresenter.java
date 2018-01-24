package cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj.presenter;

import com.xinyi_tech.comm.base.BasePresenter;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository.RycjRepository;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj.mode.RycjOtherModel;

/**
 * Created by Fracesuit on 2018/1/18.
 */

public class RycjPresenter extends BasePresenter {

    RycjRepository rycjRepository = new RycjRepository();

    public void addPersonidentity(RycjOtherModel rycjOtherModel, int requestCode) {
        execute(rycjRepository.addPersonidentity(rycjOtherModel), requestCode);
    }
}
