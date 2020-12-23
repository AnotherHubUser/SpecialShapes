package model.shapes;

public class Parallelepiped extends Shape {
    private final Double length_;
    private final Double side_;
    private final Double high_;


    public Parallelepiped() {
        this.length_ = this.side_ = this.high_ = 10.0;
    }

    public Parallelepiped(Double length, Double side, Double high) {
        this.length_ = length;
        this.side_ = side;
        this.high_ = high;
    }

    public Double Perimeter() {return (length_ + side_ + high_) * 4;}

    @Override
    public Double volume() {
        return length_*side_*high_;
    }

    @Override
    public String type() { return "Parallelepiped";}

    @Override
    public boolean isPretty() {
        return length_ % 2 == 0;
    }

    @Override
    public Double getSide() { return  side_;}

    public Double getLength() {return  length_;}

    public Double getHigh() {return high_;}
}
