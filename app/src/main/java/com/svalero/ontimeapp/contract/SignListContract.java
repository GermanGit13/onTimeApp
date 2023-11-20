package com.svalero.ontimeapp.contract;

import com.svalero.ontimeapp.domain.Sign;

import java.util.List;

/**
 * Declaramos la lógica las view y presenter que une ambas
 */
public interface SignListContract {

    /**
     * Que necesita el model para solicitar a la API
     */
    interface Model {
        interface OnLoadSignsListener {
            void onLoadSignsSucess(List<Sign> signs);
            void onLoadSignsError(String message);
        }
        void loadAllSigns(OnLoadSignsListener listener, String firstDay); // quien le llame tiene que tener un listener para que le chive lo que ha pasado
    }

    /**
     * Que recibe la view después de solicitarle al presenter
     */
    interface View {
        void showSigns(List<Sign> signs);
        void showMessage(String message);
    }

    interface Presenter {
        void loadAllSings(String firstDay);
    }
}
