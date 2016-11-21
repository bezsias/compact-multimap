package com.github.bezsias.multimap.scala

import org.scalatest._

import scala.util.Random

class CompactMultiMapTest extends FunSpecLike with Matchers {

  class MultimapTester[K, V](mapGen: => MultiMap[K, V], keyGen: => K, valueGen: => V) {

    private val key = keyGen
    private val value = valueGen

    private def assertEmptyMap(map: MultiMap[K, V]): Unit = {
      map.size shouldBe 0
      map.keys shouldBe Set()
      map.isEmpty shouldBe true
    }

    describe("empty tests") {

      it("should init an empty map") {
        val map = mapGen

        assertEmptyMap(map)
      }

      it("should clear an empty map") {
        val map = mapGen
        map.clear()

        assertEmptyMap(map)
      }

      it("should clear a non empty map") {
        val map = mapGen
        map.put(keyGen, valueGen)
        map.clear()

        assertEmptyMap(map)
      }
    }

    describe("put tests") {

      it("should put a single item") {
        val map = mapGen
        map.put(key, value)

        map.size shouldBe 1
        map.keys shouldBe Set(key)
        map.contains(key) shouldBe true
        map.contains(key, value) shouldBe true
        map.get(key) shouldBe List(value)
      }

      it("should put a single item multiple times") {
        val map = mapGen
        map.put(key, value)
        map.put(key, value)
        map.put(key, value)

        map.size shouldBe 3
        map.keys shouldBe Set(key)
        map.contains(key) shouldBe true
        map.contains(key, value) shouldBe true
        map.get(key) shouldBe List(value, value, value)
      }

      it("should put multiple distinct keyed items") {
        val map = mapGen
        val n = 100
        val keys = 1.to(n).map(idx => keyGen)
        val values = 1.to(n).map(idx => valueGen)

        val keyvalues = keys zip values
        keyvalues.foreach { case (k, v) => map.put(k, v) }

        map.size shouldBe n
        map.keys shouldBe keys.toSet
        keys.forall(map.contains) shouldBe true
        keyvalues.forall { case (k, v) => map.contains(k, v) } shouldBe true
        keyvalues.forall { case (k, v) => map.get(k) == List(v) } shouldBe true
      }

      it("should put multiple items") {
        val map = mapGen
        val n = 100
        val keyvalues = 1.to(n).map { idx =>
          val k = keyGen
          val vs = 1.to(idx).map { idx =>
            val v = valueGen
            map.put(k, v)
            v
          }
          (k, vs)
        }
        val keys = keyvalues.map(_._1)

        map.size shouldBe (n * (n + 1)) /2
        map.keys.size shouldBe n
        map.keys shouldBe keys.toSet
        keys.forall(map.contains) shouldBe true

        keyvalues.forall { case (k, vs) => vs.forall(map.contains(k, _)) } shouldBe true
        keyvalues.forall { case (k, vs) => map.get(k) == vs.toList } shouldBe true
      }
    }

    describe("remove key tests") {

      it("should not remove from empty map") {
        val map = mapGen
        map.remove(key)
        assertEmptyMap(map)
      }

      it("should remove a single item") {
        val map = mapGen
        map.put(key, value)
        map.remove(key)
        assertEmptyMap(map)
      }

      it("should remove multiple items") {
        val map = mapGen
        map.put(key, value)
        map.put(key, value)
        map.put(key, value)

        map.remove(key)
        assertEmptyMap(map)
      }

      it("should remove multiple distinct keyed items") {
        val map = mapGen
        val n = 100
        val keys = 1.to(n).map(idx => keyGen)
        val values = 1.to(n).map(idx => valueGen)

        val keyvalues = keys zip values
        keyvalues.foreach { case (k, v) => map.put(k, v) }

        keys.foreach(map.remove)

        assertEmptyMap(map)
      }

      it("should remove multiple items (complex)") {
        val map = mapGen
        val n = 100
        val keyvalues = 1.to(n).map { idx =>
          val k = keyGen
          val vs = 1.to(idx).map { idx =>
            val v = valueGen
            map.put(k, v)
            v
          }
          (k, vs)
        }
        val keys = keyvalues.map(_._1)
        keys.foreach(map.remove)

        assertEmptyMap(map)
      }
    }

    describe("remove key-value tests") {

      it("should not remove from empty map") {
        val map = mapGen
        map.remove(key)
        assertEmptyMap(map)
      }

      it("should remove a single item") {
        val map = mapGen
        map.put(key, value)
        map.remove(key, value)
        assertEmptyMap(map)
      }

      it("should remove multiple items") {
        val map = mapGen
        map.put(key, value)
        map.put(key, value)
        map.put(key, value)

        map.remove(key, value)
        assertEmptyMap(map)
      }

      it("should remove multiple distinct keyed items") {
        val map = mapGen
        val n = 100
        val keys = 1.to(n).map(idx => keyGen)
        val values = 1.to(n).map(idx => valueGen)

        val keyvalues = keys zip values
        keyvalues.foreach { case (k, v) => map.put(k, v) }

        keyvalues.foreach { case (k, v) => map.remove(k, v) }

        assertEmptyMap(map)
      }

      it("should remove multiple items (complex)") {
        val map = mapGen
        val n = 10
        val keyvalues = 1.to(n).map { idx =>
          val k = keyGen
          val vs = 1.to(idx).map { idx =>
            val v = valueGen
            map.put(k, v)
            v
          }
          (k, vs)
        }
        val keys = keyvalues.map(_._1)

        keyvalues.foreach { case (k, vs) => vs.foreach(map.remove(k, _)) }
        assertEmptyMap(map)
      }
    }
  }

  describe("Boolean CompactMultiMap") {
    new MultimapTester[String, Boolean](CompactMultiMap.booleanMap[String](), Random.nextString(10), Random.nextBoolean())
  }

  describe("Byte CompactMultiMap") {
    new MultimapTester[String, Byte](CompactMultiMap.byteMap[String](), Random.nextString(10), Random.nextInt(100).toByte)
  }

  describe("Short CompactMultiMap") {
    new MultimapTester[String, Short](CompactMultiMap.shortMap[String](), Random.nextString(10), Random.nextInt(100).toShort)
  }

  describe("Int CompactMultiMap") {
    new MultimapTester[String, Int](CompactMultiMap.intMap[String](), Random.nextString(10), Random.nextInt(100))
  }

  describe("Long CompactMultiMap") {
    new MultimapTester[String, Long](CompactMultiMap.longMap[String](), Random.nextString(10), Random.nextLong)
  }

  describe("Float CompactMultiMap") {
    new MultimapTester[String, Float](CompactMultiMap.floatMap[String](), Random.nextString(10), Random.nextFloat)
  }

  describe("Double CompactMultiMap") {
    new MultimapTester[String, Double](CompactMultiMap.doubleMap[String](), Random.nextString(10), Random.nextDouble())
  }

  describe("String CompactMultiMap") {
    new MultimapTester[String, String](CompactMultiMap.objectMap[String, String](), Random.nextString(10), Random.nextString(10))
  }

}
