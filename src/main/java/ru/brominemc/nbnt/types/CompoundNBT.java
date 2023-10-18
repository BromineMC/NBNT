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

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brominemc.nbnt.utils.NBTLimiter;
import ru.brominemc.nbnt.utils.NBTReader;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Compound (map, dictionary) NBT type.
 *
 * @author VidTu
 */
public final class CompoundNBT implements NBT, Map<String, NBT> {
    /**
     * NBT serialization type for {@link CompoundNBT} tags.
     *
     * @since 1.1.0
     */
    public static final byte COMPOUND_NBT_TYPE = 10;

    /**
     * Reader that reads {@link CompoundNBT}.
     *
     * @since 1.4.0
     */
    public static final NBTReader COMPOUND_NBT_READER = CompoundNBT::read;

    /**
     * Hold NBT value.
     */
    private Map<String, NBT> value;

    /**
     * Creates a new empty compound NBT backed by {@link HashMap#HashMap()}.
     */
    public CompoundNBT() {
        this.value = new HashMap<>();
    }

    /**
     * Creates a new compound NBT.
     *
     * @param value NBT value
     * @apiNote This will unwrap any compound NBT, i.e. this compound NBT won't be backed by another compound NBT
     */
    public CompoundNBT(@NotNull Map<String, NBT> value) {
        this.value = value instanceof CompoundNBT nbt ? nbt.value : value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    @NotNull
    public Map<String, NBT> value() {
        return value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     * @apiNote This will unwrap any compound NBT, i.e. this compound NBT won't be backed by another compound NBT
     */
    public void value(@NotNull Map<String, NBT> value) {
        this.value = value instanceof CompoundNBT nbt ? nbt.value : value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        for (Entry<String, NBT> en : value.entrySet()) {
            NBT.writeNamed(out, en.getKey(), en.getValue());
        }
        out.writeByte(0);
    }

    /**
     * Gets the boolean wrapper by the key.
     *
     * @param key Target key
     * @return Boolean wrapper or {@code null} if no primitive tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Boolean getBoolean(@NotNull String key) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asBoolean() : null;
    }

    /**
     * Gets the boolean wrapper by the key.
     *
     * @param key Target key
     * @return Boolean wrapper or {@code null} if no byte tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Boolean getBooleanStrict(@NotNull String key) {
        return value.get(key) instanceof ByteNBT nbt ? nbt.asBoolean() : null;
    }

    /**
     * Gets the boolean value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Boolean value or fallback value if no primitive tag exists by that key
     */
    @Contract(pure = true)
    public boolean getBoolean(@NotNull String key, boolean fallback) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asBoolean() : fallback;
    }

    /**
     * Gets the boolean value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Boolean value or fallback value if no byte tag exists by that key
     */
    @Contract(pure = true)
    public boolean getBooleanStrict(@NotNull String key, boolean fallback) {
        return value.get(key) instanceof ByteNBT nbt ? nbt.asBoolean() : fallback;
    }

    /**
     * Gets the byte wrapper by the key.
     *
     * @param key Target key
     * @return Byte wrapper or {@code null} if no primitive tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Byte getByte(@NotNull String key) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asByte() : null;
    }

    /**
     * Gets the byte wrapper by the key.
     *
     * @param key Target key
     * @return Byte wrapper or {@code null} if no byte tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Byte getByteStrict(@NotNull String key) {
        return value.get(key) instanceof ByteNBT nbt ? nbt.asByte() : null;
    }

    /**
     * Gets the byte value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Byte value or fallback value if no primitive tag exists by that key
     */
    @Contract(pure = true)
    public byte getByte(@NotNull String key, byte fallback) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asByte() : fallback;
    }

    /**
     * Gets the byte value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Byte value or fallback value if no byte tag exists by that key
     */
    @Contract(pure = true)
    public byte getByteStrict(@NotNull String key, byte fallback) {
        return value.get(key) instanceof ByteNBT nbt ? nbt.asByte() : fallback;
    }

