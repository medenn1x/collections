package net.arinoru.collections;

import net.arinoru.collections.Views.ForwardingType;
import net.arinoru.collections.Views.IteratorView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
class IteratorViewTest {
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
}
