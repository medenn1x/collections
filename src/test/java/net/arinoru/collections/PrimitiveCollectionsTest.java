package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("SuspiciousMethodCalls")
class PrimitiveCollectionsTest {
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
}
