package net.arinoru.collections;

import net.arinoru.collections.Views.UnmodifiableLongSetView;
import net.arinoru.collections.Views.ForwardingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
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
class UnmodifiableLongSetViewTest {
    @ParameterizedTest
    @EnumSource
    void add__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.add(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void addAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = Set.of(1L);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void addAll_OfLong__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = PrimitiveSet.OfLong.of(1L);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void addLong__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.addLong(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void clear__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(cut::clear);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @Test
    void contains__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.contains(1L)).thenReturn(true);

        var result = cut.contains(1L);

        assertThat(result).isTrue();
        verify(set).contains(1L);
        verifyNoMoreInteractions(set);
    }

    @Test
    void contains__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.SHALLOW));
        when(set.iterator()).thenReturn(LongStream.of(1L).iterator());

        var result = cut.contains(1L);

        assertThat(result).isTrue();
        verify(cut).containsLong(1L);
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void contains__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.contains(1L));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsLong(1L);
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void containsAll_Collection__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        doReturn(true).when(set).containsAll(collection);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(set).containsAll(collection);
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_Collection__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.SHALLOW));
        when(collection.stream()).thenReturn(Stream.of(1L, 2L));
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(cut).containsLong(1L);
        verify(cut).containsLong(2L);
        verify(set, atLeastOnce()).size();
        verify(set, atLeastOnce()).iterator();
        verify(collection).stream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_Collection__shallowViewPassedPrimitiveCollection__divertsToContainsAll_OfLong() {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.SHALLOW));
        when(collection.primitiveStream()).thenReturn(LongStream.of(1L, 2L));
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.containsAll((Collection<Long>) collection);

        assertThat(result).isTrue();
        verify(cut).containsAll((Collection<Long>) collection);
        verify(cut).containsAll(collection);
        verify(cut).containsLong(1L);
        verify(cut).containsLong(2L);
        verify(set, atLeastOnce()).size();
        verify(set, atLeastOnce()).iterator();
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_Collection__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));
        when(collection.stream()).thenReturn(Stream.of(1L));

        var t = catchThrowable(() -> cut.containsAll(collection));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsLong(1L);
        verify(collection).stream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_Collection__minimalViewPassedPrimitiveCollection__divertsToContainsAll_OfLong() {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));
        when(collection.primitiveStream()).thenReturn(LongStream.of(1L));

        var t = catchThrowable(() -> cut.containsAll((Collection<Long>) collection));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsAll((Collection<Long>) collection);
        verify(cut).containsAll(collection);
        verify(cut).containsLong(1L);
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_OfLong__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        doReturn(true).when(set).containsAll(collection);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(set).containsAll(collection);
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_OfLong__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.SHALLOW));
        when(collection.primitiveStream()).thenReturn(LongStream.of(1L, 2L));
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(cut).containsLong(1L);
        verify(cut).containsLong(2L);
        verify(set, atLeastOnce()).size();
        verify(set, atLeastOnce()).iterator();
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_OfLong__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));
        when(collection.primitiveStream()).thenReturn(LongStream.of(1L));

        var t = catchThrowable(() -> cut.containsAll(collection));


        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsLong(1L);
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsLong__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.containsLong(1L)).thenReturn(true);

        var result = cut.containsLong(1L);

        assertThat(result).isTrue();
        verify(set).containsLong(1L);
        verifyNoMoreInteractions(set);
    }

    @Test
    void containsLong__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(LongStream.of(1L).iterator());

        var result = cut.containsLong(1L);

        assertThat(result).isTrue();
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void containsLong__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.containsLong(1L));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__pureView__forwardsRequest() {
        // Note: Because a mock cannot verify behavior connected with the equals
        // method, it is necessary to use a custom class to validate the call.
        var set = new EqualsAndHashCodeTestContainers.LongSet(1L);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);

        var result = cut.equals(Set.of(1L));

        assertThat(result).isTrue();
        assertThat(set.calledEquals()).isTrue();
    }

    @Test
    void equals__shallowView__processesRequest() {
        // Note: Because a mock cannot verify behavior connected with the equals
        // method, it is necessary to use a custom class to validate the call.
        var set = new EqualsAndHashCodeTestContainers.LongSet(1L);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);

        var result = cut.equals(Set.of(1L));

        assertThat(result).isTrue();
        assertThat(set.calledEquals()).isFalse();
    }

    @Test
    void equals__minimalView__throwsException() {
        var set = new EqualsAndHashCodeTestContainers.LongSet(1L);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.equals(Set.of(1L)));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        assertThat(set.calledEquals()).isFalse();
    }

    @Test
    void forEach_Consumer__pureView__forwardsRequest() {
        var consumer = (Consumer<Long>) mock(Consumer.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(set).forEach(consumer);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_Consumer__shallowView__processesRequest() {
        var consumer = (Consumer<Long>) mock(Consumer.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(LongStream.of(1L).iterator());

        cut.forEach(consumer);

        verify(set).iterator();
        verify(set).size();
        verify(consumer).accept(1L);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_Consumer__minimalView__throwsException() {
        var consumer = (Consumer<Long>) mock(Consumer.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_LongConsumer__pureView__forwardsRequest() {
        var consumer = mock(LongConsumer.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(set).forEach(consumer);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_LongConsumer__shallowView__processesRequest() {
        var consumer = mock(LongConsumer.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(LongStream.of(1L).iterator());

        cut.forEach(consumer);

        verify(set).iterator();
        verify(set).size();
        verify(consumer).accept(1L);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_LongConsumer__minimalView__throwsException() {
        var consumer = mock(LongConsumer.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void hashCode__pureView__forwardsRequest() {
        // Note: Because a mock cannot verify behavior connected with the hashCode
        // method, it is necessary to use a custom class to validate the call.
        var set = new EqualsAndHashCodeTestContainers.LongSet(1L);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(Long.hashCode(1L));
        assertThat(set.calledHashCode()).isTrue();
    }

    @Test
    void hashCode__shallowView__processesRequest() {
        // Note: Because a mock cannot verify behavior connected with the equals
        // method, it is necessary to create a custom class to validate the call.
        var set = new EqualsAndHashCodeTestContainers.LongSet(1L);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(Long.hashCode(1L));
        assertThat(set.calledHashCode()).isFalse();
    }

    @Test
    void hashCode__minimalView__throwsException() {
        var set = new EqualsAndHashCodeTestContainers.LongSet(1L);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::hashCode);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        assertThat(set.calledHashCode()).isFalse();
    }

    @Test
    void isEmpty__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.isEmpty()).thenReturn(true);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(set).isEmpty();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void isEmpty__shallowOrMinimalView__processesRequest(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void iterator__pureOrShallowView__returnsMaskedIterator(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);
        when(set.iterator()).thenReturn(iterator);

        var result = cut.iterator();

        assertThat(result).isNotSameAs(iterator)
                .isInstanceOf(UnmodifiableView.class);
        verify(set).iterator();
        verifyNoMoreInteractions(set, iterator);
    }

    @Test
    void iterator__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::iterator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(set);
    }

    @Test
    void parallelPrimitiveStream__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var stream = mock(LongStream.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.parallelPrimitiveStream()).thenReturn(stream);

        var result = cut.parallelPrimitiveStream();

        assertThat(result).isSameAs(stream);
        verify(set).parallelPrimitiveStream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void parallelPrimitiveStream__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.SHALLOW));
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var stream = cut.parallelPrimitiveStream();
        var result = stream.sum();

        assertThat(result).isEqualTo(3L);
        verify(cut).spliterator();
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void parallelPrimitiveStream__minimalView__failsOnTerminalOperation() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var stream = cut.parallelPrimitiveStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void parallelStream__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var stream = (Stream<Long>) mock(Stream.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.parallelStream()).thenReturn(stream);

        var result = cut.parallelStream();

        assertThat(result).isSameAs(stream);
        verify(set).parallelStream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void parallelStream__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var stream = cut.parallelStream();
        var result = stream.toList();

        assertThat(result).containsExactly(1L, 2L);
    }

    @Test
    void parallelStream__minimalView__failsOnTerminalOperation() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var stream = cut.parallelStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void primitiveStream__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var stream = mock(LongStream.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.primitiveStream()).thenReturn(stream);

        var result = cut.primitiveStream();

        assertThat(result).isSameAs(stream);
        verify(set).primitiveStream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void primitiveStream__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.SHALLOW));
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var stream = cut.primitiveStream();
        var result = stream.sum();

        assertThat(result).isEqualTo(3L);
        verify(cut).spliterator();
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void primitiveStream__minimalView__failsOnTerminalOperation() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var stream = cut.primitiveStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void remove__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.remove(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void removeAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource
    void removeAll_OfLong__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource
    void removeLong__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeLong(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void removeIf_LongPredicate__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var predicate  = mock(LongPredicate.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, predicate);
    }

    @ParameterizedTest
    @EnumSource
    void removeIf_Predicate__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var predicate = (Predicate<Long>) mock(Predicate.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, predicate);
    }

    @ParameterizedTest
    @EnumSource
    void removeIfLong__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var predicate = mock(LongPredicate.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeIfLong(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, predicate);
    }

    @ParameterizedTest
    @EnumSource
    void retainAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource
    void retainAll_OfLong__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void size__anyView__forwardsRequest(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, forwardingType);
        when(set.size()).thenReturn(123);

        var result = cut.size();

        assertThat(result).isEqualTo(123);
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void spliterator__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var spliterator = mock(Spliterator.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.spliterator()).thenReturn(spliterator);

        var result = cut.spliterator();

        assertThat(result).isSameAs(spliterator);
        verify(set).spliterator();
        verifyNoMoreInteractions(set, spliterator);
    }

    @Test
    void spliterator__shallowView__processesRequest() {
        var consumer = mock(LongConsumer.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.nextLong()).thenReturn(1L);

        var spliterator = cut.spliterator();
        var result = spliterator.tryAdvance(consumer);

        assertThat(result).isTrue();
        verify(set).size();
        verify(set).iterator();
        verify(iterator).hasNext();
        verify(iterator).nextLong();
        verify(consumer).accept(1L);
        verifyNoMoreInteractions(set, iterator, consumer);
    }

    @Test
    void spliterator__minimalView__failsOnBind() {
        var consumer = mock(LongConsumer.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var spliterator = cut.spliterator();
        var t = catchThrowable(() -> spliterator.tryAdvance(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(set, consumer);
    }

    @Test
    void stream__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var stream = (Stream<Long>) mock(Stream.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.stream()).thenReturn(stream);

        var result = cut.stream();

        assertThat(result).isSameAs(stream);
        verify(set).stream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void stream__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var stream = cut.stream();
        var result = stream.toList();

        assertThat(result).containsExactly(1L, 2L);
    }

    @Test
    void stream__minimalView__failsOnTerminalOperation() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var stream = cut.stream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var arr = new Object[0];
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.toArray()).thenReturn(arr);

        var result = cut.toArray();

        assertThat(result).isSameAs(arr);
        verify(set).toArray();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.toArray();

        assertThat(result).containsExactly(1L, 2L);
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_IntFunction__pureView__forwardsRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        var arr = new Object[0];
        when(set.toArray(intFunction)).thenReturn(arr);

        var result = cut.toArray(intFunction);

        assertThat(result).isSameAs(arr);
        verify(set).toArray(intFunction);
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_IntFunction__shallowView__processesRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);
        when(set.iterator()).thenReturn(PrimitiveCollections.emptyLongIterator());

        var result = cut.toArray(intFunction);

        assertThat(result).isSameAs(arr);
        verify(set).size();
        verify(set).iterator();
        verify(intFunction).apply(0);
        verifyNoMoreInteractions(set, intFunction);
    }

    @Test
    void toArray_IntFunction__minimalView__throwsException() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);

        var t = catchThrowable(() -> cut.toArray(intFunction));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).toArray(arr);
        verify(set).size();
        verify(intFunction).apply(0);
        verifyNoMoreInteractions(set, intFunction);
    }

    @Test
    void toArray_TArray__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        var arr = new Object[0];
        when(set.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr);
        verify(set).toArray(arr);
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_TArray__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());

        var result = cut.toArray(arr);

        assertThat(result).containsExactly(1L, 2L);
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_TArray__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));
        var arr = new Object[0];

        var t = catchThrowable(() -> cut.toArray(arr));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(set).size();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toPrimitiveArray__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var arr = new long[] { 1L };
        var cut = new UnmodifiableLongSetView(set, ForwardingType.PURE);
        when(set.toPrimitiveArray()).thenReturn(arr);

        var result = cut.toPrimitiveArray();

        assertThat(result).isSameAs(arr);
        verify(set).toPrimitiveArray();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toPrimitiveArray__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfLong.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(iterator);
        when(set.size()).thenReturn(3);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextLong()).thenReturn(1L, 2L, 3L);

        var result = cut.toPrimitiveArray();

        assertThat(result).containsExactly(1L, 2L, 3L);
        verify(set).iterator();
        verify(set).size();
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextLong();
        verifyNoMoreInteractions(set, iterator);
    }

    @Test
    void toPrimitiveArray__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = spy(new UnmodifiableLongSetView(set, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toPrimitiveArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }
}
