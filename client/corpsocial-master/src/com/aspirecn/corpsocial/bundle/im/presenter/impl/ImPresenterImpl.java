package com.aspirecn.corpsocial.bundle.im.presenter.impl;

import com.aspirecn.corpsocial.bundle.im.presenter.ImPresenter;
import com.aspirecn.corpsocial.bundle.im.view.ImView;
import com.aspirecn.corpsocial.bundle.im.viewModel.ImViewModel;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.util.AssetsUtils;

/**
 * Created by chenziqiang on 15-8-28.
 */
public class ImPresenterImpl implements ImPresenter {
    private ImView view;
    private ImViewModel viewModel;

    public ImPresenterImpl(ImView view){
        this.view=view;
    }

    @Override
    public void doAfterViews() {
        viewModel = AssetsUtils.getInstance().read(AspirecnCorpSocial.getContext(), "im.json",ImViewModel.class);
        view.initView(viewModel.isShowMyGroup);
    }
}
