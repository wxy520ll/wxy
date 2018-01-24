package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;


import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.model.AnSwerInfo;
import cn.net.xinyi.xmjt.model.SaveQuestionInfo;
import cn.net.xinyi.xmjt.utils.ConstantUtil;
import cn.net.xinyi.xmjt.utils.UI;

public class OneCameraExamSubmitAdapter extends PagerAdapter {

	OneCameraExamAty mContext;
	// 传递过来的页面view的集合
	List<View> viewItems;
	// 每个item的页面view
	View convertView;
	// 传递过来的所有数据
	List<AnSwerInfo> dataItems;

	String imgServerUrl="";

	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> mapClick = new HashMap<Integer, Boolean>();
	private Map<Integer, String> mapMultiSelect = new HashMap<Integer, String>();

	boolean isClick=false;

	boolean isNext = false;


	StringBuffer answer1=new StringBuffer();



	String isCorrect= ConstantUtil.isCorrect;//1对，0错

	int errortopicNum=0;

	boolean resultA=false;
	boolean resultB=false;
	boolean resultC=false;
	boolean resultD=false;
	String result="";


	public OneCameraExamSubmitAdapter(OneCameraExamAty context, List<View> viewItems, List<AnSwerInfo> dataItems, String imgServerUrl) {
		mContext = context;
		this.viewItems = viewItems;
		this.dataItems = dataItems;
		this.imgServerUrl = imgServerUrl;

	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewItems.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container,final int position) {
		final ViewHolder holder = new ViewHolder();
		convertView = viewItems.get(position);
		holder.questionType = (TextView) convertView.findViewById(R.id.activity_prepare_test_no);
		holder.question = (TextView) convertView.findViewById(R.id.activity_prepare_test_question);
		holder.previousBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_upLayout);
		holder.nextBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_nextLayout);
		holder.nextText = (TextView) convertView.findViewById(R.id.menu_bottom_nextTV);
		holder.totalText = (TextView) convertView.findViewById(R.id.activity_prepare_test_totalTv);
		holder.nextImage = (ImageView) convertView.findViewById(R.id.menu_bottom_nextIV);
		holder.layoutA=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_a);
		holder.layoutB=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_b);
		holder.layoutC=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_c);
		holder.layoutD=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_d);
		holder.ivA=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_a);
		holder.ivB=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_b);
		holder.ivC=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_c);
		holder.ivD=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_d);
		holder.tvA=(TextView) convertView.findViewById(R.id.vote_submit_select_text_a);
		holder.tvB=(TextView) convertView.findViewById(R.id.vote_submit_select_text_b);
		holder.tvC=(TextView) convertView.findViewById(R.id.vote_submit_select_text_c);
		holder.tvD=(TextView) convertView.findViewById(R.id.vote_submit_select_text_d);
		holder.ivA_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_a_);
		holder.ivB_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_b_);
		holder.ivC_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_c_);
		holder.ivD_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_d_);


		holder.totalText.setText(position+1+"/"+dataItems.size());



		if(null==dataItems.get(position).getOPTIONA()){
			holder.layoutA.setVisibility(View.GONE);
		}if(null==dataItems.get(position).getOPTIONB()){
			holder.layoutB.setVisibility(View.GONE);
		}if(null==dataItems.get(position).getOPTIONC()){
			holder.layoutC.setVisibility(View.GONE);
		}if(null==dataItems.get(position).getOPTIOND()){
			holder.layoutD.setVisibility(View.GONE);
		}

		//判断是否文字图片题目
		//文字题目
		holder.ivA_.setVisibility(View.GONE);
		holder.ivB_.setVisibility(View.GONE);
		holder.ivC_.setVisibility(View.GONE);
		holder.ivD_.setVisibility(View.GONE);
		holder.tvA.setVisibility(View.VISIBLE);
		holder.tvB.setVisibility(View.VISIBLE);
		holder.tvC.setVisibility(View.VISIBLE);
		holder.tvD.setVisibility(View.VISIBLE);
		holder.tvA.setText("A." + dataItems.get(position).getOPTIONA());
		holder.tvB.setText("B." + dataItems.get(position).getOPTIONB());
		holder.tvC.setText("C." + dataItems.get(position).getOPTIONC());
		holder.tvD.setText("D." + dataItems.get(position).getOPTIOND());

		//判断题型
		if(dataItems.get(position).getQUESTIONTYPE().equals("0")){
			//单选题
			holder.questionType.setText("[单选题]");
			holder.question.setText(dataItems.get(position).getQUESTIONNAME());


			holder.layoutA.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//去除之前的选择项
					if (result.equals("B")){
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("C")){
						holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("D")){
						holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					}

					if (result.isEmpty()||result!="A"){
						result="A";
						holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvA.setTextColor(Color.parseColor("#61bc31"));
					}else {
						result="";
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					}

				}
			});
			holder.layoutB.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//去除之前的选择项
					if (result.equals("A")){
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("C")){
						holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("D")){
						holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					}

					if (result.isEmpty()||result!="B"){
						result="B";
						holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvB.setTextColor(Color.parseColor("#61bc31"));
					}else {
						result="";
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					}
				}
			});
			holder.layoutC.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//去除之前的选择项
					if (result.equals("A")){
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("B")){
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("D")){
						holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					}


					if (result.isEmpty()||result!="C"){
						result="C";
						holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvC.setTextColor(Color.parseColor("#61bc31"));
					} else {
						result="";
						holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					}

				}
			});
			holder.layoutD.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//去除之前的选择项
					if (result.equals("A")){
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("B")){
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("C")){
						holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					}

					if (result.isEmpty()||result!="D"){
						result="D";
						holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvD.setTextColor(Color.parseColor("#61bc31"));
					}else {
						result="";
						holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					}
				}
			});

		}else if(dataItems.get(position).getQUESTIONTYPE().equals("1")){
			holder.questionType.setText("[多选题]");
			//多选题
			holder.question.setText(dataItems.get(position).getQUESTIONNAME());

			holder.layoutA.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!resultA){
						resultA=true;
						holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvA.setTextColor(Color.parseColor("#61bc31"));
					}else {
						resultA=false;
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					}
				}
			});
			holder.layoutB.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!resultB){
						resultB=true;
						holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvB.setTextColor(Color.parseColor("#61bc31"));
					}else {
						resultB=false;
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					}
//					if(dataItems.get(position).getCORRECTANSWER().contains("B")){
//						holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
//						holder.tvB.setTextColor(Color.parseColor("#61bc31"));
//						isCorrect=ConstantUtil.isCorrect;
//						if(position==viewItems.size()-1){
//							answerLast.append("B");
//						}else{
//							answer.append("B");
//						}
//					}else{
//						isCorrect=ConstantUtil.isError;
//						mapMultiSelect.put(position, isCorrect);
//						errortopicNum+=1;
//
//						map.put(position, true);
//						holder.ivB.setImageResource(R.drawable.ic_practice_test_wrong);
//						holder.tvB.setTextColor(Color.parseColor("#d53235"));
//						//显示正确选项
//						if(dataItems.get(position).getCORRECTANSWER().contains("A")){
//							holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
//							holder.tvA.setTextColor(Color.parseColor("#61bc31"));
//						}if(dataItems.get(position).getCORRECTANSWER().contains("B")){
//							holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
//							holder.tvB.setTextColor(Color.parseColor("#61bc31"));
//						}if(dataItems.get(position).getCORRECTANSWER().contains("C")){
//							holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
//							holder.tvC.setTextColor(Color.parseColor("#61bc31"));
//						}if(dataItems.get(position).getCORRECTANSWER().contains("D")){
//							holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
//							holder.tvD.setTextColor(Color.parseColor("#61bc31"));
//						}
//
//						//保存数据
//						SaveQuestionInfo questionInfo=new SaveQuestionInfo();
//						questionInfo.setQuestionId(dataItems.get(position).getId());
//						questionInfo.setQuestionType(dataItems.get(position).getQUESTIONTYPE());
//						questionInfo.setRealAnswer(dataItems.get(position).getCORRECTANSWER());
//						questionInfo.setIs_correct(isCorrect);
//						questionInfo.setScore(dataItems.get(position).getSCORE());
//						mContext.questionInfos.add(questionInfo);
//						dataItems.get(position).setISSELECT("0");
//					}
//					resultB="B";
				}
			});
			holder.layoutC.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!resultC){
						resultC=true;
						holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvC.setTextColor(Color.parseColor("#61bc31"));
					}else {
						resultC=false;
						holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					}
				}
			});
			holder.layoutD.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!resultD){
						resultD=true;
						holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvD.setTextColor(Color.parseColor("#61bc31"));
					}else {
						resultD=false;
						holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					}
				}
			});
		}else{
			//判断题
			holder.questionType.setText("[判断题]");
			holder.question.setText(dataItems.get(position).getQUESTIONNAME());
			holder.layoutA.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//去除之前的选择项
					if (result.equals("B")){
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("C")){
						holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("D")){
						holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					}

					if (result.isEmpty()||result!="A"){
						result="A";
						holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvA.setTextColor(Color.parseColor("#61bc31"));
					}else {
						result="";
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					}
				}
			});
			holder.layoutB.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//去除之前的选择项
					if (result.equals("A")){
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("C")){
						holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					}else if (result.equals("D")){
						holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					}

					if (result.isEmpty()||result!="B"){
						result="B";
						holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvB.setTextColor(Color.parseColor("#61bc31"));
					}else {
						result="";
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					}
				}
			});
		}

		ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#2b89e9"));

		SpannableStringBuilder builder1 = new SpannableStringBuilder(holder.question.getText().toString());
		builder1.setSpan(blueSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.question.setText(builder1);

		// 最后一页修改"下一步"按钮文字
		if (position == viewItems.size() - 1) {
			holder.nextText.setText("提交");
			holder.nextImage.setImageResource(R.drawable.vote_submit_finish);
		}
		holder.previousBtn.setOnClickListener(new LinearOnClickListener(position - 1, false,position,holder));
		holder.nextBtn.setOnClickListener(new LinearOnClickListener(position + 1, true,position,holder));
		container.addView(viewItems.get(position));
		return viewItems.get(position);
	}

	/**
	 * @author  设置上一步和下一步按钮监听
	 *
	 */
	class LinearOnClickListener implements OnClickListener {

		private int mPosition;
		private int mPosition1;
		private boolean mIsNext;
		private ViewHolder viewHolder;

		public LinearOnClickListener(int position, boolean mIsNext,int position1,ViewHolder viewHolder) {
			mPosition = position;
			mPosition1 = position1;
			this.viewHolder = viewHolder;
			this.mIsNext = mIsNext;
		}

		@Override
		public void onClick(View v) {
			//单选
			if(dataItems.get(mPosition1).getQUESTIONTYPE().equals("0")){
				if (result.isEmpty()){
					Toast.makeText(mContext, "请选择选项", Toast.LENGTH_SHORT).show();
					return;
				}
				//与正确答案比较正确选项
				if(dataItems.get(mPosition1).getCORRECTANSWER().contains("A") &&result.contains("A")){
					isCorrect=ConstantUtil.isCorrect;
				}else if(dataItems.get(mPosition1).getCORRECTANSWER().contains("B") &&result.contains("B")){
					isCorrect=ConstantUtil.isCorrect;
				}else if(dataItems.get(mPosition1).getCORRECTANSWER().contains("C") &&result.contains("C")){
					isCorrect=ConstantUtil.isCorrect;
				}else if(dataItems.get(mPosition1).getCORRECTANSWER().contains("D") &&result.contains("D")){
					isCorrect=ConstantUtil.isCorrect;
				}else {
					isCorrect=ConstantUtil.isError;
					errortopicNum+=1;
					UI.toast(mContext,"提示：答题错误");
				}

				//保存数据
				SaveQuestionInfo questionInfo=new SaveQuestionInfo();
				questionInfo.setQuestionId(dataItems.get(mPosition1).getId());
				questionInfo.setQuestionType(dataItems.get(mPosition1).getQUESTIONTYPE());
				questionInfo.setRealAnswer(dataItems.get(mPosition1).getCORRECTANSWER());
				questionInfo.setScore(dataItems.get(mPosition1).getSCORE());
				questionInfo.setIs_correct(isCorrect);
				mContext.questionInfos.add(questionInfo);
				dataItems.get(mPosition1).setISSELECT("0");
				result="";
				mContext.setCurrentView(mPosition);
				if (dataItems.size()==mPosition){
					mContext.uploadExamination(errortopicNum);
				}
			}else if(dataItems.get(mPosition1).getQUESTIONTYPE().equals("1")){

				String ssStr=dataItems.get(mPosition1).getCORRECTANSWER();
				ssStr=ssStr.replace("|", "");
				if(resultA){
					answer1.append("A");
				}
				if(resultB){
					answer1.append("B");
				}
				if(resultC){
					answer1.append("C");
				}
				if(resultD){
					answer1.append("D");
				}

				if (answer1.length()<=0){
					Toast.makeText(mContext, "请选择选项", Toast.LENGTH_SHORT).show();
					return;
				}
				if(answer1.toString().equals(ssStr)) {
					isCorrect = ConstantUtil.isCorrect;
				}else {
					errortopicNum+=1;
					isCorrect=ConstantUtil.isError;
					UI.toast(mContext,"提示：答题错误");
				}

				//清除答案
				answer1.delete(0, answer1.length());
				resultA=false;
				resultB=false;
				resultC=false;
				resultD=false;
				//保存数据
				SaveQuestionInfo questionInfo = new SaveQuestionInfo();
				questionInfo.setQuestionId(dataItems.get(mPosition1).getId());
				questionInfo.setQuestionType(dataItems.get(mPosition1).getQUESTIONTYPE());
				questionInfo.setRealAnswer(dataItems.get(mPosition1).getCORRECTANSWER());
				questionInfo.setScore(dataItems.get(mPosition1).getSCORE());
				questionInfo.setIs_correct(isCorrect);
				mContext.questionInfos.add(questionInfo);
				dataItems.get(mPosition1).setISSELECT("0");
				mContext.setCurrentView(mPosition);
				if (dataItems.size()==mPosition){
					mContext.uploadExamination(errortopicNum);
				}

			}else {
				if (result.isEmpty()){
					Toast.makeText(mContext, "请选择选项", Toast.LENGTH_SHORT).show();
					return;
				}
				//显示正确选项
				if(dataItems.get(mPosition1).getCORRECTANSWER().contains("A") &&result.contains("A")){
					isCorrect=ConstantUtil.isCorrect;
				}else if(dataItems.get(mPosition1).getCORRECTANSWER().contains("B") &&result.contains("B")){
					isCorrect=ConstantUtil.isCorrect;
				}else {
					isCorrect=ConstantUtil.isError;
					errortopicNum+=1;
					UI.toast(mContext,"提示：答题错误");
				}
				//保存数据
				SaveQuestionInfo questionInfo=new SaveQuestionInfo();
				questionInfo.setQuestionId(dataItems.get(mPosition1).getId());
				questionInfo.setQuestionType(dataItems.get(mPosition1).getQUESTIONTYPE());
				questionInfo.setRealAnswer(dataItems.get(mPosition1).getCORRECTANSWER());
				questionInfo.setScore(dataItems.get(mPosition1).getSCORE());
				questionInfo.setIs_correct(isCorrect);
				mContext.questionInfos.add(questionInfo);
				dataItems.get(mPosition1).setISSELECT("0");
				result="";
				mContext.setCurrentView(mPosition);
				if (dataItems.size()==mPosition){
					mContext.uploadExamination(errortopicNum);
				}
			}
		}
	}

	@Override
	public int getCount() {
		if (viewItems == null)
			return 0;
		return viewItems.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	//错题数
	public int errorTopicNum(){
		if(errortopicNum!=0){
			return errortopicNum;
		}
		return 0;
	}

	public class ViewHolder {
		TextView questionType;
		TextView question;
		LinearLayout previousBtn, nextBtn;
		TextView nextText;
		TextView totalText;
		ImageView nextImage;
		LinearLayout layoutA;
		LinearLayout layoutB;
		LinearLayout layoutC;
		LinearLayout layoutD;
		ImageView ivA;
		ImageView ivB;
		ImageView ivC;
		ImageView ivD;
		TextView tvA;
		TextView tvB;
		TextView tvC;
		TextView tvD;
		ImageView ivA_;
		ImageView ivB_;
		ImageView ivC_;
		ImageView ivD_;
	}

}
