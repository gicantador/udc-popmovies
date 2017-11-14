package com.pgcn.udcpopmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pgcn.udcpopmovies.utils.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Giselle on 12/11/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {


    private static int viewHolderCount;

    // a lista de movies
    private ArrayList<MovieModel> mMovieList;

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private MovieAdapterOnClickHandler mClickHandler;

    public MoviesAdapter(ArrayList<MovieModel> movieList, MovieAdapterOnClickHandler clickHandler) {
        mMovieList = movieList;
        mClickHandler = clickHandler;
        viewHolderCount = 0;
    }


    public MoviesAdapter(ArrayList<MovieModel> mMovieModelArrayList) {
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
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        MovieModel movie = mMovieList.get(position);
        if (movie != null) {

            holder.movieId = movie.getId();

            String imagePath = movie.getPosterPath();
            if (imagePath != null && !imagePath.isEmpty()) {
                //Picasso.with(context).load(movie.getPosterPath()).into(viewHolder.moviePoster);
                Picasso.with(holder.moviePoster.getContext()).load(imagePath).placeholder(R.drawable.progress_animation).into(holder.moviePoster);

                // Log.d(TAG, "Imagem: " + imagePath);
            }
        }

        this.viewHolderCount++;
        //Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "+ viewHolderCount);


    }

    @Override
    public int getItemCount() {

        if (mMovieList != null) {
            return mMovieList.size();
        }
        return 0;
    }

    /**
     * Cache of the children views for a list item.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView moviePoster;
        //private ListItemClickListener mOnClickListener;

        public int movieId;

        public MovieViewHolder(View itemView) {

            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.img_poster);
            itemView.setOnClickListener(this);


        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         *
         * @param listIndex Position of the item in the list
         */
        void bind(int listIndex) {

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


    public void setMovieData(ArrayList<MovieModel> movies) {
        mMovieList = movies;
        notifyDataSetChanged();
    }


}
