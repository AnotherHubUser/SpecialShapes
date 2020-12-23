package model.shapes;

public class Cube extends Shape {
    private final Double length_;


    public Cube() {
        this.length_ = 10.0;
    }

    public Cube(Double length) {
        this.length_ = length;
    }

    public Double Perimeter() {return length_ * 12;}

    @Override
    public Double volume() {
        return length_*length_*length_;
    }

    @Override
    public String type() { return "Cube";}

    @Override
    public boolean isPretty() {
        return length_ % 2 == 0;
    }

    @Override
    public Double getSide() { return  length_;}
}
