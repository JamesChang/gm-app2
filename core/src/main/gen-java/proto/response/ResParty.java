// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: res_party.proto

package proto.response;

public final class ResParty {
  private ResParty() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class PartyModel extends
      com.google.protobuf.GeneratedMessage {
    // Use PartyModel.newBuilder() to construct.
    private PartyModel() {
      initFields();
    }
    private PartyModel(boolean noInit) {}
    
    private static final PartyModel defaultInstance;
    public static PartyModel getDefaultInstance() {
      return defaultInstance;
    }
    
    public PartyModel getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.response.ResParty.internal_static_PartyModel_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.response.ResParty.internal_static_PartyModel_fieldAccessorTable;
    }
    
    // required string uuid = 1;
    public static final int UUID_FIELD_NUMBER = 1;
    private boolean hasUuid;
    private java.lang.String uuid_ = "";
    public boolean hasUuid() { return hasUuid; }
    public java.lang.String getUuid() { return uuid_; }
    
    // repeated .PartyMember users = 2;
    public static final int USERS_FIELD_NUMBER = 2;
    private java.util.List<proto.response.ResParty.PartyMember> users_ =
      java.util.Collections.emptyList();
    public java.util.List<proto.response.ResParty.PartyMember> getUsersList() {
      return users_;
    }
    public int getUsersCount() { return users_.size(); }
    public proto.response.ResParty.PartyMember getUsers(int index) {
      return users_.get(index);
    }
    
    // optional uint32 leaderID = 3;
    public static final int LEADERID_FIELD_NUMBER = 3;
    private boolean hasLeaderID;
    private int leaderID_ = 0;
    public boolean hasLeaderID() { return hasLeaderID; }
    public int getLeaderID() { return leaderID_; }
    
    // optional uint32 userCount = 4;
    public static final int USERCOUNT_FIELD_NUMBER = 4;
    private boolean hasUserCount;
    private int userCount_ = 0;
    public boolean hasUserCount() { return hasUserCount; }
    public int getUserCount() { return userCount_; }
    
    private void initFields() {
    }
    public static proto.response.ResParty.PartyModel parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.response.ResParty.PartyModel parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyModel parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.response.ResParty.PartyModel parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyModel parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.response.ResParty.PartyModel parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyModel parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.response.ResParty.PartyModel parseDelimitedFrom(
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
    public static proto.response.ResParty.PartyModel parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.response.ResParty.PartyModel parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.response.ResParty.PartyModel prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.response.ResParty.PartyModel result;
      
      // Construct using proto.response.ResParty.PartyModel.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.response.ResParty.PartyModel();
        return builder;
      }
      
