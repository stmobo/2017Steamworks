package org.usfirst.frc.team5002.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *@author (Jo)Nathan
 *@version Last Updated 1/19/17
 */
public class Intake extends Subsystem {
	private CANTalon intake;
	
	public Intake(){ 							//Creation of intake motor
	    	intake = new CANTalon(712); //TODO: put actual motor id
	    	intake.changeControlMode(TalonControlMode.Speed);
	    }
	
	public void initDefaultCommand() {
	    	
	    }
	 
	public void run(){ 	//Call to change the motor speed
		intake.set(1.0);
	}
	public void runBackwards(){
		intake.set(-1.0);
	}
	public void stop() {
		intake.set(0);
	}
		
   
}
