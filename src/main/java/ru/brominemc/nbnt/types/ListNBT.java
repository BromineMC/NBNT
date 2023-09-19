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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brominemc.nbnt.utils.NBTLimiter;
import ru.brominemc.nbnt.utils.NBTReader;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

/**
 * List NBT type.
 *
 * @author VidTu
 */
public final class ListNBT implements NBT, List<NBT> {
    /**
     * NBT serialization type for {@link ListNBT} tags.
     *
     * @since 1.1.0
     */
    public static final byte LIST_NBT_TYPE = 9;

    /**
     * Hold NBT value.
     */
    private List<NBT> value;

    /**
     * Creates a new empty list NBT backed by {@link ArrayList#ArrayList()}.
     */
    public ListNBT() {
        this.value = new ArrayList<>();
    }

    /**
     * Creates a new list NBT.
     *
     * @param value NBT value
     * @apiNote This will unwrap any list NBT, i.e. this list NBT won't be backed by another list NBT
     */
    public ListNBT(@NotNull List<NBT> value) {
        this.value = value instanceof ListNBT nbt ? nbt.value : value;
    }

    /**
     * Gets the NBT value.
     *
     * @return NBT value
     */
    @Contract(pure = true)
    @NotNull
    public List<NBT> value() {
        return value;
    }

    /**
     * Sets the NBT value.
     *
     * @param value New NBT value
     * @apiNote This will unwrap any list NBT, i.e. this list NBT won't be backed by another list NBT
     */
    public void value(@NotNull List<NBT> value) {
        this.value = value instanceof ListNBT nbt ? nbt.value : value;
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeByte(value.isEmpty() ? 0 : NBT.type(value.get(0)));
        out.writeInt(value.size());
        for (NBT nbt : value) {
            nbt.write(out);
        }
    }

    /**
     * Gets the list type.
     *
     * @return List type, {@code null} if list is empty
     */
    @Contract(pure = true)
    @Nullable
    public Class<? extends NBT> type() {
        return value.isEmpty() ? null : value.get(0).getClass();
    }

    /**
     * Validates the NBT type.
     *
     * @param nbt Target NBT
     * @throws IllegalArgumentException If the NBT type is not applicable for this list
     */
    public void validateType(@NotNull NBT nbt) {
        Class<? extends NBT> thisType = type();
        if (thisType == null) return;
        Class<? extends NBT> thatType = nbt.getClass();
        if (thisType.equals(thatType)) return;
        throw new IllegalArgumentException("Using " + thatType + " in the NBT list of " + thisType);
    }

    /**
     * Adds the boolean value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code boolean} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addBoolean(boolean value) {
        add(new ByteNBT(value));
    }

    /**
     * Adds the byte value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code byte} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addByte(byte value) {
        add(new ByteNBT(value));
    }

    /**
     * Adds the short value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code short} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addShort(short value) {
        add(new ShortNBT(value));
    }

    /**
     * Adds the int value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code int} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addInt(int value) {
        add(new IntNBT(value));
    }

    /**
     * Adds the long value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code long} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addLong(long value) {
        add(new LongNBT(value));
    }

    /**
     * Adds the float value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code float} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addFloat(float value) {
        add(new FloatNBT(value));
    }

    /**
     * Adds the double value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code double} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addDouble(float value) {
        add(new DoubleNBT(value));
    }

    /**
     * Adds the byte array value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code byte[]} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addByteArray(byte @NotNull [] value) {
        add(new ByteArrayNBT(value));
    }

    /**
     * Adds the string value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@link String} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addString(@NotNull String value) {
        add(new StringNBT(value));
    }

    /**
     * Adds the list value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@link List} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addList(@NotNull List<NBT> value) {
        add(new ListNBT(value));
    }

    /**
     * Adds the compound value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@link Map} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addCompound(@NotNull Map<String, NBT> value) {
        add(new CompoundNBT(value));
    }

    /**
     * Adds the int array value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code int[]} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addIntArray(int @NotNull [] value) {
        add(new IntArrayNBT(value));
    }

    /**
     * Adds the long array value into the list.
     *
     * @param value Target value
     * @throws IllegalArgumentException If the list doesn't support {@code long[]} elements
     * @since 1.1.0
     * @see #add(NBT)
     */
    public void addLongArray(long @NotNull [] value) {
        add(new LongArrayNBT(value));
    }

