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

import ru.brominemc.nbnt.utils.NBTLimiter;

/**
 * An exception thrown by {@link NBTLimiter#readSigned(long)} or {@link NBTLimiter#readUnsigned(long)} indicating a maximum NBT length has been reached.
 *
 * @author threefusii
 */
public final class LongNBTException extends NBTLimitException {
    /**
     * Creates a new exception.
     *
     * @param length    Current length
     * @param maxLength Maximum length
     */
    public LongNBTException(long length, long maxLength) {
        super("Max length reached. (" + length + " > " + maxLength + ")");
    }
}
