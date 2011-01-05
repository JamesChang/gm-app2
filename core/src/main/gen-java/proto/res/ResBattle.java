// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: res_battle.proto

package proto.res;

public final class ResBattle {
  private ResBattle() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class BattleMessage extends
      com.google.protobuf.GeneratedMessage {
    // Use BattleMessage.newBuilder() to construct.
    private BattleMessage() {
      initFields();
    }
    private BattleMessage(boolean noInit) {}
    
    private static final BattleMessage defaultInstance;
    public static BattleMessage getDefaultInstance() {
      return defaultInstance;
    }
    
    public BattleMessage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.res.ResBattle.internal_static_BattleMessage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.res.ResBattle.internal_static_BattleMessage_fieldAccessorTable;
    }
    
    // required string uuid = 1;
    public static final int UUID_FIELD_NUMBER = 1;
    private boolean hasUuid;
    private java.lang.String uuid_ = "";
    public boolean hasUuid() { return hasUuid; }
    public java.lang.String getUuid() { return uuid_; }
    
    // optional .War3Detail war3 = 2;
    public static final int WAR3_FIELD_NUMBER = 2;
    private boolean hasWar3;
    private proto.response.ResWar3Detail.War3Detail war3_;
    public boolean hasWar3() { return hasWar3; }
    public proto.response.ResWar3Detail.War3Detail getWar3() { return war3_; }
    
    // optional string text = 3;
    public static final int TEXT_FIELD_NUMBER = 3;
    private boolean hasText;
    private java.lang.String text_ = "";
    public boolean hasText() { return hasText; }
    public java.lang.String getText() { return text_; }
    
    // optional uint32 startTime = 8;
    public static final int STARTTIME_FIELD_NUMBER = 8;
    private boolean hasStartTime;
    private int startTime_ = 0;
    public boolean hasStartTime() { return hasStartTime; }
    public int getStartTime() { return startTime_; }
    
    // optional string eventName = 10;
    public static final int EVENTNAME_FIELD_NUMBER = 10;
    private boolean hasEventName;
    private java.lang.String eventName_ = "";
    public boolean hasEventName() { return hasEventName; }
    public java.lang.String getEventName() { return eventName_; }
    
    // optional string replay_file = 11;
    public static final int REPLAY_FILE_FIELD_NUMBER = 11;
    private boolean hasReplayFile;
    private java.lang.String replayFile_ = "";
    public boolean hasReplayFile() { return hasReplayFile; }
    public java.lang.String getReplayFile() { return replayFile_; }
    
    private void initFields() {
      war3_ = proto.response.ResWar3Detail.War3Detail.getDefaultInstance();
    }
    public static proto.res.ResBattle.BattleMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.res.ResBattle.BattleMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.res.ResBattle.BattleMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.res.ResBattle.BattleMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.res.ResBattle.BattleMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.res.ResBattle.BattleMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.res.ResBattle.BattleMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.res.ResBattle.BattleMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.res.ResBattle.BattleMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.res.ResBattle.BattleMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.res.ResBattle.BattleMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.res.ResBattle.BattleMessage result;
      
