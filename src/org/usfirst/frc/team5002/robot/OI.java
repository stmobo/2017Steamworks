package org.usfirst.frc.team5002.robot;

import org.usfirst.frc.team5002.robot.commands.ClimbUp;
import org.usfirst.frc.team5002.robot.commands.INtaker;
import org.usfirst.frc.team5002.robot.commands.LaunchererC;
import org.usfirst.frc.team5002.robot.commands.OUTtaker;
import org.usfirst.frc.team5002.robot.commands.ReverseInTaker;
import org.usfirst.frc.team5002.robot.commands.TakeOuter;

import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc.team5002.robot.replay.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

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

    public ControlState currentState = ControlState.getDefaultInstance();   // Current control state.
    public Replay currentReplay;                                            // The replay we're reading from. Will be null if not replaying (i.e. in teleop / no replay loaded).
    public Replay.Builder currentRecording;                                 // The builder we're writing to. Will be null if not recording.

	public OI(){
		arcadeStick = new Joystick(0); //gave Joystick a job
		Button A = new JoystickButton(arcadeStick, 1);
		Button B = new JoystickButton(arcadeStick, 2);
		Button X = new JoystickButton(arcadeStick, 3);
		Button Y = new JoystickButton(arcadeStick, 4);
		Button LB = new JoystickButton(arcadeStick, 5);
		Button RB = new JoystickButton(arcadeStick, 6);
		Button home = new JoystickButton(arcadeStick, 7);
		Button menu = new JoystickButton(arcadeStick, 8);

		Y.toggleWhenActive(new ClimbUp());//turns the climb motor on while Y is being held, but reverses the motor while the button is held down the second time (for emergencies)
		B.toggleWhenPressed(new LaunchererC());//turns launcher motor on when B is pressed once, and off when B is pressed again
		A.toggleWhenPressed(new INtaker()); //turns the intake motor on when A is pressed once, and off when A is pressed again
		A.toggleWhenPressed(new OUTtaker()); //turns the outake motor on at the same time as the intake motor
		LB.whileHeld(new TakeOuter()); // reverses outtake motor
		LB.whileHeld(new ReverseInTaker());// reverse intake motor
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
            currentState = currentReplay.getState(currentReplayIndex);
            currentReplayIndex += 1;
        } else {
            // If this is the end of the replay, then just go to default values.
            // (These values should effectively stop the robot.)
            currentState = ControlState.getDefaultInstance();
        }

        // TODO: If/when button-triggered commands are added, make sure to trigger them here if necessary.
    }

    public void saveStateToReplay() {
        if(currentRecording != null) {
            currentRecording.addState(
                ControlState.newBuilder()
                    .setForwardAxis(getRawForwardAxis())
                    .setHorizontalAxis(getRawHorizontalAxis())
                    .setTurnAxis(getRawTurnAxis())
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
