package com.github.bezsias.multimap.scala

import com.github.bezsias._

class CompactMultiMap[K, V <: java.io.Serializable] private (blockSizeKb: Int = 64) extends MultiMap[K, V] {
  import collection.JavaConverters._

  private val map = new multimap.CompactMultiMap[K, V](blockSizeKb)

  override def size: Int = map.size

  override def isEmpty: Boolean = map.isEmpty

  override def contains(key: K): Boolean = map.contains(key)

  override def contains(key: K, value: V): Boolean = map.contains(key, value)

  override def keys: Set[K] = map.keys().asScala.toSet

  override def put(key: K, value: V): Unit = map.put(key, value)

  override def putAll(key: K, values: List[V]): Unit = map.putAll(key, values.asJava)

  override def get(key: K): List[V] = map.get(key).asScala.toList

  override def remove(key: K): Unit = map.remove(key)

  override def remove(key: K, value: V): Unit = map.remove(key, value)

  override def clear(): Unit = map.clear()
}

object CompactMultiMap {

  def apply[K, V <: java.io.Serializable](blockSizeKb: Int = 64): CompactMultiMap[K, V] = new CompactMultiMap[K, V](blockSizeKb)
}
