package org.apache.ibatis.executor.result;

import org.apache.ibatis.annotations.MapValue;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.IConverter;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.Map;

/**
 * @since 3.2.2.m1
 */
public class CustomMapResultHandler<K, V> implements ResultHandler {

  private final Map<K, Object> mappedResults;
  private final String mapKey;
  private final MapValue mapValue;
  private final ObjectFactory objectFactory;
  private final ObjectWrapperFactory objectWrapperFactory;

  @SuppressWarnings("unchecked")
  public CustomMapResultHandler(String mapKey, MapValue mapValue, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
    this.objectFactory = objectFactory;
    this.objectWrapperFactory = objectWrapperFactory;
    this.mappedResults = objectFactory.create(Map.class);
    this.mapKey = mapKey;
    this.mapValue = mapValue;
  }

  public void handleResult(ResultContext context) {
    Object value = context.getResultObject();
    final MetaObject mo = MetaObject.forObject(value, objectFactory, objectWrapperFactory);
    final K key = (K) mo.getValue(mapKey);
    final Object newValue = getValue(value, mo);
    mappedResults.put(key, newValue);
  }

  private Object getValue(final Object value, final MetaObject mo) {
    if (mapValue == null) { return value; }
    String valueKey = mapValue.value();
    if (!isEmpty(valueKey)) { return mo.getValue(valueKey); }
    Class<? extends IConverter> clazz = mapValue.converter();
    if (IConverter.class.equals(clazz)) { return value; }
    try {
      return clazz.newInstance().convert(value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  public Map<K, Object> getMappedResults() {
    return mappedResults;
  }
}
