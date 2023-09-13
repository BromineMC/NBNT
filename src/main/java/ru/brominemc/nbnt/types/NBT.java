/*
 * Copyright 2023 BromineMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.brominemc.nbnt.types;

import com.google.errorprone.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brominemc.nbnt.utils.NBTLimiter;
import ru.brominemc.nbnt.utils.NBTReader;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Parent interface for all NBT data types.
 *
 * @author VidTu
 * @see <a href="https://wiki.vg/NBT">NBT on wiki.vg</a>
 */
public sealed interface NBT permits PrimitiveNBT, ByteArrayNBT, StringNBT, ListNBT, CompoundNBT, IntArrayNBT, LongArrayNBT {
    /**
     * NBT serialization type for {@code null} ("End") NBT tags.
     *
     * @since 1.1.0
     */
    byte NULL_NBT_TYPE = 0;

    /**
     * Writes the NBT to the output.
     *
     * @param out Target output
     * @throws IOException On I/O exception
     */
    void write(@NotNull DataOutput out) throws IOException;

    /**
     * Reads the named NBT from the input.
     *
     * @param in      Target input
     * @param limiter Target limiter
     * @return Read name and NBT, {@code null} if read the "NBT End" type
     * @throws IOException              On I/O exception
     * @throws IllegalArgumentException If the provided NBT type is unknown, read bytes exceeded the maximum {@link NBTLimiter} length or by underlying reader
     * @throws IllegalStateException    By underlying reader
     * @throws NullPointerException     By underlying reader
     */
    @CheckReturnValue
    @Nullable
    static Map.Entry<String, NBT> readNamed(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.readUnsigned(Byte.BYTES); // Type
        byte type = in.readByte();
        if (type == NULL_NBT_TYPE) return null; // NBT End
        String name = NBTLimiter.readLimitedUTF(in, limiter);
        NBTReader reader = reader(type);
        NBT nbt = reader.read(in, limiter);
        Objects.requireNonNull(nbt, "NBT of non-zero type is null");
        return Map.entry(name, nbt);
    }

    /**
     * Reads the unnamed NBT from the input.
     *
     * @param in      Target input
     * @param limiter Target limiter
     * @return Read NBT, {@code null} if read the "NBT End" type
     * @throws IOException              On I/O exception
     * @throws IllegalArgumentException If the provided NBT type is unknown, read bytes exceeded the maximum {@link NBTLimiter} length or by underlying reader
     * @throws IllegalStateException    By underlying reader
     * @throws NullPointerException     By underlying reader
     */
    @CheckReturnValue
    @Nullable
    static NBT readUnnamed(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.readUnsigned(Byte.BYTES); // Type
        byte type = in.readByte();
        if (type == NULL_NBT_TYPE) return null; // NBT End
        limiter.readUnsigned(Short.BYTES); // Name (Length)
        int skip = in.readUnsignedShort();
        limiter.readUnsigned(skip); // Name
        in.skipBytes(skip);
        NBTReader reader = reader(type);
        NBT nbt = reader.read(in, limiter);
        Objects.requireNonNull(nbt, "NBT of non-zero type is null");
        return nbt;
    }

