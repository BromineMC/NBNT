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

/**
 * Parent interface for all primitive NBT types.
 *
 * @author VidTu
 */
public sealed interface PrimitiveNBT extends NBT permits ByteNBT, ShortNBT, IntNBT, LongNBT, FloatNBT, DoubleNBT {
    /**
     * Gets the primitive as a boolean.
     *
     * @return Primitive value
     */
    @CheckReturnValue
    boolean asBoolean();

    /**
     * Gets the primitive as a byte.
     *
     * @return Primitive value
     */
    @CheckReturnValue
    byte asByte();

    /**
     * Gets the primitive as a short.
     *
     * @return Primitive value
     */
    @CheckReturnValue
    short asShort();

    /**
     * Gets the primitive as an int.
     *
     * @return Primitive value
     */
    @CheckReturnValue
    int asInt();

    /**
     * Gets the primitive as a long.
     *
     * @return Primitive value
     */
    @CheckReturnValue
    long asLong();

    /**
     * Gets the primitive as a float.
     *
     * @return Primitive value
     */
    @CheckReturnValue
    float asFloat();

    /**
     * Gets the primitive as a double.
     *
     * @return Primitive value
     */
    @CheckReturnValue
    double asDouble();
}
