package models;

public interface BuildMethods {

    void build(CustomBuffer buffer);
    CustomBuffer scale(CustomBuffer buffer, double factor, boolean rebuild);
    CustomBuffer rotate(CustomBuffer buffer, double angle, boolean rebuild);
}