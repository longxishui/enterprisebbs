package com.aspirecn.corpsocial.bundle.addrbook.ui.widget;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<User> {

    public int compare(User o1, User o2) {
        if (o1.getInitialKey().equals("@")
                || o2.getInitialKey().equals("#")) {
            return -1;
        } else if (o1.getInitialKey().equals("#")
                || o2.getInitialKey().equals("@")) {
            return 1;
        } else {
            return o1.getInitialKey().compareTo(o2.getInitialKey());
        }
    }

}