    /**
     * Gets the short wrapper by the key.
     *
     * @param key Target key
     * @return Short wrapper or {@code null} if no primitive tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Short getShort(@NotNull String key) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asShort() : null;
    }

    /**
     * Gets the short wrapper by the key.
     *
     * @param key Target key
     * @return Short wrapper or {@code null} if no short tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Short getShortStrict(@NotNull String key) {
        return value.get(key) instanceof ShortNBT nbt ? nbt.asShort() : null;
    }

    /**
     * Gets the short value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Short value or fallback value if no primitive tag exists by that key
     */
    @Contract(pure = true)
    public short getShort(@NotNull String key, short fallback) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asShort() : fallback;
    }

    /**
     * Gets the short value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Short value or fallback value if no short tag exists by that key
     */
    @Contract(pure = true)
    public short getShortStrict(@NotNull String key, short fallback) {
        return value.get(key) instanceof ShortNBT nbt ? nbt.asShort() : fallback;
    }

    /**
     * Gets the int wrapper by the key.
     *
     * @param key Target key
     * @return Int wrapper or {@code null} if no primitive tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Integer getInt(@NotNull String key) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asInt() : null;
    }

    /**
     * Gets the int wrapper by the key.
     *
     * @param key Target key
     * @return Int wrapper or {@code null} if no int tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Integer getIntStrict(@NotNull String key) {
        return value.get(key) instanceof IntNBT nbt ? nbt.asInt() : null;
    }

    /**
     * Gets the int value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Int value or fallback value if no primitive tag exists by that key
     */
    @Contract(pure = true)
    public int getInt(@NotNull String key, int fallback) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asInt() : fallback;
    }

    /**
     * Gets the int value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Int value or fallback value if no int tag exists by that key
     */
    @Contract(pure = true)
    public int getIntStrict(@NotNull String key, int fallback) {
        return value.get(key) instanceof IntNBT nbt ? nbt.asInt() : fallback;
    }

    /**
     * Gets the long wrapper by the key.
     *
     * @param key Target key
     * @return Long wrapper or {@code null} if no primitive tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Long getLong(@NotNull String key) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asLong() : null;
    }

    /**
     * Gets the long wrapper by the key.
     *
     * @param key Target key
     * @return Long wrapper or {@code null} if no long tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Long getLongStrict(@NotNull String key) {
        return value.get(key) instanceof LongNBT nbt ? nbt.asLong() : null;
    }

    /**
     * Gets the long value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Long value or fallback value if no primitive tag exists by that key
     */
    @Contract(pure = true)
    public long getLong(@NotNull String key, long fallback) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asLong() : fallback;
    }

    /**
     * Gets the long value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Long value or fallback value if no long tag exists by that key
     */
    @Contract(pure = true)
    public long getLongStrict(@NotNull String key, long fallback) {
        return value.get(key) instanceof LongNBT nbt ? nbt.asLong() : fallback;
    }

    /**
     * Gets the float wrapper by the key.
     *
     * @param key Target key
     * @return Float wrapper or {@code null} if no primitive tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Float getFloat(@NotNull String key) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asFloat() : null;
    }

    /**
     * Gets the float wrapper by the key.
     *
     * @param key Target key
     * @return Float wrapper or {@code null} if no float tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Float getFloatStrict(@NotNull String key) {
        return value.get(key) instanceof FloatNBT nbt ? nbt.asFloat() : null;
    }

    /**
     * Gets the float value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Float value or fallback value if no primitive tag exists by that key
     */
    @Contract(pure = true)
    public float getFloat(@NotNull String key, float fallback) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asFloat() : fallback;
    }

    /**
     * Gets the float value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Float value or fallback value if no float tag exists by that key
     */
    @Contract(pure = true)
    public float getFloatStrict(@NotNull String key, float fallback) {
        return value.get(key) instanceof FloatNBT nbt ? nbt.asFloat() : fallback;
    }

    /**
     * Gets the double wrapper by the key.
     *
     * @param key Target key
     * @return Double wrapper or {@code null} if no primitive tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Double getDouble(@NotNull String key) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asDouble() : null;
    }

    /**
     * Gets the double wrapper by the key.
     *
     * @param key Target key
     * @return Double wrapper or {@code null} if no double tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public Double getDoubleStrict(@NotNull String key) {
        return value.get(key) instanceof DoubleNBT nbt ? nbt.asDouble() : null;
    }

    /**
     * Gets the double value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Double value or fallback value if no primitive tag exists by that key
     */
    @Contract(pure = true)
    public double getDouble(@NotNull String key, double fallback) {
        return value.get(key) instanceof PrimitiveNBT nbt ? nbt.asDouble() : fallback;
    }

    /**
     * Gets the double value by the key.
     *
     * @param key      Target key
     * @param fallback Fallback value
     * @return Double value or fallback value if no double tag exists by that key
     */
    @Contract(pure = true)
    public double getDoubleStrict(@NotNull String key, double fallback) {
        return value.get(key) instanceof DoubleNBT nbt ? nbt.asDouble() : fallback;
    }

    /**
     * Gets the byte array by the key.
     *
     * @param key Target key
     * @return Byte array or {@code null} if no byte array tag exists by that key
     */
    public byte @Nullable [] getByteArray(@NotNull String key) {
        return value.get(key) instanceof ByteArrayNBT nbt ? nbt.value() : null;
    }

    /**
     * Gets the string by the key.
     *
     * @param key Target key
     * @return String or {@code null} if no string tag exists by that key
     */
    @Nullable
    public String getString(@NotNull String key) {
        return value.get(key) instanceof StringNBT nbt ? nbt.value() : null;
    }

    /**
     * Gets the list by the key.
     *
     * @param key Target key
     * @return List or {@code null} if no list tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public ListNBT getList(@NotNull String key) {
        return value.get(key) instanceof ListNBT nbt ? nbt : null;
    }

    /**
     * Gets the list of the specified type by the key.
     *
     * @param key         Target key
     * @param type        Target list type
     * @param emptyAsNull Whether empty lists should return null
     * @return List or {@code null} if no list tag exists with the specified type exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public ListNBT getList(@NotNull String key, @NotNull Class<? extends NBT> type, boolean emptyAsNull) {
        if (!(value.get(key) instanceof ListNBT nbt)) return null;
        Class<? extends NBT> thatType = nbt.type();
        if (thatType == null) return emptyAsNull ? null : nbt;
        return thatType.equals(type) ? nbt : null;
    }

    /**
     * Gets the compound by the key.
     *
     * @param key Target key
     * @return Compound or {@code null} if no compound tag exists by that key
     */
    @Contract(pure = true)
    @Nullable
    public CompoundNBT getCompound(@NotNull String key) {
        return value.get(key) instanceof CompoundNBT nbt ? nbt : null;
    }

    /**
     * Gets the int array by the key.
     *
     * @param key Target key
     * @return Int array or {@code null} if no int array tag exists by that key
     */
    @Contract(pure = true)
    public int @Nullable [] getIntArray(@NotNull String key) {
        return value.get(key) instanceof IntArrayNBT nbt ? nbt.value() : null;
    }

    /**
     * Gets the long array by the key.
     *
     * @param key Target key
     * @return Long array or {@code null} if no long array tag exists by that key
     */
    @Contract(pure = true)
    public long @Nullable [] getLongArray(@NotNull String key) {
        return value.get(key) instanceof LongArrayNBT nbt ? nbt.value() : null;
    }

    /**
     * Puts the boolean value into the compound.
     *
     * @param key   Target key
     * @param value Target value
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putBoolean(@NotNull String key, boolean value) {
        return this.value.put(key, new ByteNBT(value));
    }

    /**
     * Puts the byte value into the compound.
     *
     * @param key   Target key
     * @param value Target value
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putByte(@NotNull String key, byte value) {
        return this.value.put(key, new ByteNBT(value));
    }

    /**
     * Puts the short value into the compound.
     *
     * @param key   Target key
     * @param value Target value
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putShort(@NotNull String key, short value) {
        return this.value.put(key, new ShortNBT(value));
    }

    /**
     * Puts the boolean value into the compound.
     *
     * @param key   Target key
     * @param value Target value
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putInt(@NotNull String key, int value) {
        return this.value.put(key, new IntNBT(value));
    }

    /**
     * Puts the long value into the compound.
     *
     * @param key   Target key
     * @param value Target value
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putLong(@NotNull String key, long value) {
        return this.value.put(key, new LongNBT(value));
    }

    /**
     * Puts the float value into the compound.
     *
     * @param key   Target key
     * @param value Target value
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putFloat(@NotNull String key, float value) {
        return this.value.put(key, new FloatNBT(value));
    }

    /**
     * Puts the double value into the compound.
     *
     * @param key   Target key
     * @param value Target value
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putDouble(@NotNull String key, double value) {
        return this.value.put(key, new DoubleNBT(value));
    }

    /**
     * Puts the byte array into the compound.
     *
     * @param key   Target key
     * @param value Target array
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putByteArray(@NotNull String key, byte @NotNull [] value) {
        return this.value.put(key, new ByteArrayNBT(value));
    }

    /**
     * Puts the string into the compound.
     *
     * @param key   Target key
     * @param value Target string
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putString(@NotNull String key, @NotNull String value) {
        return this.value.put(key, new StringNBT(value));
    }

    /**
     * Puts the list into the compound.
     *
     * @param key   Target key
     * @param value Target list
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putList(@NotNull String key, @NotNull List<NBT> value) {
        return this.value.put(key, value instanceof ListNBT nbt ? nbt : new ListNBT(value));
    }

    /**
     * Puts the compound into the compound.
     *
     * @param key   Target key
     * @param value Target compound
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putCompound(@NotNull String key, @NotNull Map<String, NBT> value) {
        return this.value.put(key, value instanceof CompoundNBT nbt ? nbt : new CompoundNBT(value));
    }

    /**
     * Puts the int array into the compound.
     *
     * @param key   Target key
     * @param value Target array
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putIntArray(@NotNull String key, int @NotNull [] value) {
        return this.value.put(key, new IntArrayNBT(value));
    }

    /**
     * Puts the long array into the compound.
     *
     * @param key   Target key
     * @param value Target array
     * @return Previous tag or {@code null} if none
     */
    @CanIgnoreReturnValue
    @Nullable
    public NBT putLongArray(@NotNull String key, long @NotNull [] value) {
        return this.value.put(key, new LongArrayNBT(value));
    }

    // Delegate methods start

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean containsKey(@NotNull Object key) {
        Objects.requireNonNull(key, "Key is null");
        return value.containsKey(key);
    }

    @Override
    public boolean containsValue(@NotNull Object value) {
        Objects.requireNonNull(value, "Value is null");
        return this.value.containsValue(value);
    }

    @Override
    public NBT get(@NotNull Object key) {
        Objects.requireNonNull(key, "Key is null");
        return value.get(key);
    }

    @Nullable
    @Override
    public NBT put(@NotNull String key, @NotNull NBT value) {
        Objects.requireNonNull(key, "Key is null");
        Objects.requireNonNull(value, "Value is null");
        return this.value.put(key, value);
    }

    @Override
    public NBT remove(@NotNull Object key) {
        Objects.requireNonNull(key, "Key is null");
        return value.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends NBT> m) {
        value.putAll(m);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return value.keySet();
    }

    @NotNull
    @Override
    public Collection<NBT> values() {
        return value.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, NBT>> entrySet() {
        return value.entrySet();
    }

    // Delegate methods end

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CompoundNBT that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "CompoundNBT{" +
                "value=" + value +
                '}';
    }

    /**
     * Reads the NBT from the input.
     *
     * @param in      Target input
     * @param limiter Target limiter
     * @return Read NBT
     * @throws IOException              On I/O exception
     * @throws IllegalArgumentException By underlying readers
     * @throws IllegalStateException    If read bytes has exceeded the maximum {@link NBTLimiter} length, the maximum depth has been reached or by underlying readers
     * @throws NullPointerException     By underlying readers
     */
    @Contract("_, _ -> new")
    @CheckReturnValue
    @NotNull
    public static CompoundNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.push();
        Map<String, NBT> map = new HashMap<>();
        while (true) {
            Map.Entry<String, NBT> pair = NBT.readNamed(in, limiter);
            if (pair == null) break;
            map.put(pair.getKey(), pair.getValue());
        }
        limiter.pop();
        return new CompoundNBT(map);
    }
}
