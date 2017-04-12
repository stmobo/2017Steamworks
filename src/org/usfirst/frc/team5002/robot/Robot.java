
package org.usfirst.frc.team5002.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5002.robot.commands.Teleop;
import org.usfirst.frc.team5002.robot.commands.ViewControl;
import org.usfirst.frc.team5002.robot.commands.AutoDriveIMU;
import org.usfirst.frc.team5002.robot.commands.AutonomousTemp;
import org.usfirst.frc.team5002.robot.commands.KillDrivetrain;
import org.usfirst.frc.team5002.robot.commands.PIDSteerCollective;
import org.usfirst.frc.team5002.robot.commands.PIDSteerTestSingle;
import org.usfirst.frc.team5002.robot.commands.SteerTestVbus;
import org.usfirst.frc.team5002.robot.subsystems.RopeClimber;
import org.usfirst.frc.team5002.robot.subsystems.Sensors;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;
import org.usfirst.frc.team5002.robot.subsystems.ViewPort;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static SwerveDrive drivetrain;
	public static RopeClimber ropeClimber;
    public static ViewPort viewport;
    public static Sensors sensors;
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		drivetrain = new SwerveDrive();
		ropeClimber = new RopeClimber();
        viewport = new ViewPort();
        sensors = new Sensors();
        oi = new OI();
        
        if(viewport != null) {
        	UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
            cam.setFPS(15);
            cam.setResolution(240, 320);
        }
        
        SmartDashboard.putData("Autonomous", chooser);

        chooser.addObject("Auto Left", new AutonomousTemp(-0.1));
        chooser.addObject("Auto Right", new AutonomousTemp(0.1));
        chooser.addObject("Auto Straight", new AutonomousTemp(0.0));
        chooser.addObject("Auto IMU Drive", new AutoDriveIMU());
        chooser.addDefault("No Auto", new KillDrivetrain());
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
		sensors.updateDistance();
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
		autonomousCommand = chooser.getSelected();

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		oi.UpdateSD();
		sensors.updateDistance();
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();

		Teleop teleop = new Teleop();
		Scheduler.getInstance().add(teleop);

		if(Robot.viewport != null) {
			ViewControl viewCtrl = new ViewControl();
			Scheduler.getInstance().add(viewCtrl);	
		}
		
		oi.updateOIState();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		sensors.updateDistance();
		oi.UpdateSD();
		oi.updateOIState();
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
