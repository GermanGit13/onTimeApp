package com.svalero.ontimeapp.contract;

import com.svalero.ontimeapp.domain.Sign;

import java.util.List;

/**
 * Declaramos la lógica las view y presenter que une ambas
 */
public interface SignListByParamsContract {

    interface Model {
        interface OnLoadSignsByParamsListener {

            void OnLoadSignsByParamsSucess(List<Sign> signs);
            void OnLoadSignsByParamsError(String message);

        }

        void loadSignsByParams( OnLoadSignsByParamsListener listener, String department, String firstDay);
    }

    /**
     * Que recibe la view después de solicitarle al presenter
     *
     */
    interface View {
        void showSignsByParams(List<Sign> signs);
        void showMessage(String message);
    }


    interface Presenter {
        void loadSignsByParams(String department, String firstDay);
    }
}
