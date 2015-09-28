package com.aspirecn.corpsocial.bundle.im.facade.impl;

import com.aspirecn.corpsocial.bundle.im.facade.Group;
import com.aspirecn.corpsocial.bundle.im.facade.LoadMyGroupService;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;

import java.util.ArrayList;
import java.util.List;

@OsgiService(serviceType = LoadMyGroupService.class)
public class LoadMyGroupServiceImpl implements LoadMyGroupService {

    private GroupDao groupDao = new GroupDao();

    @Override
    public List<Group> invoke(Void params) {
        List<Group> groupList = new ArrayList<Group>();
        List<GroupEntity> groupEntityList = groupDao.findAllByWhere(
                "belongUserId", Config.getInstance().getUserId(),
                "createTime DESC");

        for (GroupEntity groupEntity : groupEntityList) {
            Group group = new Group();
            group.setGroupId(groupEntity.getGroupId());
            group.setName(groupEntity.getName());
            group.setGroupType(groupEntity.getGroupType());
            group.setDescription(groupEntity.getDescription());

            groupList.add(group);
        }

        return groupList;
    }

}
