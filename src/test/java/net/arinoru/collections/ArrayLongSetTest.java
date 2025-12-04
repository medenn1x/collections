package net.arinoru.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ArrayLongSetTest {
    @Test
    void fromArray__arrayContainsDuplicates__throwsException() {
        var arr = new long[] { 1L, 2L, 3L, 3L, 5L };

        var t = catchThrowable(() -> PrimitiveCollections.setOf(arr));

        assertThat(t).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duplicate element");
    }

    @Test
    void fromArray__validArray__returnedSetNotBackedByProvidedArray() {
        var arr = new long[] { 1L, 2L, 3L, 4L, 5L };

        var result = PrimitiveCollections.setOf(arr);
        arr[0] = 0L;

        assertThat(result.toPrimitiveArray())
                .containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void fromCollection__collectionContainsDuplicates__throwsException() {
        var values = new long[] { 1L, 2L, 3L, 3L, 5L };
        var collection = mock(PrimitiveCollection.OfLong.class);
        when(collection.toPrimitiveArray()).thenReturn(values);

        var t = catchThrowable(() -> PrimitiveCollections.setOf(collection));

        assertThat(t).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duplicate element");
    }

    @Test
    void fromCollection__validCollection__returnedSetNotBackedByCollection() {
        var values = new long[] { 1L, 2L, 3L, 4L, 5L };
        var collection = mock(PrimitiveCollection.OfLong.class);
        when(collection.toPrimitiveArray()).thenReturn(values);

        // Note: The normal behavior is to make a defensive copy
        // It is permissible to avoid this if the collection is of a class
        // known not to violate the contract of
        // PrimitiveCollection.toPrimitiveArray (i.e. by retaining an array whose
        // state it may modify later). A mock should always trigger a defensive
        // copy.
        var result = PrimitiveCollections.setOf(collection);
        values[0] = 0L;

        assertThat(result.toPrimitiveArray())
                .containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void containsLong__elementInSet__returnsTrue() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var result = cut.containsLong(1L);

        assertThat(result).isTrue();
    }

    @Test
    void containsLong__elementNotInSet__returnsFalse() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var result = cut.containsLong(4L);

        assertThat(result).isFalse();
    }

    @Test
    void iterator__always__returnsIteratorOverElements() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var iterator = cut.iterator();
        var result = new ArrayList<Long>();
        while(iterator.hasNext())
            result.add(iterator.nextLong());
        assertThat(result).containsExactly(1L, 2L, 3L);
    }

    @Test
    void primitiveStream__always__returnsElementStream() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var stream = cut.primitiveStream();
        var result = stream.toArray();

        assertThat(result).containsExactly(1L, 2L, 3L);
    }

    @Test
    void spliterator__always__returnsSpliteratorOverElements() {
        var consumer = mock(LongConsumer.class);
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var spliterator = cut.spliterator();
        spliterator.forEachRemaining(consumer);

        verify(consumer).accept(1L);
        verify(consumer).accept(2L);
        verify(consumer).accept(3L);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void spliterator__always__returnsSpliteratorWithExpectedCharacteristics() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var spliterator = cut.spliterator();
        var result = spliterator.characteristics();

        assertThat(result & Spliterator.IMMUTABLE).isNotZero();
        assertThat(result & Spliterator.ORDERED).isNotZero();
        assertThat(result & Spliterator.DISTINCT).isNotZero();
    }

    @Test
    void stream__always__returnsElementStream() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var stream = cut.stream();
        var result = stream.toArray();

        assertThat(result).containsExactly(1L, 2L, 3L);
    }

    @Test
    void toArray__always__returnsBoxedArray() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var result = cut.toArray();

        assertThat(result).isInstanceOf(Object[].class)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    void toArray_array__passedEmptyArray__returnsBoxedArrayOfMatchingComponentType() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var result = cut.toArray(new Long[0]);

        assertThat(result).isInstanceOf(Long[].class).hasSize(3)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    void toArray_array__passedArrayWithMatchingSize__populatesAndReturnsArray() {
        var arr = new Long[3];
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    void toArray_array__passedArrayWithLargerSize__marksEndWithNull() {
        var arr = Stream.generate(() -> 0L).limit(5).toArray(Long[]::new);
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr)
                .containsExactly(1L, 2L, 3L, null, 0L);
    }

    @Test
    void toPrimitiveArray__always__returnsElementArray() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var result = cut.toPrimitiveArray();

        assertThat(result).containsExactly(1L, 2L, 3L);
    }

    @Test
    void toPrimitiveArray__multipleInvocations__returnsSeparateArrays() {
        var cut = PrimitiveCollections.setOf(new long[] { 1L, 2L, 3L });

        var arr1 = cut.toPrimitiveArray();
        var arr2 = cut.toPrimitiveArray();

        assertThat(arr1).isNotSameAs(arr2).containsExactly(arr2);
    }
}
