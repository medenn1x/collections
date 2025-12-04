package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("SuspiciousMethodCalls")
class PrimitiveCollectionsTest {
    @Test
    void doubleSpliterator__noCharacteristicsSpecified__returnedSpliteratorIsSizedAndSubsized() {
        var collection = mock(PrimitiveCollection.OfDouble.class);

        var spliterator = PrimitiveCollections.doubleSpliterator(collection, 0);
        var characteristics = spliterator.characteristics();

        assertThat(characteristics)
                .isEqualTo(Spliterator.SIZED | Spliterator.SUBSIZED);
    }

    @Test
    void doubleSpliterator__concurrentSpecified__returnedSpliteratorIsNotSizedOrSubsized() {
        var collection = mock(PrimitiveCollection.OfDouble.class);

        var spliterator = PrimitiveCollections.doubleSpliterator(collection, Spliterator.CONCURRENT);
        var characteristics = spliterator.characteristics();

        assertThat(characteristics).isEqualTo(Spliterator.CONCURRENT);
    }

    @Test
    void doubleSpliterator__always__returnedSpliteratorIsLateBinding() {
        var collection = mock(PrimitiveCollection.OfDouble.class);

        PrimitiveCollections.doubleSpliterator(collection, 0);

        verifyNoMoreInteractions(collection);
    }

