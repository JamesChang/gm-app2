// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: soc_login.proto

package proto.soc;

public final class SocLogin {
  private SocLogin() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class SocketLoginMessage extends
      com.google.protobuf.GeneratedMessage {
    // Use SocketLoginMessage.newBuilder() to construct.
    private SocketLoginMessage() {
      initFields();
    }
    private SocketLoginMessage(boolean noInit) {}
    
    private static final SocketLoginMessage defaultInstance;
    public static SocketLoginMessage getDefaultInstance() {
      return defaultInstance;
    }
    
    public SocketLoginMessage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.soc.SocLogin.internal_static_SocketLoginMessage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.soc.SocLogin.internal_static_SocketLoginMessage_fieldAccessorTable;
    }
    
    // required uint32 code = 1;
    public static final int CODE_FIELD_NUMBER = 1;
    private boolean hasCode;
    private int code_ = 0;
    public boolean hasCode() { return hasCode; }
    public int getCode() { return code_; }
    
    // optional string userName = 2;
    public static final int USERNAME_FIELD_NUMBER = 2;
    private boolean hasUserName;
    private java.lang.String userName_ = "";
    public boolean hasUserName() { return hasUserName; }
    public java.lang.String getUserName() { return userName_; }
    
    // optional string password = 3;
    public static final int PASSWORD_FIELD_NUMBER = 3;
    private boolean hasPassword;
    private java.lang.String password_ = "";
    public boolean hasPassword() { return hasPassword; }
    public java.lang.String getPassword() { return password_; }
    
    // optional uint32 remeberMe = 4;
    public static final int REMEBERME_FIELD_NUMBER = 4;
    private boolean hasRemeberMe;
    private int remeberMe_ = 0;
    public boolean hasRemeberMe() { return hasRemeberMe; }
    public int getRemeberMe() { return remeberMe_; }
    
    // optional string sessionID = 5;
    public static final int SESSIONID_FIELD_NUMBER = 5;
    private boolean hasSessionID;
    private java.lang.String sessionID_ = "";
    public boolean hasSessionID() { return hasSessionID; }
    public java.lang.String getSessionID() { return sessionID_; }
    
    // optional uint32 result = 6;
    public static final int RESULT_FIELD_NUMBER = 6;
    private boolean hasResult;
    private int result_ = 0;
    public boolean hasResult() { return hasResult; }
    public int getResult() { return result_; }
    
    // optional string resultMessage = 7;
    public static final int RESULTMESSAGE_FIELD_NUMBER = 7;
    private boolean hasResultMessage;
    private java.lang.String resultMessage_ = "";
    public boolean hasResultMessage() { return hasResultMessage; }
    public java.lang.String getResultMessage() { return resultMessage_; }
    
    private void initFields() {
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseDelimitedFrom(
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
    public static proto.soc.SocLogin.SocketLoginMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.soc.SocLogin.SocketLoginMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.soc.SocLogin.SocketLoginMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.soc.SocLogin.SocketLoginMessage result;
      
      // Construct using proto.soc.SocLogin.SocketLoginMessage.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.soc.SocLogin.SocketLoginMessage();
        return builder;
      }
      
      protected proto.soc.SocLogin.SocketLoginMessage internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.soc.SocLogin.SocketLoginMessage();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.soc.SocLogin.SocketLoginMessage.getDescriptor();
      }
      
      public proto.soc.SocLogin.SocketLoginMessage getDefaultInstanceForType() {
        return proto.soc.SocLogin.SocketLoginMessage.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.soc.SocLogin.SocketLoginMessage build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.soc.SocLogin.SocketLoginMessage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.soc.SocLogin.SocketLoginMessage buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        proto.soc.SocLogin.SocketLoginMessage returnMe = result;
        result = null;
        return returnMe;
      }
      
      
      // required uint32 code = 1;
      public boolean hasCode() {
        return result.hasCode();
      }
      public int getCode() {
        return result.getCode();
      }
      public Builder setCode(int value) {
        result.hasCode = true;
        result.code_ = value;
        return this;
      }
      public Builder clearCode() {
        result.hasCode = false;
        result.code_ = 0;
        return this;
      }
      
