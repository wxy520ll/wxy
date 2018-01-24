package cn.net.xinyi.xmjt.activity.Plate.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.model.PlateInfoModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.View.SwipeListView;

public class PlateListAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	private List<PlateInfoModle> listitems;
	private Context mContext;
	private SwipeListView mSwipeListView;

	public PlateListAdapter(Context context, List<PlateInfoModle> listitems,SwipeListView listView) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.listitems = listitems;
		this.mSwipeListView = listView;
	}

	public void setItemList(List list) {
		this.listitems = list;
	}

	@Override
	public int getCount() {
		// How many items are in the data set represented by this Adapter.
		// 在此适配器中所代表的数据集中的条目数
		return listitems.size();
	}

	@Override
	public Object getItem(int position) {
		// Get the data item associated with the specified position in the data
		// set.
		// 获取数据集中与指定索引对应的数据项
		return position;
	}

	@Override
	public long getItemId(int position) {
		// Get the row id associated with the specified position in the list.
		// 获取在列表中与指定索引对应的行id
		return position;
	}

	public boolean delPlateInfo(int position) {
		boolean mFlag = false;
		DBOperation dbo = new DBOperation(mContext);
		int id = listitems.get(position).getId();
		String imagePath = listitems.get(position).getPlateImage();
		String thumbPath = listitems.get(position).getPlateThumb();
		mFlag = dbo.delPlateInfo(id);
		dbo.clossDb();

		//删除本地图片
		if(mFlag){
			File plateImage = new File(imagePath);
			File plateImageThub  = new File(thumbPath);
			if(plateImage.exists()){
				plateImage.delete();
			}
			if(plateImageThub.exists()){
				plateImageThub.delete();
			}
		}

		return mFlag;
	}

	// Get a View that displays the data at the specified position in the data
	// set.
	// 获取一个在数据集中指定索引的视图来显示数据
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//final int mPosition = position;
		//SwipeListView mSwipeListView = (SwipeListView) parent;
		PlateInfoModle mPlateInfo = listitems.get(position);

		ViewHolder holder = null;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(R.layout.plate_list_item, null);

			holder.plateImage = (ImageView) convertView
					.findViewById(R.id.plate_detail_image);
			holder.plateNumber = (TextView) convertView
					.findViewById(R.id.plate_detail_number);
			holder.time = (TextView) convertView
					.findViewById(R.id.plate_detail_time);
			holder.cellid = (TextView) convertView
					.findViewById(R.id.plate_detail_cellid);
			holder.gps = (TextView) convertView
					.findViewById(R.id.plate_detail_gps);
			holder.bAction1 = (Button) convertView
					.findViewById(R.id.btn_item_del);
			holder.bAction2 = (Button) convertView
					.findViewById(R.id.btn_item_edit);
			holder.uploadstatus = (Button) convertView
					.findViewById(R.id.plate_detail_status);
			holder.uploadstatustext = (TextView) convertView
					.findViewById(R.id.plate_detail_statustext);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		((SwipeListView) parent).recycle(convertView, position);

		String thumbnailStr = listitems.get(position).getPlateThumb();
		if (thumbnailStr == null)
			thumbnailStr = "";
		File thumbnailFile = new File(thumbnailStr);
		if (thumbnailFile.exists())
			holder.plateImage.setBackgroundDrawable(BaseUtil
					.getLocalBitmap(thumbnailStr));
		else
			holder.plateImage.setBackgroundResource(R.drawable.publish_fail);

		holder.time.setText(listitems.get(position).getTime());
		holder.plateNumber.setText(listitems.get(position).getPlateNumber());
		holder.gps.setText(listitems.get(position).getLatitude() + ","
				+ listitems.get(position).getLongitude());
		holder.cellid.setText(listitems.get(position).getCellID() + ","
				+ listitems.get(position).getCellLocationCode());
		if (listitems.get(position).getIsupdate().equals(String.valueOf(PlateInfoModle.ISUPDATE_FINNISH_SERVER))) {
			holder.uploadstatus.setVisibility(View.GONE);
			holder.uploadstatustext.setVisibility(View.GONE);
		}else{
			holder.uploadstatus.setVisibility(View.GONE);
			holder.uploadstatustext.setVisibility(View.GONE);
		}


		holder.bAction1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(mContext)
						.setTitle(R.string.msg_delete_confirm_title)
						.setMessage(R.string.msg_delete_confirm)
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mSwipeListView.closeAnimate(position);
										mSwipeListView.closeOpenedItems();
										mSwipeListView.dismiss(position);
									}
								})
						.setNegativeButton(R.string.cancle,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mSwipeListView.closeAnimate(position);
										mSwipeListView.closeOpenedItems();
									}
								}).show();

			}
		});
		return convertView;
	}

	static class ViewHolder {
		public ImageView plateImage;
		public TextView plateNumber;
		public TextView time;
		public TextView gps;
		public TextView cellid;
		public Button uploadstatus;
		public TextView uploadstatustext;
		public Button bAction1;
		public Button bAction2;
	}
}