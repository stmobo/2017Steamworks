// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: replay.proto

package org.usfirst.frc.team5002.robot.replay;

public final class ReplayOuterClass {
  private ReplayOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_team5002_ControlState_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_team5002_ControlState_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_team5002_Replay_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_team5002_Replay_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014replay.proto\022\010team5002\"\350\001\n\014ControlStat" +
      "e\022\024\n\014forward_axis\030\001 \001(\001\022\027\n\017horizontal_ax" +
      "is\030\002 \001(\001\022\021\n\tturn_axis\030\003 \001(\001\022\020\n\010button_A\030" +
      "\004 \001(\010\022\020\n\010button_B\030\005 \001(\010\022\020\n\010button_X\030\006 \001(" +
      "\010\022\020\n\010button_Y\030\007 \001(\010\022\021\n\tbutton_LB\030\010 \001(\010\022\021" +
      "\n\tbutton_RB\030\t \001(\010\022\023\n\013button_home\030\n \001(\010\022\023" +
      "\n\013button_menu\030\013 \001(\010\"b\n\006Replay\022%\n\005state\030\001" +
      " \003(\0132\026.team5002.ControlState\022\030\n\020replay_f" +
      "requency\030\002 \001(\001\022\027\n\017battery_voltage\030\003 \001(\001B" +
      ")\n%org.usfirst.frc.team5002.robot.replay",
      "P\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_team5002_ControlState_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_team5002_ControlState_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_team5002_ControlState_descriptor,
        new java.lang.String[] { "ForwardAxis", "HorizontalAxis", "TurnAxis", "ButtonA", "ButtonB", "ButtonX", "ButtonY", "ButtonLB", "ButtonRB", "ButtonHome", "ButtonMenu", });
    internal_static_team5002_Replay_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_team5002_Replay_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_team5002_Replay_descriptor,
        new java.lang.String[] { "State", "ReplayFrequency", "BatteryVoltage", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
