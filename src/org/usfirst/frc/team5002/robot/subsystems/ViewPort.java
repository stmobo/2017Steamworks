package org.usfirst.frc.team5002.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.cscore.*;

/**
 * @author stmobo @version Last Modified 12-Mar-17
 * ViewPort.java -- subsystem for switchable camera views
 */
public class ViewPort extends Subsystem {
    /* Names here are assumed to be in the same order as GetVideoSources().
     * Not a very reasonable assumption, but it's what we've got to work with...
     */
    private String[] cameraNames = {
        "Feed 1"
    };
    private VideoSource[] sources;
    private int currentSrc;

    private MjpegServer server;

    public ViewPort() {
        /* Init all sources */
        sources = VideoSource.enumerateSources();

        int i = 0;
        for(VideoSource src : sources) {
            src.setFPS(15);
            src.setResolution(320, 240);
            if(cameraNames.length > i) {
                cameraNames[i] = cameraNames[i] + " (" + src.getName() + ")";
            }
            i++;
        }

        server = CameraServer.getInstance().addServer("ViewPort");
        if(i != 0) {
            currentSrc = 0;
            server.setSource(sources[0]);
        }
	}

    public String getFeedName() {
        if(cameraNames.length > currentSrc) {
            return cameraNames[currentSrc];
        } else {
            return sources[currentSrc].getName();
        }
    }

    public void setView(int index) {
        if(index < sources.length && index != currentSrc) {
            currentSrc = index;
            server.setSource(sources[index]);
        }
    }

    /**
     * Retrieves the number of views available for use.
     */
    public int getViewCount() {
        return sources.length;
    }

    public void updateSD() {
        SmartDashboard.putString("Selected View", getFeedName());
    }

	public void initDefaultCommand() {}
}
