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
import ru.brominemc.nbnt.utils.NBTLimiter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Int NBT type.
 *
 * @author VidTu
 */
public final class IntNBT implements PrimitiveNBT {
    /**
     * NBT serialization type for {@link IntNBT} tags.
     */
    public static final byte INT_NBT_TYPE = 3;

    /**
     * Hold NBT value.
     */
    private int value;

    /**
     * Creates a new int NBT.
     *
     * @param value NBT value
     */
    public IntNBT(int value) {
        this.value = value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    public int value() {
        return value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     */
    public void value(int value) {
        this.value = value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeInt(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IntNBT that)) return false;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return "IntNBT{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean asBoolean() {
        return value != 0;
    }

    @Override
    public byte asByte() {
        return (byte) value;
    }

    @Override
    public short asShort() {
        return (short) value;
    }

    @Override
    public int asInt() {
        return value;
    }

    @Override
    public long asLong() {
        return value;
    }

    @Override
    public float asFloat() {
        return value;
    }

    @Override
    public double asDouble() {
        return value;
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
    public static IntNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.readUnsigned(Integer.BYTES); // Data
        return new IntNBT(in.readInt());
    }
}
