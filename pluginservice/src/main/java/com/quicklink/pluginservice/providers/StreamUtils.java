package com.quicklink.pluginservice.providers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class StreamUtils {

  static <T> Stream<T> asStream(Iterator<T> iterator) {
    Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.NONNULL);
    return StreamSupport.stream(spliterator, false);
  }

}