      // Construct using proto.res.ResBattle.BattleMessage.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.res.ResBattle.BattleMessage();
        return builder;
      }
      
      protected proto.res.ResBattle.BattleMessage internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.res.ResBattle.BattleMessage();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.res.ResBattle.BattleMessage.getDescriptor();
      }
      
      public proto.res.ResBattle.BattleMessage getDefaultInstanceForType() {
        return proto.res.ResBattle.BattleMessage.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.res.ResBattle.BattleMessage build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.res.ResBattle.BattleMessage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.res.ResBattle.BattleMessage buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        proto.res.ResBattle.BattleMessage returnMe = result;
        result = null;
        return returnMe;
      }
      
      
      // required string uuid = 1;
      public boolean hasUuid() {
        return result.hasUuid();
      }
      public java.lang.String getUuid() {
        return result.getUuid();
      }
      public Builder setUuid(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasUuid = true;
        result.uuid_ = value;
        return this;
      }
      public Builder clearUuid() {
        result.hasUuid = false;
        result.uuid_ = getDefaultInstance().getUuid();
        return this;
      }
      
      // optional .War3Detail war3 = 2;
      public boolean hasWar3() {
        return result.hasWar3();
      }
      public proto.response.ResWar3Detail.War3Detail getWar3() {
        return result.getWar3();
      }
      public Builder setWar3(proto.response.ResWar3Detail.War3Detail value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.hasWar3 = true;
        result.war3_ = value;
        return this;
      }
      public Builder setWar3(proto.response.ResWar3Detail.War3Detail.Builder builderForValue) {
        result.hasWar3 = true;
        result.war3_ = builderForValue.build();
        return this;
      }
      public Builder mergeWar3(proto.response.ResWar3Detail.War3Detail value) {
        if (result.hasWar3() &&
            result.war3_ != proto.response.ResWar3Detail.War3Detail.getDefaultInstance()) {
          result.war3_ =
            proto.response.ResWar3Detail.War3Detail.newBuilder(result.war3_).mergeFrom(value).buildPartial();
        } else {
          result.war3_ = value;
        }
        result.hasWar3 = true;
        return this;
      }
      public Builder clearWar3() {
        result.hasWar3 = false;
        result.war3_ = proto.response.ResWar3Detail.War3Detail.getDefaultInstance();
        return this;
      }
      
      // optional string text = 3;
      public boolean hasText() {
        return result.hasText();
      }
      public java.lang.String getText() {
        return result.getText();
      }
      public Builder setText(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasText = true;
        result.text_ = value;
        return this;
      }
      public Builder clearText() {
        result.hasText = false;
        result.text_ = getDefaultInstance().getText();
        return this;
      }
      
      // optional uint32 startTime = 8;
      public boolean hasStartTime() {
        return result.hasStartTime();
      }
      public int getStartTime() {
        return result.getStartTime();
      }
      public Builder setStartTime(int value) {
        result.hasStartTime = true;
        result.startTime_ = value;
        return this;
      }
      public Builder clearStartTime() {
        result.hasStartTime = false;
        result.startTime_ = 0;
        return this;
      }
      
      // optional string eventName = 10;
      public boolean hasEventName() {
        return result.hasEventName();
      }
      public java.lang.String getEventName() {
        return result.getEventName();
      }
      public Builder setEventName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasEventName = true;
        result.eventName_ = value;
        return this;
      }
      public Builder clearEventName() {
        result.hasEventName = false;
        result.eventName_ = getDefaultInstance().getEventName();
        return this;
      }
      
      // optional string replay_file = 11;
      public boolean hasReplayFile() {
        return result.hasReplayFile();
      }
      public java.lang.String getReplayFile() {
        return result.getReplayFile();
      }
      public Builder setReplayFile(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasReplayFile = true;
        result.replayFile_ = value;
        return this;
      }
      public Builder clearReplayFile() {
        result.hasReplayFile = false;
        result.replayFile_ = getDefaultInstance().getReplayFile();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:BattleMessage)
    }
    
    static {
      defaultInstance = new BattleMessage(true);
      proto.res.ResBattle.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:BattleMessage)
  }
  
  public static final class BattleList extends
      com.google.protobuf.GeneratedMessage {
    // Use BattleList.newBuilder() to construct.
    private BattleList() {
      initFields();
    }
    private BattleList(boolean noInit) {}
    
    private static final BattleList defaultInstance;
    public static BattleList getDefaultInstance() {
      return defaultInstance;
    }
    
    public BattleList getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.res.ResBattle.internal_static_BattleList_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.res.ResBattle.internal_static_BattleList_fieldAccessorTable;
    }
    
    // repeated .BattleMessage battles = 1;
    public static final int BATTLES_FIELD_NUMBER = 1;
    private java.util.List<proto.res.ResBattle.BattleMessage> battles_ =
      java.util.Collections.emptyList();
    public java.util.List<proto.res.ResBattle.BattleMessage> getBattlesList() {
      return battles_;
    }
    public int getBattlesCount() { return battles_.size(); }
    public proto.res.ResBattle.BattleMessage getBattles(int index) {
      return battles_.get(index);
    }
    
    private void initFields() {
    }
    public static proto.res.ResBattle.BattleList parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.res.ResBattle.BattleList parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.res.ResBattle.BattleList parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.res.ResBattle.BattleList parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.res.ResBattle.BattleList parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.res.ResBattle.BattleList parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.res.ResBattle.BattleList parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.res.ResBattle.BattleList parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.res.ResBattle.BattleList parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.res.ResBattle.BattleList parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.res.ResBattle.BattleList prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.res.ResBattle.BattleList result;
      
      // Construct using proto.res.ResBattle.BattleList.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.res.ResBattle.BattleList();
        return builder;
      }
      
      protected proto.res.ResBattle.BattleList internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.res.ResBattle.BattleList();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.res.ResBattle.BattleList.getDescriptor();
      }
      
      public proto.res.ResBattle.BattleList getDefaultInstanceForType() {
        return proto.res.ResBattle.BattleList.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.res.ResBattle.BattleList build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.res.ResBattle.BattleList buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.res.ResBattle.BattleList buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.battles_ != java.util.Collections.EMPTY_LIST) {
          result.battles_ =
            java.util.Collections.unmodifiableList(result.battles_);
        }
        proto.res.ResBattle.BattleList returnMe = result;
        result = null;
        return returnMe;
      }
      
      
      // repeated .BattleMessage battles = 1;
      public java.util.List<proto.res.ResBattle.BattleMessage> getBattlesList() {
        return java.util.Collections.unmodifiableList(result.battles_);
      }
      public int getBattlesCount() {
        return result.getBattlesCount();
      }
      public proto.res.ResBattle.BattleMessage getBattles(int index) {
        return result.getBattles(index);
      }
      public Builder setBattles(int index, proto.res.ResBattle.BattleMessage value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.battles_.set(index, value);
        return this;
      }
      public Builder setBattles(int index, proto.res.ResBattle.BattleMessage.Builder builderForValue) {
        result.battles_.set(index, builderForValue.build());
        return this;
      }
      public Builder addBattles(proto.res.ResBattle.BattleMessage value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.battles_.isEmpty()) {
          result.battles_ = new java.util.ArrayList<proto.res.ResBattle.BattleMessage>();
        }
        result.battles_.add(value);
        return this;
      }
      public Builder addBattles(proto.res.ResBattle.BattleMessage.Builder builderForValue) {
        if (result.battles_.isEmpty()) {
          result.battles_ = new java.util.ArrayList<proto.res.ResBattle.BattleMessage>();
        }
        result.battles_.add(builderForValue.build());
        return this;
      }
      public Builder addAllBattles(
          java.lang.Iterable<? extends proto.res.ResBattle.BattleMessage> values) {
        if (result.battles_.isEmpty()) {
          result.battles_ = new java.util.ArrayList<proto.res.ResBattle.BattleMessage>();
        }
        super.addAll(values, result.battles_);
        return this;
      }
      public Builder clearBattles() {
        result.battles_ = java.util.Collections.emptyList();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:BattleList)
    }
    
    static {
      defaultInstance = new BattleList(true);
      proto.res.ResBattle.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:BattleList)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_BattleMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_BattleMessage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_BattleList_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_BattleList_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020res_battle.proto\032\024res_war3detail.proto" +
      "\032\nutil.proto\"\201\001\n\rBattleMessage\022\014\n\004uuid\030\001" +
      " \002(\t\022\031\n\004war3\030\002 \001(\0132\013.War3Detail\022\014\n\004text\030" +
      "\003 \001(\t\022\021\n\tstartTime\030\010 \001(\r\022\021\n\teventName\030\n " +
      "\001(\t\022\023\n\013replay_file\030\013 \001(\t\"-\n\nBattleList\022\037" +
      "\n\007battles\030\001 \003(\0132\016.BattleMessageB\r\n\tproto" +
      ".resH\002"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_BattleMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_BattleMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_BattleMessage_descriptor,
              new java.lang.String[] { "Uuid", "War3", "Text", "StartTime", "EventName", "ReplayFile", },
              proto.res.ResBattle.BattleMessage.class,
              proto.res.ResBattle.BattleMessage.Builder.class);
          internal_static_BattleList_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_BattleList_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_BattleList_descriptor,
              new java.lang.String[] { "Battles", },
              proto.res.ResBattle.BattleList.class,
              proto.res.ResBattle.BattleList.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          proto.response.ResWar3Detail.getDescriptor(),
          proto.util.Util.getDescriptor(),
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}
