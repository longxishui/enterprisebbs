package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.im.facade.Group;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.IMIconConfig;
import com.aspiren.corpsocial.R;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.StringUtils;

/**
 * 我的群组Adapter
 *
 * @author chenziqiang
 */
public class ImGroupListAdapter extends BaseAdapter {

    private List<Group> list = new ArrayList<Group>();
    private ViewHolder holder = null;
    private Context context;

    public ImGroupListAdapter(Context context, List<Group> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getItemViewType(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.addrbook_contatcs_item, null);
            holder.deptName = (TextView) convertView
                    .findViewById(R.id.addrbook_contatcs_item_name);
            holder.deptSignature = (TextView) convertView
                    .findViewById(R.id.addrbook_contatcs_item_department);
            holder.headImageUrl = (ImageView) convertView
                    .findViewById(R.id.addrbook_component_headimg);
            holder.headImgText = (TextView) convertView
                    .findViewById(R.id.addrbook_component_headtext);
            holder.state = (ImageView) convertView
                    .findViewById(R.id.addrbook_contatcs_item_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 头像
        if (list.get(position).getGroupType() == GroupType.GROUP.getValue()) {// 自建群
            holder.headImageUrl.setImageResource(IMIconConfig
                    .getGroupColor(list.get(position).getGroupId()));
            holder.headImgText.setText("");
        } else {
            holder.headImageUrl.setImageResource(IMIconConfig.getDeptColor(list
                    .get(position).getGroupId()));
            holder.headImgText.setText(list.get(position).getName()
                    .substring(0, 1));
//			holder.headImgText.setTextColor(IMIconConfig.getTextColor(
//					list.get(position).getGroupId(), context));
        }
        holder.deptName.setText(list.get(position).getName());

        if (StringUtils.isBlank(list.get(position).getDescription()))
            holder.deptSignature.setText(list.get(position).getName()
                    + "群已创建，欢迎大家踊跃发言");
        else
            holder.deptSignature.setText(list.get(position).getDescription());
        holder.state.setVisibility(View.GONE);
        return convertView;
    }

    private final class ViewHolder {
        ImageView headImageUrl;// 头像
        TextView headImgText;
        TextView deptName;// 名字
        TextView deptSignature;// 部门名称
        ImageView state;// 状态
    }

}
