package org.usfirst.frc.team5002.robot.subsystems;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.RobotMap;
import org.usfirst.frc.team5002.robot.commands.*;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Encoders extends Subsystem {
	
	public float getYaw() {
		
	}
		
		
	
	private CANTalon fl_drive = 0;

	Encoder encoder = new Encoder(0, 1, false);
	
	private double coder = encoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
	

    public void initDefaultCommand() {
       encoder.reset();
       
       
       if(encoder.get() != 2500){
    	   while(true){
    		   
    		   Robot.fl_drive.motorGo(1.0);
    		   
    		   
    		   
    	   }
    	   
    	   
       }
    }
}

