package org.usfirst.frc.team5002.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *@author Bri 
 *@version Last Modified  4/17/17
 */
public class GearMech extends Subsystem {
	
	private CANTalon gearmechFwd;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
    }
    
    	public GearMech(){
        	gearmechFwd = new CANTalon(28743);//TODO input actual port number
        	gearmechFwd.changeControlMode(TalonControlMode.PercentVbus);
        
        }
        
        public void open(){
        	gearmechFwd.set(1.0);
        }
 
        public void stop(){
        	gearmechFwd.set(0);
        }
    }

