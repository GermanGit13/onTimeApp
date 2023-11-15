package com.svalero.ontimeapp.contract;

import com.svalero.ontimeapp.domain.Sign;

/**
 * Declaramos la lógica las view y presenter que une ambas
 */
public interface SignRegisterContract {

    interface Model {
        interface OnRegisterSignListener { // Creamos un listener para devolver el Equipo creado  si xtodo va bien o el error si va mal
            void onRegisterSuccess(Sign sign);
            void onRegisterError(String message);
        }

        void registerSign(long userId, Sign sign, OnRegisterSignListener listener);
    }

    interface View {
        void showError(String errorMessage);
        void showMessage(String message);
        void resetForm(); // Parar resetear los datos de la ventana
    }

    interface Presenter {
        void registerSign(long userId, Sign sign);
    }
}
