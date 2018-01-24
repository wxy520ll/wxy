package cn.net.xinyi.xmjt.v527.presentation.gzrz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.request.RequestOptions;
import com.xinyi_tech.comm.picker.picture.entity.LocalMedia;
import com.xinyi_tech.comm.picker.picture.ucrop.util.FileUtils;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.comm.widget.picker.SuperImageView;
import com.xinyi_tech.comm.widget.picker.SuperMutiPickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.GsonUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.v527.base.BaseJwtActivity;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzPageModel;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzTypeModel;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.presenter.AddGzrzPresenter;
import cn.net.xinyi.xmjt.v527.presentation.txl.TxlActivity;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlPersonModel;
import cn.net.xinyi.xmjt.v527.util.String2CodeUtils;

import static cn.net.xinyi.xmjt.R.id.bt_select_File;
import static cn.net.xinyi.xmjt.R.id.ed_time;
import static cn.net.xinyi.xmjt.api.ApiHttpClient.IMAGE_HOST;
import static cn.net.xinyi.xmjt.utils.BaseUtil.getFileName;
import static cn.net.xinyi.xmjt.v527.presentation.gzrz.presenter.AddGzrzPresenter.JSONSUCCESS;
import static cn.net.xinyi.xmjt.v527.util.GzrzUtils.checkTextView;
import static cn.net.xinyi.xmjt.v527.util.GzrzUtils.onYearMonthDayTimePicker;
import static cn.net.xinyi.xmjt.v527.util.GzrzUtils.showFileChooser;
import static cn.net.xinyi.xmjt.v527.util.GzrzUtils.showTypeSelect;



/**
 * Created by jiajun.wang on 2017/12/29.
 * 添加工作日志
 */

