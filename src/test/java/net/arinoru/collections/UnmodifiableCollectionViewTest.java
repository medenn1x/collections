package net.arinoru.collections;

import net.arinoru.collections.Views.ForwardingType;
import net.arinoru.collections.Views.UnmodifiableCollectionView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked","ResultOfMethodCallIgnored"})
class UnmodifiableCollectionViewTest {
    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void add__anyView__throwsException(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);
        var ob = new Object();

        var t = catchThrowable(() -> cut.add(ob));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void addAll__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection1, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void clear__anyView__throwsException(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);

        var t = catchThrowable(cut::clear);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void contains__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);
        var ob = new Object();
        when(collection.contains(ob)).thenReturn(true);

        var result = cut.contains(ob);

        assertThat(result).isTrue();
        verify(collection).contains(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__minimalView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL);
        var ob = new Object();

        var t = catchThrowable(() -> cut.contains(ob));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void containsAll__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection1, forwardingType);
        doReturn(true).when(collection1).containsAll(collection2);

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll__minimalView__throwsException() {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection1, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.containsAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void equals__pureView__forwardsRequest() {
        // Note: This is a contrived test to validate PURE forwarding rules on
        // a view. In general, sets should not be used to back a pure collection
        // view (unless that view also implements Set), as it will create a view
        // which violates the symmetric property of algebra.
        var collection = Set.of();
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.PURE);

        @SuppressWarnings("EqualsBetweenInconvertibleTypes")
        var result = cut.equals(collection);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void equals__shallowOrMinimalView__processesRequest(ForwardingType forwardingType) {
        var collection = Set.of();
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);

        @SuppressWarnings("EqualsBetweenInconvertibleTypes")
        var result = cut.equals(collection);

        assertThat(result).isFalse();
    }

    @Test
    void forEach__pureView__forwardsRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(collection).forEach(consumer);
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void forEach__shallowView__processesRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var ob1 = new Object();
        var ob2 = new Object();
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);

        cut.forEach(consumer);

        verify(collection).iterator();
        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(consumer).accept(ob1);
        verify(consumer).accept(ob2);
        verifyNoMoreInteractions(consumer, collection, iterator);
    }

    @Test
    void forEach__minimalView__failsOnIteratorInvocation() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void hashCode__pureView__forwardsRequest() {
        // Note: This is a contrived test to validate PURE forwarding rules on
        // a view. In general, sets should not be used to back a pure collection
        // view (unless that view also implements Set), as it will create a view
        // which violates the symmetric property of algebra.
        var collection = Set.of(1);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.PURE);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(1);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    void hashCode__shallowOrMinimalView__processesRequest(ForwardingType forwardingType) {
        var collection = Set.of();
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);

        var result = cut.hashCode();

        // Note: An empty set should have a hash code of 0. It is statistically
        // unlikely that the class under test will have a hash code of zero, but
        // it may not be guaranteed. Given the limits of Mockito, it is not
        // possible to test the forwarding behavior directly. Should this test
        // prove to be subject to intermittent failures, it should be deleted.
        assertThat(result).isNotEqualTo(collection.hashCode());
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void isEmpty__anyView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);
        when(collection.isEmpty()).thenReturn(true);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(collection).isEmpty();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void iterator__pureOrShallowView__forwardsRequestWithMasking(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);
        when(collection.iterator()).thenReturn(iterator);

        var result = cut.iterator();

        verify(collection).iterator();
        assertThat(result).isNotSameAs(iterator)
                .isInstanceOf(UnmodifiableView.class);
        result.hasNext();
        verify(iterator).hasNext();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void iterator__minimalView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::iterator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void parallelStream__pureView__forwardsRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var stream = (Stream<Object>) mock(Stream.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.PURE);
        when(collection.parallelStream()).thenReturn(stream);

        var result = cut.parallelStream();

        assertThat(result).isSameAs(stream);
        verify(collection).parallelStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void parallelStream__shallowView__processesRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.SHALLOW);
        var obs = new Object[] { new Object(), new Object() };
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(Arrays.stream(obs).iterator());

        var stream = cut.parallelStream();
        var result = stream.toList();

        assertThat(result).containsExactly(obs);
    }

    @Test
    void parallelStream__minimalView__failsOnTerminalOperation() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned stream behaves
        // correctly for a collection-backed stream, and fails on terminal
        // operation only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL));

        var stream = cut.parallelStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void remove__anyView__throwsException(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);
        var ob = new Object();

        var t = catchThrowable(() -> cut.remove(ob));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeAll__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection1, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeIf__anyView__throwsException(ForwardingType forwardingType) {
        var predicate = (Predicate<Object>) mock(Predicate.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection, predicate);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void retainAll__anyView__throwsException(ForwardingType forwardingType) {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection1, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection2));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void size__anyView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);
        when(collection.size()).thenReturn(123);

        var result = cut.size();

        assertThat(result).isEqualTo(123);
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void spliterator__pureView__forwardsRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var spliterator = (Spliterator<Object>) mock(Spliterator.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.PURE);
        when(collection.spliterator()).thenReturn(spliterator);

        var result = cut.spliterator();

        assertThat(result).isSameAs(spliterator);
        verify(collection).spliterator();
        verifyNoMoreInteractions(collection, spliterator);
    }

    @Test
    void spliterator__shallowView__processesRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.SHALLOW);
        var ob1 = new Object();
        var ob2 = new Object();
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);

        var spliterator = cut.spliterator();
        spliterator.tryAdvance(consumer);

        verify(consumer).accept(ob1);
        verify(iterator).hasNext();
        verify(iterator).next();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(consumer, collection, iterator);
    }

    @Test
    void spliterator__minimalView__failsOnBind() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned spliterator
        // behaves correctly for a collection-backed spliterator, and fails
        // on bind only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL));
        when(collection.size()).thenReturn(1);

        var spliterator = cut.spliterator();
        var t = catchThrowable(() -> spliterator.tryAdvance(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void stream__pureView__forwardsRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var stream = (Stream<Object>) mock(Stream.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.PURE);
        when(collection.stream()).thenReturn(stream);

        var result = cut.stream();

        assertThat(result).isSameAs(stream);
        verify(collection).stream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    void stream__shallowView__processesRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.SHALLOW);
        var obs = new Object[] { new Object(), new Object() };
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(Arrays.stream(obs).iterator());

        var stream = cut.stream();
        var result = stream.toList();

        assertThat(result).containsExactly(obs);
    }

    @Test
    void stream__minimalView__failsOnTerminalOperation() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned stream behaves
        // correctly for a collection-backed stream, and fails on terminal
        // operation only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL));

        var stream = cut.stream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void toArray__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var arr = new Object[0];
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);
        when(collection.toArray()).thenReturn(arr);

        var result = cut.toArray();

        assertThat(result).isSameAs(arr);
        verify(collection).toArray();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray__minimalView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::toArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_IntFunction__pureView__forwardsRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.PURE);
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
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);
        when(collection.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(intFunction);

        assertThat(result).isSameAs(arr);
        verify(collection).toArray(arr);
        verify(intFunction).apply(0);
        verifyNoMoreInteractions(collection, intFunction);
    }

    @Test
    void toArray_IntFunction__minimalView__failsOnToArray_TArray() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL));
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);

        var t = catchThrowable(() -> cut.toArray(intFunction));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).toArray(arr);
        verify(intFunction).apply(0);
        verifyNoMoreInteractions(collection, intFunction);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void toArray_TArray__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, forwardingType);
        var arr = new Object[0];
        when(collection.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr);
        verify(collection).toArray(arr);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void toArray_TArray__minimalView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableCollectionView<>(collection, ForwardingType.MINIMAL);
        var arr = new Object[0];

        var t = catchThrowable(() -> cut.toArray(arr));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }
}
