package com.example.newssourceapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriesFragment extends Fragment {
    ListView listView;
    ArrayList<NewsData.Category> categories = new ArrayList<>();
    CategoriesAdapter adapter;
    CategoriesListener mListener;
    NewsData.Category mCategory;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("News Source Categories");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        this.categories = NewsData.categories;
        listView = view.findViewById(R.id.listView);
        adapter = new CategoriesAdapter(Objects.requireNonNull(getContext()), R.layout.row_item_for_categories, categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCategory = (NewsData.Category) listView.getItemAtPosition(position);
                mListener.gotoNewsSource(mCategory);
            }
        });

        return view;
    }

    class CategoriesAdapter extends ArrayAdapter<NewsData.Category> {
        public CategoriesAdapter(@NonNull Context context, int resource, @NonNull List<NewsData.Category> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_for_categories, parent, false);
            }

            NewsData.Category category = getItem(position);
            TextView textViewCity = convertView.findViewById(R.id.textViewCategories);
            //String name = category.getCategory();
            textViewCity.setText(category.getName());

            return convertView;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mListener = (CategoriesListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement CategoriesListener");
        }
    }

    interface CategoriesListener{
        void gotoNewsSource(NewsData.Category category);
    }
}