package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.model.SignListByUserModel;
import com.svalero.ontimeapp.view.MainActivity;
import com.svalero.ontimeapp.view.SignListByUserView;
import com.svalero.ontimeapp.view.SignRegisterView;

import java.util.List;

public class SignListByUserPresenter implements SignListByUserContract.Presenter, SignListByUserContract.Model.OnLoadSignsByUserListener {

    private SignListByUserModel model;
    private SignListByUserView view;
    private SignRegisterView viewRegister;
    private MainActivity viewMain;

    public SignListByUserPresenter(SignListByUserView view) {
        this.view = view;
        this.model = new SignListByUserModel(view.getApplicationContext());
    }

    public SignListByUserPresenter(MainActivity viewMain) {
        this.viewMain = viewMain;
        this.model = new SignListByUserModel(viewMain.getApplicationContext());
    }

    public SignListByUserPresenter(SignRegisterView viewRegister) {
        this.viewRegister = viewRegister;
        this.model = new SignListByUserModel(viewRegister.getApplicationContext());
    }

    @Override
    public void OnLoadSignsByUserListenerSucess(List<Sign> signs) {

        if (view != null){
            view.showSignsByUser(signs);
        }
        if (viewMain !=null) {
            viewMain.showSignsByUser(signs);
        }
        if (viewRegister !=null)
            viewRegister.showSignsByUser(signs);
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
