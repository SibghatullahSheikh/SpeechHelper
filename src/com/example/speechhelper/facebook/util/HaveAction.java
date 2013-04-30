package com.example.speechhelper.facebook.util;

import com.facebook.model.OpenGraphAction;

public interface HaveAction extends OpenGraphAction {
    // The talk object
    public TalkGraphObject getTalk();
    public void setTalk(TalkGraphObject talk);
}	
