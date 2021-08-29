package com.example.newssourceapp;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsData {
    public static final ArrayList<Category>  categories = new ArrayList<Category>(){{
       add(new Category("Business","business"));
        add(new Category("Entertainment","entertainment"));
        add(new Category("General","general"));
        add(new Category("Health","health"));
        add(new Category("Science","science"));
        add(new Category("Sports","sports"));
        add(new Category("Technology","technology"));
    }};

    static class Category implements Serializable {
        String name, category;

        public Category(String name, String category) {
            this.name = name;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        @Override
        public String toString() {
            return "Category{" +
                    "name='" + name + '\'' +
                    ", category='" + category + '\'' +
                    '}';
        }
    }
}