      // optional string userName = 2;
      public boolean hasUserName() {
        return result.hasUserName();
      }
      public java.lang.String getUserName() {
        return result.getUserName();
      }
      public Builder setUserName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasUserName = true;
        result.userName_ = value;
        return this;
      }
      public Builder clearUserName() {
        result.hasUserName = false;
        result.userName_ = getDefaultInstance().getUserName();
        return this;
      }
      
      // optional string password = 3;
      public boolean hasPassword() {
        return result.hasPassword();
      }
      public java.lang.String getPassword() {
        return result.getPassword();
      }
      public Builder setPassword(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasPassword = true;
        result.password_ = value;
        return this;
      }
      public Builder clearPassword() {
        result.hasPassword = false;
        result.password_ = getDefaultInstance().getPassword();
        return this;
      }
      
      // optional uint32 remeberMe = 4;
      public boolean hasRemeberMe() {
        return result.hasRemeberMe();
      }
      public int getRemeberMe() {
        return result.getRemeberMe();
      }
      public Builder setRemeberMe(int value) {
        result.hasRemeberMe = true;
        result.remeberMe_ = value;
        return this;
      }
      public Builder clearRemeberMe() {
        result.hasRemeberMe = false;
        result.remeberMe_ = 0;
        return this;
      }
      
      // optional string sessionID = 5;
      public boolean hasSessionID() {
        return result.hasSessionID();
      }
      public java.lang.String getSessionID() {
        return result.getSessionID();
      }
      public Builder setSessionID(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasSessionID = true;
        result.sessionID_ = value;
        return this;
      }
      public Builder clearSessionID() {
        result.hasSessionID = false;
        result.sessionID_ = getDefaultInstance().getSessionID();
        return this;
      }
      
      // optional uint32 result = 6;
      public boolean hasResult() {
        return result.hasResult();
      }
      public int getResult() {
        return result.getResult();
      }
      public Builder setResult(int value) {
        result.hasResult = true;
        result.result_ = value;
        return this;
      }
      public Builder clearResult() {
        result.hasResult = false;
        result.result_ = 0;
        return this;
      }
      
      // optional string resultMessage = 7;
      public boolean hasResultMessage() {
        return result.hasResultMessage();
      }
      public java.lang.String getResultMessage() {
        return result.getResultMessage();
      }
      public Builder setResultMessage(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasResultMessage = true;
        result.resultMessage_ = value;
        return this;
      }
      public Builder clearResultMessage() {
        result.hasResultMessage = false;
        result.resultMessage_ = getDefaultInstance().getResultMessage();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:SocketLoginMessage)
    }
    
    static {
      defaultInstance = new SocketLoginMessage(true);
      proto.soc.SocLogin.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:SocketLoginMessage)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_SocketLoginMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_SocketLoginMessage_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017soc_login.proto\"\223\001\n\022SocketLoginMessage" +
      "\022\014\n\004code\030\001 \002(\r\022\020\n\010userName\030\002 \001(\t\022\020\n\010pass" +
      "word\030\003 \001(\t\022\021\n\tremeberMe\030\004 \001(\r\022\021\n\tsession" +
      "ID\030\005 \001(\t\022\016\n\006result\030\006 \001(\r\022\025\n\rresultMessag" +
      "e\030\007 \001(\tB\r\n\tproto.socH\002"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_SocketLoginMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_SocketLoginMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_SocketLoginMessage_descriptor,
              new java.lang.String[] { "Code", "UserName", "Password", "RemeberMe", "SessionID", "Result", "ResultMessage", },
              proto.soc.SocLogin.SocketLoginMessage.class,
              proto.soc.SocLogin.SocketLoginMessage.Builder.class);
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
