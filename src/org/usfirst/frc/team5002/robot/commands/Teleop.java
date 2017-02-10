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
		//double rcw = 0.0;
		double rcw = (Math.abs(Robot.oi.getTurnAxis()) > joystickDeadband) ? Robot.oi.getTurnAxis() : 0.0; 
		
		if(Math.abs(fwd)>1.0 || Math.abs(str)>1.0 || Math.abs(rcw)>1.0){
			return;
		}

		double r = Math.sqrt(Math.pow(LENGTH_INCHES,2) + Math.pow(WIDTH_INCHES,2));

		double a = str - rcw * (LENGTH_INCHES / r);
		double b = str + rcw * (LENGTH_INCHES / r);
		double c = fwd - rcw * (WIDTH_INCHES / r);
		double d = fwd + rcw * (WIDTH_INCHES / r);

		//System.out.println("r:"+r+", a:"+a+", b:"+b+", c:"+c+", d:"+d);

		double maxWs;

		double spd_fr = Math.sqrt(Math.pow(b, 2) + Math.pow(c, 2));
		maxWs = spd_fr;

		double spd_fl = Math.sqrt(Math.pow(b, 2) + Math.pow(d, 2));
		maxWs = spd_fl > maxWs ? spd_fl : maxWs;

		double spd_bl = Math.sqrt(Math.pow(a, 2) + Math.pow(d, 2));
		maxWs = spd_bl > maxWs ? spd_bl : maxWs;

		double spd_br = Math.sqrt(Math.pow(a, 2) + Math.pow(c, 2));
		maxWs = spd_br > maxWs ? spd_br : maxWs;

		double[] angles = new double[4];
		double[] speeds = new double[4];

		speeds[0] = maxWs > 1 ? spd_fr / maxWs : spd_fr;
		speeds[1] = maxWs > 1 ? spd_fl / maxWs : spd_fl;
		speeds[2] = maxWs > 1 ? spd_bl / maxWs : spd_bl;
		speeds[3] = maxWs > 1 ? spd_br / maxWs : spd_br;

		angles[0] = (c==0 && b==0) ? 0.0 : (Math.atan2(b, c) * 180 / Math.PI); // front right
		angles[1] = (d==0 && b==0) ? 0.0 : (Math.atan2(b, d) * 180 / Math.PI); // front left
		angles[2] = (d==0 && a==0) ? 0.0 : (Math.atan2(a, d) * 180 / Math.PI); // back left
		angles[3] = (c==0 && a==0) ? 0.0 : (Math.atan2(a, c) * 180 / Math.PI); // back right

		setDrivetrain(angles, speeds);
	}

	public void setDrivetrain(double[] ang, double[] spd) {
		Robot.drivetrain.fr_steer.set(ang[3] * (1024.0 / 360.0) + Robot.drivetrain.steer_offsets[1]);
		Robot.drivetrain.fl_steer.set(ang[2] * (1024.0 / 360.0) + Robot.drivetrain.steer_offsets[0]);
		Robot.drivetrain.bl_steer.set(ang[1] * (1024.0 / 360.0) + Robot.drivetrain.steer_offsets[2]);
		Robot.drivetrain.br_steer.set(ang[0] * (1024.0 / 360.0) + Robot.drivetrain.steer_offsets[3]);

		Robot.drivetrain.fr_drive.set(spd[0]);
		Robot.drivetrain.fl_drive.set(spd[1] * -1.0);
		Robot.drivetrain.bl_drive.set(spd[2] * -1.0);
		Robot.drivetrain.br_drive.set(spd[3]);
	}

    public Teleop() {
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	/* Set main controls for driving... */
        Robot.drivetrain.configureDriveMotorTeleop(Robot.drivetrain.fl_drive, false);
        Robot.drivetrain.configureDriveMotorTeleop(Robot.drivetrain.fr_drive, false);
        Robot.drivetrain.configureDriveMotorTeleop(Robot.drivetrain.bl_drive, false);
        Robot.drivetrain.configureDriveMotorTeleop(Robot.drivetrain.br_drive, false);
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
