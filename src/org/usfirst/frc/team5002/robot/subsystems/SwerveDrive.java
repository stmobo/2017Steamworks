package org.usfirst.frc.team5002.robot.subsystems;

import org.usfirst.frc.team5002.robot.RobotMap;
import org.usfirst.frc.team5002.robot.commands.KillDrivetrain;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    	srx.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
    	srx.setPID(p, i, d); // TODO: Set these at some point
    	srx.setPosition(0); // Reset to initial position
    }

    private void configureDriveMotor(CANTalon srx) {
    	srx.changeControlMode(TalonControlMode.PercentVbus);
    	srx.set(0); // Reset to initial position
    }

    public SwerveDrive() {
    	/* Init swivel (swerve? motor-turner?) motors */
        fl_swiv = new CANTalon(RobotMap.fl_steer);
        fr_swiv = new CANTalon(RobotMap.fr_steer);
        bl_swiv = new CANTalon(RobotMap.bl_steer);
        br_swiv = new CANTalon(RobotMap.br_steer);

        this.configureSteerMotor(fl_swiv, 0.0, 0.0, 0.0);
        this.configureSteerMotor(fr_swiv, 0.0, 0.0, 0.0);
        this.configureSteerMotor(bl_swiv, 0.0, 0.0, 0.0);
        this.configureSteerMotor(br_swiv, 0.0, 0.0, 0.0);

        /* Init drive motors... */
        fl_drive = new CANTalon(RobotMap.fl_drive);
        fr_drive = new CANTalon(RobotMap.fr_drive);
        bl_drive = new CANTalon(RobotMap.bl_drive);
        br_drive = new CANTalon(RobotMap.br_drive);

        /* Set main controls for driving... */
        this.configureDriveMotor(fl_drive);
        this.configureDriveMotor(fr_drive);
        this.configureDriveMotor(bl_drive);
        this.configureDriveMotor(br_drive);
    }
    
    public void writePIDErrorToDashboard() {
    	SmartDashboard.putNumber("FrontLeft-Steer-Error", fl_swiv.getClosedLoopError());
    	SmartDashboard.putNumber("FrontRight-Steer-Error", fr_swiv.getClosedLoopError());
    	SmartDashboard.putNumber("BackLeft-Steer-Error", bl_swiv.getClosedLoopError());
    	SmartDashboard.putNumber("BackRight-Steer-Error", br_swiv.getClosedLoopError());
    }

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
