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

package ru.brominemc.nbnt.tests;

import org.junit.jupiter.api.Test;
import ru.brominemc.nbnt.TestConstants;
import ru.brominemc.nbnt.types.NBT;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the sanity of the {@code toString()} method.
 *
 * @author VirtualFreeEx
 * @author VidTu
 */
public class ToStringSanityTest {
    @Test
    public void toStringTest() {
        for (NBT nbt : TestConstants.nbtObjects()) {
            assertEquals(expectedToString(nbt), nbt.toString(), () -> "Invalid toString() representation of: " + nbt.getClass());
        }
    }

    /**
     * Generates an expected {@link #toString()} message based on the {@link NBT} instance.
     *
     * @param nbt An instance of {@link NBT}
     * @return The expected {@link #toString()} method result
     */
    private String expectedToString(NBT nbt) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(nbt.getClass().getSimpleName());
            builder.append('{');
            boolean comma = false;
            for (Field field : nbt.getClass().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) continue;
                if (comma) {
                    builder.append(',');
                }
                comma = true;
                field.setAccessible(true);
                builder.append(field.getName());
                builder.append('=');
                builder.append(expectedToStringField(nbt, field));
            }
            builder.append('}');
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to make toString() representation of (" + nbt.getClass() + "): " + nbt, e);
        }
    }

    /**
     * Returns a string representation of a field as expected in the {@link #toString()}.
     *
     * @param nbt   The NBT instance to read from
     * @param field Target field
     * @return A value of the string as expected in {@link #toString()} method
     */
    private String expectedToStringField(NBT nbt, Field field) {
        try {
            Object value = field.get(nbt);
            return switch (value) {
                case boolean[] arr -> Arrays.toString(arr);
                case byte[] arr -> Arrays.toString(arr);
                case short[] arr -> Arrays.toString(arr);
                case char[] arr -> Arrays.toString(arr);
                case int[] arr -> Arrays.toString(arr);
                case long[] arr -> Arrays.toString(arr);
                case float[] arr -> Arrays.toString(arr);
                case double[] arr -> Arrays.toString(arr);
                case Object[] arr -> Arrays.deepToString(arr);
                case CharSequence seq -> "'" + seq + "'";
                default -> String.valueOf(value);
            };
        } catch (Exception e) {
            throw new RuntimeException("Unable to read field " + field + " from (" + nbt.getClass() + "): " + nbt, e);
        }
    }
}
