package com.example.flickrexample.ui;

import android.view.View;

import androidx.databinding.BindingAdapter;

/**
 * Example of a custom BindingAdapter that makes the binding code a little easier to manage.
 */
public class BindingAdapters {

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