public class AddGzrzActivity extends BaseJwtActivity<AddGzrzPresenter> {

    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.ed_type)
    EditText edType;
    @BindView(R.id.ed_time)
    EditText edTime;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;

    @BindView(R.id.iv_photo)
    SuperMutiPickerView ivPhoto;
    @BindView(R.id.tv_title)
    EditText tvTitle;
    @BindView(bt_select_File)
    Button btSelectFile;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.iv_photo2)
    SuperImageView ivPhoto2;
    @BindView(R.id.iv_photo3)
    SuperImageView ivPhoto3;
    @BindView(R.id.ed_person)
    EditText edPerson;
    @BindView(R.id.hide)
    LinearLayout hide;

    private String Filepath;
    private JSONObject jsonObject;
    private List<GzrzTypeModel> gzrzTypeModelList;
    private String[] items;
    private GzrzPageModel gzrzPageModel;
    private final static int REPORTID = 201;
    private TxlPersonModel txlPersonModel;
    private String typeCode;
    private String tag="/storage";
    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {

        ToolbarUtils.with(this, toolBar).setSupportBack(true).setTitle("工作日志", true).build();
        ivPhoto.setupParams(new SuperMutiPickerView.Builder(this).maxSelectCount(3).isOnlyPicture(true));
        mPresenter.getDictionary();

        if (getIntent().getExtras() != null) {
            gzrzPageModel = (GzrzPageModel) getIntent().getExtras().getSerializable("data");
            initView();
        }
        edTime.setText(DateUtil.getNowTime().split(" ")[0]);
        rbYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hide.setVisibility(!isChecked ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void initView() {
        tvTitle.setText(gzrzPageModel.getRECORD_TITLE());
        edType.setText(gzrzPageModel.getRECORD_TYPE_NAME());
        edType.setTextColor(Color.parseColor("#ff000000"));
        edTime.setTextColor(Color.parseColor("#ff000000"));
        edTime.setText(gzrzPageModel.getCREATED_TIME().split(" ")[0]);
        edContent.setText(gzrzPageModel.getRECORD_BODY());
        if (!StringUtils.isEmpty(gzrzPageModel.getATT_ID())){
            btSelectFile.setText(gzrzPageModel.getATT_ID());
        }

        Filepath=gzrzPageModel.getATT_ID();
        if (gzrzPageModel.getREPORTED() == 1) {
            rbYes.setChecked(true);
            btSave.setVisibility(View.GONE);
            edType.setEnabled(false);
            tvTitle.setFocusable(false);
            edTime.setEnabled(false);
            edContent.setFocusable(false);
            btSelectFile.setClickable(false);
            ivPhoto.setEnabled(false);
            //edPerson.setText(gzrzPageModel.get);
        } else {
            rbNo.setChecked(true);
            hide.setVisibility(View.GONE);
            btSave.setText("更新");
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.camera)
                .error(R.drawable.camera);
        List<String> mlist = new ArrayList<String>();
        if (!StringUtils.isEmpty(gzrzPageModel.getFILE_ID())) {
            mlist.add(IMAGE_HOST + "/log/" + gzrzPageModel.getFILE_ID());
        }
        if (!StringUtils.isEmpty(gzrzPageModel.getFILE_ID2())) {
            mlist.add(IMAGE_HOST + "/log/" + gzrzPageModel.getFILE_ID2());
        }
        if (!StringUtils.isEmpty(gzrzPageModel.getFILE_ID3())) {
            mlist.add(IMAGE_HOST + "/log/" + gzrzPageModel.getFILE_ID3());
        }
        ivPhoto.setVaule(GsonUtils.obj2Json(mlist));
        if (!StringUtils.isEmpty(gzrzPageModel.getSBRY())) {
            edPerson.setText(gzrzPageModel.getSBRY().split("_")[1]);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.aty_gzrz_upload;
    }

    @Override
    protected AddGzrzPresenter getPresenter() {
        return new AddGzrzPresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == AddGzrzPresenter.FILESUCCESS) {
            JSONArray jsonArray = JSON.parseArray(data.toString());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String key = jsonObject.keySet().iterator().next();
                if (key.equals("1")) {
                    AddGzrzActivity.this.jsonObject.put("ATT_ID", jsonObject.getString("1"));
                } else if (key.equals("2")) {
                    AddGzrzActivity.this.jsonObject.put("FILE_ID", jsonObject.getString("2"));
                } else if (key.equals("3")) {
                    AddGzrzActivity.this.jsonObject.put("FILE_ID2", jsonObject.getString("3"));
                } else if (key.equals("4")) {
                    AddGzrzActivity.this.jsonObject.put("FILE_ID3", jsonObject.getString("4"));
                }
            }
            String s = jsonObject.toJSONString();
            if (btSave.getText().toString().equals("更新")) {
                jsonObject.put("ID",gzrzPageModel.getID());
                //mPresenter.updateJson(jsonObject);
                mPresenter.updateJson(jsonObject);
            }else {
                mPresenter.uploadJson(jsonObject);
            }
        } else if (requestCode == AddGzrzPresenter.DICTIONARY) {
            gzrzTypeModelList = (List<GzrzTypeModel>) data;
            items = String2CodeUtils.getItems(gzrzTypeModelList);
        } else if (requestCode == JSONSUCCESS) {
            DialogHelper.showAlertDialog(AddGzrzActivity.this, "上传成功", new DialogHelper.OnOptionClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, Object o) {
                    dialog.dismiss();
                    setResult(RESULT_OK);
                    AddGzrzActivity.this.finish();
                }
            });
        }
    }

    @OnClick({R.id.bt_save, R.id.ed_type, ed_time, bt_select_File, R.id.ed_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                if (checkData()) {
                    jsonObject = getInputData();
                    mPresenter.uploadFile(jsonObject);
                }
                break;
            case R.id.ed_type:
                showTypeSelect((TextView) view, items, AddGzrzActivity.this);
                break;
            case ed_time:
                onYearMonthDayTimePicker((TextView) view, AddGzrzActivity.this);
                break;
            case bt_select_File:
                showFileChooser(AddGzrzActivity.this);
                break;
            case R.id.ed_person:
                Intent i = new Intent(AddGzrzActivity.this, TxlActivity.class);
                i.setFlags(100);
                startActivityForResult(i, REPORTID);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ivPhoto.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Filepath = FileUtils.getPath(this, uri);
                    if (!Filepath.contains("jpg") && !Filepath.contains("png")) {
                        btSelectFile.setText(getFileName(Filepath));
                    } else {
                        ToastyUtil.warningLong("附件不能上传图片,请更改");
                    }
                }
                break;
            case REPORTID:
                if (data != null) {
                    txlPersonModel = (TxlPersonModel) data.getSerializableExtra("person");
                    edPerson.setText(txlPersonModel.getNAME());
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean checkData() {
        if (!checkTextView(tvTitle)) return false;
        if (!checkTextView(edType)) return false;
        if (!checkTextView(edTime)) return false;
        if (rbYes.isChecked()) {
            if (!checkTextView(edPerson)) return false;
        }
        return true;
    }

    public JSONObject getInputData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CREATED_TIME", DateUtil.getNowTime());
        jsonObject.put("RECORD_TYPE", String2CodeUtils.getCodeByName(gzrzTypeModelList, edType.getText().toString()));
        jsonObject.put("RECORD_BODY", edContent.getText().toString());
        jsonObject.put("RECORD_TITLE", tvTitle.getText().toString());
        jsonObject.put("REPORTED", rbYes.isChecked() ? "1" : "0");
        if (Filepath!=null&&Filepath.contains(tag)) {
            jsonObject.put("ATT_ID", Filepath);//
        }
        List<LocalMedia> selectImage = ivPhoto.getSelectImage();
        for (int i = 0; i < selectImage.size(); i++) {
            LocalMedia l = selectImage.get(i);
            if (!l.getCompressPath().contains("http")&&l.getCompressPath().contains(tag)){
                if (i == 0) {
                    jsonObject.put("FILE_ID",l.getCompressPath());
                }
                if (i == 1) {
                    jsonObject.put("FILE_ID2",l.getCompressPath());
                }
                if (i == 2) {
                    jsonObject.put("FILE_ID3", l.getCompressPath());
                }
            }
        }
        if (txlPersonModel != null) {
            jsonObject.put("POLICENOLIST", txlPersonModel.getPOLICENO());
        }
        return jsonObject;
    }
}
