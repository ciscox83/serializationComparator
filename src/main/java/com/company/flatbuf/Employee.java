// automatically generated by the FlatBuffers compiler, do not modify

package com.company.flatbuf;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Employee extends Table {
  public static Employee getRootAsEmployee(ByteBuffer _bb) { return getRootAsEmployee(_bb, new Employee()); }
  public static Employee getRootAsEmployee(ByteBuffer _bb, Employee obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public Employee __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String name() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer nameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer nameInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public short age() { int o = __offset(6); return o != 0 ? bb.getShort(o + bb_pos) : 0; }

  public static int createEmployee(FlatBufferBuilder builder,
      int nameOffset,
      short age) {
    builder.startObject(2);
    Employee.addName(builder, nameOffset);
    Employee.addAge(builder, age);
    return Employee.endEmployee(builder);
  }

  public static void startEmployee(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(0, nameOffset, 0); }
  public static void addAge(FlatBufferBuilder builder, short age) { builder.addShort(1, age, 0); }
  public static int endEmployee(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

