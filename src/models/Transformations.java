package models;

public class Transformations {
    private Rotator rotator;
    private Scaler scaler;

    public Transformations() {

    }

    public Rotator getRotator() {
        return this.rotator;
    }

    public Scaler getScaler() {
        return this.scaler;
    }

    public void setRotator(Rotator _rotator) {
        this.rotator = _rotator;
    }

    public void setScaler(Scaler _scaler) {
        this.scaler = _scaler;
    }
}
