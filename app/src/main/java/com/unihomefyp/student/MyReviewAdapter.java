package com.unihomefyp.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unihomefyp.R;
import com.unihomefyp.models.ReviewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.MyViewHolder> {

    Context context;
    List<ReviewModel> reviewModelList;

    public MyReviewAdapter(Context context, List<ReviewModel> reviewModelList) {
        this.context = context;
        this.reviewModelList = reviewModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_review_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Long timeStamp = Long.valueOf(reviewModelList.get(position).getReviewTimestamp().get("reviewTimestamp/timeStamp").toString());
        //holder.txt_comment_date.setText(DateUtils.getRelativeTimeSpanString(timeStamp));
        holder.txt_review.setText(reviewModelList.get(position).getReview());
        holder.txt_review_name.setText(reviewModelList.get(position).getName());
        holder.ratingBar.setRating(reviewModelList.get(position).getRatingValue2());
    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private Unbinder unbinder;

        //        @BindView(R.id.txt_comment_date)
//        TextView txt_comment_date;
        @BindView(R.id.txtReviewName)
        TextView txt_review_name;
        @BindView(R.id.txt_review)
        TextView txt_review;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);

        }
    }
}
