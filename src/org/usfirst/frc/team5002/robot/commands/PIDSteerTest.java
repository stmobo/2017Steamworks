package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 * PIDSteerTest -- Flips steer motors 180 degrees for PID testing purposes
 */
public class PIDSteerTest extends Command {
	protected void execute(){
		Robot.drivetrain.setSwervePosition(180, 180, 180, 180);
	}

    public PIDSteerTest() {
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
