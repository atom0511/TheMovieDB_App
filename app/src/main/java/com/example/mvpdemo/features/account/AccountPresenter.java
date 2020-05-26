package com.example.mvpdemo.features.account;

import com.example.mvpdemo.models.share_pref.AccountSharePref;

import retrofit2.Response;

public class AccountPresenter implements AccountContract.Presenter,
        AccountContract.Model.OnFinishSignIn,
        AccountContract.Model.OnFinishSignOut {

    AccountContract.View view;
    AccountContract.Model model;

    public AccountPresenter(AccountContract.View view, AccountSharePref accountSharePref) {
        this.view = view;
        this.model = new AccountModel(accountSharePref);
    }

    @Override
    public void getSessionId() {
        if (model.getSessionId() == null) {
            view.showLoginSection();
        } else {
            view.showAccountSection();
        }
    }

    @Override
    public void signIn(String username, String password) {
        view.showLoadingIndicator();
        model.signIn(this, username, password);
    }

    @Override
    public void signOut() {
        view.showLoadingIndicator();
        model.signOut(this);
    }

    @Override
    public void onResponseSignInSuccess() {
        view.hideLoadingIndicator();
        view.showAccountSection();
    }

    @Override
    public void onResponseSignInError(Response response) {
        view.hideLoadingIndicator();
        view.showErrorFromServer(response);
    }

    @Override
    public void onResponseSignOut(boolean isSuccess) {
        view.hideLoadingIndicator();
        if (isSuccess) view.showLoginSection();
    }

    @Override
    public void onFailure(String error) {
        view.hideLoadingIndicator();
        view.showErrorWhenFailure(error);
    }
}
