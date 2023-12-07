package com.svalero.ontimeapp.presenter;

import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.UserPassDtoContract;
import com.svalero.ontimeapp.domain.Dto.UserPassDto;
import com.svalero.ontimeapp.model.UserPassDtoModel;

public class UserPassDtoPresenter implements UserPassDtoContract.Presenter, UserPassDtoContract.Model.OnUpdatePassListener {
    private UserPassDtoModel model;
    private UserPassDtoContract.View view;

    public UserPassDtoPresenter(UserPassDtoContract.View view) {
        this.view = view;
        this.model = new UserPassDtoModel();
    }

    @Override
    public void onUpdatePassSuccess(UserPassDto userPassDto) {
        view.showMessage(String.valueOf(R.string.password_update_correct));
    }

    @Override
    public void onUpdatePassError(String message) {
        view.showError(message);

    }

    @Override
    public void updatePass(long id, UserPassDto userPassDto) {
        model.updatePass(id, userPassDto, this);
    }
}
