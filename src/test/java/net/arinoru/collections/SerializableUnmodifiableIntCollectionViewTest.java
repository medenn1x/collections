package net.arinoru.collections;

import net.arinoru.collections.Views.SerializableUnmodifiableIntCollectionView;
import net.arinoru.collections.Views.ForwardingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collection;
import java.util.PrimitiveIterator;
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
class SerializableUnmodifiableIntCollectionViewTest {
    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void add__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.add(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void addAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void addAll_OfInt__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void addInt__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.addInt(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void clear__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(cut::clear);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.contains(1)).thenReturn(true);

        var result = cut.contains(1);

        assertThat(result).isTrue();
        verify(collection).contains(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(IntStream.of(1).iterator());

        var result = cut.contains(1);

        assertThat(result).isTrue();
        verify(cut).containsInt(1);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.contains(1));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsInt(1);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection1, ForwardingType.PURE);
        doReturn(true).when(collection1).containsAll(collection2);

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__shallowView__processesRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection1, ForwardingType.SHALLOW));
        when(collection2.stream()).thenReturn(Stream.of(1, 2));
        when(collection1.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(cut).containsInt(1);
        verify(cut).containsInt(2);
        verify(collection1, atLeastOnce()).size();
        verify(collection1, atLeastOnce()).iterator();
        verify(collection2).stream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__shallowViewPassedPrimitiveCollection__divertsToContainsAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection1, ForwardingType.SHALLOW));
        when(collection2.primitiveStream()).thenReturn(IntStream.of(1, 2));
        when(collection1.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.containsAll((Collection<Integer>) collection2);

        assertThat(result).isTrue();
        verify(cut).containsAll((Collection<Integer>) collection2);
        verify(cut).containsAll(collection2);
        verify(cut).containsInt(1);
        verify(cut).containsInt(2);
        verify(collection1, atLeastOnce()).size();
        verify(collection1, atLeastOnce()).iterator();
        verify(collection2).primitiveStream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__minimalView__throwsException() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection1, ForwardingType.MINIMAL));
        when(collection2.stream()).thenReturn(Stream.of(1));

        var t = catchThrowable(() -> cut.containsAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsInt(1);
        verify(collection2).stream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__minimalViewPassedPrimitiveCollection__divertsToContainsAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection1, ForwardingType.MINIMAL));
        when(collection2.primitiveStream()).thenReturn(IntStream.of(1));

        var t = catchThrowable(() -> cut.containsAll((Collection<Integer>) collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsAll((Collection<Integer>) collection2);
        verify(cut).containsAll(collection2);
        verify(cut).containsInt(1);
        verify(collection2).primitiveStream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfInt__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection1, ForwardingType.PURE);
        doReturn(true).when(collection1).containsAll(collection2);

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfInt__shallowView__processesRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection1, ForwardingType.SHALLOW));
        when(collection2.primitiveStream()).thenReturn(IntStream.of(1, 2));
        when(collection1.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(cut).containsInt(1);
        verify(cut).containsInt(2);
        verify(collection1, atLeastOnce()).size();
        verify(collection1, atLeastOnce()).iterator();
        verify(collection2).primitiveStream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfInt__minimalView__throwsException() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection1, ForwardingType.MINIMAL));
        when(collection2.primitiveStream()).thenReturn(IntStream.of(1));

        var t = catchThrowable(() -> cut.containsAll(collection2));


        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).containsInt(1);
        verify(collection2).primitiveStream();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsInt__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.containsInt(1)).thenReturn(true);

        var result = cut.containsInt(1);

        assertThat(result).isTrue();
        verify(collection).containsInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsInt__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(IntStream.of(1).iterator());

        var result = cut.containsInt(1);

        assertThat(result).isTrue();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsInt__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.containsInt(1));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource
    void equals__anyView__processesRequest(ForwardingType forwardingType) {
        // Note: Because a mock cannot verify behavior connected with the equals
        // method, it is necessary to use a custom class to validate the call.
        // Note additionally that equals is a special case for
        // base collection views in that even in a PURE view they do not forward
        // the call; if there is a good reason for forwarding equals, it must be
        // overridden by a subclass.
        var collection = new EqualsAndHashCodeTestContainers.IntCollection(1);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        @SuppressWarnings("EqualsBetweenInconvertibleTypes")
        var result = cut.equals(collection);

        assertThat(result).isFalse();
        assertThat(collection.calledEquals()).isFalse();
    }

    @Test
    void forEach_Consumer__pureView__forwardsRequest() {
        var consumer = (Consumer<Integer>) mock(Consumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(collection).forEach(consumer);
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void forEach_Consumer__shallowView__processesRequest() {
        var consumer = (Consumer<Integer>) mock(Consumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(IntStream.of(1).iterator());

        cut.forEach(consumer);

        verify(collection).iterator();
        verify(collection).size();
        verify(consumer).accept(1);
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void forEach_Consumer__minimalView__throwsException() {
        var consumer = (Consumer<Integer>) mock(Consumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void forEach_IntConsumer__pureView__forwardsRequest() {
        var consumer = mock(IntConsumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(collection).forEach(consumer);
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void forEach_IntConsumer__shallowView__processesRequest() {
        var consumer = mock(IntConsumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(IntStream.of(1).iterator());

        cut.forEach(consumer);

        verify(collection).iterator();
        verify(collection).size();
        verify(consumer).accept(1);
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void forEach_IntConsumer__minimalView__throwsException() {
        var consumer = mock(IntConsumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void hashCode__pureView__forwardsRequest() {
        // Note: Because a mock cannot verify behavior connected with the hashCode
        // method, it is necessary to use a custom class to validate the call.
        var collection = new EqualsAndHashCodeTestContainers.IntCollection(1);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);

        var result = cut.hashCode();

        // Must check call status before getting hash code for comparison
        assertThat(collection.calledHashCode()).isTrue();
        assertThat(result).isEqualTo(collection.hashCode());
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void hashCode__shallowOrMinimalView__processesRequest(ForwardingType forwardingType) {
        // Note: Because a mock cannot verify behavior connected with the hashCode
        // method, it is necessary to use a custom class to validate the call.
        var collection = new EqualsAndHashCodeTestContainers.IntCollection(1);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        //noinspection ResultOfMethodCallIgnored
        cut.hashCode();

        assertThat(collection.calledHashCode()).isFalse();
    }

    @Test
    void isEmpty__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.isEmpty()).thenReturn(true);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(collection).isEmpty();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void isEmpty__shallowOrMinimalView__processesRequest(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void iterator__pureOrShallowView__returnsMaskedIterator(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);
        when(collection.iterator()).thenReturn(iterator);

        var result = cut.iterator();

        assertThat(result).isNotSameAs(iterator)
                .isInstanceOf(UnmodifiableView.class);
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void iterator__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::iterator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void parallelPrimitiveStream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var stream = mock(IntStream.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.parallelPrimitiveStream()).thenReturn(stream);

        var result = cut.parallelPrimitiveStream();

        assertThat(result).isSameAs(stream);
        verify(collection).parallelPrimitiveStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void parallelPrimitiveStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.parallelPrimitiveStream();
        var result = stream.sum();

        assertThat(result).isEqualTo(3);
        verify(cut).spliterator();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void parallelPrimitiveStream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

        var stream = cut.parallelPrimitiveStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void parallelStream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var stream = (Stream<Integer>) mock(Stream.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.parallelStream()).thenReturn(stream);

        var result = cut.parallelStream();

        assertThat(result).isSameAs(stream);
        verify(collection).parallelStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void parallelStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.parallelStream();
        var result = stream.toList();

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    void parallelStream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

        var stream = cut.parallelStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void primitiveStream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var stream = mock(IntStream.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.primitiveStream()).thenReturn(stream);

        var result = cut.primitiveStream();

        assertThat(result).isSameAs(stream);
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void primitiveStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.primitiveStream();
        var result = stream.sum();

        assertThat(result).isEqualTo(3);
        verify(cut).spliterator();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void primitiveStream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

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
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.remove(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeAll_OfInt__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeInt__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeInt(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeIf_IntPredicate__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = mock(IntPredicate.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeIf_Predicate__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = (Predicate<Integer>) mock(Predicate.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeIfInt__anyView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = mock(IntPredicate.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeIfInt(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection, predicate);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void retainAll_Collection__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void retainAll_OfInt__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection1, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void size__anyView__forwardsRequest(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, forwardingType);
        when(collection.size()).thenReturn(123);

        var result = cut.size();

        assertThat(result).isEqualTo(123);
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void spliterator__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var spliterator = mock(Spliterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.spliterator()).thenReturn(spliterator);

        var result = cut.spliterator();

        assertThat(result).isSameAs(spliterator);
        verify(collection).spliterator();
        verifyNoMoreInteractions(collection, spliterator);
    }

    @Test
    void spliterator__shallowView__processesRequest() {
        var consumer = mock(IntConsumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.nextInt()).thenReturn(1);

        var spliterator = cut.spliterator();
        var result = spliterator.tryAdvance(consumer);

        assertThat(result).isTrue();
        verify(collection).size();
        verify(collection).iterator();
        verify(iterator).hasNext();
        verify(iterator).nextInt();
        verify(consumer).accept(1);
        verifyNoMoreInteractions(collection, iterator, consumer);
    }

    @Test
    void spliterator__minimalView__failsOnBind() {
        var consumer = mock(IntConsumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

        var spliterator = cut.spliterator();
        var t = catchThrowable(() -> spliterator.tryAdvance(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void stream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var stream = (Stream<Integer>) mock(Stream.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.stream()).thenReturn(stream);

        var result = cut.stream();

        assertThat(result).isSameAs(stream);
        verify(collection).stream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void stream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.stream();
        var result = stream.toList();

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    void stream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

        var stream = cut.stream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var arr = new Object[0];
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.toArray()).thenReturn(arr);

        var result = cut.toArray();

        assertThat(result).isSameAs(arr);
        verify(collection).toArray();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.toArray();

        assertThat(result).containsExactly(1, 2);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_IntFunction__pureView__forwardsRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
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
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyIntIterator());

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
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));
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
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        var arr = new Object[0];
        when(collection.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr);
        verify(collection).toArray(arr);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_TArray__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var result = cut.toArray(arr);

        assertThat(result).containsExactly(1, 2);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_TArray__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));
        var arr = new Object[0];

        var t = catchThrowable(() -> cut.toArray(arr));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(collection).size();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toPrimitiveArray__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var arr = new int[] { 1 };
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.toPrimitiveArray()).thenReturn(arr);

        var result = cut.toPrimitiveArray();

        assertThat(result).isSameAs(arr);
        verify(collection).toPrimitiveArray();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toPrimitiveArray__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(collection.size()).thenReturn(3);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);

        var result = cut.toPrimitiveArray();

        assertThat(result).containsExactly(1, 2, 3);
        verify(collection).iterator();
        verify(collection).size();
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void toPrimitiveArray__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toPrimitiveArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }
}
