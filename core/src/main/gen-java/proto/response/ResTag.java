// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: res_tag.proto

package proto.response;

public final class ResTag {
  private ResTag() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class Tag extends
      com.google.protobuf.GeneratedMessage {
    // Use Tag.newBuilder() to construct.
    private Tag() {
      initFields();
    }
    private Tag(boolean noInit) {}
    
    private static final Tag defaultInstance;
    public static Tag getDefaultInstance() {
      return defaultInstance;
    }
    
    public Tag getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.response.ResTag.internal_static_Tag_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.response.ResTag.internal_static_Tag_fieldAccessorTable;
    }
    
    // required string cls = 1;
    public static final int CLS_FIELD_NUMBER = 1;
    private boolean hasCls;
    private java.lang.String cls_ = "";
    public boolean hasCls() { return hasCls; }
    public java.lang.String getCls() { return cls_; }
    
    // required string text = 2;
    public static final int TEXT_FIELD_NUMBER = 2;
    private boolean hasText;
    private java.lang.String text_ = "";
    public boolean hasText() { return hasText; }
    public java.lang.String getText() { return text_; }
    
    private void initFields() {
    }
    public static proto.response.ResTag.Tag parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.response.ResTag.Tag parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResTag.Tag parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static proto.response.ResTag.Tag parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResTag.Tag parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.response.ResTag.Tag parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static proto.response.ResTag.Tag parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static proto.response.ResTag.Tag parseDelimitedFrom(
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
    public static proto.response.ResTag.Tag parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static proto.response.ResTag.Tag parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(proto.response.ResTag.Tag prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private proto.response.ResTag.Tag result;
      
      // Construct using proto.response.ResTag.Tag.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new proto.response.ResTag.Tag();
        return builder;
      }
      
      protected proto.response.ResTag.Tag internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new proto.response.ResTag.Tag();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.response.ResTag.Tag.getDescriptor();
      }
      
      public proto.response.ResTag.Tag getDefaultInstanceForType() {
        return proto.response.ResTag.Tag.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public proto.response.ResTag.Tag build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private proto.response.ResTag.Tag buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public proto.response.ResTag.Tag buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        proto.response.ResTag.Tag returnMe = result;
        result = null;
        return returnMe;
      }
      
      
      // required string cls = 1;
      public boolean hasCls() {
        return result.hasCls();
      }
      public java.lang.String getCls() {
        return result.getCls();
      }
      public Builder setCls(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasCls = true;
        result.cls_ = value;
        return this;
      }
      public Builder clearCls() {
        result.hasCls = false;
        result.cls_ = getDefaultInstance().getCls();
        return this;
      }
      
      // required string text = 2;
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
      
      // @@protoc_insertion_point(builder_scope:Tag)
    }
    
    static {
      defaultInstance = new Tag(true);
      proto.response.ResTag.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Tag)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Tag_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Tag_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rres_tag.proto\" \n\003Tag\022\013\n\003cls\030\001 \002(\t\022\014\n\004t" +
      "ext\030\002 \002(\tB\022\n\016proto.responseH\002"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_Tag_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_Tag_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Tag_descriptor,
              new java.lang.String[] { "Cls", "Text", },
              proto.response.ResTag.Tag.class,
              proto.response.ResTag.Tag.Builder.class);
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
