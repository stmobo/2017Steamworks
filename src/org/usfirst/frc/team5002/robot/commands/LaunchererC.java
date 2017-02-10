package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.Launcherer;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
/**
 * @author Grace/Justice
*/
public class LaunchererC extends Command {
	
	 //organization, is set inside the command package


    public LaunchererC() {
    	//the overall command
        requires(Robot.launcherer);
        //making sure it uses launcherer
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.launcherer.run();
    	//sets the motors to 1 speed so that it moves
    	//runs launcherer subsystem
    	
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
        return false;
        //just stops
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.launcherer.stop();
    	//sets the motors to 0 speed so that it doesn't move
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}