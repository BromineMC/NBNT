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
        limiter.readUnsigned(1L);
        byte type = in.readByte();
        limiter.readUnsigned(4L);
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
        limiter.push();
        for (int i = 0; i < length; i++) {
            NBT nbt = reader.read(in, limiter);
            Objects.requireNonNull(nbt, "Null NBT at: " + i);
            list.add(nbt);
        }
        limiter.pop();
        return new ListNBT(list);
    }
}
