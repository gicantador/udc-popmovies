package com.pgcn.udcpopmovies;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgcn.udcpopmovies.utils.MovieModel;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();

    private TextView mOriginalTitle;
    private ImageView mPoster;
    private TextView mSynopsis;
    private TextView mRating;
    private TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mOriginalTitle = (TextView) findViewById(R.id.tv_movie_title);
        mSynopsis = (TextView) findViewById(R.id.tv_synopsis);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mPoster = (ImageView) findViewById(R.id.iv_poster);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("Movie")) {

                MovieModel movie = (MovieModel) intentThatStartedThisActivity.getSerializableExtra("Movie");
                if (null != movie) {
//                    Log.d(TAG, " MOVIE SELECIONADO :" + movie.toString());

                    mOriginalTitle.setText(movie.getOriginalTitle());
                    mSynopsis.setText(movie.getOverview());
                    mRating.setText(String.valueOf(movie.getVoteAverage()));
                    mReleaseDate.setText(formataDataRelease(movie.getDtaReleaseDate()));

                    String imagePath = movie.getPosterPath();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Picasso.with(this).load(imagePath).placeholder(R.drawable.progress_animation).into(mPoster);
                    }
                }
            }
        }
    }

    private String formataDataRelease(Date dtaReleaseDate) {

        if (null != dtaReleaseDate) {
            final DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            return df.format(dtaReleaseDate);
        }
        return StringUtils.EMPTY;
    }
}
