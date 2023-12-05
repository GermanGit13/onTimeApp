package com.svalero.ontimeapp.presenter;

import android.content.Context;

import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.model.SignListByUserModel;
import com.svalero.ontimeapp.view.MainActivity;
import com.svalero.ontimeapp.view.SignListByUserView;
import com.svalero.ontimeapp.view.SignRegisterView;

import java.util.List;

public class SignListByUserPresenter implements SignListByUserContract.Presenter, SignListByUserContract.Model.OnLoadSignsByUserListener {

    private SignListByUserModel model;
    /**
     * Implementar siempre la View del contracto para encapsular bien y poder usarla
     * desde cualquier vview que implemente el contrato
     */
    private SignListByUserContract.View view;

    public SignListByUserPresenter(SignListByUserContract.View view) {
        this.view = view;
        this.model = new SignListByUserModel();
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
