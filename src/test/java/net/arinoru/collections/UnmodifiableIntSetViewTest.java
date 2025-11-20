package net.arinoru.collections;

import net.arinoru.collections.Views.UnmodifiableIntSetView;
import net.arinoru.collections.Views.ForwardingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class UnmodifiableIntSetViewTest {
    @ParameterizedTest
    @EnumSource
    void add__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.add(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void addAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = Set.of(1);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void addAll_OfInt__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = PrimitiveSet.OfInt.of(1);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void addInt__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.addInt(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void clear__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(cut::clear);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @Test
    void contains__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.contains(1)).thenReturn(true);

        var result = cut.contains(1);

        assertThat(result).isTrue();
        verify(set).contains(1);
        verifyNoMoreInteractions(set);
    }

    @Test
    void contains__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.SHALLOW));
        when(set.iterator()).thenReturn(IntStream.of(1).iterator());

        var result = cut.contains(1);

        assertThat(result).isTrue();
        verify(cut).containsInt(1);
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void contains__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.contains(1));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsInt(1);
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void containsAll_Collection__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = (Collection<Integer>) mock(Collection.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        doReturn(true).when(set).containsAll(collection);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(set).containsAll(collection);
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_Collection__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = (Collection<Integer>) mock(Collection.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.SHALLOW));
        when(collection.stream()).thenReturn(Stream.of(1, 2));
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(cut).containsInt(1);
        verify(cut).containsInt(2);
        verify(set, atLeastOnce()).size();
        verify(set, atLeastOnce()).iterator();
        verify(collection).stream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_Collection__shallowViewPassedPrimitiveCollection__divertsToContainsAll_OfInt() {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.SHALLOW));
        when(collection.primitiveStream()).thenReturn(IntStream.of(1, 2));
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.containsAll((Collection<Integer>) collection);

        assertThat(result).isTrue();
        verify(cut).containsAll((Collection<Integer>) collection);
        verify(cut).containsAll(collection);
        verify(cut).containsInt(1);
        verify(cut).containsInt(2);
        verify(set, atLeastOnce()).size();
        verify(set, atLeastOnce()).iterator();
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_Collection__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = (Collection<Integer>) mock(Collection.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));
        when(collection.stream()).thenReturn(Stream.of(1));

        var t = catchThrowable(() -> cut.containsAll(collection));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsInt(1);
        verify(collection).stream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_Collection__minimalViewPassedPrimitiveCollection__divertsToContainsAll_OfInt() {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));
        when(collection.primitiveStream()).thenReturn(IntStream.of(1));

        var t = catchThrowable(() -> cut.containsAll((Collection<Integer>) collection));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsAll((Collection<Integer>) collection);
        verify(cut).containsAll(collection);
        verify(cut).containsInt(1);
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_OfInt__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        doReturn(true).when(set).containsAll(collection);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(set).containsAll(collection);
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_OfInt__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.SHALLOW));
        when(collection.primitiveStream()).thenReturn(IntStream.of(1, 2));
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(cut).containsInt(1);
        verify(cut).containsInt(2);
        verify(set, atLeastOnce()).size();
        verify(set, atLeastOnce()).iterator();
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll_OfInt__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));
        when(collection.primitiveStream()).thenReturn(IntStream.of(1));

        var t = catchThrowable(() -> cut.containsAll(collection));


        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsInt(1);
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsInt__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.containsInt(1)).thenReturn(true);

        var result = cut.containsInt(1);

        assertThat(result).isTrue();
        verify(set).containsInt(1);
        verifyNoMoreInteractions(set);
    }

    @Test
    void containsInt__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(IntStream.of(1).iterator());

        var result = cut.containsInt(1);

        assertThat(result).isTrue();
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void containsInt__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.containsInt(1));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__pureView__forwardsRequest() {
        // Note: Because a mock cannot verify behavior connected with the equals
        // method, it is necessary to use a custom class to validate the call.
        var set = new EqualsAndHashCodeTestContainers.IntSet(1);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);

        var result = cut.equals(Set.of(1));

        assertThat(result).isTrue();
        assertThat(set.calledEquals()).isTrue();
    }

    @Test
    void equals__shallowView__processesRequest() {
        // Note: Because a mock cannot verify behavior connected with the equals
        // method, it is necessary to use a custom class to validate the call.
        var set = new EqualsAndHashCodeTestContainers.IntSet(1);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);

        var result = cut.equals(Set.of(1));

        assertThat(result).isTrue();
        assertThat(set.calledEquals()).isFalse();
    }

    @Test
    void equals__minimalView__throwsException() {
        var set = new EqualsAndHashCodeTestContainers.IntSet(1);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.equals(Set.of(1)));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        assertThat(set.calledEquals()).isFalse();
    }

    @Test
    void forEach_Consumer__pureView__forwardsRequest() {
        var consumer = (Consumer<Integer>) mock(Consumer.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(set).forEach(consumer);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_Consumer__shallowView__processesRequest() {
        var consumer = (Consumer<Integer>) mock(Consumer.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(IntStream.of(1).iterator());

        cut.forEach(consumer);

        verify(set).iterator();
        verify(set).size();
        verify(consumer).accept(1);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_Consumer__minimalView__throwsException() {
        var consumer = (Consumer<Integer>) mock(Consumer.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_IntConsumer__pureView__forwardsRequest() {
        var consumer = mock(IntConsumer.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(set).forEach(consumer);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_IntConsumer__shallowView__processesRequest() {
        var consumer = mock(IntConsumer.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(IntStream.of(1).iterator());

        cut.forEach(consumer);

        verify(set).iterator();
        verify(set).size();
        verify(consumer).accept(1);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void forEach_IntConsumer__minimalView__throwsException() {
        var consumer = mock(IntConsumer.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(consumer, set);
    }

    @Test
    void hashCode__pureView__forwardsRequest() {
        // Note: Because a mock cannot verify behavior connected with the hashCode
        // method, it is necessary to use a custom class to validate the call.
        var set = new EqualsAndHashCodeTestContainers.IntSet(1);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(1);
        assertThat(set.calledHashCode()).isTrue();
    }

    @Test
    void hashCode__shallowView__processesRequest() {
        // Note: Because a mock cannot verify behavior connected with the equals
        // method, it is necessary to create a custom class to validate the call.
        var set = new EqualsAndHashCodeTestContainers.IntSet(1);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(1);
        assertThat(set.calledHashCode()).isFalse();
    }

    @Test
    void hashCode__minimalView__throwsException() {
        var set = new EqualsAndHashCodeTestContainers.IntSet(1);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::hashCode);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        assertThat(set.calledHashCode()).isFalse();
    }

    @Test
    void isEmpty__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.isEmpty()).thenReturn(true);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(set).isEmpty();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void isEmpty__shallowOrMinimalView__processesRequest(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void iterator__pureOrShallowView__returnsMaskedIterator(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);
        when(set.iterator()).thenReturn(iterator);

        var result = cut.iterator();

        assertThat(result).isNotSameAs(iterator)
                .isInstanceOf(UnmodifiableView.class);
        verify(set).iterator();
        verifyNoMoreInteractions(set, iterator);
    }

    @Test
    void iterator__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::iterator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(set);
    }

    @Test
    void parallelPrimitiveStream__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var stream = mock(IntStream.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.parallelPrimitiveStream()).thenReturn(stream);

        var result = cut.parallelPrimitiveStream();

        assertThat(result).isSameAs(stream);
        verify(set).parallelPrimitiveStream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void parallelPrimitiveStream__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.SHALLOW));
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.parallelPrimitiveStream();
        var result = stream.sum();

        assertThat(result).isEqualTo(3);
        verify(cut).spliterator();
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void parallelPrimitiveStream__minimalView__failsOnTerminalOperation() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

        var stream = cut.parallelPrimitiveStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void parallelStream__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var stream = (Stream<Integer>) mock(Stream.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.parallelStream()).thenReturn(stream);

        var result = cut.parallelStream();

        assertThat(result).isSameAs(stream);
        verify(set).parallelStream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void parallelStream__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.parallelStream();
        var result = stream.toList();

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    void parallelStream__minimalView__failsOnTerminalOperation() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

        var stream = cut.parallelStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void primitiveStream__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var stream = mock(IntStream.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.primitiveStream()).thenReturn(stream);

        var result = cut.primitiveStream();

        assertThat(result).isSameAs(stream);
        verify(set).primitiveStream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void primitiveStream__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.SHALLOW));
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.primitiveStream();
        var result = stream.sum();

        assertThat(result).isEqualTo(3);
        verify(cut).spliterator();
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void primitiveStream__minimalView__failsOnTerminalOperation() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

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
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.remove(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void removeAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = (Collection<Integer>) mock(Collection.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource
    void removeAll_OfInt__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource
    void removeInt__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeInt(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource
    void removeIf_IntPredicate__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var predicate  = mock(IntPredicate.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, predicate);
    }

    @ParameterizedTest
    @EnumSource
    void removeIf_Predicate__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var predicate = (Predicate<Integer>) mock(Predicate.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, predicate);
    }

    @ParameterizedTest
    @EnumSource
    void removeIfInt__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var predicate = mock(IntPredicate.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.removeIfInt(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, predicate);
    }

    @ParameterizedTest
    @EnumSource
    void retainAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = (Collection<Integer>) mock(Collection.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource
    void retainAll_OfInt__anyView__throwsException(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void size__anyView__forwardsRequest(ForwardingType forwardingType) {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, forwardingType);
        when(set.size()).thenReturn(123);

        var result = cut.size();

        assertThat(result).isEqualTo(123);
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void spliterator__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var spliterator = mock(Spliterator.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.spliterator()).thenReturn(spliterator);

        var result = cut.spliterator();

        assertThat(result).isSameAs(spliterator);
        verify(set).spliterator();
        verifyNoMoreInteractions(set, spliterator);
    }

    @Test
    void spliterator__shallowView__processesRequest() {
        var consumer = mock(IntConsumer.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.nextInt()).thenReturn(1);

        var spliterator = cut.spliterator();
        var result = spliterator.tryAdvance(consumer);

        assertThat(result).isTrue();
        verify(set).size();
        verify(set).iterator();
        verify(iterator).hasNext();
        verify(iterator).nextInt();
        verify(consumer).accept(1);
        verifyNoMoreInteractions(set, iterator, consumer);
    }

    @Test
    void spliterator__minimalView__failsOnBind() {
        var consumer = mock(IntConsumer.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

        var spliterator = cut.spliterator();
        var t = catchThrowable(() -> spliterator.tryAdvance(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(set, consumer);
    }

    @Test
    void stream__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var stream = (Stream<Integer>) mock(Stream.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.stream()).thenReturn(stream);

        var result = cut.stream();

        assertThat(result).isSameAs(stream);
        verify(set).stream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void stream__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.stream();
        var result = stream.toList();

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    void stream__minimalView__failsOnTerminalOperation() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

        var stream = cut.stream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var arr = new Object[0];
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.toArray()).thenReturn(arr);

        var result = cut.toArray();

        assertThat(result).isSameAs(arr);
        verify(set).toArray();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.toArray();

        assertThat(result).containsExactly(1, 2);
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_IntFunction__pureView__forwardsRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
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
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);
        when(set.iterator()).thenReturn(PrimitiveCollections.emptyIntIterator());

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
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));
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
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        var arr = new Object[0];
        when(set.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr);
        verify(set).toArray(arr);
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_TArray__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.toArray(arr);

        assertThat(result).containsExactly(1, 2);
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_TArray__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));
        var arr = new Object[0];

        var t = catchThrowable(() -> cut.toArray(arr));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(set).size();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toPrimitiveArray__pureView__forwardsRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var arr = new int[] { 1 };
        var cut = new UnmodifiableIntSetView(set, ForwardingType.PURE);
        when(set.toPrimitiveArray()).thenReturn(arr);

        var result = cut.toPrimitiveArray();

        assertThat(result).isSameAs(arr);
        verify(set).toPrimitiveArray();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toPrimitiveArray__shallowView__processesRequest() {
        var set = mock(PrimitiveSet.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntSetView(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(iterator);
        when(set.size()).thenReturn(3);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);

        var result = cut.toPrimitiveArray();

        assertThat(result).containsExactly(1, 2, 3);
        verify(set).iterator();
        verify(set).size();
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verifyNoMoreInteractions(set, iterator);
    }

    @Test
    void toPrimitiveArray__minimalView__throwsException() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = spy(new UnmodifiableIntSetView(set, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toPrimitiveArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }
}
