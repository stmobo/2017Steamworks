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
	
	private CANTalon gearmechMove;
	

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
    }
    
    	public GearMech(){
        	gearmechMove = new CANTalon(4);//TODO input actual port number
        	gearmechMove.changeControlMode(TalonControlMode.PercentVbus);
        
        }
        
        public void open(){
        	gearmechMove.set(-1.0);
        }
        public void reset(){
        	gearmechMove.set(0.6);
        }
 
        public void stop(){
        	gearmechMove.set(0);
        }
    }

