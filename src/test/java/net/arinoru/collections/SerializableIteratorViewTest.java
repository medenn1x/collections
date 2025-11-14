package net.arinoru.collections;

import net.arinoru.collections.Views.ForwardingType;
import net.arinoru.collections.Views.SerializableIteratorView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
class SerializableIteratorViewTest {
    @Test
    public void forEachRemaining__pureView__forwardsRequest() {
        var action = (Consumer<Object>) mock(Consumer.class);
        var iterator = mock(Iterator.class);
        var cut = new SerializableIteratorView<>(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void forEachRemaining__shallowOrMinimalView__processesRequest(ForwardingType forwardingType) {
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
    public void hasNext__anyView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new SerializableIteratorView<>(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void next__anyViewNotBackedByPrimitiveIterator__forwardsRequest(ForwardingType forwardingType) {
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
    public void next__pureViewBackedByDoubleIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalViewBackedByDoubleIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new SerializableIteratorView<Double>(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.next();

        assertThat(result.doubleValue()).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureViewBackedByIntIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalViewBackedByIntIterator__processesRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIteratorView<Integer>(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.next();

        assertThat(result.intValue()).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureViewBackedByLongIterator__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableIteratorView<>(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"SHALLOW","MINIMAL"})
    public void next__shallowOrMinimalViewBackedByLongIterator__processesRequest(ForwardingType forwardingType) {
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
    public void remove__anyView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(Iterator.class);
        var cut = new SerializableIteratorView<>(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }
}
