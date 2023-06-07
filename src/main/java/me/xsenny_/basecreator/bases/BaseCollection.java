package me.xsenny_.basecreator.bases;

import java.util.ArrayList;

public class BaseCollection {
    private ArrayList<Base> collection;

    public BaseCollection(ArrayList<Base> collection) {
        this.collection = collection;
    }

    public ArrayList<Base> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<Base> collection) {
        this.collection = collection;
    }
}
