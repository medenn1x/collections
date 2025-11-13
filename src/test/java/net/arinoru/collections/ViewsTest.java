package net.arinoru.collections;

import net.arinoru.collections.Views.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
class ViewsTest {
    @Test
    public void forEachRemaining__pureIteratorView__forwardsRequest() {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new IteratorView<>(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void forEachRemaining__shallowOrMinimalIteratorView__processesRequest(ForwardingType forwardingType) {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new IteratorView<>(iterator, forwardingType);
        var ob1 = new Object();
        var ob2 = new Object();
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(action).accept(ob1);
        verify(action).accept(ob2);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__iteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new IteratorView<>(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void next__iteratorViewNotBackedByPrimitiveIterator__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new IteratorView<>(iterator, forwardingType);
        var ob = new Object();
        when(iterator.next()).thenReturn(ob);

        var result = cut.next();

        assertThat(result).isSameAs(ob);
        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureIteratorViewBackedByDoubleIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new IteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalIteratorViewBackedByDoubleIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new IteratorView<Double>(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result.doubleValue()).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureIteratorViewBackedByIntIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalIteratorViewBackedByIntIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IteratorView<Integer>(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result.intValue()).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureIteratorViewBackedByLongIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new IteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalIteratorViewBackedByLongIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new IteratorView<Long>(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.next();

        assertThat(result.longValue()).isEqualTo(1L);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__iteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new IteratorView<>(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining__pureUnmodifiableIteratorView__forwardsRequest() {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new UnmodifiableIteratorView<>(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void forEachRemaining__shallowOrMinimalUnmodifiableIteratorView__processesRequest(ForwardingType forwardingType) {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new UnmodifiableIteratorView<>(iterator, forwardingType);
        var ob1 = new Object();
        var ob2 = new Object();
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(action).accept(ob1);
        verify(action).accept(ob2);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__unmodifiableIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new UnmodifiableIteratorView<>(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void next__unmodifiableIteratorViewNotBackedByPrimitiveIterator__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new UnmodifiableIteratorView<>(iterator, forwardingType);
        var ob = new Object();
        when(iterator.next()).thenReturn(ob);

        var result = cut.next();

        assertThat(result).isSameAs(ob);
        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureUnmodifiableIteratorViewBackedByDoubleIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalUnmodifiableIteratorViewBackedByDoubleIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableIteratorView<Double>(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result.doubleValue()).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureUnmodifiableIteratorViewBackedByIntIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalUnmodifiableIteratorViewBackedByIntIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIteratorView<Integer>(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result.intValue()).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureUnmodifiableIteratorViewBackedByLongIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalUnmodifiableIteratorViewBackedByLongIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableIteratorView<Long>(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.next();

        assertThat(result.longValue()).isEqualTo(1L);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__unmodifiableIteratorView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new UnmodifiableIteratorView<>(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining__pureSerializableIteratorView__forwardsRequest() {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new SerializableIteratorView<>(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void forEachRemaining__shallowOrMinimalSerializableIteratorView__processesRequest(ForwardingType forwardingType) {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new SerializableIteratorView<>(iterator, forwardingType);
        var ob1 = new Object();
        var ob2 = new Object();
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(action).accept(ob1);
        verify(action).accept(ob2);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__serializableIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new SerializableIteratorView<>(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void next__serializableIteratorViewNotBackedByPrimitiveIterator__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new SerializableIteratorView<>(iterator, forwardingType);
        var ob = new Object();
        when(iterator.next()).thenReturn(ob);

        var result = cut.next();

        assertThat(result).isSameAs(ob);
        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableIteratorViewBackedByDoubleIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalSerializableIteratorViewBackedByDoubleIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableIteratorView<Double>(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result.doubleValue()).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableIteratorViewBackedByIntIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalSerializableIteratorViewBackedByIntIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIteratorView<Integer>(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result.intValue()).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableIteratorViewBackedByLongIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalSerializableIteratorViewBackedByLongIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableIteratorView<Long>(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.next();

        assertThat(result.longValue()).isEqualTo(1L);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__serializableIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new SerializableIteratorView<>(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining__pureSerializableUnmodifiableIteratorView__forwardsRequest() {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new SerializableUnmodifiableIteratorView<>(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void forEachRemaining__shallowOrMinimalSerializableUnmodifiableIteratorView__processesRequest(ForwardingType forwardingType) {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new SerializableUnmodifiableIteratorView<>(iterator, forwardingType);
        var ob1 = new Object();
        var ob2 = new Object();
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn(ob1, ob2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(action).accept(ob1);
        verify(action).accept(ob2);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__serializableUnmodifiableIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new SerializableUnmodifiableIteratorView<>(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void next__serializableUnmodifiableIteratorViewNotBackedByPrimitiveIterator__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new SerializableUnmodifiableIteratorView<>(iterator, forwardingType);
        var ob = new Object();
        when(iterator.next()).thenReturn(ob);

        var result = cut.next();

        assertThat(result).isSameAs(ob);
        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableUnmodifiableIteratorViewBackedByDoubleIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalSerializableUnmodifiableIteratorViewBackedByDoubleIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableIteratorView<Double>(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result.doubleValue()).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableUnmodifiableIteratorViewBackedByIntIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalSerializableUnmodifiableIteratorViewBackedByIntIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIteratorView<Integer>(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result.intValue()).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableUnmodifiableIteratorViewBackedByLongIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalSerializableUnmodifiableIteratorViewBackedByLongIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableIteratorView<Long>(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.next();

        assertThat(result.longValue()).isEqualTo(1L);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__serializableUnmodifiableIteratorView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new SerializableUnmodifiableIteratorView<>(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureDoubleIteratorView__forwardsRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowDoubleIteratorView__processesRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Double.valueOf(1.0);
        var val2 = Double.valueOf(2.0);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextDouble();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalDoubleIteratorView__failsOnNextDoubleInvocation() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new DoubleIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__pureDoubleIteratorView__forwardRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__shallowDoubleIteratorView__processesRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Double.valueOf(1.0);
        var val2 = Double.valueOf(2.0);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextDouble();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__minimalDoubleIteratorView__failsOnNextDoubleInvocation() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new DoubleIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__doubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureDoubleIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowDoubleIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new DoubleIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result).isEqualTo(Double.valueOf(1.0));
        verify(iterator).nextDouble();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalDoubleIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new DoubleIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextDouble__pureOrShallowDoubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.nextDouble();

        assertThat(result).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextDouble__minimalDoubleIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextDouble);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__doubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new DoubleIteratorView(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureUnmodifiableDoubleIteratorView__forwardsRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowUnmodifiableDoubleIteratorView__processesRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Double.valueOf(1.0);
        var val2 = Double.valueOf(2.0);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextDouble();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalUnmodifiableDoubleIteratorView__failsOnNextDoubleInvocation() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new UnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__pureUnmodifiableDoubleIteratorView__forwardRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__shallowUnmodifiableDoubleIteratorView__processesRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Double.valueOf(1.0);
        var val2 = Double.valueOf(2.0);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextDouble();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__minimalUnmodifiableDoubleIteratorView__failsOnNextDoubleInvocation() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new UnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__unmodifiableDoubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureUnmodifiableDoubleIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowUnmodifiableDoubleIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new UnmodifiableDoubleIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result).isEqualTo(Double.valueOf(1.0));
        verify(iterator).nextDouble();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalUnmodifiableDoubleIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new UnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextDouble__pureOrShallowUnmodifiableDoubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.nextDouble();

        assertThat(result).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextDouble__minimalUnmodifiableDoubleIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextDouble);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__unmodifiableDoubleIteratorView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureSerializableDoubleIteratorView__forwardsRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowSerializableDoubleIteratorView__processesRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Double.valueOf(1.0);
        var val2 = Double.valueOf(2.0);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextDouble();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalSerializableDoubleIteratorView__failsOnNextDoubleInvocation() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new SerializableDoubleIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__pureSerializableDoubleIteratorView__forwardRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__shallowSerializableDoubleIteratorView__processesRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Double.valueOf(1.0);
        var val2 = Double.valueOf(2.0);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextDouble();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__minimalSerializableDoubleIteratorView__failsOnNextDoubleInvocation() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new SerializableDoubleIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__serializableDoubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableDoubleIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowSerializableDoubleIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new SerializableDoubleIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result).isEqualTo(Double.valueOf(1.0));
        verify(iterator).nextDouble();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalSerializableDoubleIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new SerializableDoubleIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextDouble__pureOrShallowSerializableDoubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.nextDouble();

        assertThat(result).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextDouble__minimalSerializableDoubleIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextDouble);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__serializableDoubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableDoubleIteratorView(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureSerializableUnmodifiableDoubleIteratorView__forwardsRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowSerializableUnmodifiableDoubleIteratorView__processesRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Double.valueOf(1.0);
        var val2 = Double.valueOf(2.0);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextDouble();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalSerializableUnmodifiableDoubleIteratorView__failsOnNextDoubleInvocation() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__pureSerializableUnmodifiableDoubleIteratorView__forwardRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__shallowSerializableUnmodifiableDoubleIteratorView__processesRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Double.valueOf(1.0);
        var val2 = Double.valueOf(2.0);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextDouble()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextDouble();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__minimalSerializableUnmodifiableDoubleIteratorView__failsOnNextDoubleInvocation() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__serializableUnmodifiableDoubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableUnmodifiableDoubleIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowSerializableUnmodifiableDoubleIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result).isEqualTo(Double.valueOf(1.0));
        verify(iterator).nextDouble();
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalSerializableUnmodifiableDoubleIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextDouble__pureOrShallowSerializableUnmodifiableDoubleIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.nextDouble();

        assertThat(result).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextDouble__minimalSerializableUnmodifiableDoubleIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextDouble);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__serializableUnmodifiableDoubleIteratorView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableUnmodifiableDoubleIteratorView(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureIntIteratorView__forwardsRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowIntIteratorView__processesRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Integer.valueOf(1);
        var val2 = Integer.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalIntIteratorView__failsOnNextIntInvocation() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new IntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__pureIntIteratorView__forwardRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__shallowIntIteratorView__processesRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Integer.valueOf(1);
        var val2 = Integer.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__minimalIntIteratorView__failsOnNextIntInvocation() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new IntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__intIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureIntIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowIntIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new IntIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result).isEqualTo(Integer.valueOf(1));
        verify(iterator).nextInt();
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalIntIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new IntIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextInt__pureOrShallowIntIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.nextInt();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextInt__minimalIntIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextInt);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__intIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new IntIteratorView(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureUnmodifiableIntIteratorView__forwardsRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowUnmodifiableIntIteratorView__processesRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Integer.valueOf(1);
        var val2 = Integer.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalUnmodifiableIntIteratorView__failsOnNextIntInvocation() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new UnmodifiableIntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__pureUnmodifiableIntIteratorView__forwardRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__shallowUnmodifiableIntIteratorView__processesRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Integer.valueOf(1);
        var val2 = Integer.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__minimalUnmodifiableIntIteratorView__failsOnNextIntInvocation() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new UnmodifiableIntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__unmodifiableIntIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureUnmodifiableIntIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowUnmodifiableIntIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new UnmodifiableIntIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result).isEqualTo(Integer.valueOf(1));
        verify(iterator).nextInt();
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalUnmodifiableIntIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new UnmodifiableIntIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextInt__pureOrShallowUnmodifiableIntIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.nextInt();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextInt__minimalUnmodifiableIntIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextInt);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__unmodifiableIntIteratorView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new UnmodifiableIntIteratorView(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureSerializableIntIteratorView__forwardsRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowSerializableIntIteratorView__processesRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Integer.valueOf(1);
        var val2 = Integer.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalSerializableIntIteratorView__failsOnNextIntInvocation() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__pureSerializableIntIteratorView__forwardRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__shallowSerializableIntIteratorView__processesRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Integer.valueOf(1);
        var val2 = Integer.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__minimalSerializableIntIteratorView__failsOnNextIntInvocation() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__serializableIntIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableIntIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowSerializableIntIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result).isEqualTo(Integer.valueOf(1));
        verify(iterator).nextInt();
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalSerializableIntIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextInt__pureOrShallowSerializableIntIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.nextInt();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextInt__minimalSerializableIntIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextInt);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__serializableIntIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureSerializableUnmodifiableIntIteratorView__forwardsRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowSerializableUnmodifiableIntIteratorView__processesRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Integer.valueOf(1);
        var val2 = Integer.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalSerializableUnmodifiableIntIteratorView__failsOnNextIntInvocation() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__pureSerializableUnmodifiableIntIteratorView__forwardRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__shallowSerializableUnmodifiableIntIteratorView__processesRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Integer.valueOf(1);
        var val2 = Integer.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextInt()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextInt();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__minimalSerializableUnmodifiableIntIteratorView__failsOnNextIntInvocation() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__serializableUnmodifiableIntIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableUnmodifiableIntIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowSerializableUnmodifiableIntIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result).isEqualTo(Integer.valueOf(1));
        verify(iterator).nextInt();
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalSerializableUnmodifiableIntIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextInt__pureOrShallowSerializableUnmodifiableIntIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.nextInt();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextInt__minimalSerializableUnmodifiableIntIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextInt);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__serializableUnmodifiableIntIteratorView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableUnmodifiableIntIteratorView(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureLongIteratorView__forwardsRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowLongIteratorView__processesRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Long.valueOf(1);
        var val2 = Long.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextLong();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalLongIteratorView__failsOnNextLongInvocation() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new LongIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextLong();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__pureLongIteratorView__forwardRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__shallowLongIteratorView__processesRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Long.valueOf(1);
        var val2 = Long.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextLong();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__minimalLongIteratorView__failsOnNextLongInvocation() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new IntIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextInt();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__longIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureLongIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowLongIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new LongIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.next();

        assertThat(result).isEqualTo(Long.valueOf(1));
        verify(iterator).nextLong();
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalLongIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new LongIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextLong__pureOrShallowLongIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.nextLong();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextLong__minimalLongIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextLong);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__longIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new LongIteratorView(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureUnmodifiableLongIteratorView__forwardsRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowUnmodifiableLongIteratorView__processesRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Long.valueOf(1);
        var val2 = Long.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextLong();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalUnmodifiableLongIteratorView__failsOnNextLongInvocation() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new UnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextLong();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__pureUnmodifiableLongIteratorView__forwardRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__shallowUnmodifiableLongIteratorView__processesRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Long.valueOf(1);
        var val2 = Long.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextLong();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__minimalUnmodifiableLongIteratorView__failsOnNextLongInvocation() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new UnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextLong();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__unmodifiableLongIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureUnmodifiableLongIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowUnmodifiableLongIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new UnmodifiableLongIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.next();

        assertThat(result).isEqualTo(Long.valueOf(1));
        verify(iterator).nextLong();
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalUnmodifiableLongIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new UnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextLong__pureOrShallowUnmodifiableLongIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.nextLong();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextLong__minimalUnmodifiableLongIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextLong);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__unmodifiableLongIteratorView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new UnmodifiableLongIteratorView(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureSerializableLongIteratorView__forwardsRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowSerializableLongIteratorView__processesRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Long.valueOf(1);
        var val2 = Long.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextLong();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalSerializableLongIteratorView__failsOnNextLongInvocation() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableLongIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextLong();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__pureSerializableLongIteratorView__forwardRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__shallowSerializableLongIteratorView__processesRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Long.valueOf(1);
        var val2 = Long.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextLong();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__minimalSerializableLongIteratorView__failsOnNextLongInvocation() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableLongIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextLong();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__serializableLongIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableLongIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowSerializableLongIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableLongIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.next();

        assertThat(result).isEqualTo(Long.valueOf(1));
        verify(iterator).nextLong();
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalSerializableLongIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableLongIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextLong__pureOrShallowSerializableLongIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.nextLong();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextLong__minimalSerializableLongIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextLong);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__serializableLongIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableLongIteratorView(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void forEachRemaining_Consumer__pureSerializableUnmodifiableLongIteratorView__forwardsRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowSerializableUnmodifiableLongIteratorView__processesRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Long.valueOf(1);
        var val2 = Long.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextLong();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__minimalSerializableUnmodifiableLongIteratorView__failsOnNextLongInvocation() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextLong();
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__pureSerializableUnmodifiableLongIteratorView__forwardRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__shallowSerializableUnmodifiableLongIteratorView__processesRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.SHALLOW);
        var val1 = Long.valueOf(1);
        var val2 = Long.valueOf(2);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(val1, val2);

        cut.forEachRemaining(action);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).nextLong();
        verify(action).accept(val1);
        verify(action).accept(val2);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_LongConsumer__minimalSerializableUnmodifiableLongIteratorView__failsOnNextLongInvocation() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL));
        when(iterator.hasNext()).thenReturn(true);

        var t = catchThrowable(() -> cut.forEachRemaining(action));

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(iterator).hasNext();
        verify(cut).nextLong();
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void hasNext__serializableUnmodifiableLongIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureSerializableUnmodifiableLongIteratorView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowSerializableUnmodifiableLongIteratorView__processesRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.SHALLOW));
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.next();

        assertThat(result).isEqualTo(Long.valueOf(1));
        verify(iterator).nextLong();
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__minimalSerializableUnmodifiableLongIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextLong__pureOrShallowSerializableUnmodifiableLongIteratorView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.nextLong();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextLong__minimalSerializableUnmodifiableLongIteratorView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextLong);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__serializableUnmodifiableLongIteratorView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }
}
