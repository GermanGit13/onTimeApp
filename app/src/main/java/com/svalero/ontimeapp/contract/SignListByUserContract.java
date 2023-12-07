package com.svalero.ontimeapp.contract;

import com.svalero.ontimeapp.domain.Sign;

import java.util.List;

public interface SignListByUserContract {

    interface Model {
        interface OnLoadSignsByUserListener{
            void OnLoadSignsByUserListenerSucess(List<Sign> signs);
            void OnLoadSignsByUserListenerError(String message);
        }

        void loadSignsByUser(String userId, OnLoadSignsByUserListener listener, String firstDay, String secondDay);
    }

    interface View {
        void showSignsByUser(List<Sign> signs);
        void showMessage(String message);
    }

    interface Presenter {
        void loadSignsByUser(String userId, String firstDay, String secondDay);
    }
}
