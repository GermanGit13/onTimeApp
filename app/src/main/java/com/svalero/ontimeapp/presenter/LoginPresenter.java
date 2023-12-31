package com.svalero.ontimeapp.presenter;

import android.speech.SpeechRecognizer;

import com.svalero.ontimeapp.contract.LoginContract;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.model.LoginModel;
import com.svalero.ontimeapp.view.LoginView;

public class LoginPresenter implements LoginContract.Presenter, LoginContract.Model.OnLoginListener {
    private LoginModel model;
    /**
     * Implementar siempre la View del contracto para encapsular bien y poder usarla
     * desde cualquier vview que implemente el contrato
     */
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view){
        this.model = new LoginModel();
        this.view = view;
    }

    @Override
    public void onLoginSuccess(User user) {
        view.showLogin(user );
    }

    @Override
    public void onLoginError(String message) {
        view.showError("Usuario o Contraseña Incorrectos");
    }

    @Override
    public void getLogin(String username, String pass) {
        model.getLogin(username, pass, this);
    }
}