    /**
     * Removes the boolean value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeBoolean(boolean value) {
        if (!ByteNBT.class.equals(type())) return false;
        return remove(new ByteNBT(value));
    }

    /**
     * Removes the byte value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeByte(byte value) {
        if (!ByteNBT.class.equals(type())) return false;
        return remove(new ByteNBT(value));
    }

    /**
     * Removes the short value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeShort(short value) {
        if (!ShortNBT.class.equals(type())) return false;
        return remove(new ShortNBT(value));
    }

    /**
     * Removes the int value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeInt(int value) {
        if (!IntNBT.class.equals(type())) return false;
        return remove(new IntNBT(value));
    }

    /**
     * Removes the long value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeLong(long value) {
        if (!LongNBT.class.equals(type())) return false;
        return remove(new LongNBT(value));
    }

    /**
     * Removes the float value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeFloat(float value) {
        if (!FloatNBT.class.equals(type())) return false;
        return remove(new FloatNBT(value));
    }

    /**
     * Removes the double value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeDouble(double value) {
        if (!DoubleNBT.class.equals(type())) return false;
        return remove(new DoubleNBT(value));
    }

    /**
     * Removes the byte array value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeByteArray(byte @NotNull [] value) {
        if (!ByteArrayNBT.class.equals(type())) return false;
        return remove(new ByteArrayNBT(value));
    }

    /**
     * Removes the string value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeString(@NotNull String value) {
        if (!StringNBT.class.equals(type())) return false;
        return remove(new StringNBT(value));
    }

    /**
     * Removes the list value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeList(@NotNull List<NBT> value) {
        if (!ListNBT.class.equals(type())) return false;
        return remove(new ListNBT(value));
    }

    /**
     * Removes the compound value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeCompound(@NotNull Map<String, NBT> value) {
        if (!CompoundNBT.class.equals(type())) return false;
        return remove(new CompoundNBT(value));
    }

    /**
     * Removes the int array value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeIntArray(int @NotNull [] value) {
        if (!IntArrayNBT.class.equals(type())) return false;
        return remove(new IntArrayNBT(value));
    }

    /**
     * Removes the int array value from the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value has been removed
     * @since 1.1.0
     * @see #remove(Object)
     */
    public boolean removeLongArray(long @NotNull [] value) {
        if (!LongArrayNBT.class.equals(type())) return false;
        return remove(new LongArrayNBT(value));
    }

