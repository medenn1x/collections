package net.arinoru.collections;

import net.arinoru.collections.Views.CollectionView;
import net.arinoru.collections.Views.ForwardingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

@SuppressWarnings("unchecked")
class CollectionViewTest {
    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void add__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, forwardingType);
        var ob = new Object();

        cut.add(ob);

        verify(collection).add(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void add__minimalCollectionView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.MINIMAL);
        var ob = new Object();

        var t = catchThrowable(() -> cut.add(ob));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void addAll__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection1, forwardingType);

        cut.addAll(collection2);

        verify(collection1).addAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    public void addAll__minimalCollectionView__throwsException() {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection1, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.addAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void clear__collectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, forwardingType);

        cut.clear();

        verify(collection).clear();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void contains__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, forwardingType);
        var ob = new Object();
        when(collection.contains(ob)).thenReturn(true);

        var result = cut.contains(ob);

        assertThat(result).isTrue();
        verify(collection).contains(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void contains__minimalCollectionView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.MINIMAL);
        var ob = new Object();

        var t = catchThrowable(() -> cut.contains(ob));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void containsAll__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection1, forwardingType);
        doReturn(true).when(collection1).containsAll(collection2);

        var result = cut.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    public void containsAll__minimalCollectionView__throwsException() {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection1, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.containsAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    public void equals__pureCollectionView__forwardsRequest() {
        // Note: This is a contrived test to validate PURE forwarding rules on
        // a view. In general, sets should not be used to back a pure collection
        // view, as it will create a view which violates the symmetric property
        // of algebra.
        var collection = Collections.emptySet();
        var cut = new CollectionView<>(collection, ForwardingType.PURE);

        @SuppressWarnings("EqualsBetweenInconvertibleTypes")
        var result = cut.equals(collection);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void equals__shallowOrMinimalCollectionView__processesRequest(ForwardingType forwardingType) {
        var collection = Collections.emptySet();
        var cut = new CollectionView<>(collection, forwardingType);

        @SuppressWarnings("EqualsBetweenInconvertibleTypes")
        var result = cut.equals(collection);

        assertThat(result).isFalse();
    }

    @Test
    public void forEach__pureCollectionView__forwardsRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(collection).forEach(consumer);
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    public void forEach__shallowCollectionView__processesRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var ob1 = new Object();
        var ob2 = new Object();
        var cut = new CollectionView<>(collection, ForwardingType.SHALLOW);
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
    public void forEach__minimalCollectionView__failsOnIteratorInvocation() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new CollectionView<>(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    public void hashCode__pureCollectionView__forwardsRequest() {
        // Note: This is a contrived test to validate PURE forwarding rules on
        // a view. In general, sets should not be used to back a pure collection
        // view, as it will create a view which violates the symmetric property
        // of algebra.
        var collection = Set.of(1);
        var cut = new CollectionView<Integer>(collection, ForwardingType.PURE);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(1);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void hashCode__shallowOrMinimalCollectionView__processesRequest(ForwardingType forwardingType) {
        var collection = Set.of();
        var cut = new CollectionView<>(collection, forwardingType);

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
    public void isEmpty__collectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, forwardingType);
        when(collection.isEmpty()).thenReturn(true);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(collection).isEmpty();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void iterator__pureOrShallowCollectionView__forwardsRequestWithoutMasking(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var cut = new CollectionView<>(collection, forwardingType);
        when(collection.iterator()).thenReturn(iterator);

        var result = cut.iterator();

        assertThat(result).isSameAs(iterator);
    }

    @Test
    public void iterator__minimalCollectionView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::iterator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void parallelStream__pureCollectionView__forwardsRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var stream = (Stream<Object>) mock(Stream.class);
        var cut = new CollectionView<>(collection, ForwardingType.PURE);
        when(collection.parallelStream()).thenReturn(stream);

        var result = cut.parallelStream();

        assertThat(result).isSameAs(stream);
        verify(collection).parallelStream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    public void parallelStream__shallowCollectionView__processesRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.SHALLOW);
        var obs = new Object[] { new Object(), new Object() };
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(Arrays.stream(obs).iterator());

        var stream = cut.parallelStream();
        var result = stream.toList();

        assertThat(result).containsExactly(obs);
    }

    @Test
    public void parallelStream__minimalCollectionView__failsOnTerminalOperation() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned stream behaves
        // correctly for a collection-backed stream, and fails on terminal
        // operation only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new CollectionView<>(collection, ForwardingType.MINIMAL));

        var stream = cut.parallelStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void remove__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, forwardingType);
        var ob = new Object();
        when(collection.remove(ob)).thenReturn(true);

        var result = cut.remove(ob);

        assertThat(result).isTrue();
        verify(collection).remove(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void remove__minimalCollectionView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.MINIMAL);
        var ob = new Object();

        var t = catchThrowable(() -> cut.remove(ob));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void removeAll__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection1, forwardingType);
        when(collection1.removeAll(collection2)).thenReturn(true);

        var result = cut.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    public void removeAll__minimalCollectionView__throwsException() {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection1, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.removeAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    public void removeIf__pureCollectionView__forwardsRequest() {
        var predicate = (Predicate<Object>) mock(Predicate.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.PURE);
        when(collection.removeIf(predicate)).thenReturn(true);

        var result = cut.removeIf(predicate);

        assertThat(result).isTrue();
        verify(collection).removeIf(predicate);
        verifyNoMoreInteractions(collection, predicate);
    }

    @Test
    public void removeIf__shallowCollectionView__processesRequest() {
        var predicate = (Predicate<Object>) mock(Predicate.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var ob1 = new Object();
        var ob2 = new Object();
        var cut = new CollectionView<>(collection, ForwardingType.SHALLOW);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);
        when(predicate.test(ob1)).thenReturn(false);
        when(predicate.test(ob2)).thenReturn(true);

        var result = cut.removeIf(predicate);

        assertThat(result).isTrue();
        verify(predicate).test(ob1);
        verify(predicate).test(ob2);
        verify(collection).iterator();
        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(iterator).remove();
        verifyNoMoreInteractions(predicate, collection, iterator);
    }

    @Test
    public void removeIf__minimalCollectionView__throwsException() {
        var predicate = (Predicate<Object>) mock(Predicate.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new CollectionView<>(collection, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, predicate);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void retainAll__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection1, forwardingType);
        when(collection1.retainAll(collection2)).thenReturn(true);

        var result = cut.retainAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).retainAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    public void retainAll__minimalCollectionView__throwsException() {
        var collection1 = (Collection<Object>) mock(Collection.class);
        var collection2 = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection1, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.retainAll(collection2));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void size__collectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, forwardingType);
        when(collection.size()).thenReturn(123);

        var result = cut.size();

        assertThat(result).isEqualTo(123);
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void spliterator__pureCollectionView__forwardsRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var spliterator = (Spliterator<Object>) mock(Spliterator.class);
        var cut = new CollectionView<>(collection, ForwardingType.PURE);
        when(collection.spliterator()).thenReturn(spliterator);

        var result = cut.spliterator();

        assertThat(result).isSameAs(spliterator);
        verify(collection).spliterator();
        verifyNoMoreInteractions(collection, spliterator);
    }

    @Test
    public void spliterator__shallowCollectionView__processesRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var cut = new CollectionView<>(collection, ForwardingType.SHALLOW);
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
    public void spliterator__minimalCollectionView__failsOnBind() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned spliterator
        // behaves correctly for a collection-backed spliterator, and fails
        // on bind only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new CollectionView<>(collection, ForwardingType.MINIMAL));
        when(collection.size()).thenReturn(1);

        var spliterator = cut.spliterator();
        var t = catchThrowable(() -> spliterator.tryAdvance(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    public void stream__pureCollectionView__forwardsRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var stream = (Stream<Object>) mock(Stream.class);
        var cut = new CollectionView<>(collection, ForwardingType.PURE);
        when(collection.stream()).thenReturn(stream);

        var result = cut.stream();

        assertThat(result).isSameAs(stream);
        verify(collection).stream();
        verifyNoMoreInteractions(collection, stream);
    }

    @Test
    public void stream__shallowCollectionView__processesRequest() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.SHALLOW);
        var obs = new Object[] { new Object(), new Object() };
        when(collection.size()).thenReturn(2);
        when(collection.iterator()).thenReturn(Arrays.stream(obs).iterator());

        var stream = cut.stream();
        var result = stream.toList();

        assertThat(result).containsExactly(obs);
    }

    @Test
    public void stream__minimalCollectionView__failsOnTerminalOperation() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned stream behaves
        // correctly for a collection-backed stream, and fails on terminal
        // operation only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new CollectionView<>(collection, ForwardingType.MINIMAL));

        var stream = cut.stream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void toArray__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var arr = new Object[0];
        var cut = new CollectionView<>(collection, forwardingType);
        when(collection.toArray()).thenReturn(arr);

        var result = cut.toArray();

        assertThat(result).isSameAs(arr);
        verify(collection).toArray();
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void toArray__minimalCollectionView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::toArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void toArray_IntFunction__pureCollectionView__forwardsRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.PURE);
        var arr = new Object[0];
        when(collection.toArray(intFunction)).thenReturn(arr);

        var result = cut.toArray(intFunction);

        assertThat(result).isSameAs(arr);
        verify(collection).toArray(intFunction);
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void toArray_IntFunction__shallowCollectionView__processesRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.SHALLOW);
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
    public void toArray_IntFunction__minimalCollectionView__failsOnToArray_TArray() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = spy(new CollectionView<>(collection, ForwardingType.MINIMAL));
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
    public void toArray_TArray__pureOrShallowCollectionView__forwardsRequest(ForwardingType forwardingType) {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, forwardingType);
        var arr = new Object[0];
        when(collection.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr);
        verify(collection).toArray(arr);
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void toArray_TArray__minimalCollectionView__throwsException() {
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new CollectionView<>(collection, ForwardingType.MINIMAL);
        var arr = new Object[0];

        var t = catchThrowable(() -> cut.toArray(arr));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }
}
