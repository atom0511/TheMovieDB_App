package com.example.mvpdemo.features.movies;

import com.example.mvpdemo.models.data_models.GetMoviesResponse;

import java.util.List;

//2. create presenter
public class MoviesPresenter implements MoviesContract.Presenter, MoviesContract.Model.OnFinishedListener {

    private MoviesContract.View view;
    private MoviesContract.Model model;

    public MoviesPresenter(MoviesContract.View view) {
        this.view = view;
        this.model = new MoviesModel();
    }

    @Override
    public void getMovies(int page) {
        view.showLoadingIndicator();
        model.getMovies(this, page);
    }

    @Override
    public void onResponse(List<GetMoviesResponse.ResultsBean> movies) {
        view.setDataToRecyclerView(movies);
        view.hideLoadingIndicator();
    }

    @Override
    public void onFailure(String error) {
        view.showErrorToast(error);
        view.hideLoadingIndicator();
    }
}
