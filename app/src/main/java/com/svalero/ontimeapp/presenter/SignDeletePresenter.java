package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.R;
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
        view.showMessage(String.valueOf(R.string.sign_deleted_correctly));
    }

    @Override
    public void onDeleteError(String message) {
        view.showError(String.valueOf(R.string.error_to_deleted_sing));
    }

    @Override
    public void deleteSign(String signId) {
        model.deleteSing(signId, this);
    }
}
