package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.domain.KeyValue;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSItemDao;
import com.aspirecn.corpsocial.bundle.workgrp.ui.widget.BBSPraiseListAdapter;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.workgrp_bbs_praiselist)
public class WorkgrpBBSPraiseListActivity extends EventFragmentActivity {
    @ViewById(R.id.workgrp_praiselist_titleid)
    TextView praiselistTitle;
    @ViewById(R.id.workgrp_praiselist_listid)
    ListView praisesListView;
    private BBSItem bbsItem = null;
    //private ContactDao contactDao;
    private UserDao userDao;

    @Click({R.id.workgrp_praiselist_titleid})
    void toDetail() {
        Intent intent = new Intent(this, WorkGrpBBSDetailActivity_.class);
        intent.putExtra("bbsid", bbsItem.getBbsItemEntity().getItemId());
        startActivity(intent);
    }

    @AfterViews
    void doAfterViews() {
        //contactDao = new ContactDao();
        userDao = new UserDao();
        String bbsId = getIntent().getStringExtra("bbsId");
        bbsItem = new BBSItemDao().findItemById(bbsId);
//        praiselistTip.setText("有" + bbsItem.getBbsItemEntity().getPraiseTimes() + "个人觉得这篇帖子很赞");
        ActionBarFragment fab = ActionBarFragment_.builder().
                title("有" + bbsItem.getBbsItemEntity().getPraiseTimes() + "个人觉得这篇帖子很赞").
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        praiselistTitle.setText(bbsItem.getBbsItemEntity().getTitle());
        final ArrayList<KeyValue> pUserNames = new ArrayList<KeyValue>();
//        List<User> contactVO = getcontactVO(bbsItem.getPraiseUseridList());
//        if (contactVO != null && contactVO.size() > 0) {
//            for (User cb : contactVO) {
//                pUserNames.add(new KeyValue(cb.getUserid(), cb.getName()));
//            }
//        } else {
//            if (Integer.valueOf(bbsItem.getBbsItemEntity().getPraiseTimes()) > 0) {
//                for (String userid : bbsItem.getPraiseUseridList()) {
//                    pUserNames.add(new KeyValue(userid, userid));
//                }
//            }
//        }
        BBSPraiseListAdapter adapter = new BBSPraiseListAdapter(this, pUserNames);
        praisesListView.setAdapter(adapter);
        praisesListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				KeyValue kv = bbsItem.getPraisedUserinfos().get(position);
                KeyValue kv = pUserNames.get(position);
                if (kv.getKey().equals(kv.getValue())) {
                    Toast.makeText(getApplicationContext(), "该用户已不存在！", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(
                            WorkgrpBBSPraiseListActivity.this,
                            com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_.class);
                    intent.putExtra("ContactId", kv.getKey());
                    startActivity(intent);
                }
            }
        });

//		final ArrayList<HashMap<String, String>> datas = getDatas(bbsItem
//				.getPraisedUserinfos());
//		SimpleAdapter simpleAdapter = new SimpleAdapter(this, datas,
//				R.layout.workgrp_praises_list_item, new String[] { "name" },
//				new int[] { R.id.workgrp_praises_item_usernameid });
//		praisesListView.setAdapter(simpleAdapter);
//		praisesListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				HashMap<String, String> entity = datas.get(position);
//				if(entity.get("id").equals(entity.get("name"))){
//					Toast.makeText(getApplicationContext(), "该用户已不存在！", Toast.LENGTH_LONG).show();
//				}else{
//					Intent intent = new Intent(
//							WorkgrpBBSPraiseListActivity.this,
//							com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_.class);
//					intent.putExtra("ContactId", entity.get("id"));
//					startActivity(intent);
//				}
//			}
//		});
    }

    private List<User> getcontactVO(ArrayList<String> praiseUserIds) {
        // TODO Auto-generated method stub
        //return contactDao.findByContactIds(Config.getInstance().getUserId(), praiseUserIds);
        List<User> users = new ArrayList<User>();
        try {
            users.addAll(userDao.findFilterByContactIds(Config.getInstance().getUserId(), praiseUserIds));
        } catch (Exception e) {
        }

        return users;
    }
//	private ArrayList<HashMap<String, String>> getDatas(ArrayList<KeyValue> kvs) {
//		ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
//		HashMap<String, String> entity = null;
//		for (KeyValue kv : kvs) {
//			entity = new HashMap<String, String>();
//			entity.put("id", kv.getKey());
//			entity.put("name", kv.getValue());
//			results.add(entity);
//		}
//		return results;
//	}
}
