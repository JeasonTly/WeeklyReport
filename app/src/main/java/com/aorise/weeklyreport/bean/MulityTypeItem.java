package com.aorise.weeklyreport.bean;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/27.
 */
public class MulityTypeItem<T> {
    @Override
    public String toString() {
        return "MulityTypeItem{" +
                "data=" + data +
                ", data_type=" + data_type +
                ", item_name='" + item_name + '\'' +
                '}';
    }

    private T data;
    private int data_type;
    private String item_name;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
