package com.aspirecn.corpsocial.bundle.workgrp.domain;

import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSGroupEntity;

import java.util.List;

/**
 * @author chenping
 */
public class BBSGroup {
    private BBSGroupEntity bbsGroupEntity;
    private List<String> admins;

    public BBSGroup() {
        super();
    }

    public BBSGroupEntity getBbsGroupEntity() {
        return bbsGroupEntity;
    }

    public void setBbsGroupEntity(BBSGroupEntity bbsGroupEntity) {
        this.bbsGroupEntity = bbsGroupEntity;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }
}
