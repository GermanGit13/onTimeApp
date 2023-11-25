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
    private SignListByParamsView view;

    public SignListByParamsPresenter(SignListByParamsView view) {
        this.view = view;
        this.model = new SignListByParamsModel(view.getApplicationContext());
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
