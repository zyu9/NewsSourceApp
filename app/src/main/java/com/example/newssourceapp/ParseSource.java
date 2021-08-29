package com.example.newssourceapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseSource {
    ArrayList<Source> parse(String string){
        ArrayList<Source> sources = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(string);
            JSONArray sourcesJson = json.getJSONArray("sources");

            for(int i = 0; i < sourcesJson.length(); i++){
                JSONObject newsObject = sourcesJson.getJSONObject(i);
                News news = new News();
                news.setId(newsObject.getString("id"));
                news.setName(newsObject.getString("name"));
                news.setDescription(newsObject.getString("description"));
                news.setUrl(newsObject.getString("url"));
                news.setCategory(newsObject.getString("category"));
                news.setLanguage(newsObject.getString("language"));
                news.setCountry(newsObject.getString("country"));

                Source source = new Source();
                source.setNews(news);

                sources.add(source);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return sources;
    }

    ArrayList<Source.Articles> parseArticles(String string){
        ArrayList<Source.Articles> articles = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(string);
            JSONArray articlesJson = json.getJSONArray("articles");

            for(int i = 0; i < articlesJson.length(); i++){
                JSONObject articleObject = articlesJson.getJSONObject(i);

                JSONObject sourceObject = articleObject.getJSONObject("source");
                Source source = new Source();
                source.setId(sourceObject.getString("id"));
                source.setName(sourceObject.getString("name"));

                Source.Articles article = new Source.Articles();
                article.setAuthor(articleObject.getString("author"));
                article.setTitle(articleObject.getString("title"));
                article.setDescription(articleObject.getString("description"));
                article.setUrl(articleObject.getString("url"));
                article.setUrlToImage(articleObject.getString("urlToImage"));
                article.setPublishedAt(articleObject.getString("publishedAt"));
                article.setContent(articleObject.getString("content"));

                article.setSource(source);
                articles.add(article);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return articles;
    }
}
