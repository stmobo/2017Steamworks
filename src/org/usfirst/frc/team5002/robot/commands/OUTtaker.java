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
<<<<<<< HEAD
        requires(Robot.intake);
        //getting right infor from right places
=======
        requires(Robot.outtake);
>>>>>>> refs/remotes/origin/master
    }

    protected void initialize() {
    }

<<<<<<< HEAD
    protected void execute() {							//Makes motor go backward when activated
        Robot.intake.runBackwards();
        //the other part to intaker, reverse
=======
    protected void execute() {		//Makes motor go backward when activated
        Robot.outtake.run();
>>>>>>> refs/remotes/origin/master

    }

    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
<<<<<<< HEAD
    	Robot.intake.stop();
    	//stops once it's done
=======
    	Robot.outtake.stop();
>>>>>>> refs/remotes/origin/master
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}