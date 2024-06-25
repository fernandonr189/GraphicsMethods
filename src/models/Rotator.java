package models;

public class Rotator {

    private final double targetAngle;
    private final double originalTime;
    private final double rotationRate;
    private double newAngle;

    public Rotator(double targetAngle, double initialTime, double timeDelta) {
        this.targetAngle = targetAngle;
        this.originalTime = initialTime;
        this.rotationRate = (targetAngle) / (timeDelta);
    }
    
    public double newAngle(double t) {
        newAngle = (rotationRate * (t - originalTime));
        return newAngle;
    }

    public boolean isFinished() {
        return (newAngle >= targetAngle);
    }

    public double getTargetAngle() {
        return targetAngle;
    }
}
