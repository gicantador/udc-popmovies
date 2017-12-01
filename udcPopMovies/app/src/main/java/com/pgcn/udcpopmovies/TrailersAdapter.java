package com.pgcn.udcpopmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgcn.udcpopmovies.model.TrailerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Giselle on 28/11/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {
    private static final String TAG = TrailersAdapter.class.getSimpleName();
    // a lista de trailers
    private final ArrayList<TrailerModel> mTrailerList;
    private final TrailersAdapter.TrailersAdapterOnClickHandler mClickHandler;

    public TrailersAdapter(ArrayList<TrailerModel> trailerList, TrailersAdapterOnClickHandler clickHandler) {
        mTrailerList = trailerList;
        mClickHandler = clickHandler;
        Log.d(TAG, "TrailersAdapter creator");
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new TrailersAdapter.TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        TrailerModel trailer = mTrailerList.get(position);
        if (trailer != null) {

            holder.trailer.setText(trailer.getName());

            Picasso.with(holder.setaPlay.getContext()).load(R.drawable.play1).placeholder(
                    R.drawable.progress_animation).into(holder.setaPlay);

        }
    }

    @Override
    public int getItemCount() {
        if (mTrailerList != null) {
            return mTrailerList.size();
        }
        return 0;
    }

    public interface TrailersAdapterOnClickHandler {
        void onClick(TrailerModel trailer);
    }

    /**
     * Cache of the children views for a list item.
     */
    class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView trailer;
        final ImageView setaPlay;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            setaPlay = itemView.findViewById(R.id.img_play_trailer);
            trailer = itemView.findViewById(R.id.tv_trailers_item_title);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TrailerModel trailerSelected = mTrailerList.get(adapterPosition);
            if (trailerSelected != null) {
                Log.d(TAG, "Traielr selecionado : posicao[" + adapterPosition + "] title[" + trailerSelected + "]");
                mClickHandler.onClick(trailerSelected);
            }
        }
    }


}
