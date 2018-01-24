package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.KpiCertificate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera.ViewPagerScroller;
import cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera.VoteSubmitViewPager;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.AnSwerInfo;
import cn.net.xinyi.xmjt.model.MachineExamModle;
import cn.net.xinyi.xmjt.model.SaveQuestionInfo;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.ConstantUtil;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * 答题
 *
 * @author
 */
public class KpiExamAty extends BaseActivity2 {

	private ImageView leftIv;
	private TextView titleTv;
	private TextView right;
	VoteSubmitViewPager viewPager;
	KpiExamSubmitAdapter pagerAdapter;
	List<View> viewItems = new ArrayList<View>();
	private String errorMsg="";
	public List<Map<String, SaveQuestionInfo>> list = new ArrayList<Map<String, SaveQuestionInfo>>();
	public List<SaveQuestionInfo> questionInfos = new ArrayList<SaveQuestionInfo>();
	private List<AnSwerInfo> dataItems;
	Timer timer;
	TimerTask timerTask;
	int minute = 30;
	int second = 00;

	boolean isPause = false;
	int isFirst;
	String imgServerUrl = "";



	Handler handlerTime = new Handler() {
		public void handleMessage(Message msg) {
			// 判断时间快到前2分钟字体颜色改变
			if (minute < 1) {
				right.setTextColor(Color.RED);
			} else {
				right.setTextColor(Color.WHITE);
			}
			if (minute == 0) {
				if (second == 0) {
					isFirst+=1;
					// 时间到
					if(isFirst==1){
						showTimeOutDialog(true, "0");
					}
					right.setText("00:00");
					if (timer != null) {
						timer.cancel();
						timer = null;
					}
					if (timerTask != null) {
						timerTask = null;
					}
				} else {
					second--;
					if (second >= 10) {
						right.setText("0" + minute + ":" + second);
					} else {
						right.setText("0" + minute + ":0" + second);
					}
				}
			} else {
				if (second == 0) {
					second = 59;
					minute--;
					if (minute >= 10) {
						right.setText(minute + ":" + second);
					} else {
						right.setText("0" + minute + ":" + second);
					}
				} else {
					second--;
					if (second >= 10) {
						if (minute >= 10) {
							right.setText(minute + ":" + second);
						} else {
							right.setText("0" + minute + ":" + second);
						}
					} else {
						if (minute >= 10) {
							right.setText(minute + ":0" + second);
						} else {
							right.setText("0" + minute + ":0" + second);
						}
					}
				}
			}
		}
	};

