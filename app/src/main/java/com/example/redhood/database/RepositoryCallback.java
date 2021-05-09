package com.example.redhood.database;

public interface RepositoryCallback<T> {
    void onComplete(T result);
}
