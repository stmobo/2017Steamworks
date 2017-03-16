package org.usfirst.frc.team5002.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    /* Swerve Drive Controller Network IDs:*/
	public static int fl_drive = 15;
	public static int fl_steer = 10;

	public static int fr_drive = 12;
	public static int fr_steer = 13;

	public static int bl_drive = 19;
	public static int bl_steer = 1;

	public static int br_drive = 2;
	public static int br_steer = 4;

    /* Analog distance sensor ports: */
    public static int distSensorLeft = 0;
    public static int distSensorRight = 1;
}
