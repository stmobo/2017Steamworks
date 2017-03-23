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

    /* Voltage varies inversely with distance measured by sensor. */
    private final double minDistance = 10;
    private final double maxVoltage = 3.0;

    private final double maxDistance = 80;
    private final double minVoltage = 0.4;

    private double startYaw;
    public static AHRS navx;

    public Sensors() {
        distLeft = new AnalogInput(RobotMap.distSensorLeft);
        distRight = new AnalogInput(RobotMap.distSensorRight);

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
        } else {
        	startYaw = 0;
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

        SmartDashboard.putNumber("Start Yaw", startYaw);
		if(navx != null) {
			SmartDashboard.putBoolean("NavX Present", true);
			SmartDashboard.putBoolean("Calibrating", navx.isCalibrating());
			SmartDashboard.putBoolean("Connected", navx.isConnected());

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
