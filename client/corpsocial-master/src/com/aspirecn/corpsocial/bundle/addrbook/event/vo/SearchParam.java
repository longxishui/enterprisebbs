package com.aspirecn.corpsocial.bundle.addrbook.event.vo;

/**
 * Created by Amos on 15-6-17.
 */
public class SearchParam {

    /**
     * 参数名
     */
    private String name;
    /**
     * 参数值
     */
    private Object value;
    /**
     * 是否模糊查询
     */
    private boolean misty;
    /**
     * 0 and , 1 or
     */
    private int operate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isMisty() {
        return misty;
    }

    public void setMisty(boolean misty) {
        this.misty = misty;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }
}
