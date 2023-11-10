package com.svalero.ontimeapp.contract;

import com.svalero.ontimeapp.domain.User;

/**
 * Declaramos la lógica las view y presenter que une ambas
 */
public interface LoginContract {
    /**
     * Que necesita el model para solicitar a la API
     */
    interface Model {
        interface OnLoginListener { //Creamos un listener para devolver el User si está registrado en la BBDD
            void onLoginSuccess(User user); //Devolvemos el usuario
            void onLoginError(String message);
        }
        void getLogin(String username, String pass, LoginContract.Model.OnLoginListener listener);
    }

    interface View {
        void showError(String errorMessage);
        void showMessage(String message, User user);
    }

    interface Presenter {
        void getLogin(String username, String pass);
    }

}
