package com.aspirecn.corpsocial.common.ui.component.imageSelect.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.common.ui.component.imageSelect.domain.ImageFloder;
import com.aspiren.corpsocial.R;


public class ImageSelectorActivity extends Activity implements ListImageDirPopupWindow.OnImageDirSelected
{
	private ProgressDialog mProgressDialog;

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */

	private GridView mGirdView;
	private MyAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();
	private HashMap<String,ImageFloder> mHashCach = new HashMap<String, ImageFloder>();
	/**
	 * 当前的ImageFloader
	 */
	private ImageFloder currentImageFloader;
	private List<String> mCurrentFileNames = new ArrayList<String>();
	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	private RelativeLayout mBottomLy;

	private TextView mChooseDir;
	private TextView mImageCount;

	private int mScreenHeight;

	private ListImageDirPopupWindow mListImageDirPopupWindow;
	/**
	 * 所有文件的对象
	 */
	private ImageFloder mAllPic = new ImageFloder();

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			if(msg.what==12){
				mGirdView.setSelection(0);
				return;
			}
			mProgressDialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			initListDirPopupWindw();
		}
	};

	/**
	 * 为View绑定数据
	 */
	private void data2View()
	{
		if (currentImageFloader == null)
		{
			Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mCurrentFileNames.clear();
		mCurrentFileNames.addAll(currentImageFloader.getFileNames());
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(getApplicationContext(), mCurrentFileNames,
				R.layout.common_imageselector_grid_item,currentImageFloader.getDir());
		mGirdView.setAdapter(mAdapter);
		mImageCount.setText(currentImageFloader.getCount() + "张");
	};

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.common_imageselector_list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_imageselector_activity);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;

		initView();
		getImages();
		initEvent();

	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = ImageSelectorActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_ADDED+" desc");

				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					String fileName = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					mAllPic.setCount(mAllPic.getCount() + 1);
					if(mAllPic.getFirstImagePath()==null){
						mAllPic.setFirstImagePath(path);
					}
					//防止文件目录不同但是文件夹名相同的问题
					String fileDirname = dirPath.substring(dirPath.lastIndexOf("/"));
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(fileDirname))
					{
						imageFloder = mHashCach.get(fileDirname);
						if(dirPath.equals(imageFloder.getDir())) {
							imageFloder.getFileNames().add(fileName);
							mAllPic.getFileNames().add(path);
						}
						imageFloder.setCount(imageFloder.getCount()+1);
						if(imageFloder.getCount()>mPicsSize){
							mPicsSize = imageFloder.getCount();
							currentImageFloader = imageFloder;
						}
						continue;
					} else
					{
						mDirPaths.add(fileDirname);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
						imageFloder.setCount(1);
						imageFloder.getFileNames().add(fileName);
						mAllPic.getFileNames().add(path);
						if(currentImageFloader==null){
							currentImageFloader = imageFloder;
						}
						mHashCach.put(fileDirname, imageFloder);
					}
					mImageFloders.add(imageFloder);
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;
				currentImageFloader = mAllPic;
				mImageFloders.add(0,mAllPic);
				mAllPic.setDir("所有图片");
				mAllPic.setIsChecked(true);
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}

	/**
	 * 初始化View
	 */
	private void initView()
	{
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);

		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);

	}

	private void initEvent()
	{
		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */
		mBottomLy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListImageDirPopupWindow
						.setAnimationStyle(R.style.anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}
		});
	}

	@Override
	public void selected(ImageFloder floder)
	{
		for(ImageFloder f:mImageFloders){
			f.setIsChecked(false);
		}
		floder.setIsChecked(true);
		currentImageFloader = floder;
		mCurrentFileNames.clear();
		mCurrentFileNames.addAll(currentImageFloader.getFileNames());
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
//		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
//				R.layout.common_imageselector_grid_item);
//		mGirdView.setAdapter(mAdapter);
		if(mAdapter!= null) {
			mAdapter.updateDirPath(currentImageFloader.getDir());
			mAdapter.notifyDataSetChanged();
			mHandler.sendEmptyMessage(12);
		}
		// mAdapter.notifyDataSetChanged();
		mImageCount.setText(floder.getCount() + "张");
		mChooseDir.setText(floder.getName());
		mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();

	}
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	mProgressDialog.dismiss();
	mHashCach.clear();
	mHashCach = null;
	currentImageFloader = null;
	mImageFloders = null;
}
}
