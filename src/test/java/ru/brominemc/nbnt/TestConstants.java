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

package ru.brominemc.nbnt;

import ru.brominemc.nbnt.types.ByteArrayNBT;
import ru.brominemc.nbnt.types.ByteNBT;
import ru.brominemc.nbnt.types.CompoundNBT;
import ru.brominemc.nbnt.types.DoubleNBT;
import ru.brominemc.nbnt.types.FloatNBT;
import ru.brominemc.nbnt.types.IntArrayNBT;
import ru.brominemc.nbnt.types.IntNBT;
import ru.brominemc.nbnt.types.ListNBT;
import ru.brominemc.nbnt.types.LongArrayNBT;
import ru.brominemc.nbnt.types.LongNBT;
import ru.brominemc.nbnt.types.NBT;
import ru.brominemc.nbnt.types.ShortNBT;
import ru.brominemc.nbnt.types.StringNBT;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

/**
 * Constants for the reflection-based tests, such as which values to use in constructors of various NBT.
 *
 * @author VirtualFreeEx
 */
public final class TestConstants {
    /**
     * A shared random instance.
     */
    public static final Random RANDOM = new Random();

    /**
     * Length for randomly-generated values.
     */
    private static final int RANDOM_LENGTH = 32;

    /**
     * The targets that the tests will be performed on.
     */
    public static final List<Class<? extends NBT>> TYPES = List.of(
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
    public static final Map<Class<? extends NBT>, Class<?>> TYPE_VALUES = ofEntries(
            entry(ByteArrayNBT.class, byte[].class),
            entry(ByteNBT.class, byte.class),
            entry(CompoundNBT.class, Map.class),
            entry(DoubleNBT.class, double.class),
            entry(FloatNBT.class, float.class),
            entry(IntArrayNBT.class, int[].class),
            entry(IntNBT.class, int.class),
            entry(ListNBT.class, List.class),
            entry(LongArrayNBT.class, long[].class),
            entry(LongNBT.class, long.class),
            entry(ShortNBT.class, short.class),
            entry(StringNBT.class, String.class)
    );

    /**
     * Map of NBT type to its constructor parameter values.
     */
    public static final Map<Class<? extends NBT>, Object> TYPE_EXAMPLES = ofEntries(
            entry(ByteArrayNBT.class, randomByteArray()),
            entry(ByteNBT.class, (byte) RANDOM.nextInt()),
            entry(CompoundNBT.class, ofEntries(
                    entry("key", new StringNBT(randomString())),
                    entry("another_key", new StringNBT(randomString())),
                    entry("final_key", new ByteNBT((byte) RANDOM.nextInt())),
                    entry("nested_list_key", new ListNBT(List.of(
                            new ByteArrayNBT(randomByteArray()),
                            new ByteArrayNBT(randomByteArray()),
                            new ByteArrayNBT(randomByteArray()),
                            new ByteArrayNBT(randomByteArray())
                    ))),
                    entry("nested_compound_key", new CompoundNBT(ofEntries(
                            entry("example_nested_key1", new ByteNBT((byte) RANDOM.nextInt())),
                            entry("example_nested_key2", new LongNBT(RANDOM.nextLong())),
                            entry("example_nested_key3", new IntArrayNBT(RANDOM.ints(RANDOM_LENGTH).toArray()))
                    )))
            )),
            entry(DoubleNBT.class, RANDOM.nextDouble()),
            entry(FloatNBT.class, RANDOM.nextFloat()),
            entry(IntArrayNBT.class, RANDOM.ints(RANDOM_LENGTH).toArray()),
            entry(IntNBT.class, RANDOM.nextInt()),
            entry(ListNBT.class, List.of(
                    new StringNBT(randomString()),
                    new StringNBT(randomString()),
                    new StringNBT(randomString())
            )),
            entry(LongArrayNBT.class, RANDOM.longs(RANDOM_LENGTH).toArray()),
            entry(LongNBT.class, RANDOM.nextLong()),
            entry(ShortNBT.class, (short) RANDOM.nextInt()),
            entry(StringNBT.class, randomString())
    );

    private static List<NBT> nbtObjects;

    /**
     * An instance of this class cannot be created.
     *
     * @throws AssertionError Always
     */
    private TestConstants() {
        throw new AssertionError("No instances.");
    }

    /**
     * Returns a random byte array.
     *
     * @return Randomly generated byte array
     */
    private static byte[] randomByteArray() {
        byte[] bytes = new byte[RANDOM_LENGTH];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    /**
     * Returns a random {@link String}.
     *
     * @return Randomly generated string
     */
    private static String randomString() {
        StringBuilder builder = new StringBuilder(RANDOM_LENGTH);
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            builder.appendCodePoint(RANDOM.nextInt(Character.MAX_CODE_POINT + 1));
        }
        return builder.toString();
    }

    /**
     * Returns a list of NBT object instances from the {@link TestConstants#TYPES} classes list with {@link TestConstants#TYPE_EXAMPLES} as a values.
     *
     * @return An immutable list of NBT object instances
     */
    public static List<NBT> nbtObjects() {
        if (nbtObjects == null) {
            List<NBT> list = new ArrayList<>(TYPES.size());
            for (Class<? extends NBT> type : TYPES) {
                try {
                    Class<?> value = TYPE_VALUES.get(type);
                    Objects.requireNonNull(type, () -> "Null value for: " + type);
                    Object example = TYPE_EXAMPLES.get(type);
                    Objects.requireNonNull(example, () -> "Null example for: " + type);
                    Constructor<? extends NBT> constructor = type.getConstructor(value);
                    NBT instance = constructor.newInstance(example);
                    list.add(instance);
                } catch (Throwable t) {
                    throw new RuntimeException("Unable to create instance: " + type, t);
                }
            }
            nbtObjects = List.copyOf(list);
        }
        return nbtObjects;
    }
}