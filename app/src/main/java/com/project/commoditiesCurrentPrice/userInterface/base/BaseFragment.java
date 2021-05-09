package com.project.commoditiesCurrentPrice.userInterface.base;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.project.commoditiesCurrentPrice.Application;
import com.project.commoditiesCurrentPrice.repository.Repository;
import com.project.commoditiesCurrentPrice.viewModel.MainViewModel;

public abstract class BaseFragment<T extends ViewModel> extends Fragment {
    protected T viewModel;
    public abstract T getViewModel();

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        viewModel = getViewModel();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }
}
