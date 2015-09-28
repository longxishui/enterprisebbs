package com.aspirecn.corpsocial.bundle.addrbook.ui.company;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.facade.AddrbookSelectConfig;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.CompanyEntity;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.util.DensityUtil;
import com.aspirecn.corpsocial.common.util.StringUtils;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by ChenZiqiang on 15-5-7.
 */
public class CompanyAdapter extends BaseAdapter {

    private CompanyFragment mCompanyFragment;
    private List<CompanyEntity> mCompanyEntities;
    private ViewHolder holder;

    public CompanyAdapter(CompanyFragment companyFragment, List<CompanyEntity> companyEntities) {
        this.mCompanyFragment = companyFragment;
        this.mCompanyEntities = companyEntities;
    }

    @Override
    public int getCount() {
        return mCompanyEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return mCompanyEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mCompanyFragment.getActivity(), R.layout.addrbook_company_item, null);
            holder = new ViewHolder();

            viewById(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        showData(mCompanyEntities.get(i));
        return view;
    }

    /*显示头像*/
    private  DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
            .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
            .showImageOnFail(R.drawable.im_main_tab_head_portrait_3)
            .build();

    private void showData(CompanyEntity entity) {
        if (entity.getType() == CompanyEntity.CompanyType.GROUP) {
            holder.groupLl.setVisibility(View.VISIBLE);
            holder.contactLl.setVisibility(View.GONE);

            holder.groupName.setText(entity.getName());

            if (!entity.isExpanded()) {
                holder.expandView.setBackgroundResource(R.drawable.addrbook_btn_open);
            } else {
                holder.expandView.setBackgroundResource(R.drawable.addrbook_btn_close);
            }

            /*设置偏移量*/
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.itemRl.getLayoutParams();
            layoutParams.leftMargin = DensityUtil.dip2px(mCompanyFragment.getActivity(), 10) * entity.getLevel();
            layoutParams.rightMargin = DensityUtil.dip2px(mCompanyFragment.getActivity(), 10);
            holder.itemRl.setLayoutParams(layoutParams);

            holder.groupLl.setOnClickListener(new groupOnClick(entity));
        } else {
            holder.groupLl.setVisibility(View.GONE);
            holder.contactLl.setVisibility(View.VISIBLE);

            holder.contactName.setText(entity.getName());
            if (StringUtils.isEmpty(entity.getSignature()))
                holder.signature.setText("让我们一起聊吧");
            else
                holder.signature.setText(entity.getSignature());

            /*设置偏移量*/
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.itemRl.getLayoutParams();
            layoutParams.leftMargin = DensityUtil.dip2px(mCompanyFragment.getActivity(), 10)
                    * entity.getLevel() + DensityUtil.dip2px(mCompanyFragment.getActivity(), 20);
            holder.itemRl.setLayoutParams(layoutParams);


            ImageLoader.getInstance().displayImage(entity.getHeadImgUrl(), holder.headImgUrl, options);

        }

        /*选择框和选择事件处理*/
        //查看联系人
        if (mCompanyFragment.getSelectConfig().getSelectType() == AddrbookSelectConfig.AddrbookSelectType.WATCH) {
            holder.contactLl.setOnClickListener(new ContactDetail(entity.getId(), entity.getCorpId()));
            holder.select.setVisibility(View.GONE);
            //选择联系人
        } else if (mCompanyFragment.getSelectConfig().getSelectType() == AddrbookSelectConfig.AddrbookSelectType.SELECT) {
            /*选择部门，全选操作*/
            if (entity.getType() == CompanyEntity.CompanyType.GROUP) {
                holder.select.setOnClickListener(new CompanySelect(entity, holder));
            } else {
                /*单个选择联系人*/
                holder.select.setOnClickListener(new CompanySelect(entity, holder));
                holder.contactLl.setOnClickListener(new CompanySelect(entity, holder));
            }
            //单选联系人
        } else if (mCompanyFragment.getSelectConfig().getSelectType() == AddrbookSelectConfig.AddrbookSelectType.ONLYSELECT) {
            if (entity.getType() == CompanyEntity.CompanyType.GROUP)
                holder.select.setVisibility(View.GONE);
            else {
                holder.select.setVisibility(View.VISIBLE);
                holder.select.setOnClickListener(new OnlySelect(entity, holder));
                holder.contactLl.setOnClickListener(new OnlySelect(entity, holder));
            }
        }

        /*设置选择框样式*/
        if (entity.isUnSelect()) {
            holder.select.setClickable(false);
            holder.select.setOnClickListener(null);
            holder.contactLl.setOnClickListener(null);
            holder.select.setButtonDrawable(R.drawable.addrbook_checkbox);
        } else {
            holder.select.setButtonDrawable(R.drawable.login_checkbox);
            if (entity.isSelector()) {
                holder.select.setChecked(true);
            } else {
                holder.select.setChecked(false);
            }
        }

    }

