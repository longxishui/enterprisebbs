package com.aspirecn.corpsocial.common.ui.chatitemview.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 基类
 * Created by lihaiqiang on 2015/5/29.
 */
public class ViewHolder {

    public TextView timeTV;

    public View receiveLayout;
    public ImageView receiveHeadIconIV;
    public TextView receiveNameTV;
    public ImageView receiveRelationIconIV;

    public View sendLayout;
    public ImageView sendHeadIconIV;
    public TextView sendNameTV;

    public Button sendAgainBT;
    public ProgressBar sendSendingPB;

}
