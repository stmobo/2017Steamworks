package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * PIDSteerTest -- Flips one module's steer motor 180 degrees
 */
public class PIDSteerTestSingle extends Command {
	private CANTalon srx;

	protected void execute(){
		this.srx.set(Robot.oi.getForwardAxis());
	}

	public PIDSteerTestSingle(CANTalon motor) {
		requires(Robot.drivetrain);
		this.srx = motor;
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
