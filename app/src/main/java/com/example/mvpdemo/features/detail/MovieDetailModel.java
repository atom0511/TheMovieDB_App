package com.example.mvpdemo.features.detail;

import com.example.mvpdemo.api.APIService;
import com.example.mvpdemo.api.RetrofitConfiguration;
import com.example.mvpdemo.models.data_models.GetMovieAccountStatesResponse;
import com.example.mvpdemo.models.data_models.GetMovieDetailResponse;
import com.example.mvpdemo.models.data_models.SetFavouriteMovieRequest;
import com.example.mvpdemo.models.data_models.SetFavouriteMovieResponse;
import com.example.mvpdemo.models.share_pref.AccountSharePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailModel implements MovieDetailContract.Model {

    AccountSharePref accountSharePref;
    APIService service;

    public MovieDetailModel(AccountSharePref accountSharePref) {
        this.accountSharePref = accountSharePref;
        this.service = RetrofitConfiguration.getInstance().create(APIService.class);
    }

    @Override
    public String getSessionId() {
        return accountSharePref.getSessionId();
    }

    @Override
    public void updateFavouriteMovie(OnFinishUpdateFavouriteMovie onFinishUpdateFavouriteMovie, int movieId, boolean isFavourite) {
        SetFavouriteMovieRequest body = new SetFavouriteMovieRequest(movieId, !isFavourite);
        Call<SetFavouriteMovieResponse> call = service.setFavouriteMovie(body, accountSharePref.getSessionId());
        call.enqueue(new Callback<SetFavouriteMovieResponse>() {
            @Override
            public void onResponse(Call<SetFavouriteMovieResponse> call, Response<SetFavouriteMovieResponse> response) {
                onFinishUpdateFavouriteMovie.onResponseUpdateFavouriteMovie(
                                response.code() == 200 || response.code() == 201,
                                response,
                                isFavourite);
            }

            @Override
            public void onFailure(Call<SetFavouriteMovieResponse> call, Throwable t) {
                onFinishUpdateFavouriteMovie.onFailure(t.toString());
            }
        });
    }

    @Override
    public void getMovieDetail(OnFinishGetMovieDetail onFinishGetMovieDetail, int movieId) {
        Call<GetMovieDetailResponse> call = service.getMovieDetail(movieId);
        call.enqueue(new Callback<GetMovieDetailResponse>() {
            @Override
            public void onResponse(Call<GetMovieDetailResponse> call, Response<GetMovieDetailResponse> response) {
                onFinishGetMovieDetail.onResponseGetMovieDetail(
                        response.code() == 200,
                        response,
                        accountSharePref.getSessionId() != null
                );
            }

            @Override
            public void onFailure(Call<GetMovieDetailResponse> call, Throwable t) {
                onFinishGetMovieDetail.onFailure(t.toString());
            }
        });
    }

    @Override
    public void getMovieAccountStates(OnFinishGetMovieAccountStates onFinishGetMovieAccountStates, int movieId) {
        Call<GetMovieAccountStatesResponse> call = service.getMovieAccountStates(
                movieId,
                accountSharePref.getSessionId()
        );
        call.enqueue(new Callback<GetMovieAccountStatesResponse>() {
            @Override
            public void onResponse(Call<GetMovieAccountStatesResponse> call, Response<GetMovieAccountStatesResponse> response) {
                onFinishGetMovieAccountStates.onResponseGetMovieAccountStates(
                        response.code() == 200,
                        response
                );
            }

            @Override
            public void onFailure(Call<GetMovieAccountStatesResponse> call, Throwable t) {
                onFinishGetMovieAccountStates.onFailure(t.toString());
            }
        });
    }
}
