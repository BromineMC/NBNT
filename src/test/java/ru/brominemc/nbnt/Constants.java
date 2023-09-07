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

package ru.brominemc.nbnt;

import ru.brominemc.nbnt.types.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Constants for the reflection-based tests, such as which values to use in constructors of various NBT.
 *
 * @author VirtualFreeEx
 */
public final class Constants {
    /**
     * A shared random instance.
     */
    public static final Random RANDOM = new Random();

    /**
     * The maximum size for a randomly generated array.
     */
    private static final byte MAX_ARRAY_SIZE = 32;

    /**
     * The targets that the tests will be performed on.
     */
    public static final List<Class<? extends NBT>> TARGETS = List.of(
            ByteArrayNBT.class,
            ByteNBT.class,
            CompoundNBT.class,
            DoubleNBT.class,
            FloatNBT.class,
            IntArrayNBT.class,
            IntNBT.class,
            ListNBT.class,
            LongArrayNBT.class,
            LongNBT.class,
            ShortNBT.class,
            StringNBT.class
    );

    /**
     * Map of NBT type to its constructor parameter types.
     */
    public static final Map<Class<? extends NBT>, Class<?>> CONSTRUCTOR_PARAMETER_TYPE = Map.ofEntries(
            Map.entry(ByteArrayNBT.class, byte[].class),
            Map.entry(ByteNBT.class, byte.class),
            Map.entry(CompoundNBT.class, Map.class),
            Map.entry(DoubleNBT.class, double.class),
            Map.entry(FloatNBT.class, float.class),
            Map.entry(IntArrayNBT.class, int[].class),
            Map.entry(IntNBT.class, int.class),
            Map.entry(ListNBT.class, List.class),
            Map.entry(LongArrayNBT.class, long[].class),
            Map.entry(LongNBT.class, long.class),
            Map.entry(ShortNBT.class, short.class),
            Map.entry(StringNBT.class, String.class)
    );

    /**
     * Map of NBT type to its constructor parameter values.
     */
    public static final Map<Class<? extends NBT>, Object> CONSTRUCTOR_PARAMETER_VALUE = Map.ofEntries(
            Map.entry(ByteArrayNBT.class, randomByteArray()),
            Map.entry(ByteNBT.class, (byte) RANDOM.nextInt(Byte.MAX_VALUE + 1)),
            Map.entry(CompoundNBT.class, Map.ofEntries(
                    Map.entry("key", new StringNBT("value")),
                    Map.entry("another_key", new StringNBT("another_value")),
                    Map.entry("final_key", new ByteNBT((byte) RANDOM.nextInt(Byte.MAX_VALUE + 1)))
            )),
            Map.entry(DoubleNBT.class, RANDOM.nextDouble()),
            Map.entry(FloatNBT.class, RANDOM.nextFloat()),
            Map.entry(IntArrayNBT.class, RANDOM.ints(MAX_ARRAY_SIZE).toArray()),
            Map.entry(IntNBT.class, RANDOM.nextInt()),
            Map.entry(ListNBT.class, List.of(
                    new StringNBT("value"),
                    new StringNBT("another_value"),
                    new StringNBT("final_value")
            )),
            Map.entry(LongArrayNBT.class, RANDOM.longs(MAX_ARRAY_SIZE).toArray()),
            Map.entry(LongNBT.class, RANDOM.nextLong()),
            Map.entry(ShortNBT.class, (short) RANDOM.nextInt()),
            Map.entry(StringNBT.class, "Constant StringNBT parameter.")
    );

    /**
     * Returns a random byte array.
     *
     * @return A random byte array.
     */
    private static byte[] randomByteArray() {
        byte[] bytes = new byte[MAX_ARRAY_SIZE];

        RANDOM.nextBytes(bytes);

        return bytes;
    }

    /**
     * Returns a list of NBT object instances from the {@code TARGETS} classes list.
     *
     * @return A list of NBT object instances.
     */
    public static List<? extends NBT> createNbtObjects() {
        return TARGETS.stream().map(target -> {
            try {
                return target.getDeclaredConstructor(CONSTRUCTOR_PARAMETER_TYPE.get(target)).newInstance(CONSTRUCTOR_PARAMETER_VALUE.get(target));
            } catch (NoSuchMethodException exception) {
                throw new RuntimeException("Failed to find constructor of %s".formatted(target), exception);
            } catch (Throwable throwable) {
                throw new RuntimeException("Failed to create instances of %s".formatted(target), throwable);
            }
        }).toList();
    }
}