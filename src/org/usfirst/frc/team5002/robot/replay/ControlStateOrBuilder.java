// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: org/usfirst/frc/team5002/robot/replay.proto

package org.usfirst.frc.team5002.robot.replay;

public interface ControlStateOrBuilder extends
    // @@protoc_insertion_point(interface_extends:team5002.ControlState)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Forward / backward movement axis.
   * </pre>
   *
   * <code>optional double forward_axis = 1;</code>
   */
  boolean hasForwardAxis();
  /**
   * <pre>
   * Forward / backward movement axis.
   * </pre>
   *
   * <code>optional double forward_axis = 1;</code>
   */
  double getForwardAxis();

  /**
   * <pre>
   * Left / right strafing axis.
   * </pre>
   *
   * <code>optional double horizontal_axis = 2;</code>
   */
  boolean hasHorizontalAxis();
  /**
   * <pre>
   * Left / right strafing axis.
   * </pre>
   *
   * <code>optional double horizontal_axis = 2;</code>
   */
  double getHorizontalAxis();

  /**
   * <pre>
   * CW / CCW rotation axis.
   * </pre>
   *
   * <code>optional double turn_axis = 3;</code>
   */
  boolean hasTurnAxis();
  /**
   * <pre>
   * CW / CCW rotation axis.
   * </pre>
   *
   * <code>optional double turn_axis = 3;</code>
   */
  double getTurnAxis();
}
