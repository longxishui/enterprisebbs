package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.Dept;
import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindUserByMobileEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindUserEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactByDeptEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactByIdsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactDetailEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactIdsByDeptIdsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetDeptChildrenEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetSelectContactCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.SearchContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.QueryResult;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.SearchParam;
import com.aspirecn.corpsocial.bundle.addrbook.repository.DepartDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.DepartEntity;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-17.
 */
@EventBusAnnotation.UIEventHandler(eventType = FindUserEvent.class)
public class FindUserEventHandler implements
        IHandler<QueryResult, FindUserEvent> {

    @EventBusAnnotation.Component
    private UserDao dao ;
    @EventBusAnnotation.Component
    private DepartDao departDao ;

    private void doLog(BusEvent b, Exception e) {
        LogUtil.e(b.getClass().getSimpleName() + "查找用户出错:" + LogUtil.getStackMsg(e));
    }

    @Override
    public QueryResult handle(FindUserEvent busEvent) {

        QueryResult r = new QueryResult();
        if (busEvent.getEvent() != null) {
            //根据手机号码查找本地用户
            if (busEvent.getEvent() instanceof FindUserByMobileEvent) {
                SearchParam param = new SearchParam();
                param.setMisty(((FindUserByMobileEvent) busEvent.getEvent()).isMisty());
                param.setName("cellphone");
                param.setValue(((FindUserByMobileEvent) busEvent.getEvent()).getMobile());
                List<SearchParam> params = new ArrayList();
                params.add(param);
                List<String> orders = new ArrayList<String>();
                orders.add("initialKey");
                try {
                    List<User> users = dao.find(Config.getInstance().getUserId(), params, 0, 0, orders);
                    r.setResult(users);
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
                //根据部门Id查询所有联系人(不包括子部门)
            } else if (busEvent.getEvent() instanceof GetContactByDeptEvent) {
                SearchParam param = new SearchParam();
                param.setName("deptCode");
                param.setMisty(false);
                param.setValue(((GetContactByDeptEvent) busEvent.getEvent()).getDeptId());
                List<SearchParam> params = new ArrayList();
                params.add(param);
                List<String> orders = new ArrayList<String>();
                orders.add("sortNo");
                orders.add("spellKey");

                try {
                    if (param.getValue() == null) {
                        r.setResult(new ArrayList());
                    } else {
                        List<User> users = dao.find(Config.getInstance().getUserId(), params, 0, 0, orders);

                        //List<User> users=dao.findByDeptCodes(Config.getInstance().getUserId(), ((GetContactByDeptEvent) busEvent.getEvent()).getDeptIds(), ((GetContactIdsByDeptIdsEvent) busEvent.getEvent()).getCorpId());

                        r.setResult(users);
                    }
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
                //根据联系人的ID列表，查询联系人。
            } else if (busEvent.getEvent() instanceof GetContactByIdsEvent) {
                try {
                    List<User> users = dao.findFilterByContactIds(Config.getInstance().getUserId(), ((GetContactByIdsEvent) busEvent.getEvent()).getIds());
                    r.setResult(users);
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
                // 根据部门列表，获取这些部门所有下级（直至树叶级）的联系人简要信息
            } else if (busEvent.getEvent() instanceof GetContactIdsByDeptIdsEvent) {
                try {
//                    List<User> users = dao.findByDeptCodes(Config.getInstance().getUserId(), ((GetContactIdsByDeptIdsEvent) busEvent.getEvent()).getDeptIds(), ((GetContactIdsByDeptIdsEvent) busEvent.getEvent()).getCorpId());
                    List<User> users = new ArrayList<User>();
                    r.setResult(users);
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
            } else if (busEvent.getEvent() instanceof GetContactsEvent) {
                try {
                    List<User> users = dao.findUsers(Config.getInstance().getUserId(), ((GetContactsEvent) busEvent.getEvent()).getContactIds());
                    r.setResult(users);
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
                //获取联系人详情事件
            } else if (busEvent.getEvent() instanceof GetContactDetailEvent) {
                try {
                    User user = dao.findUserDetail(((GetContactDetailEvent) busEvent.getEvent()).getContactId(), null, ((GetContactDetailEvent) busEvent.getEvent()).getCorpId());

                    r.setResult(user);
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
                //查找子部门
            } else if (busEvent.getEvent() instanceof GetDeptChildrenEvent) {
                try {
                    List<String> corpIds = new ArrayList<String>();
                    List<UserCorp> userItems = GetSelfCorpListRespBean.find(Config.getInstance().getUserId());
                    for (UserCorp uc : userItems) {
                        if (uc.getStatus().equals("0")) {
                            UserCorp entity = GetSelfCorpListRespBean.findByCorpId(uc.getCorpId());
                            corpIds.add(entity.getCorpId());
                        }
                    }

                    List<Dept> depts = departDao.findChildren(((GetDeptChildrenEvent) busEvent.getEvent()).getDeptId(), corpIds);
                    r.setResult(depts);
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
                //根据部门，获取其联系人数量。
            } else if (busEvent.getEvent() instanceof GetSelectContactCountEvent) {
                try {
                    DepartEntity entity = departDao.findDeptById(((GetSelectContactCountEvent) busEvent.getEvent()).getId());
//                    int count = dao.getContactCountByDept(Config.getInstance().getUserId(), entity.getCode(), entity.getCorpId(), true);
                    int count = 0;
                    r.setResult(count);
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
                //根据关键字搜索联系人
            } else if (busEvent.getEvent() instanceof FindFriendEvent) {
                String key = ((FindFriendEvent) busEvent.getEvent()).getKey();
                String sort = ((FindFriendEvent) busEvent.getEvent()).getSort();
                try {
                    List<User> users = dao.findByKeyWord(Config.getInstance().getUserId(), key, null, null, 1, sort, 0);
                    r.setResult(dao.merge(users));
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
            } else if (busEvent.getEvent() instanceof SearchContactEvent) {
                String key = ((SearchContactEvent) busEvent.getEvent()).getKeyword();
                int type = ((SearchContactEvent) busEvent.getEvent()).getType();
                String corpId = ((SearchContactEvent) busEvent.getEvent()).getCorpId();
                try {
                    List<User> users = dao.findByKeyWord(Config.getInstance().getUserId(), key, null, corpId, 0, null, type);
                    r.setResult(dao.merge(users));
                    r.setErrorCode(0);
                } catch (Exception e) {
                    doLog(busEvent.getEvent(), e);
                    r.setErrorCode(-1);
                }
            }
        }

        return r;
    }
}
