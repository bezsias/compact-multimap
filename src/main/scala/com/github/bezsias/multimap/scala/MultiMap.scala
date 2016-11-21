package com.github.bezsias.multimap.scala

trait MultiMap[K, V] {

  /** Returns the number of key-value pairs. */
  def size: Int

  def isEmpty: Boolean

  def contains(key: K): Boolean

  def contains(key: K, value: V): Boolean

  def keys: Set[K]

  def put(key: K, value: V)

  def putAll(key: K, values: List[V])

  def get(key: K): List[V]

  /** Removes all values associated with key. */
  def remove(key: K)

  /** Returns the count of removed elements */
  def remove(key: K, value: V)

  def clear()

}
