package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.OI;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author sebastian and bri
 * PIDSteerCollective -- Steer all swerve modules collectively.
 */
public class PIDSteerCollective extends Command {
	
	private static final double joystickDeadband = 0.10;
	private static final double maxDriveOutput = 1.0;
	//setting limits and max output
	
	
	private double strRevs = 0;
	
	protected void execute(){
		if(Robot.oi.arcadeStick.getRawButton(1)) {
			strRevs = Robot.oi.getHorizontalAxis() * 1024.0;
			//settin the oi and making math
		}
		double fwd = (Math.abs(Robot.oi.getForwardAxis()) > joystickDeadband) ? Robot.oi.getForwardAxis() : 0.0;
		//math that does something

		Robot.drivetrain.fl_steer.set(strRevs+Robot.drivetrain.steer_offsets[0]);
		Robot.drivetrain.fr_steer.set(strRevs+Robot.drivetrain.steer_offsets[1]);
		Robot.drivetrain.bl_steer.set(strRevs+Robot.drivetrain.steer_offsets[2]);
		Robot.drivetrain.br_steer.set(strRevs+Robot.drivetrain.steer_offsets[3]);
		//setting steer offsets

		/*
		Robot.drivetrain.fr_drive.set(fwd);
		Robot.drivetrain.fl_drive.set(fwd);
		Robot.drivetrain.br_drive.set(fwd);
		Robot.drivetrain.bl_drive.set(fwd);
		*/
		
		SmartDashboard.putNumber("FL-Pos", Robot.drivetrain.fl_steer.getPosition());
		SmartDashboard.putNumber("FR-Pos", Robot.drivetrain.fr_steer.getPosition());
		SmartDashboard.putNumber("BL-Pos", Robot.drivetrain.bl_steer.getPosition());
		SmartDashboard.putNumber("BR-Pos", Robot.drivetrain.br_steer.getPosition());
		//getting position, puts to smartdashboard
		
		
		//SmartDashboard.putNumber("Steer Cmd", strRevs);
	}

	public PIDSteerCollective() {
		requires(Robot.drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		/*
		Robot.drivetrain.fl_steer.setPosition(0);
		Robot.drivetrain.bl_steer.setPosition(0);
		Robot.drivetrain.br_steer.setPosition(0);
		Robot.drivetrain.fr_steer.setPosition(0);
		*/
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
