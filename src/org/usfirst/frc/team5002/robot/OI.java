package org.usfirst.frc.team5002.robot;

import edu.wpi.first.wpilibj.Joystick;



/**
 * @author elweb @version Last Modified 1/18/17
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick arcadeStick; //named Joystick
	
	public OI(){
		arcadeStick = new Joystick(0); //gave Joystick a job
	}
	
	public double getForwardAxis() {
		return arcadeStick.getRawAxis(1) * -1.0;//allows the Joystick to command the Robot's forwards and backwards movement
	}
	
	public double getHorizontalAxis(){
		return arcadeStick.getRawAxis(0) * -1.0;//allows the Joystick to command the Robot's side to side movement
	}
	
	public double getTurnAxis(){
		return arcadeStick.getRawAxis(4);//allows the Joystick to command the rotation of the Robot
	}
	public void UpdateSD(){
		Robot.drivetrain.UpdateSD();//sends all the data from SwerveDrive subsystem to the SmartDashboard
	}
}
