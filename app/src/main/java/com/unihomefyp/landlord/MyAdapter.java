package com.unihomefyp.landlord;

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
import com.unihomefyp.R;
import com.unihomefyp.models.PropertyData;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PropertyViewHolder> {

    private Context mContext;
    private List<PropertyData> myPropertyList;



    public class PropertyViewHolder extends ViewHolder {

        ImageView imageView;
        TextView mName, mDescription, mPrice, mUser;
        CardView mCardView;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivImage);
            mName = itemView.findViewById(R.id.tvName);
            mDescription = itemView.findViewById(R.id.tvDescription);
            mPrice = itemView.findViewById(R.id.tvPrice);
            mCardView = itemView.findViewById(R.id.myCardView);
        }

    }

    public MyAdapter(Context mContext, List<PropertyData> myPropertyList) {
        this.mContext = mContext;
        this.myPropertyList = myPropertyList;

    }

    @Override
    public MyAdapter.PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.property_recycler_row_item, viewGroup, false);

        return new PropertyViewHolder(mView);



    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder propertyViewHolder, int i) {
        Glide.with(mContext)
                .load(myPropertyList.get(i).getImageUrl())
                .into(propertyViewHolder.imageView);

        //  propertyViewHolder.imageView.setImageResource(myPropertyList.get(i).getPropertyImage());
        propertyViewHolder.mName.setText(myPropertyList.get(i).getName());
        propertyViewHolder.mDescription.setText(myPropertyList.get(i).getAddress());
        propertyViewHolder.mPrice.setText("€ "+ (myPropertyList.get(i).getPrice()));

        final String property_key = myPropertyList.get(i).getKey().toString();


        propertyViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(mContext, LandlordPropertyDetail.class);
                intent2.putExtra("PropertyID",property_key);
                mContext.startActivity(intent2);
            }

        });

//        propertyViewHolder.mCardView.setOnLongClickListener(new OnLongClickListener(){
//
//            @Override
//            public boolean onLongClick(View view) {
//                showDeleteDataDialog(property_key);
//                return false;
//            }
//        }
//
//
//        );





    }

//    private void showDeleteDataDialog(String property_key) {
//        //Alert Dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//    }

    @Override
    public int getItemCount() {
        return myPropertyList.size();
    }

    public void filteredList(ArrayList<PropertyData> filterList) {
        myPropertyList = filterList;
        notifyDataSetChanged();
    }
}
