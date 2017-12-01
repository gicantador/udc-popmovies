package com.pgcn.udcpopmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pgcn.udcpopmovies.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Giselle on 12/11/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    // a lista de movies
    private ArrayList<MovieModel> mMovieList;

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private final MovieAdapterOnClickHandler mClickHandler;

    public MoviesAdapter(ArrayList<MovieModel> movieList, MovieAdapterOnClickHandler clickHandler) {
        mMovieList = movieList;
        mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieModel movie);
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Log.d(TAG, "onCreateViewHolder");
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        MovieModel movie = mMovieList.get(position);
        if (movie != null) {

            holder.movieId = movie.getId();

            String imagePath = movie.getPosterPath();
            if (imagePath != null && !imagePath.isEmpty()) {
                Picasso.with(holder.moviePoster.getContext()).load(imagePath).placeholder(R.drawable.progress_animation).into(holder.moviePoster);
                // Log.d(TAG, "Imagem: " + imagePath);
            }
        }
    }

    @Override
    public int getItemCount() {

        if (mMovieList != null) {
            return mMovieList.size();
        }
        return 0;
    }

    public void setMovieData() {
        mMovieList = null;
        notifyDataSetChanged();
    }



    /**
     * Cache of the children views for a list item.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView moviePoster;

        public int movieId;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.img_poster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieModel movieSelected = mMovieList.get(adapterPosition);
            if (movieSelected != null) {
                Log.d(TAG, "Filme selecionado : posicao[" + adapterPosition + "] title[" + movieSelected.getTitle() + "]");
                mClickHandler.onClick(movieSelected);
            }
        }
    }


}
