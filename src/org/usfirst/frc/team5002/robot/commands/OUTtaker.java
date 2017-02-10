package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *@author (Jo)Nathan, Bri, elweb
 *@version Last Updated 2/8/17
 *motor that spits balls into hopper
 */
public class OUTtaker extends Command {

    public OUTtaker() {
        requires(Robot.outtake);
        //getting right infor from right places
    }

    protected void initialize() {
    }

    protected void execute() {		//Makes motor go backward when activated
        Robot.outtake.run();
        //the other part to intaker, reverse

    }

    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.outtake.stop();
    	//stops once it's done
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}