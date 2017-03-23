package org.usfirst.frc.team5002.robot;

import org.usfirst.frc.team5002.robot.commands.ClimbDown;
import org.usfirst.frc.team5002.robot.commands.ClimbUp;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.*;
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

    private Button A;
    private Button B;
    private Button X;
    private Button Y;
	private Button LB;
    private Button RB;
	private Button home;
	private Button menu;

    public boolean focEnabled = false;

    private final Timer ctrlShakeTimer = new Timer();
    private boolean ctrlShaking = false;

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

		Y.toggleWhenPressed(new ClimbUp());//turns the climb motor on while Y is being held
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
        	Robot.sensors.navx.zeroYaw();
        }

        if(ctrlShaking) {
            if(ctrlShakeTimer.hasPeriodPassed(1.0)) {
                ctrlShaking = false;
                arcadeStick.setRumble(RumbleType.kLeftRumble, 0.0);
                arcadeStick.setRumble(RumbleType.kRightRumble, 0.0);
                ctrlShakeTimer.stop();
            }
        }
    }

    public void shakeController() {
        ctrlShakeTimer.reset();
        ctrlShakeTimer.start();

        ctrlShaking = true;

        arcadeStick.setRumble(RumbleType.kLeftRumble, 1.0);
        arcadeStick.setRumble(RumbleType.kRightRumble, 1.0);
    }

    public boolean autoAlignButtonActivated() {
        return X.get();
    }

    public boolean reverseButtonActivated() {
    	return LB.get();
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
        if(focEnabled && Robot.sensors.navx != null) {
            // Zero degrees = towards opposing side
            double x = arcadeStick.getRawAxis(1) * -1.0;
            double y = arcadeStick.getRawAxis(0) * -1.0;
            double hdg = Robot.sensors.getRobotHeading();

            // X-coordinate -> CW alias rotation (left-handed)
            return (x * Math.cos(hdg*Math.PI/180.0)) + (y * -Math.sin(hdg*Math.PI/180.0));
        } else {
            return arcadeStick.getRawAxis(1) * -1.0;  // (axis 1 = left-hand Y axis) allows the Joystick to command the Robot's forwards and backwards movement
        }
	}

	public double getHorizontalAxis(){
        if(focEnabled && Robot.sensors.navx != null) {
            // Right = +Y
            double x = arcadeStick.getRawAxis(1) * -1.0;
            double y = arcadeStick.getRawAxis(0) * -1.0;
            double hdg = Robot.sensors.getRobotHeading();

            // Y-coordinate, CW alias rotation w/ left-handed coordinate system
            return (x * Math.sin(hdg*Math.PI/180.0)) + (y * Math.cos(hdg*Math.PI/180.0));
        } else {
            return arcadeStick.getRawAxis(0) * -1.0; // (axis 0 = left-hand X axis) allows the Joystick to command the Robot's side to side movement
        }
	}

	public double getTurnAxis(){
    	return arcadeStick.getRawAxis(3) - arcadeStick.getRawAxis(2);
	}

	public void UpdateSD(){
		Robot.drivetrain.updateSD();//sends all the data from SwerveDrive subsystem to the SmartDashboard
		//Robot.viewport.updateSD();
        Robot.sensors.updateSD();

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

        SmartDashboard.putNumber("POV", arcadeStick.getPOV(0));
	}
}
