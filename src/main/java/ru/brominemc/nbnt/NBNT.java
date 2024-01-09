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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brominemc.nbnt.types.CompoundNBT;
import ru.brominemc.nbnt.types.ListNBT;
import ru.brominemc.nbnt.types.NBT;
import ru.brominemc.nbnt.types.StringNBT;

import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * NBNT (Non-Binary Named Tag) is the library for processing Minecraft NBTs. (Named Binary Tags)
 *
 * @author VidTu
 */
public final class NBNT {
    /**
     * An instance of this class cannot be created.
     *
     * @throws AssertionError Always
     */
    @Contract(value = "-> fail", pure = true)
    private NBNT() {
        throw new AssertionError("No instances.");
    }

    /**
     * Interns the strings in the NBT and children recursively.
     * Note that the strings are the only things that can be interned, since every
     * other data type is either primitive or mutable.
     * This method uses {@link String#intern()} as interner in {@link #internStrings(NBT, UnaryOperator)}.
     *
     * @param nbt      Target NBT
     * @return Provided NBT
     */
    @Contract("_ -> param1")
    public static NBT internStrings(@Nullable NBT nbt) {
        return internStrings(nbt, String::intern);
    }

    /**
     * Interns the strings in the NBT and children recursively.
     * Note that the strings are the only things that can be interned, since every
     * other data type is either primitive or mutable.
     *
     * @param nbt      Target NBT
     * @param interner Intern function
     * @return Provided NBT
     */
    @Contract("_, _ -> param1")
    public static NBT internStrings(@Nullable NBT nbt, @NotNull UnaryOperator<String> interner) {
        switch (nbt) {
            // Optimize string value.
            case StringNBT str -> str.value(interner.apply(str.value()));

            // Optimize keys and continue into values.
            case CompoundNBT map -> {
                // Iterate over entries.
                for (Map.Entry<String, NBT> entry : map.entrySet()) {
                    // Extract data.
                    String key = entry.getKey();
                    NBT value = entry.getValue();

                    // Optimize.
                    key = interner.apply(key);
                    internStrings(value, interner);

                    // Put back in.
                    map.put(key, value);
                }
            }

            // Optimize entries.
            case ListNBT list -> {
                // Iterate over entries.
                for (NBT entry : list) {
                    internStrings(entry, interner);
                }
            }

            // Do nothing for other types.
            case null, default -> {}
        }

        // Return NBT.
        return nbt;
    }
}
