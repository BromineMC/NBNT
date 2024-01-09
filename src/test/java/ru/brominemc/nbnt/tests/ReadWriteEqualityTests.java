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
import ru.brominemc.nbnt.utils.NBTLimiter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for read-write equality on different NBT types.
 *
 * @author VirtualFreeEx
 * @author VidTu
 */
public final class ReadWriteEqualityTests {
    @Test
    public void testNamed() {
        String name = "example_tag_name";
        for (NBT nbt : TestConstants.nbtObjects()) {
            try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(byteOut)) {
                assertDoesNotThrow(() -> NBT.writeNamed(out, name, nbt), () -> "Named writer for " + nbt.getClass() + " wasn't able to write: " + nbt);
                try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(byteOut.toByteArray()))) {
                    Map.Entry<String, NBT> entry = assertDoesNotThrow(() -> NBT.readNamed(in, NBTLimiter.unlimited()), () -> "Named reader for " + nbt.getClass() + " wasn't able to read: " + nbt);
                    assertNotNull(entry, () -> "Named reader for " + nbt.getClass() + " returned null");
                    String readName = entry.getKey();
                    assertEquals(name, readName, () -> "Named reader for " + nbt.getClass() + " returned invalid name: " + entry);
                    NBT readNbt = entry.getValue();
                    assertEquals(nbt, readNbt, () -> "Named reader for " + nbt.getClass() + " returned invalid NBT: " + entry);
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to read/write named NBT (" + nbt.getClass() + "): " + nbt, e);
            }
        }
    }

    @Test
    public void testUnnamed() {
        for (NBT nbt : TestConstants.nbtObjects()) {
            try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(byteOut)) {
                assertDoesNotThrow(() -> NBT.writeUnnamed(out, nbt), () -> "Unnamed writer for " + nbt.getClass() + " wasn't able to write: " + nbt);
                try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(byteOut.toByteArray()))) {
                    NBT readNbt = assertDoesNotThrow(() -> NBT.readUnnamed(in, NBTLimiter.unlimited()), () -> "Unnamed reader for " + nbt.getClass() + " wasn't able to read: " + nbt);
                    assertNotNull(readNbt, () -> "Unnamed reader for " + nbt.getClass() + " returned null");
                    assertEquals(nbt, readNbt, () -> "Unnamed reader for " + nbt.getClass() + " returned invalid NBT: " + readNbt);
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to read/write unnamed NBT (" + nbt.getClass() + "): " + nbt, e);
            }
        }
    }

    @Test
    public void testPlain() {
        for (NBT nbt : TestConstants.nbtObjects()) {
            try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(byteOut)) {
                assertDoesNotThrow(() -> NBT.write(out, nbt), () -> "Plain writer for " + nbt.getClass() + " wasn't able to write: " + nbt);
                try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(byteOut.toByteArray()))) {
                    NBT readNbt = assertDoesNotThrow(() -> NBT.read(in, NBTLimiter.unlimited()), () -> "Plain reader for " + nbt.getClass() + " wasn't able to read: " + nbt);
                    assertNotNull(readNbt, () -> "Plain reader for " + nbt.getClass() + " returned null");
                    assertEquals(nbt, readNbt, () -> "Plain reader for " + nbt.getClass() + " returned invalid NBT: " + readNbt);
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to read/write plain NBT (" + nbt.getClass() + "): " + nbt, e);
            }
        }
    }
}
