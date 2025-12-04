package net.arinoru.collections;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class EmptyLongIteratorTest {
    @Test
    void hasNext__always__returnsFalse() {
        var cut = PrimitiveCollections.emptyLongIterator();

        var result = cut.hasNext();

        assertThat(result).isFalse();
    }

    @Test
    void next__always__throwsException() {
        var cut = PrimitiveCollections.emptyLongIterator();

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void nextLong__always__throwsException() {
        var cut = PrimitiveCollections.emptyLongIterator();

        var t = catchThrowable(cut::nextLong);

        assertThat(t).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void remove__always__throwsException() {
        var cut = PrimitiveCollections.emptyLongIterator();

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    void forEachRemaining_Consumer__always__doesNothing() {
        var consumer = (Consumer<? super Long>) mock(Consumer.class);
        var cut = PrimitiveCollections.emptyLongIterator();

        cut.forEachRemaining(consumer);

        verifyNoMoreInteractions(consumer);
    }

    @Test
    void forEachRemaining_LongConsumer__always__doesNothing() {
        var consumer = mock(LongConsumer.class);
        var cut = PrimitiveCollections.emptyLongIterator();

        cut.forEachRemaining(consumer);

        verifyNoMoreInteractions(consumer);
    }
}
