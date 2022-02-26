package com.exzork.simpedarku.model;

import com.google.gson.annotations.SerializedName;

public class Report {
    @SerializedName("id")
    private Double id;
    @SerializedName("type")
    private Type type;
    @SerializedName("user")
    private User user;
    @SerializedName("title")
    private String title;
    @SerializedName("location")
    private String location;
    @SerializedName("description")
    private String description;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("status")
    private String status;

    public Double getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", type=" + type +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", created_at='" + created_at + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}