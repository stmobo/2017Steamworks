package org.usfirst.frc.team5002.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5002.robot.commands.*;
import org.usfirst.frc.team5002.robot.replay.*;
import org.usfirst.frc.team5002.robot.subsystems.*;

import edu.wpi.first.wpilibj.Timer;
import java.io.FileInputStream;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final SwerveDrive drivetrain = new SwerveDrive();
	public static final Intake intake = new Intake();
	public static final Launcherer launcherer = new Launcherer();
	public static final RopeClimber ropeClimber = new RopeClimber();
	public static final Outtake outtake = new Outtake();
	public static OI oi;

	public static DigitalInput limSwitch;
	private Timer intakeStopTimer;
	private boolean intakeTimingStarted;
	private boolean intakeTimePassed;

	public static AHRS navx;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
    SendableChooser<String> slotSelector = new SendableChooser<String>();

    Timer replayTimer = new Timer();
    public double replayFrequency = 1/0.0020;   // Hz

    // Paths are in UNIX format (forward slashes)
    public String replayDir = "/home/lvuser/"; // stick it in the homedir by default, I'm pretty sure FRCUserProgram.jar runs as lvuser on the RIO

    Timer replayUpdateTimer;


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();

		limSwitch = new DigitalInput(0);
		intakeStopTimer = new Timer();

		try {
			/* NOTE: With respect to the NavX, the robot's front is in the -X direction.
			 * The robot's right side is in the +Y direction,
			 * and the robot's top side is in the +Z direction as usual.
		     * Clockwise rotation = increasing yaw.
		     */

			navx = new AHRS(SerialPort.Port.kMXP);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
			navx = null;
		}

        // start camera stream lol
        UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
        cam.setFPS(15);
        cam.setResolution(320, 240);

        if(navx != null) {
            navx.zeroYaw();
        }

        /* Recording commands. */
        SmartDashboard.putData("StartRecording", new StartRecording());
        SmartDashboard.putData("Save-Slot1", new SaveRecording(replayDir + "slot1.replay"));
        SmartDashboard.putData("Save-Slot2", new SaveRecording(replayDir + "slot2.replay"));
        SmartDashboard.putData("Save-Slot3", new SaveRecording(replayDir + "slot3.replay"));

        slotSelector.addObject("Slot 1", "Slot 1");
        slotSelector.addObject("Slot 2", "Slot 2");
        slotSelector.addObject("Slot 3", "Slot 3");

        replayUpdateTimer = new Timer();
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
		/*
		SmartDashboard.putNumber("FL-Pos", Robot.drivetrain.fl_steer.getPosition());
		SmartDashboard.putNumber("FR-Pos", Robot.drivetrain.fr_steer.getPosition());
		SmartDashboard.putNumber("BL-Pos", Robot.drivetrain.bl_steer.getPosition());
		SmartDashboard.putNumber("BR-Pos", Robot.drivetrain.br_steer.getPosition());
		*/

		//Robot.drivetrain.UpdateSDSingle(Robot.drivetrain.fr_steer);
		//Robot.drivetrain.UpdateSDSingle(Robot.drivetrain.fl_steer);

		oi.UpdateSD();
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
        oi.loadReplayFromFile(replayDir + "slot1.replay");

        if(Robot.oi.currentReplay != null) {
            replayFrequency = Robot.oi.currentReplay.getReplayFrequency();
        }

        Robot.oi.currentReplayIndex = 0;

        replayTimer.reset();
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
        if(replayUpdateTimer.hasPeriodPassed(1/replayFrequency)) {
            oi.loadStateFromReplay();
        }

        oi.updateSD();
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

        oi.currentlyReplaying = false;
        oi.currentRecording = Replay.newBuilder().setBatteryVoltage(12.0).setReplayFrequency(replayFrequency);

        replayUpdateTimer.reset();
        replayUpdateTimer.start();

		Teleop teleopTest = new Teleop();
		Scheduler.getInstance().add(teleopTest);

		//Scheduler.getInstance().add(new AutoIntake());

		oi.updateOIState();

		//Command test = new SteerTestVbus();
		//Scheduler.getInstance().add(test);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		//Robot.oi.testing();

		if(!limSwitch.get()) {
			Robot.intake.run();
			intakeTimePassed = false;
			intakeTimingStarted = false;
			intakeStopTimer.stop();
		} else {
			if(!intakeTimingStarted) {
				intakeTimingStarted = true;
				intakeStopTimer.reset();
				intakeStopTimer.start();
			} else {
				if(!intakeTimePassed && intakeStopTimer.get() >= 1.5) {
					Robot.intake.stop();
					intakeStopTimer.stop();
					intakeTimePassed = true;
				}
			}
		}

        oi.loadStateFromController();

        if(replayUpdateTimer.hasPeriodPassed(1/replayFrequency)) {
            oi.saveStateToReplay();
        }

        oi.updateSD();

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
