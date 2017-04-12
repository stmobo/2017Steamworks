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

public class Sensors extends Subsystem {
    private AnalogInput distLeft;
    private AnalogInput distRight;
    private AnalogInput distClimb;

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

    public Sensors() {
        distLeft = new AnalogInput(RobotMap.distSensorLeft);
        distRight = new AnalogInput(RobotMap.distSensorRight);
        distClimb = new AnalogInput(RobotMap.climbDistSensor);

        distLeft.setAverageBits(64);
        distRight.setAverageBits(64);
        
        try {
			/* NOTE: With respect to the NavX, the robot's front is in the -X direction.
			 * The robot's right side is in the +Y direction,
			 * and the robot's top side is in the +Z direction as usual.
		     * Clockwise rotation = increasing yaw.
		     */
			navx = new AHRS(SerialPort.Port.kMXP);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
			navx = null;
		}

        if(navx != null) {
            navx.zeroYaw();
            startYaw = navx.getYaw();
            yawAxis = navx.getBoardYawAxis();
        } else {
        	startYaw = 0;
            yawAxis = null;
        }
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
    
    public double getClimbVoltage() {
    	return distClimb.getVoltage();
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
