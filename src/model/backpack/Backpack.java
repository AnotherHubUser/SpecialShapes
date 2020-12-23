package model.backpack;

import model.shapes.Shape;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;

public class Backpack<T extends Shape> {
    private Double size_ = 0.0;
    private final Double capacity_;
    private final ArrayList<T> shapes_;

    public Backpack() {
        this.capacity_ = 1923096.0;
        this.size_ = 0.0;
        this.shapes_ = new ArrayList<>();
    }

    public Backpack(Double volume) {
        this.capacity_ = volume;
        this.size_ = 0.0;
        this.shapes_ = new ArrayList<>();
    }


    public static class SizeException extends RuntimeException {
        public SizeException(String message) {
            super(message);
        }
    }

    public static class FindException extends RuntimeException {
        public FindException(String message) {
            super(message);
        }
    }

    public void put(T figure) throws SizeException {
        if (size_ + figure.volume() > capacity_) {
            throw new SizeException("Not this time, mate, with this figure you will be overfull");
        }
        shapes_.add(figure);
        size_ += figure.volume();
        sort();
    }

    public void erase(T figure) throws FindException {
        if (!shapes_.contains(figure)) {
            throw new FindException("Someone has already erased this figure before you!");
        }
        shapes_.remove(figure);
        size_ -= figure.volume();
        sort();
    }


    public void sort() {
        shapes_.sort(
                Comparator.comparing(
                        (Function<Shape, Double>) Shape::volume).reversed());
//        shapes_.sort(Collections.reverseOrder());
    }

    public Double capacity() {
        return capacity_;
    }

    public Double size() {
        return size_;
    }

    public ArrayList<T> getItems() {
        return shapes_;
    }
}
