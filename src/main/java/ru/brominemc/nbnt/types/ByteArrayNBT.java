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
import java.util.Arrays;
import java.util.Objects;

/**
 * Byte array NBT type.
 *
 * @author VidTu
 * @author threefusii
 */
public final class ByteArrayNBT implements NBT {
    /**
     * NBT serialization type for {@link ByteArrayNBT} tags.
     *
     * @since 1.1.0
     */
    public static final byte BYTE_ARRAY_NBT_TYPE = 7;

    /**
     * Reader that reads {@link ByteArrayNBT}.
     *
     * @since 1.4.0
     */
    public static final NBTReader BYTE_ARRAY_NBT_READER = ByteArrayNBT::read;

    /**
     * Empty byte array.
     *
     * @since 1.5.0
     */
    private static final byte[] EMPTY_BYTE_ARRAY = {};

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
        Objects.requireNonNull(value, "Array is null");
        this.value = value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    public byte @NotNull [] value() {
        return this.value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     */
    public void value(byte @NotNull [] value) {
        Objects.requireNonNull(value, "Array is null");
        this.value = value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        out.write(this.value);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ByteArrayNBT that)) return false;
        return Arrays.equals(this.value, that.value);
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Contract(pure = true)
    @Override
    @NotNull
    public String toString() {
        return "ByteArrayNBT{" +
                "value=" + Arrays.toString(this.value) +
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
        // Push length.
        limiter.readUnsigned(Integer.BYTES);

        // Read length.
        int length = in.readInt();

        // Empty shortcut.
        if (length == 0) return new ByteArrayNBT(EMPTY_BYTE_ARRAY);

        // Push data.
        limiter.readSigned(length);

        // Read data.
        byte[] data = new byte[length];
        in.readFully(data);
        return new ByteArrayNBT(data);
    }
}
