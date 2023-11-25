package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.model.SignListByUserModel;
import com.svalero.ontimeapp.view.SignListByUserView;

import java.util.List;

public class SignListByUserPresenter implements SignListByUserContract.Presenter, SignListByUserContract.Model.OnLoadSignsByUserListener {

    private SignListByUserModel model;
    private SignListByUserView view;

    public SignListByUserPresenter(SignListByUserView view) {
        this.view = view;
        this.model = new SignListByUserModel(view.getApplicationContext());
    }

    @Override
    public void OnLoadSignsByUserListenerSucess(List<Sign> signs) {
        view.showSignsByUser(signs);
    }

    @Override
    public void OnLoadSignsByUserListenerError(String message) {
        view.showMessage(message);
    }

    @Override
    public void loadSignsByUser(String userId, String firstDay, String secondDay) {
        model.loadSignsByUser(userId, this, firstDay, secondDay);
    }
}
