package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class SwerveCalibrate extends Command {

    Timer swerveTime;

    // yes, both are supposed to be initialized to the opposite extreme
    int minObservedADC[] = {1024, 1024, 1024, 1024};
    int maxObservedADC[] = {0, 0, 0, 0};

    public SwerveCalibrate() {
    	requires(Robot.drivetrain);
        swerveTime = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.setSteerVbus(SwerveDrive.ModulePosition.FL, 1.0);
        Robot.drivetrain.setSteerVbus(SwerveDrive.ModulePosition.FR, 1.0);
        Robot.drivetrain.setSteerVbus(SwerveDrive.ModulePosition.BL, 1.0);
        Robot.drivetrain.setSteerVbus(SwerveDrive.ModulePosition.BR, 1.0);

        swerveTime.reset();
        swerveTime.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        int currentADC[] = {
            Robot.drivetrain.getCurrentSteerADC(SwerveDrive.ModulePosition.FL),
            Robot.drivetrain.getCurrentSteerADC(SwerveDrive.ModulePosition.FR),
            Robot.drivetrain.getCurrentSteerADC(SwerveDrive.ModulePosition.BL),
            Robot.drivetrain.getCurrentSteerADC(SwerveDrive.ModulePosition.BR)
        };

        for (int i=0; i<4; i++) {
            if(minObservedADC[i] > currentADC[i]) {
                minObservedADC[i] = currentADC[i];
            } else if(maxObservedADC[i] < currentADC[i]) {
                maxObservedADC[i] = currentADC[i];
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return swerveTime.get() < 2.5;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.calibrateSteerADC(SwerveDrive.ModulePosition.FL, minObservedADC[0], maxObservedADC[0]);
        Robot.drivetrain.calibrateSteerADC(SwerveDrive.ModulePosition.FR, minObservedADC[1], maxObservedADC[1]);
        Robot.drivetrain.calibrateSteerADC(SwerveDrive.ModulePosition.BL, minObservedADC[2], maxObservedADC[2]);
        Robot.drivetrain.calibrateSteerADC(SwerveDrive.ModulePosition.BR, minObservedADC[3], maxObservedADC[3]);

        System.out.println("Minimum observed ADC values: "
            + Integer.toString(minObservedADC[0])
            + " " + Integer.toString(minObservedADC[1])
            + " " + Integer.toString(minObservedADC[2])
            + " " + Integer.toString(minObservedADC[3])
        );

        System.out.println("Maximum observed ADC values: "
            + Integer.toString(maxObservedADC[0])
            + " " + Integer.toString(maxObservedADC[1])
            + " " + Integer.toString(maxObservedADC[2])
            + " " + Integer.toString(maxObservedADC[3])
        );

        Robot.drivetrain.setSteerVbus(SwerveDrive.ModulePosition.FL, 0.0);
        Robot.drivetrain.setSteerVbus(SwerveDrive.ModulePosition.FR, 0.0);
        Robot.drivetrain.setSteerVbus(SwerveDrive.ModulePosition.BL, 0.0);
        Robot.drivetrain.setSteerVbus(SwerveDrive.ModulePosition.BR, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
