package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.OI;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * PIDSteerCollective -- Steer all swerve modules collectively.
 */
public class PIDSteerCollective extends Command {
	private static final double joystickDeadband = 0.10;
	private static final double maxDriveOutput = 1.0;
	
	protected void execute(){
		double fwd = (Math.abs(Robot.oi.getForwardAxis()) > joystickDeadband) ? Robot.oi.getForwardAxis() : 0.0;
		double str = (Math.abs(Robot.oi.getHorizontalAxis()) > joystickDeadband) ? Robot.oi.getHorizontalAxis() : 0.0;

		Robot.drivetrain.fr_steer.set(str);
		Robot.drivetrain.fl_steer.set(str);
		Robot.drivetrain.br_steer.set(str);
		Robot.drivetrain.bl_steer.set(str);

		Robot.drivetrain.fr_drive.set(fwd);
		Robot.drivetrain.fl_drive.set(fwd);
		Robot.drivetrain.br_drive.set(fwd);
		Robot.drivetrain.bl_drive.set(fwd);
	}

	public PIDSteerCollective() {
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
