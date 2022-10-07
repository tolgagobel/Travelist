package com.example.travelist.Model;

import com.google.firebase.database.ServerValue;

public class PostModel {

   /* String postkey;
     String title;
     String description;
     String picture;
     String userId;
     String userPhoto;

    public PostModel() {
    }

    public PostModel(String title, String description, String picture, String userId, String userPhoto) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }*/


 String pImage,pTitle,pDescription,uEmail;
  public PostModel() {

    }

    public PostModel(String pImage, String pTitle, String pDescription, String uEmail, String postkey) {
        this.pImage = pImage;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
        this.uEmail = uEmail;
    }

   /* public PostModel(String pImage, String pTitle, String pDescription) {
        this.pImage = pImage;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
    }*/

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }


    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }
}
