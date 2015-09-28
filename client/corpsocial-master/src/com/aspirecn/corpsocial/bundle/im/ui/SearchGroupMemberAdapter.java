package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

public class SearchGroupMemberAdapter extends BaseAdapter {

    private Context mContext;

    private List<GroupMemberEntity> mBeans;

    public SearchGroupMemberAdapter(Context context, List<GroupMemberEntity> beans) {
        this.mContext = context;
        this.mBeans = beans;
    }

    @Override
    public int getCount() {
        return mBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // 初始化控件
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.im_search_group_member_gridview_item, null);
            holder.headPortraitBt = (ImageView) convertView
                    .findViewById(R.id.im_search_group_member_item_head_iv);
            holder.userNameTV = (TextView) convertView
                    .findViewById(R.id.im_search_group_member_item_name_tv);
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
     */
    private void bindingData(ViewHolder holder, int position) {
        GroupMemberEntity groupMemberEntity = mBeans.get(position);

        // 获取用户头像
        User user = getUser(groupMemberEntity.getMemberId());
        String headImgUrl = null;
        if (user != null) {
            headImgUrl = user.getUrl();
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
                .build();
        ImageLoader.getInstance().displayImage(headImgUrl, holder.headPortraitBt, options);


        // holder.headPortraitBt.setOnClickListener(new OnClickListener() {
        // // 点击头像看用户详情
        // @Override
        // public void onClick(View v) {
        // Intent intent = new Intent(mContext,
        // AddrbookPersonalParticularsActivity_.class);
        // intent.putExtra("ContactId", groupMemberEntity.getMemberId());
        // mContext.startActivity(intent);
        // }
        // });
        holder.userNameTV.setText(groupMemberEntity.getMemberName());
        // holder.msgTV.setText(msg.getStatus());
        // holder.timeTV.setText(DateUtils.getWeiXinTime(msg.getCreateTime()));
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
        public ImageView headPortraitBt;
        public TextView userNameTV;
    }

}
