package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignOutDtoContract;
import com.svalero.ontimeapp.domain.Dto.SignOutDto;
import com.svalero.ontimeapp.model.SignOutDtoModel;

public class SignOutDtoPresenter implements SignOutDtoContract.presenter, SignOutDtoContract.Model.OnSingOutListener {

   private SignOutDtoModel model;
   private SignOutDtoContract.View view;

    public SignOutDtoPresenter(SignOutDtoContract.View view) {
        this.view = view;
        this.model = new SignOutDtoModel();
    }

    @Override
    public void OnSingOutSucess(SignOutDto signOutDto) {
        view.showMessage("Sign Register Correct");
    }

    @Override
    public void OnSingOutError(String message) {
        view.showError(message);
    }

    @Override
    public void signOut(long id, SignOutDto signOutDto) {
        model.singOut(id, signOutDto, this);
    }
}
