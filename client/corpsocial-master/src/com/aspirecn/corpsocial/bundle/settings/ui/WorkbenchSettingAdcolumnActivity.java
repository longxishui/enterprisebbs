package com.aspirecn.corpsocial.bundle.settings.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.settings.event.DownloadAdcolumnEvent;
import com.aspirecn.corpsocial.bundle.settings.event.DownloadAdcolumnRespEvent;
import com.aspirecn.corpsocial.bundle.settings.event.GetAdcolumnListEvent;
import com.aspirecn.corpsocial.bundle.settings.listener.DownloadAdcolumnRespSubject;
import com.aspirecn.corpsocial.bundle.settings.repository.vo.AdcolumnListVO;
import com.aspirecn.corpsocial.bundle.settings.utils.AdcolumnUtils;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.StringUtils;

/**
 * Created by chenziqiang on 15-3-24.
 */
@EActivity(R.layout.setting_ui_adcolumn_activity)
public class WorkbenchSettingAdcolumnActivity extends EventFragmentActivity
        implements DownloadAdcolumnRespSubject.DownloadAdcolumnRespEventListener, PullToRefreshBase.OnRefreshListener2<ListView> {

    private static final int COUNT = 10;
    @ViewById(R.id.setting_adcolumn_list)
    PullToRefreshListView adcolumnList;
    @ViewById(R.id.adcolumn_list_progressbar)
    ProgressBar bar;
    private List<AdcolumnListVO> adcolumnListVOs;
    private Context mContext;
    private AdcolumnAdepter adepter;

    @AfterViews
    void doAfterView() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_about_mobile)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        adcolumnListVOs = new ArrayList<AdcolumnListVO>();
        mContext = this;
        adepter = new AdcolumnAdepter(mContext, adcolumnListVOs);
        adcolumnList.setAdapter(adepter);
        adcolumnList.setMode(PullToRefreshBase.Mode.BOTH);
        adcolumnList.setOnRefreshListener(this);

        DownloadData();
    }

    //下载数据
    @Background
    void DownloadData() {
        DownloadAdcolumnEvent event = new DownloadAdcolumnEvent();
        event.setLastModieTime(AdcolumnUtils.getAdcolumnLastTime());
        uiEventHandleFacade.handle(event);
    }

    //下载完成监听
    @Override
    public void onDownloadAdcolumnRespEventListener(DownloadAdcolumnRespEvent event) {
        progressbarDimss();
        getData(LoadType.PULL_DOWN, 0, COUNT);
    }

    //消除转圈
    @UiThread
    void progressbarDimss() {
        bar.setVisibility(View.GONE);
    }

    @UiThread
    void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
    }

    //拉取本地数据
    @Background
    void getData(LoadType loadType, int index, int count) {
        GetAdcolumnListEvent event1 = new GetAdcolumnListEvent();
        event1.setIndex(index);
        event1.setCount(count);
        List<AdcolumnListVO> vos = (List<AdcolumnListVO>) uiEventHandleFacade.handle(event1);

        if (vos != null && vos.size() > 0) {
            showData(loadType, vos);
        } else {
            if (loadType == LoadType.PULL_DOWN) {
                showToast("数据读取失败，请检查网络");
            }
            adcolumnListlistRefreshComplete();
        }


    }

    @UiThread
    void adcolumnListlistRefreshComplete() {
        adcolumnList.onRefreshComplete();
    }

    @UiThread
    void showData(LoadType loadType, List<AdcolumnListVO> vos) {
        if (loadType != LoadType.PULL_UP) {
            adcolumnListVOs.clear();
        }
        adcolumnListVOs.addAll(vos);
        adepter.notifyDataSetChanged();

        if (loadType != LoadType.INIT) {
            adcolumnList.onRefreshComplete();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
        DownloadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
        getData(LoadType.PULL_UP, adcolumnListVOs.size(), COUNT);
    }

    public enum LoadType {
        INIT, PULL_DOWN, PULL_UP
    }

    public class AdcolumnAdepter extends BaseAdapter {
        private Context mContext;
        private List<AdcolumnListVO> adcolumnListVOs;
        private ViewHolder holder;

        public AdcolumnAdepter(Context mContext, List<AdcolumnListVO> adcolumnListVOs) {
//            super();
            this.mContext = mContext;
            this.adcolumnListVOs = adcolumnListVOs;
        }

        @Override
        public int getCount() {
            return adcolumnListVOs.size();
        }

        @Override
        public AdcolumnListVO getItem(int i) {
            return adcolumnListVOs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int postion, View view, ViewGroup viewGroup) {
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.setting_adcolumn_item, null);
                holder.adcolumnView = (LinearLayout) view.findViewById(R.id.sdcolumn_item_view);
                holder.headImageView = (ImageView) view.findViewById(R.id.adcolumn_headimg);
                holder.adcolumnTittle = (TextView) view.findViewById(R.id.adcolumn_item_name);
                holder.adcolumnTime = (TextView) view.findViewById(R.id.adcolumn_item_time);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (!StringUtils.isBlank(adcolumnListVOs.get(postion).getIconUrl()))
//                ImageDownloadUtil.INSTANCE.showImage(adcolumnListVOs.get(postion).getIconUrl(), holder.headImageView);
                ImageLoader.getInstance().displayImage(adcolumnListVOs.get(postion).getIconUrl(), holder.headImageView);
            holder.adcolumnTittle.setText(adcolumnListVOs.get(postion).getTitle());
            holder.adcolumnTime.setText(adcolumnListVOs.get(postion).getDescri());
            holder.adcolumnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick(postion);
                }
            });
            return view;
        }

        private void itemClick(int i) {
            Intent intent = new Intent(mContext, WorkbenchSettingAdcolumnDetailActivity_.class);
            intent.putExtra(AdcolumnUtils.ADCOLUMNID, adcolumnListVOs.get(i).getAdcolumnId());
            mContext.startActivity(intent);
        }

        private final class ViewHolder {
            ImageView headImageView;// 新闻图标
            TextView adcolumnTittle;// 新闻名字
            TextView adcolumnTime;// 新闻时间
            LinearLayout adcolumnView;
        }

    }
}
