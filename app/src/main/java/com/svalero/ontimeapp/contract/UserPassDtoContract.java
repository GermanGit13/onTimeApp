package com.svalero.ontimeapp.contract;

import com.svalero.ontimeapp.domain.Dto.UserPassDto;

public interface UserPassDtoContract {

    interface Model {
        interface OnUpdatePassListener {
            void onUpdatePassSuccess(UserPassDto userPassDto);
            void onUpdatePassError(String message);
        }

        void updatePass(long id, UserPassDto userPassDto, UserPassDtoContract.Model.OnUpdatePassListener listener);
    }

    interface View {
        void showError(String errorMessage);
        void showMessage(String message);
    }

    interface Presenter {
        void updatePass(long id, UserPassDto userPassDto);
    }
}
