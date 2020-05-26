package com.example.mvpdemo.features.detail;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.example.mvpdemo.R;
import com.example.mvpdemo.api.RetrofitConfiguration;
import com.example.mvpdemo.models.data_models.GetMovieDetailResponse;
import com.example.mvpdemo.models.share_pref.AccountSharePref;
import com.example.mvpdemo.utils.Utils;

import java.text.NumberFormat;
import java.util.Currency;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_genres)
    TextView tvGenres;
    @BindView(R.id.tv_rating_score)
    TextView tvRatingScore;
    @BindView(R.id.rb_rating_star)
    RatingBar rbRatingStar;
    @BindView(R.id.tv_vote_count)
    TextView tvVoteCount;
    @BindView(R.id.tv_run_time)
    TextView tvRunTime;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    @BindView(R.id.tv_revenue)
    TextView tvRevenue;
    @BindView(R.id.tv_prod_companies)
    TextView tvProdCompanies;
    @BindView(R.id.tv_prod_countries)
    TextView tvProdCountries;
    @BindView(R.id.ns_detail)
    NestedScrollView nsDetail;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.iv_favourite)
    ImageView ivFavourite;

    private MovieDetailPresenter movieDetailPresenter;
    private boolean isFavourite;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        movieDetailPresenter = new MovieDetailPresenter(this, new AccountSharePref(this));
        movieDetailPresenter.getSessionId();

        setupUI();
        loadData();
    }

    private void loadData() {
        movieId = getIntent().getIntExtra("movie_id", -1);
        movieDetailPresenter.getMovieDetail(movieId);
    }

    private void setupUI() {
        //1. get screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        //2. set max height for NestedScrollView
        ViewGroup.LayoutParams params = nsDetail.getLayoutParams();
        params.height = (int) (screenHeight * 0.85);
        nsDetail.setLayoutParams(params);
    }

    @OnClick({R.id.ll_watch_trailer, R.id.iv_back, R.id.iv_favourite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_watch_trailer:
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_favourite:
                movieDetailPresenter.updateFavouriteMovie(movieId, isFavourite);
                break;
        }
    }

    @Override
    public void showFavouriteIcon(boolean isShown) {
        if (isShown) {
            ivFavourite.setVisibility(View.VISIBLE);
        } else {
            ivFavourite.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showLoadingIndicator() {
        llLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        llLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorFromServer(Response error) {
        Utils.showErrorFromServer(error, this);
    }

    @Override
    public void showErrorWhenFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMovieDetail(GetMovieDetailResponse movie) {
        Glide.with(this)
                .load(RetrofitConfiguration.getImageBaseUrlOriginal() + movie.getPoster_path())
                .centerCrop()
                .into(ivBackground);

        tvTitle.setText(movie.getTitle());

        StringBuilder genres = new StringBuilder();
        for (int i = 0; i < movie.getGenres().size(); i++) {
            genres.append(movie.getGenres().get(i).getName());
            if (i != movie.getGenres().size() - 1) {
                genres.append(", ");
            }
        }
        tvGenres.setText(genres);

        tvRatingScore.setText(String.valueOf(movie.getVote_average()));
        rbRatingStar.setProgress((int) movie.getVote_average());
        tvVoteCount.setText(String.valueOf(movie.getVote_count()));
        tvRunTime.setText(movie.getRuntime() + "m");

        tvReleaseDate.setText(movie.getRelease_date());
        tvOverview.setText(movie.getOverview());

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setCurrency(Currency.getInstance("USD"));
        tvRevenue.setText(numberFormat.format(movie.getRevenue()));

        StringBuilder prodCompanies = new StringBuilder();
        for (int i = 0; i < movie.getProduction_companies().size(); i++) {
            prodCompanies.append(movie.getProduction_companies().get(i).getName());
            if (i != movie.getProduction_companies().size() - 1) {
                prodCompanies.append(", ");
            }
        }
        tvProdCompanies.setText(prodCompanies);

        StringBuilder prodCountries = new StringBuilder();
        for (int i = 0; i < movie.getProduction_countries().size(); i++) {
            prodCountries.append(movie.getProduction_countries().get(i).getName());
            if (i != movie.getProduction_countries().size() - 1) {
                prodCountries.append(", ");
            }
        }
        tvProdCountries.setText(prodCountries);
    }

    @Override
    public void updateFavouriteIcon(boolean isFavourite) {
        this.isFavourite = isFavourite;
        if (isFavourite) {
            ivFavourite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            ivFavourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
