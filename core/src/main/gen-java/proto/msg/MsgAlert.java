// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: msg_alert.proto

package proto.msg;

public final class MsgAlert {
  private MsgAlert() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class AlertMessage extends
      com.google.protobuf.GeneratedMessage {
    // Use AlertMessage.newBuilder() to construct.
    private AlertMessage() {
      initFields();
    }
    private AlertMessage(boolean noInit) {}
    
    private static final AlertMessage defaultInstance;
    public static AlertMessage getDefaultInstance() {
      return defaultInstance;
    }
    
    public AlertMessage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.msg.MsgAlert.internal_static_AlertMessage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.msg.MsgAlert.internal_static_AlertMessage_fieldAccessorTable;
    }
    
    // required string text = 1;
    public static final int TEXT_FIELD_NUMBER = 1;
    private boolean hasText;
    private java.lang.String text_ = "";
    public boolean hasText() { return hasText; }
    public java.lang.String getText() { return text_; }
    
    // required string uri = 2;
    public static final int URI_FIELD_NUMBER = 2;
    private boolean hasUri;
    private java.lang.String uri_ = "";
    public boolean hasUri() { return hasUri; }
    public java.lang.String getUri() { return uri_; }
    
    // optional string image = 4;
    public static final int IMAGE_FIELD_NUMBER = 4;
    private boolean hasImage;
    private java.lang.String image_ = "";
    public boolean hasImage() { return hasImage; }
    public java.lang.String getImage() { return image_; }
    
    // optional bool modal = 3;
    public static final int MODAL_FIELD_NUMBER = 3;
    private boolean hasModal;
    private boolean modal_ = false;
    public boolean hasModal() { return hasModal; }
    public boolean getModal() { return modal_; }
    
    private void initFields() {
    }
    public static proto.msg.MsgAlert.AlertMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.msg.MsgAlert.AlertMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.msg.MsgAlert.AlertMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.msg.MsgAlert.AlertMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.msg.MsgAlert.AlertMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.msg.MsgAlert.AlertMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.msg.MsgAlert.AlertMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.msg.MsgAlert.AlertMessage parseDelimitedFrom(
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
    public static proto.msg.MsgAlert.AlertMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.msg.MsgAlert.AlertMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.msg.MsgAlert.AlertMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.msg.MsgAlert.AlertMessage result;
      
      // Construct using proto.msg.MsgAlert.AlertMessage.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.msg.MsgAlert.AlertMessage();
        return builder;
      }
      
