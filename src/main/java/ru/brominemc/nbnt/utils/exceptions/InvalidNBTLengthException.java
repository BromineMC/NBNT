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

package ru.brominemc.nbnt.utils.exceptions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brominemc.nbnt.utils.NBTLimiter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

/**
 * An exception thrown by {@link NBTLimiter#readSigned(long)} when accepted a negative length argument.
 *
 * @author threefusii
 */
public sealed class InvalidNBTLengthException extends IllegalArgumentException {
    /**
     * Creates a new exception.
     */
    InvalidNBTLengthException() {
        super("Invalid NBT length. (Quick)");
    }

    /**
     * Creates a new exception.
     *
     * @param type   NBT type
     * @param length Provided length
     */
    public InvalidNBTLengthException(byte type, int length) {
        super("Invalid NBT length. (type: " + type + "; length: " + length + ")");
    }

    /**
     * Creates a new exception.
     *
     * @param length Provided length
     */
    public InvalidNBTLengthException(long length) {
        super("Invalid NBT length. (" + length + ")");
    }

    /**
     * Does nothing.
     *
     * @param ignored Ignored
     * @throws IOException Never thrown
     */
    @Contract(pure = true)
    @Serial
    private void readObject(@Nullable ObjectInputStream ignored) throws IOException {
        // NO-OP
    }

    /**
     * Does nothing.
     *
     * @param ignored Ignored
     * @throws IOException Never thrown
     */
    @Contract(pure = true)
    @Serial
    private void writeObject(@Nullable ObjectOutputStream ignored) throws IOException {
        // NO-OP
    }

    /**
     * A quick variant of {@link InvalidNBTLengthException}.
     *
     * @author threefusii
     */
    public static final class QuickInvalidNBTLengthException extends InvalidNBTLengthException {
        /**
         * Shared exception instance.
         */
        public static final QuickInvalidNBTLengthException INSTANCE = new QuickInvalidNBTLengthException();

        /**
         * Empty {@link StackTraceElement} array.
         */
        private static final StackTraceElement[] EMPTY_STACK = {};

        /**
         * Creates a new quick exception.
         *
         * @see #INSTANCE
         */
        public QuickInvalidNBTLengthException() {
            // Empty
        }

        @Contract(pure = true)
        @Override
        @NotNull
        public String getMessage() {
            return "Invalid NBT length. (Quick)";
        }

        @Contract(pure = true)
        @Override
        @NotNull
        public String getLocalizedMessage() {
            return "Invalid NBT length. (Quick)";
        }

        /**
         * Always returns null.
         *
         * @return null
         */
        @Contract(value = "-> null", pure = true)
        @SuppressWarnings("NonSynchronizedMethodOverridesSynchronizedMethod")
        @Override
        @Nullable
        public Throwable getCause() {
            return null;
        }

        /**
         * Does nothing.
         *
         * @param cause Ignored
         * @return this
         */
        @Contract(value = "_ -> this", pure = true)
        @SuppressWarnings("NonSynchronizedMethodOverridesSynchronizedMethod")
        @Override
        @NotNull
        public Throwable initCause(@Nullable Throwable cause) {
            return this;
        }

        /**
         * Always returns a constant string representation for this.
         *
         * @return A constant string
         */
        @Contract(pure = true)
        @Override
        @NotNull
        public String toString() {
            return this.getClass().getName() + ": Invalid NBT length. (Quick)";
        }

        /**
         * Does nothing.
         *
         * @return this
         */
        @Contract(value = "-> this", pure = true)
        @SuppressWarnings("NonSynchronizedMethodOverridesSynchronizedMethod")
        @Override
        @NotNull
        public Throwable fillInStackTrace() {
            return this;
        }

        /**
         * Always returns empty array.
         *
         * @return Empty array
         */
        @Contract(pure = true)
        @Override
        @NotNull
        public StackTraceElement @NotNull [] getStackTrace() {
            return EMPTY_STACK;
        }

        /**
         * Does nothing.
         *
         * @param stackTrace Ignored
         */
        @Contract(pure = true)
        @Override
        public void setStackTrace(@Nullable StackTraceElement @Nullable [] stackTrace) {
            // NO-OP
        }

        /**
         * Does nothing.
         *
         * @param ignored Ignored
         * @throws IOException Never thrown
         */
        @Contract(pure = true)
        @Serial
        private void readObject(@Nullable ObjectInputStream ignored) throws IOException {
            // NO-OP
        }

        /**
         * Does nothing.
         *
         * @param ignored Ignored
         * @throws IOException Never thrown
         */
        @Contract(pure = true)
        @Serial
        private void writeObject(@Nullable ObjectOutputStream ignored) throws IOException {
            // NO-OP
        }
    }
}
