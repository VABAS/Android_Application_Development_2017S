package net.net.siekkiset.jesse.androidcourse2017s.exercise11;

public class Product {
    public String name;
    public int dbId, count;
    public double price;

    public Product(int dbId, String name, int count, double price) {
        this.dbId = dbId;
        this.name = name;
        this.count = count;
        this.price = price;
    }
}
