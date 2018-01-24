package cn.net.xinyi.xmjt.v527.presentation.mine.presenter;

import com.alibaba.fastjson.JSONObject;
import com.xinyi_tech.comm.base.BasePresenter;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository.MineRepository;

/**
 * Created by Fracesuit on 2017/12/28.
 */

public class MinePresenter extends BasePresenter {
    MineRepository mineRepository = new MineRepository();

    public void updateUseInfo(int requestCode, JSONObject jsonObject) {
        execute(mineRepository.updateUseInfo(jsonObject), requestCode);
    }
}
