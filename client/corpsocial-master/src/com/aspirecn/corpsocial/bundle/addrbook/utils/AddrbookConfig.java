package com.aspirecn.corpsocial.bundle.addrbook.utils;

/**
 * Created by Amos on 15-6-29.
 */
public class AddrbookConfig {

    public static final String GET_ADDRESS_LIST = "GET_ADDRESS_LIST";
    public static final String GET_DEPART_LIST = "GET_DEPART_LIST";
    public static final String GET_FRIEND_LIST = "GET_FRIEND_LIST";
    //public static final String GET_CREATED_CORP_LIST = "GET_CREATED_CORP_LIST";
    public static final String FIND_CONTACT = "FIND_CONTACT";
    //public static final String GET_SELF_CORP_LIST = "GET_SELF_CORP_LIST";
    public static final String FIND_CONTACT_BATCH = "FIND_CONTACT_BATCH";

    public static int ADDRESS_STATUS=0;

    public static Object obj=new Object();

    public static boolean setWorking(){
        synchronized (obj) {
            if(ADDRESS_STATUS==1) return false;
            ADDRESS_STATUS = 1;
            return true;
        }
    }

    public static void reset(){
        synchronized (obj) {
            ADDRESS_STATUS = 0;
        }
    }

    public static int getWorkStatus(){
        return ADDRESS_STATUS;
    }
}
