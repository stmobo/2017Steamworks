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
 * @author stmobo @version Last Modified 1/18/17
 * SwerveDrive.java -- swerve drive subsystem
 */
public class SwerveDrive extends Subsystem {
    // The actual steer motors...
    public CANTalon fl_steer;
    public CANTalon fr_steer;
    public CANTalon bl_steer;
    public CANTalon br_steer;

    // The main drive motors...
    public CANTalon fl_drive;
    public CANTalon fr_drive;
    public CANTalon bl_drive;
    public CANTalon br_drive;

    /* Steering zero positions:
     *  FL: -0.54,
     *  FR: -0.04,
     *  BL: -0.35,
     *  BR: -0.33
     */
    
    /*
     * Control system values:
     * (P/I/D/FeedFwd/IZone/RampRate)
     * FL: 28 / 0.00125 / 300 / 0 / 512  / 15
     * FR: 12 / 0.0015  / 300 / 0 / 1023 / 25
     * BL: 24 / 0.0015  / 720 / 0 / 2048 / 10
     * BR: 24 / 0.0015  / 350 / 0 / 0    / 15
     */
    
    public static double[] steer_offsets = { -289.0, -49.0, -555.0, 27.0 };
    
	public SwerveDrive() {
    	/* Init steer (swerve? motor-turner?) motors */
        fl_steer = new CANTalon(RobotMap.fl_steer);
        fr_steer = new CANTalon(RobotMap.fr_steer);
        bl_steer = new CANTalon(RobotMap.bl_steer);
        br_steer = new CANTalon(RobotMap.br_steer);

		this.configureSteerMotor(fr_steer, false);
        this.configureSteerMotor(fl_steer, false);
        this.configureSteerMotor(bl_steer, false);
        this.configureSteerMotor(br_steer, false);

        /* Init drive motors... */
        fl_drive = new CANTalon(RobotMap.fl_drive);
        fr_drive = new CANTalon(RobotMap.fr_drive);
        bl_drive = new CANTalon(RobotMap.bl_drive);
        br_drive = new CANTalon(RobotMap.br_drive);

        /* Set main controls for driving... */
        this.configureDriveMotor(fl_drive, false);
        this.configureDriveMotor(fr_drive, false);
        this.configureDriveMotor(bl_drive, false);
        this.configureDriveMotor(br_drive, false);
    }

    public void configureSteerMotor(CANTalon srx, boolean reverse) {
    	srx.changeControlMode(TalonControlMode.Position);
    	//control mode is in position
    	srx.reverseSensor(true);
    	//making the sensor read positively
    	srx.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	//making it a quad encoder
    	srx.configEncoderCodesPerRev(40);
    	//setting the CPR to 360, good easy number
    	//srx.configPotentiometerTurns(1);
		srx.setProfile(0);
        //srx.setPosition(0);
		srx.reverseOutput(reverse);
		srx.set(0.5); // reset to midpoint
		
    }

    public void configureDriveMotor(CANTalon srx, boolean reverse) {
    	srx.changeControlMode(TalonControlMode.PercentVbus);
    	srx.set(0); // Reset to stopped
		srx.reverseOutput(reverse);
    }

    public void initDefaultCommand() {
    	this.setDefaultCommand(new KillDrivetrain());
    }
    
    public void UpdateSDSingle(CANTalon srx) {
    	SmartDashboard.putNumber("Error", srx.getClosedLoopError());
    	SmartDashboard.putNumber("Pos", srx.getPosition());
    	SmartDashboard.putNumber("ADC", srx.getAnalogInRaw());
    }

    /*
     * sends data to the SmartDashboard
     */
    public void UpdateSD(){
    	SmartDashboard.putNumber("ADC-FR", fr_steer.getAnalogInRaw());
    	SmartDashboard.putNumber("ADC-FL", fl_steer.getAnalogInRaw());
    	SmartDashboard.putNumber("ADC-BL", bl_steer.getAnalogInRaw());
    	SmartDashboard.putNumber("ADC-BR", br_steer.getAnalogInRaw());
    }
}
