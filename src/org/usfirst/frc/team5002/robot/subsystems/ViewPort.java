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
    private VideoSource currentSrc;
    private MjpegServer server;
    private SendableChooser<VideoSource> srcChooser;

    public ViewPort() {
        /* Init all sources */
        sources = VideoSource.enumerateSources();
        currentSrc = null;
        srcChooser = new SendableChooser<VideoSource>();

        int i = 0;
        for(VideoSource src : sources) {
            src.setFPS(15);
            src.setResolution(320, 240);

            if(i == 0) {
                srcChooser.addDefault(cameraNames[i] + " (" + src.getName()+")", src);
            } else {
                srcChooser.addObject(cameraNames[i] + " (" + src.getName()+")", src);
            }

            i++;
        }

        server = CameraServer.getInstance().addServer("ViewPort");
        if(i != 0) {
            server.setSource(sources[0]);
            currentSrc = sources[0];
        }
        SmartDashboard.putData("Camera", srcChooser);
	}

	public void initDefaultCommand() {}

	public void updateSD() {
        /* Update video source if necessary */
        VideoSource chosen = srcChooser.getSelected();
        if(chosen != null && chosen != currentSrc) {
            server.setSource(chosen);
            currentSrc = chosen;
        }
    }
}
