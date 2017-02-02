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
   * <code>optional double forwardAxis = 1;</code>
   */
  boolean hasForwardAxis();
  /**
   * <pre>
   * Forward / backward movement axis.
   * </pre>
   *
   * <code>optional double forwardAxis = 1;</code>
   */
  double getForwardAxis();

  /**
   * <pre>
   * Left / right strafing axis.
   * </pre>
   *
   * <code>optional double horizontalAxis = 2;</code>
   */
  boolean hasHorizontalAxis();
  /**
   * <pre>
   * Left / right strafing axis.
   * </pre>
   *
   * <code>optional double horizontalAxis = 2;</code>
   */
  double getHorizontalAxis();

  /**
   * <pre>
   * CW / CCW rotation axis.
   * </pre>
   *
   * <code>optional double turnAxis = 3;</code>
   */
  boolean hasTurnAxis();
  /**
   * <pre>
   * CW / CCW rotation axis.
   * </pre>
   *
   * <code>optional double turnAxis = 3;</code>
   */
  double getTurnAxis();
}
