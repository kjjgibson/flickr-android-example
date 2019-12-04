package com.example.flickrexample.ui;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.flickrexample.R;
import com.example.flickrexample.databinding.MainFragmentBinding;
import com.example.flickrexample.viewmodel.MainViewModel;

public class MainFragment extends Fragment {

    private FlickrImageAdapter mFlickrImageAdapter;
    private MainFragmentBinding mBinding;

    static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);

        mFlickrImageAdapter = new FlickrImageAdapter();
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.recyclerViewImageList.setAdapter(mFlickrImageAdapter);
        mBinding.recyclerViewImageList.setHasFixedSize(true);
        mBinding.recyclerViewImageList.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MainViewModel mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mBinding.setMainViewModel(mViewModel);

        mBinding.buttonSearchImages.setOnClickListener(v -> {
            Editable editable = mBinding.editTextSearchImages.getText();
            if (editable == null || editable.toString().length() == 0) {
                Toast.makeText(getActivity(), R.string.enter_search_string, Toast.LENGTH_SHORT).show();
            } else {
                mViewModel.setSearchString(editable.toString().toLowerCase());
                mViewModel.searchImages();
            }
        });

        mViewModel.getFlickrImages().observe(getViewLifecycleOwner(), flickrImages -> {
            if (flickrImages != null) {
                mFlickrImageAdapter.submitList(flickrImages);
            }
        });
    }

}