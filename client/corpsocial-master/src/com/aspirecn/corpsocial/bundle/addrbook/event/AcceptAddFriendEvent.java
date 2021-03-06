package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-19.
 */
public class AcceptAddFriendEvent extends BusEvent {

    private FriendInvite invite;

    public FriendInvite getInvite() {
        return invite;
    }

    public void setInvite(FriendInvite invite) {
        this.invite = invite;
    }
}
