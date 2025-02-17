package com.esprit.services;

import java.util.ArrayList;

public interface iService <T>{
    void ajouter(T t);
    void modifier(T t);
    void supprimer(T t);
    ArrayList<T> rechercher();
}
