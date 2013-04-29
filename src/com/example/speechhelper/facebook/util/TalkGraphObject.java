package com.example.speechhelper.facebook.util;

import com.facebook.model.GraphObject;

public interface TalkGraphObject extends GraphObject {
    // A URL
    public String getUrl();
    public void setUrl(String url);

    // An ID
    public String getId();
    public void setId(String id);
}