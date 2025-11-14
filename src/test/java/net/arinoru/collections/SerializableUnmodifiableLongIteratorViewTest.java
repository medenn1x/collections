package net.arinoru.collections;

import net.arinoru.collections.Views.ForwardingType;
import net.arinoru.collections.Views.SerializableUnmodifiableLongIteratorView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
class SerializableUnmodifiableLongIteratorViewTest {
    @Test
    void forEachRemaining_Consumer__pureView__forwardsRequest() {
        var action = (Consumer<Long>) mock(Consumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    void forEachRemaining_Consumer__shallowView__processesRequest() {
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
    void forEachRemaining_Consumer__minimalView__failsOnNextLongInvocation() {
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
    void forEachRemaining_LongConsumer__pureView__forwardRequest() {
        var action = mock(LongConsumer.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.forEachRemaining(action);

        verify(iterator).forEachRemaining(action);
        verifyNoMoreInteractions(action, iterator);
    }

    @Test
    void forEachRemaining_LongConsumer__shallowView__processesRequest() {
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
    void forEachRemaining_LongConsumer__minimalView__failsOnNextLongInvocation() {
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
    void hasNext__anyView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, forwardingType);

        cut.hasNext();

        verify(iterator).hasNext();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    void next__pureView__forwardsRequest() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.PURE);

        cut.next();

        verify(iterator).next();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    void next__shallowView__processesRequest() {
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
    void next__minimalView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = spy(new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL));

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verify(cut).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(names = {"PURE","SHALLOW"})
    void nextLong__pureOrShallowView__forwardsRequest(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, forwardingType);
        when(iterator.nextLong()).thenReturn(1L);

        var result = cut.nextLong();

        assertThat(result).isEqualTo(1);
        verify(iterator).nextLong();
        verifyNoMoreInteractions(iterator);
    }

    @Test
    void nextLong__minimalView__throwsException() {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, ForwardingType.MINIMAL);

        var t = catchThrowable(cut::nextLong);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(iterator);
    }

    @ParameterizedTest
    @EnumSource(ForwardingType.class)
    void remove__anyView__throwsException(ForwardingType forwardingType) {
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = new SerializableUnmodifiableLongIteratorView(iterator, forwardingType);

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(iterator);
    }
}
