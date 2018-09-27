// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: svga.proto
package com.mar.lib.svgaplayer.proto;

import android.os.Parcelable;
import com.squareup.wire.AndroidMessage;
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class MovieParams extends AndroidMessage<MovieParams, MovieParams.Builder> {
  public static final ProtoAdapter<MovieParams> ADAPTER = new ProtoAdapter_MovieParams();

  public static final Parcelable.Creator<MovieParams> CREATOR = AndroidMessage.newCreator(ADAPTER);

  private static final long serialVersionUID = 0L;

  public static final Float DEFAULT_VIEWBOXWIDTH = 0.0f;

  public static final Float DEFAULT_VIEWBOXHEIGHT = 0.0f;

  public static final Integer DEFAULT_FPS = 0;

  public static final Integer DEFAULT_FRAMES = 0;

  /**
   * 画布宽
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  public final Float viewBoxWidth;

  /**
   * 画布高
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  public final Float viewBoxHeight;

  /**
   * 动画每秒播放帧数，合法值是 [1, 2, 3, 5, 6, 10, 12, 15, 20, 30, 60] 中的任意一个。
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer fps;

  /**
   * 动画总帧数
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer frames;

  public MovieParams(Float viewBoxWidth, Float viewBoxHeight, Integer fps, Integer frames) {
    this(viewBoxWidth, viewBoxHeight, fps, frames, ByteString.EMPTY);
  }

  public MovieParams(Float viewBoxWidth, Float viewBoxHeight, Integer fps, Integer frames,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.viewBoxWidth = viewBoxWidth;
    this.viewBoxHeight = viewBoxHeight;
    this.fps = fps;
    this.frames = frames;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.viewBoxWidth = viewBoxWidth;
    builder.viewBoxHeight = viewBoxHeight;
    builder.fps = fps;
    builder.frames = frames;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MovieParams)) return false;
    MovieParams o = (MovieParams) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(viewBoxWidth, o.viewBoxWidth)
        && Internal.equals(viewBoxHeight, o.viewBoxHeight)
        && Internal.equals(fps, o.fps)
        && Internal.equals(frames, o.frames);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (viewBoxWidth != null ? viewBoxWidth.hashCode() : 0);
      result = result * 37 + (viewBoxHeight != null ? viewBoxHeight.hashCode() : 0);
      result = result * 37 + (fps != null ? fps.hashCode() : 0);
      result = result * 37 + (frames != null ? frames.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (viewBoxWidth != null) builder.append(", viewBoxWidth=").append(viewBoxWidth);
    if (viewBoxHeight != null) builder.append(", viewBoxHeight=").append(viewBoxHeight);
    if (fps != null) builder.append(", fps=").append(fps);
    if (frames != null) builder.append(", frames=").append(frames);
    return builder.replace(0, 2, "MovieParams{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<MovieParams, Builder> {
    public Float viewBoxWidth;

    public Float viewBoxHeight;

    public Integer fps;

    public Integer frames;

    public Builder() {
    }

    /**
     * 画布宽
     */
    public Builder viewBoxWidth(Float viewBoxWidth) {
      this.viewBoxWidth = viewBoxWidth;
      return this;
    }

    /**
     * 画布高
     */
    public Builder viewBoxHeight(Float viewBoxHeight) {
      this.viewBoxHeight = viewBoxHeight;
      return this;
    }

    /**
     * 动画每秒播放帧数，合法值是 [1, 2, 3, 5, 6, 10, 12, 15, 20, 30, 60] 中的任意一个。
     */
    public Builder fps(Integer fps) {
      this.fps = fps;
      return this;
    }

    /**
     * 动画总帧数
     */
    public Builder frames(Integer frames) {
      this.frames = frames;
      return this;
    }

    @Override
    public MovieParams build() {
      return new MovieParams(viewBoxWidth, viewBoxHeight, fps, frames, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MovieParams extends ProtoAdapter<MovieParams> {
    public ProtoAdapter_MovieParams() {
      super(FieldEncoding.LENGTH_DELIMITED, MovieParams.class);
    }

    @Override
    public int encodedSize(MovieParams value) {
      return ProtoAdapter.FLOAT.encodedSizeWithTag(1, value.viewBoxWidth)
          + ProtoAdapter.FLOAT.encodedSizeWithTag(2, value.viewBoxHeight)
          + ProtoAdapter.INT32.encodedSizeWithTag(3, value.fps)
          + ProtoAdapter.INT32.encodedSizeWithTag(4, value.frames)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MovieParams value) throws IOException {
      ProtoAdapter.FLOAT.encodeWithTag(writer, 1, value.viewBoxWidth);
      ProtoAdapter.FLOAT.encodeWithTag(writer, 2, value.viewBoxHeight);
      ProtoAdapter.INT32.encodeWithTag(writer, 3, value.fps);
      ProtoAdapter.INT32.encodeWithTag(writer, 4, value.frames);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MovieParams decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.viewBoxWidth(ProtoAdapter.FLOAT.decode(reader)); break;
          case 2: builder.viewBoxHeight(ProtoAdapter.FLOAT.decode(reader)); break;
          case 3: builder.fps(ProtoAdapter.INT32.decode(reader)); break;
          case 4: builder.frames(ProtoAdapter.INT32.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public MovieParams redact(MovieParams value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
