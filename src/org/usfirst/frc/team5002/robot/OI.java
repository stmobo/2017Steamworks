package org.usfirst.frc.team5002.robot;

import edu.wpi.first.wpilibj.Joystick;
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

    public boolean currentlyReplaying = false;
    public int currentReplayIndex = 0;

    public ControlState currentState = ControlState.getDefaultInstance();
    public Replay currentReplay;
    public Replay.Builder currentRecording;

	public OI(){
		arcadeStick = new Joystick(0); //gave Joystick a job
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
        if(currentReplayIndex < currentReplay.getStateCount()) {
            currentState = currentReplay.getState(currentReplayIndex);
            currentReplayIndex += 1;
        } else {
            currentState = ControlState.getDefaultInstance();
        }
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
            currentRecording.build().writeTo(ofstream);

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
		Robot.drivetrain.UpdateSD();//sends all the data from SwerveDrive subsystem to the SmartDashboard
	}
}
