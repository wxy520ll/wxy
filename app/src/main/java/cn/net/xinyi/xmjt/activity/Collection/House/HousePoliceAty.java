package cn.net.xinyi.xmjt.activity.Collection.House;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.AppManager;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.HouseCheckModle;
import cn.net.xinyi.xmjt.model.HousePoliceModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.UI;
import cn.net.xinyi.xmjt.utils.zxing.activity.CaptureActivity;

public class HousePoliceAty extends BaseActivity2 implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {
    public static Context instance;
    //执法办法 办案地址
    @BindView(id = R.id.ll_badz)
    private LinearLayout ll_badz;
    //执法办法 处罚信息
    @BindView(id = R.id.ll_cfxx)
    private LinearLayout ll_cfxx;
    //执法办法 停租套间信息
    @BindView(id = R.id.ll_tztj)
    private LinearLayout ll_tztj;

    /**
     * 是否停租办案地址
     **/
    @BindView(id = R.id.rg_tzbadz)
    private RadioGroup rg_tzbadz;
    @BindView(id = R.id.rb_tzbadz_0)
    private RadioButton rb_tzbadz_0;
    @BindView(id = R.id.rb_tzbadz_1)
    private RadioButton rb_tzbadz_1;
    int i_tzbadz = 3;
    /**
     * 是否居住证办案地址
     **/
    @BindView(id = R.id.rg_jzzbadz)
    private RadioGroup rg_jzzbadz;
    @BindView(id = R.id.rb_jzzbadz_0)
    private RadioButton rb_jzzbadz_0;
    @BindView(id = R.id.rb_jzzbadz_1)
    private RadioButton rb_jzzbadz_1;
    int i_jzzbadz = 3;
    /**
     * 是否停租楼栋
     **/
    @BindView(id = R.id.rg_tzzdl)
    private RadioGroup rg_tzzdl;
    @BindView(id = R.id.rb_tzzdl_0)
    private RadioButton rb_tzzdl_0;
    @BindView(id = R.id.rb_tzzdl_1)
    private RadioButton rb_tzzdl_1;
    int i_tzzdl = 3;
    /**
     * 是否停租套间
     **/
    @BindView(id = R.id.rg_tztj)
    private RadioGroup rg_tztj;
    @BindView(id = R.id.rb_tztj_0)
    private RadioButton rb_tztj_0;
    @BindView(id = R.id.rb_tztj_1)
    private RadioButton rb_tztj_1;
    int i_tztj = 3;
    /**
     * 套间房号
     **/
    @BindView(id = R.id.et_tjfh)
    private EditText et_tjfh;
    /**
     * 是否人员处罚
     **/
    @BindView(id = R.id.rg_rycf)
    private RadioGroup rg_rycf;
    @BindView(id = R.id.rb_rycf_0)
    private RadioButton rb_rycf_0;
    @BindView(id = R.id.rb_rycf_1)
    private RadioButton rb_rycf_1;
    int i_rycf = 3;
    /**
     * 是否居住执法
     **/
    @BindView(id = R.id.rg_jzzf)
    private RadioGroup rg_jzzf;
    @BindView(id = R.id.rb_jzzf_0)
    private RadioButton rb_jzzf_0;
    @BindView(id = R.id.rb_jzzf_1)
    private RadioButton rb_jzzf_1;
    int i_jzzf = 3;
    /**
     * 处罚对象
     **/
    @BindView(id = R.id.tv_cfdx,click = true)
    private TextView tv_cfdx;
    /**
     * 处罚信息
     **/
    @BindView(id = R.id.tv_cfxx,click = true)
    private TextView tv_cfxx;
    /**
     * 处罚对象名字
     **/
    @BindView(id = R.id.et_cfdxmz)
    private EditText et_cfdxmz;
    /**
     * 处罚对象身份证号码
     **/
    @BindView(id = R.id.et_cfdxsfz)
    private EditText et_cfdxsfz;
    /**
     * 上传
     **/
    @BindView(id = R.id.btn_upl,click = true)
    private Button btn_upl;
    /**
     * 解封
     **/
    @BindView(id = R.id.btn_jf,click = true)
    private Button btn_jf;
    /**
     * 更新
     **/
    @BindView(id = R.id.btn_update,click = true)
    private Button btn_update;
    /**
     * 封条图片
     **/
    @BindView(id = R.id.iv_ft,click = true)
    private ImageView iv_ft;
    /***ImageView拍照标识*/
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private HouseCheckModle houseCheckModle;
    private int uploadCount=0;
    private HousePoliceModle policeModle;
    private List<HousePoliceModle> housePoliceModles;
    private HousePoliceModle poInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_house_zfba);
        AnnotateManager.initBindView(this);//注解式绑定控件
        instance=this;
        TypeUtils.compatibleWithJavaBean = true;
        houseCheckModle= (HouseCheckModle) getIntent().getSerializableExtra("houseCheckModle");
        initView();
    }

    private void getData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("CZWXXCJ_ID",houseCheckModle.getID());
        String jo= requestJson.toJSONString();
        ApiResource.getCZWZFList(jo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if (i==200&&result.length()>2){
                    housePoliceModles = JSON.parseArray(result, HousePoliceModle.class);
                    if (housePoliceModles!=null&&housePoliceModles.size()>0){
                        initData();
                    }
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }

    private void initData() {
        poInfo=housePoliceModles.get(0);
        BaseDataUtils.initRBCheck(poInfo.getSFTZBADZ(),rb_tzbadz_0,rb_tzbadz_1,i_tzbadz);
        BaseDataUtils.initRBCheck(poInfo.getSFJZZBADZ(),rb_jzzbadz_0,rb_jzzbadz_1,i_jzzbadz);
        if (poInfo.getSFTZBADZ()==0){
            setZFBAView(true,true,false);
            BaseDataUtils.initRBCheck(poInfo.getSFTZZDL(),rb_tzzdl_0,rb_tzzdl_1,i_tzzdl);
            if (poInfo.getSFTZZDL()==1){
                setZFBAView(true,true,true);
                BaseDataUtils.initRBCheck(poInfo.getSFTZTJ(),rb_tztj_0,rb_tztj_1,i_tztj);
                et_tjfh.setText(poInfo.getTZTJFH());
            }
            path1="j"+poInfo.getPSFTTP();
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czwzf/" +poInfo.getPSFTTP(), iv_ft);
            BaseDataUtils.initRBCheck(poInfo.getSFDRYCF(),rb_rycf_0,rb_rycf_1,i_rycf);
            BaseDataUtils.initRBCheck(poInfo.getSFDRYZJZZF(),rb_jzzf_0,rb_jzzf_1,i_jzzf);
        }
        if (poInfo.getSFTZBADZ()==0||poInfo.getSFJZZBADZ()==0){
            btn_jf.setVisibility(View.VISIBLE);
            if (poInfo.getSFTZBADZ()!=0){
                setZFBAView(false,true,false);
            }
            tv_cfdx.setText(HousePoliceModle.getCFDXType(poInfo.getCFRYLX()));
            tv_cfxx.setText(HousePoliceModle.getCFXXType(poInfo.getCFXX()));
            et_cfdxmz.setText(poInfo.getXM());
            et_cfdxsfz.setText(poInfo.getSFZH());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_ft:
                i_flag = 1;
                cameraPhoto();
                break;

            case R.id.tv_cfdx:
                BaseDataUtils.showAlertDialog(HousePoliceAty.this, HousePoliceModle.sCJDX, tv_cfdx);
                break;

            case R.id.tv_cfxx:
                BaseDataUtils.showAlertDialog(HousePoliceAty.this, HousePoliceModle.sCFXX, tv_cfxx);
                break;

            case R.id.btn_jf:
                Intent intent=new Intent(this,HouseJfActivity.class);
                intent.putExtra("poInfo",poInfo);
                startActivityForResult(intent,1001);
                break;

            case R.id.btn_update:
                if (uplMesTips()){
                    startUploadThread(houseCheckModle); //开始更新上传信息
                }
                break;
            case R.id.btn_upl:
                if (uplMesTips()){
                    startUploadThread(houseCheckModle); //开始上传采集信息
                }
                break;
        }
    }

    private boolean uplMesTips() {
        String msg="";
        boolean u_Flag=false;
        if (i_tzbadz==3){
            msg="请选择是否停租办案地址";
        }else if (i_jzzbadz==3){
            msg="请选择是否居住证办案地址";
        }else if (i_tzbadz==0&&i_tzzdl==3){
            msg="请选择是否整栋楼停租";
        }else if (i_tzbadz==0&&i_tzzdl==1&&i_tztj==3){
            msg="请选择是否停租套间";
        }else if (i_tzbadz==0&&i_tzzdl==1&&et_tjfh.getText().toString().isEmpty()){
            msg="请输入停租套间房号";
        }else if (i_tzbadz==0&&i_rycf==3){
            msg="请选择是否对人员信息做处罚";
        }else if (i_tzbadz==0&&i_jzzf==3) {
            msg = "请选择是否对人员信息做居住执法";
        }else if (i_tzbadz==0&&null==path1){
            msg = "封条图片不能为空";
        }else if ((i_tzbadz==0||i_jzzbadz==0)&&tv_cfdx.getText().toString().isEmpty()){
            msg="请选择处罚对象";
        }else if ((i_tzbadz==0||i_jzzbadz==0)&&tv_cfxx.getText().toString().isEmpty()){
            msg="请选择处罚信息";
        }else if ((i_tzbadz==0||i_jzzbadz==0)&&et_cfdxmz.getText().toString().isEmpty()){
            msg="请输入处罚对象名字";
        }else if ((i_tzbadz==0||i_jzzbadz==0)&&(et_cfdxsfz.getText().toString().isEmpty()||et_cfdxsfz.getText().toString().length()<15)){
            msg="请输入正确的处罚对象身份证号码";
        }
        if (((AppContext) getApplication()).getNetworkType() == 0) {
            UI.toast(this,getString(R.string.network_not_connected));
            return false;
        } else if (!msg.isEmpty()){
            UI.toast(this,msg);
            return false;
        }else {
            policeModle=new HousePoliceModle();
            policeModle.setSFTZBADZ(i_tzbadz);
            policeModle.setLDID(houseCheckModle.getLDID());
            policeModle.setSFJZZBADZ(i_jzzbadz);
            if (i_tzbadz==0){
                policeModle.setSFTZZDL(i_tzzdl);
                if (i_tzzdl==1){
                    policeModle.setSFTZTJ(i_tztj);
                    policeModle.setTZTJFH(et_tjfh.getText().toString());
                }
                policeModle.setPSFTTP(path1);
                policeModle.setSFDRYCF(i_rycf);
                policeModle.setSFDRYZJZZF(i_jzzf);
            }
            if (i_tzbadz==0||i_jzzbadz==0){
                policeModle.setCFRYLX(HousePoliceModle.getCFDXTypeNum(tv_cfdx.getText().toString()));
                policeModle.setCFXX(HousePoliceModle.getCFXXTypeNum(tv_cfxx.getText().toString()));
                policeModle.setXM(et_cfdxmz.getText().toString());
                policeModle.setSFZH(et_cfdxsfz.getText().toString());
            }
            u_Flag=true;
        }
        return u_Flag;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id. rg_tztj:
                i_tztj = new BaseDataUtils().getRGNum(group, R.id.rb_tztj_0);
                break;

            case R.id. rg_rycf:
                i_rycf = new BaseDataUtils().getRGNum(group, R.id.rb_rycf_0);
                break;

            case R.id. rg_jzzf:
                i_jzzf = new BaseDataUtils().getRGNum(group, R.id.rb_jzzf_0);
                break;

            case R.id.rg_tzbadz:
                if (checkedId == R.id.rb_tzbadz_0) {
                    i_tzbadz=0;
                    setZFBAView(true,true,false);
                } else if (checkedId == R.id.rb_tzbadz_1) {
                    i_tzbadz=1;
                    if (i_jzzbadz==0){
                        setZFBAView(false,true,false);
                    }else {
                        setZFBAView(false,false,false);
                    }
                }
                break;

            case R.id.rg_jzzbadz:
                if (checkedId == R.id.rb_jzzbadz_0) {
                    i_jzzbadz=0;
                    if (i_tzbadz!=0){
                        setZFBAView(false,true,false);
                    }
                } else if (checkedId == R.id.rb_jzzbadz_1) {
                    i_jzzbadz=1;
                    if (i_tzbadz!=0){
                        setZFBAView(false,false,false);
                    }
                }
                break;
            case R.id.rg_tzzdl:
                if (checkedId == R.id.rb_tzzdl_0) {
                    i_tzzdl=0;
                    setZFBAView(true,true,false);
                } else if (checkedId == R.id.rb_tzzdl_1) {
                    i_tzzdl=1;
                    setZFBAView(true,true,true);
                }
                break;
        }
    }

    private void initView() {
        if (getIntent().getFlags()==1) {
            getData();
            btn_upl.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);
        }
        rg_tzbadz.setOnCheckedChangeListener(this);
        rg_jzzbadz.setOnCheckedChangeListener(this);
        rg_tzzdl.setOnCheckedChangeListener(this);
        rg_tzbadz.setOnCheckedChangeListener(this);
        rg_jzzbadz.setOnCheckedChangeListener(this);
        rg_rycf.setOnCheckedChangeListener(this);
        rg_tztj.setOnCheckedChangeListener(this);
        rg_jzzf.setOnCheckedChangeListener(this);

    }

    //执法办案界面view
    private void setZFBAView(boolean b1, boolean b2, boolean b3) {
        ll_badz.setVisibility(b1 == true ? View.VISIBLE : View.GONE);
        ll_cfxx.setVisibility(b2 == true ? View.VISIBLE : View.GONE);
        ll_tztj.setVisibility(b3 == true ? View.VISIBLE : View.GONE);
    }

    /**
     *返回拍照
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0 && imagePath != null) {
            if (i_flag == 1) {
                path1 = imagePath;
                iv_ft.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        }else if (requestCode==1001&&resultCode==RESULT_OK){
            getData();
        }else if (null != data && requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String code = bundle.getString("result");
            if (code != null) {
                Intent intent=new Intent(this, HouseCheckActivity.class);
                intent.putExtra("ldbh",code.substring(code.indexOf("=") + 1).trim());
                startActivity(intent);
                HousePoliceAty.this.finish();
            } else {
                UI.toast(this, "楼栋二维码识别错误!");
            }
        }
    }


    private void startUploadThread(final HouseCheckModle Info) {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadImage(Info);
                int result = uploadCount;
                if (result > 0) {
                    msg = new Message();
                    msg.what = 2;
                    msg.arg1 = result;
                    handler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //同步上传图片到服务端
    void uploadImage(final HouseCheckModle mInfo) {
        if (mInfo.getRFIMG() != null&&!mInfo.getRFIMG().startsWith("j")) {
            String path = mInfo.getRFIMG();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }

        if (mInfo.getWFIMG() != null&&!mInfo.getWFIMG().startsWith("j")) {
            String path = mInfo.getWFIMG();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }

        if (mInfo.getXFIMG() != null&&!mInfo.getXFIMG().startsWith("j")) {
            String path = mInfo.getXFIMG();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }
        if (mInfo.getJFIMG() != null&&!mInfo.getJFIMG().startsWith("j")) {
            String path = mInfo.getJFIMG();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }
        if (filePath.size()>0&&fileName.size()>0){
            //上传图片
            ApiResource.uploadCZWImage(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        if(getIntent().getFlags()==1){
                            updateInfo(mInfo);
                        }else {
                            uploadInfo(mInfo);
                        }
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                }

                @Override
                public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                    if (bytes != null) {
                        String result = new String(bytes);
                    }
                }
            });
        }else {
            if(getIntent().getFlags()==1){
                updateInfo(mInfo);
            }else {
                uploadInfo(mInfo);
            }
        }
    }


    //同步上传采集数据到服务端
    public void uploadInfo(final HouseCheckModle info) {
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("ID");
            jo.remove("RFIMG");
            jo.remove("WFIMG");
            jo.remove("XFIMG");
            jo.remove("JFIMG");
            jo.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
            jo.put("SSPCS", AppContext.instance.getLoginInfo().getOrgancode());
            if (null!=info.getWFIMG()){
                jo.put("WFIMG",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(info.getWFIMG()));
            }
            if (null!=info.getJFIMG()){
                jo.put("JFIMG",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(info.getJFIMG()));
            }
            if (null!=info.getRFIMG()){
                jo.put("RFIMG",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(info.getRFIMG()));
            }
            if (null!=info.getXFIMG()){
                jo.put("XFIMG",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(info.getXFIMG()));
            }
        }
        String json = jo.toJSONString();
        ApiResource.addCZWInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                JSONObject jo= JSON.parseObject(result);
                int ID= Integer.parseInt(jo.get("ID").toString());
                if (!result.isEmpty() && ID>0) {
                    policeModle.setCZWXXCJ_ID(ID);
                    uploadImage();
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                }
            }
        });
    }

    //同步上传图片到服务端
    void uploadImage() {
        if (null!=policeModle.getPSFTTP()&&!policeModle.getPSFTTP().startsWith("j")) {
            String path = policeModle.getPSFTTP();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
            //上传图片
            ApiResource.uploadCZWZFImage(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        if (getIntent().getFlags()==1){
                            updatePoliceInfo();
                        }else {
                            uploadPoliceInfo();
                        }
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                }
                @Override
                public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                    if (bytes != null) {
                        String result = new String(bytes);
                    }
                }
            });
        }else {
            if (getIntent().getFlags()==1){
                updatePoliceInfo();
            }else {
                uploadPoliceInfo();
            }
        }
    }


    //同步上传采集数据到服务端
    public void uploadPoliceInfo() {
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(policeModle));
        jo.remove("ID");
        jo.remove("PSFTTP");
        if (null!=policeModle.getPSFTTP()) {
            jo.put("PSFTTP",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(policeModle.getPSFTTP()));
        }
        String json = jo.toJSONString();
        ApiResource.addCZWZFInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                JSONObject jo= JSON.parseObject(result);
                int ID= Integer.parseInt(jo.get("ID").toString());
                if (!result.isEmpty() && ID>0) {
                    uploadCount++;
                    if (null!=policeModle.getPSFTTP()) {
                        File plateImage = new File(policeModle.getPSFTTP());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (houseCheckModle.getWFIMG() != null) {
                        File plateImage = new File(houseCheckModle.getWFIMG());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }

                    if (houseCheckModle.getJFIMG() != null) {
                        File plateImage = new File(houseCheckModle.getJFIMG());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (houseCheckModle.getXFIMG() != null) {
                        File plateImage = new File(houseCheckModle.getXFIMG());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (houseCheckModle.getRFIMG() != null) {
                        File plateImage = new File(houseCheckModle.getRFIMG());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                }
            }
        });
    }


    //同步更新采集数据到服务端
    public void updateInfo(final HouseCheckModle info) {
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("RFIMG");
            jo.remove("WFIMG");
            jo.remove("XFIMG");
            jo.remove("JFIMG");
            jo.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
            jo.put("SSPCS", AppContext.instance.getLoginInfo().getOrgancode());
            if (null!=info.getWFIMG()&&info.getWFIMG().startsWith("j")) {
                jo.put("WFIMG",info.getWFIMG().substring(1));
            } else if (null!=info.getWFIMG()){
                jo.put("WFIMG",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(info.getWFIMG()));
            }
            if (null!=info.getJFIMG()&&info.getJFIMG().startsWith("j")) {
                jo.put("JFIMG",info.getJFIMG().substring(1));
            } else if (null!=info.getJFIMG()){
                jo.put("JFIMG",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(info.getJFIMG()));
            }
            if (null!=info.getRFIMG()&&info.getRFIMG().startsWith("j")) {
                jo.put("RFIMG",info.getRFIMG().substring(1));
            } else if (null!=info.getRFIMG()){
                jo.put("RFIMG",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(info.getRFIMG()));
            }
            if (null!=info.getXFIMG()&&info.getXFIMG().startsWith("j")) {
                jo.put("XFIMG",info.getXFIMG().substring(1));
            } else if (null!=info.getXFIMG()){
                jo.put("XFIMG",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(info.getXFIMG()));
            }
        }
        String json = jo.toJSONString();
        ApiResource.updateCZWInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result.startsWith("true")) {
                    uploadImage();
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                }
            }
        });
    }

    //同步更新采集数据到服务端
    public void  updatePoliceInfo(){
        policeModle.setID(poInfo.getID());
        policeModle.setCZWXXCJ_ID(houseCheckModle.getID());
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(policeModle));
        if (jo != null) {
            jo.remove("PSFTTP");
            if (null!=policeModle.getPSFTTP()&&policeModle.getPSFTTP().startsWith("j")) {
                jo.put("PSFTTP",policeModle.getPSFTTP().substring(1));
            } else if (null!=policeModle.getPSFTTP()){
                jo.put("PSFTTP",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(policeModle.getPSFTTP()));
            }
        }
        String json = jo.toJSONString();
        ApiResource.upCZWZFInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result.startsWith("true")) {
                    uploadCount++;
                    if (null!=policeModle.getPSFTTP()) {
                        File plateImage = new File(policeModle.getPSFTTP());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (houseCheckModle.getWFIMG() != null) {
                        File plateImage = new File(houseCheckModle.getWFIMG());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (houseCheckModle.getJFIMG() != null) {
                        File plateImage = new File(houseCheckModle.getJFIMG());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (houseCheckModle.getXFIMG() != null) {
                        File plateImage = new File(houseCheckModle.getXFIMG());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (houseCheckModle.getRFIMG() != null) {
                        File plateImage = new File(houseCheckModle.getRFIMG());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                }
            }
        });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding("正在上传数据",HousePoliceAty.this);
                    break;

                case 2:// 上传之后
                    stopLoading();
//                    DialogHelper.showAlertDialog(HousePoliceAty.this, "上传成功!", new DialogHelper.OnOptionClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which, Object o) {
//                            AppManager.getAppManager().finishActivity(HouseCheckActivity.class);
//                            HousePoliceAty.this.finish();
//                        }
//                    });
                    new AlertDialog.Builder(HousePoliceAty.this).setTitle(R.string.tips)
                            .setMessage("上传成功!")
                            .setNegativeButton ("返回",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            AppManager.getAppManager().finishActivity(HouseCheckActivity.class);
                                            HousePoliceAty.this.finish();
                                        }
                                    })
                            .setPositiveButton("继续采集",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            AppManager.getAppManager().finishActivity(HouseCheckActivity.class);
                                            Intent intent = new Intent(HousePoliceAty.this, CaptureActivity.class);
                                            startActivityForResult(intent, 100);
                                        }
                                    }).setCancelable(false).show();
                    break;

                case 3:// 上传失败
                    stopLoading();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", HousePoliceAty.this);
                    break;
            }
        }
    };


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.house_police);
    }

}
