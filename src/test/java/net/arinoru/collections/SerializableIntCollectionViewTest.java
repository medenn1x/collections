package net.arinoru.collections;

import net.arinoru.collections.Views.SerializableIntCollectionView;
import net.arinoru.collections.Views.ForwardingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
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
class SerializableIntCollectionViewTest {
    @Test
    void add__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.add(1)).thenReturn(true);

        var result = cut.add(1);

        assertThat(result).isTrue();
        verify(collection).add(1);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void add__shallowOrMinimalView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, forwardingType));

        var t = catchThrowable(() -> cut.add(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verify(cut).addInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void addAll_Collection__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = Set.of(1, 2);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.PURE);
        when(collection1.addAll(collection2)).thenReturn(true);

        var result = cut.addAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll(collection2);
        verifyNoMoreInteractions(collection1);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void addAll_Collection__shallowOrMinimalView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = Set.of(1);
        var cut = spy(new SerializableIntCollectionView(collection1, forwardingType));

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verify(cut).addInt(1);
        verifyNoMoreInteractions(collection1);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void addAll_Collection__shallowOrMinimalViewPassedPrimitiveCollection__divertsToAddAll_OfInt(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) PrimitiveSet.OfInt.of(1);
        var cut = spy(new SerializableIntCollectionView(collection1, forwardingType));

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verify(cut).addAll(collection2);
        verify(cut).addAll((PrimitiveCollection.OfInt) collection2);
        verify(cut).addInt(1);
        verifyNoMoreInteractions(collection1);
    }

    @Test
    void addAll_OfInt__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = PrimitiveSet.OfInt.of(1);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.PURE);
        when(collection1.addAll(collection2)).thenReturn(true);

        var result = cut.addAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll(collection2);
        verifyNoMoreInteractions(collection1);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void addAll_OfInt__shallowOrMinimalView__throwsException(ForwardingType forwardingType) {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = PrimitiveSet.OfInt.of(1);
        var cut = spy(new SerializableIntCollectionView(collection1, forwardingType));

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verify(cut).addAll(collection2);
        verify(cut).addInt(1);
        verifyNoMoreInteractions(collection1);
    }

    @Test
    void addInt__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.addInt(1)).thenReturn(true);

        var result = cut.addInt(1);

        assertThat(result).isTrue();
        verify(collection).addInt(1);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void addInt__shallowOrMinimalView__throwsException(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, forwardingType);

        var t = catchThrowable(() -> cut.addInt(1));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void clear__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);

        cut.clear();

        verify(collection).clear();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void clear__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);

        cut.clear();
        verify(collection).iterator();
        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(iterator, times(2)).remove();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void clear__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::clear);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.contains(1)).thenReturn(true);

        var result = cut.contains(1);

        assertThat(result).isTrue();
        verify(collection).contains(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.SHALLOW));
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

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
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.PURE);
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
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW));
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
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW));
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
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));
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
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));
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
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.PURE);
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
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW));
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
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));
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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.containsInt(1)).thenReturn(true);

        var result = cut.containsInt(1);

        assertThat(result).isTrue();
        verify(collection).containsInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsInt__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.containsInt(1));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void isEmpty__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
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
        var cut = new SerializableIntCollectionView(collection, forwardingType);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void iterator__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, forwardingType);
        when(collection.iterator()).thenReturn(iterator);

        var result = cut.iterator();

        assertThat(result).isSameAs(iterator);
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void iterator__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::iterator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void parallelPrimitiveStream__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var stream = mock(IntStream.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.parallelPrimitiveStream()).thenReturn(stream);

        var result = cut.parallelPrimitiveStream();

        assertThat(result).isSameAs(stream);
        verify(collection).parallelPrimitiveStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void parallelPrimitiveStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.SHALLOW));
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.parallelStream()).thenReturn(stream);

        var result = cut.parallelStream();

        assertThat(result).isSameAs(stream);
        verify(collection).parallelStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void parallelStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.parallelStream();
        var result = stream.toList();

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    void parallelStream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.primitiveStream()).thenReturn(stream);

        var result = cut.primitiveStream();

        assertThat(result).isSameAs(stream);
        verify(collection).primitiveStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void primitiveStream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.SHALLOW));
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var stream = cut.primitiveStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void remove__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.remove(1)).thenReturn(true);

        var result = cut.remove(1);

        assertThat(result).isTrue();
        verify(collection).remove(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void remove__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.remove(1);

        assertThat(result).isTrue();
        verify(cut).removeInt(1);
        verify(collection).iterator();
        verify(iterator).hasNext();
        verify(iterator).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void remove__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.remove(1));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).removeInt(1);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    @SuppressWarnings("SuspiciousMethodCalls")
    void remove__shallowOrMinimalViewProvidedNonInt__returnsFalse(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, forwardingType));

        var result = cut.remove(new Object());

        assertThat(result).isFalse();
        verify(cut, never()).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void removeAll_Collection__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.PURE);
        doReturn(true).when(collection1).removeAll(collection2);

        var result = cut.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_Collection__shallowView__processesRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW);
        when(collection1.iterator()).thenReturn(iterator);
        when(collection2.contains(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);

        var result = cut.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).iterator();
        verify(collection2).contains(1);
        verify(collection2).contains(2);
        verify(collection2).contains(3);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection1, collection2, iterator);
    }

    @Test
    void removeAll_Collection__minimalView__throwsException() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.removeAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).removeIfInt(any());
        verify(cut).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_Collection__shallowViewPassedPrimitiveCollection__divertsToRemoveAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW));
        when(collection1.iterator()).thenReturn(iterator);
        when(collection2.containsInt(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);

        var result = cut.removeAll((Collection<Integer>) collection2);

        assertThat(result).isTrue();
        verify(cut).removeAll((Collection<Integer>) collection2);
        verify(cut).removeAll(collection2);
        verify(collection1).iterator();
        verify(collection2).containsInt(1);
        verify(collection2).containsInt(2);
        verify(collection2).containsInt(3);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection1, collection2, iterator);
    }

    @Test
    void removeAll_Collection__minimalViewPassedPrimitiveCollection__divertsToRemoveAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.removeAll((Collection<Integer>) collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).removeAll((Collection<Integer>) collection2);
        verify(cut).removeAll(collection2);
        verify(cut).removeIfInt(any());
        verify(cut).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_OfInt__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.PURE);
        doReturn(true).when(collection1).removeAll(collection2);

        var result = cut.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_OfInt__shallowView__processesRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW);
        when(collection1.iterator()).thenReturn(iterator);
        when(collection2.containsInt(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);

        var result = cut.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).iterator();
        verify(collection2).containsInt(1);
        verify(collection2).containsInt(2);
        verify(collection2).containsInt(3);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection1, collection2, iterator);
    }

    @Test
    void removeAll_OfInt__minimalView__throwsException() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.removeAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).removeIfInt(any());
        verify(cut).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeInt__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.removeInt(1)).thenReturn(true);

        var result = cut.removeInt(1);

        assertThat(result).isTrue();
        verify(collection).removeInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void removeInt__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.removeInt(1);

        assertThat(result).isTrue();
        verify(collection).iterator();
        verify(iterator).hasNext();
        verify(iterator).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void removeInt__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.removeInt(1));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void removeIf_IntPredicate__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = mock(IntPredicate.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        doReturn(true).when(collection).removeIf(predicate);

        var result = cut.removeIf(predicate);

        assertThat(result).isTrue();
        verify(collection).removeIf(predicate);
        verifyNoMoreInteractions(collection, predicate);
    }

    @Test
    void removeIf_IntPredicate__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = mock(IntPredicate.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(iterator);
        when(predicate.test(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2);

        var result = cut.removeIf(predicate);

        assertThat(result).isTrue();
        verify(cut).removeIfInt(predicate);
        verify(collection).iterator();
        verify(predicate).test(1);
        verify(predicate).test(2);
        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection, predicate, iterator);
    }

    @Test
    void removeIf_IntPredicate__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate  = mock(IntPredicate.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).removeIfInt(predicate);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, predicate);
    }

    @Test
    void removeIf_Predicate__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = (Predicate<Integer>) mock(Predicate.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        doReturn(true).when(collection).removeIf(predicate);

        var result = cut.removeIf(predicate);

        assertThat(result).isTrue();
        verify(collection).removeIf(predicate);
        verifyNoMoreInteractions(collection, predicate);
    }

    @Test
    void removeIf_Predicate__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = (Predicate<Integer>) mock(Predicate.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.SHALLOW));
        when(collection.iterator()).thenReturn(iterator);
        when(predicate.test(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2);

        var result = cut.removeIf(predicate);

        assertThat(result).isTrue();
        verify(cut).removeIf(predicate);
        verify(collection).iterator();
        verify(predicate).test(1);
        verify(predicate).test(2);
        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection, predicate, iterator);
    }

    @Test
    void removeIf_Predicate__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = (Predicate<Integer>) mock(Predicate.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, predicate);
    }

    @Test
    void removeIfInt__pureView__forwardsRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = mock(IntPredicate.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        doReturn(true).when(collection).removeIfInt(predicate);

        var result = cut.removeIfInt(predicate);

        assertThat(result).isTrue();
        verify(collection).removeIfInt(predicate);
        verifyNoMoreInteractions(collection, predicate);
    }

    @Test
    void removeIfInt__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = mock(IntPredicate.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(predicate.test(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2);

        var result = cut.removeIfInt(predicate);

        assertThat(result).isTrue();
        verify(collection).iterator();
        verify(predicate).test(1);
        verify(predicate).test(2);
        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection, predicate, iterator);
    }

    @Test
    void removeIfInt__minimalView__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var predicate = mock(IntPredicate.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.removeIfInt(predicate));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, predicate);
    }

    @Test
    void retainAll_Collection__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.PURE);
        doReturn(true).when(collection1).retainAll(collection2);

        var result = cut.retainAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).retainAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_Collection__shallowView__processesRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW);
        when(collection1.iterator()).thenReturn(iterator);
        when(collection2.contains(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);

        var result = cut.retainAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).iterator();
        verify(collection2).contains(1);
        verify(collection2).contains(2);
        verify(collection2).contains(3);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verify(iterator, times(2)).remove();
        verifyNoMoreInteractions(collection1, collection2, iterator);
    }

    @Test
    void retainAll_Collection__minimalView__throwsException() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = (Collection<Integer>) mock(Collection.class);
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.retainAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).retainAll(collection2);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_Collection__shallowViewPassedPrimitiveCollection__divertsToRetainAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW));
        when(collection1.iterator()).thenReturn(iterator);
        when(collection2.containsInt(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);

        var result = cut.retainAll((Collection<Integer>) collection2);

        assertThat(result).isTrue();
        verify(cut).retainAll((Collection<Integer>) collection2);
        verify(cut).retainAll(collection2);
        verify(collection1).iterator();
        verify(collection2).containsInt(1);
        verify(collection2).containsInt(2);
        verify(collection2).containsInt(3);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verify(iterator, times(2)).remove();
        verifyNoMoreInteractions(collection1, collection2, iterator);
    }

    @Test
    void retainAll_Collection__minimalViewPassedPrimitiveCollection__divertsToRetainAll_OfInt() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.retainAll((Collection<Integer>) collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).retainAll((Collection<Integer>) collection2);
        verify(cut).retainAll(collection2);
        verify(cut).removeIfInt(any());
        verify(cut).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_OfInt__pureView__forwardsRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.PURE);
        doReturn(true).when(collection1).retainAll(collection2);

        var result = cut.retainAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).retainAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_OfInt__shallowView__processesRequest() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntCollectionView(collection1, ForwardingType.SHALLOW);
        when(collection1.iterator()).thenReturn(iterator);
        when(collection2.containsInt(1)).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextInt()).thenReturn(1, 2, 3);

        var result = cut.retainAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).iterator();
        verify(collection2).containsInt(1);
        verify(collection2).containsInt(2);
        verify(collection2).containsInt(3);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextInt();
        verify(iterator, times(2)).remove();
        verifyNoMoreInteractions(collection1, collection2, iterator);
    }

    @Test
    void retainAll_OfInt__minimalView__throwsException() {
        var collection1 = mock(PrimitiveCollection.OfInt.class);
        var collection2 = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection1, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.retainAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).removeIfInt(any());
        verify(cut).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void size__anyView__forwardsRequest(ForwardingType forwardingType) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, forwardingType);
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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.stream()).thenReturn(stream);

        var result = cut.stream();

        assertThat(result).isSameAs(stream);
        verify(collection).stream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void stream__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(IntStream.of(1, 2).iterator());

        var stream = cut.stream();
        var result = stream.toList();

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    void stream__minimalView__failsOnTerminalOperation() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
        when(collection.toArray()).thenReturn(arr);

        var result = cut.toArray();

        assertThat(result).isSameAs(arr);
        verify(collection).toArray();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray__shallowView__processesRequest() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));
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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));
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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.PURE);
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
        var cut = new SerializableIntCollectionView(collection, ForwardingType.SHALLOW);
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
        var cut = spy(new SerializableIntCollectionView(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::toPrimitiveArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }
}
