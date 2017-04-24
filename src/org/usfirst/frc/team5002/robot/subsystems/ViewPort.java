package org.usfirst.frc.team5002.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import org.usfirst.frc.team5002.robot.Robot;

import edu.wpi.cscore.*;

/**
 * @author stmobo @version Last Modified 12-Mar-17
 * ViewPort.java -- subsystem for switchable camera views
 */
public class ViewPort extends Subsystem {
    /* Names here are assumed to be in the same order as GetVideoSources().
     * Not a very reasonable assumption, but it's what we've got to work with...
     */
	private UsbCamera cam1;
	private UsbCamera cam2;
    
	private int currentSrc;
    
    private VideoSink server;
    private CvSink dummy1;
    private CvSink dummy2;
    
    public ViewPort() {
        /* Init all sources */
        cam1 = CameraServer.getInstance().startAutomaticCapture(0);
        cam2 = CameraServer.getInstance().startAutomaticCapture(1);
        
        cam1.setFPS(15);
        cam1.setResolution(240, 320);
        
        cam2.setFPS(15);
        cam2.setResolution(240, 320);
        
        server = CameraServer.getInstance().getServer();
        
        dummy1 = new CvSink("dummy1");
        dummy2 = new CvSink("dummy2");
        
        dummy1.setSource(cam1);
        dummy1.setEnabled(true);

        dummy2.setSource(cam2);
        dummy2.setEnabled(true);
        
        currentSrc = 0;
	}

    public void nextView() {
    	if(currentSrc == 0) {
    		server.setSource(cam2);
    		currentSrc = 1;
    	} else {
    		server.setSource(cam1);
    		currentSrc = 0;
    	}
    }
    
    private boolean nextViewDebounce = false;

    public void update() {
    	if(Robot.oi.viewForwardButtonActivated()) {
    		if(!nextViewDebounce) {
    			nextView();
    		}
    		nextViewDebounce = true;
    	} else {
    		nextViewDebounce = false;
    	}
    }

	public void initDefaultCommand() {}
}
