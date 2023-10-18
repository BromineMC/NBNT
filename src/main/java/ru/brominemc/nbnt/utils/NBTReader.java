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

package ru.brominemc.nbnt.utils;

import com.google.errorprone.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brominemc.nbnt.types.NBT;

import java.io.DataInput;
import java.io.IOException;

/**
 * Functional interface for reading the NBT.
 *
 * @author VidTu
 */
@FunctionalInterface
public interface NBTReader {
    /**
     * Reader that reads {@code null}.
     */
    NBTReader NULL_READER = (in, limiter) -> null;

    /**
     * Reads the NBT from the input.
     *
     * @param in      Target input
     * @param limiter Target limiter
     * @return Read NBT
     * @throws IOException On I/O exception
     */
    @CheckReturnValue
    @Nullable
    NBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException;
}
