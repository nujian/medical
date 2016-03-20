package com.care.controller.uiModels;

import com.care.controller.uiModels.base.BaseVoModel;
import com.care.domain.Comment;
import com.care.domain.User;

import java.util.List;

/**
 * Created by nujian on 16/3/13.
 */
public class UserProfileVo extends BaseVoModel {

    private User user;

    private List<Comment> comments;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
