package com.project.commoditiesCurrentPrice.userInterface.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.project.commoditiesCurrentPrice.Application;
import com.project.commoditiesCurrentPrice.R;
import com.project.commoditiesCurrentPrice.adapter.MainAdapter;
import com.project.commoditiesCurrentPrice.databinding.ActivityMainBinding;
import com.project.commoditiesCurrentPrice.databinding.MainFragmentBinding;
import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.repository.Repository;
import com.project.commoditiesCurrentPrice.userInterface.base.BaseFragment;
import com.project.commoditiesCurrentPrice.utils.Constants;
import com.project.commoditiesCurrentPrice.utils.Util;
import com.project.commoditiesCurrentPrice.viewModel.MainViewModel;
import com.project.commoditiesCurrentPrice.viewModel.MainViewModelFactory;

import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainFragment extends BaseFragment<MainViewModel> implements SwipeRefreshLayout.OnRefreshListener, MainViewModel.DatabaseProcessInterface {

    ActivityMainBinding activityMainBinding;
    MainFragmentBinding mainFragmentBinding;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean m_bIsAPIDataLoading = false, m_bIsDataLoadedFromDB = false;
    private RecyclerView.OnScrollListener mOnScrollListener;

    private MainFragment(ActivityMainBinding binding) {
        activityMainBinding = binding;
    }

    public static MainFragment getInstance(ActivityMainBinding binding) {
        return new MainFragment(binding);
    }

    @Override
    public MainViewModel getViewModel() {
        MainViewModelFactory factory = new MainViewModelFactory(Repository.getInstance(Application.getInstance()), this);
        return new ViewModelProvider(this, factory).get(MainViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainFragmentBinding = MainFragmentBinding.inflate(inflater, container, false);
        return mainFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        intVariables();
        setupListeners();
        setupRecycler();
        setLiveDataObserver();
        LoadData();
    }

    private void initViews() {
        mainFragmentBinding.swipeRefreshLayout.setColorSchemeResources(android.R.color.black);
        showLoading(true);
        showError(false);
    }

    private void intVariables() {
        m_bIsAPIDataLoading = false;
        m_bIsDataLoadedFromDB = false;
        Constants.PAGE_COUNT = 1;
    }

    private void setupListeners() {
        mainFragmentBinding.swipeRefreshLayout.setOnRefreshListener(this);
        activityMainBinding.retryButton.setOnClickListener(this::retry);
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (m_bIsAPIDataLoading) {
                    return;
                }
                int totalItemCount = mLayoutManager.getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

                if (totalItemCount > 1 && lastVisibleItem >= totalItemCount - 1) {
                    if (Util.isNetworkAvailable(Application.getInstance())) {
                        if (!m_bIsDataLoadedFromDB) {
                            Constants.PAGE_COUNT++;
                        }
                        displaySnackbar(false, getString(R.string.loading));
                        LoadRecordsFromAPI();
                    }
                    //else displaySnackbar(true,"No internet Connection ! ");
                }
            }
        };
    }

    private void setLiveDataObserver() {
        viewModel.getApiData().observe(getViewLifecycleOwner(), recordList -> {
            m_bIsAPIDataLoading = false;
            if (Constants.PAGE_COUNT == 1) {
                mAdapter.clearData();
                displaySnackbar(false, getString(R.string.data_updated));
                m_bIsDataLoadedFromDB = false;
            }
            mAdapter.addData(recordList);
            updateRefreshLayout(false);
            viewModel.insertRecordsToDatabase(recordList, Constants.PAGE_COUNT);
        });
        viewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            m_bIsAPIDataLoading = false;
            if (isError) {
                displaySnackbar(true, getString(R.string.cant_load_records));
                updateRefreshLayout(false);
                if (mAdapter.getData().isEmpty()) {
                    showError(true);
                }
            }
        });
    }

    private void LoadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    if (!viewModel.bIsDatabaseExpired()) {
                        m_bIsAPIDataLoading = false;
                        viewModel.bIsLoadDataFromDatabase();
                    } else if (Util.isNetworkAvailable(Application.getInstance())) {
                        LoadRecordsFromAPI();
                    } else {
                        m_bIsAPIDataLoading = false;
                        displaySnackbar(true, getString(R.string.network_not_available_error));
                        viewModel.bIsLoadDataFromDatabase();
                    }
                    Objects.requireNonNull(Looper.myLooper()).quit();
                } catch (Exception e) {
                    e.printStackTrace();
                    displaySnackbar(true, getString(R.string.some_error_occurred));
                }
            }
        }).start();
    }

    private void LoadRecordsFromAPI() {
        m_bIsAPIDataLoading = true;
        viewModel.loadRecords();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_price:
                mAdapter.sortDataByModalPrice();
                break;
            case R.id.menu_sort_by_states:
                mAdapter.sortDataByState();
                break;
            case R.id.menu_refresh:
                mainFragmentBinding.recyclerView.scrollToPosition(0);
                onRefresh();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setupRecycler() {
        mLayoutManager = new LinearLayoutManager(Application.getInstance());
        mAdapter = new MainAdapter();
        RecyclerView recyclerView = mainFragmentBinding.recyclerView;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(Application.getInstance(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void retry(View view) {
        Constants.PAGE_COUNT = 1;
        m_bIsDataLoadedFromDB = false;
        showLoading(true);
        showError(false);
        LoadData();
    }

    @Override
    public void onRefresh() {
        Constants.PAGE_COUNT = 1;
        updateRefreshLayout(true);
        m_bIsDataLoadedFromDB = false;

        //During Refresh we try to fetch data from Web first
        if (Util.isNetworkAvailable(Application.getInstance())) {
            LoadRecordsFromAPI();
        } else {
            LoadData();
        }
    }

    @Override
    public void onReadRecordFromDBCompleted(List<Record> recordList) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (recordList != null && !recordList.isEmpty()) {
                    m_bIsDataLoadedFromDB = true;
                    if (Constants.PAGE_COUNT == 1) {
                        mAdapter.clearData();
                    }
                    mAdapter.addData(recordList);
                    showError(false);
                    updateRefreshLayout(false);
                } else if (Util.isNetworkAvailable(Application.getInstance())) {
                    LoadRecordsFromAPI();
                } else {
                    showError(true);
                    updateRefreshLayout(false);
                }
            }
        });
    }

    private void updateRefreshLayout(boolean refresh) {
        if (!refresh) {
            showLoading(refresh);
        }
        mainFragmentBinding.swipeRefreshLayout.setRefreshing(refresh);
    }

    private void showError(boolean visibility) {
        activityMainBinding.error.setVisibility(visibility ? View.VISIBLE : View.GONE);
        activityMainBinding.retryButton.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void showLoading(boolean visibility) {
        activityMainBinding.loading.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void displaySnackbar(boolean isError, String message) {
        Util.showSnack(mainFragmentBinding.getRoot(), isError, message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainFragmentBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onClear();
        viewModel.closeDatabase();
    }

    public void printTestLog() {
        Log.d(TAG, "TestLog");
    }
}



