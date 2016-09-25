package com.webapplication.utils;


public class DoubleList implements Comparable<DoubleList> {

    private Double similarity;
    private String id;

    public DoubleList(Double similarity, String id) {
        this.similarity = similarity;
        this.id = id;
    }

    @Override
    public int compareTo(DoubleList o) {
        return similarity.compareTo(o.similarity);
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
