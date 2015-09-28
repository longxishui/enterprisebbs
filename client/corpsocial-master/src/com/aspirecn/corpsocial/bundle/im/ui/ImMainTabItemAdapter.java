package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.im.ui.ImMainTabItem.ItemType;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.config.IMIconConfig;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.util.DateUtils;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

public class ImMainTabItemAdapter extends BaseAdapter {

    private List<ImMainTabItem> imMainTabItems;

    private LayoutInflater mInflater;
    private Context mContext;

    public ImMainTabItemAdapter(Context context) {
        setImMainTabItems(null);
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public ImMainTabItemAdapter(Context context,
                                List<ImMainTabItem> imMainTabItems) {
        setImMainTabItems(imMainTabItems);
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public List<ImMainTabItem> getImMainTabItems() {
        return this.imMainTabItems;
    }

    /**
     * 配置数据集
     *
     * @param imMainTabItems
     */
    public void setImMainTabItems(List<ImMainTabItem> imMainTabItems) {
        if (imMainTabItems == null) {
            this.imMainTabItems = new ArrayList<ImMainTabItem>();
        } else {
            this.imMainTabItems = imMainTabItems;
        }
    }

    @Override
    public int getCount() {
        return imMainTabItems.size();
    }

    @Override
    public ImMainTabItem getItem(int position) {
        return imMainTabItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.im_main_tab_item, null);
            holder.mItemLayout = convertView
                    .findViewById(R.id.im_main_tab_item_layout);
            holder.mHeadPortraitTv = (TextView) convertView
                    .findViewById(R.id.im_main_tab_head_portrait_tv);
            holder.mHeadPortraitIv = (ImageView) convertView.findViewById(R.id.im_main_tab_head_portrait_iv);
            holder.mMsgCountTv = (TextView) convertView
                    .findViewById(R.id.im_main_tab_msg_count_tv);
            holder.mTitleTv = (TextView) convertView
                    .findViewById(R.id.im_main_tab_title_tv);
            holder.mTimeTv = (TextView) convertView
                    .findViewById(R.id.im_main_tab_time_tv);
            holder.mNewMsgTv = (TextView) convertView
                    .findViewById(R.id.im_main_tab_new_msg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 绑定数据
        bindingData(holder, position);

        return convertView;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    private void bindingData(ViewHolder holder, int position) {
        ImMainTabItem item = getItem(position);
        // 聊天是否为置顶
        if (item.isSetMoveToTop()) {// 已置顶
            holder.mItemLayout
                    .setBackgroundResource(R.drawable.listview_item_bg_selector2);
        } else {
            holder.mItemLayout
                    .setBackgroundResource(R.drawable.listview_item_bg_selector);
        }
        // 头像
        if (item.getItemType() == ItemType.CorpGroupChat) {// 公司群
            holder.mHeadPortraitTv.setVisibility(View.VISIBLE);
            holder.mHeadPortraitIv.setVisibility(View.GONE);
            holder.mHeadPortraitTv.setBackgroundResource(IMIconConfig
                    .getDeptColor(item.getItemId()));
            holder.mHeadPortraitTv.setText(item.getTitle().substring(0, 1));
//			holder.mHeadPortraitTv.setTextColor(IMIconConfig.getTextColor(
//					item.getItemId(), mContext));
            bindMessageView(item, holder);
        } else if (item.getItemType() == ItemType.CustomGroupChat) {// 自建群
            holder.mHeadPortraitTv.setVisibility(View.VISIBLE);
            holder.mHeadPortraitIv.setVisibility(View.GONE);
            holder.mHeadPortraitTv.setBackgroundResource(IMIconConfig
                    .getGroupColor(item.getItemId()));
            holder.mHeadPortraitTv.setText("");
            bindMessageView(item, holder);
        } else if (item.getItemType() == ItemType.P2PChat) {// 点对点
            holder.mHeadPortraitTv.setVisibility(View.GONE);
            holder.mHeadPortraitIv.setVisibility(View.VISIBLE);

            User user = getUser(item.getItemId());

            String headImgUrl = null;
            if (user != null) {
                headImgUrl = user.getUrl();
            }
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                    .showImageForEmptyUri(R.drawable.addrbook_contact)
                    .build();
            ImageLoader.getInstance().displayImage(headImgUrl, holder.mHeadPortraitIv, options);
            holder.mHeadPortraitTv.setText("");
            bindMessageView(item, holder);
        } else if (item.getItemType() == ItemType.Teleconference) {
            holder.mHeadPortraitTv.setVisibility(View.VISIBLE);
            holder.mHeadPortraitIv.setVisibility(View.GONE);
            holder.mHeadPortraitTv.setBackgroundResource(R.drawable.app_meeting_head);
            holder.mTitleTv.setText("电话会议");
            holder.mMsgCountTv.setText("0");
            holder.mNewMsgTv.setText("电话会议已经走起");
            holder.mHeadPortraitTv.setText("");
        }
    }

    /**
     * 消息条目共性的布局
     */
    private void bindMessageView(ImMainTabItem item, ViewHolder holder) {
        // 消息数量
        int unReadCount = item.getUnReadCount();
        if (unReadCount > 0) {
            holder.mMsgCountTv.setVisibility(View.VISIBLE);
            if (unReadCount > 99) {
                holder.mMsgCountTv.setText("99+");
            } else {
                holder.mMsgCountTv.setText(String.valueOf(unReadCount));
            }
        } else {
            holder.mMsgCountTv.setVisibility(View.GONE);
        }

        // 标题
        holder.mTitleTv.setText(item.getTitle());

        // 时间
        holder.mTimeTv.setText(DateUtils.getWeiXinTime(item.getTime()));

        // 最新消息
        // String intr = Html.fromHtml(item.getIntro()).toString();
        String newMsg = item.getIntro();
        if (TextUtils.isEmpty(newMsg) && item.getItemType() != ItemType.P2PChat) {
            holder.mNewMsgTv.setText(item.getTitle() + "群已创建，欢迎大家踊跃发言");
        } else {
            holder.mNewMsgTv.setText(newMsg);
        }
    }

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     */
    private User getUser(String userId) {
//        UserService service = (UserService) OsgiServiceLoader.getInstance()
//                .getService(UserService.class);
//        UserServiceParam param = new UserServiceParam();
//        param.setServie("FindContactService");
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", userId);
//        param.setParams(map);
//        UserServiceResult<List<User>> result = service.invoke(param);
//        List<User> users = result.getData();
//        if (users != null && users.size() > 0) {
//            return users.get(0);
//        }
//        return null;
        return (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(userId);
    }

    class ViewHolder {
        public View mItemLayout;
        public TextView mHeadPortraitTv;
        public ImageView mHeadPortraitIv;
        public TextView mMsgCountTv;
        public TextView mTitleTv;
        public TextView mTimeTv;
        public TextView mNewMsgTv;
    }

}
