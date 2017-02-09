package org.usfirst.frc.team5002.robot;

import org.usfirst.frc.team5002.robot.commands.ClimbDown;
import org.usfirst.frc.team5002.robot.commands.ClimbUp;
import org.usfirst.frc.team5002.robot.commands.INtaker;
import org.usfirst.frc.team5002.robot.commands.LaunchererC;
import org.usfirst.frc.team5002.robot.commands.OUTtaker;
import org.usfirst.frc.team5002.robot.commands.ReverseInTaker;
import org.usfirst.frc.team5002.robot.commands.TakeOuter;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc.team5002.robot.replay.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;

/**
 * @author elweb @version Last Modified 1/18/17
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick arcadeStick; //named Joystick

    // replay control state:
    public boolean currentlyReplaying = false;
    public int currentReplayIndex = 0;                                      // Our index into the state list in currentReplay (zero-based).

    public ControlState lastState = ControlState.getDefaultInstance();      // Previous control state.
    public ControlState currentState = ControlState.getDefaultInstance();   // Current control state.
    public Replay currentReplay;                                            // The replay we're reading from. Will be null if not replaying (i.e. in teleop / no replay loaded).
    public Replay.Builder currentRecording;                                 // The builder we're writing to. Will be null if not recording.

    private boolean toggleA = false;
    private boolean toggleB = false;

    /* Keep instances of the Commands here to share between teleop & replay-auto */
    private ClimbUp climbup = new ClimbUp();
    private ClimbDown climbdown = new ClimbDown();
    private LaunchererC launchererC = new LaunchererC();
    private INtaker intaker = new INtaker();
    private OUTtaker outtaker = new OUTtaker();
    private TakeOuter takeouter = new TakeOuter();
    private ReverseInTaker reverseInTaker = new ReverseInTaker();

    /* Ditto with the buttons. */
    public Button A = new JoystickButton(arcadeStick, 1);
    public Button B = new JoystickButton(arcadeStick, 2);
    public Button X = new JoystickButton(arcadeStick, 3);
    public Button Y = new JoystickButton(arcadeStick, 4);
    public Button LB = new JoystickButton(arcadeStick, 5);
    public Button RB = new JoystickButton(arcadeStick, 6);
    public Button home = new JoystickButton(arcadeStick, 7);
    public Button menu = new JoystickButton(arcadeStick, 8);

	public OI(){
		arcadeStick = new Joystick(0); //gave Joystick a job
		Y.whileHeld(climbup);//turns the climb motor on while Y is being held
		B.toggleWhenPressed(launchererC);//turns launcher motor on when B is pressed once, and off when B is pressed again
		A.toggleWhenPressed(intaker); //turns the intake motor on when A is pressed once, and off when A is pressed again
		A.toggleWhenPressed(outtaker); //turns the outake motor on at the same time as the intake motor
		LB.whileHeld(takeouter); // emergency reverse for outtake motor
		LB.whileHeld(reverseInTaker);// emergency reverse for intake motor
		LB.whileHeld(climbdown);//emergency reverse for climb motor
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

        /* Trigger button-based commands if necessary: */
        if(currentState.getButtonY()) {
            climbup.start();
        } else {
            climbup.cancel();
        }

        if(currentState.getButtonLB()) {
            takeouter.start();
            reverseInTaker.start();
            climbdown.start();
        } else {
            takeouter.cancel();
            reverseInTaker.cancel();
            climbdown.cancel();
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

        /* Start toggled commands if necessary. */
        if(toggleA) {
            intaker.start();
            outtaker.start();
        } else {
            intaker.cancel();
            outtaker.cancel();
        }

        if(toggleB) {
            launchererC.start();
        } else {
            launchererC.cancel();
        }
    }

    public void saveStateToReplay() {
        if(currentRecording != null) {
            currentRecording.addState(
                ControlState.newBuilder()
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
            );
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

	public double getForwardAxis() {
        if(currentlyReplaying) {
            return currentState.getForwardAxis();
        } else {
            return getRawForwardAxis();
        }
	}

	public double getHorizontalAxis(){
        if(currentlyReplaying) {
            return currentState.getHorizontalAxis();
        } else {
            return getRawHorizontalAxis();
        }
	}

	public double getTurnAxis(){
        if(currentlyReplaying) {
            return currentState.getTurnAxis();
        } else {
            return getRawTurnAxis();
        }
	}

	public void UpdateSD(){
		Robot.drivetrain.updateSD();//sends all the data from SwerveDrive subsystem to the SmartDashboard
	}
}
