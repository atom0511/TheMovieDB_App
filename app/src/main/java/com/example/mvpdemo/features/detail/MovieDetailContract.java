package com.example.mvpdemo.features.detail;

import com.example.mvpdemo.models.data_models.GetMovieAccountStatesResponse;
import com.example.mvpdemo.models.data_models.GetMovieDetailResponse;

import retrofit2.Response;

public interface MovieDetailContract {
    interface View {
        void showFavouriteIcon(boolean isShown);
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showErrorFromServer(Response error);
        void showErrorWhenFailure(String message);
        void showMovieDetail(GetMovieDetailResponse movie);
        void updateFavouriteIcon(boolean isFavourite);
        void onBackPressed();
    }

    interface Presenter {
        void getSessionId();
        void updateFavouriteMovie(int movieId, boolean isFavourite);
        void getMovieDetail(int movieId);
    }

    interface Model {
        String getSessionId();

        interface OnFinishUpdateFavouriteMovie {
            void onResponseUpdateFavouriteMovie(boolean isSuccess, Response response, boolean isFavourite);
            void onFailure(String error);
        }

        void updateFavouriteMovie(OnFinishUpdateFavouriteMovie onFinishUpdateFavouriteMovie, int movieId, boolean isFavourite);

        interface OnFinishGetMovieDetail {
            void onResponseGetMovieDetail(boolean isSuccess, Response<GetMovieDetailResponse> response, boolean isAuth);
            void onFailure(String error);
        }

        void getMovieDetail(OnFinishGetMovieDetail onFinishGetMovieDetail, int movieId);

        interface OnFinishGetMovieAccountStates {
            void onResponseGetMovieAccountStates(boolean isSuccess, Response<GetMovieAccountStatesResponse> response);
            void onFailure(String error);
        }

        void getMovieAccountStates(OnFinishGetMovieAccountStates onFinishGetMovieAccountStates, int movieId);
    }
}
