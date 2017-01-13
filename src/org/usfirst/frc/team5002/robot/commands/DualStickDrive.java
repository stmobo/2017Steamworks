package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 * DualStickDrive.java -- teleop drive control code for linear movement + rotation
 */
public class DualStickDrive extends Command {

	Joystick linStick; // controls linear movement
	Joystick rotStick; // controls rotation
	
	private static final double joystickDeadband = 0.10;
	private static final double maxDriveOutput = 1.0;
	
    public DualStickDrive(Joystick lin, Joystick rot) {
        requires(Robot.drivetrain);
        
        linStick = lin;
        rotStick = rot;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double driveAngle = linStick.getDirectionDegrees();
    	
    	Robot.drivetrain.setSwervePosition(driveAngle);
    	
    	/* Get joystick inputs w/ deadbanding */
    	double rotMag = (Math.abs(rotStick.getX()) >= joystickDeadband) ? rotStick.getX() : 0;
    	double driveMag = (Math.abs(linStick.getMagnitude()) >= joystickDeadband) ? linStick.getMagnitude() : 0;
    	
    	/* Arcade-drive esque code, I think?
    	 * I took this off of some VEX code I have, and it seems _about_ right
    	 * but I don't quite know the theory / reasoning behind it. (ask David) */
    	double right = driveMag - rotMag;
    	double left = driveMag + rotMag;
    	
    	/* cap motor outputs */
    	left = (Math.abs(left) > maxDriveOutput) ? Math.signum(left) * maxDriveOutput : left;
    	right = (Math.abs(right) > maxDriveOutput) ? Math.signum(right) * maxDriveOutput : right;
    	
    	Robot.drivetrain.setDriveOutput(left, right);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
