package model.shapes;

public class Tetrahedron  extends Shape {
    private final Double side_;

    public Tetrahedron() {
        this.side_  = 10.0;
    }

    public Tetrahedron(Double radius) {
        this.side_ = radius;
    }

    public Double Perimeter(){ return side_ * 6; }

    @Override
    public Double volume() {
        return Math.sqrt(2) * side_ * side_ * side_ / 12;
    }

    @Override
    public String type() { return "Tetrahedron";}

    @Override
    public boolean isPretty() {
        return side_ <= volume();
    }

    @Override
    public Double getSide() { return  side_; }
}
