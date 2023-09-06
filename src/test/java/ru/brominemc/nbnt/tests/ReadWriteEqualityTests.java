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
import ru.brominemc.nbnt.utils.NBTLimiter;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.brominemc.nbnt.Constants.*;

/**
 * Test for read-write equality on different NBT types.
 *
 * @author VirtualFreeEx
 */
public final class ReadWriteEqualityTests {
    @Test
    public void equalityTest() {
        createNbtObjects().forEach(v -> {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(baos)) {
                NBT.writeUnnamed(out, v);
                DataInput in = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
                assertEquals(v, NBT.readUnnamed(in, NBTLimiter.vanillaProtocol()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to write/read NBT %s into a stream.".formatted(v), e);
            }
        });
    }
}
