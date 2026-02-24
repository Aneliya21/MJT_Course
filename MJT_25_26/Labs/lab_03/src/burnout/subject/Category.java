package bg.sofia.uni.fmi.mjt.burnout.subject;

public enum Category {
    MATH(0, 0.2),
    PROGRAMMING(1, 0.1),
    THEORY(2, 0.15),
    PRACTICAL(3, 0.05);

    private int value;
    private double coef;

    Category(int value, double coef) {
        this.value = value;
        this.coef = coef;
    }

    public int getValue() {
        return value;
    }

    public double getCoef() {
        return coef;
    }
}