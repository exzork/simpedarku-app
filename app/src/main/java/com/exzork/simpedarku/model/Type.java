package com.exzork.simpedarku.model;

import com.google.gson.annotations.SerializedName;

public class Type {
    @SerializedName("id")
    private Double id;
    @SerializedName("name")
    private String name;

    public Double getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
