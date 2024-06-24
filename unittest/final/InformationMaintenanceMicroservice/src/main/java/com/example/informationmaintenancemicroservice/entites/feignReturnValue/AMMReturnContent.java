package com.example.informationmaintenancemicroservice.entites.feignReturnValue;

import com.example.informationmaintenancemicroservice.entites.Comment;

import java.util.List;

public class AMMReturnContent {
    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<Comment> getContentList() {
        return contentList;
    }

    public void setContentList(List<Comment> contentList) {
        this.contentList = contentList;
    }

    public AMMReturnContent(int resultCode, List<Comment> contentList) {
        this.resultCode = resultCode;
        this.contentList = contentList;
    }
    public AMMReturnContent() {
        this.resultCode = 1;
        this.contentList = null;
    }

    int resultCode;
    List<Comment> contentList;
}
