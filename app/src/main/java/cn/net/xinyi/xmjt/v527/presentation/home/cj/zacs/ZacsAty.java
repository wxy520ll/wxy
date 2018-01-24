package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCCYZAdp;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCCYZAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCCYZWatchAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCJKAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCJKWatchAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCLocalAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCXFInfoAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCXFWatchAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseWithLocActivity;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.Presenter.ZAJCPresenter;
import cn.net.xinyi.xmjt.model.View.IZAJCView;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.CollectDBUtils;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;


/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZacsAty extends BaseWithLocActivity implements IZAJCView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    /**
     * 采集分类
     **/
    @BindView(id = R.id.tv_type)
    private TextView tv_type;
    /**
     * 名称
     **/
    @BindView(id = R.id.et_mc)
    private EditText et_mc;
    /**
     * 地址
     **/
    @BindView(id = R.id.et_dz, click = true)
    private EditText et_dz;
    /**
     * 地址
     **/
    @BindView(id = R.id.et_mph, click = true)
    private EditText et_mph;
    /**
     * 坐标
     **/
    @BindView(id = R.id.tv_zb)
    private TextView tv_zb;
    /**
     * 手动定位
     **/
    @BindView(id = R.id.tv_sddw, click = true)
    private TextView tv_sddw;
    /**
     * 手动定位
     **/
    @BindView(id = R.id.tv_sxwz, click = true)
    private TextView tv_sxwz;
    /**
     * 楼栋编码
     **/
    @BindView(id = R.id.et_ldbm)
    private AutoCompleteTextView et_ldbm;
    /**
     * 门牌号照片
     **/
    @BindView(id = R.id.iv_mph, click = true)
    private ImageView iv_mph;
    /**
     * 店门全景图
     **/
    @BindView(id = R.id.iv_qjt, click = true)
    private ImageView iv_qjt;
    /**
     * 经营者姓名
     **/
    @BindView(id = R.id.et_jyzxm)
    private EditText et_jyzxm;
    /**
     * 经营者电话
     **/
    @BindView(id = R.id.et_jyzdh)
    private EditText et_jyzdh;
    /**
     * 经营者服务电话
     **/
    @BindView(id = R.id.et_jyzfwdh)
    private EditText et_jyzfwdh;
    /**
     * 营业执照
     **/
    @BindView(id = R.id.iv_yyzz, click = true)
    private ImageView iv_yyzz;
    /**
     * 特种行业许可证
     **/
    @BindView(id = R.id.iv_tzhy, click = true)
    private ImageView iv_tzhy;
    /**
     * 消防验收合格证
     **/
    @BindView(id = R.id.iv_xfys, click = true)
    private ImageView iv_xfys;
    /**
     * 环保批文
     **/
    @BindView(id = R.id.iv_hbpw, click = true)
    private ImageView iv_hbpw;
    /**
     * 其他
     **/
    @BindView(id = R.id.iv_qt, click = true)
    private ImageView iv_qt;
    /**
     * 治安负责人
     **/
    @BindView(id = R.id.et_zafzr)
    private EditText et_zafzr;
    /**
     * 治安负责人电话
     **/
    @BindView(id = R.id.et_zafzrdh)
    private EditText et_zafzrdh;
    /**
     * 所属派出所
     **/
    @BindView(id = R.id.tv_sspcs, click = true)
    private TextView tv_sspcs;
    /**
     * 所属警务室
     **/
    @BindView(id = R.id.tv_ssjws, click = true)
    private TextView tv_ssjws;
    /**
     * listview从业者
     **/
    @BindView(id = R.id.lv_cyz)
    private ListView lv_cyz;
    /**
     * 增加从业者信息
     **/
    @BindView(id = R.id.tv_addcyz, click = true)
    private TextView tv_addcyz;
    /**
     * 监控信息采集
     **/
    @BindView(id = R.id.rg_sfcjxf)
    private RadioGroup rg_sfcjxf;
    /**
     * 消防信息采集
     **/
    @BindView(id = R.id.rg_sfcjjk)
    private RadioGroup rg_sfcjjk;
    /**
     * 保存
     **/
    @BindView(id = R.id.btn_save, click = true)
    private Button btn_save;
    /**
     * 备注
     **/
    @BindView(id = R.id.et_bz, click = true)
    private EditText et_bz;
    /**
     * 上传
     **/
    @BindView(id = R.id.btn_upl, click = true)
    private Button btn_upl;
    /***底部 保存  地图模式隐藏**/
    @BindView(id = R.id.ll_boom)
    private LinearLayout ll_boom;
    /***字段 信息布局  当地图模式隐藏*/
    @BindView(id = R.id.sv_all)
    private ScrollView sv_all;
    /**
     * 是否采集监控
     **/
    @BindView(id = R.id.rb_sfcjjk_0)
    private RadioButton rb_sfcjjk_0;
    @BindView(id = R.id.rb_sfcjjk_1)
    private RadioButton rb_sfcjjk_1;
    /**
     * 是否采集消防
     **/
    @BindView(id = R.id.rb_sfcjxf_0)
    private RadioButton rb_sfcjxf_0;
    @BindView(id = R.id.rb_sfcjxf_1)
    private RadioButton rb_sfcjxf_1;
    /**
     * 是否采集监控
     **/
    @BindView(id = R.id.ll_jk, click = true)
    private LinearLayout ll_jk;
    @BindView(id = R.id.tv_sxtzs)
    private TextView tv_sxtzs;
    @BindView(id = R.id.tv_sxtzcs)
    private TextView tv_sxtzcs;
    /**
     * 是否采集消防
     **/
    @BindView(id = R.id.ll_xf, click = true)
    private LinearLayout ll_xf;
    @BindView(id = R.id.tv_mhqsl)
    private TextView tv_mhqsl;
    @BindView(id = R.id.tv_mhqzcs)
    private TextView tv_mhqzcs;
    /**
     * 百度坐标纬度
     **/
    private double bWd;
    /**
     * 百度坐标经度
     **/
    private double bJD;
    /***ImageView拍照标识*/
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private String path2;
    private String path3;
    private String path4;
    private String path5;
    private String path6;
    private String path7;
    /***定位类型*/
    private int map_type;
    private ZAJCPresenter pres;
    private List<ZAJCCYZModle> cyyModles;
    private ZAJCXFModle xfInfo;
    private ZAJCJKModle jkInfo;
    private int i_sfcjjk = 1;
    private int i_sfcjxf = 1;
    private boolean flag = false;
    private String pcs_num;
    private List<String> jwsOrgans = new ArrayList<String>();
    private ProgressDialog mProgressDialog = null;
    private int uploadCount = 0;
    private List<String> filePath = new ArrayList<String>();
    private List<String> fileName = new ArrayList<String>();
    private LocaionInfo mLocation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zacs);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        initData();
    }

    private void initData() {
        /**presenter 初始化**/
        pres = new ZAJCPresenter(this);
        /***从业者 数据集合*/
        cyyModles = new ArrayList<ZAJCCYZModle>();
        /**设置派出所、警务室本地默认数据*/
        tv_sspcs.setText(AppContext.instance.getLoginInfo().getPcs());
        tv_ssjws.setText(AppContext.instance.getLoginInfo().getJws());

        tv_type.setText(getIntent().getStringExtra("zdz"));
        tv_type.setTag(getIntent().getStringExtra("zdnum"));
        //如果派出所编码为空，根据派出所名称寻找派出所编码 来匹配寻找下属警务室！
        pcs_num = zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_PCS, tv_sspcs.getText().toString().trim());
        initClick();
    }

    private void initClick() {
        rg_sfcjxf.setOnCheckedChangeListener(this);
        rg_sfcjjk.setOnCheckedChangeListener(this);
        checkTextLength(et_mc, 20);
        checkTextLength(et_dz, 30);
        checkTextLength(et_mph, 10);
        checkTextLength(et_ldbm, 19);
        checkTextLength(et_jyzxm, 12);
        checkTextLength(et_jyzdh, 12);
        checkTextLength(et_jyzfwdh, 12);
        checkTextLength(et_jyzfwdh, 12);
        checkTextLength(et_zafzr, 12);
        checkTextLength(et_zafzrdh, 12);
        checkTextLength(et_bz, 12);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_qjt:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                i_flag = 2;
                cameraPhoto();
                break;

            case R.id.iv_yyzz:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                i_flag = 3;
                cameraPhoto();
                break;
            case R.id.iv_qt:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                i_flag = 4;
                cameraPhoto();
                break;
            case R.id.iv_hbpw:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                i_flag = 5;
                cameraPhoto();
                break;
            case R.id.iv_xfys:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                i_flag = 6;
                cameraPhoto();
                break;
            case R.id.iv_tzhy:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                i_flag = 7;
                cameraPhoto();
                break;

            /***手动定位*/
            case R.id.tv_sddw:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                Intent inten = new Intent(this, PickupMapActivity.class);
                startActivityForResult(inten, PickupMapActivity.MAP_PICK_UP);
                break;

            case R.id.tv_sxwz:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                loc();//获取定位
                break;

            case R.id.tv_addcyz:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                Intent intent = new Intent(this, ZAJCCYZAty.class);
                startActivityForResult(intent, 0);

                break;

            case R.id.ll_jk:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                Intent intent1 = new Intent(this, ZAJCJKWatchAty.class);
                intent1.putExtra("jkInfo", jkInfo);
                startActivity(intent1);
                break;

            case R.id.ll_xf:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                Intent intent2 = new Intent(this, ZAJCXFWatchAty.class);
                intent2.putExtra("xfInfo", xfInfo);
                startActivity(intent2);
                break;

            //选择派出所
            case R.id.tv_sspcs:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                //需要清空警务室信息，以免数据重复
                jwsOrgans.clear();
                tv_ssjws.setText("");
                tv_ssjws.setHint("请选择警务室");
                final Map<String, String> pcsMaps = zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_PCS);
                new AlertDialog.Builder(this).setItems(pcsMaps.values().toArray(new String[pcsMaps.values().size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_sspcs.setText(pcsMaps.values().toArray(new String[pcsMaps.values().size()])[which]);
                                pcs_num = zdUtils.getMapKey(pcsMaps, tv_sspcs.getText().toString());
                            }
                        }).create().show();
                break;

            //警务室的选择
            case R.id.tv_ssjws:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                jwsOrgans.clear();
                tv_ssjws.setText("");
                tv_ssjws.setHint("请选择警务室");
                if (tv_sspcs.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("派出所不能为空", this);
                } else if (pcs_num != null && !tv_sspcs.getText().toString().trim().isEmpty()) {
                    final Map<String, String> jwsMaps = zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_JWS, pcs_num);
                    jwsMaps.put("1", "其他");
                    new AlertDialog.Builder(this).setItems(jwsMaps.values().toArray(new String[jwsMaps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_ssjws.setText(jwsMaps.values().toArray(new String[jwsMaps.values().size()])[which]);
                                }
                            }).create().show();
                }
                break;

            case R.id.btn_save:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                String msg = getMsg();
                if (!msg.isEmpty()) {
                    BaseUtil.showDialog(msg, this);
                } else {
                    boolean flag = pres.saveInfo(this, getLB(), getCJFL(), getMC(), getDZ(), getMPH(), getLDBM(), getIV_MPHQJT(), getIV_DMQJT(),
                            getJYZXM(), getJYZDH(), getFWDH(), getIV_YYZZ(), getZAFZR(), getSSPCS(),
                            getSSJWS(), getJD(), getWD(), getZAFZRDH(), getSFCJJK(), getSFCJXF(), getLOCTYPE()
                            , getBZ(), cyyModles, xfInfo, jkInfo, getIV_QT(), getIV_HBPW(), getIV_XFYS(), getIV_TZHY());

                    if (flag == true) {
                        /***保存房屋编码历史记录***/
                        BaseDataUtils.saveHistory(this, "history", "440307005", et_ldbm);
                        new AlertDialog.Builder(this).setTitle("提示")
                                .setMessage("采集数据成功!")
                                .setNegativeButton("查看详情",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                Intent intent = new Intent(ZacsAty.this, ZAJCLocalAty.class);
                                                startActivity(intent);
                                                ZacsAty.this.finish();
                                            }
                                        })
                                .setPositiveButton("继续采集",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                Intent intent = new Intent(ZacsAty.this, ZacsAty.class);
                                                intent.putExtra("zdnum",getIntent().getStringExtra("zdnum"));
                                                intent.putExtra("zdz",getIntent().getStringExtra("zdz"));
                                                startActivity(intent);
                                                ZacsAty.this.finish();
                                            }
                                        }).setCancelable(false).show();
                    } else {
                        BaseUtil.showDialog("保存失败", this);
                    }
                }
                break;

            case R.id.btn_upl:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                String msg1 = getMsg();
                if (!msg1.isEmpty()) {
                    BaseUtil.showDialog(msg1, ZacsAty.this);
                } else {
                    final ZAJCModle zajcModle = pres.uploadInfo(getLB(), getCJFL(), getMC(),
                            getDZ(), getMPH(), getLDBM(), getIV_MPHQJT(), getIV_DMQJT(), getJYZXM(),
                            getJYZDH(), getFWDH(), getIV_YYZZ(), getZAFZR(), getSSPCS(), getSSJWS(),
                            getJD(), getWD(), getZAFZRDH(), getSFCJJK(), getSFCJXF(), getLOCTYPE(), getBZ()
                            , getIV_QT(), getIV_HBPW(), getIV_XFYS(), getIV_TZHY());
                    // if (zajcModle != null && cyyModles.size() != 0) {
                    if (zajcModle != null) {
                        if (((AppContext) getApplication()).getNetworkType() != 1) {

                            DialogHelper.showAlertDialog(getResources().getString(R.string.upload_no_wifi_tips), ZacsAty.this, new DialogHelper.OnOptionClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, Object o) {
                                    startUploadThread(zajcModle);
                                }
                            });
                        } else {
                            startUploadThread(zajcModle);
                        }
                    } else {
                        Message msg2 = new Message();
                        msg2.what = 3;
                        handler.sendMessage(msg2);
                    }
                }
                break;
        }
    }

    //
    private void startUploadThread(final ZAJCModle zajcModle) {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                // 将数据上传到服务器
                uploadImage(zajcModle);
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
    void uploadImage(final ZAJCModle mInfo) {
        for (ZAJCCYZModle cyzInfo : cyyModles) {
            if (cyzInfo.getIV_CYZQSZ() != null) {
                String path = cyzInfo.getIV_CYZQSZ();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }
        }
        if (jkInfo != null && jkInfo.getIV_JKPMT() != null) {
            String path = jkInfo.getIV_JKPMT();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }

        if (jkInfo != null && jkInfo.getIV_SXTQJT() != null) {
            String path = jkInfo.getIV_SXTQJT();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }

        if (xfInfo != null && xfInfo.getIV_XFQJT() != null) {
            String path = xfInfo.getIV_XFQJT();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }

        /**判断数据是不是为空  然后判断有没有照片**/
        if (filePath.size() != 0 || mInfo.getIV_MPHQJT() != null
                || mInfo.getIV_DMQJT() != null || mInfo.getIV_YYZZ() != null) {
            if (mInfo.getIV_MPHQJT() != null) {
                String path = mInfo.getIV_MPHQJT();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            if (mInfo.getIV_DMQJT() != null) {
                String path = mInfo.getIV_DMQJT();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }
            if (mInfo.getIV_YYZZ() != null) {
                String path = mInfo.getIV_YYZZ();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }
            if (mInfo.getIV_QT() != null) {
                String path = mInfo.getIV_QT();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }
            if (mInfo.getIV_HBPW() != null) {
                String path = mInfo.getIV_HBPW();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            if (mInfo.getIV_TZHY() != null) {
                String path = mInfo.getIV_TZHY();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }
            if (mInfo.getIV_XFYS() != null) {
                String path = mInfo.getIV_XFYS();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            //上传图片
            ApiResource.uploadZAJCImage(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        uploadInfo(mInfo);
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
        } else {
            uploadInfo(mInfo);
        }
    }


    //同步上传采集数据到服务端
    public void uploadInfo(final ZAJCModle info) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        /***治安基础信息*/
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        /***消防信息*/
        JSONObject jo1 = JSON.parseObject(JSON.toJSONString(xfInfo));
        /***监控信息*/
        JSONObject jo2 = JSON.parseObject(JSON.toJSONString(jkInfo));

        /**治安基础*/
        if (jo != null) {
            jo.remove(ZAJCModle.sZAJCID);
            jo.remove("gLID");
            jo.remove("cJFL");

            jo.put("CJFL", tv_type.getTag());

            jo.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
            jo.put("CJDW", AppContext.instance.getLoginInfo().getOrgancode());
            if (info.getIV_MPHQJT() != null) {
                jo.remove(ZAJCModle.sIV_MPHQJT);
                jo.put(ZAJCModle.sIV_MPHQJT, BaseUtil.getFileName(info.getIV_MPHQJT()));
            }

            if (info.getIV_DMQJT() != null) {
                jo.remove(ZAJCModle.sIV_DMQJT);
                jo.put(ZAJCModle.sIV_DMQJT, BaseUtil.getFileName(info.getIV_DMQJT()));
            }
            if (info.getIV_YYZZ() != null) {
                jo.remove(ZAJCModle.sIV_YYZZ);
                jo.put(ZAJCModle.sIV_YYZZ, BaseUtil.getFileName(info.getIV_YYZZ()));
            }
            if (info.getIV_QT() != null) {
                jo.remove(ZAJCModle.sIV_QT);
                jo.put(ZAJCModle.sIV_QT, BaseUtil.getFileName(info.getIV_QT()));
            }
            if (info.getIV_TZHY() != null) {
                jo.remove(ZAJCModle.sIV_TZHY);
                jo.put(ZAJCModle.sIV_TZHY, BaseUtil.getFileName(info.getIV_TZHY()));
            }
            if (info.getIV_XFYS() != null) {
                jo.remove(ZAJCModle.sIV_XFYS);
                jo.put(ZAJCModle.sIV_XFYS, BaseUtil.getFileName(info.getIV_XFYS()));
            }
            if (info.getIV_HBPW() != null) {
                jo.remove(ZAJCModle.sIV_HBPW);
                jo.put(ZAJCModle.sIV_HBPW, BaseUtil.getFileName(info.getIV_HBPW()));
            }
            jsonObject.put("ZAJCXX", jo.toString());
        }

        /**消防*/
        if (jo1 != null) {
            jo1.remove("gLID");
            if (xfInfo.getIV_XFQJT() != null) {
                jo1.remove(ZAJCXFModle.sIV_XFQJT);
                jo1.put(ZAJCXFModle.sIV_XFQJT, BaseUtil.getFileName(xfInfo.getIV_XFQJT()));
            }
            jsonObject.put("XFXX", jo1.toString());
        }
        /**监控*/
        if (jo2 != null) {
            jo2.remove("jKID");
            jo2.remove("gLID");
            if (jkInfo.getIV_JKPMT() != null) {
                jo2.remove(ZAJCJKModle.sIV_JKPMT);
                jo2.put(ZAJCJKModle.sIV_JKPMT, BaseUtil.getFileName(jkInfo.getIV_JKPMT()));
            }

            if (jkInfo.getIV_SXTQJT() != null) {
                jo2.remove(ZAJCJKModle.sIV_SXTQJT);
                jo2.put(ZAJCJKModle.sIV_SXTQJT, BaseUtil.getFileName(jkInfo.getIV_SXTQJT()));
            }
            jsonObject.put("JKXX", jo2.toString());

        }
        /**从业者*/
        for (ZAJCCYZModle cyzInfo : cyyModles) {
            JSONObject jo3 = JSON.parseObject(JSON.toJSONString(cyzInfo));
            jo3.remove("cYZID");
            jo3.remove("gLID");
            if (cyzInfo.getIV_CYZQSZ() != null) {
                jo3.remove(ZAJCCYZModle.sIV_CYZQSZ);
                jo3.put(ZAJCCYZModle.sIV_CYZQSZ, BaseUtil.getFileName(cyzInfo.getIV_CYZQSZ()));
            }
            jsonArray.add(jo3);
        }

        jsonObject.put("CYZ", jsonArray.toString());
        /**转成ARRAY*/
        String json = jsonObject.toString();

        ApiResource.addZAJCInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);

                if (!result.isEmpty() && result.startsWith("true")) {
                    uploadCount++;
                    /**删除采集本地照片**/
                    CollectDBUtils.DeleteZAImgLocal(cyyModles, jkInfo, xfInfo, info);
                    mProgressDialog.incrementProgressBy(1);
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

    private String getMsg() {


        String msg = "";

        if (TextUtils.isEmpty(getMC())) {
            msg = getResources().getString(R.string.mc) + getResources().getString(R.string.info_null);
            /***判断 地址是否为空  **/
        } else if (TextUtils.isEmpty(getDZ())) {
            msg = getResources().getString(R.string.dz) + getResources().getString(R.string.info_null);
            /*** 判断 店门全景图是否为空 **/
        } else if (TextUtils.isEmpty(getIV_DMQJT())) {
            msg = "店面全景图"+ getResources().getString(R.string.info_null);
            /*** 判断 经营者姓名是否为空 **/
        } else if (TextUtils.isEmpty(getJYZXM()) ) {
            msg = getResources().getString(R.string.za_jyzxm) + getResources().getString(R.string.info_null);
            /*** 判断 经营者电话是否为空 **/
        } else if (TextUtils.isEmpty(getJYZDH())) {
            msg = getResources().getString(R.string.za_jyzdh) + getResources().getString(R.string.info_null);
            /*** 判断 经营者电话是否符合规范**/
        } else if (StringUtils.isMobileNO(getJYZDH()) == false && getJYZDH().length() != 12 ) {
            msg = "经营者电话请输入11位手机号码或12位座机号码";
            /***判断是否采集从业人员  **/
        } else if (cyyModles.size() == 0 ) {
            msg = "请采集从业者人员信息";
        }
        return msg;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            /**是否采集监控*/
            case R.id.rg_sfcjjk:
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) this.findViewById(radioButtonId);
                /**显示有监控需要填写监控资料**/
                if (jkInfo == null && radioButtonId == R.id.rb_sfcjjk_0) {
                    Intent intent = new Intent(this, ZAJCJKAty.class);
                    startActivityForResult(intent, 0);
                    rb_sfcjjk_1.setChecked(true);
                    //// TODO: 2016/5/22
                } else if (radioButtonId == R.id.rb_sfcjjk_1) {
                    i_sfcjjk = 1;
                    ll_jk.setVisibility(View.GONE);
                    jkInfo = null;
                }
                break;
            /**是否采集消防*/
            case R.id.rg_sfcjxf:
                //获取变更后的选中项的ID
                int radioButtonId2 = group.getCheckedRadioButtonId();
                /**显示有监控需要填写监控资料**/
                if (xfInfo == null && radioButtonId2 == R.id.rb_sfcjxf_0) {
                    Intent intent = new Intent(this, ZAJCXFInfoAty.class);
                    startActivityForResult(intent, 0);
                    rb_sfcjxf_1.setChecked(true);
                } else if (radioButtonId2 == R.id.rb_sfcjxf_1) {
                    i_sfcjxf = 1;
                    ll_xf.setVisibility(View.GONE);
                    xfInfo = null;
                }
                break;
        }
    }


    @Override
    public int getZAJCID() {
        return 0;
    }

    @Override
    public String getLB() {
        return null;//tv_lb.getText().toString().trim();
    }

    @Override
    public String getCJFL() {
        return tv_type.getText().toString().trim();
    }

    @Override
    public String getMC() {
        return et_mc.getText().toString().trim();
    }

    @Override
    public String getDZ() {
        return et_dz.getText().toString().trim();
    }

    @Override
    public String getMPH() {
        return et_mph.getText().toString().trim();
    }

    @Override
    public String getLDBM() {
        return et_ldbm.getText().toString().trim();
    }

    @Override
    public String getIV_MPHQJT() {
        return path1;
    }

    @Override
    public String getIV_DMQJT() {
        return path2;
    }

    @Override
    public String getJYZXM() {
        return et_jyzxm.getText().toString().trim();
    }

    @Override
    public String getJYZDH() {
        return et_jyzdh.getText().toString().trim();
    }

    @Override
    public String getFWDH() {
        return et_jyzfwdh.getText().toString().trim();
    }

    @Override
    public String getIV_YYZZ() {
        return path3;
    }

    @Override
    public String getIV_QT() {
        return path4;
    }

    @Override
    public String getIV_HBPW() {
        return path5;
    }

    @Override
    public String getIV_XFYS() {
        return path6;
    }

    @Override
    public String getIV_TZHY() {
        return path7;
    }

    @Override
    public String getZAFZR() {
        return et_zafzr.getText().toString().trim();
    }

    @Override
    public String getSSPCS() {
        return tv_sspcs.getText().toString().trim();
    }

    @Override
    public String getSSJWS() {
        return tv_ssjws.getText().toString().trim();
    }

    @Override
    public Double getJD() {
        return bJD;
    }

    @Override
    public Double getWD() {
        return bWd;
    }

    @Override
    public String getZAFZRDH() {
        return et_zafzrdh.getText().toString().trim();
    }

    @Override
    public int getSFCJJK() {
        return i_sfcjjk;
    }

    @Override
    public int getSFCJXF() {
        return i_sfcjxf;
    }

    @Override
    public String getBZ() {
        return et_bz.getText().toString();
    }

    @Override
    public String getLOCTYPE() {
        return "" + map_type;
    }

    @Override
    public void setLB(String LB) {

    }

    @Override
    public void setCJFL(String CJFL) {

    }

    @Override
    public void setMC(String MC) {

    }


    @Override
    public void setDZ(String DZ) {

    }

    @Override
    public void setMPH(String MPH) {

    }

    @Override
    public void setLDBM(String LDBM) {

    }

    @Override
    public void setIV_MPHQJT(String IV_MPHQJT) {

    }

    @Override
    public void setIV_DMQJT(String IV_DMQJT) {

    }

    @Override
    public void setJYZXM(String JYZXM) {

    }

    @Override
    public void setJYZDH(String JYZDH) {

    }

    @Override
    public void setFWDH(String FWDH) {

    }

    @Override
    public void setIV_YYZZ(String IV_YYZZ) {

    }

    @Override
    public void setIV_HBPW(String IV_HBPW) {

    }

    @Override
    public void setIV_TZHY(String IV_TZHY) {

    }

    @Override
    public void setIV_QT(String IV_QT) {

    }

    @Override
    public void setIV_XFYS(String IV_XFYS) {

    }

    @Override
    public void setZAFZR(String ZAFZR) {

    }

    @Override
    public void setSSPCS(String SSPCS) {

    }

    @Override
    public void setSSJWS(String SSJWS) {

    }

    @Override
    public void setJD(Double JD) {

    }

    @Override
    public void setWD(Double WD) {

    }

    @Override
    public void setZAFZRDH(String ZAFZRDH) {

    }

    @Override
    public void setSFCJJK(int SFCJJK) {

    }

    @Override
    public void setSFCJXF(int SFCJXF) {

    }

    @Override
    public void setLOCTYPE(String LOCTYPE) {

    }

    @Override
    public void setBZ(String BZ) {

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:// 上传进度条显示
                    mProgressDialog = new ProgressDialog(ZacsAty.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("上传");
                    mProgressDialog.setMessage("数据正在上传中...");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 2:// 上传之后
                    mProgressDialog.cancel();
                    new AlertDialog.Builder(ZacsAty.this).setTitle("提示")
                            .setMessage("上传成功!")
                            .setNegativeButton("返回",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            ZacsAty.this.finish();
                                        }
                                    })
                            .setPositiveButton("继续采集",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent(ZacsAty.this, ZacsAty.class);
                                            intent.putExtra("zdnum",getIntent().getStringExtra("zdnum"));
                                            intent.putExtra("zdz",getIntent().getStringExtra("zdz"));
                                            startActivity(intent);
                                            ZacsAty.this.finish();
                                        }
                                    }).setCancelable(false).show();

                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", ZacsAty.this);
                    break;
                case 4://上传检测完成
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到当前APP 版本过低，请回到民警通主菜单点击【系统设置】-【APP 更新】，按提示升级APP 后重新上传！", ZacsAty.this);
                    }
                    break;
            }
        }
    };


    /**
     * 返回拍照
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                && resultCode != 0
                && imagePath != null) {
            /***获得拍照的路径 写入数据库**/
            if (i_flag == 1) {
                path1 = imagePath;
                iv_mph.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            } else if (i_flag == 2) {
                path2 = imagePath;
                iv_qjt.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            } else if (i_flag == 3) {//iv_qt  iv_hbpw   iv_xfys  iv_tzhy
                path3 = imagePath;
                iv_yyzz.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            } else if (i_flag == 4) {
                path4 = imagePath;
                iv_qt.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            } else if (i_flag == 5) {
                path5 = imagePath;
                iv_hbpw.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            } else if (i_flag == 6) {
                path6 = imagePath;
                iv_xfys.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            } else if (i_flag == 7) {
                path7 = imagePath;
                iv_tzhy.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        } else if (resultCode == 1) {
            ZAJCCYZModle cyyModle = (ZAJCCYZModle) data.getSerializableExtra("cyzInfo");
            if (cyyModles.size() == 0) {
                cyyModles.add(cyyModle);
            } else {
                for (ZAJCCYZModle zajc : cyyModles) {
                    if (zajc.getCYZSFZ().equals(cyyModle.getCYZSFZ())) {
                        toast("该从业人员身份证号码已经采集！");
                        return;
                    }
                }
                cyyModles.add(cyyModle);
            }
            /**初始化从业者adapter**/
            initCYZAdapt(cyyModles);
        } else if (resultCode == 2) {
            i_sfcjxf = 0;
            xfInfo = (ZAJCXFModle) data.getSerializableExtra("xfInfo");
            tv_mhqsl.setText(((ZAJCXFModle) data.getSerializableExtra("xfInfo")).getMHQSL());
            tv_mhqzcs.setText(((ZAJCXFModle) data.getSerializableExtra("xfInfo")).getZCS());
            ll_xf.setVisibility(View.VISIBLE);
            rb_sfcjxf_0.setChecked(true);
        } else if (resultCode == 3) {
            jkInfo = (ZAJCJKModle) data.getSerializableExtra("jkInfo");
            tv_sxtzs.setText(((ZAJCJKModle) data.getSerializableExtra("jkInfo")).getSXTZS());
            tv_sxtzcs.setText(((ZAJCJKModle) data.getSerializableExtra("jkInfo")).getZCS());
            ll_jk.setVisibility(View.VISIBLE);
            rb_sfcjjk_0.setChecked(true);
            i_sfcjjk = 0;
        } else if (PickupMapActivity.MAP_PICK_UP == requestCode) { //地图选点
            if (Activity.RESULT_OK == resultCode) {
                LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
                onReceiveLoc(info, true, null);
            }
        }
    }

    /**
     * 初始化从业者adapter
     **/
    private void initCYZAdapt(final List<ZAJCCYZModle> cyyModles) {
        if (cyyModles.size() != 0) {
            lv_cyz.setVisibility(View.VISIBLE);
            boolean bflag = false;
            ZAJCCYZAdp mAdapter = new ZAJCCYZAdp(bflag, lv_cyz, cyyModles, R.layout.aty_zacyz_item, ZacsAty.this);
            lv_cyz.setAdapter(mAdapter);
            lv_cyz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ZacsAty.this, ZAJCCYZWatchAty.class);
                    intent.putExtra(GeneralUtils.Info, cyyModles.get(position));
                    startActivity(intent);
                }
            });

            mAdapter.setonClickListener(new ZAJCCYZAdp.onClickListener() {
                @Override
                public void onDel(final int index) {
                    new AlertDialog.Builder(ZacsAty.this)
                            .setTitle(R.string.msg_delete_confirm_title)
                            .setMessage(R.string.msg_delete_confirm)
                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    List delList = new ArrayList();     /**需要删除集合*/
                                    for (Iterator it = cyyModles.iterator(); it.hasNext(); ) {
                                        ZAJCCYZModle cyzInfo = (ZAJCCYZModle) it.next();
                                        if (cyzInfo.getCYZSFZ().equals(cyyModles.get(index).getCYZSFZ())) {
                                            //     it.remove();
                                            delList.add(cyzInfo);
                                        }
                                    }
                                    cyyModles.removeAll(delList);
                                    /**初始化从业者adapter**/
                                    initCYZAdapt(cyyModles);
                                }
                            })
                            .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            });

        } else {
            lv_cyz.setVisibility(View.GONE);
        }
    }


    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess) {
            mLocation = location;
            /**获得纬度信息**/
            bWd = mLocation.getLat();
            /**获得经度信息信息**/
            bJD = mLocation.getLongt();
            /**获得地址信息**/
            et_dz.setText(mLocation.getAddress());
            /**百度坐标，经纬度展现**/
            tv_zb.setText("(" + bWd + "," + bJD + ")");
            map_type = mLocation.getLoctype();
        }
    }


    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            new BaseDataUtils().loginOut(ZacsAty.this);
        }
        // super.dispatchKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem action_list = menu.findItem(R.id.action_list);
//        MenuItem action_map = menu.findItem(R.id.action_map);
//        action_map.setVisible(false);
//        return true;
//    }

    /***activity退出*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new BaseDataUtils().loginOut(ZacsAty.this);
                break;

            /**缓存数据列表模式**/
            case R.id.action_list:
                showActivity(ZAJCLocalAty.class);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.zamanage);
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}