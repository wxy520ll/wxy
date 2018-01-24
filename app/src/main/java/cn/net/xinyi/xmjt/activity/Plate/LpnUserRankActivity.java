package cn.net.xinyi.xmjt.activity.Plate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Plate.Adapter.LpnUserRankAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.GeneralUtils;


/**
 * Created by hao.zhou on 2015/8/27.
 */
public class LpnUserRankActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = LpnUserRankActivity.class.getName();
    private Calendar c_start= Calendar.getInstance();
    private Calendar c_end= Calendar.getInstance();
    private TextView tv_choose_start_date;
    private TextView tv_choose_end_date;
    private TextView tv_choose_pcs;
    private ListView mListView;
    private LpnUserRankAdapter rankAdapter;
    private int now_year, now_month, now_day;
    private JSONArray rankArray;
    private ProgressDialog mProgressDialog = null;
    private int days = 0;
    private SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd");
    private TextView tv_choose_jws;
    private String pcs_num=null;
    private List<String> jwsOrgans=new ArrayList<String>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_of_month);
        setViews();
        getRank();

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("个人上传排行");
        getActionBar().setHomeButtonEnabled(true);
    }

    private void setViews() {
        mProgressDialog = new ProgressDialog(this);

        //开始时间
        tv_choose_start_date = (TextView) findViewById(R.id.tv_choose_start_date);
        tv_choose_start_date.setOnClickListener(this);

        //结束时间
        tv_choose_end_date = (TextView) findViewById(R.id.tv_choose_end_date);
        tv_choose_end_date.setOnClickListener(this);

        //选择派出所
        tv_choose_pcs = (TextView) findViewById(R.id.tv_choose_pcs);
        tv_choose_pcs.setOnClickListener(this);


        //选择警务室
        tv_choose_jws = (TextView) findViewById(R.id.tv_choose_jws);
        tv_choose_jws.setOnClickListener(this);
        tv_choose_jws.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getRank();
            }
        });

        tv_choose_pcs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getRank();
            }
        });

        tv_choose_jws.setText(AppContext.instance.getLoginInfo().getJws());
        tv_choose_pcs.setText(AppContext.instance.getLoginInfo().getPcs());
        //如果派出所编码为空，根据派出所名称寻找派出所编码 来匹配寻找下属警务室！
        pcs_num = zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_PCS,tv_choose_pcs.getText().toString().trim());
        mListView = (ListView) findViewById(R.id.listView3);
        //初始化数据，设置默认值
        setData();
    }



    private void getRank(){
        final String kssj = tv_choose_start_date.getText().toString().trim();
        final String jssj = tv_choose_end_date.getText().toString().trim();

        JSONObject jo = new JSONObject();
        jo.put("kssj",kssj);
        jo.put("jssj",jssj);

        if(!tv_choose_pcs.getText().toString().trim().isEmpty()){
            jo.put("pcs", tv_choose_pcs.getText().toString().trim());
        }
        if(!tv_choose_jws.getText().toString().trim().isEmpty()){
            jo.put("jws",tv_choose_jws.getText().toString().trim());
        }


        String json = jo.toJSONString();

        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);

        ApiResource.getUserRank(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    rankArray = JSON.parseArray(result);
                    days = DateUtil.daysBetween(kssj, jssj) + 1;
                    Message msg = new Message();
                    msg.what = 1;
                    mHandle.sendMessage(msg);

                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 2;
                mHandle.sendMessage(msg);
            }
        });
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在联网获取排行榜数据");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    initListView();
                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("获取数据失败", LpnUserRankActivity.this);
                    break;
                default:
                    break;
            }
        }
    };

    private void initListView(){
        rankAdapter = new LpnUserRankAdapter(this, rankArray,days);
        mListView.setAdapter(rankAdapter);
    }

    private void setData() {
        /***获得当前时间的年月日
         *
         * 获取的数值比实际小1  用的时候需要+1   */
        now_year = c_start.get(Calendar.YEAR);
        now_month =c_start.get(Calendar.MONTH);
        now_day = c_start.get(Calendar.DAY_OF_MONTH);

        //和当前的日期进行比较，如果小于25日，显示上个月26-现在时间
        if (now_day<=25){
            /***如果实际的月份 是1月  需要设置成为前一年的12月*/
            if (now_month==0){
                tv_choose_start_date.setText( new StringBuilder().append(now_year-1).append("-").
                        append(12)
                        .append("-").append(26));
                tv_choose_end_date.setText( new StringBuilder().append(now_year).append("-").
                        append((now_month + 1) < 10 ? "0" + (now_month + 1) : (now_month + 1))
                        .append("-").append((now_day < 10) ? "0" + now_day : now_day));

            }else {
                tv_choose_start_date.setText( new StringBuilder().append(now_year).append("-").
                        append(now_month  < 10 ? "0" + (now_month ) : (now_month ))
                        .append("-").append(26));
                tv_choose_end_date.setText( new StringBuilder().append(now_year).append("-").
                        append((now_month + 1) < 10 ? "0" + (now_month + 1) : (now_month + 1))
                        .append("-").append((now_day < 10) ? "0" + now_day : now_day));

            }
        }else{
            //和当前的日期进行比较，如果大于25日，显示本月26-现在时间
            //正则表达式  如果月份不足10，补0；
            tv_choose_start_date.setText(new StringBuilder().append(now_year).append("-").
                    append((now_month + 1) < 10 ? "0" + (now_month + 1) : (now_month + 1))
                    .append("-").append(26));
            tv_choose_end_date.setText( new StringBuilder().append(now_year).append("-").
                    append((now_month + 1) < 10 ? "0" + (now_month + 1) : (now_month + 1))
                    .append("-").append((now_day < 10) ? "0" + now_day : now_day));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击时间获得开始时间
            case  R.id.tv_choose_start_date:
                String start_date=tv_choose_start_date.getText().toString().trim();
                DatePickerDialog dialog = new DatePickerDialog(
                        LpnUserRankActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c_start.set(year, monthOfYear, dayOfMonth);

                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c_start);
                        //判断起始时间不能大于结束时间
                        try {
                            //获得结束时间转为秒
                            long  choose_end_date = (format.parse(tv_choose_end_date.getText().toString().trim()).getTime())/1000;
                            //获得输入的开始时间转为秒
                            long  choose_start_date = (format.parse(forma.toString()).getTime())/1000;
                            //开始日期为2015-08-26
                            long  init_date=format.parse("2015-08-26").getTime()/1000;
                            //开始时间不能小于2015-08-26
                            if (choose_start_date<init_date){
                                Toast.makeText(LpnUserRankActivity.this, "开始日期不能小于2015-08-26",Toast.LENGTH_SHORT).show();
                                //结束时间和开始时间进行比较
                            } else if (choose_end_date<choose_start_date){
                                Toast.makeText(LpnUserRankActivity.this, "开始日期不能大于结束日期",Toast.LENGTH_SHORT).show();
                            }else{
                                tv_choose_start_date.setText(forma);
                                getRank();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },Integer.valueOf(start_date.substring(0, 4).toString()), (Integer.valueOf(start_date.substring(5, 7).toString()) - 1),
                        Integer.valueOf(start_date.substring(8,10).toString()));
                dialog.show();
                break;


            //点击时间获得结束时间
            case  R.id.tv_choose_end_date:
                DatePickerDialog dialog2 = new DatePickerDialog(
                        LpnUserRankActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c_end.set(year, monthOfYear, dayOfMonth);
                        //判断起始时间不能大于结束时间
                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c_end);
                        //判断起始时间不能大于结束时间
                        try {
                            //获得开始时间转为秒
                            long  choose_start_date = (format.parse(tv_choose_start_date.getText().toString().trim()).getTime())/1000;
                            //获得结束时间 转为秒
                            long  choose_end_date = (format.parse(forma.toString()).getTime())/1000;
                            //结束时间应该大于开始时间
                            if (choose_end_date<choose_start_date){
                                Toast.makeText(LpnUserRankActivity.this, "开始日期不能大于结束日期",Toast.LENGTH_SHORT).show();
                            }else{
                                tv_choose_end_date.setText(forma);
                                getRank();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, c_end.get(Calendar.YEAR), c_end .get(Calendar.MONTH), c_end
                        .get(Calendar.DAY_OF_MONTH));
                dialog2.show();
                break;

            //获得派出所
            case R.id.tv_choose_pcs:
                //需要清空警务室信息，以免数据重复
                jwsOrgans.clear();
                tv_choose_jws.setText("");
                tv_choose_jws.setHint("请选择警务室");
                final Map<String,String> pcsMaps=zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_PCS,userInfo.getFjbm());
                new AlertDialog.Builder(this).setItems(pcsMaps.values().toArray(new String[pcsMaps.values().size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_choose_pcs.setText(pcsMaps.values().toArray(new String[pcsMaps.values().size()])[which]);
                                pcs_num=zdUtils.getMapKey(pcsMaps,tv_choose_pcs.getText().toString());
                            }
                        }).create().show();

                break;

            //获得警务室
            case R.id.tv_choose_jws:
                jwsOrgans.clear();
                tv_choose_jws.setText("");
                tv_choose_jws.setHint("请选择警务室");
                if (tv_choose_pcs.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("派出所不能为空", LpnUserRankActivity.this);
                } else if (pcs_num!=null&&!tv_choose_pcs.getText().toString().trim().isEmpty()) {
                    final Map<String,String> jwsMaps=zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_JWS,pcs_num);
                    jwsMaps.put("1","其他");
                    new AlertDialog.Builder(this).setItems(jwsMaps.values().toArray(new String[jwsMaps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_choose_jws.setText(jwsMaps.values().toArray(new String[jwsMaps.values().size()])[which]);
                                }
                            }).create().show();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

}