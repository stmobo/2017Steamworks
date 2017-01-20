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
 * @author elweb @version Last Modified 1/18/17
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
    	srx.set(0); // Reset to initial position
    }
    
    public enum ModulePosition {
    	FRONT_LEFT,
    	FRONT_RIGHT,
    	BACK_LEFT,
    	BACK_RIGHT
    }
    
    /* Testing only! */
    public void reconfigureSteer_vbus(ModulePosition mod) {
    	switch(mod) {
    	case FRONT_LEFT:
    		configureDriveMotor(fl_swiv);
    		return;
    	case FRONT_RIGHT:
    		configureDriveMotor(fr_swiv);
    		return;
    	case BACK_LEFT:
    		configureDriveMotor(bl_swiv);
    		return;
    	case BACK_RIGHT:
    		configureDriveMotor(br_swiv);
    		return;
    	}
    }
    
    public void reconfigurePID(ModulePosition mod, double p, double i, double d) {
    	switch(mod) {
    	case FRONT_LEFT:
    		configureSteerMotor(fl_swiv, p, i, d);
    		return;
    	case FRONT_RIGHT:
    		configureSteerMotor(fr_swiv, p, i, d);
    		return;
    	case BACK_LEFT:
    		configureSteerMotor(bl_swiv, p, i, d);
    		return;
    	case BACK_RIGHT:
    		configureSteerMotor(br_swiv, p, i, d);
    		return;
    	}
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
    
    public void setSwervePosition(ModulePosition mod, double pos) {
    	switch(mod) {
    	case FRONT_LEFT:
    		fl_swiv.set(pos);
    		return;
    	case FRONT_RIGHT:
    		fr_swiv.set(pos);
    		return;
    	case BACK_LEFT:
    		bl_swiv.set(pos);
    		return;
    	case BACK_RIGHT:
    		br_swiv.set(pos);
    		return;
    	}
    }

    public void initDefaultCommand() {
    	this.setDefaultCommand(new KillDrivetrain());
    }
    
    /*
     * sends data to the SmartDashboard
     */
    public void UpdateSD(){
    	SmartDashboard.putNumber("Swiv get", fl_swiv.get());
		SmartDashboard.putNumber("Swiv get", fr_swiv.get());
		SmartDashboard.putNumber("Swiv get", bl_swiv.get());
		SmartDashboard.putNumber("Swiv get", br_swiv.get());
		//necessary to begin collecting data from the motors that control the rotation of the wheels
		SmartDashboard.putNumber("Drive get", fl_drive.get());
		SmartDashboard.putNumber("Drive get", fr_drive.get());
		SmartDashboard.putNumber("Drive get", bl_drive.get());
		SmartDashboard.putNumber("Drive get", br_drive.get());
		//necessary to begin collecting data from the motors that drive the wheels
		SmartDashboard.putNumber("Drive BusVoltage", fl_drive.getBusVoltage());
		SmartDashboard.putNumber("Drive BusVoltage", fr_drive.getBusVoltage());
		SmartDashboard.putNumber("Drive BusVoltage", bl_drive.getBusVoltage());
		SmartDashboard.putNumber("Drive BusVoltage", br_drive.getBusVoltage());
		//Measures the voltage distributed to the motors
		SmartDashboard.putNumber("Drive ClosedLoopError", fl_drive.getClosedLoopError());
		SmartDashboard.putNumber("Drive ClosedLoopError", fr_drive.getClosedLoopError());
		SmartDashboard.putNumber("Drive ClosedLoopError", bl_drive.getClosedLoopError());
		SmartDashboard.putNumber("Drive ClosedLoopError", br_drive.getClosedLoopError());
		//Displays an error if the drive loop is broken
		SmartDashboard.putNumber("Drive OutputVoltage", fl_drive.getOutputVoltage());
		SmartDashboard.putNumber("Drive OutputVoltage", fr_drive.getOutputVoltage());
		SmartDashboard.putNumber("Drive OutputVoltage", bl_drive.getOutputVoltage());
		SmartDashboard.putNumber("Drive OutputVoltage", br_drive.getOutputVoltage());
		//measure the actual voltage the motor receives
		SmartDashboard.putNumber("Drive OutputCurrent", fl_drive.getOutputCurrent());
		SmartDashboard.putNumber("Drive OutputCurrent", fr_drive.getOutputCurrent());
		SmartDashboard.putNumber("Drive OutputCurrent", bl_drive.getOutputCurrent());
		SmartDashboard.putNumber("Drive OutputCurrent", br_drive.getOutputCurrent());
		//measures the amperage the motor receives (so we don't burn through the insulation on the wires)
		SmartDashboard.putNumber("Swiv OutputVoltage", fl_swiv.getOutputVoltage());
		SmartDashboard.putNumber("Swiv OutputVoltage", fr_swiv.getOutputVoltage());
		SmartDashboard.putNumber("Swiv OutputVoltage", bl_swiv.getOutputVoltage());
		SmartDashboard.putNumber("Swiv OutputVoltage", br_swiv.getOutputVoltage());
		//measures the voltage the Swivel motors receive
		SmartDashboard.putNumber("Swiv OutputCurrent", fl_swiv.getOutputCurrent());
		SmartDashboard.putNumber("Swiv OutputCurrent", fr_swiv.getOutputCurrent());
		SmartDashboard.putNumber("Swiv OutputCurrent", bl_swiv.getOutputCurrent());
		SmartDashboard.putNumber("Swiv OutputCurrent", br_swiv.getOutputCurrent());
		//measures the amperage the Swivel motors receive
    	
    }
}
