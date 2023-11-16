package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.contract.SignListContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.model.SignListModel;
import com.svalero.ontimeapp.view.SignListView;

import java.util.List;

/**
 * Implementamos el contrato y el listener
 */
public class SignListPresenter implements SignListContract.Presenter, SignListContract.Model.OnLoadSignsListener {

    /**
     * Le pasamos el model y la view ya que es el único que conoce a ambos
     */
    private SignListModel model;
    private SignListView view;
    private String department;

    public SignListPresenter(SignListView view) {
        this.view = view;
        this.model = new SignListModel(view.getApplicationContext()); // Le pasamos el contexto
    }

    @Override
    public void onLoadSignsSucess(List<Sign> signs) {
        view.showSigns(signs);
    }

    @Override
    public void onLoadSignsError(String message) {
        view.showMessage(message);
    }

    @Override
    public void loalAllSigns() {
        model.loadAllSigns(this);
    }

//    @Override
//    public void loadSignsByDeparment(String department) {
//        model.loadSignsByDeparment(this, department);
//    }
//
//    @Override
//    public void OnLoadSignsByDepartmentSucess(List<Sign> signs) {
//        view.showSignsByDepartment(signs);
//    }
//
//    @Override
//    public void OnLoadSignsByDepartmentError(String message) {
//        view.showMessage(message);
//    }
}
