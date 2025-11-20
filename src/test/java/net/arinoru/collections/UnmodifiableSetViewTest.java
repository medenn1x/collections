package net.arinoru.collections;

import net.arinoru.collections.Views.ForwardingType;
import net.arinoru.collections.Views.UnmodifiableSetView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked","ResultOfMethodCallIgnored"})
class UnmodifiableSetViewTest {
    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void add__anyView__throwsException(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        var ob = new Object();

        var t = catchThrowable(() -> cut.add(ob));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void addAll__anyView__throwsException(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);

        var t = catchThrowable(() -> cut.addAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void clear__anyView__throwsException(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);

        var t = catchThrowable(cut::clear);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void contains__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        var ob = new Object();
        when(set.contains(ob)).thenReturn(true);

        var result = cut.contains(ob);

        assertThat(result).isTrue();
        verify(set).contains(ob);
        verifyNoMoreInteractions(set);
    }

    @Test
    void contains__minimalView__throwsException() {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.MINIMAL);
        var ob = new Object();

        var t = catchThrowable(() -> cut.contains(ob));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void containsAll__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        doReturn(true).when(set).containsAll(collection);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(set).containsAll(collection);
        verifyNoMoreInteractions(set, collection);
    }

    @Test
    void containsAll__minimalView__throwsException() {
        var set = (Set<Object>) mock(Set.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.MINIMAL);

        var t = catchThrowable(() -> cut.containsAll(collection));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void equals__anyView__forwardsRequest(ForwardingType forwardingType) {
        // Note: Because a mock cannot verify behavior connected with the equals
        // method, it is necessary to create a custom class to validate the call.
        // TODO: Use trigger
        var calledEquals = new AtomicBoolean(false);
        var ob = new Object();
        var set = new AbstractSet<>() {
            public boolean equals(Object o) {
                calledEquals.set(true);
                return super.equals(o);
            }

            public Iterator<Object> iterator() {
                return Stream.of(ob).iterator();
            }

            public int size() {
                return 1;
            }
        };
        var cut = new UnmodifiableSetView<>(set, forwardingType);

        var result = cut.equals(Set.of(ob));

        assertThat(result).isTrue();
        assertThat(calledEquals).isTrue();
    }

    @Test
    void forEach__pureView__forwardsRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.PURE);

        cut.forEach(consumer);

