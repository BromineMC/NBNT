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
import org.jetbrains.annotations.Nullable;
import ru.brominemc.nbnt.utils.NBTLimiter;
import ru.brominemc.nbnt.utils.NBTReader;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Double NBT type.
 *
 * @author VidTu
 * @author threefusii
 */
public final class DoubleNBT implements PrimitiveNBT {
    /**
     * NBT serialization type for {@link DoubleNBT} tags.
     *
     * @since 1.1.0
     */
    public static final byte DOUBLE_NBT_TYPE = 6;

    /**
     * Reader that reads {@link DoubleNBT}.
     *
     * @since 1.4.0
     */
    public static final NBTReader DOUBLE_NBT_READER = DoubleNBT::read;

    /**
     * Hold NBT value.
     */
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
        return this.value;
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
        out.writeDouble(this.value);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DoubleNBT that)) return false;
        return Double.compare(this.value, that.value) == 0;
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Contract(pure = true)
    @Override
    @NotNull
    public String toString() {
        return "DoubleNBT{" +
                "value=" + this.value +
                '}';
    }

    @Override
    public boolean asBoolean() {
        return this.value != 0;
    }

    @Override
    public byte asByte() {
        return (byte) this.value;
    }

    @Override
    public short asShort() {
        return (short) this.value;
    }

    @Override
    public int asInt() {
        return (int) this.value;
    }

    @Override
    public long asLong() {
        return (long) this.value;
    }

    @Override
    public float asFloat() {
        return (float) this.value;
    }

    @Override
    public double asDouble() {
        return this.value;
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
        limiter.readUnsigned(Double.BYTES); // Data
        return new DoubleNBT(in.readDouble());
    }
}
