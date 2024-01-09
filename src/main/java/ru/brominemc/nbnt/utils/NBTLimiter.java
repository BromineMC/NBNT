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

package ru.brominemc.nbnt.utils;

import com.google.errorprone.annotations.CheckReturnValue;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brominemc.nbnt.types.LongArrayNBT;
import ru.brominemc.nbnt.types.NBT;
import ru.brominemc.nbnt.utils.exceptions.InvalidNBTLengthException;
import ru.brominemc.nbnt.utils.exceptions.LongNBTException;
import ru.brominemc.nbnt.utils.exceptions.NBTOverflowException;
import ru.brominemc.nbnt.utils.exceptions.NBTUnderflowException;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Depth and length NBT limiter for reading.
 *
 * @author VidTu
 * @author threefusii
 * @apiNote NBT limiters are not thread-safe!
 */
public sealed class NBTLimiter implements AutoCloseable {
    /**
     * Limiter without limits.
     *
     * @see #unlimited()
     * @deprecated Will be removed in 2.0.0, use {@link #unlimited()}
     */
    @Deprecated(since = "1.2.1", forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public static final NBTLimiter UNLIMITED = new NBTUnlimiter();

    /**
     * Simple empty string.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Maximum NBT length in bytes.
     */
    private final long maxLength;

    /**
     * Maximum NBT depth.
     */
    private final int maxDepth;

    /**
     * Whether the {@link NBT#readUnnamed(DataInput, NBTLimiter)} should require names to be empty.
     */
    private final boolean strictEmptyNames;

    /**
     * Whether the {@link LongArrayNBT} should be readable. Minecraft versions prior to {@code 1.12} (i.e. {@code 1.11.2} and below) don't support long array NBT tags.
     */
    private final boolean longArrays;

    /**
     * Whether the exceptions thrown by this limiter should be shared and stackless.
     */
    private final boolean quickExceptions;

    /**
     * Read NBT bytes.
     */
    private long length;

    /**
     * Current stack position.
     */
    private int depth;

    /**
     * Creates a new NBT limiter.
     * <p>
     * This is equal to calling {@link #NBTLimiter(long, int, boolean, boolean)} with {@code strictEmptyNames} set to {@code false} and {@code longArrays} set to {@code true}.
     *
     * @param maxLength Maximum NBT length in bytes
     * @param maxDepth  Maximum NBT depth
     * @deprecated Will be removed in 2.0.0, use {@link #NBTLimiter(long, int, boolean, boolean, boolean)}
     */
    @Deprecated(since = "1.5.0", forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public NBTLimiter(long maxLength, int maxDepth) {
        this(maxLength, maxDepth, false, true);
    }

    /**
     * Creates a new NBT limiter.
     * <p>
     * This is equal to calling {@link #NBTLimiter(long, int, boolean, boolean, boolean)} with {@code quickExceptions} set to {@code false}
     *
     * @param maxLength        Maximum NBT length in bytes
     * @param maxDepth         Maximum NBT depth
     * @param strictEmptyNames Whether the {@link NBT#readUnnamed(DataInput, NBTLimiter)} should require names to be empty
     * @param longArrays       Whether the {@link LongArrayNBT} should be readable. Minecraft versions prior to {@code 1.12} (i.e. {@code 1.11.2} and below) don't support long array NBT tags
     * @since 1.1.0
     * @deprecated Will be removed in 2.0.0, use {@link #NBTLimiter(long, int, boolean, boolean, boolean)}
     */
    @Deprecated(since = "1.5.0", forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public NBTLimiter(long maxLength, int maxDepth, boolean strictEmptyNames, boolean longArrays) {
        this(maxLength, maxDepth, strictEmptyNames, longArrays, false);
    }

    /**
     * Creates a new NBT limiter.
     *
     * @param maxLength        Maximum NBT length in bytes
     * @param maxDepth         Maximum NBT depth
     * @param strictEmptyNames Whether the {@link NBT#readUnnamed(DataInput, NBTLimiter)} should require names to be empty
     * @param longArrays       Whether the {@link LongArrayNBT} should be readable. Minecraft versions prior to {@code 1.12} (i.e. {@code 1.11.2} and below) don't support long array NBT tags
     * @param quickExceptions  Whether the exceptions thrown by this limiter should be shared and stackless
     * @since 1.5.0
     */
    public NBTLimiter(long maxLength, int maxDepth, boolean strictEmptyNames, boolean longArrays, boolean quickExceptions) {
        this.maxLength = maxLength;
        this.maxDepth = maxDepth;
        this.strictEmptyNames = strictEmptyNames;
        this.longArrays = longArrays;
        this.quickExceptions = quickExceptions;
    }

    /**
     * Gets the maximum allowed length of this limiter.
     *
     * @return Maximum NBT length in bytes
     * @since 1.1.0
     */
    @Contract(pure = true)
    public long maxLength() {
        return this.maxLength;
    }

    /**
     * Gets the maximum allowed depth of this limiter.
     *
     * @return Maximum NBT depth
     * @since 1.1.0
     */
    @Contract(pure = true)
    public int maxDepth() {
        return this.maxDepth;
    }

    /**
     * Gets whether the {@link NBT#readUnnamed(DataInput, NBTLimiter)} should require names to be empty.
     *
     * @return Whether the unnamed NBTs are required to have empty names
     * @since 1.1.0
     */
    @Contract(pure = true)
    public boolean strictEmptyNames() {
        return this.strictEmptyNames;
    }

    /**
     * Gets whether the {@link LongArrayNBT} should be readable.
     *
     * @return Whether the {@link LongArrayNBT} is readable
     * @apiNote Minecraft versions prior to {@code 1.12} (i.e. {@code 1.11.2} and below) don't support long array NBT tags
     */
    @Contract(pure = true)
    public boolean longArrays() {
        return this.longArrays;
    }

    /**
     * Whether the exceptions thrown by this limiter should be shared and stackless
     *
     * @return Whether the exceptions thrown by this limiter are shared and stackless
     */
    @Contract(pure = true)
    public boolean quickExceptions() {
        return this.quickExceptions;
    }

    /**
     * Gets the length read by this limiter.
     *
     * @return Read NBT bytes
     * @since 1.1.0
     */
    @Contract(pure = true)
    public long length() {
        return this.length;
    }

    /**
     * Gets the current depth of this limiter.
     *
     * @return Current stack position
     * @since 1.1.0
     */
    @Contract(pure = true)
    public int depth() {
        return this.depth;
    }

    /**
     * Adds the read bytes to the total read length.
     * <p>
     * This method checks for negative amount of bytes. If checking is not required, use {@link #readUnsigned(long)}.
     *
     * @param bytes Read bytes
     * @throws InvalidNBTLengthException If the amount of read bytes is smaller than zero
     * @throws LongNBTException           If read bytes has exceeded the maximum length
     * @throws ArithmeticException        If amount of read bytes reached {@link Long#MAX_VALUE}
     */
    public void readSigned(long bytes) {
        if (bytes < 0) {
            throw this.quickExceptions ? InvalidNBTLengthException.QuickInvalidNBTLengthException.INSTANCE : new InvalidNBTLengthException(bytes);
        }
        bytes = Math.addExact(this.length, bytes);
        if (bytes > this.maxLength) {
            throw this.quickExceptions ? LongNBTException.QuickLongNBTException.INSTANCE : new LongNBTException(bytes, this.maxLength);
        }
        this.length = bytes;
    }

    /**
     * Adds the read bytes to the total read length.
     * <p>
     * This method does <b>NOT</b> check for negative amount of bytes. If checking is required, use {@link #readSigned(long)}.
     *
     * @param bytes Read bytes
     * @throws LongNBTException    If read bytes exceeded the maximum length
     * @throws ArithmeticException If amount of read bytes reached {@link Long#MAX_VALUE}
     */
    public void readUnsigned(long bytes) {
        bytes = Math.addExact(this.length, bytes);
        if (bytes > this.maxLength) {
            throw this.quickExceptions ? LongNBTException.QuickLongNBTException.INSTANCE : new LongNBTException(bytes, this.maxLength);
        }
        this.length = bytes;
    }

    /**
     * Pushes the depth stack. (Increases the depth by one)
     *
     * @throws NBTOverflowException If the current depth exceeds the maximum depth
     */
    public void push() {
        if (this.depth >= this.maxDepth) {
            throw this.quickExceptions ? NBTOverflowException.QuickNBTOverflowException.INSTANCE : new NBTOverflowException(this.depth);
        }
        this.depth++;
    }

    /**
     * Pops the depth stack. (Decreases the depth by one)
     *
     * @throws NBTUnderflowException If the current depth is smaller than zero
     */
    public void pop(){
        if (this.depth <= 0) {
            throw this.quickExceptions ? NBTUnderflowException.QuickNBTUnderflowException.INSTANCE : new NBTUnderflowException(this.depth);
        }
        this.depth--;
    }

    /**
     * Resets the NBT limiter for reusing.
     *
     * @apiNote It is not required to call this method like in many {@link AutoCloseable} implementations. This method should be only called if this instance is going to be reused.
     * @see #close()
     * @since 1.2.0
     */
    public void reset() {
        this.length = 0L;
        this.depth = 0;
    }

    /**
     * Resets the NBT limiter for reusing.
     *
     * @apiNote It is not required to call this method like in many {@link AutoCloseable} implementations. This method should be only called if this instance is going to be reused.
     * @see #reset()
     * @since 1.2.0
     */
    @Override
    public void close() {
        this.length = 0L;
        this.depth = 0;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || !obj.getClass().equals(NBTLimiter.class)) return false;
        NBTLimiter that = (NBTLimiter) obj; // Manual casting due to NBTUnlimiter
        return this.maxLength == that.maxLength && this.maxDepth == that.maxDepth &&
                this.strictEmptyNames == that.strictEmptyNames && this.longArrays == that.longArrays &&
                this.quickExceptions == that.quickExceptions && this.length == that.length &&
                this.depth == that.depth;
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return Objects.hash(this.maxLength, this.maxDepth, this.strictEmptyNames,
                this.longArrays, this.quickExceptions, this.length, this.depth);
    }

    @Contract(pure = true)
    @Override
    @NotNull
    public String toString() {
        return "NBTLimiter{" +
                "maxLength=" + this.maxLength +
                ", maxDepth=" + this.maxDepth +
                ", strictEmptyNames=" + this.strictEmptyNames +
                ", longArrays=" + this.longArrays +
                ", quickExceptions=" + this.quickExceptions +
                ", length=" + this.length +
                ", depth=" + this.depth +
                '}';
    }

    /**
     * Reads the modified UTF-8 from the input with support for limiting before reading.
     *
     * @param in      Target input
     * @param limiter Target limiter
     * @return Read string
     * @throws IOException           On I/O exception
     * @throws IllegalStateException If read bytes has exceeded the {@link #maxLength()}
     * @see DataInputStream#readUTF(DataInput)
     */
    @Contract("_, _ -> new")
    @CheckReturnValue
    @NotNull
    public static String readLimitedUTF(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        // Push length.
        limiter.readUnsigned(Short.BYTES);

        // Read length.
        int length = in.readUnsignedShort();

        // Empty shortcut.
        if (length == 0) return EMPTY_STRING;

        // Push data.
        limiter.readUnsigned(length);

        // Read data.
        byte[] data = new byte[length + 2];
        data[0] = (byte) (0xff & (length >> 8));
        data[1] = (byte) (0xff & length);
        in.readFully(data, 2, length);
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
             DataInputStream innerIn = new DataInputStream(byteIn)) {
            String line = innerIn.readUTF();
            if (innerIn.available() > 0) {
                throw new IOException("Leftover: " + innerIn.available());
            }
            return line;
        }
    }

    /**
     * Returns {@link #UNLIMITED}.
     *
     * @return Unlimited NBT limiters
     */
    @Contract(pure = true)
    @NotNull
    public static NBTLimiter unlimited() {
        return UNLIMITED;
    }

    /**
     * Returns protocol limiter analogous to current vanilla protocol implementation.
     *
     * @return Limiter analogous to vanilla protocol implementation
     * @see #vanillaLength()
     * @see #vanillaDepth()
     */
    @Contract(value = "-> new", pure = true)
    @NotNull
    public static NBTLimiter vanillaProtocol() {
        return new NBTLimiter(0x200000L, 512);
    }

    /**
     * Returns the maximum vanilla NBT length.
     *
     * @return Maximum vanilla NBT limiter length
     * @implNote Current value: {@code 2097152} ({@code 0x200000})
     */
    @Contract(pure = true)
    public static long vanillaLength() {
        return 0x200000L;
    }

    /**
     * Returns the maximum vanilla NBT depth.
     *
     * @return Maximum vanilla NBT limiter depth
     * @implNote Current value: {@code 512}
     */
    @Contract(pure = true)
    public static int vanillaDepth() {
        return 512;
    }

    /**
     * NBT limiter without limits.
     *
     * @author VidTu
     */
    private static final class NBTUnlimiter extends NBTLimiter {
        /**
         * Creates a new NBT limiter without limits.
         *
         * @see #unlimited()
         */
        private NBTUnlimiter() {
            super(Long.MAX_VALUE, Integer.MAX_VALUE, false, true, false);
        }

        /**
         * Always returns {@link Long#MAX_VALUE}.
         *
         * @return {@link Long#MAX_VALUE}
         * @since 1.1.0
         */
        @Contract(pure = true)
        @Override
        public long maxLength() {
            return Long.MAX_VALUE;
        }

        /**
         * Always returns {@link Integer#MAX_VALUE}.
         *
         * @return {@link Integer#MAX_VALUE}
         * @since 1.1.0
         */
        @Contract(pure = true)
        @Override
        public int maxDepth() {
            return Integer.MAX_VALUE;
        }

        /**
         * Always returns {@code 0}.
         *
         * @return Zero
         * @since 1.1.0
         */
        @Contract(pure = true)
        @Override
        public long length() {
            return 0L;
        }

        /**
         * Always returns {@code 0}.
         *
         * @return Zero
         * @since 1.1.0
         */
        @Contract(pure = true)
        @Override
        public int depth() {
            return 0;
        }

        /**
         * Always returns {@code false}.
         *
         * @return {@code false}
         * @since 1.1.0
         */
        @Contract(value = "-> false", pure = true)
        @Override
        public boolean strictEmptyNames() {
            return false;
        }

        /**
         * Always returns {@code true}.
         *
         * @return {@code true}
         * @since 1.1.0
         */
        @Contract(value = "-> true", pure = true)
        @Override
        public boolean longArrays() {
            return true;
        }

        /**
         * Always returns {@code false}.
         *
         * @return {@code false}
         * @since 1.1.0
         */
        @Contract(value = "-> false", pure = true)
        @Override
        public boolean quickExceptions() {
            return false;
        }

        /**
         * Does nothing.
         *
         * @param bytes Ignored
         */
        @Contract(pure = true)
        @Override
        public void readSigned(long bytes) {
            // NO-OP
        }

        /**
         * Does nothing.
         *
         * @param bytes Ignored
         */
        @Contract(pure = true)
        @Override
        public void readUnsigned(long bytes) {
            // NO-OP
        }

        /**
         * Does nothing.
         */
        @Contract(pure = true)
        @Override
        public void push() {
            // NO-OP
        }

        /**
         * Does nothing.
         */
        @Contract(pure = true)
        @Override
        public void pop() {
            // NO-OP
        }

        /**
         * Does nothing.
         *
         * @since 1.2.0
         */
        @Contract(pure = true)
        @Override
        public void reset() {
            // NO-OP
        }

        /**
         * Does nothing.
         *
         * @since 1.2.0
         */
        @Contract(pure = true)
        @Override
        public void close() {
            // NO-OP
        }

        @Contract(value = "null -> false", pure = true)
        @Override
        public boolean equals(@Nullable Object obj) {
            return obj instanceof NBTUnlimiter;
        }

        @Contract(pure = true)
        @Override
        public int hashCode() {
            return 109810637; // Arbitrary prime
        }

        @Contract(pure = true)
        @Override
        @NotNull
        public String toString() {
            return "NBTUnlimiter{}";
        }
    }
}
