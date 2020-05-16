package com.cpulse.tagfinder.newsapi;

public class ArticleObject {

    private String mTitle;
    private String mDescription;
    private String mURL;
    private String mURLToImage;

    public ArticleObject(String iTitle, String iDescription, String iURL, String iURLToImage) {
        mTitle = iTitle;
        mDescription = iDescription;
        mURL = iURL;
        mURLToImage = iURLToImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getURL() {
        return mURL;
    }

    public String getURLToImage() {
        return mURLToImage;
    }

}
