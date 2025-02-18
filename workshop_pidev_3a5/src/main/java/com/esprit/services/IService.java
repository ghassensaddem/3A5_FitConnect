package com.esprit.services;

import java.util.ArrayList;

public interface iService <T>{
    void ajouter(T t);
    boolean modifier(T t);
    boolean supprimer(int id);
    ArrayList<T> rechercher();
}
