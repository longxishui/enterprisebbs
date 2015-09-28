package com.aspirecn.corpsocial.bundle.addrbook.utils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.Comparator;

/**
 * Created by chenbin on 2015/8/27.
 */
public class UserNameSort implements Comparator {
    @Override
    public int compare(Object uu1, Object uu2) {
        User u1=(User)uu1;
        User u2=(User)uu2;
        return u1.getSpellKey().compareTo(u2.getSpellKey());
    }
}
