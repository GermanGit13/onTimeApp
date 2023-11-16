package com.svalero.ontimeapp.contract;

import com.svalero.ontimeapp.domain.Sign;

import java.util.List;

/**
 * Declaramos la lógica las view y presenter que une ambas
 */
public interface SignListByDepartmentContract {

    interface Model {
        interface OnLoadSignsByDepartmentListener {
            void OnLoadSignsByDepartmentSucess(List<Sign> signs);
            void OnLoadSignsByDepartmentError(String message);
        }

        void loadSignsByDepartment( OnLoadSignsByDepartmentListener listener, String department);
    }

    /**
     * Que recibe la view después de solicitarle al presenter
     */
    interface View {
        void showSignsByDepartment(List<Sign> signs);
        void showMessage(String message);
    }

    interface Presenter {
        void loadSignsByDepartment(String department);
    }
}
