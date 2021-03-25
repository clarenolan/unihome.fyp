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
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.PropertyDetail;
import com.unihomefyp.R;
import com.unihomefyp.models.PropertyData;
import com.unihomefyp.models.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder>{

    private Context mContext;
    private List<PropertyData> myPropertyList;
    private FirebaseUser firebaseUser;


    private MutableLiveData<ReviewModel> myReviewList;

    public  void setReviewModel(ReviewModel reviewModel)
    {
        if(myReviewList != null)
            myReviewList.setValue(reviewModel);
    }

    public MutableLiveData<ReviewModel> getMyReviewList() {
        return myReviewList;
    }

    public void setReviewModel(MutableLiveData<ReviewModel> myReviewList) {
        this.myReviewList = myReviewList;
    }

    public PropertyAdapter(){
        myReviewList = new MutableLiveData<>();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView mName, mAddress, mPrice;
        ImageView imageView;
        ImageView btnSave;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.myCardView);
            imageView = itemView.findViewById(R.id.ivImage);
            mName = itemView.findViewById(R.id.tvName);
            mAddress = itemView.findViewById(R.id.tvDescription);
            mPrice = itemView.findViewById(R.id.tvPrice);
            btnSave = itemView.findViewById(R.id.btnSave);

        }

    }


    public PropertyAdapter(Context mContext, List<PropertyData> myPropertyList) {
        this.mContext = mContext;
        this.myPropertyList = myPropertyList;

    }


    @Override
    public PropertyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.property_recycler_row_item, viewGroup, false);
        return new PropertyAdapter.ViewHolder(mView);

    }



    @Override
    public void onBindViewHolder(@NonNull PropertyAdapter.ViewHolder propertyViewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String property_key = myPropertyList.get(i).getKey().toString();

        Glide.with(mContext)
                .load(myPropertyList.get(i).getImageUrl())
                .into(propertyViewHolder.imageView);

        //  propertyViewHolder.imageView.setImageResource(myPropertyList.get(i).getPropertyImage());
        propertyViewHolder.mName.setText(myPropertyList.get(i).getName());
        propertyViewHolder.mAddress.setText(myPropertyList.get(i).getAddress());
        propertyViewHolder.mPrice.setText("€ "+ (myPropertyList.get(i).getPrice()));
        isSaved(property_key, propertyViewHolder.btnSave);


        //Save/unsave property
        propertyViewHolder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set value = true for saved property
                if (propertyViewHolder.btnSave.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Saved").child(firebaseUser.getUid())
                            .child(property_key).setValue(true);
                    //remove property key from list of saved
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Saved").child(firebaseUser.getUid())
                            .child(property_key).removeValue();
                }
            }
        });


        propertyViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(mContext, PropertyDetail.class);
//                intent2.putExtra("PropertyName",myPropertyList.get(propertyViewHolder.getAdapterPosition()).getPropertyName());
                //              intent2.putExtra("Image",myPropertyList.get(propertyViewHolder.getAdapterPosition()).getPropertyImage());
//                intent2.putExtra("Description",myPropertyList.get(propertyViewHolder.getAdapterPosition()).getCounty());
//                intent2.putExtra("PropertyAddress",myPropertyList.get(propertyViewHolder.getAdapterPosition()).getPropertyAddress());
//                intent2.putExtra("Price",myPropertyList.get(propertyViewHolder.getAdapterPosition()).getRentPrice());
//                intent2.putExtra("keyValue",myPropertyList.get(propertyViewHolder.getAdapterPosition()).getKey());
                intent2.putExtra("PropertyID",property_key);
                mContext.startActivity(intent2);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myPropertyList.size();
    }

//    public void filteredList(ArrayList<PropertyData> filterList) {
//        myPropertyList = filterList;
//        notifyDataSetChanged();
//    }

    //Check if property is saved by student
    private void isSaved(String property_key, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saved")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //If property is saved, bookmark icon is filled
                if (snapshot.child(property_key).exists()) {
                    imageView.setImageResource(R.drawable.ic_action_name);
                    imageView.setTag("saved");
                    //If property is not saved, bookmark icon is empty
                }else{
                    imageView.setImageResource(R.drawable.ic_save);
                    imageView.setTag("save");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }





//    @Override
//protected void onBindViewHolder(@NonNull ViewHolder holder,int position,@NonNull PropertyData model){
//        holder.mName.setText(model.getPropertyName());
//        holder.mAddress.setText(model.getPropertyAddress());
//        holder.mPrice.setText(model.getRentPrice());
//        //holder.imageView.setImageURI(model.getPropertyImage());
//        Glide.with(mContext)
//        .load(model.getPropertyImage())
//        .into(holder.imageView);
//
//final String property_key=getRef(position).getKey().toString();
//
//
//        holder.cardView.setOnClickListener(new View.OnClickListener(){
//@Override
//public void onClick(View view){
//        Intent detailActivity=new Intent(mContext,PropertyDetail.class);
//        detailActivity.putExtra("PropertyID",property_key);
//        mContext.startActivity(detailActivity);
//        }
//        });
//        }
//

//    public void filteredList(ArrayList<PropertyData> filterList) {
//        myPropertyList = filterList;
//        notifyDataSetChanged();
//    }


}
