package com.pgcn.udcpopmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pgcn.udcpopmovies.model.ReviewModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by Giselle on 30/11/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();
    // a lista de trailers
    private final ArrayList<ReviewModel> mReviewList;
    private final ReviewAdapter.ReviewAdapterOnClickHandler mClickHandler;
    private Context context;

    public ReviewAdapter(ArrayList<ReviewModel> reviewList, ReviewAdapterOnClickHandler clickHandler) {
        mReviewList = reviewList;
        mClickHandler = clickHandler;
        Log.d(TAG, "ReviewAdapter creator");
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewModel review = mReviewList.get(position);
        if (review != null) {
            String aux = context.getString(R.string.text_aux_author) + StringUtils.SPACE + review.getAuthor();
            holder.author.setText(aux);
            holder.content.setText(getShortReview(review.getContent()));
        }
    }

    private String getShortReview(String content) {
        int maxContentSize = 400;
        if (null != content && content.length() > maxContentSize) {
            return content.substring(0, maxContentSize).concat(StringUtils.SPACE + context.getString(R.string.text_aux_see_more));
        }
        return content;
    }

    @Override
    public int getItemCount() {
        if (mReviewList != null) {
            return mReviewList.size();
        }
        return 0;
    }


    public interface ReviewAdapterOnClickHandler {
        void onClick(ReviewModel trailer);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView content;
        public final TextView author;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_review_content);
            author = itemView.findViewById(R.id.tv_review_author);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ReviewModel reviewSelected = mReviewList.get(adapterPosition);
            if (reviewSelected != null) {
                Log.d(TAG, "Review selecionado : posicao[" + adapterPosition + "] autor[" + reviewSelected.getAuthor() + "]");
                mClickHandler.onClick(reviewSelected);
            }
        }
    }


}