        verify(set).forEach(consumer);
        verifyNoMoreInteractions(set, consumer);
    }

    @Test
    void forEach__shallowView__processesRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var set = (Set<Object>) mock(Set.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var ob1 = new Object();
        var ob2 = new Object();
        var cut = new UnmodifiableSetView<>(set, ForwardingType.SHALLOW);
        when(set.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);

        cut.forEach(consumer);

        verify(set).iterator();
        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(consumer).accept(ob1);
        verify(consumer).accept(ob2);
        verifyNoMoreInteractions(consumer, set, iterator);
    }

    @Test
    void forEach__minimalView__failsOnIteratorInvocation() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var set = (Set<Object>) mock(Set.class);
        var cut = spy(new UnmodifiableSetView<>(set, ForwardingType.MINIMAL));

        var t = catchThrowable(() -> cut.forEach(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(set, consumer);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void hashCode__anyView__forwardsRequest(ForwardingType forwardingType) {
        // TODO: Use trigger
        var calledHashCode = new AtomicBoolean(false);
        var ob = new Object();
        var set = new AbstractSet<>() {
            public int hashCode() {
                calledHashCode.set(true);
                return super.hashCode();
            }

            public Iterator<Object> iterator() {
                return Stream.of(ob).iterator();
            }

            public int size() {
                return 1;
            }
        };
        var cut = new UnmodifiableSetView<>(set, forwardingType);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(ob.hashCode());
        assertThat(calledHashCode).isTrue();
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void isEmpty__anyView__forwardsRequest(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        when(set.isEmpty()).thenReturn(true);

        var result = cut.isEmpty();

        assertThat(result).isTrue();
        verify(set).isEmpty();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void iterator__pureOrShallowView__forwardsRequestWithMasking(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        when(set.iterator()).thenReturn(iterator);

        var result = cut.iterator();

        verify(set).iterator();
        assertThat(result).isNotSameAs(iterator)
                .isInstanceOf(UnmodifiableView.class);
        result.hasNext();
        verify(iterator).hasNext();
        verifyNoMoreInteractions(set, iterator);
    }

    @Test
    void iterator__minimalView__throwsException() {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::iterator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(set);
    }

    @Test
    void parallelStream__pureView__forwardsRequest() {
        var set = (Set<Object>) mock(Set.class);
        var stream = (Stream<Object>) mock(Stream.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.PURE);
        when(set.parallelStream()).thenReturn(stream);

        var result = cut.parallelStream();

        assertThat(result).isSameAs(stream);
        verify(set).parallelStream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void parallelStream__shallowView__processesRequest() {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.SHALLOW);
        var obs = new Object[] { new Object(), new Object() };
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(Arrays.stream(obs).iterator());

        var stream = cut.parallelStream();
        var result = stream.toList();

        assertThat(result).containsExactly(obs);
    }

    @Test
    void parallelStream__minimalView__failsOnTerminalOperation() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned stream behaves
        // correctly for a set-backed stream, and fails on terminal
        // operation only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var set = (Set<Object>) mock(Set.class);
        var cut = spy(new UnmodifiableSetView<>(set, ForwardingType.MINIMAL));

        var stream = cut.parallelStream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void remove__anyView__throwsException(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        var ob = new Object();

        var t = catchThrowable(() -> cut.remove(ob));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeAll__anyView__throwsException(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);

        var t = catchThrowable(() -> cut.removeAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void removeIf__anyView__throwsException(ForwardingType forwardingType) {
        var predicate = (Predicate<Object>) mock(Predicate.class);
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, predicate);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void retainAll__anyView__throwsException(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var collection = (Collection<Object>) mock(Collection.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);

        var t = catchThrowable(() -> cut.retainAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(set, collection);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void size__anyView__forwardsRequest(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        when(set.size()).thenReturn(123);

        var result = cut.size();

        assertThat(result).isEqualTo(123);
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void spliterator__pureView__forwardsRequest() {
        var set = (Set<Object>) mock(Set.class);
        var spliterator = (Spliterator<Object>) mock(Spliterator.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.PURE);
        when(set.spliterator()).thenReturn(spliterator);

        var result = cut.spliterator();

        assertThat(result).isSameAs(spliterator);
        verify(set).spliterator();
        verifyNoMoreInteractions(set, spliterator);
    }

    @Test
    void spliterator__shallowView__processesRequest() {
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var set = (Set<Object>) mock(Set.class);
        var iterator = (Iterator<Object>) mock(Iterator.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.SHALLOW);
        var ob1 = new Object();
        var ob2 = new Object();
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);

        var spliterator = cut.spliterator();
        spliterator.tryAdvance(consumer);

        verify(consumer).accept(ob1);
        verify(iterator).hasNext();
        verify(iterator).next();
        verify(set).size();
        verify(set).iterator();
        verifyNoMoreInteractions(consumer, set, iterator);
    }

    @Test
    void spliterator__minimalView__failsOnBind() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned spliterator
        // behaves correctly for a set-backed spliterator, and fails
        // on bind only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var consumer = (Consumer<Object>) mock(Consumer.class);
        var set = (Set<Object>) mock(Set.class);
        var cut = spy(new UnmodifiableSetView<>(set, ForwardingType.MINIMAL));
        when(set.size()).thenReturn(1);

        var spliterator = cut.spliterator();
        var t = catchThrowable(() -> spliterator.tryAdvance(consumer));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).iterator();
        verifyNoMoreInteractions(set, consumer);
    }

    @Test
    void stream__pureView__forwardsRequest() {
        var set = (Set<Object>) mock(Set.class);
        var stream = (Stream<Object>) mock(Stream.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.PURE);
        when(set.stream()).thenReturn(stream);

        var result = cut.stream();

        assertThat(result).isSameAs(stream);
        verify(set).stream();
        verifyNoMoreInteractions(set, stream);
    }

    @Test
    void stream__shallowView__processesRequest() {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.SHALLOW);
        var obs = new Object[] { new Object(), new Object() };
        when(set.size()).thenReturn(2);
        when(set.iterator()).thenReturn(Arrays.stream(obs).iterator());

        var stream = cut.stream();
        var result = stream.toList();

        assertThat(result).containsExactly(obs);
    }

    @Test
    void stream__minimalView__failsOnTerminalOperation() {
        // Note: While the behavior required by this test may seem strange on
        // balance, it is in fact useful behavior. The returned stream behaves
        // correctly for a set-backed stream, and fails on terminal
        // operation only because the view has not correctly implemented the
        // iterator method. A properly-designed minimal view should override
        // the iterator method, avoiding this failure.
        var set = (Set<Object>) mock(Set.class);
        var cut = spy(new UnmodifiableSetView<>(set, ForwardingType.MINIMAL));

        var stream = cut.stream();
        var t = catchThrowable(stream::count);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).spliterator();
        verify(cut).iterator();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void toArray__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var arr = new Object[0];
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        when(set.toArray()).thenReturn(arr);

        var result = cut.toArray();

        assertThat(result).isSameAs(arr);
        verify(set).toArray();
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray__minimalView__throwsException() {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::toArray);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_IntFunction__pureView__forwardsRequest() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.PURE);
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
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.SHALLOW);
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);
        when(set.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(intFunction);

        assertThat(result).isSameAs(arr);
        verify(set).toArray(arr);
        verify(intFunction).apply(0);
        verifyNoMoreInteractions(set, intFunction);
    }

    @Test
    void toArray_IntFunction__minimalView__failsOnToArray_TArray() {
        var intFunction = (IntFunction<Object[]>) mock(IntFunction.class);
        var set = (Set<Object>) mock(Set.class);
        var cut = spy(new UnmodifiableSetView<>(set, ForwardingType.MINIMAL));
        var arr = new Object[0];
        when(intFunction.apply(0)).thenReturn(arr);

        var t = catchThrowable(() -> cut.toArray(intFunction));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).toArray(arr);
        verify(intFunction).apply(0);
        verifyNoMoreInteractions(set, intFunction);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void toArray_TArray__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, forwardingType);
        var arr = new Object[0];
        when(set.toArray(arr)).thenReturn(arr);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr);
        verify(set).toArray(arr);
        verifyNoMoreInteractions(set);
    }

    @Test
    void toArray_TArray__minimalView__throwsException() {
        var set = (Set<Object>) mock(Set.class);
        var cut = new UnmodifiableSetView<>(set, ForwardingType.MINIMAL);
        var arr = new Object[0];

        var t = catchThrowable(() -> cut.toArray(arr));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(set);
    }
}
