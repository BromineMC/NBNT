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

package ru.brominemc.nbnt.tests;

import org.junit.jupiter.api.Test;
import ru.brominemc.nbnt.types.NBT;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.brominemc.nbnt.Constants.createNbtObjects;

/**
 * Test for the sanity of the {@code toString()} method.
 *
 * @author VirtualFreeEx
 */
public class ToStringSanityTest {
    @Test
    public void toStringTest() {
        createNbtObjects().forEach(v -> assertEquals(v.toString(), expectedToString(v),
                "toString() method for %s is not sane!".formatted(v.getClass().getSimpleName())));
    }

    /**
     * Generates an expected toString message based on the {@code NBT} instance.
     *
     * @param nbt An instance of {@code NBT}.
     * @return The expected toString() method result.
     */
    private String expectedToString(NBT nbt) {
        return nbt.getClass().getSimpleName() + '{' +
                String.join(",", Arrays.stream(nbt.getClass().getDeclaredFields()).map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.getName() + "=" + stringify(nbt, field);
                    } catch (Throwable throwable) {
                        throw new RuntimeException("Failed to predict toString() for %s".formatted(nbt), throwable);
                    }
                }).toList()) +
                "}";
    }

    /**
     * Returns a string representation of a field, works fine on arrays and other strings.
     *
     * @param thisReceiver The {@code this} pointer that the field will be received from.
     * @param field        The field to receive from {@code thisReceiver}
     * @return A stringified representation of the field.
     * @throws IllegalAccessException If the field couldn't be received due to privacy issues.
     */
    private String stringify(NBT thisReceiver, Field field) throws IllegalAccessException {
        Object instance = field.get(thisReceiver);
        if (instance.getClass().isArray()) {
            return switch (instance.getClass().componentType().getSimpleName()) {
                case "int" -> Arrays.toString((int[]) instance);
                case "char" -> Arrays.toString((char[]) instance);
                case "short" -> Arrays.toString((short[]) instance);
                case "byte" -> Arrays.toString((byte[]) instance);
                case "long" -> Arrays.toString((long[]) instance);
                case "float" -> Arrays.toString((float[]) instance);
                case "double" -> Arrays.toString((double[]) instance);
                case "boolean" -> Arrays.toString((boolean[]) instance);
                default -> Arrays.toString((Object[]) instance);
            };
        }
        if (instance.getClass().equals(String.class)) {
            return "'" + instance + "'";
        }
        return instance.toString();
    }
}
