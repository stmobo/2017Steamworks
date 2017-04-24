package org.usfirst.frc.team5002.robot.subsystems;

import org.usfirst.frc.team5002.robot.Robot;
import org.usfirst.frc.team5002.robot.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SPI.Port;

import edu.wpi.first.wpilibj.networktables.*;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

public class Sensors extends Subsystem {
    private double startYaw;
    public static AHRS navx;
    private AHRS.BoardYawAxis yawAxis;

    private NetworkTable jetson;

    private UsbCamera visionCam;
    private CvSink visionSink;
    
    private static final Scalar filterLow = new Scalar(0, 94, 0);
    private static final Scalar filterHigh = new Scalar(60, 255, 255);
    
    private static final double fovHoriz = 0.623525;
    
    private double filteredOffset = 0.0;
    private double filteredDistance = 0.0;
    private static final double smoothingConst = 0.10;
    
    public Sensors() {
        jetson = NetworkTable.getTable("vision");
        
        /*
        visionCam = new UsbCamera("Vision Camera", 2);
        visionSink = new CvSink("Vision Sink");
        visionSink.setSource(visionCam);
		*/

        try {
			/* NOTE: With respect to the NavX, the robot's front is in the -X direction.
			 * The robot's right side is in the +Y direction,
			 * and the robot's top side is in the +Z direction as usual.
		     * Clockwise rotation = increasing yaw.
		     */
			navx = new AHRS(SerialPort.Port.kMXP);

            navx.zeroYaw();
            startYaw = navx.getYaw();
            yawAxis = navx.getBoardYawAxis();

		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);

            navx = null;
            startYaw = 0;
            yawAxis = null;
		}
    }

    private void visionLoop() {
    	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	
    	/* Preprocessing. */
    	Mat frame = new Mat();
    	Mat filtered = new Mat();
    	
    	visionSink.grabFrame(frame);
    	Imgproc.boxFilter(frame, filtered, -1, new Size(7,7));
    	Imgproc.cvtColor(filtered, filtered, Imgproc.COLOR_BGR2HLS);
    	Core.inRange(filtered, filterLow, filterHigh, filtered);
    	Imgproc.findContours(filtered, contours, null, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
    	
    	MatOfPoint bestContour = null;
    	MatOfPoint secBestContour = null;
    	
    	double bestScore = 0;
    	double secBestScore = 0;
    	
    	for(MatOfPoint ct : contours) {
    		double ct_area = Imgproc.contourArea(ct);
    		MatOfPoint2f ct2f = new MatOfPoint2f(ct.toArray());
    		RotatedRect minRect = Imgproc.minAreaRect(ct2f);
    		
    		double as = minRect.size.width / minRect.size.height;
    		double bb_area = minRect.size.width * minRect.size.height;
    		
    		if((ct_area < 100) || (minRect.size.width > 550)
    				|| (as < 0.1) || (as > 0.6)
    				|| (bb_area < 10)) {
    			continue;
    		}
    		
    		double cvg_ratio = ct_area / bb_area;
    		
    		double cvg_score = 100.0/Math.exp(Math.abs(cvg_ratio-1));
    		double as_score = 100.0/Math.exp(Math.abs(as - (2/5)));
    		
    		double total_score = cvg_score + as_score;
    		
			if(total_score > bestScore) {
				secBestContour = bestContour;
				secBestScore = bestScore;
				
				bestScore = total_score;
				bestContour = ct;
			}
    	}
    	
    	if((bestContour != null) && (secBestContour != null)) {
    		Rect tgt1 = Imgproc.boundingRect(bestContour);
    		Rect tgt2 = Imgproc.boundingRect(secBestContour);
    		
    		double d1 = (2.0 * (double)frame.width()) / ((double)tgt1.width * Math.tan(fovHoriz));
    		double d2 = (2.0 * (double)frame.width()) / ((double)tgt2.width * Math.tan(fovHoriz));
    		
    		double o1 = (tgt1.x + (tgt1.width/2.0) - (double)(frame.width()/2.0)) * (2.0 / tgt1.width);
    		double o2 = (tgt1.x + (tgt2.width/2.0) - (double)(frame.width()/2.0)) * (2.0 / tgt2.width);
    		
    		double dist = (d1+d2)/2.0;
    		double offset = (o1+o2);
    	}
    }
    

    /**
     * Returns true if the jetson is connected, false if not.
     */
    public boolean getJetsonStatus() {
        return jetson.getBoolean("connected", false);
    }

    /**
     * Returns true if the vision targets have been detected.
     */
    public boolean canSeeTargets() {
        return jetson.getBoolean("valid", false);
    }

    /**
     * Returns the distance to the vision targets, as reported by the Jetson.
     *
     * If the jetson is not connected, this returns 0.
     */
    public double getVisualDistance() {
        return jetson.getNumber("distance", 0.0);
    }

    /**
     * Returns the lateral (left/right) offset from the vision targets,
     * as reported by the Jetson.
     */
    public double getVisualOffset() {
        return jetson.getNumber("offset", 0.0);
    }

    /* A more reliable form of navx.getAngle() */
	public double getRobotHeading() {
		if(navx != null) {
			if(Math.abs(navx.getAngle()) <= 0.001) {
				return (navx.getYaw() - startYaw);
			} else {
				return navx.getAngle();
			}
		} else {
			return 0;
		}
	}

    public void updateSD() {
    	if(getJetsonStatus()) {
        	SmartDashboard.putBoolean("Jetson Connected", true);
        	SmartDashboard.putBoolean("Targets Visible", canSeeTargets());
        	SmartDashboard.putNumber("Vision Distance", getVisualDistance());
        	SmartDashboard.putNumber("Vision Offset", getVisualOffset());
    	} else {
        	SmartDashboard.putBoolean("Jetson Connected", false);
    	}

        SmartDashboard.putNumber("Start Yaw", startYaw);
		if(navx != null) {
			SmartDashboard.putBoolean("NavX Present", true);
			SmartDashboard.putBoolean("Calibrating", navx.isCalibrating());
			SmartDashboard.putBoolean("Connected", navx.isConnected());

			SmartDashboard.putNumber("X-Displacement", navx.getDisplacementX());
			SmartDashboard.putNumber("Y-Displacement", navx.getDisplacementY());

			SmartDashboard.putNumber("Heading", navx.getAngle());
			SmartDashboard.putNumber("Compass", navx.getCompassHeading());
			SmartDashboard.putNumber("Yaw", navx.getYaw());
			SmartDashboard.putNumber("Fused", navx.getFusedHeading());
			if(Robot.oi.focEnabled) {
				SmartDashboard.putString("Control Mode", "Field-Oriented");
			} else {
				SmartDashboard.putString("Control Mode", "Robot-Oriented (FOC available)");
			}
		} else {
			SmartDashboard.putBoolean("NavX Present", false);
			SmartDashboard.putString("Control Mode", "Robot-Oriented (FOC unavailable)");
		}
    }

    public void initDefaultCommand() {
    }
}
