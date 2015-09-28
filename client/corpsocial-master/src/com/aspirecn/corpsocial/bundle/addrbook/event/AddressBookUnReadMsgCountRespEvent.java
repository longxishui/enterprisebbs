package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by lihaiqiang on 15-6-29.
 */
public class AddressBookUnReadMsgCountRespEvent extends BusEvent {

    private int unReadInviteFriendCount;

    public int getUnReadInviteFriendCount() {
        return unReadInviteFriendCount;
    }

    public void setUnReadInviteFriendCount(int unReadInviteFriendCount) {
        this.unReadInviteFriendCount = unReadInviteFriendCount;
    }

}
