package com.example.gatemtquiz3;

import androidx.annotation.NonNull;

public class Categories {
    public static final int PHYSICAL_METALLURGY = 1;
    public static final int MECHANICAL_METALLURGY = 2;
    public static final int THERMODYNAMICS = 3;

    private int id;
    private String name;

    public Categories() {}

    public Categories(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
