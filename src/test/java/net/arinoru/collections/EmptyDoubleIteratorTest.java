package net.arinoru.collections;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class EmptyDoubleIteratorTest {
    @Test
    void hasNext__always__returnsFalse() {
        var cut = PrimitiveCollections.emptyDoubleIterator();

        var result = cut.hasNext();

        assertThat(result).isFalse();
    }

    @Test
    void next__always__throwsException() {
        var cut = PrimitiveCollections.emptyDoubleIterator();

        var t = catchThrowable(cut::next);

        assertThat(t).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void nextDouble__always__throwsException() {
        var cut = PrimitiveCollections.emptyDoubleIterator();

        var t = catchThrowable(cut::nextDouble);

        assertThat(t).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void remove__always__throwsException() {
        var cut = PrimitiveCollections.emptyDoubleIterator();

        var t = catchThrowable(cut::remove);

        assertThat(t).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    void forEachRemaining_Consumer__always__doesNothing() {
        var consumer = (Consumer<? super Double>) mock(Consumer.class);
        var cut = PrimitiveCollections.emptyDoubleIterator();

        cut.forEachRemaining(consumer);

        verifyNoMoreInteractions(consumer);
    }

    @Test
    void forEachRemaining_DoubleConsumer__always__doesNothing() {
        var consumer = mock(DoubleConsumer.class);
        var cut = PrimitiveCollections.emptyDoubleIterator();

        cut.forEachRemaining(consumer);

        verifyNoMoreInteractions(consumer);
    }
}
