package com.aspirecn.corpsocial.bundle.addrbook.utils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;

import java.util.Comparator;

/**
 * Created by Amos on 15-6-23.
 */
public class UserContactSort implements Comparator<User> {

    public int compare(User u1, User u2) {
        if (u1.getCorpId().equals(u2.getCorpId())) {
//            if(u1.getDeptCode().equals(u2.getDeptCode())){
//                return 0;
//            }else{
//                if(u1.getDeptCode().startsWith(u2.getDeptCode())){
//                    return 1;
//                }else if(u2.getDeptCode().startsWith(u1.getDeptCode())){
//                    return -1;
//                }else{
//                    return u1.getDeptCode().compareTo(u2.getDeptCode());
//                }
//            }
            return u1.getDeptCode().compareTo(u2.getDeptCode());
        } else
            return u1.getCorpId().compareTo(u2.getCorpId());
    }
}
