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
        limiter.readUnsigned(1L);
        byte type = in.readByte();
        if (type == 0) return null;
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
        limiter.readUnsigned(1L);
        byte type = in.readByte();
        if (type == 0) return null;
        limiter.readUnsigned(2L);
        int skip = in.readUnsignedShort();
        limiter.readUnsigned(skip);
        in.skipBytes(skip);
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
        out.write(type(nbt));
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
        out.write(type(nbt));
        if (nbt == null) return;
        out.writeShort(0);
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
            case 0 -> NBTReader.NULL_READER;
            case 1 -> ByteNBT::read;
            case 2 -> ShortNBT::read;
            case 3 -> IntNBT::read;
            case 4 -> LongNBT::read;
            case 5 -> FloatNBT::read;
            case 6 -> DoubleNBT::read;
            case 7 -> ByteArrayNBT::read;
            case 8 -> StringNBT::read;
            case 9 -> ListNBT::read;
            case 10 -> CompoundNBT::read;
            case 11 -> IntArrayNBT::read;
            case 12 -> LongArrayNBT::read;
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
        if (nbt == null) return 0;
        if (nbt instanceof ByteNBT) return 1;
        if (nbt instanceof ShortNBT) return 2;
        if (nbt instanceof IntNBT) return 3;
        if (nbt instanceof LongNBT) return 4;
        if (nbt instanceof FloatNBT) return 5;
        if (nbt instanceof DoubleNBT) return 6;
        if (nbt instanceof ByteArrayNBT) return 7;
        if (nbt instanceof StringNBT) return 8;
        if (nbt instanceof ListNBT) return 9;
        if (nbt instanceof CompoundNBT) return 10;
        if (nbt instanceof IntArrayNBT) return 11;
        if (nbt instanceof LongArrayNBT) return 12;
        throw new IllegalArgumentException("Unknown NBT type: " + nbt);
    }
}
