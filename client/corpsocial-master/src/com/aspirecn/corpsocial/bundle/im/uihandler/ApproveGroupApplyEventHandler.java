package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.ApproveGroupApplyEvent;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupOperateDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupOperateEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupOperateEntity.GroupOperateStatus;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 接收到入群申请后批准处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = ApproveGroupApplyEvent.class)
public class ApproveGroupApplyEventHandler implements
        IHandler<Null, ApproveGroupApplyEvent> {

//    @Autowired
//    private IIMNetClient imNetClient;

    @Autowired
    private EventListenerSubjectLoader eventListener;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private GroupOperateDao groupNoticeDao;

    @Override
    public Null handle(final ApproveGroupApplyEvent busEvent) {

        GroupOperateEntity groupNoticeEntity = new GroupOperateEntity();
//        groupNoticeEntity.setOperateType(CommandType.APPROVE_GROUP_APPLY
//                .getValue());
        GroupOperateEntity insert = groupNoticeDao.insert(groupNoticeEntity);

        return new Null();
    }


    private void handleApproveSuccess(
            ApproveGroupApplyEvent approveGroupApplyEvent, int seqNo) {
        Boolean accept = approveGroupApplyEvent.getAccept();
        if (accept) {
            Map<String, Object> where = new HashMap<String, Object>();
            where.put("groupId", approveGroupApplyEvent.getGroupId());
            where.put("belongUserId", Config.getInstance().getUserId());
            GroupEntity groupEntity = groupDao.findByWhere(where);

            // 将被批准的成员加入群组成员列表
            String approvedUserId = approveGroupApplyEvent.getApprovedUserId();
            groupEntity.getMemberList().add(approvedUserId);

            groupDao.update(groupEntity);

        }

        GroupOperateEntity findById = groupNoticeDao.findById(seqNo);
        findById.setStatus(GroupOperateStatus.SUCCESS.value);
        groupNoticeDao.update(findById);

    }

}
