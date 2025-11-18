package net.arinoru.collections;

import net.arinoru.collections.Views.UnmodifiableLongCollectionView;
import net.arinoru.collections.Views.ForwardingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class UnmodifiableLongCollectionViewTest {
    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void add__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.add(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void addAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        var cut = new UnmodifiableLongCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void addAll_OfLong__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void addLong__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.addLong(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void clear__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var t = catchThrowable(cut::clear);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.contains(1L)).thenReturn(true);

        var result = cut.contains(1L);

        assertThat(result).isTrue();
        verify(collection).contains(1L);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(LongStream.of(1L).iterator());

        var result = cut.contains(1L);

        assertThat(result).isTrue();
        verify(cut).containsLong(1L);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.contains(1L));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsLong(1L);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        var cut = new UnmodifiableLongCollectionView(collection1, ForwardingType.PURE);
        doReturn(true).when(collection1).containsAll(collection2);

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__shallowView__processesRequest() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection1, ForwardingType.SHALLOW));
        when(collection2.stream()).thenReturn(Stream.of(1L, 2L));
        when(collection1.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(cut).containsLong(1L);
        verify(cut).containsLong(2L);
        verify(collection1, atLeastOnce()).size();
        verify(collection1, atLeastOnce()).iterator();
        verify(collection2).stream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__shallowViewPassedPrimitiveCollection__divertsToContainsAll_OfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection1, ForwardingType.SHALLOW));
        when(collection2.primitiveStream()).thenReturn(LongStream.of(1L, 2L));
        when(collection1.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.containsAll((Collection<Long>) collection2);

        assertThat(result).isTrue();
        verify(cut).containsAll((Collection<Long>) collection2);
        verify(cut).containsAll(collection2);
        verify(cut).containsLong(1L);
        verify(cut).containsLong(2L);
        verify(collection1, atLeastOnce()).size();
        verify(collection1, atLeastOnce()).iterator();
        verify(collection2).primitiveStream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__minimalView__throwsException() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection1, ForwardingType.MINIMAL));
        when(collection2.stream()).thenReturn(Stream.of(1L));

        var t = catchThrowable(() -> cut.containsAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsLong(1L);
        verify(collection2).stream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__minimalViewPassedPrimitiveCollection__divertsToContainsAll_OfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection1, ForwardingType.MINIMAL));
        when(collection2.primitiveStream()).thenReturn(LongStream.of(1L));

        var t = catchThrowable(() -> cut.containsAll((Collection<Long>) collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsAll((Collection<Long>) collection2);
        verify(cut).containsAll(collection2);
        verify(cut).containsLong(1L);
        verify(collection2).primitiveStream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfLong__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection1, ForwardingType.PURE);
        doReturn(true).when(collection1).containsAll(collection2);

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfLong__shallowView__processesRequest() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection1, ForwardingType.SHALLOW));
        when(collection2.primitiveStream()).thenReturn(LongStream.of(1L, 2L));
        when(collection1.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(cut).containsLong(1L);
        verify(cut).containsLong(2L);
        verify(collection1, atLeastOnce()).size();
        verify(collection1, atLeastOnce()).iterator();
        verify(collection2).primitiveStream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfLong__minimalView__throwsException() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection1, ForwardingType.MINIMAL));
        when(collection2.primitiveStream()).thenReturn(LongStream.of(1L));

        var t = catchThrowable(() -> cut.containsAll(collection2));


        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsLong(1L);
        verify(collection2).primitiveStream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsLong__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.containsLong(1L)).thenReturn(true);

        var result = cut.containsLong(1L);

        assertThat(result).isTrue();
        verify(collection).containsLong(1L);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsLong__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(LongStream.of(1L).iterator());

        var result = cut.containsLong(1L);

        assertThat(result).isTrue();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsLong__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.containsLong(1L));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void isEmpty__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.isEmpty()).thenReturn(true);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(collection).isEmpty();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void isEmpty__shallowOrMinimalView__processesRequest(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void iterator__pureOrShallowView__returnsMaskedIterator(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);
        when(collection.iterator()).thenReturn(iterator);

        var result = cut.iterator();

        assertThat(result).isNotSameAs(iterator)
                .isInstanceOf(UnmodifiableView.class);
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void iterator__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::iterator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void parallelPrimitiveStream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var stream = mock(LongStream.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.parallelPrimitiveStream()).thenReturn(stream);

        var result = cut.parallelPrimitiveStream();

        assertThat(result).isSameAs(stream);
        verify(collection).parallelPrimitiveStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void parallelPrimitiveStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var stream = cut.parallelPrimitiveStream();
        var result = stream.sum();

        assertThat(result).isEqualTo(3L);
        verify(cut).spliterator();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void parallelPrimitiveStream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var stream = cut.parallelPrimitiveStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void parallelStream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var stream = (Stream<Long>) mock(Stream.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.parallelStream()).thenReturn(stream);

        var result = cut.parallelStream();

        assertThat(result).isSameAs(stream);
        verify(collection).parallelStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void parallelStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var stream = cut.parallelStream();
        var result = stream.toList();

        assertThat(result).containsExactly(1L, 2L);
    }

    @Test
    void parallelStream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var stream = cut.parallelStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void primitiveStream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var stream = mock(LongStream.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.primitiveStream()).thenReturn(stream);

        var result = cut.primitiveStream();

        assertThat(result).isSameAs(stream);
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void primitiveStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var stream = cut.primitiveStream();
        var result = stream.sum();

        assertThat(result).isEqualTo(3L);
        verify(cut).spliterator();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void primitiveStream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var stream = cut.primitiveStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void remove__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.remove(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        var cut = new UnmodifiableLongCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeAll_OfLong__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeLong__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeLong(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeIf_LongPredicate__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var predicate = mock(LongPredicate.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeIf_Predicate__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var predicate = (Predicate<Long>) mock(Predicate.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeIfLong__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var predicate = mock(LongPredicate.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeIfLong(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection, predicate);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void retainAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        var cut = new UnmodifiableLongCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void retainAll_OfLong__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void size__anyView__forwardsRequest(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, forwardingType);
        when(collection.size()).thenReturn(123);

        var result = cut.size();

        assertThat(result).isEqualTo(123);
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void spliterator__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var spliterator = mock(Spliterator.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.spliterator()).thenReturn(spliterator);

        var result = cut.spliterator();

        assertThat(result).isSameAs(spliterator);
        verify(collection).spliterator();
        verifyNoMoreInteractions(collection, spliterator);
    }

    @Test
    void spliterator__shallowView__processesRequest() {
        var consumer = mock(LongConsumer.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.nextLong()).thenReturn(1L);

        var spliterator = cut.spliterator();
        var result = spliterator.tryAdvance(consumer);

        assertThat(result).isTrue();
        verify(collection).size();
        verify(collection).iterator();
        verify(iterator).hasNext();
        verify(iterator).nextLong();
        verify(consumer).accept(1L);
        verifyNoMoreInteractions(collection, iterator, consumer);
    }

    @Test
    void spliterator__minimalView__failsOnBind() {
        var consumer = mock(LongConsumer.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var spliterator = cut.spliterator();
        var t = catchThrowable(() -> spliterator.tryAdvance(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void stream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var stream = (Stream<Long>) mock(Stream.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.stream()).thenReturn(stream);

        var result = cut.stream();

        assertThat(result).isSameAs(stream);
        verify(collection).stream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void stream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var stream = cut.stream();
        var result = stream.toList();

        assertThat(result).containsExactly(1L, 2L);
    }

    @Test
    void stream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var stream = cut.stream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var arr = new Object[0];
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.toArray()).thenReturn(arr);

        var result = cut.toArray();

        assertThat(result).isSameAs(arr);
        verify(collection).toArray();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.toArray();

        assertThat(result).containsExactly(1L, 2L);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_IntFunction__pureView__forwardsRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        var arr = new Object[0];
        when(collection.toArray(intFunction)).thenReturn(arr);

        var result = cut.toArray(intFunction);

        assertThat(result).isSameAs(arr);
        verify(collection).toArray(intFunction);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_IntFunction__shallowView__processesRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyLongIterator());

        var result = cut.toArray(intFunction);

        assertThat(result).isSameAs(arr);
        verify(collection).size();
        verify(collection).iterator();
        verify(intFunction).apply(0);
        verifyNoMoreInteractions(collection, intFunction);
    }

    @Test
    void toArray_IntFunction__minimalView__throwsException() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);

        var t = catchThrowable(() -> cut.toArray(intFunction));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).toArray(arr);
        verify(collection).size();
        verify(intFunction).apply(0);
        verifyNoMoreInteractions(collection, intFunction);
    }

    @Test
    void toArray_TArray__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        var arr = new Object[0];
        when(collection.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr);
        verify(collection).toArray(arr);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_TArray__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.toArray(arr);

        assertThat(result).containsExactly(1L, 2L);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_TArray__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));
        var arr = new Object[0];

        var t = catchThrowable(() -> cut.toArray(arr));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(collection).size();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toPrimitiveArray__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var arr = new long[] { 1L };
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.PURE);
        when(collection.toPrimitiveArray()).thenReturn(arr);

        var result = cut.toPrimitiveArray();

        assertThat(result).isSameAs(arr);
        verify(collection).toPrimitiveArray();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toPrimitiveArray__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(collection.size()).thenReturn(3);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextLong()).thenReturn(1L, 2L, 3L);

        var result = cut.toPrimitiveArray();

        assertThat(result).containsExactly(1L, 2L, 3L);
        verify(collection).iterator();
        verify(collection).size();
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextLong();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void toPrimitiveArray__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toPrimitiveArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }
}
