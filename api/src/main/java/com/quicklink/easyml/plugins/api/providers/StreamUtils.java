package com.quicklink.easyml.plugins.api.providers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jetbrains.annotations.NotNull;

class StreamUtils {

  static @NotNull <T> Stream<T> asStream(@NotNull Iterator<T> iterator) {
    Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.NONNULL);
    return StreamSupport.stream(spliterator, false);
  }

}