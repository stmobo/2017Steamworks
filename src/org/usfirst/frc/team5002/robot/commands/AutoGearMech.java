package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoGearMech extends Command {
	private final double invertAxis = -1.0;
    private final double distance = 1.9812;       // meters
    private final double distThreshold = 0.1;     // meters
    private final double driveSpeed = 0.25;       //

    public AutoGearMech() {
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.sensors.navx.resetDisplacement();
        Robot.drivetrain.setSteerDegreesCollective(0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        /* Note: due to the orientation of the roboRIO / NavX relative to the
         * robot body, forward and back lie along the X axis. */

        if(Math.abs(Robot.sensors.navx.getDisplacementX() - distance) > distThreshold) {
            if(Robot.sensors.navx.getDisplacementX() > (invertAxis*distance)) {
                Robot.drivetrain.setDriveSpeedCollective(driveSpeed);
            } else {
                Robot.drivetrain.setDriveSpeedCollective(-driveSpeed);
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.sensors.navx.getDisplacementX() - distance) < distThreshold;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.setDriveSpeedCollective(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}