      protected proto.response.ResParty.PartyModel internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.response.ResParty.PartyModel();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.response.ResParty.PartyModel.getDescriptor();
      }
      
      public proto.response.ResParty.PartyModel getDefaultInstanceForType() {
        return proto.response.ResParty.PartyModel.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.response.ResParty.PartyModel build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.response.ResParty.PartyModel buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.response.ResParty.PartyModel buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.users_ != java.util.Collections.EMPTY_LIST) {
          result.users_ =
            java.util.Collections.unmodifiableList(result.users_);
        }
        proto.response.ResParty.PartyModel returnMe = result;
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
      
      // repeated .PartyMember users = 2;
      public java.util.List<proto.response.ResParty.PartyMember> getUsersList() {
        return java.util.Collections.unmodifiableList(result.users_);
      }
      public int getUsersCount() {
        return result.getUsersCount();
      }
      public proto.response.ResParty.PartyMember getUsers(int index) {
        return result.getUsers(index);
      }
      public Builder setUsers(int index, proto.response.ResParty.PartyMember value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.users_.set(index, value);
        return this;
      }
      public Builder setUsers(int index, proto.response.ResParty.PartyMember.Builder builderForValue) {
        result.users_.set(index, builderForValue.build());
        return this;
      }
      public Builder addUsers(proto.response.ResParty.PartyMember value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.users_.isEmpty()) {
          result.users_ = new java.util.ArrayList<proto.response.ResParty.PartyMember>();
        }
        result.users_.add(value);
        return this;
      }
      public Builder addUsers(proto.response.ResParty.PartyMember.Builder builderForValue) {
        if (result.users_.isEmpty()) {
          result.users_ = new java.util.ArrayList<proto.response.ResParty.PartyMember>();
        }
        result.users_.add(builderForValue.build());
        return this;
      }
      public Builder addAllUsers(
          java.lang.Iterable<? extends proto.response.ResParty.PartyMember> values) {
        if (result.users_.isEmpty()) {
          result.users_ = new java.util.ArrayList<proto.response.ResParty.PartyMember>();
        }
        super.addAll(values, result.users_);
        return this;
      }
      public Builder clearUsers() {
        result.users_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional uint32 leaderID = 3;
      public boolean hasLeaderID() {
        return result.hasLeaderID();
      }
      public int getLeaderID() {
        return result.getLeaderID();
      }
      public Builder setLeaderID(int value) {
        result.hasLeaderID = true;
        result.leaderID_ = value;
        return this;
      }
      public Builder clearLeaderID() {
        result.hasLeaderID = false;
        result.leaderID_ = 0;
        return this;
      }
      
      // optional uint32 userCount = 4;
      public boolean hasUserCount() {
        return result.hasUserCount();
      }
      public int getUserCount() {
        return result.getUserCount();
      }
      public Builder setUserCount(int value) {
        result.hasUserCount = true;
        result.userCount_ = value;
        return this;
      }
      public Builder clearUserCount() {
        result.hasUserCount = false;
        result.userCount_ = 0;
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:PartyModel)
    }
    
    static {
      defaultInstance = new PartyModel(true);
      proto.response.ResParty.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:PartyModel)
  }
  
  public static final class PartyMember extends
      com.google.protobuf.GeneratedMessage {
    // Use PartyMember.newBuilder() to construct.
    private PartyMember() {
      initFields();
    }
    private PartyMember(boolean noInit) {}
    
    private static final PartyMember defaultInstance;
    public static PartyMember getDefaultInstance() {
      return defaultInstance;
    }
    
    public PartyMember getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.response.ResParty.internal_static_PartyMember_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.response.ResParty.internal_static_PartyMember_fieldAccessorTable;
    }
    
    // required .UserModel user = 1;
    public static final int USER_FIELD_NUMBER = 1;
    private boolean hasUser;
    private proto.response.ResUser.UserModel user_;
    public boolean hasUser() { return hasUser; }
    public proto.response.ResUser.UserModel getUser() { return user_; }
    
    // optional string status = 2;
    public static final int STATUS_FIELD_NUMBER = 2;
    private boolean hasStatus;
    private java.lang.String status_ = "";
    public boolean hasStatus() { return hasStatus; }
    public java.lang.String getStatus() { return status_; }
    
    // optional bool is_out = 3;
    public static final int IS_OUT_FIELD_NUMBER = 3;
    private boolean hasIsOut;
    private boolean isOut_ = false;
    public boolean hasIsOut() { return hasIsOut; }
    public boolean getIsOut() { return isOut_; }
    
    // optional bool is_updating = 4;
    public static final int IS_UPDATING_FIELD_NUMBER = 4;
    private boolean hasIsUpdating;
    private boolean isUpdating_ = false;
    public boolean hasIsUpdating() { return hasIsUpdating; }
    public boolean getIsUpdating() { return isUpdating_; }
    
    // optional bool is_waited = 5;
    public static final int IS_WAITED_FIELD_NUMBER = 5;
    private boolean hasIsWaited;
    private boolean isWaited_ = false;
    public boolean hasIsWaited() { return hasIsWaited; }
    public boolean getIsWaited() { return isWaited_; }
    
    private void initFields() {
      user_ = proto.response.ResUser.UserModel.getDefaultInstance();
    }
    public static proto.response.ResParty.PartyMember parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.response.ResParty.PartyMember parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyMember parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.response.ResParty.PartyMember parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyMember parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.response.ResParty.PartyMember parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyMember parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.response.ResParty.PartyMember parseDelimitedFrom(
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
    public static proto.response.ResParty.PartyMember parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.response.ResParty.PartyMember parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.response.ResParty.PartyMember prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.response.ResParty.PartyMember result;
      
      // Construct using proto.response.ResParty.PartyMember.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.response.ResParty.PartyMember();
        return builder;
      }
      
      protected proto.response.ResParty.PartyMember internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.response.ResParty.PartyMember();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.response.ResParty.PartyMember.getDescriptor();
      }
      
      public proto.response.ResParty.PartyMember getDefaultInstanceForType() {
        return proto.response.ResParty.PartyMember.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.response.ResParty.PartyMember build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.response.ResParty.PartyMember buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.response.ResParty.PartyMember buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        proto.response.ResParty.PartyMember returnMe = result;
        result = null;
        return returnMe;
      }
      
      
      // required .UserModel user = 1;
      public boolean hasUser() {
        return result.hasUser();
      }
      public proto.response.ResUser.UserModel getUser() {
        return result.getUser();
      }
      public Builder setUser(proto.response.ResUser.UserModel value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.hasUser = true;
        result.user_ = value;
        return this;
      }
      public Builder setUser(proto.response.ResUser.UserModel.Builder builderForValue) {
        result.hasUser = true;
        result.user_ = builderForValue.build();
        return this;
      }
      public Builder mergeUser(proto.response.ResUser.UserModel value) {
        if (result.hasUser() &&
            result.user_ != proto.response.ResUser.UserModel.getDefaultInstance()) {
          result.user_ =
            proto.response.ResUser.UserModel.newBuilder(result.user_).mergeFrom(value).buildPartial();
        } else {
          result.user_ = value;
        }
        result.hasUser = true;
        return this;
      }
      public Builder clearUser() {
        result.hasUser = false;
        result.user_ = proto.response.ResUser.UserModel.getDefaultInstance();
        return this;
      }
      
      // optional string status = 2;
      public boolean hasStatus() {
        return result.hasStatus();
      }
      public java.lang.String getStatus() {
        return result.getStatus();
      }
      public Builder setStatus(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasStatus = true;
        result.status_ = value;
        return this;
      }
      public Builder clearStatus() {
        result.hasStatus = false;
        result.status_ = getDefaultInstance().getStatus();
        return this;
      }
      
      // optional bool is_out = 3;
      public boolean hasIsOut() {
        return result.hasIsOut();
      }
      public boolean getIsOut() {
        return result.getIsOut();
      }
      public Builder setIsOut(boolean value) {
        result.hasIsOut = true;
        result.isOut_ = value;
        return this;
      }
      public Builder clearIsOut() {
        result.hasIsOut = false;
        result.isOut_ = false;
        return this;
      }
      
      // optional bool is_updating = 4;
      public boolean hasIsUpdating() {
        return result.hasIsUpdating();
      }
      public boolean getIsUpdating() {
        return result.getIsUpdating();
      }
      public Builder setIsUpdating(boolean value) {
        result.hasIsUpdating = true;
        result.isUpdating_ = value;
        return this;
      }
      public Builder clearIsUpdating() {
        result.hasIsUpdating = false;
        result.isUpdating_ = false;
        return this;
      }
      
      // optional bool is_waited = 5;
      public boolean hasIsWaited() {
        return result.hasIsWaited();
      }
      public boolean getIsWaited() {
        return result.getIsWaited();
      }
      public Builder setIsWaited(boolean value) {
        result.hasIsWaited = true;
        result.isWaited_ = value;
        return this;
      }
      public Builder clearIsWaited() {
        result.hasIsWaited = false;
        result.isWaited_ = false;
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:PartyMember)
    }
    
    static {
      defaultInstance = new PartyMember(true);
      proto.response.ResParty.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:PartyMember)
  }
  
  public static final class PartyList extends
      com.google.protobuf.GeneratedMessage {
    // Use PartyList.newBuilder() to construct.
    private PartyList() {
      initFields();
    }
    private PartyList(boolean noInit) {}
    
    private static final PartyList defaultInstance;
    public static PartyList getDefaultInstance() {
      return defaultInstance;
    }
    
    public PartyList getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.response.ResParty.internal_static_PartyList_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.response.ResParty.internal_static_PartyList_fieldAccessorTable;
    }
    
    // repeated .PartyModel parties = 1;
    public static final int PARTIES_FIELD_NUMBER = 1;
    private java.util.List<proto.response.ResParty.PartyModel> parties_ =
      java.util.Collections.emptyList();
    public java.util.List<proto.response.ResParty.PartyModel> getPartiesList() {
      return parties_;
    }
    public int getPartiesCount() { return parties_.size(); }
    public proto.response.ResParty.PartyModel getParties(int index) {
      return parties_.get(index);
    }
    
    // optional .ListParams params = 2;
    public static final int PARAMS_FIELD_NUMBER = 2;
    private boolean hasParams;
    private proto.response.ResListbase.ListParams params_;
    public boolean hasParams() { return hasParams; }
    public proto.response.ResListbase.ListParams getParams() { return params_; }
    
    private void initFields() {
      params_ = proto.response.ResListbase.ListParams.getDefaultInstance();
    }
    public static proto.response.ResParty.PartyList parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.response.ResParty.PartyList parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyList parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.response.ResParty.PartyList parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyList parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.response.ResParty.PartyList parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResParty.PartyList parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.response.ResParty.PartyList parseDelimitedFrom(
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
    public static proto.response.ResParty.PartyList parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.response.ResParty.PartyList parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.response.ResParty.PartyList prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.response.ResParty.PartyList result;
      
      // Construct using proto.response.ResParty.PartyList.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.response.ResParty.PartyList();
        return builder;
      }
      
      protected proto.response.ResParty.PartyList internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.response.ResParty.PartyList();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.response.ResParty.PartyList.getDescriptor();
      }
      
      public proto.response.ResParty.PartyList getDefaultInstanceForType() {
        return proto.response.ResParty.PartyList.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.response.ResParty.PartyList build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.response.ResParty.PartyList buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.response.ResParty.PartyList buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.parties_ != java.util.Collections.EMPTY_LIST) {
          result.parties_ =
            java.util.Collections.unmodifiableList(result.parties_);
        }
        proto.response.ResParty.PartyList returnMe = result;
        result = null;
        return returnMe;
      }
      
      
      // repeated .PartyModel parties = 1;
      public java.util.List<proto.response.ResParty.PartyModel> getPartiesList() {
        return java.util.Collections.unmodifiableList(result.parties_);
      }
      public int getPartiesCount() {
        return result.getPartiesCount();
      }
      public proto.response.ResParty.PartyModel getParties(int index) {
        return result.getParties(index);
      }
      public Builder setParties(int index, proto.response.ResParty.PartyModel value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.parties_.set(index, value);
        return this;
      }
      public Builder setParties(int index, proto.response.ResParty.PartyModel.Builder builderForValue) {
        result.parties_.set(index, builderForValue.build());
        return this;
      }
      public Builder addParties(proto.response.ResParty.PartyModel value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.parties_.isEmpty()) {
          result.parties_ = new java.util.ArrayList<proto.response.ResParty.PartyModel>();
        }
        result.parties_.add(value);
        return this;
      }
      public Builder addParties(proto.response.ResParty.PartyModel.Builder builderForValue) {
        if (result.parties_.isEmpty()) {
          result.parties_ = new java.util.ArrayList<proto.response.ResParty.PartyModel>();
        }
        result.parties_.add(builderForValue.build());
        return this;
      }
      public Builder addAllParties(
          java.lang.Iterable<? extends proto.response.ResParty.PartyModel> values) {
        if (result.parties_.isEmpty()) {
          result.parties_ = new java.util.ArrayList<proto.response.ResParty.PartyModel>();
        }
        super.addAll(values, result.parties_);
        return this;
      }
      public Builder clearParties() {
        result.parties_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional .ListParams params = 2;
      public boolean hasParams() {
        return result.hasParams();
      }
      public proto.response.ResListbase.ListParams getParams() {
        return result.getParams();
      }
      public Builder setParams(proto.response.ResListbase.ListParams value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.hasParams = true;
        result.params_ = value;
        return this;
      }
      public Builder setParams(proto.response.ResListbase.ListParams.Builder builderForValue) {
        result.hasParams = true;
        result.params_ = builderForValue.build();
        return this;
      }
      public Builder mergeParams(proto.response.ResListbase.ListParams value) {
        if (result.hasParams() &&
            result.params_ != proto.response.ResListbase.ListParams.getDefaultInstance()) {
          result.params_ =
            proto.response.ResListbase.ListParams.newBuilder(result.params_).mergeFrom(value).buildPartial();
        } else {
          result.params_ = value;
        }
        result.hasParams = true;
        return this;
      }
      public Builder clearParams() {
        result.hasParams = false;
        result.params_ = proto.response.ResListbase.ListParams.getDefaultInstance();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:PartyList)
    }
    
    static {
      defaultInstance = new PartyList(true);
      proto.response.ResParty.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:PartyList)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_PartyModel_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_PartyModel_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_PartyMember_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_PartyMember_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_PartyList_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_PartyList_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017res_party.proto\032\016res_user.proto\032\022res_l" +
      "istbase.proto\"\\\n\nPartyModel\022\014\n\004uuid\030\001 \002(" +
      "\t\022\033\n\005users\030\002 \003(\0132\014.PartyMember\022\020\n\010leader" +
      "ID\030\003 \001(\r\022\021\n\tuserCount\030\004 \001(\r\"o\n\013PartyMemb" +
      "er\022\030\n\004user\030\001 \002(\0132\n.UserModel\022\016\n\006status\030\002" +
      " \001(\t\022\016\n\006is_out\030\003 \001(\010\022\023\n\013is_updating\030\004 \001(" +
      "\010\022\021\n\tis_waited\030\005 \001(\010\"F\n\tPartyList\022\034\n\007par" +
      "ties\030\001 \003(\0132\013.PartyModel\022\033\n\006params\030\002 \001(\0132" +
      "\013.ListParamsB\022\n\016proto.responseH\002"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_PartyModel_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_PartyModel_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_PartyModel_descriptor,
              new java.lang.String[] { "Uuid", "Users", "LeaderID", "UserCount", },
              proto.response.ResParty.PartyModel.class,
              proto.response.ResParty.PartyModel.Builder.class);
          internal_static_PartyMember_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_PartyMember_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_PartyMember_descriptor,
              new java.lang.String[] { "User", "Status", "IsOut", "IsUpdating", "IsWaited", },
              proto.response.ResParty.PartyMember.class,
              proto.response.ResParty.PartyMember.Builder.class);
          internal_static_PartyList_descriptor =
            getDescriptor().getMessageTypes().get(2);
          internal_static_PartyList_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_PartyList_descriptor,
              new java.lang.String[] { "Parties", "Params", },
              proto.response.ResParty.PartyList.class,
              proto.response.ResParty.PartyList.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          proto.response.ResUser.getDescriptor(),
          proto.response.ResListbase.getDescriptor(),
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}
