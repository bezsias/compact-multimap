package com.github.bezsias.multimap.scala

import org.scalatest.{FunSpecLike, Matchers}

class MultiMapBenchmarks extends FunSpecLike with Matchers {

  class MultiMapTester (
    map: CompactMultiMap[String, String],
    gen: => String,
    keyCount: Int,
    valueCount: Int
  ) {

    def test: String = {
      val time = System.currentTimeMillis()
      var keySize = 0
      var valueSize = 0
      1.to(keyCount).map { row =>
        val key = gen
        val values = 1.to(valueCount).map(_ => gen).toList
        keySize += key.getBytes.length
        valueSize += values.map(_.getBytes.length).sum
        values.foreach(map.put(key, _))
        (key, values)
      }.toMap

      val elapsed = System.currentTimeMillis() - time

      val dataSize = keySize + valueSize

      val mapSize = keySize + map.memoryUsage()

      val inflation = mapSize / dataSize.toDouble

      s"data: $dataSize, stored: $mapSize bytes, inflation: ${"%02.2f".format(inflation * 100)}%, $elapsed ms"
    }

    it(s"$test") {}
  }

  def test(blockSizeKb: Int, length: Int, keyCount: Int, valueCount: Int): Unit = {
    describe(s"string multimap (${blockSizeKb}K buffer, $length bytes, $keyCount keys, $valueCount values)") {
      val map = CompactMultiMap[String, String](blockSizeKb)
      new MultiMapTester(map, randomString(length), keyCount, valueCount)
    }
  }

  test(1, 100, 1, 100)
  test(1, 100, 1, 1000)
  test(1, 100, 1, 5000)
  test(1, 100, 1, 10000)
  test(1, 100, 1, 15000)

  test(64, 100, 1, 100)
  test(64, 100, 1, 1000)
  test(64, 100, 1, 5000)
  test(64, 100, 1, 10000)
  test(64, 100, 1, 15000)

}
