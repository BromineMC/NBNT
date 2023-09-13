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
 * String NBT type.
 *
 * @author VidTu
 */
public final class StringNBT implements NBT {
    /**
     * NBT serialization type for {@link StringNBT} tags.
     *
     * @since 1.1.0
     */
    public static final byte STRING_NBT_TYPE = 8;

    /**
     * Hold NBT value.
     */
    private String value;

    /**
     * Creates a new string NBT.
     *
     * @param value NBT value
     */
    public StringNBT(@NotNull String value) {
        this.value = value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    @NotNull
    public String value() {
        return value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     */
    public void value(@NotNull String value) {
        this.value = value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeUTF(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StringNBT that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "StringNBT{" +
                "value='" + value + '\'' +
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
    public static StringNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        return new StringNBT(NBTLimiter.readLimitedUTF(in, limiter));
    }
}
