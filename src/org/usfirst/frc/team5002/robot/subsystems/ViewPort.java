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
    private CvSink[] dummySinks;
    private int currentSrc;

    private MjpegServer server;

    public ViewPort() {
        /* Init all sources */
        UsbCameraInfo[] cams = UsbCamera.enumerateUsbCameras();
        dummySinks = new CvSink[cams.length];
        sources = new VideoSource[cams.length];

        System.out.println("Got " + Integer.toString(sources.length) + " sources");
        System.out.println("Got " + Integer.toString(cams.length) + " USB cameras");
        
        if(cams.length > 0) {
	        int i = 0;
	        server = CameraServer.getInstance().addServer("ViewPort");
	        for(UsbCameraInfo cam : cams) {
	        	VideoSource src = new UsbCamera(cam.name, cam.dev);
	        	System.out.println("Initialized camera " + Integer.toString(i) + ": device " + Integer.toString(cam.dev) + ": " + cam.name);
	        	sources[i] = src;
	            src.setFPS(15);
	            src.setResolution(320, 240);
	
	            dummySinks[i] = new CvSink("DummySink-"+Integer.toString(i));
	            dummySinks[i].setSource(src);
	            dummySinks[i].setEnabled(true);
	
	            if(cameraNames.length > i) {
	                cameraNames[i] = cameraNames[i] + " (" + src.getName() + ")";
	            }
	            i++;
	        }

	        //server = (MjpegServer) CameraServer.getInstance().getServer();
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
