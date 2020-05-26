package com.example.mvpdemo.features.account;

import retrofit2.Response;

public interface AccountContract {
    interface View {
        void showAccountSection();
        void showLoginSection();
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showErrorFromServer(Response error);
        void showErrorWhenFailure(String message);
    }

    interface Presenter {
        void getSessionId();
        void signIn(String username, String password);
        void signOut();
    }

    interface Model {
        String getSessionId();

        interface OnFinishSignIn {
            void onResponseSignInSuccess();

            void onResponseSignInError(Response response);

            void onFailure(String error);
        }

        void signIn(OnFinishSignIn onFinishSignIn, String username, String password);

        interface OnFinishSignOut {
            void onResponseSignOut(boolean isSuccess);

            void onFailure(String error);
        }

        void signOut(OnFinishSignOut onFinishSignOut);
    }
}
