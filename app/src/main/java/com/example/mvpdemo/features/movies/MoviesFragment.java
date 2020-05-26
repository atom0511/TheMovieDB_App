package com.example.mvpdemo.features.movies;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvpdemo.BaseFragment;
import com.example.mvpdemo.R;
import com.example.mvpdemo.models.data_models.GetMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
//3. create view
public class MoviesFragment extends BaseFragment implements MoviesContract.View {

    @BindView(R.id.rv_movies)
    RecyclerView rvMovies;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private MoviesAdapter moviesAdapter;
    private List<GetMoviesResponse.ResultsBean> movies = new ArrayList<>();

    private MoviesPresenter moviesPresenter;

    private int page = 1;
    private int totalItemCount, lastVisibleItem;
    private int visibleThreshold = 5;
    private boolean isLoading;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onViewCreated(View rootView) {
        moviesPresenter = new MoviesPresenter(this);

        setupUI();
        loadData();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_movies;
    }

    private void loadData() {
        moviesPresenter.getMovies(page);
    }

    private void setupUI() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvMovies.setLayoutManager(gridLayoutManager);

        moviesAdapter = new MoviesAdapter(movies);
        rvMovies.setAdapter(moviesAdapter);

        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = gridLayoutManager.getItemCount();
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && lastVisibleItem >= totalItemCount - visibleThreshold) {
                    page++;
                    loadData();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public void setDataToRecyclerView(List<GetMoviesResponse.ResultsBean> movies) {
        isLoading = false;
        this.movies.addAll(movies);
        moviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorToast(String error) {
        isLoading = false;
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingIndicator() {
        llLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        llLoading.setVisibility(View.GONE);
    }
}
