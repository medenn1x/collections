package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

// Tests for default methods of PrimitiveCollection.OfInt
@SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
class IntCollectionDefaultTest {
    @Test
    void add__always__delegatesToAddInt() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection).add(1);
        when(collection.addInt(1)).thenReturn(true);

        var result = collection.add(1);

        assertThat(result).isTrue();
        verify(collection).add(1);
        verify(collection).addInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void addAll_Collection__passedPrimitiveCollection__delegatesToAddAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection1).addAll((Collection<Integer>) collection2);
        when(collection1.addAll(collection2)).thenReturn(true);

        var result = collection1.addAll((Collection<Integer>) collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll((Collection<Integer>) collection2);
        verify(collection1).addAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addAll_Collection__passedBoxedCollection__iteratesAndCallsAddInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        doCallRealMethod().when(collection1).addAll(collection2);
        when(collection2.iterator()).thenReturn(Stream.of(1, 2).iterator());
        when(collection1.addInt(anyInt())).thenReturn(true);

        var result = collection1.addAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll(collection2);
        verify(collection1).addInt(1);
        verify(collection1).addInt(2);
        verify(collection2).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addAll_OfInt__always__iteratesAndCallsAddInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection1).addAll(collection2);
        when(collection2.iterator()).thenReturn(IntStream.of(1, 2).iterator());
        when(collection1.addInt(anyInt())).thenReturn(true);

        var result = collection1.addAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll(collection2);
        verify(collection1).addInt(1);
        verify(collection1).addInt(2);
        verify(collection2).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addInt__always__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection).addInt(1);

        var t = catchThrowable(() -> collection.addInt(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verify(collection).addInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__passedInt__delegatesToContainsInt() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection).contains(1);
        when(collection.containsInt(1)).thenReturn(true);

        var result = collection.contains(1);

        assertThat(result).isTrue();
        verify(collection).contains(1);
        verify(collection).containsInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__passedObject__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var ob = new Object();
        doCallRealMethod().when(collection).contains(ob);

        var result = collection.contains(ob);

        assertThat(result).isFalse();
        verify(collection).contains(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__passedPrimitiveCollection__delegatesToContainsAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection1).containsAll((Collection<Integer>) collection2);
        when(collection1.containsAll(collection2)).thenReturn(true);

        var result = collection1.containsAll((Collection<Integer>) collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll((Collection<Integer>) collection2);
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__passedBoxedCollection__streamCalledWithAllMatchContains() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var stream = spy(Stream.of(1, 2));
        doCallRealMethod().when(collection1).containsAll(collection2);
        when(collection2.stream()).thenReturn(stream);
        when(collection1.contains(any())).thenReturn(true);

        var result = collection1.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verify(collection1).contains(1);
        verify(collection1).contains(2);
        verify(collection2).stream();
        verify(stream).allMatch(any());
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfInt__always__primitiveStreamCalledWithAllMatchContainsInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var stream = spy(IntStream.of(1, 2));
        doCallRealMethod().when(collection1).containsAll(collection2);
        when(collection2.primitiveStream()).thenReturn(stream);
        when(collection1.containsInt(anyInt())).thenReturn(true);

        var result = collection1.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verify(collection1).containsInt(1);
        verify(collection1).containsInt(2);
        verify(collection2).primitiveStream();
        verify(stream).allMatch(any());
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4 })
    void containsInt__always__primitiveStreamCalledWithAnyMatchEquals(int v) {
        var values = new int[] { 1, 2 };
        // Careful: very test-structure-specific logic here
        var expectedResult = v < 3;
        var collection = mock(PrimitiveCollection.OfInt.class);
        var stream = spy(IntStream.of(values));
        doCallRealMethod().when(collection).containsInt(anyInt());
        when(collection.primitiveStream()).thenReturn(stream);

        var result = collection.containsInt(v);

        assertThat(result).isEqualTo(expectedResult);
        verify(collection).containsInt(v);
        verify(collection).primitiveStream();
        verify(stream).anyMatch(any());
        verifyNoMoreInteractions(collection);
    }

    @Test
    void remove__passedInt__delegatesToRemoveInt() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection).remove(1);
        when(collection.removeInt(1)).thenReturn(true);

        var result = collection.remove(1);

        assertThat(result).isTrue();
        verify(collection).remove(1);
        verify(collection).removeInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void remove__passedObject__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var ob = new Object();
        doCallRealMethod().when(collection).remove(ob);

        var result = collection.remove(ob);

        assertThat(result).isFalse();
        verify(collection).remove(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void removeAll_Collection__passedPrimitiveCollection__delegatesToRemoveAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection1).removeAll((Collection<Integer>) collection2);
        when(collection1.removeAll(collection2)).thenReturn(true);

        var result = collection1.removeAll((Collection<Integer>) collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll((Collection<Integer>) collection2);
        verify(collection1).removeAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_Collection__passedBoxedCollection__delegatesToRemoveIfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        doCallRealMethod().when(collection1).removeAll(collection2);
        when(collection1.removeIfInt(any())).thenAnswer(invocationOnMock ->
                ((IntPredicate) invocationOnMock.getArgument(0)).test(1));
        when(collection2.contains(1)).thenReturn(true);

        var result = collection1.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verify(collection1).removeIfInt(any());
        verify(collection2).contains(1);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_OfInt__always__delegatesToRemoveIfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection1).removeAll(collection2);
        when(collection1.removeIfInt(any())).thenAnswer(invocationOnMock ->
                ((IntPredicate) invocationOnMock.getArgument(0)).test(1));
        when(collection2.containsInt(1)).thenReturn(true);

        var result = collection1.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verify(collection1).removeIfInt(any());
        verify(collection2).containsInt(1);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4 })
    void removeInt__always__iteratesAndCallsRemoveOnEqual(int v) {
        // Careful: very test-structure-specific logic here
        var expectedResult = v < 3;
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        doCallRealMethod().when(collection).removeInt(v);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2);

        var result = collection.removeInt(v);

        assertThat(result).isEqualTo(expectedResult);
        verify(collection).removeInt(v);
        verify(collection).iterator();
        verify(iterator, times(Math.min(v, 3))).hasNext();
        verify(iterator, times(Math.min(v, 2))).nextInt();
        if(expectedResult)
            verify(iterator).remove();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void removeIf_IntPredicate__always__delegatesToRemoveIfInt() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var filter = mock(IntPredicate.class);
        doCallRealMethod().when(collection).removeIf(filter);
        when(collection.removeIfInt(filter)).thenReturn(true);

        var result = collection.removeIf(filter);

        assertThat(result).isTrue();
        verify(collection).removeIf(filter);
        verify(collection).removeIfInt(filter);
        verifyNoMoreInteractions(collection, filter);
    }

    @Test
    void removeIf_Predicate__always__delegatesToRemoveIfInt() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var filter = (Predicate<Integer>) mock(Predicate.class);
        doCallRealMethod().when(collection).removeIf(filter);
        when(collection.removeIfInt(any())).thenAnswer(invocationOnMock ->
                ((IntPredicate) invocationOnMock.getArgument(0)).test(1));
        when(filter.test(1)).thenReturn(true);

        var result = collection.removeIf(filter);

        assertThat(result).isTrue();
        verify(collection).removeIf(filter);
        verify(collection).removeIfInt(any());
        verify(filter).test(1);
        verifyNoMoreInteractions(collection, filter);
    }

    @Test
    void removeIfInt__always__iteratesOverCollectionAndCallsRemoveOnTrue() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var filter = mock(IntPredicate.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        doCallRealMethod().when(collection).removeIfInt(filter);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);
        when(filter.test(1)).thenReturn(true);

        var result = collection.removeIfInt(filter);

        assertThat(result).isTrue();
        verify(collection).removeIfInt(filter);
        verify(collection).iterator();
        verify(filter).test(1);
        verify(filter).test(2);
        verify(filter).test(3);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection, filter, iterator);
    }

    @Test
    void retainAll_Collection__passedPrimitiveCollection__delegatesToRetainAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection1).retainAll((Collection<Integer>) collection2);
        when(collection1.retainAll(collection2)).thenReturn(true);

        var result = collection1.retainAll((Collection<Integer>) collection2);

        assertThat(result).isTrue();
        verify(collection1).retainAll((Collection<Integer>) collection2);
        verify(collection1).retainAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_Collection__passedBoxedCollection__delegatesToRemoveIfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        doCallRealMethod().when(collection1).retainAll(collection2);
        when(collection1.removeIfInt(any())).thenAnswer(invocationOnMock ->
                ((IntPredicate) invocationOnMock.getArgument(0)).test(1));
        when(collection2.contains(1)).thenReturn(true);

        var result = collection1.retainAll(collection2);

        assertThat(result).isFalse();
        verify(collection1).retainAll(collection2);
        verify(collection1).removeIfInt(any());
        verify(collection2).contains(1);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_OfInt__always__delegatesToRetainIfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        doCallRealMethod().when(collection1).retainAll(collection2);
        when(collection1.removeIfInt(any())).thenAnswer(invocationOnMock ->
                ((IntPredicate) invocationOnMock.getArgument(0)).test(1));
        when(collection2.containsInt(1)).thenReturn(true);

        var result = collection1.retainAll(collection2);

        assertThat(result).isFalse();
        verify(collection1).retainAll(collection2);
        verify(collection1).removeIfInt(any());
        verify(collection2).containsInt(1);
        verifyNoMoreInteractions(collection1, collection2);
    }
}
