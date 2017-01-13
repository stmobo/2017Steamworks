package org.usfirst.frc.team5002.robot.subsystems;

import org.usfirst.frc.team5002.robot.commands.KillDrivetrain;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * SwerveDrive.java -- swerve drive subsystem
 * Fill in the port numbers to be first!
 */
public class SwerveDrive extends Subsystem {
	   
    // The actual swivel motors...
    private CANTalon fl_swiv;
    private CANTalon fr_swiv;
    private CANTalon bl_swiv;
    private CANTalon br_swiv;
    
    // The main drive motors...
    private CANTalon fl_drive;
    private CANTalon fr_drive;
    private CANTalon bl_drive;
    private CANTalon br_drive;
    
    private void configureTalon(CANTalon srx, double p, double i, double d) {
    	srx.changeControlMode(TalonControlMode.Position);
    	srx.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	srx.configEncoderCodesPerRev(7); // the encoders on the swivel motors return 7 ticks per revolution
    	srx.setPID(p, i, d); // TODO: Set these at some point
    	srx.setPosition(0); // Reset to initial position
    }
    
    public SwerveDrive() {
    	/* XXX: Change the ports around as necessary! */
    	
    	/* Init swivel (swerve? motor-turner?) motors */
        fl_swiv = new CANTalon(0);
        fr_swiv = new CANTalon(2);
        bl_swiv = new CANTalon(4);
        br_swiv = new CANTalon(6);
        
        /* Chain all four swivel motors together.
         * We _could_ control them all individually w/ PID
         * (and in fact for one of them we do need to do that)
         * But that's hard and I'm lazy. */
        fr_swiv.changeControlMode(TalonControlMode.Follower);
        bl_swiv.changeControlMode(TalonControlMode.Follower);
        br_swiv.changeControlMode(TalonControlMode.Follower);
        
        fr_swiv.set(fl_swiv.getDeviceID());
        bl_swiv.set(fl_swiv.getDeviceID());
        br_swiv.set(fl_swiv.getDeviceID());
        
        /* Now set controls for the main swivel motor. */
        this.configureTalon(fl_swiv, 0.0, 0.0, 0.0);
        
        /* Init drive motors... */
        fl_drive = new CANTalon(1);
        fr_drive = new CANTalon(3);
        bl_drive = new CANTalon(5);
        br_drive = new CANTalon(7);
        
        /* We chain only the motors on each side together
         * (to allow for turning) */
        bl_drive.changeControlMode(TalonControlMode.Follower);
        br_drive.changeControlMode(TalonControlMode.Follower);
        bl_drive.set(fl_drive.getDeviceID());
        br_drive.set(fr_drive.getDeviceID());
        
        /* Set main controls for driving... */
        fl_drive.changeControlMode(TalonControlMode.PercentVbus);
        fr_drive.changeControlMode(TalonControlMode.PercentVbus);
        fl_drive.set(0.0); /* All drives are initially off */
        fr_drive.set(0.0);
    }
    
    /* Handy functions for getting master motor controllers directly. */
    public CANTalon getLeftController() { return fl_drive; }
    public CANTalon getRightController() { return fr_drive; }
    public CANTalon getSwivelController() { return fl_swiv; }
    
    public void setDriveOutput(double vbus) {
    	fl_drive.set(vbus);
    	fr_drive.set(vbus);
    }
    
    public void setDriveOutput(double vbus_left, double vbus_right) {
    	fl_drive.set(vbus_left);
    	fr_drive.set(vbus_right);
    }
    
    public void setSwervePosition(double pos) {
    	fl_swiv.set(pos);
    }
    
    public void initDefaultCommand() {
    	this.setDefaultCommand(new KillDrivetrain());
    }
}

