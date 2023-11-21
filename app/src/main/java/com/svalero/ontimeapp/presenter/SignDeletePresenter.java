package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.adapter.SignAdapter;
import com.svalero.ontimeapp.contract.SignDeleteContract;
import com.svalero.ontimeapp.model.SignDeleteModel;

public class SignDeletePresenter implements SignDeleteContract.Presenter, SignDeleteContract.Model.onDeleteSignListener {

    private SignDeleteModel model;
    private SignAdapter view; // La view es el adapter porque realizamos el borrado desde un boton

    public SignDeletePresenter(SignAdapter view) {
        model = new SignDeleteModel();
        this.view = view;
    }

    @Override
    public void onDeleteSuccess() {
        view.showMessage("Sign deleted correctly");
    }

    @Override
    public void onDeleteError(String message) {
        view.showError("Error to deleted sing");
    }

    @Override
    public void deleteSign(String signId) {
        model.deleteSing(signId, this);
    }
}
