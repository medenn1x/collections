package net.arinoru.collections;

import net.arinoru.collections.Views.ForwardingType;
import net.arinoru.collections.Views.SerializableIntIteratorView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
class SerializableIntIteratorViewTest {
    @Test
    public void forEachRemaining_Consumer__pureView__forwardsRequest() {
        var action = (Consumer<Integer>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_Consumer__shallowView__processesRequest() {
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
    public void forEachRemaining_Consumer__minimalView__failsOnNextIntInvocation() {
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
    public void forEachRemaining_IntConsumer__pureView__forwardRequest() {
        var action = mock(IntConsumer.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    public void forEachRemaining_IntConsumer__shallowView__processesRequest() {
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
    public void forEachRemaining_IntConsumer__minimalView__failsOnNextIntInvocation() {
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
    public void hasNext__anyView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__pureView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void next__shallowView__processesRequest() {
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
    public void next__minimalView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = spy(new SerializableIntIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    public void nextInt__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, forwardingType);
        when(iterator.nextInt()).thenReturn(1);

        var result = cut.nextInt();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextInt();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    public void nextInt__minimalView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextInt);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    public void remove__anyView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = new SerializableIntIteratorView(iterator, forwardingType);

        cut.remove();

        verify(iterator).remove();
        verifyNoMoreInteractions(iterator);
    }
}
