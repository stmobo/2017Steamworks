package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Teleop.java -- teleop drive control code for linear movement + rotation
 */
public class Teleop extends Command {
	private static final double joystickDeadband = 0.10;
	private static final double maxDriveOutput = 1.0;
	
	public static final double LENGTH_INCHES = 30.0;
	public static final double WIDTH_INCHES = 20.0;
	
	/**
	 * Given forward, left/right and rotational clockwise speeds return an array of doubles matching:
	 * WS1 (front right wheel speed command, 0 to +1)
	 * WS2 (front left wheel speed command, 0 to +1)
	 * WS3 (rear left wheel speed command, 0 to +1)
	 * WS4 (rear right wheel speed command, 0 to +1)
	 * 
	 * WA1 (front right clockwise angle, degrees)
	 * WA2 (front left clockwise angle, degrees)
	 * WA3 (rear left clockwise angle, degrees)
	 * WA4 (rear right clockwise angle, degrees)
	 * @param fwd	-1.0 to 1.0, forward to reverse velocity
	 * @param str	-1.0 to 1.0, left to right velocity
	 * @param rcw	-1.0 to 1.0, clockwise rotational velocity
	 * @return		Array of Doubles matching ws1-ws4 and wa1-wa4
	 */
	protected void execute(){
		Joystick stick = OI.getJoystick();
		double fwd = stick.getX();
		double str = stick.getY();
		double rcw = stick.getZ();
		
		if(Math.abs(fwd)>1.0 || Math.abs(str)>1.0 || Math.abs(rcw)>1.0){
			return;
		}
		
		double r = Math.sqrt(Math.pow(LENGTH_INCHES,2) + Math.pow(WIDTH_INCHES,2));
		
		double a = str - rcw * (LENGTH_INCHES / r);	
		double b = str + rcw * (LENGTH_INCHES / r);
		double c = fwd - rcw * (WIDTH_INCHES / r);
		double d = fwd + rcw * (WIDTH_INCHES / r);
		
		System.out.println("r:"+r+", a:"+a+", b:"+b+", c:"+c+", d:"+d);
		
		double maxWs; 
		
		double ws1 = Math.sqrt(Math.pow(b, 2) + Math.pow(c, 2));
		maxWs = ws1;
		
		double ws2 = Math.sqrt(Math.pow(b, 2) + Math.pow(d, 2));
		maxWs = ws2 > maxWs ? ws2 : maxWs;
		
		double ws3 = Math.sqrt(Math.pow(a, 2) + Math.pow(d, 2));
		maxWs = ws3 > maxWs ? ws3 : maxWs;
		
		double ws4 = Math.sqrt(Math.pow(a, 2) + Math.pow(c, 2));
		maxWs = ws4 > maxWs ? ws4 : maxWs;
		
		ws1 = maxWs > 1 ? ws1 / maxWs : ws1;
		ws2 = maxWs > 1 ? ws2 / maxWs : ws2;
		ws3 = maxWs > 1 ? ws3 / maxWs : ws3;
		ws4 = maxWs > 1 ? ws4 / maxWs : ws4;

		double wa1 = (c==0 && b==0) ? 0.0 : (Math.atan2(b, c) * 180 / Math.PI);
		double wa2 = (d==0 && b==0) ? 0.0 : (Math.atan2(b, d) * 180 / Math.PI);
		double wa3 = (d==0 && a==0) ? 0.0 : (Math.atan2(a, d) * 180 / Math.PI);
		double wa4 = (c==0 && a==0) ? 0.0 : (Math.atan2(a, c) * 180 / Math.PI);
		
		Robot.drivetrain.setSwervePosition(wa2, wa1, wa4, wa3);
		Robot.drivetrain.setDriveOutput(ws2, ws1, ws4, ws3);
	}
	
    public Teleop(Joystick in) {
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
