package org.usfirst.frc.team5002.robot.subsystems;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SPI.Port;

import edu.wpi.first.wpilibj.networktables.*;

public class Sensors extends Subsystem {
    private AnalogInput distLeft;
    private AnalogInput distRight;

    /* Voltage varies inversely with distance measured by sensor. */
    private final double minDistance = 10;
    private final double maxVoltage = 3.0;

    private final double maxDistance = 80;
    private final double minVoltage = 0.4;

    private final double smoothingConstant = 0.25;
    private double distOut = 0;

    private double startYaw;
    public static AHRS navx;
    private AHRS.BoardYawAxis yawAxis;

    private NetworkTable jetson;

    public Sensors() {
        distLeft = new AnalogInput(RobotMap.distSensorLeft);
        distRight = new AnalogInput(RobotMap.distSensorRight);

        distLeft.setAverageBits(64);
        distRight.setAverageBits(64);

        jetson = NetworkTable.getTable("vision");

        try {
			/* NOTE: With respect to the NavX, the robot's front is in the -X direction.
			 * The robot's right side is in the +Y direction,
			 * and the robot's top side is in the +Z direction as usual.
		     * Clockwise rotation = increasing yaw.
		     */
			navx = new AHRS(SerialPort.Port.kMXP);

            navx.zeroYaw();
            startYaw = navx.getYaw();
            yawAxis = navx.getBoardYawAxis();

		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);

            navx = null;
            startYaw = 0;
            yawAxis = null;
		}
    }

    /**
     * Returns true if the jetson is connected, false if not.
     */
    public boolean getJetsonStatus() {
        return jetson.getBoolean("connected", false);
    }

    /**
     * Returns true if the vision targets have been detected.
     */
    public double canSeeTargets() {
        return jetson.getBoolean("valid", false);
    }

    /**
     * Returns the distance to the vision targets, as reported by the Jetson.
     *
     * If the jetson is not connected, this returns 0.
     */
    public double getVisualDistance() {
        return jetson.getNumber("distance", 0.0);
    }

    /**
     * Returns the lateral (left/right) offset from the vision targets,
     * as reported by the Jetson.
     */
    public double getVisualOffset() {
        return jetson.getNumber("offset", 0.0);
    }

    /* A more reliable form of navx.getAngle() */
	public double getRobotHeading() {
		if(navx != null) {
			if(Math.abs(navx.getAngle()) <= 0.001) {
				return (navx.getYaw() - startYaw);
			} else {
				return navx.getAngle();
			}
		} else {
			return 0;
		}
	}

    private double voltageToDistance(double v) {
        double cvtFactor = (minDistance - maxDistance) / (maxVoltage - minVoltage);
        return maxDistance + ((v - minVoltage) * cvtFactor);
    }

    public double getLeftVoltage() {
    	return distLeft.getAverageVoltage();
    }

    public double getRightVoltage() {
    	return distRight.getAverageVoltage();
    }

    public double getLeftDistance() {
        return voltageToDistance(distLeft.getAverageVoltage());
    }

    public double getRightDistance() {
        return voltageToDistance(distRight.getAverageVoltage());
    }

    public void updateDistance() {
    	double dist = distLeft.getAverageVoltage();
    	distOut += (smoothingConstant * (dist - distOut));
    }

    public double getFrontDistance() {
    	return distOut;
    }

    public void updateSD() {
        SmartDashboard.putNumber("LeftDist-Voltage", distLeft.getAverageVoltage());
        SmartDashboard.putNumber("RightDist-Voltage", distRight.getAverageVoltage());

    	double avgDist = (distLeft.getAverageVoltage() + distRight.getAverageVoltage()) / 2.0;
    	SmartDashboard.putNumber("Avg. Sensor Voltage", avgDist);
        SmartDashboard.putNumber("Frontal Distance", getFrontDistance());

        SmartDashboard.putNumber("LeftDist-cm", getLeftDistance());
        SmartDashboard.putNumber("RightDist-cm", getRightDistance());

        SmartDashboard.putNumber("Start Yaw", startYaw);
		if(navx != null) {
			SmartDashboard.putBoolean("NavX Present", true);
			SmartDashboard.putBoolean("Calibrating", navx.isCalibrating());
			SmartDashboard.putBoolean("Connected", navx.isConnected());

			SmartDashboard.putNumber("X-Displacement", navx.getDisplacementX());
			SmartDashboard.putNumber("Y-Displacement", navx.getDisplacementY());

			SmartDashboard.putNumber("Heading", navx.getAngle());
			SmartDashboard.putNumber("Compass", navx.getCompassHeading());
			SmartDashboard.putNumber("Yaw", navx.getYaw());
			SmartDashboard.putNumber("Fused", navx.getFusedHeading());
			if(Robot.oi.focEnabled) {
				SmartDashboard.putString("Control Mode", "Field-Oriented");
			} else {
				SmartDashboard.putString("Control Mode", "Robot-Oriented (FOC available)");
			}
		} else {
			SmartDashboard.putBoolean("NavX Present", false);
			SmartDashboard.putString("Control Mode", "Robot-Oriented (FOC unavailable)");
		}
    }

    public void initDefaultCommand() {
    }
}
