package com.aspirecn.corpsocial.common.eventbus;

import java.util.ArrayList;
import java.util.List;

public abstract class BusEvent {

    public List<BusEvent> nextEvents=new ArrayList<>();

    public long failedtime=0;
    //0 not run , 1 running
    public int status;
}
