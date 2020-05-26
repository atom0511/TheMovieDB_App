package com.example.mvpdemo.features.movies;

import com.example.mvpdemo.api.APIService;
import com.example.mvpdemo.api.RetrofitConfiguration;
import com.example.mvpdemo.models.data_models.GetMoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesModel implements  MoviesContract.Model {
    @Override
    public void getMovies(OnFinishedListener onFinishedListener, int page) {
        APIService service = RetrofitConfiguration.getInstance().create(APIService.class);
        Call<GetMoviesResponse> call = service.getMovies(page);
        call.enqueue(new Callback<GetMoviesResponse>() {
            @Override
            public void onResponse(Call<GetMoviesResponse> call, Response<GetMoviesResponse> response) {
                onFinishedListener.onResponse(response.body().getResults());
            }

            @Override
            public void onFailure(Call<GetMoviesResponse> call, Throwable t) {
                onFinishedListener.onFailure(t.toString());
            }
        });
    }
}
