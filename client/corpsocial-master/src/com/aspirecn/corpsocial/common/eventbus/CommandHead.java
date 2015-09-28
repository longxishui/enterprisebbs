package com.aspirecn.corpsocial.common.eventbus;

/**
 * Created by duyinzhou on 2015/9/10.
 */
public class CommandHead {
    public String targetId;
    public String userId;
    public boolean groupMsg;
    public long sendTime;
    public String messageId;
    public CommandData.CommandType commandType;
}
