
package org.usfirst.frc.team5002.robot.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5002.robot.Robot;

/**
 *@author (Jo)Nathan, elweb
 *@version Last Updated 2/8/17
 */
public class INtaker extends Command {
	private Timer intakeStopTimer;
	private boolean intakeTimingStarted;
	private boolean intakeTimePassed;
	
	private final boolean enableLimSwitch = false;
	
    public INtaker() {
        requires(Robot.intake);
        intakeStopTimer = new Timer();
        //requires pulls from the correct info
    }

    protected void initialize() {		//Makes motor start at 0
    
    }

    protected void execute() {
    	if(Robot.oi.intakeButtonActivated()) {
    		Robot.intake.run();			//Makes motor go forward when activated
    	} else if(Robot.oi.reverseButtonActivated()) {
    		Robot.intake.runBackwards();
    	} else {
    		Robot.intake.stop();
    	}
        
    	if(enableLimSwitch) {
	    	if(Robot.intake.getLimSwitchPressed()) {
				Robot.intake.run();
				intakeTimePassed = false;
				intakeTimingStarted = false;
				intakeStopTimer.stop();
			} else {
				if(!intakeTimingStarted) {
					intakeTimingStarted = true;
					intakeStopTimer.reset();
					intakeStopTimer.start();
				} else {
					if(!intakeTimePassed && intakeStopTimer.get() >= 1.5) {
						Robot.intake.stop();
						intakeStopTimer.stop();
						intakeTimePassed = true;
					}
				}
			}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.stop();
    	//stops when it ends
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
