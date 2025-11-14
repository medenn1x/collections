package net.arinoru.collections;

import net.arinoru.collections.Views.ForwardingType;
import net.arinoru.collections.Views.UnmodifiableDoubleIteratorView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
class UnmodifiableDoubleIteratorViewTest {
    @Test
    public void forEachRemaining_Consumer__pureView__forwardsRequest() {
        var action = (Consumer<Double>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowView__processesRequest() {
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
    public void forEachRemaining_Consumer__minimalView__failsOnNextDoubleInvocation() {
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
    public void forEachRemaining_DoubleConsumer__pureView__forwardRequest() {
        var action = mock(DoubleConsumer.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_DoubleConsumer__shallowView__processesRequest() {
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
    public void forEachRemaining_DoubleConsumer__minimalView__failsOnNextDoubleInvocation() {
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
    public void hasNext__anyView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowView__processesRequest() {
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
    public void next__minimalView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = spy(new UnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextDouble__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, forwardingType);
        when(iterator.nextDouble()).thenReturn(1.0);

        var result = cut.nextDouble();

        assertThat(result).isEqualTo(1.0);
        verify(iterator).nextDouble();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextDouble__minimalView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextDouble);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__anyView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = new UnmodifiableDoubleIteratorView(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }
}
