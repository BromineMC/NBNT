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
import java.util.Arrays;

/**
 * Long array NBT type.
 *
 * @author VidTu
 */
public final class LongArrayNBT implements NBT {
    /**
     * NBT serialization type for {@link LongArrayNBT} tags.
     *
     * @since 1.1.0
     */
    public static final byte LONG_ARRAY_NBT_TYPE = 12;

    /**
     * Reader that reads {@link LongArrayNBT}.
     *
     * @since 1.4.0
     */
    public static final NBTReader LONG_ARRAY_NBT_READER = LongArrayNBT::read;

    /**
     * Hold NBT value.
     */
    private long[] value;

    /**
     * Creates a new long array NBT.
     *
     * @param value NBT value
     */
    public LongArrayNBT(long @NotNull [] value) {
        this.value = value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    public long @NotNull [] value() {
        return value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     */
    public void value(long @NotNull [] value) {
        this.value = value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeInt(value.length);
        for (long l : value) {
            out.writeLong(l);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LongArrayNBT that)) return false;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        return "LongArrayNBT{" +
                "value=" + Arrays.toString(value) +
                '}';
    }

    /**
     * Reads the NBT from the input.
     *
     * @param in      Target input
     * @param limiter Target limiter
     * @return Read NBT
     * @throws IOException              On I/O exception
     * @throws IllegalArgumentException If the provided length is smaller than zero or reading long arrays is prohibited by the limiter
     * @throws IllegalStateException    If read bytes has exceeded the maximum {@link NBTLimiter} length
     */
    @Contract("_, _ -> new")
    @CheckReturnValue
    @NotNull
    public static LongArrayNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        if (!limiter.longArrays()) {
            throw new IllegalArgumentException("Tried to read LongArrayNBT by reader that prohibits reading it: " + limiter);
        }
        limiter.readUnsigned(Integer.BYTES); // Length
        int length = in.readInt();
        limiter.readSigned((long) length * Long.BYTES); // Data
        long[] data = new long[length];
        for (int i = 0; i < length; i++) {
            data[i] = in.readLong();
        }
        return new LongArrayNBT(data);
    }
}
