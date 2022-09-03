package com.example.rranz.ricettario;


public class Card {
        private String imgURL;
        private String title;
        private int index;

        public Card(String imgURL, String title,int index) {
            this.imgURL = imgURL;
            this.title = title;
            this.index = index;
        }

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {

            this.title = title;
        }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    }

