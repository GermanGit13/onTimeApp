package com.svalero.ontimeapp.contract;

import com.svalero.ontimeapp.domain.Dto.SignOutDto;

public interface SignOutDtoContract {

    interface Model {
        interface OnSingOutListener {
            void OnSingOutSucess(SignOutDto signOutDto);
            void OnSingOutError(String message);
        }
        void singOut(long id, SignOutDto signOutDto, SignOutDtoContract.Model.OnSingOutListener listener);
    }

    interface View {
        void showError(String errorMessage);
        void showMessage(String message);
    }

    interface presenter {
        void signOut(long id, SignOutDto signOutDto);
    }
}
