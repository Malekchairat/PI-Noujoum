package controller;

import java.util.List;

public interface InterfaceProduit<T>{
    public void add(T t);
    public void update(T t);
    public void delete(T t);
    public T find(T t);
    public List<T> findAll();

}
