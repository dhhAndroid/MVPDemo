package com.dhh.mvp.main.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhh on 2017/6/22.
 */

public class CommBean<T> {
    public String name;
    @SerializedName(value = "age",alternate = "data")
    public T data;

    public CommBean() {
    }

    public CommBean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CommBean{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}
