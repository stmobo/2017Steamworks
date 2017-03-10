package org.usfirst.frc.team5002.robot;

import org.usfirst.frc.team5002.robot.commands.*;
import org.usfirst.frc.team5002.robot.replay.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.FileOutputStream;
import java.io.FileInputStream;

/**
 * @author elweb @version Last Modified 1/18/17
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick arcadeStick = new Joystick(0); //named Joystick

    // replay control state:
    public boolean currentlyReplaying = false;
    public int currentReplayIndex = 0;                                      // Our index into the state list in currentReplay (zero-based).

    public ControlState lastState = ControlState.getDefaultInstance();      // Previous control state.
    public ControlState currentState = ControlState.getDefaultInstance();   // Current control state.
    public Replay currentReplay;                                            // The replay we're reading from. Will be null if not replaying (i.e. in teleop / no replay loaded).
    public Replay.Builder currentRecording;                                 // The builder we're writing to. Will be null if not recording.

    private boolean toggleA = false;
    private boolean toggleB = false;
    private boolean focEnabled = true;

    /* Keep instances of the Commands here to share between teleop & replay-auto */
    private ClimbUp climbup = new ClimbUp();
    private ClimbDown climbdown = new ClimbDown();
    //private INtaker intaker = new INtaker();
    private OUTtaker outtaker = new OUTtaker();
    private TakeOuter takeouter = new TakeOuter();
    private ReverseInTaker reverseInTaker = new ReverseInTaker();

    /* Ditto with the buttons. */
    private Button A = new JoystickButton(arcadeStick, 1);
    private Button B = new JoystickButton(arcadeStick, 2);
    private Button X = new JoystickButton(arcadeStick, 3);
    private Button Y = new JoystickButton(arcadeStick, 4);
    private Button LB = new JoystickButton(arcadeStick, 5);
    private Button RB = new JoystickButton(arcadeStick, 6);
    private Button home = new JoystickButton(arcadeStick, 7);
    private Button menu = new JoystickButton(arcadeStick, 8);

    private Button toggleFOC = new JoystickButton(arcadeStick, 7);
    private Button activateLowSpeed = new JoystickButton(arcadeStick, 10); // Bumper 1 (left)
    private Button activateHighSpeed = new JoystickButton(arcadeStick, 11); // Bumper 2 (right)

	public OI(){
		Y.whileHeld(new ClimbUp());//turns the climb motor on while Y is being held
		RB.whileHeld(new ClimbDown());//turns launcher motor on when B is pressed once, and off when B is pressed again

		//A.toggleWhenPressed(new INtaker()); //turns the intake motor on when A is pressed once, and off when A is pressed again
		B.toggleWhenPressed(new OUTtaker()); //turns the outake motor on at the same time as the intake motor

		LB.whileHeld(new TakeOuter()); // emergency reverse for outtake motor
		LB.whileHeld(new ReverseInTaker());// emergency reverse for intake motor
	}

    private double getRawForwardAxis() {
        return arcadeStick.getRawAxis(1) * -1.0;//allows the Joystick to command the Robot's forwards and backwards movement
    }

    private double getRawHorizontalAxis() {
        return arcadeStick.getRawAxis(0) * -1.0;//allows the Joystick to command the Robot's side to side movement
    }

    private double getRawTurnAxis() {
        return arcadeStick.getRawAxis(4);//allows the Joystick to command the rotation of the Robot
    }

    public void loadStateFromController() {
        lastState = currentState;
        currentState = ControlState.newBuilder()
            .setForwardAxis(getRawForwardAxis())
            .setHorizontalAxis(getRawHorizontalAxis())
            .setTurnAxis(getRawTurnAxis())
            .setButtonA(A.get())
            .setButtonB(B.get())
            .setButtonX(X.get())
            .setButtonY(Y.get())
            .setButtonLB(LB.get())
            .setButtonRB(RB.get())
            .setButtonHome(home.get())
            .setButtonMenu(menu.get())
            .setToggleFOC(menu.get())
            .setActivateLowSpeed(activateLowSpeed.get())
            .setActivateHighSpeed(activateHighSpeed.get())
            .setPOV(arcadeStick.getPOV())
            .build();


        updateOIState();
    }

    public void loadStateFromReplay() {
        if( currentReplay != null && currentReplayIndex < currentReplay.getStateCount()) {
            lastState = currentState;
            currentState = currentReplay.getState(currentReplayIndex);
            currentReplayIndex += 1;
        } else {
            // If this is the end of the replay, then just go to default values.
            // (These values should effectively stop the robot.)
            currentState = ControlState.getDefaultInstance();
        }

        updateOIState();
    }

    public void saveStateToReplay() {
        if(currentRecording != null) {
            currentRecording.addState(currentState);
        }
    }

    public void saveReplayToFile(String filename) {
        try {
            FileOutputStream ofstream = new FileOutputStream(filename);
            Replay builtReplay = currentRecording.build();
            builtReplay.writeTo(ofstream);

            System.out.println("Wrote " + builtReplay.getStateCount() + " replay frames to " + filename);

            ofstream.flush();
            ofstream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadReplayFromFile(String filename) {
        try {
            FileInputStream ifstream = new FileInputStream(filename);
            currentReplay = Replay.parseFrom(ifstream);

            System.out.println("Read " + currentReplay.getStateCount() + " replay frames from " + filename);
        } catch(Exception e) {
            e.printStackTrace();

            currentReplay = null;
            currentState = ControlState.getDefaultInstance();
        }
    }

    public void updateOIState() {
        /* Trigger button-based commands if necessary: */
        if(currentState.getButtonY()) {
            climbup.start();
        } else {
            climbup.cancel();
        }

        if(currentState.getButtonRB()) {
            climbdown.start();
        } else {
            climbdown.cancel();
        }

        if(currentState.getButtonLB()) {
            takeouter.start();
            reverseInTaker.start();
        } else {
            takeouter.cancel();
            reverseInTaker.cancel();
        }

        /* Keep track of toggle state. */

        /* Check for button state transition:  */
        /* If button A was not pressed last time and it is pressed now... */
        if(!lastState.getButtonA() && currentState.getButtonA()) {
            toggleA = !toggleA; // Invert toggleA.
        }

        /* Ditto for B. */
        if(!lastState.getButtonB() && currentState.getButtonB()) {
            toggleB = !toggleB;
        }

        // and for FOC toggle...
        if(!lastState.getToggleFOC() && currentState.getToggleFOC()) {
            focEnabled = !focEnabled;
        }

        /* Start toggled commands if necessary. */

        if(toggleB) {
            outtaker.start();
        } else {
            outtaker.cancel();
        }
    }

    public boolean isPOVPressed() {
    	int angle = currentState.getPOV();
    	if(angle == -1) {
    		return true;
    	}
    	return false;
    }

    public double getFwdPOV() {
    	int angle = currentState.getPOV();
    	return Math.sin((Math.PI/180.0)*angle);
    }

    public double getStrPOV() {
    	int angle = currentState.getPOV();
    	return Math.cos((Math.PI/180.0)*angle);
    }

    /* set multipliers for teleop drive speed outputs */
    public double getDriveSpeedCoefficient() {
        if(activateLowSpeed.get()) {
            return 0.25;
        } else if(activateHighSpeed.get()) {
            return 1.0;
        } else if(isPOVPressed()) {
            return 0.25;
        } else {
            return 0.50;
        }
    }

    public double[] focRotate(double[] coords) {
        double hdg = Robot.navx.getAngle();
        double out[] = {0, 0};

        out[0] = (coords[0] * Math.cos(hdg*Math.PI/180.0)) + (coords[1] * -Math.sin(hdg*Math.PI/180.0));
        out[1] = (coords[0] * Math.sin(hdg*Math.PI/180.0)) + (coords[1] * Math.cos(hdg*Math.PI/180.0));

        return out;
    }

    /* Return magnitude of main driving control stick / inputs.
     * Should be equivalent to Math.hypot(getForwardAxis(), getHorizontalAxis()). */
    public double getDriveMagnitude() {
        return Math.hypot(currentState.getForwardAxis(), currentState.getHorizontalAxis());
    }

	public double getForwardAxis() {
        if(focEnabled && Robot.navx != null) {
            // Zero degrees = towards opposing side
            double x = currentState.getForwardAxis();
            double y = currentState.getHorizontalAxis();
            double hdg = Robot.navx.getAngle();

            // X-coordinate -> CW alias rotation (left-handed)
            return (x * Math.cos(hdg*Math.PI/180.0)) + (y * -Math.sin(hdg*Math.PI/180.0));
        } else {
            return currentState.getForwardAxis();  // (axis 1 = left-hand Y axis) allows the Joystick to command the Robot's forwards and backwards movement
        }
	}

	public double getHorizontalAxis() {
        if(focEnabled && Robot.navx != null) {
            // Right = +Y
            double x = currentState.getForwardAxis();
            double y = currentState.getHorizontalAxis();
            double hdg = Robot.navx.getAngle();

            // Y-coordinate, CW alias rotation w/ left-handed coordinate system
            return (x * Math.sin(hdg*Math.PI/180.0)) + (y * Math.cos(hdg*Math.PI/180.0));
        } else {
            return currentState.getHorizontalAxis(); // (axis 0 = left-hand X axis) allows the Joystick to command the Robot's side to side movement
        }
	}

	public double getTurnAxis(){
        return currentState.getTurnAxis();
	}

	public void updateSD(){
		Robot.drivetrain.updateSD();//sends all the data from SwerveDrive subsystem to the SmartDashboard
        SmartDashboard.putBoolean("Intake Switch", Robot.limSwitch.get());
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
