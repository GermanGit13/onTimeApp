package com.svalero.ontimeapp.presenter;

import android.speech.SpeechRecognizer;

import com.svalero.ontimeapp.contract.LoginContract;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.model.LoginModel;
import com.svalero.ontimeapp.view.LoginView;

public class LoginPresenter implements LoginContract.Presenter, LoginContract.Model.OnLoginListener {
    private LoginModel model;
    private LoginView view;

    public LoginPresenter(LoginView view){
        this.model = new LoginModel();
        this.view = view;
    }

    @Override
    public void onLoginSuccess(User user) {
        view.showMessage("Bienvenido a OnTIme", user);
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
