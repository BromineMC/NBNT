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
 * Float NBT type.
 *
 * @author VidTu
 * @author threefusii
 */
public final class FloatNBT implements PrimitiveNBT {
    /**
     * NBT serialization type for {@link FloatNBT} tags.
     *
     * @since 1.1.0
     */
    public static final byte FLOAT_NBT_TYPE = 5;

    /**
     * Reader that reads {@link FloatNBT}.
     *
     * @since 1.4.0
     */
    public static final NBTReader FLOAT_NBT_READER = FloatNBT::read;

    /**
     * Hold NBT value.
     */
    private float value;

    /**
     * Creates a new float NBT.
     *
     * @param value NBT value
     */
    public FloatNBT(float value) {
        this.value = value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    public float value() {
        return this.value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     */
    public void value(float value) {
        this.value = value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeFloat(this.value);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FloatNBT that)) return false;
        return Float.compare(this.value, that.value) == 0;
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }

    @Contract(pure = true)
    @Override
    @NotNull
    public String toString() {
        return "FloatNBT{" +
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
        return this.value;
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
    public static FloatNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.readUnsigned(Float.BYTES); // Data
        return new FloatNBT(in.readFloat());
    }
}
