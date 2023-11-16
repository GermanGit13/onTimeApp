package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.contract.SignListByDepartmentContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.model.SignListByDepartmentModel;
import com.svalero.ontimeapp.view.SignListByDepartmenView;

import java.util.List;

/**
 * Implementamos el contrato y el listener
 */
public class SignListByDepartmentPresenter implements SignListByDepartmentContract.Presenter, SignListByDepartmentContract.Model.OnLoadSignsByDepartmentListener {
    /**
     * Le pasamos el model y la view ya que es el único que conoce a ambos
     */
    private SignListByDepartmentModel model;
    private SignListByDepartmenView view;

    public SignListByDepartmentPresenter(SignListByDepartmenView view) {
        this.view = view;
        this.model = new SignListByDepartmentModel(view.getApplicationContext());
    }

    @Override
    public void OnLoadSignsByDepartmentSucess(List<Sign> signs) {
        view.showSignsByDepartment(signs);
    }

    @Override
    public void OnLoadSignsByDepartmentError(String message) {
        view.showMessage(message);
    }

    @Override
    public void loadSignsByDepartment(String department) {
        model.loadSignsByDepartment(this, department);
    }
}
