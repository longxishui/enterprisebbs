package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.Dept;
import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.SearchParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;
import com.aspirecn.corpsocial.bundle.addrbook.facade.UserServiceParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.UserServiceResult;
import com.aspirecn.corpsocial.bundle.addrbook.repository.DepartDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.DepartEntity;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.UserEntity;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Amos on 15-6-18.
 */
@OsgiAnnotation.OsgiService(serviceType = UserService.class)
public class UserServiceImpl implements UserService {

    @EventBusAnnotation.Component
    private UserDao dao ;
    @EventBusAnnotation.Component
    private FriendInviteDao idao ;
    @EventBusAnnotation.Component
    private DepartDao departDao ;

    @Override
    public UserServiceResult invoke(UserServiceParam param) {
        UserServiceResult result = new UserServiceResult();
        if ("FindContactService".equals(param.getServie())) {
            Map params = param.getParams();
            String userid = (String) params.get("userid");
            //String corpId=(String)params.get(1);
            List<User> users = dao.findByUserId(userid);
            result.setData(users);
            result.setErrorCode(0);
        } else if ("FindContactsByIdsService".equals(param.getServie())) {
            Map params = param.getParams();
            List<String> ids = (List) params.get("ids");
            List<User> users = new ArrayList<User>();
            try {
                users.addAll(dao.findFilterByContactIds(Config.getInstance().getUserId(), ids));
                result.setErrorCode(0);
            } catch (SQLException ex) {
                result.setErrorCode(1);
            }
            result.setData(users);

        } else if ("FindContactsByLoginNamesService".equals(param.getServie())) {
            Map params = param.getParams();
            List<String> loginNames = (List) params.get("loginNames");
            List<User> users = new ArrayList<User>();
            try {
                users.addAll(dao.findByContactLoginNames(Config.getInstance().getUserId(), loginNames));
                result.setErrorCode(0);
            } catch (SQLException ex) {
                result.setErrorCode(1);
            }
            result.setData(users);

        } else if ("FindGroupMembersService".equals(param.getServie())) {
            Map params = param.getParams();
//            List<String> deptCodes = (List) params.get("deptCodes");
//            String corpId = (String) params.get("corpId");
            List<User> users = new ArrayList<User>();
            String deptId = (String) params.get("deptId");
            int start = (Integer) params.get("start");
            int count = (Integer) params.get("limit");
            try {
                DepartEntity entity = departDao.findDeptById(deptId);
                List<String> depts = new ArrayList();
                depts.add(entity.getCode());
//                users.addAll(dao.findByDeptCodes(Config.getInstance().getUserId(), depts, entity.getCorpId(), start, count));
                result.setErrorCode(0);
            } catch (Exception ex) {
                result.setErrorCode(1);
            }
            result.setData(users);

        } else if ("SearchContactService".equals(param.getServie())) {
            Map params = param.getParams();
            String key = (String) params.get("key");
            String deptId = (String) params.get("deptId");
            String corpId = (String) params.get("corpId");
            List<User> users = new ArrayList<User>();
            try {
                users.addAll(dao.findByKeyWord(Config.getInstance().getUserId(), key, deptId, corpId, -1, null, 0));
                result.setErrorCode(0);
            } catch (SQLException ex) {
                result.setErrorCode(1);
            }
            result.setData(users);

        } else if ("SearchGroupMemberService".equals(param.getServie())) {//查找公司群成员
            Map params = param.getParams();
            String key = (String) params.get("key");
            String deptId = (String) params.get("deptId");
            List<User> users = new ArrayList<User>();
            try {
                //users.addAll(dao.findByKeyWord(Config.getInstance().getUserId(), key, deptId, null, 0, null,1));
//                users.addAll(dao.findByKeyWord(Config.getInstance().getUserId(), key, deptId, null, 0, null, 1, true));
                result.setErrorCode(0);
            } catch (Exception ex) {
                result.setErrorCode(1);
            }
            result.setData(users);

        } else if ("FindInviteService".equals(param.getServie())) {
            Map params = param.getParams();
            int start = (Integer) params.get("start");
            int count = (Integer) params.get("count");
            List<FriendInvite> invites = null;
            try {
                invites = idao.queryInvite(Config.getInstance().getUserId(), start, count);
                result.setErrorCode(0);
            } catch (SQLException ex) {
                result.setErrorCode(1);
            }
            result.setData(invites);

        } else if ("FindFriendServiceImpl".equals(param.getServie())) {
            Map params = param.getParams();
            String key = (String) params.get("key");
            List<SearchParam> sparams = new ArrayList();
            SearchParam sparam = new SearchParam();
            sparam.setName("userid");
            sparam.setMisty(false);
            sparam.setValue(key);
            sparams.add(sparam);

            sparam = new SearchParam();
            sparam.setName("isFriend");
            sparam.setMisty(false);
            sparam.setValue(1);
            sparams.add(sparam);
            List<User> users = new ArrayList<User>();
            try {
                users.addAll(dao.find(Config.getInstance().getUserId(), sparams, 0, 0, null));
                result.setErrorCode(0);
            } catch (SQLException ex) {
                result.setErrorCode(1);
            }
            result.setData(users);

        } else if ("FindFriendsByNameService".equals(param.getServie())) {
            Map params = param.getParams();
            String key = (String) params.get("key");
            List<SearchParam> sparams = new ArrayList();
            SearchParam sparam = new SearchParam();
            sparam.setName("name");
            sparam.setMisty(true);
            sparam.setValue(key);
            sparams.add(sparam);

            sparam = new SearchParam();
            sparam.setName("isFriend");
            sparam.setMisty(false);
            sparam.setValue(1);
            sparams.add(sparam);
            List<User> users = new ArrayList<User>();
            try {
                users.addAll(dao.find(Config.getInstance().getUserId(), sparams, 0, 0, null));
                result.setErrorCode(0);
            } catch (SQLException ex) {
                result.setErrorCode(1);
            }
            result.setData(users);

        } else if ("FindContactViewService".equals(param.getServie())) {
//            Map params = param.getParams();
//            String deptId = (String) params.get("deptId");
//            String corpId = (String) params.get("corpId");
//            String key = (String) params.get("key");
//            List<ContactView> views = new ArrayList<ContactView>();
//            try {
//                if (deptId == null && corpId == null) {
//                    List<UserCorp> userCorps = userCorpDao.find(Config.getInstance().getUserId());
//                    for (UserCorp uc : userCorps) {
//                        ContactView view = new ContactView();
//                        view.setCorpId(uc.getCorpId());
//                        view.setCorpName(uc.getCorpName());
//                        List<User> users = dao.findByKeyWord(Config.getInstance().getUserId(), key, deptId, uc.getCorpId(), -1, null,0);
//                        view.getUsers().addAll(users);
//                        List<Dept> depts = departDao.findChildren(null, uc.getCorpId());
//                        view.getDepts().addAll(depts);
//                        views.add(view);
//                    }
//                } else if (deptId != null) {
//                    DepartEntity entity = departDao.findDeptById(deptId);
//
//                    List<Dept> depts = departDao.findChildren(entity.getCode(), corpId);
//                    ContactView view = new ContactView();
//                    view.setCorpId(entity.getCorpId());
//
//                    List<User> users = dao.findByKeyWord(Config.getInstance().getUserId(), key, deptId, entity.getCorpId(), -1, null,0);
//                    view.getUsers().addAll(users);
//                    view.getDepts().addAll(depts);
//                    views.add(view);
//                }
//
//                result.setData(views);
//                result.setErrorCode(0);
//            } catch (SQLException ex) {
//                result.setErrorCode(1);
//            }
        } else if ("FindDepartService".equals(param.getServie())) {
            Map params = param.getParams();
            String deptId = (String) params.get("deptId");
            String corpId = (String) params.get("corpId");
            List<Dept> depts = departDao.findChildren(deptId, corpId);
            result.setData(depts);
            result.setErrorCode(0);
        } else if ("FindCorpService".equals(param.getServie())) {
                List<UserCorp> userCorps = GetSelfCorpListRespBean.find(Config.getInstance().getUserId());
                result.setData(userCorps);
                result.setErrorCode(0);
        } else if ("FindSingleCorpService".equals(param.getServie())) {
            Map<String, String> params = param.getParams();
            String corpId = params.get("corpId");

            UserCorp uc = GetSelfCorpListRespBean.findByCorpId(corpId);
            result.setData(uc);
            result.setErrorCode(0);

        } else if ("FindContactDetailService".equals(param.getServie())) {
            Map params = param.getParams();
            String userid = (String) params.get("userid");
            User user = dao.findUserDetail(userid);
            result.setErrorCode(0);

            result.setData(user);

        } else if ("UpdateContactService".equals(param.getServie())) {
            Map params = param.getParams();
            User user = (User) params.get("user");
            dao.updateContact(user);
            result.setErrorCode(0);
            result.setData(user);
        } else if ("GetGroupSizeService".equals(param.getServie())) {
            Map params = param.getParams();
            String deptId = (String) params.get("deptId");
            int count = 0;
            try {
                DepartEntity entity = departDao.findDeptById(deptId);
//                count = dao.getContactCountByDept(Config.getInstance().getUserId(), entity.getCode(), entity.getCorpId(), true);
            } catch (Exception e) {
                LogUtil.e("GetGroupSizeService出错", e);
            }
            result.setData(count);
            result.setErrorCode(0);
        } else if ("GetGroupIdsService".equals(param.getServie())) {
            Map params = param.getParams();
            String deptId = (String) params.get("deptId");
            List<String> ids = new ArrayList<String>();
            try {
                DepartEntity entity = departDao.findDeptById(deptId);

//                ids.addAll(dao.getContactIdsByDept(Config.getInstance().getUserId(), entity.getCode(), entity.getCorpId(), true));
            } catch (Exception e) {
                LogUtil.e("GetGroupSizeService出错", e);
            }
            result.setData(ids);
            result.setErrorCode(0);
        }

        return result;
    }

    private User build(UserEntity entity) {
        User user = new User();
        user.setName(entity.getName());
        user.setCellphone(entity.getCellphone());
        user.setUserid(entity.getUserid());
        user.setName(entity.getName());
        user.setSmallUrl(entity.getSmallUrl());
        user.setUrl(entity.getUrl());
        user.setImStatus(entity.getImStatus());

        return user;
    }
}
