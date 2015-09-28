package com.aspirecn.corpsocial.bundle.im.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.domain.GroupPictureWall;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.im_group_picture_wall_activity)
public class GroupPictureWallActivity extends EventFragmentActivity {
    static final int MENU_SET_MODE = 0;
    @ViewById(R.id.group_picture_nodata_tv)
    TextView noDataTv;
    private PullToRefreshGridView mPullRefreshGridView;
    private GridView mGridView;
    private GroupPictureWall groupPictureWall;
    private GroupPictureWallAdapter mAdapter;

    @AfterViews
    void doAfterViews() {
        initViews();
    }

    @Background
    void initViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_pirturewall_title)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
        mGridView = mPullRefreshGridView.getRefreshableView();
        mPullRefreshGridView.setMode(Mode.PULL_FROM_END);
        showView();

    }

    @UiThread
    void showView() {
//        TextView tv = new TextView(this);
//        tv.setGravity(Gravity.CENTER);
//        tv.setText("本群还没有上传图片，赶快体验一下吧！");
//        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(0,0);
//        params.width=LinearLayout.LayoutParams.WRAP_CONTENT;
//        params.height=LinearLayout.LayoutParams.WRAP_CONTENT;
//        tv.setLayoutParams(params);
//        Drawable drawable;
//        drawable=getResources().getDrawable(R.drawable.no_chat_msg_tips1);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        tv.setCompoundDrawables(null,drawable,null,null);


        mPullRefreshGridView.setEmptyView(noDataTv);

        Bundle extras = getIntent().getExtras();
        String chatId = extras.getString("chatId");

        groupPictureWall = new GroupPictureWall();
        groupPictureWall.setChatId(chatId);
        groupPictureWall.loadPictures(chatId, 0, 12);


        mAdapter = new GroupPictureWallAdapter(this,
                groupPictureWall);
        mGridView.setAdapter(mAdapter);

        mPullRefreshGridView
                .setOnRefreshListener(new OnRefreshListener2<GridView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<GridView> refreshView) {
                        // 暂未使用
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<GridView> refreshView) {
//						Toast.makeText(GroupPictureWallActivity.this,
//								"Pull Up!", Toast.LENGTH_SHORT).show();
                        new GetDataTask().execute();
                    }

                });
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
                String chatId = groupPictureWall.getChatId();
                int pictureSize = groupPictureWall.doGetPictureSize();
                groupPictureWall.loadPictures(chatId, pictureSize, 12);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshGridView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

}
