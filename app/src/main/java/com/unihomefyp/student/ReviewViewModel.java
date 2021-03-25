package com.unihomefyp.student;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unihomefyp.models.ReviewModel;

import java.util.List;

public class ReviewViewModel  extends ViewModel {
    private MutableLiveData<List<ReviewModel>> mutableLiveDataPropertyList;

    public ReviewViewModel(){
        mutableLiveDataPropertyList = new MutableLiveData<>();
    }

    public MutableLiveData<List<ReviewModel>> getMutableLiveDataPropertyList(){
        return mutableLiveDataPropertyList;
    }

    public void setMutableLiveDataPropertyList(List<ReviewModel> reviewList)
    {
        mutableLiveDataPropertyList.setValue(reviewList);
    }

}
