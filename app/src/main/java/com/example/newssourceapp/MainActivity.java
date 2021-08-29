package com.example.newssourceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements CategoriesFragment.CategoriesListener,
        NewsSourceFragment.NewsSourceListener {
    NewsData.Category mCategory;
    Source mSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rootView, new CategoriesFragment())
                .commit();
    }

    @Override
    public void gotoNewsSource(NewsData.Category category) {
        this.mCategory = category;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, NewsSourceFragment.newInstance(mCategory))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoNewsDetail(Source source) {
        this.mSource = source;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, NewsDetailFragment.newInstance(mSource))
                .addToBackStack(null)
                .commit();
    }
}