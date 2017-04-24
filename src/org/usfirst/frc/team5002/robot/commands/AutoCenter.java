package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoCenter extends Command {
	Timer timer;

	double leftBias = 0.0;
    double fwdTime = 2.0;
    double bckTime = 1.0;

	public AutoCenter(double bias) {
    	requires(Robot.drivetrain);
    	requires(Robot.gearMech);
    	timer = new Timer();
    	leftBias += bias;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.FL, 0.0);
    	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.FR, 0.0);
    	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.BL, 0.0);
    	Robot.drivetrain.setSteerDegrees(SwerveDrive.ModulePosition.BR, 0.0);

        if(timer.get() < fwdTime) {
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FL, 0.5 + leftBias);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BL, 0.5 + leftBias);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FR, 0.5 - leftBias);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BR, 0.5 - leftBias);
            Robot.gearMech.stop();
        } else if(timer.get() < (fwdTime+0.2)) {
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FL, 0);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BL, 0);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FR, 0);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BR, 0);

            Robot.gearMech.open();
        } else if(timer.get() < (fwdTime+bckTime+0.2)) {
            Robot.gearMech.stop();

        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FL, -0.5 + leftBias);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BL, -0.5 + leftBias);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FR, -0.5 - leftBias);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BR, -0.5 - leftBias);
        } else {
            Robot.gearMech.stop();
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FL, 0);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BL, 0);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FR, 0);
        	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BR, 0);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.hasPeriodPassed(fwdTime+bckTime+0.4);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FL, 0.0);
    	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.FR, 0.0);
    	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BL, 0.0);
    	Robot.drivetrain.setDriveSpeed(SwerveDrive.ModulePosition.BR, 0.0);
        Robot.gearMech.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