    /**
     * Checks whether the boolean value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsBoolean(boolean value) {
        if (!ByteNBT.class.equals(type())) return false;
        return contains(new ByteNBT(value));
    }

    /**
     * Checks whether the byte value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsByte(byte value) {
        if (!ByteNBT.class.equals(type())) return false;
        return contains(new ByteNBT(value));
    }

    /**
     * Checks whether the short value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsShort(short value) {
        if (!ShortNBT.class.equals(type())) return false;
        return contains(new ShortNBT(value));
    }

    /**
     * Checks whether the int value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsInt(int value) {
        if (!IntNBT.class.equals(type())) return false;
        return contains(new IntNBT(value));
    }

    /**
     * Checks whether the long value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsLong(long value) {
        if (!LongNBT.class.equals(type())) return false;
        return contains(new LongNBT(value));
    }

    /**
     * Checks whether the float value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsFloat(float value) {
        if (!FloatNBT.class.equals(type())) return false;
        return contains(new FloatNBT(value));
    }

    /**
     * Checks whether the float value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsDouble(double value) {
        if (!DoubleNBT.class.equals(type())) return false;
        return contains(new DoubleNBT(value));
    }

    /**
     * Checks whether the byte array value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsByteArray(byte @NotNull [] value) {
        if (!ByteArrayNBT.class.equals(type())) return false;
        return contains(new ByteArrayNBT(value));
    }

    /**
     * Checks whether the string value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsString(@NotNull String value) {
        if (!StringNBT.class.equals(type())) return false;
        return contains(new StringNBT(value));
    }

    /**
     * Checks whether the list value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsList(@NotNull List<NBT> value) {
        if (!ListNBT.class.equals(type())) return false;
        return contains(new ListNBT(value));
    }

    /**
     * Checks whether the compound value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsCompound(@NotNull Map<String, NBT> value) {
        if (!CompoundNBT.class.equals(type())) return false;
        return contains(new CompoundNBT(value));
    }

    /**
     * Checks whether the int array value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsIntArray(int @NotNull [] value) {
        if (!IntArrayNBT.class.equals(type())) return false;
        return contains(new IntArrayNBT(value));
    }

    /**
     * Checks whether the long array value is present in the list.
     *
     * @param value Target value
     * @return Whether the type of list is applicable for this type of value and the value is present in the list
     * @since 1.1.0
     * @see #contains(Object)
     */
    public boolean containsLongArray(long @NotNull [] value) {
        if (!LongArrayNBT.class.equals(type())) return false;
        return contains(new LongArrayNBT(value));
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
    public boolean contains(@NotNull Object o) {
        Objects.requireNonNull(o, "Entry is null");
        return value.contains(o);
    }

    @NotNull
    @Override
    public Iterator<NBT> iterator() {
        return value.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return value.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return value.toArray(a);
    }

    @Override
    public boolean add(@NotNull NBT nbt) {
        Objects.requireNonNull(nbt, "Entry is null");
        validateType(nbt);
        return value.add(nbt);
    }

    @Override
    public boolean remove(@NotNull Object o) {
        Objects.requireNonNull(o, "Entry is null");
        return value.remove(o);
    }

    @SuppressWarnings("SlowListContainsAll")
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return value.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends NBT> c) {
        c.forEach(this::validateType);
        return value.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends NBT> c) {
        c.forEach(this::validateType);
        return value.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return value.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return value.retainAll(c);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @Override
    public NBT get(int index) {
        return value.get(index);
    }

    @Override
    public NBT set(int index, @NotNull NBT element) {
        Objects.requireNonNull(element, "Entry is null");
        validateType(element);
        return value.set(index, element);
    }

    @Override
    public void add(int index, @NotNull NBT element) {
        Objects.requireNonNull(element, "Entry is null");
        validateType(element);
        value.add(index, element);
    }

    @Override
    public NBT remove(int index) {
        return value.remove(index);
    }

    @Override
    public int indexOf(@NotNull Object o) {
        Objects.requireNonNull(o, "Entry is null");
        return value.indexOf(o);
    }

    @Override
    public int lastIndexOf(@NotNull Object o) {
        Objects.requireNonNull(o, "Entry is null");
        return value.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<NBT> listIterator() {
        return value.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<NBT> listIterator(int index) {
        return value.listIterator(index);
    }

    @NotNull
    @Override
    public List<NBT> subList(int fromIndex, int toIndex) {
        return value.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<NBT> spliterator() {
        return value.spliterator();
    }

    // Delegate methods end

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ListNBT that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "ListNBT{" +
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
     * @throws IllegalArgumentException If the provided length is negative, non-zero for {@code null} ("NBT End") type or by underlying readers
     * @throws IllegalStateException    If read bytes has exceeded the maximum {@link NBTLimiter} length, the maximum depth has been reached or by underlying readers
     * @throws NullPointerException     If there's {@code null} ("NBT End") tag in the list or by underlying readers
     */
    @Contract("_, _ -> new")
    @CheckReturnValue
    @NotNull
    public static ListNBT read(@NotNull DataInput in, @NotNull NBTLimiter limiter) throws IOException {
        limiter.push();
        limiter.readUnsigned(Byte.BYTES); // Type
        byte type = in.readByte();
        limiter.readUnsigned(Integer.BYTES); // Length
        int length = in.readInt();
        if (type == 0) {
            if (length != 0) {
                throw new IllegalArgumentException("Invalid length (null-type): " + length);
            }
            return new ListNBT();
        }
        if (length == 0) {
            return new ListNBT();
        }
        if (length < 0) {
            throw new IllegalArgumentException("Invalid length: " + length);
        }
        NBTReader reader = NBT.reader(type);
        List<NBT> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            NBT nbt = reader.read(in, limiter);
            Objects.requireNonNull(nbt, "Null NBT at: " + i);
            list.add(nbt);
        }
        limiter.pop();
        return new ListNBT(list);
    }
}
