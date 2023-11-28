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
    /**
     * Implementar siempre la View del contracto para encapsular bien y poder usarla
     * desde cualquier vview que implemente el contrato
     */
    private SignListContract.View view;

    public SignListPresenter(SignListContract.View view) {
        this.view = view;
        this.model = new SignListModel(); // Le pasamos el contexto
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
    public void loadAllSings(String firstDay, String seconDay, String name) {
        model.loadAllSigns(this, firstDay, seconDay, name);
    }
}
