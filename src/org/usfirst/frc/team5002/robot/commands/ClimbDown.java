package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 */
public class ClimbDown extends Command {

    public ClimbDown() {
        requires(Robot.ropeClimber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        /* Disable reverse climb in competition setting */
        if(!DriverStation.getInstance().isFMSAttached()) {
        	Robot.ropeClimber.runBackwards();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return DriverStation.getInstance().isFMSAttached();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.ropeClimber.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
