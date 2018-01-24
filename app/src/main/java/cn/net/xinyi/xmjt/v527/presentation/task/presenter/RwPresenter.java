package cn.net.xinyi.xmjt.v527.presentation.task.presenter;

import com.xinyi_tech.comm.base.BasePresenter;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository.RwRepository;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.ZajcModel;

/**
 * Created by jiajun.wang on 2017/12/30.
 */

public class RwPresenter extends BasePresenter {
    private RwRepository rwRepository = new RwRepository();

    public void getTasklistByQuery(String stateCode, int pageIndex, int pageSize, int requesCode) {
        execute(rwRepository.getTasklistByQuery(stateCode, pageIndex, pageSize), requesCode);
    }

    public void getTaskDetail(String id, String taskCode, int requesCode) {
        execute(rwRepository.getTaskDetail(id, taskCode), requesCode);
    }

    public void stopTaskById(String id, int requesCode) {
        execute(rwRepository.stopTaskById(id), requesCode);
    }

    public void getJcxmByHylx(String code, int requesCode) {
        execute(rwRepository.getJcxmByHylx(code), requesCode);
    }

    public void getCheckResultById(String id, int requesCode) {
        execute(rwRepository.getCheckResultById(id), requesCode);
    }

    public void getJcjl(String sysId, int pageIndex, int pageSize, int requesCode) {
        execute(rwRepository.getJcjl(sysId, pageIndex, pageSize), requesCode);
    }

    public void addZajc(ZajcModel zajcModel, int requesCode) {
        execute(rwRepository.addZajc(zajcModel), requesCode);
    }
}
