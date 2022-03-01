package com.exzork.simpedarku.model;

import com.google.gson.annotations.SerializedName;

public class Type {
    public static final double POLICE = 1;
    public static final double HOSPITAL = 2;
    public static final double FIREFIGHTER = 3;

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

    public void setId(Double id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
