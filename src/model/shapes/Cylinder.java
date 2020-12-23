package model.shapes;

public class Cylinder extends Shape {
    private final Double radius_;
    private final Double high_;

    public Cylinder() {
        this.radius_ = 10.0;
        this.high_ = 10.0;
    }

    public Cylinder(Double radius, Double high) {
        this.radius_ = radius;
        this.high_ = high;
    }

    @Override
    public Double volume() {
        return Math.PI * radius_ * radius_ * high_;
    }

    @Override
    public String type() { return "Cylinder";}

    @Override
    public boolean isPretty() {
        return radius_ <= volume();
    }

    @Override
    public Double getSide() { return radius_; }

    public Double getHigh() {return high_; }
}
