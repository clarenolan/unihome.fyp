package com.unihomefyp.student;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unihomefyp.PropertyDetail;
import com.unihomefyp.R;
import com.unihomefyp.models.PropertyData;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ImageViewHolder> {

    private Context mContext;
    private List<PropertyData> savedPropertyList;

    public SavedAdapter(Context context, List<PropertyData> savedPropertyList){
        this.mContext = context;
        this.savedPropertyList = savedPropertyList;
    }

    @NonNull
    @Override
    public SavedAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.saved_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        //final PropertyData post = savedPropertyList.get(position);



        Glide.with(mContext)
                .load(savedPropertyList.get(position).getImageUrl())
                .into(holder.post_image);

        final String property_key = savedPropertyList.get(position).getKey().toString();

        holder.txtSavedPropName.setText(savedPropertyList.get(position).getName());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passing propertyID to open saved property details
                Intent intent2 = new Intent(mContext, PropertyDetail.class);
                intent2.putExtra("PropertyID",property_key);
                mContext.startActivity(intent2);

            }
        });

    }

    @Override
    public int getItemCount() {
        return savedPropertyList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ImageView post_image;
        public TextView txtSavedPropName;


        public ImageViewHolder(View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.imgSavedProp);
            txtSavedPropName = itemView.findViewById(R.id.txtSavedPropName);
            cardView = itemView.findViewById(R.id.savedCardView);

        }
    }
}