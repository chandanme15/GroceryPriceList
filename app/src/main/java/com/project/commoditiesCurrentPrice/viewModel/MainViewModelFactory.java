package com.project.commoditiesCurrentPrice.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.commoditiesCurrentPrice.repository.Repository;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;
    private MainViewModel.DatabaseProcessInterface listener;

    public MainViewModelFactory(Repository repository, MainViewModel.DatabaseProcessInterface listener){
        this.repository = repository;
        this.listener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if ( modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(repository, listener);
        }
        throw new IllegalArgumentException("Unknown view model class");
    }
}
