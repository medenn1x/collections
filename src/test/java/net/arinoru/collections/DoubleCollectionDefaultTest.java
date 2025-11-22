package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

// Tests for default methods of PrimitiveCollection.OfDouble
@SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
class DoubleCollectionDefaultTest {
    @Test
    void add__always__delegatesToAddDouble() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection).add(1.0);
        when(collection.addDouble(1.0)).thenReturn(true);

        var result = collection.add(1.0);

        assertThat(result).isTrue();
        verify(collection).add(1.0);
        verify(collection).addDouble(1.0);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void addAll_Collection__passedPrimitiveCollection__delegatesToAddAll_OfDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection1).addAll((Collection<Double>) collection2);
        when(collection1.addAll(collection2)).thenReturn(true);

        var result = collection1.addAll((Collection<Double>) collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll((Collection<Double>) collection2);
        verify(collection1).addAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addAll_Collection__passedBoxedCollection__iteratesAndCallsAddDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = (Collection<Double>) mock(Collection.class);
        doCallRealMethod().when(collection1).addAll(collection2);
        when(collection2.iterator()).thenReturn(Stream.of(1.0, 2.0).iterator());
        when(collection1.addDouble(anyDouble())).thenReturn(true);

        var result = collection1.addAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll(collection2);
        verify(collection1).addDouble(1.0);
        verify(collection1).addDouble(2.0);
        verify(collection2).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addAll_OfDouble__always__iteratesAndCallsAddDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection1).addAll(collection2);
        when(collection2.iterator()).thenReturn(DoubleStream.of(1.0, 2.0).iterator());
        when(collection1.addDouble(anyDouble())).thenReturn(true);

        var result = collection1.addAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll(collection2);
        verify(collection1).addDouble(1.0);
        verify(collection1).addDouble(2.0);
        verify(collection2).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addDouble__always__throwsException() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection).addDouble(1.0);

        var t = catchThrowable(() -> collection.addDouble(1.0));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verify(collection).addDouble(1.0);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__passedDouble__delegatesToContainsDouble() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection).contains(1.0);
        when(collection.containsDouble(1.0)).thenReturn(true);

        var result = collection.contains(1.0);

        assertThat(result).isTrue();
        verify(collection).contains(1.0);
        verify(collection).containsDouble(1.0);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__passedObject__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var ob = new Object();
        doCallRealMethod().when(collection).contains(ob);

        var result = collection.contains(ob);

        assertThat(result).isFalse();
        verify(collection).contains(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__passedPrimitiveCollection__delegatesToContainsAll_OfDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection1).containsAll((Collection<Double>) collection2);
        when(collection1.containsAll(collection2)).thenReturn(true);

        var result = collection1.containsAll((Collection<Double>) collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll((Collection<Double>) collection2);
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__passedBoxedCollection__streamCalledWithAllMatchContains() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = (Collection<Double>) mock(Collection.class);
        var stream = spy(Stream.of(1.0, 2.0));
        doCallRealMethod().when(collection1).containsAll(collection2);
        when(collection2.stream()).thenReturn(stream);
        when(collection1.contains(any())).thenReturn(true);

        var result = collection1.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verify(collection1).contains(1.0);
        verify(collection1).contains(2.0);
        verify(collection2).stream();
        verify(stream).allMatch(any());
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfDouble__always__primitiveStreamCalledWithAllMatchContainsDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = mock(PrimitiveCollection.OfDouble.class);
        var stream = spy(DoubleStream.of(1.0, 2.0));
        doCallRealMethod().when(collection1).containsAll(collection2);
        when(collection2.primitiveStream()).thenReturn(stream);
        when(collection1.containsDouble(anyDouble())).thenReturn(true);

        var result = collection1.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verify(collection1).containsDouble(1.0);
        verify(collection1).containsDouble(2.0);
        verify(collection2).primitiveStream();
        verify(stream).allMatch(any());
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4 })
    void containsDouble__always__primitiveStreamCalledWithAnyMatchEquals(int v) {
        var values = new double[] { 1.0, 2.0 };
        var checkValue = (double) v;
        // Careful: very test-structure-specific logic here
        var expectedResult = v < 3;
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var stream = spy(DoubleStream.of(values));
        doCallRealMethod().when(collection).containsDouble(anyDouble());
        when(collection.primitiveStream()).thenReturn(stream);

        var result = collection.containsDouble(checkValue);

        assertThat(result).isEqualTo(expectedResult);
        verify(collection).containsDouble(checkValue);
        verify(collection).primitiveStream();
        verify(stream).anyMatch(any());
        verifyNoMoreInteractions(collection);
    }

    @Test
    void remove__passedDouble__delegatesToRemoveDouble() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection).remove(1.0);
        when(collection.removeDouble(1.0)).thenReturn(true);

        var result = collection.remove(1.0);

        assertThat(result).isTrue();
        verify(collection).remove(1.0);
        verify(collection).removeDouble(1.0);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void remove__passedObject__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var ob = new Object();
        doCallRealMethod().when(collection).remove(ob);

        var result = collection.remove(ob);

        assertThat(result).isFalse();
        verify(collection).remove(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void removeAll_Collection__passedPrimitiveCollection__delegatesToRemoveAll_OfDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection1).removeAll((Collection<Double>) collection2);
        when(collection1.removeAll(collection2)).thenReturn(true);

        var result = collection1.removeAll((Collection<Double>) collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll((Collection<Double>) collection2);
        verify(collection1).removeAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_Collection__passedBoxedCollection__delegatesToRemoveIfDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = (Collection<Double>) mock(Collection.class);
        doCallRealMethod().when(collection1).removeAll(collection2);
        when(collection1.removeIfDouble(any())).thenAnswer(invocationOnMock ->
                ((DoublePredicate) invocationOnMock.getArgument(0)).test(1.0));
        when(collection2.contains(1.0)).thenReturn(true);

        var result = collection1.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verify(collection1).removeIfDouble(any());
        verify(collection2).contains(1.0);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_OfDouble__always__delegatesToRemoveIfDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection1).removeAll(collection2);
        when(collection1.removeIfDouble(any())).thenAnswer(invocationOnMock ->
                ((DoublePredicate) invocationOnMock.getArgument(0)).test(1.0));
        when(collection2.containsDouble(1.0)).thenReturn(true);

        var result = collection1.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verify(collection1).removeIfDouble(any());
        verify(collection2).containsDouble(1.0);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4 })
    void removeDouble__always__iteratesAndCallsRemoveOnEqual(int v) {
        var removeValue = (double) v;
        // Careful: very test-structure-specific logic here
        var expectedResult = v < 3;
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        doCallRealMethod().when(collection).removeDouble(removeValue);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(1.0, 2.0);

        var result = collection.removeDouble(removeValue);

        assertThat(result).isEqualTo(expectedResult);
        verify(collection).removeDouble(removeValue);
        verify(collection).iterator();
        verify(iterator, times(Math.min(v, 3))).hasNext();
        verify(iterator, times(Math.min(v, 2))).nextDouble();
        if(expectedResult)
            verify(iterator).remove();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void removeIf_DoublePredicate__always__delegatesToRemoveIfDouble() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var filter = mock(DoublePredicate.class);
        doCallRealMethod().when(collection).removeIf(filter);
        when(collection.removeIfDouble(filter)).thenReturn(true);

        var result = collection.removeIf(filter);

        assertThat(result).isTrue();
        verify(collection).removeIf(filter);
        verify(collection).removeIfDouble(filter);
        verifyNoMoreInteractions(collection, filter);
    }

    @Test
    void removeIf_Predicate__always__delegatesToRemoveIfDouble() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var filter = (Predicate<Double>) mock(Predicate.class);
        doCallRealMethod().when(collection).removeIf(filter);
        when(collection.removeIfDouble(any())).thenAnswer(invocationOnMock ->
                ((DoublePredicate) invocationOnMock.getArgument(0)).test(1.0));
        when(filter.test(1.0)).thenReturn(true);

        var result = collection.removeIf(filter);

        assertThat(result).isTrue();
        verify(collection).removeIf(filter);
        verify(collection).removeIfDouble(any());
        verify(filter).test(1.0);
        verifyNoMoreInteractions(collection, filter);
    }

    @Test
    void removeIfDouble__always__iteratesOverCollectionAndCallsRemoveOnTrue() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var filter = mock(DoublePredicate.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        doCallRealMethod().when(collection).removeIfDouble(filter);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextDouble()).thenReturn(1.0, 2.0, 3.0);
        when(filter.test(1.0)).thenReturn(true);

        var result = collection.removeIfDouble(filter);

        assertThat(result).isTrue();
        verify(collection).removeIfDouble(filter);
        verify(collection).iterator();
        verify(filter).test(1.0);
        verify(filter).test(2.0);
        verify(filter).test(3.0);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextDouble();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection, filter, iterator);
    }

    @Test
    void retainAll_Collection__passedPrimitiveCollection__delegatesToRetainAll_OfDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection1).retainAll((Collection<Double>) collection2);
        when(collection1.retainAll(collection2)).thenReturn(true);

        var result = collection1.retainAll((Collection<Double>) collection2);

        assertThat(result).isTrue();
        verify(collection1).retainAll((Collection<Double>) collection2);
        verify(collection1).retainAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_Collection__passedBoxedCollection__delegatesToRemoveIfDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = (Collection<Double>) mock(Collection.class);
        doCallRealMethod().when(collection1).retainAll(collection2);
        when(collection1.removeIfDouble(any())).thenAnswer(invocationOnMock ->
                ((DoublePredicate) invocationOnMock.getArgument(0)).test(1.0));
        when(collection2.contains(1.0)).thenReturn(true);

        var result = collection1.retainAll(collection2);

        assertThat(result).isFalse();
        verify(collection1).retainAll(collection2);
        verify(collection1).removeIfDouble(any());
        verify(collection2).contains(1.0);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_OfDouble__always__delegatesToRetainIfDouble() {
        var collection1 = mock(PrimitiveCollection.OfDouble.class);
        var collection2 = mock(PrimitiveCollection.OfDouble.class);
        doCallRealMethod().when(collection1).retainAll(collection2);
        when(collection1.removeIfDouble(any())).thenAnswer(invocationOnMock ->
                ((DoublePredicate) invocationOnMock.getArgument(0)).test(1.0));
        when(collection2.containsDouble(1.0)).thenReturn(true);

        var result = collection1.retainAll(collection2);

        assertThat(result).isFalse();
        verify(collection1).retainAll(collection2);
        verify(collection1).removeIfDouble(any());
        verify(collection2).containsDouble(1.0);
        verifyNoMoreInteractions(collection1, collection2);
    }
}
