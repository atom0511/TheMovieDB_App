package com.example.mvpdemo.features.movies;

import com.example.mvpdemo.models.data_models.GetMoviesResponse;

import java.util.List;

//1. create contract
public interface MoviesContract {
    interface View {
        void setDataToRecyclerView(List<GetMoviesResponse.ResultsBean> movies);
        void showErrorToast(String error);
        void showLoadingIndicator();
        void hideLoadingIndicator();
    }

    interface Presenter {
        void getMovies(int page);
    }

    interface Model {
        interface OnFinishedListener {
            void onResponse(List<GetMoviesResponse.ResultsBean> movies);
            void onFailure(String error);
        }

        void getMovies(OnFinishedListener onFinishedListener, int page);
    }
}