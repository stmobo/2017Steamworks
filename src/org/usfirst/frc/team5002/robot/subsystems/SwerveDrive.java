package org.usfirst.frc.team5002.robot.subsystems;

import org.usfirst.frc.team5002.robot.commands.KillDrivetrain;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
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

    private void configureSteerMotor(CANTalon srx, double p, double i, double d) {
    	srx.changeControlMode(TalonControlMode.Position);
    	srx.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	srx.configEncoderCodesPerRev(7); // the encoders on the swivel motors return 7 ticks per revolution
    	srx.setPID(p, i, d); // TODO: Set these at some point
    	srx.setPosition(0); // Reset to initial position
    }

    private void configureDriveMotor(CANTalon srx) {
    	srx.changeControlMode(TalonControlMode.PercentVbus);
    	srx.set(0); // Reset to initial position
    }

    public SwerveDrive() {
    	/* XXX: Change the ports around as necessary! */

    	/* Init swivel (swerve? motor-turner?) motors */
        fl_swiv = new CANTalon(RobotDrive.fl_steer);
        fr_swiv = new CANTalon(RobotDrive.fr_steer);
        bl_swiv = new CANTalon(RobotDrive.bl_steer);
        br_swiv = new CANTalon(RobotDrive.br_steer);

        this.configureSteerMotor(fl_swiv, 0.0, 0.0, 0.0);
        this.configureSteerMotor(fr_swiv, 0.0, 0.0, 0.0);
        this.configureSteerMotor(bl_swiv, 0.0, 0.0, 0.0);
        this.configureSteerMotor(br_swiv, 0.0, 0.0, 0.0);

        /* Init drive motors... */
        fl_drive = new CANTalon(RobotDrive.fl_drive);
        fr_drive = new CANTalon(RobotDrive.fr_drive);
        bl_drive = new CANTalon(RobotDrive.bl_drive);
        br_drive = new CANTalon(RobotDrive.br_drive);

        /* We chain only the motors on each side together
         * (to allow for turning) */

        /* Set main controls for driving... */
        this.configureDriveMotor(fl_drive);
        this.configureDriveMotor(fr_drive);
        this.configureDriveMotor(bl_drive);
        this.configureDriveMotor(br_drive);
    }

    /* Handy functions for getting master motor controllers directly. */
    public CANTalon getLeftController() { return fl_drive; }
    public CANTalon getRightController() { return fr_drive; }
    public CANTalon getSwivelController() { return fl_swiv; }

    public void setDriveOutput(double vbus_fl, double vbus_fr, double vbus_bl, double vbus_br) {
    	fl_drive.set(vbus_fl);
    	fr_drive.set(vbus_fr);
    	bl_drive.set(vbus_bl);
    	br_drive.set(vbus_br);
    }

    public void setSwervePosition(double pos_fl, double pos_fr, double pos_bl, double pos_br) {
    	fl_swiv.set(pos_fl);
    	fr_swiv.set(pos_fr);
    	br_swiv.set(pos_bl);
    	br_swiv.set(pos_br);
    }

    public void initDefaultCommand() {
    	this.setDefaultCommand(new KillDrivetrain());
    }
}
