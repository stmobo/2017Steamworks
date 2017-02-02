package org.usfirst.frc.team5002.robot;

import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc.team5002.robot.replay.*;



/**
 * @author elweb @version Last Modified 1/18/17
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick arcadeStick; //named Joystick

    // replay control state:
    public int currentReplayIndex = 0;                                              // Our index into the state list in currentReplay (zero-based).
    public ControlState currentState = ControlState.getDefaultInstance();           // Current control state.

    public boolean currentlyReplaying = false;
    public Replay currentReplay;               // The replay we're reading from. Will be null if not replaying (i.e. in teleop / no replay loaded).
    public Replay.Builder currentRecording;    // The builder we're writing to. Will be null if not recording.

	public OI(){
		arcadeStick = new Joystick(0); //gave Joystick a job
	}

    /*
     * Direct joystick access methods.
     */
    private double stickAxisDirect_fwd() {
		return arcadeStick.getRawAxis(1) * -1.0;//allows the Joystick to command the Robot's forwards and backwards movement
	}

	private double stickAxisDirect_horiz(){
		return arcadeStick.getRawAxis(0) * -1.0;//allows the Joystick to command the Robot's side to side movement
	}

	private double stickAxisDirect_turn(){
		return arcadeStick.getRawAxis(4);//allows the Joystick to command the rotation of the Robot
	}

    /*
     * Append the current state of the controls to currentRecording.
     */
    public void saveCurrentState() {
        currentRecording.addState(
            ControlState.newBuilder()
                .setForwardAxis( stickAxisDirect_fwd() )
                .setHorizontalAxis( stickAxisDirect_horiz() )
                .setTurnAxis( stickAxisDirect_turn() )
        );

        // TODO: Save other stuff if necessary (new buttons/axes/etc).
    }

    /*
     * Load the next control state from currentReplay.
     */
    public void loadNextState() {
        if(currentReplayIndex < currentReplay.getStateCount()) {
            currentState = currentReplay.getState(currentReplayIndex);
            currentReplayIndex += 1;
        } else {
            // If this is the end of the replay, then just go to default values.
            // (These values should effectively stop the robot.)
            currentState = ControlState.getDefaultInstance();
        }

        // TODO: If/when button-triggered commands are added, make sure to trigger them here if necessary.
    }

	public double getForwardAxis() {
        if(!currentlyReplaying) {
            return stickAxisDirect_fwd();
        } else {
            return currentState.getForwardAxis();
        }
	}

	public double getHorizontalAxis(){
        if(!currentlyReplaying) {
            return stickAxisDirect_horiz();
        } else {
            return currentState.getHorizontalAxis();
        }
	}

	public double getTurnAxis(){
        if(!currentlyReplaying) {
            return stickAxisDirect_turn();
        } else {
            return currentState.getTurnAxis();
        }
	}

	public void UpdateSD(){
		Robot.drivetrain.UpdateSD();//sends all the data from SwerveDrive subsystem to the SmartDashboard
	}
}
