package ru.zorb.services.meta;

import org.jooq.Record;
import org.jooq.impl.TableImpl;

import java.util.Collection;

public interface POJOContentDataSource {

  // @Override
  <R extends Record, E> Collection<E> getObjects(TableImpl<R> table, Class<? extends E> type, Collection<String> ids);

}