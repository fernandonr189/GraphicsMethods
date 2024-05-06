package models;

public interface BuildMethods {

    void build(CustomBuffer buffer);
    CustomBuffer scale(CustomBuffer buffer, double factor);
}