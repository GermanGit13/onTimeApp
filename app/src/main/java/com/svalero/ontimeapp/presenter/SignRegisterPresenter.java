package com.svalero.ontimeapp.presenter;

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
    private SignRegisterView view;

    /**
     * Constructor para pasarle ambas cosas
     */
    public SignRegisterPresenter(SignRegisterView view) {
        this.model = new SignRegisterModel();
        this.view = view;
    }

    @Override
    public void onRegisterSuccess(Sign sign) {
        view.showMessage(view.getString(R.string.sign_register_ok_to));
    }

    @Override
    public void onRegisterError(String message) {
        view.showError("Error to register Sign");
    }

    @Override
    public void registerSign(long userId, Sign sign) {
        model.registerSign(userId, sign, this);
    }
}
