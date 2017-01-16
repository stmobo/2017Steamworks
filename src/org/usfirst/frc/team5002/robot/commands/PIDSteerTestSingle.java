package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.subsystems.SwerveDrive;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * PIDSteerTest -- Flips one module's steer motor 180 degrees
 */
public class PIDSteerTestSingle extends Command {
	private SwerveDrive.ModulePosition mod;
	
	protected void execute(){
		Robot.drivetrain.setSwervePosition(mod, SmartDashboard.getNumber("Steer-PIDTest-Setpoint", 0.0));
	}

    public PIDSteerTestSingle(SwerveDrive.ModulePosition mod) {
        requires(Robot.drivetrain);
        this.mod = mod;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double p = SmartDashboard.getNumber("Steer-PIDTest-PTerm", 0.0);
    	double i = SmartDashboard.getNumber("Steer-PIDTest-ITerm", 0.0);
    	double d = SmartDashboard.getNumber("Steer-PIDTest-DTerm", 0.0);
    	
    	Robot.drivetrain.reconfigurePID(mod, p, i, d);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
