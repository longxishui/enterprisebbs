package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * Created by Amos on 15-6-25.
 */
public class InviteReadRespEvent extends BusEvent {

    private List<Integer> seqs;

    public List<Integer> getSeqs() {
        return seqs;
    }

    public void setSeqs(List<Integer> seqs) {
        this.seqs = seqs;
    }
}
