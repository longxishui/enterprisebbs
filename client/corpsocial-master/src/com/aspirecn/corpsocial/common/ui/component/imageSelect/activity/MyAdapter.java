package com.aspirecn.corpsocial.common.ui.component.imageSelect.activity;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.aspirecn.corpsocial.common.ui.component.imageSelect.util.CommonAdapter;
import com.aspirecn.corpsocial.common.ui.component.imageSelect.util.ViewHolder;
import com.aspiren.corpsocial.R;


public class MyAdapter extends CommonAdapter<String>
{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();

	/**
	 * 文件夹路径,如果文件加路径为""，则表示item为全路径
	 */
	private String mDirPath;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath)
	{
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
	}
	public List<String> getmSelectedImage(){
		return mSelectedImage;
	}
	/**
	 * 更新文件夹
	 * @param dirPath
	 */
public void updateDirPath(String dirPath){
	this.mDirPath = dirPath;
}
	@Override
	public void convert(final ViewHolder helper, final String item)
	{
		//设置no_pic
		helper.setImageResource(R.id.grid_item_image, R.drawable.component_noimg_small);
		//设置no_selected
				helper.setImageResource(R.id.grid_item_select,
						R.drawable.common_checkbox_select_close2);
		//设置图片
		helper.setImageByUrl(R.id.grid_item_image, mDirPath.equals("所有图片")?item:mDirPath + "/" + item);

		final ImageView mImageView = helper.getView(R.id.grid_item_image);
		final ImageView mSelect = helper.getView(R.id.grid_item_select);
		
		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v)
			{

				// 已经选择过该图片
				if (mSelectedImage.contains(mDirPath.equals("所有图片")?item:mDirPath + "/" + item))
				{
					mSelectedImage.remove(mDirPath.equals("所有图片")?item:mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.common_checkbox_select_close2);
					mImageView.setColorFilter(null);
				} else
				// 未选择该图片
				{
					mSelectedImage.add(mDirPath.equals("所有图片")?item:mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.common_checkbox_select_open2);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}

			}
		});
		
		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath.equals("所有图片")?item:mDirPath + "/" + item))
		{
			mSelect.setImageResource(R.drawable.common_checkbox_select_open2);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}
}
