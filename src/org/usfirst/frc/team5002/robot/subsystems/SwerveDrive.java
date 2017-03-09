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
	private CANTalon fl_steer;
	private CANTalon fr_steer;
	private CANTalon bl_steer;
	private CANTalon br_steer;

    // The main drive motors...
    private CANTalon fl_drive;
    private CANTalon fr_drive;
    private CANTalon bl_drive;
    private CANTalon br_drive;

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

    /* value ordering: FrontLeft, FrontRight, BackLeft, BackRight
     * Yes, for all 4 arrays (or however many there are.)
     */
    private double[] steer_offsets = { 135.0, 617.0, 940.0, 1013.0 };
    private double[] maxEncoderOutput = {1024.0, 1024.0, 1024.0, 1024.0};
    private double[] minEncoderOutput = {0.0, 0.0, 0.0, 0.0};
    boolean driveReversalStatus[] = {false, false, false, false};
    boolean driveReversalConst[] = {true, false, true, false};

    public enum ModulePosition {
    	FL,    ///< Front-left
    	FR,    ///< Front-right
    	BL,    ///< Back-left
    	BR     ///< Back-right
    };

    /**
     * Gets the maximum possible value returned by the encoder hardware.
     * For use in software compensation.
     *
     * @param pos position of the steer module to inspect.
     */
    private double getSteerHack(ModulePosition pos) {
    	switch(pos) {
    	case FL:
    		return maxEncoderOutput[0];
    	case FR:
    		return maxEncoderOutput[1];
    	case BL:
    		return maxEncoderOutput[2];
    	case BR:
    		return maxEncoderOutput[3];
    	}

    	return 1024;
    }

    /**
     * Gets the minimum possible value returned by the encoder hardware.
     * For use in software compensation.
     *
     * @param pos position of the steer module to inspect.
     */
    private double getMinSteerHack(ModulePosition pos) {
        switch(pos) {
        case FL:
    		return minEncoderOutput[0];
    	case FR:
    		return minEncoderOutput[1];
    	case BL:
    		return minEncoderOutput[2];
    	case BR:
    		return minEncoderOutput[3];
        }

        return 0;
    }

    /**
     * Gets the relative value of "forward" with respect to a steer encoder.
     * Driving a steer motor to this position will induce forward motion when
     * driven with positive voltage.
     *
     * @param pos position of the steer module to inspect.
     */
    private double getSteerOffset(ModulePosition pos) {
    	switch(pos) {
    	case FL:
    		return steer_offsets[0];
    	case FR:
    		return steer_offsets[1];
    	case BL:
    		return steer_offsets[2];
    	case BR:
    		return steer_offsets[3];
    	}

    	return 0;
    }

    /**
     * Gets whether a given steer module's drive motor should be reversed by default.
     *
     * @param pos position of the steer module to inspect.
     */
    private boolean getDriveReverse(ModulePosition pos) {
    	switch(pos) {
    	case FL:
    		return driveReversalStatus[0];
    	case FR:
    		return driveReversalStatus[1];
    	case BL:
    		return driveReversalStatus[2];
    	case BR:
    		return driveReversalStatus[3];
    	}

    	return false;
    }
    
    /**
     * Gets whether a given steer module's drive motor should be reversed by default.
     *
     * @param pos position of the steer module to inspect.
     */
    private boolean getDriveReverseConst(ModulePosition pos) {
    	switch(pos) {
    	case FL:
    		return driveReversalConst[0];
    	case FR:
    		return driveReversalConst[1];
    	case BL:
    		return driveReversalConst[2];
    	case BR:
    		return driveReversalConst[3];
    	}

    	return false;
    }
    
    /**
     * Gets whether a given steer module's drive motor should be reversed by default.
     *
     * @param pos position of the steer module to inspect.
     */
    private void setDriveReverse(ModulePosition pos, boolean stat) {
    	switch(pos) {
    	case FL:
    		driveReversalStatus[0] = stat;
    	case FR:
    		driveReversalStatus[1] = stat;
    	case BL:
    		driveReversalStatus[2] = stat;
    	case BR:
    		driveReversalStatus[3] = stat;
    	}
    }

    /**
     * Maps ModulePositions to their corresponding steer motor controllers.
     *
     * @param pos position of the steer module to inspect.
     */
    private CANTalon getSteerController(ModulePosition pos) {
    	switch(pos) {
    	case FL:
    		return fl_steer;
    	case FR:
    		return fr_steer;
    	case BL:
    		return bl_steer;
    	case BR:
    		return br_steer;
    	}

    	return null;
    }

    /**
     * Maps ModulePositions to their corresponding drive motor controllers.
     *
     * @param pos position of the steer module to inspect.
     */
    private CANTalon getDriveController(ModulePosition pos) {
    	switch(pos) {
    	case FL:
    		return fl_drive;
    	case FR:
    		return fr_drive;
    	case BL:
    		return bl_drive;
    	case BR:
    		return br_drive;
    	}

    	return null;
    }

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

        /* Drive motor config is left to teleop/auto commands */

    }

    /**
     * Performs steer motor intialization.
     *
     * @param srx reference to a steer motor controller
     * @param reverse reversal status of given motor controller (true = reversed motor outputs)
     */
    private void configureSteerMotor(CANTalon srx, boolean reverse) {
    	srx.changeControlMode(TalonControlMode.Position);
    	srx.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
    	//srx.configPotentiometerTurns(1);
		srx.setProfile(0);
        //srx.setPosition(0); // clear top bits of encoder position
		//srx.set(0.5); // reset to midpoint
    }

    /**
     * Configures a swerve module for direct control (Vbus mode on drive)
     *
     * @param pos Steer module to reconfigure
     */
    private void configureDriveMotorTeleop(ModulePosition pos) {
    	CANTalon srx = getDriveController(pos);
    	srx.reverseOutput(getDriveReverseConst(pos));
    	srx.changeControlMode(TalonControlMode.PercentVbus);
    	srx.set(0); // Reset to stopped
    }

    /**
     * Configures a swerve module for autonomous control (Closed-loop on drive)
     *
     * @param pos Steer module to reconfigure
     */
    private void configureDriveMotorAutonomous(ModulePosition pos) {
    	CANTalon srx = getDriveController(pos);

    	srx.changeControlMode(TalonControlMode.Position);
    	//control mode is in position
    	srx.reverseOutput(getDriveReverseConst(pos));
    	//making the sensor read positively
    	srx.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	//making it a quad encoder
    	srx.configEncoderCodesPerRev(40);
    }

    /**
     * Sets the given swerve module's drive target.
     *
     * As this effectively acts as a wrapper around CANTalon.set(), the
     * semantics of this call vary on the configured drive mode, which can
     * be either PercentVBus (teleop) or ClosedLoopPosition.
     *
     * @param pos Steer module to drive
     * @param out Output target to set to
     */
    public void setDriveOutput(ModulePosition pos, double out) {
    	CANTalon drive = getDriveController(pos);
    	drive.set(out * (getDriveReverseConst(pos) ? -1 : 1) * (getDriveReverse(pos) ? -1 : 1));
    }

    /**
     * Sets the given swerve module's steering position.
     *
     * This call will command the appropriate steer motor to move to a given
     * target in degrees (where 0 degrees equates to "straight forward").
     *
     * Unlike setDriveOutput(), the semantics of this call are the same between
     * teleop and auto.
     */
    public void setSteerDegrees(ModulePosition pos, double degrees) {
    	CANTalon steer = getSteerController(pos);
    	CANTalon drive = getDriveController(pos);

    	if(degrees >= 180.0) {
    		degrees -= 180.0;
    		setDriveReverse(pos, true);
    	} else {
    		setDriveReverse(pos, false);
    	}

    	double nativePos = degrees * ((getSteerHack(pos) - getMinSteerHack(pos)) / 360.0);
    	nativePos += getSteerOffset(pos);
        nativePos += getMinSteerHack(pos);

    	steer.set(nativePos);
    }

    /**
     * Reconfigure all drive motors for direct (teleop) control.
     */
    public void setDriveTeleop() {
    	configureDriveMotorTeleop(ModulePosition.FL);
    	configureDriveMotorTeleop(ModulePosition.FR);
    	configureDriveMotorTeleop(ModulePosition.BL);
    	configureDriveMotorTeleop(ModulePosition.BR);
    }

    /**
     * Reconfigure all drive motors for autonomous (closed-loop) control.
     */
    public void setDriveAuto() {
    	configureDriveMotorAutonomous(ModulePosition.FL);
    	configureDriveMotorAutonomous(ModulePosition.FR);
    	configureDriveMotorAutonomous(ModulePosition.BL);
    	configureDriveMotorAutonomous(ModulePosition.BR);
    }

    public void initDefaultCommand() {
    	this.setDefaultCommand(new KillDrivetrain());
    }

    /**
     * Outputs debugging data to the SmartDashboard for one swerve module.
     *
     * Each output key is suffixed with a given string for identification.
     * @param pos swerve module to inspect
     * @param suffix string identifier suffix
     */
    public void UpdateSDSingle(ModulePosition pos, String suffix) {
        CANTalon steer = getSteerController(pos);
    	CANTalon drive = getDriveController(pos);

    	SmartDashboard.putNumber("SteerErr-"+suffix, steer.getClosedLoopError());
    	SmartDashboard.putNumber("SteerPos-"+suffix, steer.getPosition());
    	SmartDashboard.putNumber("SteerADC-"+suffix, steer.getAnalogInRaw());

        SmartDashboard.putNumber("DriveSpeed-"+suffix, drive.getSpeed());
        SmartDashboard.putNumber("DrivePos-"+suffix, drive.getPosition());
        SmartDashboard.putNumber("DriveErr-"+suffix, drive.getClosedLoopError());
    }

    /*
     * sends data to the SmartDashboard
     */
    public void updateSD(){
    	UpdateSDSingle(ModulePosition.FL, "FL");
    	UpdateSDSingle(ModulePosition.FR, "FR");
    	UpdateSDSingle(ModulePosition.BL, "BL");
    	UpdateSDSingle(ModulePosition.BR, "BR");
    }
}
