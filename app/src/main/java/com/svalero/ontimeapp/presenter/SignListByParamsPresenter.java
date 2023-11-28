package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.contract.SignListByParamsContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.model.SignListByParamsModel;
import com.svalero.ontimeapp.view.SignListByParamsView;

import java.util.List;

/**
 * Implementamos el contrato y el listener
 */
public class SignListByParamsPresenter implements SignListByParamsContract.Presenter, SignListByParamsContract.Model.OnLoadSignsByParamsListener {
    /**
     * Le pasamos el model y la view ya que es el único que conoce a ambos
     */
    private SignListByParamsModel model;
    /**
     * Implementar siempre la View del contracto para encapsular bien y poder usarla
     * desde cualquier vview que implemente el contrato
     */
    private SignListByParamsContract.View view;

    public SignListByParamsPresenter(SignListByParamsContract.View view) {
        this.view = view;
        this.model = new SignListByParamsModel();
    }

    @Override
    public void OnLoadSignsByParamsSucess(List<Sign> signs) {
        view.showSignsByParams(signs);
    }

    @Override
    public void OnLoadSignsByParamsError(String message) {
        view.showMessage(message);
    }

    @Override
    public void loadSignsByParams(String department, String firstDay, String seconDay, String name) {
        model.loadSignsByParams(this, department, firstDay, seconDay, name);
    }
}
