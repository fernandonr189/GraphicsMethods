package models;

public class Transformations {
    private Rotator rotator;
    private Scaler scaler;

    private boolean isScaling;
    private boolean isRotating;

    private double currentScale;
    private double currentAngle;

    public Transformations() {
        currentScale = 1;
        isRotating = false;
        isScaling = false;
    }

    public Rotator getRotator() {
        return this.rotator;
    }

    public Scaler getScaler() {
        return this.scaler;
    }

    public double getCurrentScale() {
        return this.currentScale;
    }

    public double getCurrentAngle() {
        return this.currentAngle;
    }

    public boolean isScaling() {
        return isScaling;
    }

    public boolean isRotating() {
        return isRotating;
    }


    public void setRotator(Rotator _rotator) {
        this.rotator = _rotator;
    }

    public void setScaler(Scaler _scaler) {
        this.scaler = _scaler;
    }

    public void setCurrentScale(double _currentScale) {
        this.currentScale = _currentScale;
    }

    public void setCurrentAngle(double _currentAngle) {
        this.currentAngle = _currentAngle;
    }

    public void setScaling(boolean _isScaling) {
        this.isScaling = _isScaling;
    }

    public void setRotating(boolean _isRotating) {
        this.isRotating = _isRotating;
    }
}
