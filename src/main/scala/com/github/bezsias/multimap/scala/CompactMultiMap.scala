package com.github.bezsias.multimap.scala

import java.util

import com.github.bezsias._
import com.github.bezsias.multimap.{MapFactory, MultiMapBuilder}

import scala.collection.immutable.HashMap

case class CompactMultiMap[K, V] private (map: multimap.MultiMap[K, V]) extends MultiMap[K, V] {
  import collection.JavaConverters._

  override def size: Int = map.size

  override def memoryUsage: Int = map.memoryUsage()

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
  private val DEFAULT_BLOCK = 8

  private def DEFAULT_MAP_FACTORY[K] = new MapFactory[K] {
    override def createMap(): util.Map[K, Array[Byte]] = new util.HashMap()
  }

  def apply[K, J, S](
    blockSizeKb: Int = DEFAULT_BLOCK,
    mapFactory: MapFactory[K] = DEFAULT_MAP_FACTORY[K]
  )(ctor: MultiMapBuilder[K] => multimap.MultiMap[K, J]): MultiMap[K, S] = {
    val builder = new MultiMapBuilder[K]().blockSizeKb(blockSizeKb).mapFactory(mapFactory)
    val map: multimap.MultiMap[K,S] = ctor(builder).asInstanceOf[multimap.MultiMap[K, S]]
    new CompactMultiMap[K, S](map)
  }

  def booleanMap[K](blockSizeKb: Int = DEFAULT_BLOCK): MultiMap[K, Boolean] =
    apply(blockSizeKb)(_.booleanMap())

  def byteMap[K](blockSizeKb: Int = DEFAULT_BLOCK): MultiMap[K, Byte] =
    apply(blockSizeKb)(_.byteMap())

  def shortMap[K](blockSizeKb: Int = DEFAULT_BLOCK): MultiMap[K, Short] =
    apply(blockSizeKb)(_.shortMap())

  def intMap[K](blockSizeKb: Int = DEFAULT_BLOCK): MultiMap[K, Int] =
    apply(blockSizeKb)(_.intMap())

  def longMap[K](blockSizeKb: Int = DEFAULT_BLOCK): MultiMap[K, Long] =
    apply(blockSizeKb)(_.longMap())

  def floatMap[K](blockSizeKb: Int = DEFAULT_BLOCK): MultiMap[K, Float] =
    apply(blockSizeKb)(_.floatMap())

  def doubleMap[K](blockSizeKb: Int = DEFAULT_BLOCK): MultiMap[K, Double] =
    apply(blockSizeKb)(_.doubleMap())

  def objectMap[K, V](blockSizeKb: Int = DEFAULT_BLOCK): MultiMap[K, V] =
    apply(blockSizeKb)(_.objectMap())

}
