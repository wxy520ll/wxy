package cn.net.xinyi.xmjt.v527.presentation;

import com.xinyi_tech.comm.base.BasePresenter;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository.MainRepository;

/**
 * Created by Fracesuit on 2017/12/23.
 */

public class MainPresenter extends BasePresenter {

    public static final int NET_GETINFOBYCOMMAND = 1;
    public static final int NET_LOGINBYSCAN = 2;
    public static final int NET_TXL = 3;
    MainRepository mainRepository = new MainRepository();

    public void getInfoByCommand() {
        execute(mainRepository.getInfoByCommand(), NET_GETINFOBYCOMMAND);
    }

    public void loginByScan(String code) {
        execute(mainRepository.loginByScan(code), NET_GETINFOBYCOMMAND);
    }

    public void getTxl(String orgcode, int requestCode) {
        execute(mainRepository.getTxlById(orgcode), requestCode);
    }
    public void getTxl(String orgcode, int requestCode,int t) {
        execute(mainRepository.getTxlById(orgcode,t), requestCode);
    }

    public void getGzcx(String time, int requestCode) {
        execute(mainRepository.getGzcx(time), requestCode);
    }

    public void getGzcxDetailList(String time, String type, int requestCode) {
        execute(mainRepository.getGzcxDetailList(time, type), requestCode);
    }

    public void getJqdt(String time, int pageIndex, int pageSize, int requestCode) {
        execute(mainRepository.getJqlistByQuery(time, pageIndex, pageSize), requestCode);
    }

    public void getJqdtList(String time, int pageIndex, int pageSize, int requestCode) {
        execute(mainRepository.getJqlistByQuery(time, pageIndex, pageSize), requestCode);
    }

    public void initHomeData(int requestCode) {
        execute(mainRepository.initHomeData(), requestCode);
    }


}
