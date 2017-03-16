package org.usfirst.frc.team5002.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team5002.robot.subsystems.*;
import org.usfirst.frc.team5002.robot.commands.*;
import org.usfirst.frc.team5002.robot.*;

/**
 *
 */
public class AutoFull extends CommandGroup {
    public AutoFull() {
    	addSequential(new AutonomousDrive());
    	addSequential(new AutoTurn(-45));
    	addSequential(new AutoGear());
    }
}
