package org.usfirst.frc.team5002.robot;

import org.usfirst.frc.team5002.robot.commands.ClimbDown;
import org.usfirst.frc.team5002.robot.commands.ClimbUp;
import org.usfirst.frc.team5002.robot.commands.AutoGear;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * @author elweb @version Last Modified 1/18/17
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick arcadeStick; //named Joystick

    private Button activateLowSpeed;
    private Button activateHighSpeed;
    private Button toggleFOC;
    private Button resetHdg;
    private Button activateAutoGear;

    private Button A;
    private Button B;
    private Button X;
    private Button Y;
    private Button RB;
	private Button LB;
	private Button home;
	private Button menu;

    boolean focEnabled = false;

	public OI(){
		arcadeStick = new Joystick(0); //gave Joystick a job

		A = new JoystickButton(arcadeStick, 1);
		B = new JoystickButton(arcadeStick, 2);
		X = new JoystickButton(arcadeStick, 3);
		Y = new JoystickButton(arcadeStick, 4);
		RB = new JoystickButton(arcadeStick, 6);
		home = new JoystickButton(arcadeStick, 7);
		menu = new JoystickButton(arcadeStick, 8);
		LB  = new JoystickButton(arcadeStick, 5);

        activateLowSpeed = new JoystickButton(arcadeStick, 9); // Bumper 1 (left)
        activateHighSpeed = new JoystickButton(arcadeStick, 10); // Bumper 2 (right)
        toggleFOC = home;
        resetHdg = menu;
        activateAutoGear = X;

		Y.toggleWhenPressed(new ClimbUp());//turns the climb motor on while Y is being held
        activateAutoGear.whenPressed(new AutoGear());
        if(!DriverStation.getInstance().isFMSAttached()) {
    		RB.whileHeld(new ClimbDown());
        }
	}

    // For toggle buttons that don't warrant their own commands.
    boolean focDebounce = false;
    public void updateOIState() {
        if(toggleFOC.get() && !focDebounce) {
            focEnabled = !focEnabled;
            focDebounce = true;
        } else {
            focDebounce = false;
        }

        if(resetHdg.get()) {
        	Robot.navx.zeroYaw();
        }

    }

    public boolean intakeButtonActivated() {
    	return A.get();
    }

    public boolean reverseButtonActivated() {
    	return LB.get();
    }

    public boolean autoGearActivated() {
        return activateAutoGear.get();
    }

    public boolean viewForwardButtonActivated() {
        return A.get();
    }

    public boolean viewBackwardButtonActivated() {
        return B.get();
    }

    public boolean isPOVPressed() {
    	int angle = arcadeStick.getPOV(0);
    	if(angle == -1) {
    		return false;
    	}
    	return true;
    }

    public double getFwdPOV() {
    	int angle = arcadeStick.getPOV(0);
    	return Math.cos(Math.toRadians((double)angle));
    }

    public double getStrPOV() {
    	int angle = arcadeStick.getPOV(0);
    	return Math.sin(Math.toRadians((double)angle));
    }

    /* set multipliers for teleop drive speed outputs */
    public double getDriveSpeedCoefficient() {
        if(activateLowSpeed.get()) {
            return 0.25 * arcadeStick.getRawAxis(4) * -1.0;
        } else if(activateHighSpeed.get()) {
            return 1.0 * arcadeStick.getRawAxis(4) * -1.0;
        } else if(isPOVPressed()) {
        	return 0.25;
        } else {
            return 0.50 * arcadeStick.getRawAxis(4) * -1.0;
        }
    }

    /* Return magnitude of main driving control stick / inputs.
     * Should be equivalent to Math.hypot(getForwardAxis(), getHorizontalAxis()). */
    public double getDriveMagnitude() {
        return arcadeStick.getMagnitude();
    }

	public double getForwardAxis() {
        if(focEnabled && Robot.navx != null) {
            // Zero degrees = towards opposing side
            double x = arcadeStick.getRawAxis(1) * -1.0;
            double y = arcadeStick.getRawAxis(0) * -1.0;
            double hdg = Robot.getRobotHeading();

            // X-coordinate -> CW alias rotation (left-handed)
            return (x * Math.cos(hdg*Math.PI/180.0)) + (y * -Math.sin(hdg*Math.PI/180.0));
        } else {
            return arcadeStick.getRawAxis(1) * -1.0;  // (axis 1 = left-hand Y axis) allows the Joystick to command the Robot's forwards and backwards movement
        }
	}

	public double getHorizontalAxis(){
        if(focEnabled && Robot.navx != null) {
            // Right = +Y
            double x = arcadeStick.getRawAxis(1) * -1.0;
            double y = arcadeStick.getRawAxis(0) * -1.0;
            double hdg = Robot.getRobotHeading();

            // Y-coordinate, CW alias rotation w/ left-handed coordinate system
            return (x * Math.sin(hdg*Math.PI/180.0)) + (y * Math.cos(hdg*Math.PI/180.0));
        } else {
            return arcadeStick.getRawAxis(0) * -1.0; // (axis 0 = left-hand X axis) allows the Joystick to command the Robot's side to side movement
        }
	}

	public double getTurnAxis(){
    	return arcadeStick.getRawAxis(3) - arcadeStick.getRawAxis(2);
	}

    public void vibrate() {
        arcadeStick.setRumble(RumbleType.kLeftRumble, 1.0);
        arcadeStick.setRumble(RumbleType.kRightRumble, 1.0);
    }

    public void stopVibrate() {
        arcadeStick.setRumble(RumbleType.kLeftRumble, 0);
        arcadeStick.setRumble(RumbleType.kRightRumble, 0);
    }

	public void UpdateSD(){
		Robot.drivetrain.updateSD();//sends all the data from SwerveDrive subsystem to the SmartDashboard
		Robot.viewport.updateSD();
		if(!DriverStation.getInstance().isDisabled()) {
			if(DriverStation.getInstance().isAutonomous()) {
				SmartDashboard.putNumber("Match Time", (int)(15.0 - Timer.getMatchTime()));
			} else {
				SmartDashboard.putNumber("Match Time", (int)(150.0 - Timer.getMatchTime()));
			}

			if((int)(150.0 - Timer.getMatchTime()) <= 40.0) {
				SmartDashboard.putBoolean("40-Second Watch", true);
			} else {
				SmartDashboard.putBoolean("40-Second Watch", false);
			}
		} else {
			SmartDashboard.putNumber("Match Time", (int)0);
			SmartDashboard.putBoolean("40-Second Watch", false);
		}

		SmartDashboard.putNumber("Start Yaw", Robot.startYaw);
		SmartDashboard.putNumber("POV", arcadeStick.getPOV(0));
		if(Robot.navx != null) {
			SmartDashboard.putBoolean("NavX Present", true);
			SmartDashboard.putBoolean("Calibrating", Robot.navx.isCalibrating());
			SmartDashboard.putBoolean("Connected", Robot.navx.isConnected());

			SmartDashboard.putNumber("Heading", Robot.navx.getAngle());
			SmartDashboard.putNumber("Compass", Robot.navx.getCompassHeading());
			SmartDashboard.putNumber("Yaw", Robot.navx.getYaw());
			SmartDashboard.putNumber("Fused", Robot.navx.getFusedHeading());
			if(focEnabled) {
				SmartDashboard.putString("Control Mode", "Field-Oriented");
			} else {
				SmartDashboard.putString("Control Mode", "Robot-Oriented (FOC available)");
			}
		} else {
			SmartDashboard.putBoolean("NavX Present", false);
			SmartDashboard.putString("Control Mode", "Robot-Oriented (FOC unavailable)");
		}
	}
}
