package com.aspirecn.corpsocial.bundle.settings.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.settings.viewmodel.WorkbenchSettingItem;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenziqiang on 15-8-26.
 */
public class WorkbenchSettingAdapter extends BaseAdapter {
    private List<WorkbenchSettingItem> settingItems;
    private Context context;

    public WorkbenchSettingAdapter(Context context, List<WorkbenchSettingItem> settingItems) {
        this.context = context;
        this.settingItems = settingItems;
        iconsData();
    }

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
            .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
            .build();

    private Map<String, Integer> icons = new HashMap<>();

    private void iconsData() {
        icons.put("PASSWORD", R.drawable.settings_updatepwd_head);
        icons.put("NEW_GROUP", R.drawable.settings_newcompany);
        icons.put("NEW_MESSAGE", R.drawable.settings_newmessage_head);
        icons.put("ABOUT_US", R.drawable.settings_about_head);
        icons.put("CHECK_VERSION", R.drawable.settings_checkversion_head);
        icons.put("NINE_GRIDS", R.drawable.setting_nine_grids);
        icons.put("ABOUT_MOBILE", R.drawable.setting_about_mobile);
        icons.put("EXIT", R.drawable.settings_exit_head);
    }

    @Override
    public int getCount() {
        return settingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return settingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (settingItems.get(position).code == 0) {
            convertView = setHeadImage(convertView, settingItems.get(position));
        } else if (settingItems.get(position).code == -10) {
            convertView = spaceView(convertView);
        } else {
            convertView = setItem(convertView, settingItems.get(position));
        }

        return convertView;
    }

    private View setHeadImage(View convertView, WorkbenchSettingItem settingItem) {
        convertView = View.inflate(context, R.layout.setting_workbench_head_item, null);
        ImageView headImg = (ImageView) convertView.findViewById(R.id.settings_ui_head_iv);
        TextView nameTv = (TextView) convertView.findViewById(R.id.settings_ui_name_tv);
        TextView signatureTv = (TextView) convertView.findViewById(R.id.settings_ui_department_tv);

        nameTv.setText(settingItem.name);
        signatureTv.setText(settingItem.label);

        ImageLoader.getInstance().displayImage(settingItem.imgSrc, headImg, options);

        return convertView;
    }

    private View setItem(View convertView, WorkbenchSettingItem settingItem) {
        convertView = View.inflate(context, R.layout.setting_workbench_item, null);
        ImageView headImg = (ImageView) convertView.findViewById(R.id.settings_workbench_img);
        ImageView jumpImg = (ImageView) convertView.findViewById(R.id.settings_workbench_jump_img);
        TextView nameTv = (TextView) convertView.findViewById(R.id.settings_workbench_tv);
        TextView label = (TextView) convertView.findViewById(R.id.settings_workbench_label_tv);
        ImageView labelImg = (ImageView) convertView.findViewById(R.id.settings_workbench_label_img);

        labelImg.setVisibility(View.GONE);
        label.setText(settingItem.label);
        nameTv.setText(settingItem.name);
        headImg.setImageResource(icons.get(settingItem.iconPath));
        if (settingItem.jumpImg) {
            jumpImg.setVisibility(View.VISIBLE);
        } else {
            jumpImg.setVisibility(View.GONE);
        }

        return convertView;
    }

    private View spaceView(View convertView) {
        convertView = View.inflate(context, R.layout.setting_workbench_space, null);
        return convertView;
    }


}
