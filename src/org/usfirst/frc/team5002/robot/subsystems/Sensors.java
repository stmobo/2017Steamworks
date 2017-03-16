package org.usfirst.frc.team5002.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.AnalogInput;

public class Sensors extends Subsystem {
    private AnalogInput distLeft;
    private AnalogInput distRight;

    /* Voltage varies inversely with distance measured by sensor. */
    private final double minDistance = 10;
    private final double maxVoltage = 3.0;

    private final double maxDistance = 80;
    private final double minVoltage = 0.4;

    public Sensors() {
        distLeft = new AnalogInput(RobotMap.distSensorLeft);
        distRight = new AnalogInput(RobotMap.distSensorRight);
    }

    private double voltageToDistance(double v) {
        double cvtFactor = (minDistance - maxDistance) / (maxVoltage - minVoltage);
        return maxDistance + ((v - minVoltage) * cvtFactor);
    }

    public double getLeftDistance() {
        return voltageToDistance(distLeft.getVoltage());
    }

    public double getRightDistance() {
        return voltageToDistance(distRight.getVoltage());
    }

    public void updateSD() {
        SmartDashboard.putNumber("LeftDist-Voltage", distLeft.getVoltage());
        SmartDashboard.putNumber("RightDist-Voltage", distRight.getVoltage());

        SmartDashboard.putNumber("LeftDist-Dist (cm)", getLeftDistance());
        SmartDashboard.putNumber("RightDist-Dist (cm)", getRightDistance());
    }

    public void initDefaultCommand() {
    }
}