	private Handler handlerStopTime = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					stopTime();
					break;
				case 1:
					startTime();
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};
	private int errortopicNums;
	private int errortopicNums1;
	private int pageScore;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_analogyexam);
		TypeUtils.compatibleWithJavaBean = true;
		initView();
		loadData();

	}

	public void initView() {
		leftIv = (ImageView) findViewById(R.id.left);
		titleTv = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right);
		titleTv.setText("数字证书考核");
		Drawable drawable1 = getBaseContext().getResources().getDrawable(
				R.drawable.ic_practice_time);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
				drawable1.getMinimumHeight());
		right.setCompoundDrawables(drawable1, null, null, null);
		right.setText("30:00");
		viewPager = (VoteSubmitViewPager) findViewById(R.id.vote_submit_viewpager);
		leftIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// finish();
				isPause = true;
				showTimeOutDialog(true, "1");
				Message msg = new Message();
				msg.what = 0;
				handlerStopTime.sendMessage(msg);
			}
		});

		initViewPagerScroll();
	}

	private void loadData(){
		showLoadding();
		JSONObject jo = new JSONObject();
		jo.put("","");
		String json = jo.toJSONString();
		ApiResource.KPIQuestion(json, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				String result = new String(bytes);
				if (result.length()>4){
					stopLoading();
					JSONObject jo=JSON.parseObject(result);
					dataItems=new ArrayList<AnSwerInfo>();
					//判断题
					dataItems.addAll(JSON.parseArray(jo.getString("true-false"),AnSwerInfo.class));
					//单选
					dataItems.addAll(JSON.parseArray(jo.getString("single-choice"),AnSwerInfo.class));
					//多选
					dataItems.addAll(JSON.parseArray(jo.getString("multiple-choice"),AnSwerInfo.class));

					for (int j = 0; j < dataItems.size(); j++) {
						viewItems.add(getLayoutInflater().inflate(
								R.layout.vote_submit_viewpager_item, null));
					}
					pagerAdapter = new KpiExamSubmitAdapter(KpiExamAty.this, viewItems, dataItems,imgServerUrl);
					viewPager.setAdapter(pagerAdapter);
					viewPager.getParent().requestDisallowInterceptTouchEvent(false);
				}else {
					onFailure(i, headers, bytes, null);
				}
			}
			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
				stopLoading();
				if (StringUtils.isEmpty(new String(bytes))) {
					BaseUtil.showDialog("获取数据失败", KpiExamAty.this);
				}else {
					BaseUtil.showDialog(new String(bytes), KpiExamAty.this);
				}
			}
		});

	}

	/**
	 * 设置ViewPager的滑动速度
	 *
	 * */
	private void initViewPagerScroll( ){
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
			mScroller.set(viewPager, scroller);
		}catch(NoSuchFieldException e){

		}catch (IllegalArgumentException e){

		}catch (IllegalAccessException e){

		}
	}

	/**
	 * @param index
	 *            根据索引值切换页面
	 */
	public void setCurrentView(int index) {
		viewPager.setCurrentItem(index);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopTime();
		minute = -1;
		second = -1;
		super.onDestroy();
	}

	// 提交试卷
	public void uploadExamination(int errortopicNum) {
		// TODO Auto-generated method stub
		errortopicNums = errortopicNum;
//		if(questionInfos.size()>0){
//			//选择过题目
//			//全部选中
//			if(questionInfos.size()!=dataItems.size()){
//				//部分选中
//				for (int i = 0; i < dataItems.size(); i++) {
//					if(dataItems.get(i).getISSELECT()==null){
//						errortopicNums1+=1;
//						//保存数据
//						SaveQuestionInfo questionInfo=new SaveQuestionInfo();
//						questionInfo.setQuestionId(dataItems.get(i).getId());
//						questionInfo.setRealAnswer(dataItems.get(i).getCORRECTANSWER());
//						questionInfo.setIs_correct(ConstantUtil.isError);
//						questionInfos.add(questionInfo);
//					}
//				}
//			}
//		}else{
//			//没有选择题目
//			for (int i = 0; i < dataItems.size(); i++) {
//				if(dataItems.get(i).getISSELECT()==null){
//					errortopicNums1+=1;
//					//保存数据
//					SaveQuestionInfo questionInfo=new SaveQuestionInfo();
//					questionInfo.setQuestionId(dataItems.get(i).getId());
//					questionInfo.setRealAnswer(dataItems.get(i).getCORRECTANSWER());
//					questionInfo.setIs_correct(ConstantUtil.isError);
//					questionInfos.add(questionInfo);
//				}
//			}
//		}

		if(questionInfos.size()>0){
			//选择过题目
			//全部选中
			if(questionInfos.size()==dataItems.size()){
				for (int i = 0; i < questionInfos.size(); i++) {
					if (questionInfos.get(i).getIs_correct()
							.equals(ConstantUtil.isCorrect)) {
						int score = Integer.parseInt(questionInfos.get(i).getScore());
						pageScore += score;
					}
				}
			}else{
				//部分选中
				for (int i = 0; i < dataItems.size(); i++) {
					if(dataItems.get(i).getISSELECT()==null){
						errortopicNums1+=1;
						//保存数据
						SaveQuestionInfo questionInfo=new SaveQuestionInfo();
						questionInfo.setQuestionId(dataItems.get(i).getId());
						questionInfo.setRealAnswer(dataItems.get(i).getCORRECTANSWER());
						questionInfo.setIs_correct(ConstantUtil.isError);
						questionInfos.add(questionInfo);
						questionInfo.setScore(dataItems.get(i).getSCORE());
						questionInfo.setIs_correct(ConstantUtil.isError);
						questionInfos.add(questionInfo);
					}
				}
				for (int i = 0; i < dataItems.size(); i++){
					if (questionInfos.get(i).getIs_correct()
							.equals(ConstantUtil.isCorrect)) {
						int score = Integer.parseInt(questionInfos.get(i).getScore());
						pageScore += score;
					}
				}
			}
		}else{
			//没有选择题目
			for (int i = 0; i < dataItems.size(); i++) {
				if(dataItems.get(i).getISSELECT()==null){
					errortopicNums1+=1;
					//保存数据
					SaveQuestionInfo questionInfo=new SaveQuestionInfo();
					questionInfo.setQuestionId(dataItems.get(i).getId());
					questionInfo.setRealAnswer(dataItems.get(i).getCORRECTANSWER());
					questionInfo.setIs_correct(ConstantUtil.isError);
					questionInfos.add(questionInfo);
					questionInfo.setScore(dataItems.get(i).getSCORE());
					questionInfo.setIs_correct(ConstantUtil.isError);
					questionInfo.setIs_correct(ConstantUtil.isError);
					questionInfos.add(questionInfo);
				}
			}

			for (int i = 0; i < dataItems.size(); i++){
				if (questionInfos.get(i).getIs_correct()
						.equals(ConstantUtil.isCorrect)) {
					int score = Integer.parseInt(questionInfos.get(i).getScore());
					pageScore += score;
				}
			}
		}
		//暂停计时
		Message msg = new Message();
		msg.what = 0;
		handlerStopTime.sendMessage(msg);

		DialogHelper.showAlertDialog( KpiExamAty.this,
				"当前题目总数为："+questionInfos.size()+"，"+"错误数"+ errortopicNums+"，"+"未答或答案不全数："
						+errortopicNums1+"，"+"总分:"+(pageScore >= MachineExamModle.ResuleScore ?pageScore+"分，合格":pageScore+"分，【不合格】请继续认真学习"), new DialogHelper.OnOptionClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, Object o) {
						upateData();
					}
				});
	}

	private void upateData() {
		showLoadding();
		MachineExamModle approvalModle=new MachineExamModle();
		approvalModle.setCJYH(userInfo.getUsername());
		approvalModle.setSCORE(pageScore);
		ApiResource.KpiExamineAdd(JSON.toJSONString(approvalModle),new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				String result = new String(bytes);
				if (result!=null&&result.length()>4){
					UI.toast(KpiExamAty.this,"上传成功！");
					setResult(RESULT_OK);
					KpiExamAty.this.finish();
				}else {
					onFailure(i,headers,bytes,null);
				}
				stopLoading();
			}
			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
				stopLoading();
				UI.toast(KpiExamAty.this,"上传失败！");
			}
		});
	}

	// 弹出对话框通知用户答题时间到
	protected void showTimeOutDialog(final boolean flag, final String backtype) {
		final Dialog builder = new Dialog(this, R.style.dialog);
		builder.setContentView(R.layout.my_dialog);
		TextView title = (TextView) builder.findViewById(R.id.dialog_title);
		TextView content = (TextView) builder.findViewById(R.id.dialog_content);
		if (backtype.equals("0")) {
			content.setText("您的答题时间结束,是否提交试卷?");
		} else if(backtype.equals("1")){
			content.setText("您要结束本次答题吗？");
		}else{
			content.setText(errorMsg+"");
		}
		final Button confirm_btn = (Button) builder
				.findViewById(R.id.dialog_sure);
		Button cancel_btn = (Button) builder.findViewById(R.id.dialog_cancle);
		if (backtype.equals("0")) {
			confirm_btn.setText("提交");
			cancel_btn.setText("退出");
		} else if(backtype.equals("1")){
			confirm_btn.setText("退出");
			cancel_btn.setText("继续答题");
		}else{
			confirm_btn.setText("确定");
			cancel_btn.setVisibility(View.GONE);
		}
		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (backtype.equals("0")){
					builder.dismiss();
					uploadExamination(pagerAdapter.errorTopicNum());
				}else{
					builder.dismiss();
					finish();
				}
			}
		});

		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (backtype.equals("0")) {
					finish();
					builder.dismiss();
				} else {
					isPause = false;
					builder.dismiss();
					Message msg = new Message();
					msg.what = 1;
					handlerStopTime.sendMessage(msg);
				}
			}
		});
		builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
		builder.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
								 KeyEvent event) {

				return flag;
			}
		});
		builder.show();
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			isPause = true;
			showTimeOutDialog(true, "1");
			Message msg = new Message();
			msg.what = 0;
			handlerStopTime.sendMessage(msg);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = 0;
		handlerStopTime.sendMessage(msg);
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = 1;
		handlerStopTime.sendMessage(msg);
		super.onResume();
	}

	private void startTime() {
		if (timer == null) {
			timer = new Timer();
		}
		if (timerTask == null) {
			timerTask = new TimerTask() {

				@Override
				public void run() {
					Message msg = new Message();
					msg.what = 0;
					handlerTime.sendMessage(msg);
				}
			};
		}
		if (timer != null && timerTask != null) {
			timer.schedule(timerTask, 0, 1000);
		}
	}

	private void stopTime(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		if(timerTask!=null){
			timerTask.cancel();
			timerTask=null;
		}
	}

}
