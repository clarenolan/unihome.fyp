package com.unihomefyp;

import com.unihomefyp.models.ReviewModel;

import java.util.List;

public interface IReviewCallbackListener {
    void onCommentLoadSuccess(List<ReviewModel> reviewModels);
    void onCommentLoadFailed(String message);
}
