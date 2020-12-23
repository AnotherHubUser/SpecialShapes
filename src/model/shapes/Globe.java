package model.shapes;

public class Globe extends Shape {
    private final Double radius_;

    public Globe() {
        this.radius_ = 10.0;
    }

    public Globe(Double radius) { this.radius_ = radius;}

    @Override
    public Double volume() {
        return 4 * Math.PI * radius_ * radius_ * radius_ / 3;
    }

    @Override
    public String type() { return "Globe";}

    @Override
    public boolean isPretty() {
        return true;
    }

    @Override
    public Double getSide() { return radius_;}
}
