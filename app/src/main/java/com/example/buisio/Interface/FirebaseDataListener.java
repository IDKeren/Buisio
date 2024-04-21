package com.example.buisio.Interface;

public interface FirebaseDataListener<T> {
    void onDataReceived(T data);
    void onError(Exception e);
}
