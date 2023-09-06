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
 * Double NBT type.
 *
 * @author VidTu
 */
public final class DoubleNBT implements PrimitiveNBT {
    private double value;

    /**
     * Creates a new double NBT.
     *
     * @param value NBT value
     */
    public DoubleNBT(double value) {
        this.value = value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    public double value() {
        return value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     */
    public void value(double value) {
        this.value = value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeDouble(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DoubleNBT that)) return false;
        return Double.compare(value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return "DoubleNBT{" +
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
        return (int) value;
    }

    @Override
    public long asLong() {
        return (long) value;
    }

    @Override
    public float asFloat() {
        return (float) value;
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
    public static DoubleNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.readUnsigned(8L);
        return new DoubleNBT(in.readDouble());
    }
}
