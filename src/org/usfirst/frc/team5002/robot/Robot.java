
package org.usfirst.frc.team5002.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5002.robot.commands.*;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;
import org.usfirst.frc.team5002.robot.replay.*;

import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final SwerveDrive drivetrain = new SwerveDrive();
	public static OI oi;

    double replay_frequency = 30.0; // hz

	Command autonomousCommand;
    SendableChooser<String> slotSelector = new SendableChooser<String>();
    Timer replayTimer = new Timer();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();

		/* Add PID Test commands. */
		SmartDashboard.putData("PIDSteerTest-FrontLeft", new PIDSteerTestSingle(drivetrain.fl_steer));
		SmartDashboard.putData("PIDSteerTest-FrontRight", new PIDSteerTestSingle(drivetrain.fr_steer));
		SmartDashboard.putData("PIDSteerTest-BackLeft", new PIDSteerTestSingle(drivetrain.bl_steer));
		SmartDashboard.putData("PIDSteerTest-BackRight", new PIDSteerTestSingle(drivetrain.br_steer));

        /* Recording commands. */
        SmartDashboard.putData("StartRecording", new StartRecording());
        SmartDashboard.putData("Save-Slot1", new SaveRecording("~/slot1.replay"));
        SmartDashboard.putData("Save-Slot2", new SaveRecording("~/slot2.replay"));
        SmartDashboard.putData("Save-Slot3", new SaveRecording("~/slot3.replay"));

        slotSelector.addObject("Slot 1", "Slot 1");
        slotSelector.addObject("Slot 2", "Slot 2");
        slotSelector.addObject("Slot 3", "Slot 3");
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		SmartDashboard.putNumber("FL-Pos", Robot.drivetrain.fl_steer.getPosition());
		SmartDashboard.putNumber("FR-Pos", Robot.drivetrain.fr_steer.getPosition());
		SmartDashboard.putNumber("BL-Pos", Robot.drivetrain.bl_steer.getPosition());
		SmartDashboard.putNumber("BR-Pos", Robot.drivetrain.br_steer.getPosition());

		//Robot.drivetrain.UpdateSDSingle(Robot.drivetrain.fr_steer);
		//Robot.drivetrain.UpdateSDSingle(Robot.drivetrain.fl_steer);

		Robot.drivetrain.UpdateSD();

		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

        String slotSelect = slotSelector.getSelected();
        switch(slotSelect) {
            default:
            case "Slot 1":
                oi.loadReplayFromFile("~/slot1.replay");
                break;
            case "Slot 2":
                oi.loadReplayFromFile("~/slot2.replay");
                break;
            case "Slot 3":
                oi.loadReplayFromFile("~/slot3.replay");
                break;
        }

        replayTimer.start();
        oi.currentlyReplaying = true;

        autonomousCommand = new Teleop();
        Scheduler.getInstance().add(autonomousCommand);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
        if(replayTimer.hasPeriodPassed(1/replay_frequency)) {
            oi.loadStateFromReplay();
        }

		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();

		//PIDSteerCollective PIDTest = new PIDSteerCollective();
		Teleop teleopTest = new Teleop();
		Scheduler.getInstance().add(teleopTest);
		//Scheduler.getInstance().add(PIDTest);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
        if(replayTimer.hasPeriodPassed(1/replay_frequency)) {
            oi.saveStateToReplay();
        }

		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
