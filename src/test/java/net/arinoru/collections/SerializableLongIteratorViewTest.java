package net.arinoru.collections;

import net.arinoru.collections.Views.ForwardingType;
import net.arinoru.collections.Views.SerializableLongIteratorView;
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
class SerializableLongIteratorViewTest {
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
}
