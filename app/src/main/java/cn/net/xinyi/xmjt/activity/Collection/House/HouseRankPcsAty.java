package cn.net.xinyi.xmjt.activity.Collection.House;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Calendar;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;


//采集派出所排名
public class HouseRankPcsAty extends BaseActivity2 implements View.OnClickListener {
    public static final String TAG = HouseRankPcsAty.class.getName();
    /***选择开始时间*/
    @BindView(id = R.id.tv_choose_start_date,click = true)
    private TextView tv_choose_start_date;
    /***选择结束时间*/
    @BindView(id = R.id.tv_choose_end_date,click = true)
    private TextView tv_choose_end_date;
    /***listview*/
    @BindView(id = R.id.listView)
    private ListView mListView;
    private RankHousePCSAdapter rankAdapter;
    private int now_year, now_month, now_day;
    private JSONArray rankArray;
    private int days = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_house_rank_organ);
        /***控件绑定**/
        AnnotateManager.initBindView(this);
        setViews();
        getRank();
    }


    private void setViews() {
        //初始化数据，设置默认值
        setData();
    }

    private void getRank(){
        showLoadding();
        final String kssj = tv_choose_start_date.getText().toString().trim();
        final String jssj = tv_choose_end_date.getText().toString().trim();
        JSONObject jo = new JSONObject();
        jo.put("KSSJ",kssj);
        jo.put("JSSJ",jssj);
        String json = jo.toJSONString();
        ApiResource.HousePCSRank(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result.length()>4){
                    stopLoading();
                    try {
                        rankArray = JSON.parseArray(result);
                        rankAdapter = new RankHousePCSAdapter(HouseRankPcsAty.this, rankArray);
                        mListView.setAdapter(rankAdapter);
                        setOnItemClick();
                    } catch (Exception e) {
                        onFailure(i, headers, bytes, null);
                    }
                }else {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                BaseUtil.showDialog("获取数据失败", HouseRankPcsAty.this);
            }
        });
    }

    private void setOnItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(HouseRankPcsAty.this,HouseRankDetailsAty.class);
                intent.putExtra("PCSBM",((JSONObject)rankArray.get(position)).getString("ZDBM"));
                startActivity(intent);
            }
        });
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
                new DatePickerDialog(
                        HouseRankPcsAty.this, new DatePickerDialog.OnDateSetListener() {
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
                            long  init_date=format.parse("2015-10-26").getTime()/1000;
                            //开始时间不能小于2015-08-26
                            if (choose_start_date<init_date){
                                Toast.makeText(HouseRankPcsAty.this, "开始日期不能小于2015-10-26",Toast.LENGTH_SHORT).show();
                                //结束时间和开始时间进行比较
                            } else if (choose_end_date<choose_start_date){
                                Toast.makeText(HouseRankPcsAty.this, "开始日期不能大于结束日期",Toast.LENGTH_SHORT).show();
                            }else{
                                tv_choose_start_date.setText(forma);
                                getRank();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },Integer.valueOf(start_date.substring(0, 4).toString()), (Integer.valueOf(start_date.substring(5, 7).toString()) - 1),
                        Integer.valueOf(start_date.substring(8, 10).toString())).show();
                break;


            //点击时间获得结束时间
            case  R.id.tv_choose_end_date:
                DatePickerDialog dialog2 = new DatePickerDialog(
                        HouseRankPcsAty.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c_end.set(year, monthOfYear, dayOfMonth);
                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c_end);
                        //判断起始时间不能大于结束时间
                        try {
                            //获得开始时间转为秒
                            long  choose_start_date = (format.parse(tv_choose_start_date.getText().toString().trim()).getTime())/1000;
                            //获得结束时间 转为秒
                            long  choose_end_date = (format.parse(forma.toString()).getTime())/1000;
                            //结束时间应该大于开始时间
                            if (choose_end_date<choose_start_date){
                                Toast.makeText(HouseRankPcsAty.this, "开始日期不能大于结束日期",Toast.LENGTH_SHORT).show();
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
        }
    }
    @Override
    public String getAtyTitle() {
        return "派出所采集排名";
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}
