package cn.net.xinyi.xmjt.api;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.utils.BaiduMapUtil;

/**
 * Created by mazhongwang on 15/3/16.
 */
public class ApiResource {

    public static void login(String username, String password, String deviceId, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/sessions/login?username={0}&password={1}&LOGIN_TYPE={2}&DEVICEID={3}", username, password, "PHONE", deviceId);
        //  String url = MessageFormat.format("/sessions/login?username={0}&password={1}", username, password);
        ApiAsyncHttpClient.post(url, handler);
    }

    public static void logout(AsyncHttpResponseHandler handler) {
        String url = "/sessions";
        ApiAsyncHttpClient.delete(url, handler);
    }

    public static void addUser(String newUser, AsyncHttpResponseHandler handler) {
        String url = "/users";
        ApiAsyncHttpClient.post(url, handler);
    }

    public static void getSessionKey(String json, AsyncHttpResponseHandler handler) {
        String url = "/sessions/getSessionKey";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    public static void winLogin(String json, AsyncHttpResponseHandler handler) {
        String url = "/sessions/phoneValid";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //同步方式获取版本信息
    public static void getVersionByAppid(String appid, AsyncHttpResponseHandler handler) {
        String url = "/versions/" + appid;
        ApiSyncHttpClient.get(url, handler);
    }

    public static void uploadCapturePlateImage(String imageFileName, String imageFilePath,
                                               AsyncHttpResponseHandler handler) {
        try {
            File file = new File(imageFilePath);
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();
            params.put(imageFileName, file, contentType);
            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/plates/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void addCapturePlate(String json, AsyncHttpResponseHandler handler) {
        String url = "/plates";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    public static void addCameraRoomInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/jks";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    public static void addCameraInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    public static void getPoliceContacts(AsyncHttpResponseHandler handler) {
        String url = "/contacts";
        ApiAsyncHttpClient.get(url, handler);
    }

    public static void getOrganRank(String startDate, String endDate, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/plates/organuploadrank/{0}/{1}", startDate, endDate);
        ApiAsyncHttpClient.get(url, handler);
    }

    public static void getOrganRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/plates/organuploadrank/count";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    public static void getUserRank(String startDate, String endDate, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/plates/useruploadrank/{0}/{1}", startDate, endDate);
        ApiAsyncHttpClient.get(url, handler);
    }

    public static void getUserRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/plates/useruploadrank/count";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    public static void getUserUploadCount(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/plates/useruploadcount/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    public static void getOrganUploadCount(String organ, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/plates/organuploadcount/{0}", organ);
        ApiAsyncHttpClient.get(url, handler);
    }

    public static void getPlateBlack(String lastUpdateTime, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/blackplates/{0}", lastUpdateTime);
        ApiAsyncHttpClient.get(url, handler);
    }

    public static void getPlateBlackDb(String lastUpdateTime, FileAsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/blackplatesdb/{0}", lastUpdateTime);
        ApiAsyncHttpClient.get(url, handler);
    }

    public static String getAddress(String Latitude, String Longitude) {
        String url = MessageFormat.format("http://api.map.baidu.com/geocoder/v2/?ak=sdNrF4dLXabpedYVNlVrdZb7" +
                "&location={0},{1}&output=json", Latitude, Longitude);
        return url;
    }

