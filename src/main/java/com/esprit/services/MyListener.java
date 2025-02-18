package com.esprit.services;

// Interface générique avec un type T
public interface MyListener<T> {
    void onClickListener(T item);
}

