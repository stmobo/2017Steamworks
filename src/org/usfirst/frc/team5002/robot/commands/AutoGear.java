package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;
import org.usfirst.frc.team5002.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 *
 */
public class AutoGear extends Command {
    public static final double targetDistance = 0.7; // voltage
    public static final double distThreshold = 0.03;   // stop when closer than (targetDistance +- distThreshold)
    public static final double driveSpeed = 0.25;   // forward/back drive speed (for adjusting distance to wall)
    
	public AutoGear() {
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.setSteerDegreesCollective(0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        autoGearAlign();
    }

    public static void autoGearAlign() {
    	double dist = Robot.sensors.getFrontDistance();
        
        if(Math.abs(dist - targetDistance) >= distThreshold) {
            /* Adjust distance to target: */
            Robot.drivetrain.setSteerDegreesCollective(0);
            if(dist > targetDistance) {
                Robot.drivetrain.setDriveSpeedCollective(-driveSpeed);
            } else if(dist < targetDistance) {
                Robot.drivetrain.setDriveSpeedCollective(driveSpeed);
            }
        } else {
        	Robot.drivetrain.setDriveSpeedCollective(0.0);
        }
    }

    public static boolean finished() {
        double dist = Robot.sensors.getFrontDistance();
        if(Math.abs(dist - targetDistance) < distThreshold) {
            return true;
        }

        return false;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished();
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.setDriveSpeedCollective(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.drivetrain.setDriveSpeedCollective(0);
    }
}
