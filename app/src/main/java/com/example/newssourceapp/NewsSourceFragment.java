package com.example.newssourceapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsSourceFragment extends Fragment {
    private static final String ARG_PARAM = "mCategory";
    private NewsData.Category mCategory;
    final OkHttpClient client = new OkHttpClient();
    NewsSourceListener mListener;

    public NewsSourceFragment() {
        // Required empty public constructor
    }

    public static NewsSourceFragment newInstance(NewsData.Category category) {
        NewsSourceFragment fragment = new NewsSourceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory =  (NewsData.Category) getArguments().getSerializable(ARG_PARAM);
        }
    }

    RecyclerView recyclerView;
    NewsSourceAdapter adapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<Source> sources = new ArrayList<>();
    News news = new News();
    ParseSource parseSource = new ParseSource();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String title = String.format("%s News Sources", mCategory.getName());
        getActivity().setTitle(title);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_source, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        String apiKey = "98d8ff9c192e411db09b752b171222a9";

        HttpUrl url = HttpUrl.parse("https://newsapi.org/v2/sources").newBuilder()
                .addQueryParameter("country", "us")
                .addQueryParameter("category", mCategory.getCategory())
                .addQueryParameter("apiKey", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    sources = parseSource.parse(response.body().string());

                    adapter = new NewsSourceAdapter(sources);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });

        return view;
    }

    class NewsSourceAdapter extends RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder>{
        ArrayList<Source> sources;

        public NewsSourceAdapter(ArrayList<Source> sources) {
            this.sources = sources;
        }

        @NonNull
        @Override
        public NewsSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item_for_news_source_recycler, parent, false);
            return new NewsSourceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsSourceViewHolder holder, int position) {
            Source source = sources.get(position);
            holder.getViews(source);
        }

        @Override
        public int getItemCount() {
            return this.sources.size();
        }

        class NewsSourceViewHolder extends RecyclerView.ViewHolder{
            Source source;
            TextView textViewName, textViewDescription;
            View contextView;

            public NewsSourceViewHolder(@NonNull View itemView) {
                super(itemView);
                contextView = itemView;
                textViewName = itemView.findViewById(R.id.textViewNewsName);
                textViewDescription = itemView.findViewById(R.id.textViewNewsDescription);
            }

            public void getViews(Source data){
                this.source = data;

                news = source.getNews();

                textViewName.setText(news.getName());
                textViewDescription.setText(news.getDescription());

                contextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.gotoNewsDetail(source);
                    }
                });
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mListener = (NewsSourceListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement NewsSourceListener");
        }
    }

    interface NewsSourceListener{
        void gotoNewsDetail(Source source);
    }
}