    @Test
    void equals__setVsNonSet__returnsFalse() {
        var set = mock(PrimitiveSet.class);
        var collection = mock(Collection.class);

        var result = PrimitiveCollections.equals(set, collection);

        assertThat(result).isFalse();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void equals__setVsSelf__returnsTrue() {
        var set = mock(PrimitiveSet.class);

        var result = PrimitiveCollections.equals(set, set);

        assertThat(result).isTrue();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__emptySetVsEmptySet__returnsTrue() {
        var set = mock(PrimitiveSet.class);
        when(set.isEmpty()).thenReturn(true);

        var result = PrimitiveCollections.equals(set, Set.of());

        assertThat(result).isTrue();
        verify(set).size();
        verify(set).isEmpty();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__emptyDoubleSetVsEmptyIntSet__returnsTrue() {
        var set1 = mock(PrimitiveSet.OfDouble.class);
        var set2 = mock(PrimitiveSet.OfInt.class);
        when(set1.isEmpty()).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @Test
    void equals__emptyDoubleSetVsEmptyLongSet__returnsTrue() {
        var set1 = mock(PrimitiveSet.OfDouble.class);
        var set2 = mock(PrimitiveSet.OfLong.class);
        when(set1.isEmpty()).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @Test
    void equals__emptyIntSetVsEmptyDoubleSet__returnsTrue() {
        var set1 = mock(PrimitiveSet.OfInt.class);
        var set2 = mock(PrimitiveSet.OfDouble.class);
        when(set1.isEmpty()).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @Test
    void equals__emptyIntSetVsEmptyLongSet__returnsTrue() {
        var set1 = mock(PrimitiveSet.OfInt.class);
        var set2 = mock(PrimitiveSet.OfLong.class);
        when(set1.isEmpty()).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @Test
    void equals__emptyLongSetVsEmptyDoubleSet__returnsTrue() {
        var set1 = mock(PrimitiveSet.OfLong.class);
        var set2 = mock(PrimitiveSet.OfDouble.class);
        when(set1.isEmpty()).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @Test
    void equals__emptyLongSetVsEmptyIntSet__returnsTrue() {
        var set1 = mock(PrimitiveSet.OfLong.class);
        var set2 = mock(PrimitiveSet.OfInt.class);
        when(set1.isEmpty()).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @Test
    void equals__setsOfDifferingSize__returnsFalse() {
        var set1 = mock(PrimitiveSet.class);
        var set2 = mock(PrimitiveSet.class);
        when(set1.size()).thenReturn(1);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1).size();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__doubleSetsOfSameSizeContainingSameElements__returnsTrue(int size) {
        var set1 = mock(PrimitiveSet.OfDouble.class);
        var set2 = mock(PrimitiveSet.OfDouble.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set1.containsAll(set2)).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set1).containsAll(set2);
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__intSetsOfSameSizeContainingSameElements__returnsTrue(int size) {
        var set1 = mock(PrimitiveSet.OfInt.class);
        var set2 = mock(PrimitiveSet.OfInt.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set1.containsAll(set2)).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set1).containsAll(set2);
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__longSetsOfSameSizeContainingSameElements__returnsTrue(int size) {
        var set1 = mock(PrimitiveSet.OfLong.class);
        var set2 = mock(PrimitiveSet.OfLong.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set1.containsAll(set2)).thenReturn(true);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set1).containsAll(set2);
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__unknownNonEmptyPrimitiveSetComparedToSet__throwsClassCastException(int size) {
        // TODO: Should this be RuntimeException or a custom exception instead?
        var set1 = mock(PrimitiveSet.class);
        var set2 = mock(Set.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);

        var t = catchThrowable(() -> PrimitiveCollections.equals(set1, set2));

        assertThat(t).isInstanceOf(ClassCastException.class)
                .hasMessage("Internal error");
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__doubleSetComparedToBoxedDoubleSetWithEqualElements__returnsTrue(int size) {
        var values = IntStream.rangeClosed(1, size).asDoubleStream().boxed().toList();
        var set1 = mock(PrimitiveSet.OfDouble.class);
        var set2 = mock(Set.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set2.stream()).thenReturn(values.stream());
        when(set1.contains(any(Double.class))).thenAnswer(invocationOnMock ->
                values.contains(invocationOnMock.getArgument(0)));

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1, times(2)).size();
        verify(set1).isEmpty();
        verify(set1, times(size)).contains(any());
        verify(set2).size();
        verify(set2).stream();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__intSetComparedToBoxedIntegerSetWithEqualElements__returnsTrue(int size) {
        var values = IntStream.rangeClosed(1, size).boxed().toList();
        var set1 = mock(PrimitiveSet.OfInt.class);
        var set2 = mock(Set.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set2.stream()).thenReturn(values.stream());
        when(set1.contains(any(Integer.class))).thenAnswer(invocationOnMock ->
                values.contains(invocationOnMock.getArgument(0)));

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1, times(2)).size();
        verify(set1).isEmpty();
        verify(set1, times(size)).contains(any());
        verify(set2).size();
        verify(set2).stream();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__longSetComparedToBoxedLongSetWithEqualElements__returnsTrue(int size) {
        var values = IntStream.rangeClosed(1, size).asLongStream().boxed().toList();
        var set1 = mock(PrimitiveSet.OfLong.class);
        var set2 = mock(Set.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set2.stream()).thenReturn(values.stream());
        when(set1.contains(any(Long.class))).thenAnswer(invocationOnMock ->
                values.contains(invocationOnMock.getArgument(0)));

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isTrue();
        verify(set1, times(2)).size();
        verify(set1).isEmpty();
        verify(set1, times(size)).contains(any());
        verify(set2).size();
        verify(set2).stream();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__nonEmptyDoubleSetComparedToIntSet__returnsFalse(int size) {
        var set1 = mock(PrimitiveSet.OfDouble.class);
        var set2 = mock(PrimitiveSet.OfInt.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__nonEmptyDoubleSetComparedToLongSet__returnsFalse(int size) {
        var set1 = mock(PrimitiveSet.OfDouble.class);
        var set2 = mock(PrimitiveSet.OfLong.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5})
    void equals__nonEmptyIntSetComparedToDoubleSet__returnsFalse(int size) {
        var set1 = mock(PrimitiveSet.OfInt.class);
        var set2 = mock(PrimitiveSet.OfDouble.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__nonEmptyIntSetComparedToLongSet__returnsFalse(int size) {
        var set1 = mock(PrimitiveSet.OfInt.class);
        var set2 = mock(PrimitiveSet.OfLong.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__nonEmptyLongSetComparedToDoubleSet__returnsFalse(int size) {
        var set1 = mock(PrimitiveSet.OfLong.class);
        var set2 = mock(PrimitiveSet.OfDouble.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__nonEmptyLongSetComparedToIntSet__returnsFalse(int size) {
        var set1 = mock(PrimitiveSet.OfLong.class);
        var set2 = mock(PrimitiveSet.OfInt.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1).size();
        verify(set1).isEmpty();
        verify(set2).size();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__nonEmptyDoubleSetComparedToBoxedSetWithDifferingElements__returnsFalse(int size) {
        var values = IntStream.rangeClosed(1, size).asDoubleStream().boxed().toList();
        var set1 = mock(PrimitiveSet.OfDouble.class);
        var set2 = mock(Set.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set2.stream()).thenReturn(IntStream.range(0, size).asDoubleStream().boxed());
        when(set1.contains(any())).thenAnswer(invocationOnMock ->
                values.contains(invocationOnMock.getArgument(0)));

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1, times(2)).size();
        verify(set1).isEmpty();
        verify(set1, times(size)).contains(any());
        verify(set2).size();
        verify(set2).stream();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__nonEmptyIntSetComparedToBoxedSetWithDifferingElements__returnsFalse(int size) {
        var values = IntStream.rangeClosed(1, size).boxed().toList();
        var set1 = mock(PrimitiveSet.OfInt.class);
        var set2 = mock(Set.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set2.stream()).thenReturn(IntStream.range(0, size).boxed());
        when(set1.contains(any())).thenAnswer(invocationOnMock ->
                values.contains(invocationOnMock.getArgument(0)));

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1, times(2)).size();
        verify(set1).isEmpty();
        verify(set1, times(size)).contains(any());
        verify(set2).size();
        verify(set2).stream();
        verifyNoMoreInteractions(set1, set2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void equals__nonEmptyLongSetComparedToBoxedSetWithDifferingElements__returnsFalse(int size) {
        var values = IntStream.rangeClosed(1, size).asLongStream().boxed().toList();
        var set1 = mock(PrimitiveSet.OfLong.class);
        var set2 = mock(Set.class);
        when(set1.size()).thenReturn(size);
        when(set2.size()).thenReturn(size);
        when(set2.stream()).thenReturn(IntStream.range(0, size).asLongStream().boxed());
        when(set1.contains(any())).thenAnswer(invocationOnMock ->
                values.contains(invocationOnMock.getArgument(0)));

        var result = PrimitiveCollections.equals(set1, set2);

        assertThat(result).isFalse();
        verify(set1, times(2)).size();
        verify(set1).isEmpty();
        verify(set1, times(size)).contains(any());
        verify(set2).size();
        verify(set2).stream();
        verifyNoMoreInteractions(set1, set2);
    }

    @Test
    void hashCode__emptyDoubleSet__hashCodeIsZero() {
        var set = mock(PrimitiveSet.OfDouble.class);
        when(set.primitiveStream()).thenReturn(DoubleStream.empty());

        var result = PrimitiveCollections.hashCode(set);

        assertThat(result).isZero();
        verify(set).primitiveStream();
        verifyNoMoreInteractions(set);
    }

    @Test
    void hashCode__emptyIntSet__hashCodeIsZero() {
        var set = mock(PrimitiveSet.OfInt.class);
        when(set.primitiveStream()).thenReturn(IntStream.empty());

        var result = PrimitiveCollections.hashCode(set);

        assertThat(result).isZero();
        verify(set).primitiveStream();
        verifyNoMoreInteractions(set);
    }

    @Test
    void hashCode__emptyLongSet__hashCodeIsZero() {
        var set = mock(PrimitiveSet.OfLong.class);
        when(set.primitiveStream()).thenReturn(LongStream.empty());

        var result = PrimitiveCollections.hashCode(set);

        assertThat(result).isZero();
        verify(set).primitiveStream();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void hashCode__doubleSetWithElements__hashCodeMatchesBoxedSet(int step) {
        var values = IntStream.range(0, 5).mapToDouble(i -> 0.5 + (i * step)).toArray();
        var set = mock(PrimitiveSet.OfDouble.class);
        var expected = DoubleStream.of(values).boxed().collect(Collectors.toSet()).hashCode();
        when(set.primitiveStream()).thenReturn(DoubleStream.of(values));

        var result = PrimitiveCollections.hashCode(set);

        assertThat(result).isEqualTo(expected);
        verify(set).primitiveStream();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void hashCode__intSetWithElements__hashCodeMatchesBoxedSet(int step) {
        var values = IntStream.range(0, 5).map(i -> 1 + (i * step)).toArray();
        var set = mock(PrimitiveSet.OfInt.class);
        var expected = IntStream.of(values).boxed().collect(Collectors.toSet()).hashCode();
        when(set.primitiveStream()).thenReturn(IntStream.of(values));

        var result = PrimitiveCollections.hashCode(set);

        assertThat(result).isEqualTo(expected);
        verify(set).primitiveStream();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void hashCode__longSetWithElements__hashCodeMatchesBoxedSet(int step) {
        var values = IntStream.range(0, 5).mapToLong(i -> 1L + ((long) i * step)).toArray();
        var set = mock(PrimitiveSet.OfLong.class);
        var expected = LongStream.of(values).boxed().collect(Collectors.toSet()).hashCode();
        when(set.primitiveStream()).thenReturn(LongStream.of(values));

        var result = PrimitiveCollections.hashCode(set);

        assertThat(result).isEqualTo(expected);
        verify(set).primitiveStream();
        verifyNoMoreInteractions(set);
    }

    @Test
    void intSpliterator__noCharacteristicsSpecified__returnedSpliteratorIsSizedAndSubsized() {
        var collection = mock(PrimitiveCollection.OfInt.class);

        var spliterator = PrimitiveCollections.intSpliterator(collection, 0);
        var characteristics = spliterator.characteristics();

        assertThat(characteristics)
                .isEqualTo(Spliterator.SIZED | Spliterator.SUBSIZED);
    }

    @Test
    void intSpliterator__concurrentSpecified__returnedSpliteratorIsNotSizedOrSubsized() {
        var collection = mock(PrimitiveCollection.OfInt.class);

        var spliterator = PrimitiveCollections.intSpliterator(collection, Spliterator.CONCURRENT);
        var characteristics = spliterator.characteristics();

        assertThat(characteristics).isEqualTo(Spliterator.CONCURRENT);
    }

    @Test
    void intSpliterator__always__returnedSpliteratorIsLateBinding() {
        var collection = mock(PrimitiveCollection.OfInt.class);

        PrimitiveCollections.intSpliterator(collection, 0);

        verifyNoMoreInteractions(collection);
    }

    @Test
    void longSpliterator__noCharacteristicsSpecified__returnedSpliteratorIsSizedAndSubsized() {
        var collection = mock(PrimitiveCollection.OfLong.class);

        var spliterator = PrimitiveCollections.longSpliterator(collection, 0);
        var characteristics = spliterator.characteristics();

        assertThat(characteristics)
                .isEqualTo(Spliterator.SIZED | Spliterator.SUBSIZED);
    }

    @Test
    void longSpliterator__concurrentSpecified__returnedSpliteratorIsNotSizedOrSubsized() {
        var collection = mock(PrimitiveCollection.OfLong.class);

        var spliterator = PrimitiveCollections.longSpliterator(collection, Spliterator.CONCURRENT);
        var characteristics = spliterator.characteristics();

        assertThat(characteristics).isEqualTo(Spliterator.CONCURRENT);
    }

    @Test
    void longSpliterator__always__returnedSpliteratorIsLateBinding() {
        var collection = mock(PrimitiveCollection.OfLong.class);

        PrimitiveCollections.longSpliterator(collection, 0);

        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 5, 10, 1000, 100_000 })
    void setOf_doubleArray__comparedToSet__returnedSetIsEqual(int size) {
        var expected = IntStream.range(0, size).asDoubleStream().boxed()
                .collect(Collectors.toSet());

        var result = PrimitiveCollections.setOf(IntStream.range(0, size)
                .asDoubleStream().toArray());

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 5, 10, 1000, 100_000 })
    void setOf_doubleCollection__comparedToSet__returnedSetIsEqual(int size) {
        var values = IntStream.range(0, size).asDoubleStream().toArray();
        var expected = IntStream.range(0, size).asDoubleStream().boxed()
                .collect(Collectors.toSet());
        var collection = mock(PrimitiveCollection.OfDouble.class);
        when(collection.toPrimitiveArray()).thenReturn(values);

        var result = PrimitiveCollections.setOf(collection);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 5, 10, 1000, 100_000 })
    void setOf_intArray__comparedToSet__returnedSetIsEqual(int size) {
        var expected = IntStream.range(0, size).boxed().collect(Collectors.toSet());

        var result = PrimitiveCollections.setOf(IntStream.range(0, size)
                .toArray());

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 5, 10, 1000, 100_000 })
    void setOf_intCollection__comparedToSet__returnedSetIsEqual(int size) {
        var values = IntStream.range(0, size).toArray();
        var expected = IntStream.range(0, size).boxed().collect(Collectors.toSet());
        var collection = mock(PrimitiveCollection.OfInt.class);
        when(collection.toPrimitiveArray()).thenReturn(values);

        var result = PrimitiveCollections.setOf(collection);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 5, 10, 1000, 100_000 })
    void setOf_longArray__comparedToSet__returnedSetIsEqual(int size) {
        var expected = IntStream.range(0, size).asLongStream().boxed()
                .collect(Collectors.toSet());

        var result = PrimitiveCollections.setOf(IntStream.range(0, size)
                .asLongStream().toArray());

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 5, 10, 1000, 100_000 })
    void setOf_longCollection__comparedToSet__returnedSetIsEqual(int size) {
        var values = IntStream.range(0, size).asLongStream().toArray();
        var expected = IntStream.range(0, size).asLongStream().boxed()
                .collect(Collectors.toSet());
        var collection = mock(PrimitiveCollection.OfLong.class);
        when(collection.toPrimitiveArray()).thenReturn(values);

        var result = PrimitiveCollections.setOf(collection);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void toPrimitiveArray_OfDouble__emptyCollection__returnsEmptyArray() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyDoubleIterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).isEmpty();
        verify(collection).size();
        verify(collection).iterator();
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfDouble__hasElements__returnsExpectedElements(int size) {
        var values = IntStream.range(0, size).mapToDouble(i -> 0.5 + i).toArray();
        var collection = mock(PrimitiveCollection.OfDouble.class);
        when(collection.size()).thenReturn(size);
        when(collection.iterator()).thenReturn(DoubleStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfDouble__reportedSizeZeroButHasElements__returnsExpectedElements(int size) {
        var values = IntStream.range(0, size).mapToDouble(i -> 0.5 + i).toArray();
        var collection = mock(PrimitiveCollection.OfDouble.class);
        when(collection.iterator()).thenReturn(DoubleStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfDouble__reportedSizeExceedsElements__returnsExpectedElements(int size) {
        var values = IntStream.range(0, size).mapToDouble(i -> 0.5 + i).toArray();
        var collection = mock(PrimitiveCollection.OfDouble.class);
        when(collection.size()).thenReturn(size + 1);
        when(collection.iterator()).thenReturn(DoubleStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toPrimitiveArray_OfInt__emptyCollection__returnsEmptyArray() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyIntIterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).isEmpty();
        verify(collection).size();
        verify(collection).iterator();
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfInt__hasElements__returnsExpectedElements(int size) {
        var values = IntStream.rangeClosed(1, size).toArray();
        var collection = mock(PrimitiveCollection.OfInt.class);
        when(collection.size()).thenReturn(size);
        when(collection.iterator()).thenReturn(IntStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfInt__reportedSizeZeroButHasElements__returnsExpectedElements(int size) {
        var values = IntStream.rangeClosed(1, size).toArray();
        var collection = mock(PrimitiveCollection.OfInt.class);
        when(collection.iterator()).thenReturn(IntStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfInt__reportedSizeExceedsElements__returnsExpectedElements(int size) {
        var values = IntStream.rangeClosed(1, size).toArray();
        var collection = mock(PrimitiveCollection.OfInt.class);
        when(collection.size()).thenReturn(size + 1);
        when(collection.iterator()).thenReturn(IntStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toPrimitiveArray_OfLong__emptyCollection__returnsEmptyArray() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyLongIterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).isEmpty();
        verify(collection).size();
        verify(collection).iterator();
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfLong__hasElements__returnsExpectedElements(int size) {
        var values = IntStream.rangeClosed(1, size).asLongStream().toArray();
        var collection = mock(PrimitiveCollection.OfLong.class);
        when(collection.size()).thenReturn(size);
        when(collection.iterator()).thenReturn(LongStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfLong__reportedSizeZeroButHasElements__returnsExpectedElements(int size) {
        var values = IntStream.rangeClosed(1, size).asLongStream().toArray();
        var collection = mock(PrimitiveCollection.OfLong.class);
        when(collection.iterator()).thenReturn(LongStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toPrimitiveArray_OfLong__reportedSizeExceedsElements__returnsExpectedElements(int size) {
        var values = IntStream.rangeClosed(1, size).asLongStream().toArray();
        var collection = mock(PrimitiveCollection.OfLong.class);
        when(collection.size()).thenReturn(size + 1);
        when(collection.iterator()).thenReturn(LongStream.of(values).iterator());

        var result = PrimitiveCollections.toPrimitiveArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_Collection__emptyCollection__returnsEmptyArray() {
        var collection = mock(Collection.class);
        when(collection.iterator()).thenReturn(Collections.emptyIterator());

        var result = PrimitiveCollections.toArray(collection);

        assertThat(result).isEmpty();
        verify(collection).size();
        verify(collection).iterator();
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toArray_Collection__hasElements__returnsExpectedElements(int size) {
        var values = Stream.generate(Object::new).limit(size).toArray();
        var collection = mock(Collection.class);
        when(collection.size()).thenReturn(size);
        when(collection.iterator()).thenReturn(Stream.of(values).iterator());

        var result = PrimitiveCollections.toArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toArray_Collection__reportedSizeZeroButHasElements__returnsExpectedElements(int size) {
        var values = Stream.generate(Object::new).limit(size).toArray();
        var collection = mock(Collection.class);
        when(collection.iterator()).thenReturn(Stream.of(values).iterator());

        var result = PrimitiveCollections.toArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toArray_Collection__reportedSizeExceedsElements__returnsExpectedElements(int size) {
        var values = Stream.generate(Object::new).limit(size).toArray();
        var collection = mock(Collection.class);
        when(collection.size()).thenReturn(size + 1);
        when(collection.iterator()).thenReturn(Stream.of(values).iterator());

        var result = PrimitiveCollections.toArray(collection);

        assertThat(result).hasSize(size).containsExactly(values);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_Collection_array__emptyCollectionAndArray__returnsProvidedArray() {
        var collection = mock(Collection.class);
        var arr = new Object[0];
        when(collection.iterator()).thenReturn(Collections.emptyIterator());

        var result = PrimitiveCollections.toArray(collection, arr);

        assertThat(result).isSameAs(arr);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_Collection_array__emptyCollectionButArrayHasElements__nullsFirstElement() {
        var collection = mock(Collection.class);
        var arr = Stream.generate(Object::new).limit(5).toArray();
        when(collection.iterator()).thenReturn(Collections.emptyIterator());

        var result = PrimitiveCollections.toArray(collection, arr);

        assertThat(result).isSameAs(arr);
        assertThat(arr[0]).isNull();
        Arrays.stream(arr, 1, 5)
                .forEach(o -> assertThat(o).isNotNull());
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toArray_Collection_array__hasConvertibleElement__returnsExpectedElements(int size) {
        var values = IntStream.rangeClosed(1, size).boxed().toArray();
        var collection = mock(Collection.class);
        when(collection.size()).thenReturn(size);
        when(collection.iterator()).thenReturn(Stream.of(values).iterator());

        var result = PrimitiveCollections.toArray(collection, new Integer[0]);

        assertThat(result).hasSize(size).isInstanceOf(Integer[].class);
        IntStream.range(0, size).forEach(i ->
                assertThat(result[i]).isEqualTo(values[i]));
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toArray_Collection_array__arraySizeMatchesCollectionSize__returnsSameArray(int size) {
        var values = IntStream.rangeClosed(1, size).boxed().toArray();
        var collection = mock(Collection.class);
        var arr = new Integer[size];
        when(collection.size()).thenReturn(size);
        when(collection.iterator()).thenReturn(Stream.of(values).iterator());

        var result = PrimitiveCollections.toArray(collection, arr);

        assertThat(result).isSameAs(arr);
        IntStream.range(0, size).forEach(i ->
                assertThat(result[i]).isEqualTo(values[i]));
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toArray_Collection_array__arraySizeGreaterThanCollectionSize__marksEndWithNull(int size) {
        var values = IntStream.rangeClosed(1, size).boxed().toArray();
        var collection = mock(Collection.class);
        var arr = Stream.generate(() -> -1).limit(size * 2L)
                .toArray(Integer[]::new);
        when(collection.size()).thenReturn(size);
        when(collection.iterator()).thenReturn(Stream.of(values).iterator());

        var result = PrimitiveCollections.toArray(collection, arr);

        assertThat(result).isSameAs(arr);
        IntStream.range(0, size).forEach(i ->
                assertThat(result[i]).isEqualTo(values[i]));
        assertThat(result[size]).isNull();
        IntStream.range(size + 1, result.length).forEach(i ->
                assertThat(result[i]).isEqualTo(-1));
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_000 })
    void toArray_Collection_array__reportedSizeZeroButHasElements__returnsExpectedElements(int size) {
        var values = IntStream.rangeClosed(1, size).boxed().toArray();
        var collection = mock(Collection.class);
        when(collection.iterator()).thenReturn(Stream.of(values).iterator());

        var result = PrimitiveCollections.toArray(collection, new Integer[0]);

        assertThat(result).hasSize(size).isInstanceOf(Integer[].class);
        IntStream.range(0, size).forEach(i ->
                assertThat(result[i]).isEqualTo(values[i]));
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 100, 1000, 10_000, 100_00 })
    void toArray_Collection_array__reportedSizeExceedsElements__returnedExpectedElements(int size) {
        // Special case: when the initial array is insufficient for the reported size,
        // but the iteration runs out early, we reallocate to the iteration size rather
        // than continue with the larger reallocation.
        var values = IntStream.rangeClosed(1, size).boxed().toArray();
        var collection = mock(Collection.class);
        when(collection.size()).thenReturn(size + 1);
        when(collection.iterator()).thenReturn(Stream.of(values).iterator());

        var result = PrimitiveCollections.toArray(collection, new Integer[0]);

        assertThat(result).hasSize(size).isInstanceOf(Integer[].class);
        IntStream.range(0, size).forEach(i ->
                assertThat(result[i]).isEqualTo(values[i]));
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }
}
