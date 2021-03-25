package com.unihomefyp;

import androidx.lifecycle.MutableLiveData;

import com.unihomefyp.models.PropertyData;
import com.unihomefyp.models.ReviewModel;

public class PropertyDetailViewModel {

    private MutableLiveData<ReviewModel> mutableLiveDataReview;
    private MutableLiveData<PropertyData>mutableLiveDataProperty;

    public void setReviewModel(ReviewModel reviewModel){
        if (mutableLiveDataReview != null)
            mutableLiveDataReview.setValue(reviewModel);
    }

    public MutableLiveData<ReviewModel> getMutableLiveData() {
        return mutableLiveDataReview;
    }

//    public void setMutableLiveData(MutableLiveData<PropertyData> mutableLiveDataReview) {
//        this.mutableLiveDataReview = mutableLiveDataReview;
//    }

    PropertyDetailViewModel(){ mutableLiveDataReview = new MutableLiveData<>(); }


    public void setPropertyData(PropertyData propertyData) {
        if (mutableLiveDataProperty != null)
            mutableLiveDataProperty.setValue(propertyData);
    }
}