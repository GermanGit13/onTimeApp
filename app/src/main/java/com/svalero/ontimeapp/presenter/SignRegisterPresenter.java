package com.svalero.ontimeapp.presenter;

import android.content.Context;

import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.model.SignRegisterModel;
import com.svalero.ontimeapp.view.SignRegisterView;

/**
 * Implementamos el contrato y el listener
 */
public class SignRegisterPresenter implements SignRegisterContract.Presenter, SignRegisterContract.Model.OnRegisterSignListener {

    /**
     * Le pasamos el model y la view ya que es el único que conoce a ambos
     */
    private SignRegisterModel model;
    /**
     * Implementar siempre la View del contracto para encapsular bien y poder usarla
     * desde cualquier vview que implemente el contrato
     */
    private SignRegisterContract.View view;

    /**
     * Constructor para pasarle ambas cosas
     */
    public SignRegisterPresenter(SignRegisterContract.View view) {
        this.model = new SignRegisterModel();
        this.view = view;
    }

    @Override
    public void onRegisterSuccess(Sign sign) {
        view.showMessage("Sign Register Correct");
    }

    @Override
    public void onRegisterError(String message) {
        view.showError(String.valueOf(R.string.error_to_register_sign));
    }

    @Override
    public void registerSign(long userId, Sign sign) {
        model.registerSign(userId, sign, this);
    }
}
