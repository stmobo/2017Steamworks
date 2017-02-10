package org.usfirst.frc.team5002.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team5002.robot.commands.BackwardsAuto;
import org.usfirst.frc.team5002.robot.commands.ForwardsAuto;
import org.usfirst.frc.team5002.robot.commands.FullStopAuto;


/**
 *
 */
public class AutonomousCommandGroup extends CommandGroup {

	public AutonomousCommandGroup() {
        
		// Add Commands here:
        
    	addSequential(new ForwardsAuto());
    	addSequential(new FullStopAuto());
    	addSequential(new BackwardsAuto());

	}

	
}

	


