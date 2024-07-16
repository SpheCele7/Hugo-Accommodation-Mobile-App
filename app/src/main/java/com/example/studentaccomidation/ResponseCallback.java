package com.example.studentaccomidation;

public interface ResponseCallback {

    void onResponse(String response);

    void onError(Throwable throwable);
}