    /**
     * Reads the NBT from the input.
     *
     * @param in      Target input
     * @param limiter Target limiter
     * @return Read NBT, {@code null} if read the "NBT End" type
     * @throws IOException              On I/O exception
     * @throws IllegalArgumentException If the provided NBT type is unknown, read bytes exceeded the maximum {@link NBTLimiter} length or by underlying reader
     * @throws IllegalStateException    By underlying reader
     * @throws NullPointerException     By underlying reader
     * @since 1.1.0
     */
    @CheckReturnValue
    @Nullable
    static NBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.readUnsigned(Byte.BYTES); // Type
        byte type = in.readByte();
        if (type == NULL_NBT_TYPE) return null; // NBT End
        NBTReader reader = reader(type);
        NBT nbt = reader.read(in, limiter);
        Objects.requireNonNull(nbt, "NBT of non-zero type is null");
        return nbt;
    }

    /**
     * Writes the named NBT to the output.
     *
     * @param out  Target output
     * @param name Target name
     * @param nbt  Target NBT, {@code null} for the "NBT End" type
     * @throws IOException On I/O exception
     */
    static void writeNamed(@NotNull DataOutput out, @NotNull String name, @Nullable NBT nbt) throws IOException {
        out.writeByte(type(nbt));
        if (nbt == null) return;
        out.writeUTF(name);
        nbt.write(out);
    }

    /**
     * Writes the unnamed NBT to the output.
     *
     * @param out Target output
     * @param nbt Target NBT, {@code null} for the "NBT End" type
     * @throws IOException On I/O exception
     */
    static void writeUnnamed(@NotNull DataOutput out, @Nullable NBT nbt) throws IOException {
        out.writeByte(type(nbt));
        if (nbt == null) return;
        out.writeShort(0); // Name (Length)
        nbt.write(out);
    }

    /**
     * Writes the NBT to the output.
     *
     * @param out Target output
     * @param nbt Target NBT, {@code null} for the "NBT End" type
     * @throws IOException On I/O exception
     * @since 1.1.0
     */
    static void write(@NotNull DataOutput out, @Nullable NBT nbt) throws IOException {
        out.write(type(nbt));
        if (nbt == null) return;
        nbt.write(out);
    }

    /**
     * Gets the reader for the NBT type.
     *
     * @param type NBT type
     * @return Reader for the NBT type
     * @throws IllegalArgumentException If the NBT type is unknown
     */
    @Contract(pure = true)
    @NotNull
    static NBTReader reader(byte type) {
        return switch (type) {
            case NULL_NBT_TYPE -> NBTReader.NULL_READER;
            case ByteNBT.BYTE_NBT_TYPE -> ByteNBT::read;
            case ShortNBT.SHORT_NBT_TYPE -> ShortNBT::read;
            case IntNBT.INT_NBT_TYPE -> IntNBT::read;
            case LongNBT.LONG_NBT_TYPE -> LongNBT::read;
            case FloatNBT.FLOAT_NBT_TYPE -> FloatNBT::read;
            case DoubleNBT.DOUBLE_NBT_TYPE -> DoubleNBT::read;
            case ByteArrayNBT.BYTE_ARRAY_NBT_TYPE -> ByteArrayNBT::read;
            case StringNBT.STRING_NBT_TYPE -> StringNBT::read;
            case ListNBT.LIST_NBT_TYPE -> ListNBT::read;
            case CompoundNBT.COMPOUND_NBT_TYPE -> CompoundNBT::read;
            case IntArrayNBT.INT_ARRAY_NBT_TYPE -> IntArrayNBT::read;
            case LongArrayNBT.LONG_ARRAY_NBT_TYPE -> LongArrayNBT::read;
            default -> throw new IllegalArgumentException("Unknown NBT: " + type);
        };
    }

    /**
     * Gets the NBT type from the NBT instance.
     *
     * @param nbt Target NBT
     * @return Target NBT type, {@code 0} for {@code null}
     * @throws IllegalArgumentException If the NBT type is unknown
     */
    @Contract(pure = true)
    static byte type(@Nullable NBT nbt) {
        // TODO(VidTu): Java 21 - Pattern matching
        if (nbt == null) return NULL_NBT_TYPE;
        if (nbt instanceof ByteNBT) return ByteNBT.BYTE_NBT_TYPE;
        if (nbt instanceof ShortNBT) return ShortNBT.SHORT_NBT_TYPE;
        if (nbt instanceof IntNBT) return IntNBT.INT_NBT_TYPE;
        if (nbt instanceof LongNBT) return LongNBT.LONG_NBT_TYPE;
        if (nbt instanceof FloatNBT) return FloatNBT.FLOAT_NBT_TYPE;
        if (nbt instanceof DoubleNBT) return DoubleNBT.DOUBLE_NBT_TYPE;
        if (nbt instanceof ByteArrayNBT) return ByteArrayNBT.BYTE_ARRAY_NBT_TYPE;
        if (nbt instanceof StringNBT) return StringNBT.STRING_NBT_TYPE;
        if (nbt instanceof ListNBT) return ListNBT.LIST_NBT_TYPE;
        if (nbt instanceof CompoundNBT) return CompoundNBT.COMPOUND_NBT_TYPE;
        if (nbt instanceof IntArrayNBT) return IntArrayNBT.INT_ARRAY_NBT_TYPE;
        if (nbt instanceof LongArrayNBT) return LongArrayNBT.LONG_ARRAY_NBT_TYPE;
        throw new IllegalArgumentException("Unknown NBT type: " + nbt);
    }
}
