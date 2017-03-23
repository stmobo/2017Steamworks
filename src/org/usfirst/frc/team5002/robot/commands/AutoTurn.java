package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurn extends Command {
	private double finalTurnAngle;
    private final double turnAngleThreshold = 5.0;  // stop when robot heading is within (finalTurnAngle +/- turnAngleThreshold)
    private final double turnSpeed = 0.1;

	public AutoTurn(double degreesOffset) {
    	requires(Robot.drivetrain);
    	finalTurnAngle = Robot.sensors.getRobotHeading() + degreesOffset;
    }

    // Called just before this Command runs the first time
    protected void initialize() {}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if( Math.abs(Robot.sensors.getRobotHeading() - finalTurnAngle) >= turnAngleThreshold ) {
        	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.FL, 45.0);
        	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.FR, 135.0);
        	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.BL, -45.0);
        	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.BR, -135.0);

            if(Robot.sensors.getRobotHeading() > finalTurnAngle) {
                Robot.drivetrain.setDriveSpeedCollective(-turnSpeed);
            } else {
                Robot.drivetrain.setDriveSpeedCollective(turnSpeed);
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.sensors.getRobotHeading() - finalTurnAngle) < turnAngleThreshold;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.setDriveSpeedCollective(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.drivetrain.setDriveSpeedCollective(0.0);
    }
}