    public static void uploadCameraRoomImage(List<String> filePath, List<String> fileName,
                                             AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/jks/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传立杆图片
     *
     * @param filePath
     * @param fileName
     * @param handler
     */
    public static void uploadRodsImages(List<String> filePath, List<String> fileName,
                                        AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/rods/upload";
            ApiAsyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void uploadCameraImage(List<String> filePath, List<String> fileName,
                                         AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/sxt/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void getRegister(String json, AsyncHttpResponseHandler handler) {
        String url = "/signup";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    public static void getRegisters(String json, AsyncHttpResponseHandler handler) {
        String url = "/signup";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取所有的字典列表
     */
    public static void getAllZdlb(AsyncHttpResponseHandler handler) {
        String url = "/zdlb";
//        RequestParams params = new RequestParams();
//        params.put("GXSJ",lastUpdateTime);
//        ApiAsyncHttpClient.get(url, params, handler);
        ApiAsyncHttpClient.get(url, handler);
    }


    /***
     * 获取所有的字典信息
     */
    public static void getAllZdxx(AsyncHttpResponseHandler handler) {
        String url = "/zdxx";
//        RequestParams params = new RequestParams();
//        params.put("GXSJ",lastUpdateTime);
//        ApiAsyncHttpClient.get(url, params, handler);
        ApiAsyncHttpClient.get(url, handler);
    }

    //获取已上传监控室列表
    public static void getCameraRoomInfo(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/jks/user/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    //根据时间获取已上传的监控室列表
    public static void postJKSInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/jks/user/query";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //根据时间获取已上传的摄像头列表
    public static void postSXTInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/user/query";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);

    }

    // 监控室审核信息提交
    public static void CheckJKSInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/jks/audit";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    //摄像头审核信息提交
    public static void CheckSXTInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/audit";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    //待审核的监控室
    public static void DSHJKSList(String scdwbh, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/jks/audit/scdwbh/{0}", scdwbh);
        ApiAsyncHttpClient.get(url, handler);
    }

    //待审核的摄像头
    public static void DSHSXTList(String scdwbh, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/sxt/audit/scdwbh/{0}", scdwbh);
        ApiAsyncHttpClient.get(url, handler);
    }

    //监控室编号查询摄像头信息
    public static void BHSXTList(String jksbh, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/sxt/audit/jksbh/{0}", jksbh);
        ApiAsyncHttpClient.get(url, handler);
    }

    //获取最新(五条)上传的监控室信息
    public static void getRecentUploadJks(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/jks/recent/user/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    //修改用户注册信息
    public static void modifyUserInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/users/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //修改用户密码
    public static void resetUserPwd(String json, AsyncHttpResponseHandler handler) {
        String url = "/userreset";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //所属网格查询已上传的监控室信息
    public static void WGJKSList(String wg, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/jks/sswg/{0}", wg);
        ApiAsyncHttpClient.get(url, handler);
    }

    //1.	按上传时间段统计街道办采集排名
    public static void JDrank(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/uploadrank/jd";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //2.	按上传时间段统计派出所采集排名
    public static void PCSrank(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/uploadrank/pcs";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //5.	统计用户每日上传数据
    public static void getUserCJCount(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/sxt/useruploadcount/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    //1.	按上传时间段统计街道办采集排名
    public static void JDUserrank(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/useruploadrank/jd";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //2.	按上传时间段统计派出所采集排名
    public static void PCSUserrank(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/useruploadrank/pcs";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //5.获得所有网格的信息数据
    public static void getWangGeList(String sqbm, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/zdxx/sswg/{0}", sqbm);
        ApiAsyncHttpClient.get(url, handler);
    }

    //采集部分汇总统计
    public static void getCountAll(AsyncHttpResponseHandler handler) {
        String url = "/sxt/countall";
        ApiAsyncHttpClient.get(url, handler);
    }

    //2.	按核查时间段统计派出所核查排名
    public static void PCSCheckrank(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/auditrank/pcs";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //2.按核查时间段、派出所统计用户核查排名
    public static void PCSUserCheckrank(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/userauditrank/pcs";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //5.	统计用户每日核查数据
    public static void getUserCheckCount(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/sxt/userauditcount/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    //根据采集单位编号、所属网格获取待审核的摄像头信息
    public static void HCSXTInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/audit/query";
        RequestParams params = new RequestParams();
        //  JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //根据采集单位编号、所属网格获取待审核的监控室信息
    public static void HCJKSInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/jks/audit/query";
        RequestParams params = new RequestParams();
        //  JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取监控室核查失败列表
    public static void postCheckFaileJKS(String json, AsyncHttpResponseHandler handler) {
        String url = "/jks/audit/failed/query?";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取摄像头核查失败列表
    public static void postCheckFaileSXT(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/audit/failed/query?";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //上传修改后的  核查不通过的监控室信息
    public static void postCheckFaileJKSInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/jks/update";
        RequestParams params = new RequestParams();
        // JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);

    }

    //上传修改后的  核查不通过的摄像头信息
    public static void postCheckFaileSXTInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/sxt/update";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 店铺全部列表
     */
    public static void StoreListALL(String json, AsyncHttpResponseHandler handler) {
        String url = "/store/list/my";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);

    }


    /***
     * 人员回访 图片上传
     **/
    public static void uploadPerImage(List<String> filePath, List<String> fileName,
                                      AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }
            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/personvisits/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 上传人员回访 资料信息
     */
    public static void addPerInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/personvisits/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 人员回访采集派出所个人排行
     */
    public static void PerUserRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/personvisits/useruploadrank/pcs";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //按上传时间段统计人员回访采集排名
    public static void PerPCSRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/personvisits/uploadrank/pcs";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //人员回访统计用户每日上传数据
    public static void PerUserCount(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/personvisits/useruploadcount/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    //查询3.0版本采集的采集总数
    public static void CountAll(AsyncHttpResponseHandler handler) {
        String url = "/xxcj/countall";
        ApiAsyncHttpClient.get(url, handler);
    }


    /***
     * 随手拍  图片上传
     **/
    public static void uploadSSPImage(List<String> filePath, List<String> fileName,
                                      AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/ssp/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 上传随手拍资料信息
     */
    public static void addSSPInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/ssp/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 随手拍 采集派出所个人排行
     */
    public static void SSPUserRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/ssp/useruploadrank/pcs";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //按上传时间段统计随手拍 采集排名
    public static void SSPPCSRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/ssp/uploadrank/pcs";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //随手拍 统计用户每日上传数据
    public static void SSPUserCount(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/ssp/useruploadcount/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    /***
     * 治安基础采集  图片上传
     **/
    public static void uploadZAJCImage(List<String> filePath, List<String> fileName,
                                       AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);

            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/zajcxx/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 上传店铺资料信息
     */
    public static void addZAJCInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/zajcxx/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 店铺采集派出所个人排行
     */
    public static void ZAJCUserRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/zajcxx/useruploadrank/pcs";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //按上传时间段统计店铺派出所采集排名
    public static void ZAJCPCSRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/zajcxx/uploadrank/pcs";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //店铺统计用户每日上传数据
    public static void ZAJCUserCount(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/zajcxx/useruploadcount/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    /***
     * 警情信息 图片上传
     **/
    public static void uploadJQCJImage(List<String> filePath, List<String> fileName,
                                       AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();
            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }
            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/jqxx/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 警情信息 资料信息
     */
    public static void addJQCJInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/jqxx/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }


    /***
     * 警情采集派出所个人排行
     */
    public static void JQCJUserRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/jqxx/useruploadrank/pcs";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //按上传时间段统计警情采集派出所采集排名
    public static void JQCJPCSRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/jqxx/uploadrank/pcs";
        RequestParams params = new RequestParams();
        //JSONObject jo = JSON.parseObject(json);
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //警情采集用户每日上传数据
    public static void JQCJUserCount(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/jqxx/useruploadcount/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }

    //获取警情编号是否重复
    public static void UpdateJQBH(String username, AsyncHttpResponseHandler handler) {
        String url = MessageFormat.format("/jqxx/isexist/{0}", username);
        ApiAsyncHttpClient.get(url, handler);
    }


    /***
     * 考勤  图片上传
     **/
    public static void uploadKQImage(List<String> filePath, List<String> fileName,
                                     AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/kq/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 上传考勤资料信息
     */
    public static void addKQInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/kq/clockInOrOut";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }


    //根据用户获取已上传的考勤列表
    public static void postKQList(String json, AsyncHttpResponseHandler handler) {
        String url = "/kq/getClockInOrOutData";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    //获取最新巡逻状态
    public static void postXLLX(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/getUserLx";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 巡逻  图片上传
     **/
    public static void uploadXLImage(List<String> filePath, List<String> fileName,
                                     AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/xl/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 上传巡逻资料信息
     */
    public static void addXLInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/submitXl";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //根据用户获取已上传的巡逻列表
    public static void postXLList(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/getXlData";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);

    }

    /***
     * 轨迹数据提交
     */
    public static void addGJInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/submitPos";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 获取岗亭信息数据
     */
    public static void getGtList(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/getGtList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取用户最新岗亭值守状态
     */
    public static void getUserGtZt(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/getUserLastGtzs";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 获取需要采集的岗亭信息数据
     */
    public static void getNoPosGtList(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/getNoPosGtList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 获取需要采集的岗亭信息数据
     */
    public static void postUpdatePlcBxUserStatus(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/submitGtzs";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 更新岗亭信息
     */
    public static void postUpdatePlcBxInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 添加岗亭信息
     */
    public static void postAddPlcBxInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }


    /***
     * 添加岗亭图片上传
     **/
    public static void uploadGTImage(List<String> filePath, List<String> fileName,
                                     AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/gt/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /***
     * 添加巡逻段
     */
    public static void addXLDInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/addXld";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 编辑巡逻段
     */
    public static void edtXLDInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/xldupdate";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 删除巡逻段
     */
    public static void delXLDInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/xlddel";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 巡逻段位信息
     */
    public static void getXLDList(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/getXldList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 添加巡逻点位信息
     */
    public static void addXLDWInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/addXldw";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 编辑巡逻点信息数据
     */
    public static void editXLDW(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/xldwupdate";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 删除巡逻点信息数据
     */
    public static void delXLDW(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/xldwdel";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取巡逻段下属全部巡逻点信息数据
     *
     * @string XLDID
     */
    public static void getXLDWList(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/getXldwList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取巡逻段下属已打卡和未打卡数据
     *
     * @string XLDID
     * @string CJYH
     * @deprecated 根据 ISXL 判断 是否打卡  0 是未打 1为打卡
     */
    public static void getXLDLastList(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/getUserLastXldData";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //根据用户获取最后一条巡逻列表
    public static void postXLLast(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/getLastXlByUser";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);

    }

    /***
     * 获取未打卡数量
     */
    public static void getXLDWSL(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/getnoxlcount";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /**
     * post lbs 添加云数据
     *
     * @param params
     * @param handler
     */
    public static void postAddLbsYunPosition(Map<String, String> params, AsyncHttpResponseHandler handler) {
        String url = "http://api.map.baidu.com/geodata/v3/poi/create";
        RequestParams requestParams = new RequestParams(params);
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("geotable_id", BaiduMapUtil.GEOTABLE_ID);
        ApiAsyncHttpClient.postDirect(url, requestParams, handler);
    }


    /**
     * 获取里程数
     *
     * @param handler
     */
    public static void getTraceHistroy(String username, long startTime, long endTime, AsyncHttpResponseHandler handler) {
        String url = "http://api.map.baidu.com/trace/v2/track/gethistory";
        RequestParams requestParams = new RequestParams();
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("service_id", BaiduMapUtil.SERVICE_ID);
        requestParams.put("simple_return", 2);
        requestParams.put("is_processed", 1);
        requestParams.put("page_index", 1);
        requestParams.put("page_size", 1);
        requestParams.put("start_time", startTime);
        requestParams.put("end_time", endTime);
        requestParams.put("entity_name", username);
        ApiAsyncHttpClient.getDirect(url, requestParams, handler);
    }

    /**
     * post lbs 更新云数据
     *
     * @param params
     * @param handler
     */
    public static void postUpdatetLbsYunPosition(Map<String, String> params, AsyncHttpResponseHandler handler) {
        String url = "http://api.map.baidu.com/geodata/v3/poi/update";
        RequestParams requestParams = new RequestParams(params);
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("geotable_id", BaiduMapUtil.GEOTABLE_ID);
        ApiAsyncHttpClient.postDirect(url, requestParams, handler);
    }


    /**
     * post lbs 按用户来查找位置信息
     *
     * @param params
     * @param handler
     */
    public static void getByUsernametLbsYunPosition(Map<String, String> params, AsyncHttpResponseHandler handler) {
        String url = "http://api.map.baidu.com/geodata/v3/poi/detail";
        RequestParams requestParams = new RequestParams(params);
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("geotable_id", BaiduMapUtil.GEOTABLE_ID);
        ApiAsyncHttpClient.getDirect(url, requestParams, handler);
    }

    /**
     * post lbs 获取周边点位数据
     *
     * @param params
     * @param handler
     */
    public static void getNearbyLbsYun(Map<String, String> params, AsyncHttpResponseHandler handler) {
        String url = "http://api.map.baidu.com/geosearch/v3/nearby";
        RequestParams requestParams = new RequestParams(params);
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("geotable_id", BaiduMapUtil.GEOTABLE_ID);
        ApiAsyncHttpClient.getDirect(url, requestParams, handler);
    }

    /**
     * post lbs 获取鹰眼轨迹entityList
     *
     * @param params
     * @param handler
     */
    public static void getYinyanEntityList(Map<String, String> params, AsyncHttpResponseHandler handler) {
        String url = "http://api.map.baidu.com/trace/v2/entity/list";
        RequestParams requestParams = new RequestParams(params);
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("service_id", BaiduMapUtil.SERVICE_ID); //该track所属的service服务的唯一标识
        ApiAsyncHttpClient.getDirect(url, requestParams, handler);
    }


    /**
     * post lbs 获取鹰眼地理围栏List
     * creator
     *
     * @param creator
     * @param handler
     */
    public static void getYinyanFenceList(String creator, AsyncHttpResponseHandler handler) {
        String url = "http://api.map.baidu.com/trace/v2/fence/list";
        RequestParams requestParams = new RequestParams("creator", creator);
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("service_id", BaiduMapUtil.SERVICE_ID); //该track所属的service服务的唯一标识
        ApiAsyncHttpClient.getDirect(url, requestParams, handler);
    }

    /**
     * post 删除地理围栏
     * fence_id 地理围栏id
     *
     * @param fence_id
     * @param handler
     */
    public static void delYinyanFence(String fence_id, AsyncHttpResponseHandler handler) {
        String url = "http://api.map.baidu.com/trace/v2/fence/delete";
        RequestParams requestParams = new RequestParams("fence_id", fence_id);
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("service_id", BaiduMapUtil.SERVICE_ID); //该track所属的service服务的唯一标识
        ApiAsyncHttpClient.postDirect(url, requestParams, handler);
    }

    public static void getlatestpoint(int service_id, String entity_name, AsyncHttpResponseHandler httpResponseHandler) {
        String url = "http://yingyan.baidu.com/api/v3/track/getlatestpoint";
        RequestParams requestParams = new RequestParams();
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("service_id", service_id);
        requestParams.put("entity_name", entity_name);
        requestParams.put("coord_type_output", "bd09ll");
        requestParams.put("process_option", "need_denoise=1,need_mapmatch=1,radius_threshold=0,transport_mode=walking");
        ApiAsyncHttpClient.getDirect(url, requestParams, httpResponseHandler);
    }

    /**
     * 创建地理围栏
     *
     * @param username
     * @param fenceName
     * @param type
     * @param vaildtime
     * @param valid_cycle
     * @param coord_type
     * @param lat
     * @param lngt
     * @param radius
     * @param httpResponseHandler
     */
    public static void createYinyanFence(String username, String fenceName, String type, String vaildtime, int valid_cycle, String coord_type, double lat, double lngt, int radius, AsyncHttpResponseHandler httpResponseHandler) {
        String url = "http://api.map.baidu.com/trace/v2/fence/create";
        Map<String, String> map = new HashMap<String, String>();
        map.put("creator", username);
        map.put("monitored_persons", username);
        map.put("observers", username);
        map.put("name", fenceName);
        map.put("desc", type);
        map.put("valid_times", vaildtime);
        map.put("valid_cycle", valid_cycle + "");
        map.put("coord_type", coord_type + "");
        map.put("center", lngt + "," + lat);
        map.put("radius", radius + "");
        map.put("shape", "1");
        RequestParams requestParams = new RequestParams(map);
        requestParams.put("ak", BaiduMapUtil.AK);
        requestParams.put("service_id", BaiduMapUtil.SERVICE_ID); //该track所属的service服务的唯一标识
        ApiAsyncHttpClient.postDirect(url, requestParams, httpResponseHandler);
    }


    /**
     * 下载PDF
     */
    public static void postPDF(String url, AsyncHttpResponseHandler httpResponseHandler) {
        ApiAsyncHttpClient.postPDF(url, httpResponseHandler);
    }

    /**
     * 治安基础信息采集 模糊查询
     *
     * @string mc 店铺名称
     */
    public static void getZASerchList(String json, AsyncHttpResponseHandler handler) {
        String url = "/zajcxx/cond/list";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取最新的巡逻状态和值守状态
     *
     * @param json
     * @param handler
     */
    public static void getLastXlGtStatus(String json, AsyncHttpResponseHandler handler) {
        String url = "/user/lastxlgtzskqzt";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //查询通讯录判断是否有权限注册
    public static void queryTXL(String json, AsyncHttpResponseHandler handler) {
        String url = "/user/txlinfo";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获得数据最后的状态
    public static void postGetlastXlType(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/userlastxltype";
        RequestParams params = new RequestParams();
        params.put("CJYH", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * string username
     * String id
     * 删除岗亭
     */
    public static void delGTInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/del";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * string username
     * String id
     * 全部派出所巡逻统计
     */
    public static void getPCSXLTJ(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/pcs/statistics";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * string username
     * String id
     * 某派出所巡逻统计
     */
    public static void getPCSXLTJXQ(String json, AsyncHttpResponseHandler handler) {
        String url = "/xl/pcs/xld/statistics";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * string username
     * String id
     * 全部派出所巡逻统计
     */
    public static void getPCSGTTJ(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/pcs/statistics";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * string username
     * String id
     * 某派出所防控点统计
     */
    public static void getPCSGTTJXQ(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/pcs/gtzsinfo/statistics";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取派出所巡长最新状态
     */
    public static void getXLXZZT(String json, AsyncHttpResponseHandler handler) {
        String url = "/xlxz/getLatestXlxz";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 上传巡长数据
     */
    public static void uploadXZInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/xlxz/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取巡长检查最新状态
     */
    public static void getXLXZJCZT(String json, AsyncHttpResponseHandler handler) {
        String url = "/xlxzjc/getLatestXlxzjc";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 上传巡长检查信息
     */
    public static void uploadXZJCInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/xlxzjc/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取巡长检查总时间
     */
    public static void getXZJCSJ(String json, AsyncHttpResponseHandler handler) {
        String url = "/xlxzjc/getTodayTotalMinutes";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 巡长统计
     */
    public static void getXZTJ(String json, AsyncHttpResponseHandler handler) {
        String url = "/xlxzjc/getTodayXlxzjcTongji";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 勤务督查  图片上传
     **/
    public static void uploadQWDCImage(List<String> filePath, List<String> fileName,
                                       AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/qwdc/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 上传勤务督查资料信息
     */
    public static void addQWDCInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/qwdc/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    //根据勤务督查历史数据信息
    public static void getQWDCHistoryData(String json, AsyncHttpResponseHandler handler) {
        String url = "/qwdc/getQwdcListByRyAndRq";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获得最近5分钟警力分布数据
    public static void getGJData(String json, AsyncHttpResponseHandler handler) {
        String url = "/gj/getGjinfoTongjiByPcsAndRq";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }




    /***
     * 获取立杆列表
     */
    public static void getRodsList(String json, AsyncHttpResponseHandler handler) {
        String url = "/rods/getRodsList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 获取立杆详情
     */
    public static void getRodsDetail(int id, AsyncHttpResponseHandler handler) {
        String url = "/rods/info/" + id;
        RequestParams params = new RequestParams();
        ApiAsyncHttpClient.get(url, params, handler);
    }


    /***
     * 获取立杆详情
     */
    public static void postUpdatePointLocation(String json, AsyncHttpResponseHandler handler) {
        String url = "/rods/rodsupdate";
        RequestParams params = new RequestParams();
        params.add("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 删除立杆镜头详情
     */
    public static void postDelPointLocationCamera(String id, AsyncHttpResponseHandler handler) {
        String url = "/rodchannel/rodchanneldelete";
        RequestParams params = new RequestParams();
        params.add("json", "{\"ID\":" + id + "}");
        ApiAsyncHttpClient.post(url, params, handler);
    }



    /***
     * 获取立杆类别列表
     */
    public static void postRodTypes(AsyncHttpResponseHandler handler) {
        String url = "/roddict/getRoddictList";
        RequestParams params = new RequestParams();
        params.add("json", "{\"PAGENUMBER\":\"1\",\"PAGESIZE\":\"100\"}");
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获取立杆详情
     */
    public static void postAddPointLocation(String json, AsyncHttpResponseHandler handler) {
        String url = "/rods/add";
        RequestParams params = new RequestParams();
        params.add("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 巡逻段位信息
     */
    public static void getDutyBeatsList(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/getDuty_beatsList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 添加巡逻段
     */
    public static void addDutyBeatsInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_beats/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 编辑巡逻段
     */
    public static void updateDutyBeatsInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/duty_beatsupdate";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 删除巡逻段
     */
    public static void delDutyBeatsInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/duty_beatsdelete";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 添加巡逻点位信息
     */
    public static void addDutyBoxInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_signbox/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /*
     * 获取巡逻段下属全部巡逻点信息数据
     */
    public static void getDutyBoxList(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/getduty_signboxList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 编辑巡逻点信息数据
     */
    public static void updateDutyBoxInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/duty_signboxupdate";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 删除巡逻点信息数据
     */
    public static void delDutyBoxInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/duty_signboxdelete";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 上传巡逻资料信息
     */
    public static void addDutyOpraation(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //根据用户获取最后一条巡逻数据
    public static void getDutyOperationData(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/getLastXlByUser";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);

    }

    //根据用户获取最后一条处警巡逻数据
    public static void getLastDutyPoliceByUser(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/getLastCjByUser";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);

    }

    //获得数据最后的状态
    public static void getLastDutyType(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/userlastxltype";
        RequestParams params = new RequestParams();
        params.put("CJYH", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 更新巡逻新
     */
    public static void updateDutyOpraation(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/Duty_operation_allupdate";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 四、	获取用户最近巡逻记录各点位是否巡逻
     *
     * @string XLDID
     * @string CJYH
     * @deprecated 根据 ISXL 判断 是否打卡  0 是未打 1为打卡
     */
    public static void getUserLastXldData(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/getUserLastXldData";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获得巡逻最后的状态
    public static void getLastDutyOperation(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/userlastxltype";
        RequestParams params = new RequestParams();
        params.put("tel_number", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /**
     * 根据用户名或巡段ID获取巡逻激励
     */
    public static void getXlList(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/getXlList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /**
     * 根据巡逻ID查询巡逻详情
     */
    public static void getDutyHistory(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/getXlData";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);

    }

    /***
     * 获取最新的巡逻、值守、考勤状态
     *
     * @param json
     * @param handler
     */
    public static void getLastDutyOrPlcBxOrKqToType(String json, AsyncHttpResponseHandler handler) {
        String url = "/user/duty_operation_all/lastxlgtzskqzt";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * string username
     * String id
     * 全部派出所巡逻统计
     */
    public static void getDutyPcsTotal(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/pcs/statistics";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * string username
     * String id
     * 全部派出所巡逻统计 统计里程数
     */
    public static void getDutyPcsTotalSecond(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/pcs/statistics/new";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * string kssj
     * String jssj
     * 全部派出所巡逻统计 统计里程数
     */
    public static void getDutyPcsDistance(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/pcs/distanceAndXlrsstatistics";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * string username
     * String id
     * 某派出所巡逻统计详情
     */
    public static void getDutyPCSDetailsXQ(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/pcs/xld/statistics";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * string username
     * String id
     * 某派出所巡逻统计公里数详情
     */
    public static void getDutyPCSDetailsXQNew(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/pcs/xld/statistics/new";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 排班规则
     */
    public static void addDutyFlightRules(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_flight_rules/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 编辑排班规则
     */
    public static void edtDutyFlightRules(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/Duty_flight_rulesupdate";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 删除排班规则
     */
    public static void delDutyFlightRules(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/duty_flight_rulesdelete";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 添加勤务班次
     */
    public static void addDutyFlight(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_flightsResource/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 编辑排班班次
     */
    public static void edtDutyFlight(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/duty_flightsResourceupdate";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 删除勤务班次
     */
    public static void delDutyFlight(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/duty_flightsdelete";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 十六、	分页获取排班规则信息
     */
    public static void getDutyFlightReulsList(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/getDuty_flight_rulesList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 十七、	分页获取勤务班次信息
     */
    public static void getDutyFligtsList(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty/getDuty_flightsList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 九、	获取用户的出警数据
     */
    public static void getPoliceHitory(String json, AsyncHttpResponseHandler handler) {
        String url = "/duty_operation_all/getCjListByUser";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 我是巡长七天的记录
     */
    public static void getDutyLeaderHistoryForSereven(String json, AsyncHttpResponseHandler handler) {
        String url = "/xlxzjc/getLastSevenDayData";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 根据日期查询 我是巡长记录
     */
    public static void getDutyLeaderHistory(String json, AsyncHttpResponseHandler handler) {
        String url = "/xlxzjc/getSomedayList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /**
     * 根据用户名或岗亭ID获取值守记录
     */
    public static void getGtzsList(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/getGtzsList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /**
     * 根据值守id获取值守岗亭值守详情
     */
    public static void getGtData(String json, AsyncHttpResponseHandler handler) {
        String url = "/gt/getGtData";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 出租屋  图片上传
     **/
    public static void uploadCZWImage(List<String> filePath, List<String> fileName,
                                      AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/czw/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 上传 出租屋采集资料信息
     */
    public static void addCZWInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/czw/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 更新 出租屋采集资料信息
     */
    public static void updateCZWInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/czw/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 出租屋执法  图片上传
     **/
    public static void uploadCZWZFImage(List<String> filePath, List<String> fileName,
                                        AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/czwzf/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 出租屋执法 资料信息
     */
    public static void addCZWZFInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/czwzf/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 更新出租屋执法 资料信息
     */
    public static void upCZWZFInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/czwzf/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 根据楼栋ID获取出租屋数据
     */
    public static void getCZWList(String json, AsyncHttpResponseHandler handler) {
        String url = "/czw/getCzwcjxxList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 根据出租屋ID获取出租屋执法数据
     */
    public static void getCZWZFList(String json, AsyncHttpResponseHandler handler) {
        String url = "/czwzf/getCzwzfxxList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 根据月份 身份证号码 查询队员薪资信息
     */
    public static void getSalaryTotal(String json, AsyncHttpResponseHandler handler) {
        String url = "/wagetotal/getWagetotal";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }
    /***
     * 根据月份 身份证号码 查询队员2017年4月份后的薪资信息
     */
    public static void getSalaryNewTotal(String json, AsyncHttpResponseHandler handler) {
        String url = "/wagesDetail/getWagesDetail";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //出租屋采集用户每日上传数据
    public static void HouseUserCount(String json, AsyncHttpResponseHandler handler) {
        String url = "/czw/useruploadcount";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //按上传时间段统计出租屋采集排名
    public static void HousePCSRank(String json, AsyncHttpResponseHandler handler) {
        String url = "/czw/getCzwTongjiByRiqi";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //根据派出所编码 出租屋采集统计详情
    public static void HousePCSRankDetails(String json, AsyncHttpResponseHandler handler) {
        String url = "/czw/getCzwTongjiByPcsbm";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房管理主界面
    public static void MachineRoomMain(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroom/getMachineroomTongji";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //根据派出所查询机房详情
    public static void MachineRoomPcsMain(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroom/getMachineRoomByPcsbmTongji";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房添加
    public static void MachineRoomAdd(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroom/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房更新
    public static void MachineRoomUpdate(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroom/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房删除
    public static void MachineRoomDelete(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroom/delete";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    // //机房管理房列表
    public static void MachineRoomPersonList(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomperson/getMachineroomPersonList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房管理房添加
    public static void MachineRoomPersonAdd(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomperson/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房管理房删除
    public static void MachineRoomPersonDel(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomperson/delete";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房管理房编辑
    public static void MachineRoomPersonUpdate(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomperson/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //安全管理问题
    public static void SaftyQuestion(String json, AsyncHttpResponseHandler handler) {
        String url = "/saftyquestion/getSaftyQuestionList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //安全管理问题随机10道
    public static void SaftyQuestionRandom(String json, AsyncHttpResponseHandler handler) {
        String url = "/saftyquestion/getRandomTenQuestionList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //安全管理问题
    public static void SaftyQuestionList(String json, AsyncHttpResponseHandler handler) {
        String url = "/saftyquestion/getSaftyQuestionList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    //考核成绩上传
    public static void SaftyExamineAdd(String json, AsyncHttpResponseHandler handler) {
        String url = "/saftyExamine/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //考核成绩查询
    public static void SaftyExamineQuery(String json, AsyncHttpResponseHandler handler) {
        String url = "/saftyExamine/getSaftyExamineList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房审核添加
    public static void MachineCheckAdd(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomCheck/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    //机房审核更新
    public static void MachineCheckUpdate(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomCheck/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    //机房施工申请最新数据
    public static void MachineApproveLast(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomCheck/getMachineroomLastestSq";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //机房施工申请未过期的数据
    public static void MachineApproveAll(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomCheck/getAllMachineroomCheckBySjhm";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    //查询机房进入最新记录
    public static void MachineRoomEnterLast(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomEnter/getMachineroomEnterList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 进入机房  图片上传
     **/
    public static void upMachineRoomEnter(List<String> filePath, List<String> fileName,
                                          AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/machineroomEnter/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /***
     * 进入机房 资料信息上传
     */
    public static void MachineRoomEnterAdd(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomEnter/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 进入机房 资料信息更新
     */
    public static void MachineRoomEnterUpdate(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomEnter/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 管理员获取机房申请列表
     * * @string kssj 可传
     * * @string jssj可传
     */
    public static void MachineRoomCheckList(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomCheck/getMachineroomCheckList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 管理员获取机房申请列表
     *
     * @string kssj 可传
     * * @string jssj可传
     */
    public static void MachineRoomPersonLists(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomCheck/getMachineroomCheckListByJfgly";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 管理员施工申请
     */
    public static void MachineRoomCheckUpdate(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomCheck/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    //机房根据主人员查询 跟随人员的信息
    public static void MachineRoomFellowList(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomFellow/getMachineroomFellowList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取机房单位
    public static void getAllMachineroomDept(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroom/getAllMachineroomDept";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 机房跟随人员 图片上传
     **/
    public static void upMachineRoomFellow(List<String> filePath, List<String> fileName,
                                           AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/machineroomFellow/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //机房跟随人员的信息上传
    public static void MachineRoomFellowAdd(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomFellow/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    //根据机房类别查询 审核领导
    public static void getUserByJflb(String json, AsyncHttpResponseHandler handler) {
        String url = "/user/getUserlistByJflb";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 机房同行人员更新
     */
    public static void MachineRoomFellow(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomFellow/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 机房同行人员删除
     */
    public static void MachineRoomFellowDel(String json, AsyncHttpResponseHandler handler) {
        String url = "/machineroomFellow/delete";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 用户信息上传 图片上传
     **/
    public static void userUpload(List<String> filePath, List<String> fileName,
                                  AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/user/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //修改用户注册信息
    public static void updateUserInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/users/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    //获取提审室预约时间
    public static void getTssyyrqList(String json, AsyncHttpResponseHandler handler) {
        String url = "/Tssyyrq/getTssyyrqList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取提审室信息
    public static void getTssList(String json, AsyncHttpResponseHandler handler) {
        String url = "/tss/getTssList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取提审室信息
    public static void addTssyy(String json, AsyncHttpResponseHandler handler) {
        String url = "/tssyy/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取提审预约记录
    public static void getTssyyListByUser(String json, AsyncHttpResponseHandler handler) {
        String url = "/tssyy/getTssyyListByUser";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //更新提审预约记录
    public static void updateTssyy(String json, AsyncHttpResponseHandler handler) {
        String url = "/tssyy/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取提审预约记录
    public static void getTsjlList(String json, AsyncHttpResponseHandler handler) {
        String url = "/tsjl/getTsjlList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取提审排队记录
    public static void getTsspdList(String json, AsyncHttpResponseHandler handler) {
        String url = "/tsspd/getTsspdList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //获取用户提审排队记录
    public static void getLatestTsspdByUser(String json, AsyncHttpResponseHandler handler) {
        String url = "/tsspd/getLatestTsspdByUser";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //更新用户提审排队记录
    public static void tsspdUpdate(String json, AsyncHttpResponseHandler handler) {
        String url = "/tsspd/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 上传 寄递物流核查采集资料信息
     */
    public static void addJSWLCheck(String json, AsyncHttpResponseHandler handler) {
        String url = "/zajcxxJswlhc/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 获得 寄递物流核查采集资料信息
     */
    public static void getJSWLCheckList(String json, AsyncHttpResponseHandler handler) {
        String url = "/zajcxxJswlhc/getZajcxxJswlhcList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 巡逻信息纠偏后的点位保存
     */
    public static void postXljpNew(String json, AsyncHttpResponseHandler handler) {
        String url = "/gjinfoNew/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    public static void getUserInfosByPcs(String json, AsyncHttpResponseHandler handler) {
        String url = "/user/getUserlistByQuery";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    public static void KPIQuestion(String json, AsyncHttpResponseHandler handler) {
        String url = "/kpiquestion/getRandomTenQuestionList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }



    //kpi考核成绩上传
    public static void KpiExamineAdd(String json, AsyncHttpResponseHandler handler) {
        String url = "/kpiExamine/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    //kpi考核成绩查询
    public static void KpiExamineQuery(String json, AsyncHttpResponseHandler handler) {
        String url = "/kpiExamine/getKpiExamineList";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     *上传Kpi备案图片
     **/
    public static void KpiRecordImage(List<String> filePath, List<String> fileName,
                                      AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/kpiRecord/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /***
     * 上传Kpi备案信息
     */
    public static void AddKpiInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/kpiRecord/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }
    /***
     * 更新Kpi备案信息
     */
    public static void updateKpiInfo(String json, AsyncHttpResponseHandler handler) {
        String url = "/kpiRecord/update";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

    /***
     * 更新 身份证号码
     */
    public static void  updateSfzhm(String json, AsyncHttpResponseHandler handler) {
        String url = "/user/getWagePerson";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }

    /***
     * 查询KPI备案记录
     */
    public static void  getKpiRecordList(String json, AsyncHttpResponseHandler handler) {
        String url = "/kpiRecord/getKpiRecordListByQuery";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiAsyncHttpClient.post(url, params, handler);
    }


    /***
     * 出入申请 图片上传
     **/
    public static void upCrzsq(List<String> filePath, List<String> fileName,
                               AsyncHttpResponseHandler handler) {
        try {
            String contentType = RequestParams.APPLICATION_OCTET_STREAM;
            RequestParams params = new RequestParams();

            for (int i = 0; i < filePath.size(); i++) {
                File file = new File(filePath.get(i));
                params.put(fileName.get(i), file, contentType);
            }

            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            String url = "/crzsq/upload";
            ApiSyncHttpClient.post(url, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //出入申请信息上传
    public static void CrzsqAdd(String json, AsyncHttpResponseHandler handler) {
        String url = "/crzsq/add";
        RequestParams params = new RequestParams();
        params.put("json", json);
        ApiSyncHttpClient.post(url, params, handler);
    }

}
