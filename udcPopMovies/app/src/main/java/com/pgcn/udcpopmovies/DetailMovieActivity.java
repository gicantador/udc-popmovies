package com.pgcn.udcpopmovies;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgcn.udcpopmovies.utils.MovieModel;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();

    private String mTextoNaoFornecido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView mOriginalTitle = findViewById(R.id.tv_movie_title);
        TextView mSynopsis = findViewById(R.id.tv_synopsis);
        TextView mRating = findViewById(R.id.tv_rating);
        TextView mReleaseDate = findViewById(R.id.tv_release_date);
        ImageView mPoster = findViewById(R.id.iv_poster);

        mTextoNaoFornecido = getString(R.string.txt_empty_data);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MovieModel.LB_MOVIE)) {

                MovieModel movie = (MovieModel) intentThatStartedThisActivity.getSerializableExtra(MovieModel.LB_MOVIE);
                if (null != movie) {
//                    Log.d(TAG, " LB_MOVIE SELECIONADO :" + movie.toString());

                    mOriginalTitle.setText((String) getCleanField(movie.getOriginalTitle()));
                    mSynopsis.setText((String) getCleanField(movie.getOverview()));
                    mRating.setText((String) getCleanField(String.valueOf(movie.getVoteAverage())));
                    mReleaseDate.setText((String) getCleanField(formataDataRelease(movie.getDtaReleaseDate(), movie.getReleaseDate())));

                    String imagePath = movie.getPosterPath();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Picasso.with(this).load(imagePath).placeholder(R.drawable.placeholder_empty).into(mPoster);
                    }
                }
            }
        }
    }

    /**
     * Formata a data de release conforme um pattern
     *
     * @param dtaReleaseDate data de release em data
     * @param txtReleaseDate data de release em texto
     * @return data de release formatada
     */
    private String formataDataRelease(Date dtaReleaseDate, String txtReleaseDate) {

        if (null != dtaReleaseDate) {
            final DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            return df.format(dtaReleaseDate);
        }
        return txtReleaseDate;
    }

    /**
     * Verifica se o dado está preenchido e em caso negativo, coloca um texto avisando
     *
     * @param obj o dado a ser verificado
     * @return o dado ou o texto
     */
    private Object getCleanField(Object obj) {
        if (null == obj) {
            return mTextoNaoFornecido;
        } else if (String.class == obj.getClass()) {
            String tst = (String) obj;
            if (tst.trim().isEmpty()) {
                return mTextoNaoFornecido;
            }
        }
        return obj;
    }
}
