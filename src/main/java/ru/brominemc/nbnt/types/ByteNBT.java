/*
 * Copyright 2023-2024 BromineMC
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
import ru.brominemc.nbnt.utils.NBTLimiter;
import ru.brominemc.nbnt.utils.NBTReader;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Byte NBT type.
 *
 * @author VidTu
 */
public final class ByteNBT implements PrimitiveNBT {
    /**
     * NBT serialization type for {@link ByteNBT} tags.
     *
     * @since 1.1.0
     */
    public static final byte BYTE_NBT_TYPE = 1;

    /**
     * Reader that reads {@link ByteNBT}.
     *
     * @since 1.4.0
     */
    public static final NBTReader BYTE_NBT_READER = ByteNBT::read;

    /**
     * Hold NBT value.
     */
    private byte value;

    /**
     * Creates a new byte NBT.
     *
     * @param value NBT value. {@code true} is {@code 1}, {@code false} is {@code 0}
     */
    public ByteNBT(boolean value) {
        this.value = (byte) (value ? 1 : 0);
    }

    /**
     * Creates a new byte NBT.
     *
     * @param value NBT value
     */
    public ByteNBT(byte value) {
        this.value = value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    public byte value() {
        return this.value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     */
    public void value(byte value) {
        this.value = value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeByte(this.value);
    }

    @Override
    public boolean asBoolean() {
        return this.value != 0;
    }

    @Override
    public byte asByte() {
        return this.value;
    }

    @Override
    public short asShort() {
        return this.value;
    }

    @Override
    public int asInt() {
        return this.value;
    }

    @Override
    public long asLong() {
        return this.value;
    }

    @Override
    public float asFloat() {
        return this.value;
    }

    @Override
    public double asDouble() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ByteNBT that)) return false;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Byte.hashCode(this.value);
    }

    @Override
    public String toString() {
        return "ByteNBT{" +
                "value=" + this.value +
                '}';
    }

    /**
     * Reads the NBT from the input.
     *
     * @param in      Target input
     * @param limiter Target limiter
     * @return Read NBT
     * @throws IOException           On I/O exception
     * @throws IllegalStateException If read bytes has exceeded the maximum {@link NBTLimiter} length
     */
    @Contract("_, _ -> new")
    @CheckReturnValue
    @NotNull
    public static ByteNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.readUnsigned(Byte.BYTES); // Data
        return new ByteNBT(in.readByte());
    }
}
