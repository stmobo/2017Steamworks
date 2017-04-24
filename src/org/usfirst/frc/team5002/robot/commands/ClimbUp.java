package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbUp extends Command {
	private final double climbSlowThreshold = 2.5;

    public ClimbUp() {
        requires(Robot.ropeClimber);
    }

     protected void initialize() {
    }


    protected void execute() {
    	Robot.ropeClimber.run();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.ropeClimber.stop();
    }

    protected void interrupted() {
    	end();
    }
}
