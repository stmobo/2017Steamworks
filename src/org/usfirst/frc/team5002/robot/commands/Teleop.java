package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Teleop.java -- teleop drive control code for linear movement + rotation
 */
public class Teleop extends Command {
	private static final double joystickDeadband = 0.10;
	private static final double maxDriveOutput = 1.0;

	public static final double LENGTH_INCHES = 14.5;
	public static final double WIDTH_INCHES = 16.5;

	private double speedlimit(double speedIn) {
		return (Math.abs(speedIn) > maxDriveOutput) ? Math.signum(speedIn)*maxDriveOutput : speedIn;
	}
	
	/**
	 * Given forward, left/right and rotational clockwise speeds return an array of doubles matching:
	 * WS1 (front right wheel speed command, 0 to +1)
	 * WS2 (front left wheel speed command, 0 to +1)
	 * WS3 (rear left wheel speed command, 0 to +1)
	 * WS4 (rear right wheel speed command, 0 to +1)
	 *
	 
	 * @param fwd	-1.0 to 1.0, forward to reverse velocity
	 * @param str	-1.0 to 1.0, left to right velocity
	 * @param rcw	-1.0 to 1.0, clockwise rotational velocity
	 * @return		Array of Doubles matching ws1-ws4 and wa1-wa4
	 */
	protected void execute(){
		double fwd = (Math.abs(Robot.oi.getForwardAxis()) > joystickDeadband) ? Robot.oi.getForwardAxis() : 0.0;
		double str = (Math.abs(Robot.oi.getHorizontalAxis()) > joystickDeadband) ? Robot.oi.getHorizontalAxis() : 0.0;
		double rcw = (Math.abs(Robot.oi.getTurnAxis()) > joystickDeadband) ? Robot.oi.getTurnAxis() : 0.0;

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

		double spd_fr = Math.sqrt(Math.pow(b, 2) + Math.pow(c, 2));
		maxWs = spd_fr;

		double spd_fl = Math.sqrt(Math.pow(b, 2) + Math.pow(d, 2));
		maxWs = spd_fl > maxWs ? spd_fl : maxWs;

		double spd_bl = Math.sqrt(Math.pow(a, 2) + Math.pow(d, 2));
		maxWs = spd_bl > maxWs ? spd_bl : maxWs;

		double spd_br = Math.sqrt(Math.pow(a, 2) + Math.pow(c, 2));
		maxWs = spd_br > maxWs ? spd_br : maxWs;

		spd_fr = maxWs > 1 ? spd_fr / maxWs : spd_fr;
		spd_fl = maxWs > 1 ? spd_fl / maxWs : spd_fl;
		spd_bl = maxWs > 1 ? spd_bl / maxWs : spd_bl;
		spd_br = maxWs > 1 ? spd_br / maxWs : spd_br;

		/*
		 * WA1 (front right clockwise angle, degrees)
		 * WA2 (front left clockwise angle, degrees)
		 * WA3 (rear left clockwise angle, degrees)
		 * WA4 (rear right clockwise angle, degrees)
		 */
		double ang_fr = (c==0 && b==0) ? 0.0 : (Math.atan2(b, c) * 180 / Math.PI);
		double ang_fl = (d==0 && b==0) ? 0.0 : (Math.atan2(b, d) * 180 / Math.PI);
		double ang_bl = (d==0 && a==0) ? 0.0 : (Math.atan2(a, d) * 180 / Math.PI);
		double ang_br = (c==0 && a==0) ? 0.0 : (Math.atan2(a, c) * 180 / Math.PI);

		Robot.drivetrain.setSwervePosition(ang_fl, ang_fr, ang_bl, ang_br);
		Robot.drivetrain.setDriveOutput(
				speedlimit(spd_fl),
				speedlimit(spd_fr),
				speedlimit(spd_bl),
				speedlimit(spd_br)
			);
	}

    public Teleop() {
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