    private void expand(CompanyEntity entity) {
        if (entity.isFriend()) {
            mCompanyFragment.addFriend();
        } else {
            mCompanyFragment.getData(entity);
        }
    }

    private void close(CompanyEntity entity) {
        if (entity.isFriend()) {
            mCompanyFragment.closeFriend();
        } else {
            mCompanyFragment.closeGroupData(entity.getId());
        }
    }

    private void viewById(View view) {
        holder.expandView = (ImageView) view.findViewById(R.id.mycompay_group_develop);
        holder.headImgUrl = (ImageView) view.findViewById(R.id.addrbook_component_headimg);
        holder.groupLl = (LinearLayout) view.findViewById(R.id.mycompay_group_item_ll);
        holder.contactLl = (LinearLayout) view.findViewById(R.id.mycompay_contant_item_ll);
        holder.itemRl = (LinearLayout) view.findViewById(R.id.mycompay_item_rl);
        holder.groupName = (TextView) view.findViewById(R.id.mycompay_group_department);
        holder.contactName = (TextView) view.findViewById(R.id.addrook_mycompany_name);
        holder.signature = (TextView) view.findViewById(R.id.addrook_mycompany_signature);
        holder.select = (CheckBox) view.findViewById(R.id.addrbook_group_check);
    }


    private class ViewHolder {
        public ImageView expandView;
        public TextView groupName;
        public TextView contactName;
        public TextView signature;
        public ImageView headImgUrl;
        public CheckBox select;
        public LinearLayout groupLl;
        public LinearLayout contactLl;
        public LinearLayout itemRl;
    }

    /**
     * 部门点击监听
     */
    private class groupOnClick implements View.OnClickListener {

        private CompanyEntity companyEntity;

        public groupOnClick(CompanyEntity companyEntity) {
            this.companyEntity = companyEntity;
        }

        @Override
        public void onClick(View view) {
            if (!companyEntity.isExpanded()) {
                expand(companyEntity);
                companyEntity.setExpanded(true);
            } else {
                close(companyEntity);
                companyEntity.setExpanded(false);
            }
        }
    }

    /**
     * 查看联系人详情
     */
    private class ContactDetail implements View.OnClickListener {
        private String contactId;
        private String corpId;

        public ContactDetail(String contactId) {
            this.contactId = contactId;
        }

        public ContactDetail(String contactId, String corpId) {
            this.contactId = contactId;
            this.corpId = corpId;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(
                    mCompanyFragment.getActivity(),
                    com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_.class);
            intent.putExtra("ContactId", contactId);
            if (corpId != null)
                intent.putExtra("corpId", corpId);
            mCompanyFragment.getActivity().startActivity(intent);
        }
    }

    /**
     * 选择联系人或部门操作
     */
    private class CompanySelect implements View.OnClickListener {
        private CompanyEntity mCompanyEntity;
        private ViewHolder mHolder;

        public CompanySelect(CompanyEntity companyEntity, ViewHolder holder) {
            this.mCompanyEntity = companyEntity;
            this.mHolder = holder;
        }

        @Override
        public void onClick(View view) {
            if (mCompanyEntity.isSelector()) {
                mHolder.select.setChecked(false);
                if (mCompanyEntity.isFriend()) {
                    if (mCompanyEntity.getType() == CompanyEntity.CompanyType.GROUP) {
                        mCompanyFragment.selectAllFriend(mCompanyEntity, false);
                    } else {
                        mCompanyFragment.selectFriend(mCompanyEntity, false);
                    }
                } else {
                    mCompanyFragment.deSelectCompanyData(mCompanyEntity);
                }
            } else {
                mHolder.select.setChecked(true);
                if (mCompanyEntity.isFriend()) {
                    if (mCompanyEntity.getType() == CompanyEntity.CompanyType.GROUP) {
                        mCompanyFragment.selectAllFriend(mCompanyEntity, true);
                    } else {
                        mCompanyFragment.selectFriend(mCompanyEntity, true);
                    }
                } else {
                    mCompanyFragment.selectCompanyData(mCompanyEntity);
                }
            }
        }
    }

    /**
     * 单选方法
     */
    private class OnlySelect implements View.OnClickListener {
        private CompanyEntity mCompanyEntity;
        private ViewHolder mHolder;

        public OnlySelect(CompanyEntity companyEntity, ViewHolder holder) {
            this.mCompanyEntity = companyEntity;
            this.mHolder = holder;
        }

        @Override
        public void onClick(View view) {
            mHolder.select.setChecked(true);
            mCompanyFragment.onlySelect(mCompanyEntity);
        }
    }

}