      protected proto.msg.MsgAlert.AlertMessage internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.msg.MsgAlert.AlertMessage();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.msg.MsgAlert.AlertMessage.getDescriptor();
      }
      
      public proto.msg.MsgAlert.AlertMessage getDefaultInstanceForType() {
        return proto.msg.MsgAlert.AlertMessage.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.msg.MsgAlert.AlertMessage build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.msg.MsgAlert.AlertMessage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.msg.MsgAlert.AlertMessage buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        proto.msg.MsgAlert.AlertMessage returnMe = result;
        result = null;
        return returnMe;
      }
      
      
      // required string text = 1;
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
      
      // required string uri = 2;
      public boolean hasUri() {
        return result.hasUri();
      }
      public java.lang.String getUri() {
        return result.getUri();
      }
      public Builder setUri(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasUri = true;
        result.uri_ = value;
        return this;
      }
      public Builder clearUri() {
        result.hasUri = false;
        result.uri_ = getDefaultInstance().getUri();
        return this;
      }
      
      // optional string image = 4;
      public boolean hasImage() {
        return result.hasImage();
      }
      public java.lang.String getImage() {
        return result.getImage();
      }
      public Builder setImage(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasImage = true;
        result.image_ = value;
        return this;
      }
      public Builder clearImage() {
        result.hasImage = false;
        result.image_ = getDefaultInstance().getImage();
        return this;
      }
      
      // optional bool modal = 3;
      public boolean hasModal() {
        return result.hasModal();
      }
      public boolean getModal() {
        return result.getModal();
      }
      public Builder setModal(boolean value) {
        result.hasModal = true;
        result.modal_ = value;
        return this;
      }
      public Builder clearModal() {
        result.hasModal = false;
        result.modal_ = false;
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:AlertMessage)
    }
    
    static {
      defaultInstance = new AlertMessage(true);
      proto.msg.MsgAlert.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:AlertMessage)
  }
  
  public static final class GameResultAlertMessage extends
      com.google.protobuf.GeneratedMessage {
    // Use GameResultAlertMessage.newBuilder() to construct.
    private GameResultAlertMessage() {
      initFields();
    }
    private GameResultAlertMessage(boolean noInit) {}
    
    private static final GameResultAlertMessage defaultInstance;
    public static GameResultAlertMessage getDefaultInstance() {
      return defaultInstance;
    }
    
    public GameResultAlertMessage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.msg.MsgAlert.internal_static_GameResultAlertMessage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.msg.MsgAlert.internal_static_GameResultAlertMessage_fieldAccessorTable;
    }
    
    // required string text = 1;
    public static final int TEXT_FIELD_NUMBER = 1;
    private boolean hasText;
    private java.lang.String text_ = "";
    public boolean hasText() { return hasText; }
    public java.lang.String getText() { return text_; }
    
    // required string uri = 2;
    public static final int URI_FIELD_NUMBER = 2;
    private boolean hasUri;
    private java.lang.String uri_ = "";
    public boolean hasUri() { return hasUri; }
    public java.lang.String getUri() { return uri_; }
    
    // optional string image = 4;
    public static final int IMAGE_FIELD_NUMBER = 4;
    private boolean hasImage;
    private java.lang.String image_ = "";
    public boolean hasImage() { return hasImage; }
    public java.lang.String getImage() { return image_; }
    
    // optional bool modal = 3;
    public static final int MODAL_FIELD_NUMBER = 3;
    private boolean hasModal;
    private boolean modal_ = false;
    public boolean hasModal() { return hasModal; }
    public boolean getModal() { return modal_; }
    
    // optional int32 power = 5;
    public static final int POWER_FIELD_NUMBER = 5;
    private boolean hasPower;
    private int power_ = 0;
    public boolean hasPower() { return hasPower; }
    public int getPower() { return power_; }
    
    // optional int32 gold = 6;
    public static final int GOLD_FIELD_NUMBER = 6;
    private boolean hasGold;
    private int gold_ = 0;
    public boolean hasGold() { return hasGold; }
    public int getGold() { return gold_; }
    
    // optional int32 activity = 7;
    public static final int ACTIVITY_FIELD_NUMBER = 7;
    private boolean hasActivity;
    private int activity_ = 0;
    public boolean hasActivity() { return hasActivity; }
    public int getActivity() { return activity_; }
    
    // optional int32 eventid = 8;
    public static final int EVENTID_FIELD_NUMBER = 8;
    private boolean hasEventid;
    private int eventid_ = 0;
    public boolean hasEventid() { return hasEventid; }
    public int getEventid() { return eventid_; }
    
    private void initFields() {
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseDelimitedFrom(
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
    public static proto.msg.MsgAlert.GameResultAlertMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.msg.MsgAlert.GameResultAlertMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.msg.MsgAlert.GameResultAlertMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.msg.MsgAlert.GameResultAlertMessage result;
      
      // Construct using proto.msg.MsgAlert.GameResultAlertMessage.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.msg.MsgAlert.GameResultAlertMessage();
        return builder;
      }
      
      protected proto.msg.MsgAlert.GameResultAlertMessage internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.msg.MsgAlert.GameResultAlertMessage();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.msg.MsgAlert.GameResultAlertMessage.getDescriptor();
      }
      
      public proto.msg.MsgAlert.GameResultAlertMessage getDefaultInstanceForType() {
        return proto.msg.MsgAlert.GameResultAlertMessage.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.msg.MsgAlert.GameResultAlertMessage build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.msg.MsgAlert.GameResultAlertMessage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.msg.MsgAlert.GameResultAlertMessage buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        proto.msg.MsgAlert.GameResultAlertMessage returnMe = result;
        result = null;
        return returnMe;
      }
      
      
      // required string text = 1;
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
      
      // required string uri = 2;
      public boolean hasUri() {
        return result.hasUri();
      }
      public java.lang.String getUri() {
        return result.getUri();
      }
      public Builder setUri(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasUri = true;
        result.uri_ = value;
        return this;
      }
      public Builder clearUri() {
        result.hasUri = false;
        result.uri_ = getDefaultInstance().getUri();
        return this;
      }
      
      // optional string image = 4;
      public boolean hasImage() {
        return result.hasImage();
      }
      public java.lang.String getImage() {
        return result.getImage();
      }
      public Builder setImage(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasImage = true;
        result.image_ = value;
        return this;
      }
      public Builder clearImage() {
        result.hasImage = false;
        result.image_ = getDefaultInstance().getImage();
        return this;
      }
      
      // optional bool modal = 3;
      public boolean hasModal() {
        return result.hasModal();
      }
      public boolean getModal() {
        return result.getModal();
      }
      public Builder setModal(boolean value) {
        result.hasModal = true;
        result.modal_ = value;
        return this;
      }
      public Builder clearModal() {
        result.hasModal = false;
        result.modal_ = false;
        return this;
      }
      
      // optional int32 power = 5;
      public boolean hasPower() {
        return result.hasPower();
      }
      public int getPower() {
        return result.getPower();
      }
      public Builder setPower(int value) {
        result.hasPower = true;
        result.power_ = value;
        return this;
      }
      public Builder clearPower() {
        result.hasPower = false;
        result.power_ = 0;
        return this;
      }
      
      // optional int32 gold = 6;
      public boolean hasGold() {
        return result.hasGold();
      }
      public int getGold() {
        return result.getGold();
      }
      public Builder setGold(int value) {
        result.hasGold = true;
        result.gold_ = value;
        return this;
      }
      public Builder clearGold() {
        result.hasGold = false;
        result.gold_ = 0;
        return this;
      }
      
      // optional int32 activity = 7;
      public boolean hasActivity() {
        return result.hasActivity();
      }
      public int getActivity() {
        return result.getActivity();
      }
      public Builder setActivity(int value) {
        result.hasActivity = true;
        result.activity_ = value;
        return this;
      }
      public Builder clearActivity() {
        result.hasActivity = false;
        result.activity_ = 0;
        return this;
      }
      
      // optional int32 eventid = 8;
      public boolean hasEventid() {
        return result.hasEventid();
      }
      public int getEventid() {
        return result.getEventid();
      }
      public Builder setEventid(int value) {
        result.hasEventid = true;
        result.eventid_ = value;
        return this;
      }
      public Builder clearEventid() {
        result.hasEventid = false;
        result.eventid_ = 0;
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:GameResultAlertMessage)
    }
    
    static {
      defaultInstance = new GameResultAlertMessage(true);
      proto.msg.MsgAlert.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:GameResultAlertMessage)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_AlertMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_AlertMessage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_GameResultAlertMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_GameResultAlertMessage_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017msg_alert.proto\"G\n\014AlertMessage\022\014\n\004tex" +
      "t\030\001 \002(\t\022\013\n\003uri\030\002 \002(\t\022\r\n\005image\030\004 \001(\t\022\r\n\005m" +
      "odal\030\003 \001(\010\"\221\001\n\026GameResultAlertMessage\022\014\n" +
      "\004text\030\001 \002(\t\022\013\n\003uri\030\002 \002(\t\022\r\n\005image\030\004 \001(\t\022" +
      "\r\n\005modal\030\003 \001(\010\022\r\n\005power\030\005 \001(\005\022\014\n\004gold\030\006 " +
      "\001(\005\022\020\n\010activity\030\007 \001(\005\022\017\n\007eventid\030\010 \001(\005B\r" +
      "\n\tproto.msgH\002"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_AlertMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_AlertMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_AlertMessage_descriptor,
              new java.lang.String[] { "Text", "Uri", "Image", "Modal", },
              proto.msg.MsgAlert.AlertMessage.class,
              proto.msg.MsgAlert.AlertMessage.Builder.class);
          internal_static_GameResultAlertMessage_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_GameResultAlertMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_GameResultAlertMessage_descriptor,
              new java.lang.String[] { "Text", "Uri", "Image", "Modal", "Power", "Gold", "Activity", "Eventid", },
              proto.msg.MsgAlert.GameResultAlertMessage.class,
              proto.msg.MsgAlert.GameResultAlertMessage.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}
