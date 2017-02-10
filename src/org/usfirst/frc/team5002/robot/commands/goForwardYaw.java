package org.usfirst.frc.team5002.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.RobotMap;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class goForwardYaw extends Command {
	
	private double pos_fl = 0;
	private double pos_fr = 0;
	private double pos_bl = 0;
	private double pos_br = 0;
	
	Encoder encoder = new Encoder(0, 1, true);
	
	//1440 pulse per revolution
	
	int count = encoder.get();
	
	double rate = encoder.getRate();
	
	double distance = encoder.getDistance();
	

	
	
    public goForwardYaw() {
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.setSwervePosition(pos_fl, pos_fr, pos_bl, pos_br);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(count < 666){
    		
    		Robot.drivetrain.setDriveOutput(1.0, 1.0, 1.0, 1.0);
    		
    	}
    	
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
