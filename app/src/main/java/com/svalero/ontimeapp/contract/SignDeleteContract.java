package com.svalero.ontimeapp.contract;

public interface SignDeleteContract {

    interface Model {
        interface onDeleteSignListener{
            void onDeleteSuccess();
            void onDeleteError(String message);
        }
        void deleteSing(String signId, onDeleteSignListener listener);
    }

    interface View {
        void showError(String message);
        void showMessage(String message);
    }

    interface Presenter {
        void deleteSign(String id);
    }
}
