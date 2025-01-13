package com.example.ecosynergy;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private List<Comment> replies;
    private String userAvatar, commentText, timestamp, username;
    private int upvote;

    private String id;

    public Comment(String userAvatar, String username, String commentText, int upvote, String timestamp) {
        this.userAvatar = userAvatar;
        this.commentText = commentText;
        this.upvote = upvote;
        this.timestamp = timestamp;
        this.username = username;
        this.replies = new ArrayList<>();
    }

    public Comment(String userAvatar, String username, String commentText, int upvote, String timestamp, List<Comment> replies) {
        this.userAvatar = userAvatar;
        this.commentText = commentText;
        this.upvote = upvote;
        this.timestamp = timestamp;
        this.username = username;
        this.replies = replies;
    }

    public void setId(String username,String commentText) {
        this.id = username + commentText;
    }

    public String getId () {
        return id;
    }


    public String getUserAvatar() {
        return userAvatar;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }

    public int getVoteCount() {
        return upvote;
    }

    public void upvote() {
        upvote++;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void addReply(Comment reply) {
        this.replies.add(reply);
    }
}

