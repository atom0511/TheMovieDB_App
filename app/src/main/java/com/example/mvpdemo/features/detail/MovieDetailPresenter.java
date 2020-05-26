package com.example.mvpdemo.features.detail;

import com.example.mvpdemo.models.data_models.GetMovieAccountStatesResponse;
import com.example.mvpdemo.models.data_models.GetMovieDetailResponse;
import com.example.mvpdemo.models.share_pref.AccountSharePref;

import retrofit2.Response;

public class MovieDetailPresenter implements MovieDetailContract.Presenter,
        MovieDetailContract.Model.OnFinishUpdateFavouriteMovie,
        MovieDetailContract.Model.OnFinishGetMovieDetail,
        MovieDetailContract.Model.OnFinishGetMovieAccountStates {

    MovieDetailContract.View view;
    MovieDetailContract.Model model;

    public MovieDetailPresenter(MovieDetailContract.View view, AccountSharePref accountSharePref) {
        this.view = view;
        this.model = new MovieDetailModel(accountSharePref);
    }

    @Override
    public void getSessionId() {
        view.showFavouriteIcon(model.getSessionId() != null);
    }

    @Override
    public void updateFavouriteMovie(int movieId, boolean isFavourite) {
        view.showLoadingIndicator();
        model.updateFavouriteMovie(this, movieId, isFavourite);
    }

    @Override
    public void getMovieDetail(int movieId) {
        view.showLoadingIndicator();
        model.getMovieDetail(this, movieId);
    }

    @Override
    public void onResponseUpdateFavouriteMovie(boolean isSuccess, Response response, boolean isFavourite) {
        view.hideLoadingIndicator();
        if (isSuccess) {
            view.updateFavouriteIcon(!isFavourite);
        } else {
            view.showErrorFromServer(response);
        }
    }

    @Override
    public void onResponseGetMovieDetail(boolean isSuccess, Response<GetMovieDetailResponse> response, boolean isAuth) {
        view.hideLoadingIndicator();
        if (response.code() == 200) {
            view.showMovieDetail(response.body());
            if (isAuth) {
                view.showLoadingIndicator();
                model.getMovieAccountStates(this, response.body().getId());
            }
        } else {
            view.showErrorFromServer(response);
            view.onBackPressed();
        }
    }

    @Override
    public void onResponseGetMovieAccountStates(boolean isSuccess, Response<GetMovieAccountStatesResponse> response) {
        view.hideLoadingIndicator();
        if (isSuccess) {
            view.updateFavouriteIcon(response.body().isFavorite());
        } else {
            view.showErrorFromServer(response);
        }
    }

    @Override
    public void onFailure(String error) {
        view.hideLoadingIndicator();
        view.showErrorWhenFailure(error);
    }
}
