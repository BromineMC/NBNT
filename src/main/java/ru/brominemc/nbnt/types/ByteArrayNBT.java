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
import java.util.Arrays;

/**
 * Byte array NBT type.
 *
 * @author VidTu
 */
public final class ByteArrayNBT implements NBT {
    /**
     * NBT serialization type for {@link ByteArrayNBT} tags.
     */
    public static final byte BYTE_ARRAY_NBT_TYPE = 7;

    /**
     * Hold NBT value.
     */
    private byte[] value;

    /**
     * Creates a new byte array NBT.
     *
     * @param value NBT value
     */
    public ByteArrayNBT(byte @NotNull [] value) {
        this.value = value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    public byte @NotNull [] value() {
        return value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     */
    public void value(byte @NotNull [] value) {
        this.value = value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeInt(value.length);
        out.write(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ByteArrayNBT that)) return false;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        return "ByteArrayNBT{" +
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
     * @throws IllegalArgumentException If the provided length is smaller than zero
     * @throws IllegalStateException    If read bytes has exceeded the maximum {@link NBTLimiter} length
     */
    @Contract("_, _ -> new")
    @CheckReturnValue
    @NotNull
    public static ByteArrayNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.readUnsigned(Integer.BYTES); // Length
        int length = in.readInt();
        limiter.readSigned(length); // Data
        byte[] data = new byte[length];
        in.readFully(data);
        return new ByteArrayNBT(data);
    }
}
