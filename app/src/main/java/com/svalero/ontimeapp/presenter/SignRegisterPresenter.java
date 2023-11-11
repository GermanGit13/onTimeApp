package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.model.SignRegisterModel;
import com.svalero.ontimeapp.view.SignRegisterView;

/**
 * Implementamos el contrato y el listener
 */
public class SignRegisterPresenter implements SignRegisterContract.Presenter, SignRegisterContract.Model {

    /**
     * Le pasamos el model y la view ya que es el único que conoce a ambos
     */
    private SignRegisterModel model;
    private SignRegisterView view;

    /**
     * Constructor para pasarle ambas cosas
     */
    public SignRegisterPresenter(SignRegisterModel model, SignRegisterView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void registerSign(long userId, Sign sign, OnRegisterSignListener listener) {
        view.showMessage("Fichaje del día: " + sign.getDay() + " se ha registrado correctamente");
    }

    @Override
    public void registerSign(long userId, Sign sign) {
        view.showError("Se ha producido un error al registrar el fichaje. Intentelo más tarde o contacte con su responsable");
    }
}
