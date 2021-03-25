package com.unihomefyp.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.unihomefyp.R;
import com.unihomefyp.models.EnquiryModel;

import java.util.List;

public class PastEnquiriesAdapter extends RecyclerView.Adapter<PastEnquiriesAdapter.EnquiryViewHolder> {

    private Context mContext2;
    private List<EnquiryModel> myEnquiryList2;


    public class EnquiryViewHolder extends RecyclerView.ViewHolder {

        TextView mName2, mProperty2;
        CardView mCardView2;

        public EnquiryViewHolder(@NonNull View itemView) {
            super(itemView);
            mName2 = itemView.findViewById(R.id.tvEnqAddress);
            mProperty2 = itemView.findViewById(R.id.tvTimestamp);
            mCardView2 = itemView.findViewById(R.id.myCardView3);
        }

    }

    public PastEnquiriesAdapter(Context mContext2, List<EnquiryModel> myEnquiryList2) {
        this.mContext2 = mContext2;
        this.myEnquiryList2 = myEnquiryList2;

    }

    @Override
    public PastEnquiriesAdapter.EnquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_enquiry_row, parent, false);
        return new PastEnquiriesAdapter.EnquiryViewHolder(mView);


    }

    @Override
    public void onBindViewHolder(@NonNull PastEnquiriesAdapter.EnquiryViewHolder holder, int i) {

        holder.mName2.setText(myEnquiryList2.get(i).getSubject());
        holder.mProperty2.setText(myEnquiryList2.get(i).getTiimestamp());

//        final String property_key = myEnquiryList2.get(i).getPropertyKey().toString();
//
//
//        holder.mCardView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String property_key = myEnquiryList2.get(i).getPropertyKey().toString();
//                Intent intent2 = new Intent(mContext2, EnquiryDetail.class);
//                intent2.putExtra("PropertyID",property_key);
//                mContext2.startActivity(intent2);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return myEnquiryList2.size();
    }
}
