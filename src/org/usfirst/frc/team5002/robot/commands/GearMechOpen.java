package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *@author Bri 
 *@version Last Modified  4/17/17
 */


public class GearMechOpen extends Command {

	Timer tm;
	
    public GearMechOpen() {
    	requires(Robot.gearMech);
    	tm = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	tm.reset();
    	tm.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//if (tm.get() < 0.4){
    		Robot.gearMech.open();
    	}
    	/*else{
    		Robot.gearMech.reset();
    	}
    	
    } */

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return tm.hasPeriodPassed(0.2); //.45 if reset func used
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.gearMech.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
