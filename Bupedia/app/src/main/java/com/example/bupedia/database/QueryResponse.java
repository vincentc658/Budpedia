package com.example.bupedia.database;

public interface QueryResponse<T> {
    void onSuccess(T data, int idData);
    void onFailure(String message);
}