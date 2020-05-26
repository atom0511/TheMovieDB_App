package com.example.mvpdemo.features.account;

import com.example.mvpdemo.api.APIService;
import com.example.mvpdemo.api.RetrofitConfiguration;
import com.example.mvpdemo.models.data_models.DeleteSessionIdRequest;
import com.example.mvpdemo.models.data_models.DeleteSessionIdResponse;
import com.example.mvpdemo.models.data_models.GetCreateRequestTokenResponse;
import com.example.mvpdemo.models.data_models.PostCreateSessionRequest;
import com.example.mvpdemo.models.data_models.PostCreateSessionResponse;
import com.example.mvpdemo.models.data_models.PostCreateSessionWithLoginRequest;
import com.example.mvpdemo.models.data_models.PostCreateSessionWithLoginResponse;
import com.example.mvpdemo.models.share_pref.AccountSharePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountModel implements AccountContract.Model {

    AccountSharePref accountSharePref;
    APIService service;

    public AccountModel(AccountSharePref accountSharePref) {
        this.accountSharePref = accountSharePref;
        this.service = RetrofitConfiguration.getInstance().create(APIService.class);
    }

    @Override
    public String getSessionId() {
        return accountSharePref.getSessionId();
    }

    @Override
    public void signIn(OnFinishSignIn onFinishSignIn, String username, String password) {
        createRequestToken(onFinishSignIn, username, password);
    }

    @Override
    public void signOut(OnFinishSignOut onFinishSignOut) {
        DeleteSessionIdRequest body = new DeleteSessionIdRequest();
        body.setSession_id(accountSharePref.getSessionId());
        Call<DeleteSessionIdResponse> call = service.deleteSessionId(body);
        call.enqueue(new Callback<DeleteSessionIdResponse>() {
            @Override
            public void onResponse(Call<DeleteSessionIdResponse> call, Response<DeleteSessionIdResponse> response) {
                if (response.code() == 200) {
                    onFinishSignOut.onResponseSignOut(true);
                    accountSharePref.saveSessionId(null);
                } else {
                    onFinishSignOut.onResponseSignOut(false);
                }
            }

            @Override
            public void onFailure(Call<DeleteSessionIdResponse> call, Throwable t) {
                onFinishSignOut.onFailure(t.toString());
            }
        });
    }

    private void createRequestToken(OnFinishSignIn onFinishSignIn, String username, String password) {
        Call<GetCreateRequestTokenResponse> call = service.getCreateRequestToken();
        call.enqueue(new Callback<GetCreateRequestTokenResponse>() {
            @Override
            public void onResponse(Call<GetCreateRequestTokenResponse> call, Response<GetCreateRequestTokenResponse> response) {
                if (response.code() == 200) {
                    createSessionWithLogin(onFinishSignIn, response.body().getRequest_token(), username, password);
                } else {
                    onFinishSignIn.onResponseSignInError(response);
                }
            }

            @Override
            public void onFailure(Call<GetCreateRequestTokenResponse> call, Throwable t) {
                onFinishSignIn.onFailure(t.toString());
            }
        });
    }

    private void createSessionWithLogin(OnFinishSignIn onFinishSignIn, String token, String username, String password) {
        PostCreateSessionWithLoginRequest body = new PostCreateSessionWithLoginRequest();
        body.setUsername(username);
        body.setPassword(password);
        body.setRequest_token(token);

        Call<PostCreateSessionWithLoginResponse> call = service.postCreateSessionWithLogin(body);
        call.enqueue(new Callback<PostCreateSessionWithLoginResponse>() {
            @Override
            public void onResponse(Call<PostCreateSessionWithLoginResponse> call, Response<PostCreateSessionWithLoginResponse> response) {
                if (response.code() == 200) {
                    createSession(onFinishSignIn, response.body().getRequest_token());
                } else {
                    onFinishSignIn.onResponseSignInError(response);
                }
            }

            @Override
            public void onFailure(Call<PostCreateSessionWithLoginResponse> call, Throwable t) {
                onFinishSignIn.onFailure(t.toString());
            }
        });
    }

    private void createSession(OnFinishSignIn onFinishSignIn, String token) {
        PostCreateSessionRequest body = new PostCreateSessionRequest();
        body.setRequest_token(token);

        Call<PostCreateSessionResponse> call = service.postCreateSession(body);
        call.enqueue(new Callback<PostCreateSessionResponse>() {
            @Override
            public void onResponse(Call<PostCreateSessionResponse> call, Response<PostCreateSessionResponse> response) {
                if (response.code() == 200) {
                    onFinishSignIn.onResponseSignInSuccess();
                    accountSharePref.saveSessionId(response.body().getSession_id());
                } else {
                    onFinishSignIn.onResponseSignInError(response);
                }
            }

            @Override
            public void onFailure(Call<PostCreateSessionResponse> call, Throwable t) {
                onFinishSignIn.onFailure(t.toString());
            }
        });
    }
}
