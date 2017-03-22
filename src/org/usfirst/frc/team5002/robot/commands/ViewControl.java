package org.usfirst.frc.team5002.robot.commands;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.OI;
import org.usfirst.frc.team5002.robot.subsystems.ViewPort;

import edu.wpi.first.wpilibj.command.Command;

public class ViewControl extends Command {

    private boolean viewForwardDebounce = false;
    private boolean viewBackwardDebounce = false;
    private int selected = 0;

    public ViewControl() {
       requires(Robot.viewport);
    }

    // Called just before this Command runs the first time
    protected void initialize() {}

    protected void execute() {
        if(Robot.oi.viewForwardButtonActivated()) {
            if(!viewForwardDebounce) {
                selected++;
                if(selected >= Robot.viewport.getViewCount()) {
                    selected = 0;
                }

                Robot.viewport.setView(selected);
            }

            viewForwardDebounce = true;
        } else {
            viewForwardDebounce = false;
        }

        if(Robot.oi.viewBackwardButtonActivated()) {
            if(!viewBackwardDebounce) {
                if(selected == 0) {
                    selected = Robot.viewport.getViewCount()-1;
                } else {
                    selected--;
                }

                Robot.viewport.setView(selected);
            }

            viewBackwardDebounce = true;
        } else {
            viewBackwardDebounce = false;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {}

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {}
}
