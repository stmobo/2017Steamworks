package org.usfirst.frc.team5002.robot.subsystems;

import org.usfirst.frc.team5002.robot.Robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *@author (Jo)Nathan, Bri, elweb
 *@version Last Updated 2/8/17
 *motor that picks balls up off the ground
 */
public class Intake extends Subsystem {
	private CANTalon intake;
	private DigitalInput limSwitch;
	
	public Intake(){ 							//Creation of intake motor
		limSwitch = new DigitalInput(0);
		intake = new CANTalon(42); //TODO: put actual motor id
		intake.changeControlMode(TalonControlMode.PercentVbus);
	}
	
	public void initDefaultCommand() {	
    }
	
	public boolean getLimSwitchPressed() {
		return !limSwitch.get();
	}
	 
	public void run(){ 	//Call to change the motor speed
		intake.set(1.0);
	}
	public void runBackwards(){
		intake.set(-1.0);
	}
	public void stop() {  // stops the motor
		intake.set(0);
	} 
	
	public void updateSD() {
		SmartDashboard.putBoolean("Intake Switch", limSwitch.get());
	}
}
