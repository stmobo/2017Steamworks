// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: org/usfirst/frc/team5002/robot/replay.proto

package org.usfirst.frc.team5002.robot.replay;

/**
 * Protobuf type {@code team5002.Replay}
 */
public  final class Replay extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:team5002.Replay)
    ReplayOrBuilder {
  // Use Replay.newBuilder() to construct.
  private Replay(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Replay() {
    states_ = java.util.Collections.emptyList();
    replayFrequency_ = 30D;
    replayBatteryLevel_ = 12D;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Replay(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownField(input, unknownFields,
                                   extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
              states_ = new java.util.ArrayList<org.usfirst.frc.team5002.robot.replay.ControlState>();
              mutable_bitField0_ |= 0x00000001;
            }
            states_.add(
                input.readMessage(org.usfirst.frc.team5002.robot.replay.ControlState.PARSER, extensionRegistry));
            break;
          }
          case 17: {
            bitField0_ |= 0x00000001;
            replayFrequency_ = input.readDouble();
            break;
          }
          case 25: {
            bitField0_ |= 0x00000002;
            replayBatteryLevel_ = input.readDouble();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
        states_ = java.util.Collections.unmodifiableList(states_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.usfirst.frc.team5002.robot.replay.ReplayOuterClass.internal_static_team5002_Replay_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.usfirst.frc.team5002.robot.replay.ReplayOuterClass.internal_static_team5002_Replay_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.usfirst.frc.team5002.robot.replay.Replay.class, org.usfirst.frc.team5002.robot.replay.Replay.Builder.class);
  }

  private int bitField0_;
  public static final int STATES_FIELD_NUMBER = 1;
  private java.util.List<org.usfirst.frc.team5002.robot.replay.ControlState> states_;
  /**
   * <pre>
   * Actual replay data.
   * </pre>
   *
   * <code>repeated .team5002.ControlState states = 1;</code>
   */
  public java.util.List<org.usfirst.frc.team5002.robot.replay.ControlState> getStatesList() {
    return states_;
  }
  /**
   * <pre>
   * Actual replay data.
   * </pre>
   *
   * <code>repeated .team5002.ControlState states = 1;</code>
   */
  public java.util.List<? extends org.usfirst.frc.team5002.robot.replay.ControlStateOrBuilder> 
      getStatesOrBuilderList() {
    return states_;
  }
  /**
   * <pre>
   * Actual replay data.
   * </pre>
   *
   * <code>repeated .team5002.ControlState states = 1;</code>
   */
  public int getStatesCount() {
    return states_.size();
  }
  /**
   * <pre>
   * Actual replay data.
   * </pre>
   *
   * <code>repeated .team5002.ControlState states = 1;</code>
   */
  public org.usfirst.frc.team5002.robot.replay.ControlState getStates(int index) {
    return states_.get(index);
  }
  /**
   * <pre>
   * Actual replay data.
   * </pre>
   *
   * <code>repeated .team5002.ControlState states = 1;</code>
   */
  public org.usfirst.frc.team5002.robot.replay.ControlStateOrBuilder getStatesOrBuilder(
      int index) {
    return states_.get(index);
  }

  public static final int REPLAYFREQUENCY_FIELD_NUMBER = 2;
  private double replayFrequency_;
  /**
   * <pre>
   * Frequency of state updates, in Hz
   * </pre>
   *
   * <code>optional double replayFrequency = 2 [default = 30];</code>
   */
  public boolean hasReplayFrequency() {
    return ((bitField0_ & 0x00000001) == 0x00000001);
  }
  /**
   * <pre>
   * Frequency of state updates, in Hz
   * </pre>
   *
   * <code>optional double replayFrequency = 2 [default = 30];</code>
   */
  public double getReplayFrequency() {
    return replayFrequency_;
  }

  public static final int REPLAYBATTERYLEVEL_FIELD_NUMBER = 3;
  private double replayBatteryLevel_;
  /**
   * <pre>
   * Battery level at the beginning of the recording session, in volts.
   * </pre>
   *
   * <code>optional double replayBatteryLevel = 3 [default = 12];</code>
   */
  public boolean hasReplayBatteryLevel() {
    return ((bitField0_ & 0x00000002) == 0x00000002);
  }
  /**
   * <pre>
   * Battery level at the beginning of the recording session, in volts.
   * </pre>
   *
   * <code>optional double replayBatteryLevel = 3 [default = 12];</code>
   */
  public double getReplayBatteryLevel() {
    return replayBatteryLevel_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < states_.size(); i++) {
      output.writeMessage(1, states_.get(i));
    }
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      output.writeDouble(2, replayFrequency_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      output.writeDouble(3, replayBatteryLevel_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < states_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, states_.get(i));
    }
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      size += com.google.protobuf.CodedOutputStream
        .computeDoubleSize(2, replayFrequency_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      size += com.google.protobuf.CodedOutputStream
        .computeDoubleSize(3, replayBatteryLevel_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.usfirst.frc.team5002.robot.replay.Replay)) {
      return super.equals(obj);
    }
    org.usfirst.frc.team5002.robot.replay.Replay other = (org.usfirst.frc.team5002.robot.replay.Replay) obj;

    boolean result = true;
    result = result && getStatesList()
        .equals(other.getStatesList());
    result = result && (hasReplayFrequency() == other.hasReplayFrequency());
    if (hasReplayFrequency()) {
      result = result && (
          java.lang.Double.doubleToLongBits(getReplayFrequency())
          == java.lang.Double.doubleToLongBits(
              other.getReplayFrequency()));
    }
    result = result && (hasReplayBatteryLevel() == other.hasReplayBatteryLevel());
    if (hasReplayBatteryLevel()) {
      result = result && (
          java.lang.Double.doubleToLongBits(getReplayBatteryLevel())
          == java.lang.Double.doubleToLongBits(
              other.getReplayBatteryLevel()));
    }
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    if (getStatesCount() > 0) {
      hash = (37 * hash) + STATES_FIELD_NUMBER;
      hash = (53 * hash) + getStatesList().hashCode();
    }
    if (hasReplayFrequency()) {
      hash = (37 * hash) + REPLAYFREQUENCY_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          java.lang.Double.doubleToLongBits(getReplayFrequency()));
    }
    if (hasReplayBatteryLevel()) {
      hash = (37 * hash) + REPLAYBATTERYLEVEL_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          java.lang.Double.doubleToLongBits(getReplayBatteryLevel()));
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.usfirst.frc.team5002.robot.replay.Replay parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.usfirst.frc.team5002.robot.replay.Replay parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.usfirst.frc.team5002.robot.replay.Replay prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code team5002.Replay}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:team5002.Replay)
      org.usfirst.frc.team5002.robot.replay.ReplayOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.usfirst.frc.team5002.robot.replay.ReplayOuterClass.internal_static_team5002_Replay_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.usfirst.frc.team5002.robot.replay.ReplayOuterClass.internal_static_team5002_Replay_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.usfirst.frc.team5002.robot.replay.Replay.class, org.usfirst.frc.team5002.robot.replay.Replay.Builder.class);
    }

    // Construct using org.usfirst.frc.team5002.robot.replay.Replay.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getStatesFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      if (statesBuilder_ == null) {
        states_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        statesBuilder_.clear();
      }
      replayFrequency_ = 30D;
      bitField0_ = (bitField0_ & ~0x00000002);
      replayBatteryLevel_ = 12D;
      bitField0_ = (bitField0_ & ~0x00000004);
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.usfirst.frc.team5002.robot.replay.ReplayOuterClass.internal_static_team5002_Replay_descriptor;
    }

    public org.usfirst.frc.team5002.robot.replay.Replay getDefaultInstanceForType() {
      return org.usfirst.frc.team5002.robot.replay.Replay.getDefaultInstance();
    }

    public org.usfirst.frc.team5002.robot.replay.Replay build() {
      org.usfirst.frc.team5002.robot.replay.Replay result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.usfirst.frc.team5002.robot.replay.Replay buildPartial() {
      org.usfirst.frc.team5002.robot.replay.Replay result = new org.usfirst.frc.team5002.robot.replay.Replay(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (statesBuilder_ == null) {
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          states_ = java.util.Collections.unmodifiableList(states_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.states_ = states_;
      } else {
        result.states_ = statesBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
        to_bitField0_ |= 0x00000001;
      }
      result.replayFrequency_ = replayFrequency_;
      if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
        to_bitField0_ |= 0x00000002;
      }
      result.replayBatteryLevel_ = replayBatteryLevel_;
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.usfirst.frc.team5002.robot.replay.Replay) {
        return mergeFrom((org.usfirst.frc.team5002.robot.replay.Replay)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.usfirst.frc.team5002.robot.replay.Replay other) {
      if (other == org.usfirst.frc.team5002.robot.replay.Replay.getDefaultInstance()) return this;
      if (statesBuilder_ == null) {
        if (!other.states_.isEmpty()) {
          if (states_.isEmpty()) {
            states_ = other.states_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureStatesIsMutable();
            states_.addAll(other.states_);
          }
          onChanged();
        }
      } else {
        if (!other.states_.isEmpty()) {
          if (statesBuilder_.isEmpty()) {
            statesBuilder_.dispose();
            statesBuilder_ = null;
            states_ = other.states_;
            bitField0_ = (bitField0_ & ~0x00000001);
            statesBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getStatesFieldBuilder() : null;
          } else {
            statesBuilder_.addAllMessages(other.states_);
          }
        }
      }
      if (other.hasReplayFrequency()) {
        setReplayFrequency(other.getReplayFrequency());
      }
      if (other.hasReplayBatteryLevel()) {
        setReplayBatteryLevel(other.getReplayBatteryLevel());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.usfirst.frc.team5002.robot.replay.Replay parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.usfirst.frc.team5002.robot.replay.Replay) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<org.usfirst.frc.team5002.robot.replay.ControlState> states_ =
      java.util.Collections.emptyList();
    private void ensureStatesIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        states_ = new java.util.ArrayList<org.usfirst.frc.team5002.robot.replay.ControlState>(states_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.usfirst.frc.team5002.robot.replay.ControlState, org.usfirst.frc.team5002.robot.replay.ControlState.Builder, org.usfirst.frc.team5002.robot.replay.ControlStateOrBuilder> statesBuilder_;

    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public java.util.List<org.usfirst.frc.team5002.robot.replay.ControlState> getStatesList() {
      if (statesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(states_);
      } else {
        return statesBuilder_.getMessageList();
      }
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public int getStatesCount() {
      if (statesBuilder_ == null) {
        return states_.size();
      } else {
        return statesBuilder_.getCount();
      }
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public org.usfirst.frc.team5002.robot.replay.ControlState getStates(int index) {
      if (statesBuilder_ == null) {
        return states_.get(index);
      } else {
        return statesBuilder_.getMessage(index);
      }
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder setStates(
        int index, org.usfirst.frc.team5002.robot.replay.ControlState value) {
      if (statesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureStatesIsMutable();
        states_.set(index, value);
        onChanged();
      } else {
        statesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder setStates(
        int index, org.usfirst.frc.team5002.robot.replay.ControlState.Builder builderForValue) {
      if (statesBuilder_ == null) {
        ensureStatesIsMutable();
        states_.set(index, builderForValue.build());
        onChanged();
      } else {
        statesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder addStates(org.usfirst.frc.team5002.robot.replay.ControlState value) {
      if (statesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureStatesIsMutable();
        states_.add(value);
        onChanged();
      } else {
        statesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder addStates(
        int index, org.usfirst.frc.team5002.robot.replay.ControlState value) {
      if (statesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureStatesIsMutable();
        states_.add(index, value);
        onChanged();
      } else {
        statesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder addStates(
        org.usfirst.frc.team5002.robot.replay.ControlState.Builder builderForValue) {
      if (statesBuilder_ == null) {
        ensureStatesIsMutable();
        states_.add(builderForValue.build());
        onChanged();
      } else {
        statesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder addStates(
        int index, org.usfirst.frc.team5002.robot.replay.ControlState.Builder builderForValue) {
      if (statesBuilder_ == null) {
        ensureStatesIsMutable();
        states_.add(index, builderForValue.build());
        onChanged();
      } else {
        statesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder addAllStates(
        java.lang.Iterable<? extends org.usfirst.frc.team5002.robot.replay.ControlState> values) {
      if (statesBuilder_ == null) {
        ensureStatesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, states_);
        onChanged();
      } else {
        statesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder clearStates() {
      if (statesBuilder_ == null) {
        states_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        statesBuilder_.clear();
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public Builder removeStates(int index) {
      if (statesBuilder_ == null) {
        ensureStatesIsMutable();
        states_.remove(index);
        onChanged();
      } else {
        statesBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public org.usfirst.frc.team5002.robot.replay.ControlState.Builder getStatesBuilder(
        int index) {
      return getStatesFieldBuilder().getBuilder(index);
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public org.usfirst.frc.team5002.robot.replay.ControlStateOrBuilder getStatesOrBuilder(
        int index) {
      if (statesBuilder_ == null) {
        return states_.get(index);  } else {
        return statesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public java.util.List<? extends org.usfirst.frc.team5002.robot.replay.ControlStateOrBuilder> 
         getStatesOrBuilderList() {
      if (statesBuilder_ != null) {
        return statesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(states_);
      }
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public org.usfirst.frc.team5002.robot.replay.ControlState.Builder addStatesBuilder() {
      return getStatesFieldBuilder().addBuilder(
          org.usfirst.frc.team5002.robot.replay.ControlState.getDefaultInstance());
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public org.usfirst.frc.team5002.robot.replay.ControlState.Builder addStatesBuilder(
        int index) {
      return getStatesFieldBuilder().addBuilder(
          index, org.usfirst.frc.team5002.robot.replay.ControlState.getDefaultInstance());
    }
    /**
     * <pre>
     * Actual replay data.
     * </pre>
     *
     * <code>repeated .team5002.ControlState states = 1;</code>
     */
    public java.util.List<org.usfirst.frc.team5002.robot.replay.ControlState.Builder> 
         getStatesBuilderList() {
      return getStatesFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.usfirst.frc.team5002.robot.replay.ControlState, org.usfirst.frc.team5002.robot.replay.ControlState.Builder, org.usfirst.frc.team5002.robot.replay.ControlStateOrBuilder> 
        getStatesFieldBuilder() {
      if (statesBuilder_ == null) {
        statesBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            org.usfirst.frc.team5002.robot.replay.ControlState, org.usfirst.frc.team5002.robot.replay.ControlState.Builder, org.usfirst.frc.team5002.robot.replay.ControlStateOrBuilder>(
                states_,
                ((bitField0_ & 0x00000001) == 0x00000001),
                getParentForChildren(),
                isClean());
        states_ = null;
      }
      return statesBuilder_;
    }

    private double replayFrequency_ = 30D;
    /**
     * <pre>
     * Frequency of state updates, in Hz
     * </pre>
     *
     * <code>optional double replayFrequency = 2 [default = 30];</code>
     */
    public boolean hasReplayFrequency() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <pre>
     * Frequency of state updates, in Hz
     * </pre>
     *
     * <code>optional double replayFrequency = 2 [default = 30];</code>
     */
    public double getReplayFrequency() {
      return replayFrequency_;
    }
    /**
     * <pre>
     * Frequency of state updates, in Hz
     * </pre>
     *
     * <code>optional double replayFrequency = 2 [default = 30];</code>
     */
    public Builder setReplayFrequency(double value) {
      bitField0_ |= 0x00000002;
      replayFrequency_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Frequency of state updates, in Hz
     * </pre>
     *
     * <code>optional double replayFrequency = 2 [default = 30];</code>
     */
    public Builder clearReplayFrequency() {
      bitField0_ = (bitField0_ & ~0x00000002);
      replayFrequency_ = 30D;
      onChanged();
      return this;
    }

    private double replayBatteryLevel_ = 12D;
    /**
     * <pre>
     * Battery level at the beginning of the recording session, in volts.
     * </pre>
     *
     * <code>optional double replayBatteryLevel = 3 [default = 12];</code>
     */
    public boolean hasReplayBatteryLevel() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <pre>
     * Battery level at the beginning of the recording session, in volts.
     * </pre>
     *
     * <code>optional double replayBatteryLevel = 3 [default = 12];</code>
     */
    public double getReplayBatteryLevel() {
      return replayBatteryLevel_;
    }
    /**
     * <pre>
     * Battery level at the beginning of the recording session, in volts.
     * </pre>
     *
     * <code>optional double replayBatteryLevel = 3 [default = 12];</code>
     */
    public Builder setReplayBatteryLevel(double value) {
      bitField0_ |= 0x00000004;
      replayBatteryLevel_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Battery level at the beginning of the recording session, in volts.
     * </pre>
     *
     * <code>optional double replayBatteryLevel = 3 [default = 12];</code>
     */
    public Builder clearReplayBatteryLevel() {
      bitField0_ = (bitField0_ & ~0x00000004);
      replayBatteryLevel_ = 12D;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:team5002.Replay)
  }

  // @@protoc_insertion_point(class_scope:team5002.Replay)
  private static final org.usfirst.frc.team5002.robot.replay.Replay DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.usfirst.frc.team5002.robot.replay.Replay();
  }

  public static org.usfirst.frc.team5002.robot.replay.Replay getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @java.lang.Deprecated public static final com.google.protobuf.Parser<Replay>
      PARSER = new com.google.protobuf.AbstractParser<Replay>() {
    public Replay parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new Replay(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Replay> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Replay> getParserForType() {
    return PARSER;
  }

  public org.usfirst.frc.team5002.robot.replay.Replay getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

