package cn.net.xinyi.xmjt.activity.Plate;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.model.PlateInfoModle;

public class PlateDetailViewActivity extends Activity implements
		OnClickListener {
	private static final String TAG = "PlateDetailViewActivity";
	private ImageView iv_plate_image;
	private TextView tv_plate_info_gps;
	private TextView tv_plate_info_time;
	private TextView tv_plate_info_callid;
	private TextView tv_plate_info_address;
	private TextView tv_plate_info_color;
	private TextView tv_plate_info_number;
	private TextView tv_plate_info_accuracy;
	private TextView tv_plate_info_deviceid;
	private TextView tv_plate_info_user;
	private PlateInfoModle plateInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plate_detail_view);
		initResource();

		getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
		getActionBar().setTitle("车牌详细信息");
		getActionBar().setHomeButtonEnabled(true);
	}

	private void initResource() {
		iv_plate_image = (ImageView) findViewById(R.id.plate_image);
		tv_plate_info_gps = (TextView) findViewById(R.id.plate_info_gps);
		tv_plate_info_time = (TextView) findViewById(R.id.plate_info_time);
		tv_plate_info_callid = (TextView) findViewById(R.id.plate_info_callid);
		tv_plate_info_address = (TextView) findViewById(R.id.plate_info_address);
		tv_plate_info_number = (TextView) findViewById(R.id.plate_info_number);
		tv_plate_info_color = (TextView) findViewById(R.id.plate_info_color);
		tv_plate_info_accuracy = (TextView) findViewById(R.id.plate_info_accuracy);
		tv_plate_info_deviceid = (TextView) findViewById(R.id.plate_info_deviceid);
		tv_plate_info_user = (TextView) findViewById(R.id.plate_info_user);

		plateInfo = (PlateInfoModle) getIntent().getSerializableExtra(PlateListActivity.KEY_PLATE_INFO);
		if (plateInfo != null) {
			//车牌图片
			if (!plateInfo.getPlateImage().isEmpty()) {
				File captureImageFile = new File(plateInfo.getPlateImage());
				if (captureImageFile.exists()) {
					iv_plate_image.setImageURI(Uri.fromFile(captureImageFile));
				}
			}
			tv_plate_info_gps.setText(plateInfo.getLatitude()+","+plateInfo.getLongitude());
			tv_plate_info_time.setText(plateInfo.getTime());
			tv_plate_info_callid.setText(plateInfo.getCellID()+","+plateInfo.getCellLocationCode());
			tv_plate_info_address.setText(plateInfo.getAddress());
			tv_plate_info_number.setText(plateInfo.getPlateNumber());
			tv_plate_info_color.setText(plateInfo.getPlateColor());
			tv_plate_info_accuracy.setText(plateInfo.getAccuracy());
			tv_plate_info_deviceid.setText(plateInfo.getDeviceid());
			tv_plate_info_user.setText(plateInfo.getUserName());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
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
