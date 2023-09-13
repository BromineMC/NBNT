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
import ru.brominemc.nbnt.TestConstants;
import ru.brominemc.nbnt.types.ListNBT;
import ru.brominemc.nbnt.types.PrimitiveNBT;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for whether the {@code Constants} class NBT values/types/targets
 * are valid NBT entries, as specified in the NBT specification.
 *
 * @author VirtualFreeEx
 * @author VidTu
 * @see <a href="https://wiki.vg/NBT#Specification">https://wiki.vg/NBT#Specification</a>
 */
public class TestConstantsSanityTest {
    @Test
    public void matchingLength() {
        int tags = 12;
        assertEquals(tags, TestConstants.TYPE_EXAMPLES.size());
        assertEquals(tags, TestConstants.TYPE_VALUES.size());
        assertEquals(tags, TestConstants.TYPES.size());
        assertEquals(tags, TestConstants.nbtObjects().size());
    }

    @Test
    public void matchingConstructorParameterTypes() {
        TestConstants.TYPE_VALUES.forEach((nbt, type) -> {
            // Do not test primitives.
            if (PrimitiveNBT.class.isAssignableFrom(nbt)) return;
            Object value = TestConstants.TYPE_EXAMPLES.get(nbt);
            assertTrue(type.isInstance(value),
                    "Value %s for %s does not match the declared type %s!".formatted(value, nbt, type));
        });
    }

    @Test
    public void matchingListNbtEntriesType() {
        List<?> entries = (List<?>) TestConstants.TYPE_EXAMPLES.get(ListNBT.class);
        assertEquals(1, entries.stream().map(Object::getClass).distinct().limit(2).count(), "Distinct classes as list members!");
    }
}
