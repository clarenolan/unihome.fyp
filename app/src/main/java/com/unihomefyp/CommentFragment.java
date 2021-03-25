package com.unihomefyp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.models.ReviewModel;
import com.unihomefyp.student.MyReviewAdapter;
import com.unihomefyp.student.ReviewViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class CommentFragment extends BottomSheetDialogFragment implements IReviewCallbackListener {

    private ReviewViewModel reviewViewModel;
    private Unbinder unbinder;

    @BindView(R.id.recycler_review)
    RecyclerView recycler_review;

    AlertDialog dialog;
    IReviewCallbackListener listener;

    public CommentFragment() { listener = this; }
    private static CommentFragment instance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_review_sheet, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        String property_key = getArguments().getString("propId");
        initViews();
        loadReviews();
        reviewViewModel.getMutableLiveDataPropertyList().observe(this, reviewModels -> {
            MyReviewAdapter adapter = new MyReviewAdapter(getContext(),reviewModels);
            recycler_review.setAdapter(adapter);

        });
        return itemView;
    }

    private void loadReviews() {
        String property_key = getArguments().getString("propId");
        //dialog.show();
        List<ReviewModel> reviewModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Reviews").child(property_key)
                .orderByChild("timeStamp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot reviewSnapshot:snapshot.getChildren())
                        {
                            ReviewModel reviewModel = reviewSnapshot.getValue(ReviewModel.class);
                            reviewModels.add(reviewModel);
                        }
                        listener.onCommentLoadSuccess(reviewModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onCommentLoadFailed(error.getMessage());
                    }
                });
    }


    private void initViews() {
        reviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        recycler_review.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);

        recycler_review.setLayoutManager(layoutManager);
        recycler_review.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));
    }


    public static CommentFragment getInstance(){
        if(instance == null)
            instance = new CommentFragment();
        return instance;
    }

    @Override
    public void onCommentLoadSuccess(List<ReviewModel> reviewModels) {
        reviewViewModel.setMutableLiveDataPropertyList(reviewModels);
    }

    @Override
    public void onCommentLoadFailed(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

    }
}
