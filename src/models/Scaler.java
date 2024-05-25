package models;

public class Scaler {
    private final double scaleSlope;
    private final double scaleOffset;
    private final boolean isGrowing;
    private final double targetScale;
    private double newScale;

    public Scaler(double targetScale, double initialTime, double time, double currentScale) {
        this.targetScale = targetScale;
        isGrowing = targetScale > currentScale;
        this.scaleSlope = (targetScale - currentScale) / time;
        this.scaleOffset = currentScale - (scaleSlope * initialTime);
    }

    public double newScale(double t) {
        newScale = (scaleSlope * (t)) + scaleOffset;
        return newScale;
    }

    public boolean isFinished() {
        return (isGrowing && newScale >= targetScale || (!isGrowing && newScale <= targetScale));
    }
}
