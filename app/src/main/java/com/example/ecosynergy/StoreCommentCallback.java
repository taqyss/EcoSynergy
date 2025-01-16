package com.example.ecosynergy;

public interface StoreCommentCallback {
    void onCommentStored();  // Called when the comment is successfully stored
    void onError(String errorMessage);  // Called when there is an error storing the comment
}