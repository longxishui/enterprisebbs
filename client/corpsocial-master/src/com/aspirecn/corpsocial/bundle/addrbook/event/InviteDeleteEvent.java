package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * 删除好友邀请消息记录事件
 * Created by Amos on 15-6-25.
 */
public class InviteDeleteEvent extends BusEvent {

    public List<Integer> seqNoes;

    public List<Integer> getSeqNoes() {
        return seqNoes;
    }

    public void setSeqNoes(List<Integer> seqNoes) {
        this.seqNoes = seqNoes;
    }
}
