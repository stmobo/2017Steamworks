package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousTemp extends Command {
	Timer timer;
	
	double leftBias = 0.1;
    
	public AutonomousTemp(double bias) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    	leftBias += bias;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
    	timer.start();
    	Robot.drivetrain.setDriveTeleop();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.FL, 0.0);
    	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.FR, 0.0);
    	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.BL, 0.0);
    	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.BR, 0.0);
    	
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.FL, 0.5 + leftBias);
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.BL, 0.5 + leftBias);
    	
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.FR, 0.5 - leftBias);
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.BR, 0.5 - leftBias);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.hasPeriodPassed(1.5);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.FL, 0.0);
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.FR, 0.0);
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.BL, 0.0);
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.BR, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.FL, 0.0);
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.FR, 0.0);
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.BL, 0.0);
    	Robot.drivetrain.setDriveOutput(SwerveDrive.ModulePosition.BR, 0.0);
    }
}
