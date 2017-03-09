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

    boolean focEnabled = false;

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

        activateLowSpeed = new JoystickButton(arcadeStick, 10); // Bumper 1 (left)
        activateHighSpeed = new JoystickButton(arcadeStick, 11); // Bumper 2 (right)

		Y.whileHeld(new ClimbUp());//turns the climb motor on while Y is being held
		RB.whileHeld(new ClimbDown());//turns launcher motor on when B is pressed once, and off when B is pressed again

		//A.toggleWhenPressed(new INtaker()); //turns the intake motor on when A is pressed once, and off when A is pressed again
		B.toggleWhenPressed(new OUTtaker()); //turns the outake motor on at the same time as the intake motor

		LB.whileHeld(new TakeOuter()); // emergency reverse for outtake motor
		LB.whileHeld(new ReverseInTaker());// emergency reverse for intake motor

	}

    /* set multipliers for teleop drive speed outputs */
    public double getDriveSpeedCoefficient() {
        if(activateLowSpeed.get()) {
            return 0.5;
        } else if(activateHighSpeed.get()) {
            return 1.0;
        } else {
            return 0.75;
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
            double hdg = Robot.navx.getAngle();

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
            double hdg = Robot.navx.getAngle();

            // Y-coordinate, CW alias rotation w/ left-handed coordinate system
            return (x * Math.sin(hdg*Math.PI/180.0)) + (y * Math.cos(hdg*Math.PI/180.0));
        } else {
            return arcadeStick.getRawAxis(0) * -1.0; // (axis 0 = left-hand X axis) allows the Joystick to command the Robot's side to side movement
        }
	}

	public double getTurnAxis(){
		return arcadeStick.getRawAxis(4); // (axis 4 = right-hand X axis) allows the Joystick to command the rotation of the Robot
	}

	public void UpdateSD(){
		Robot.drivetrain.updateSD();//sends all the data from SwerveDrive subsystem to the SmartDashboard
	}
}
