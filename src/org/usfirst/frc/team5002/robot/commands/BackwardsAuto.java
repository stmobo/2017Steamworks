package org.usfirst.frc.team5002.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BackwardsAuto extends Command {

    public BackwardsAuto() {
       //define some stuff
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//nothing
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//run backwards for 6.5 rotations
    	//stop motors
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//nothing
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//nothing
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	//nothing
    }
}
