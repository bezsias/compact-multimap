package com.github.bezsias.multimap.scala

import org.scalatest._
import collection.JavaConverters._

class CompactMultiMapTest extends FunSpecLike with Matchers {

  describe("CompactMultiMap Tests") {

    val key = "some key"
    val value = "some value"

    def assertEmptyMap(map: MultiMap[String, String]): Unit = {
      map.size() shouldBe 0
      map.keys().asScala shouldBe Set()
      map.isEmpty shouldBe true
    }

    describe("empty tests") {

      it("should init an empty map") {
        val map = CompactMultiMap[String, String]()

        assertEmptyMap(map)
      }

      it("should clear an empty map") {
        val map = CompactMultiMap[String, String]()
        map.clear()

        assertEmptyMap(map)
      }

      it("should clear a non empty map") {
        val map = CompactMultiMap[String, String]()
        map.put(key, value)
        map.clear()

        assertEmptyMap(map)
      }
    }


    describe("put tests") {

      it("should put a single item") {
        val map = CompactMultiMap[String, String]()
        map.put(key, value)

        map.size() shouldBe 1
        map.keys.asScala shouldBe Set(key)
        map.contains(key) shouldBe true
        map.contains(key, value) shouldBe true
        map.get(key).asScala shouldBe List(value)
      }

      it("should put a single item multiple times") {
        val map = CompactMultiMap[String, String]()
        map.put(key, value)
        map.put(key, value)
        map.put(key, value)

        map.size() shouldBe 3
        map.keys.asScala shouldBe Set(key)
        map.contains(key) shouldBe true
        map.contains(key, value) shouldBe true
        map.get(key).asScala shouldBe List(value, value, value)
      }

      it("should put multiple distinct keyed items") {
        val map = CompactMultiMap[String, String]()
        val n = 100
        val keys = 1.to(n).map(idx => key + idx)
        val values = 1.to(n).map(idx => value + idx)

        val keyvalues = keys zip values
        keyvalues.foreach { case (k, v) => map.put(k, v) }

        map.size() shouldBe n
        map.keys().asScala shouldBe keys.toSet
        keys.forall(map.contains) shouldBe true
        keyvalues.forall { case (k, v) => map.contains(k, v) } shouldBe true
        keyvalues.forall { case (k, v) => map.get(k).asScala == List(v) } shouldBe true

      }

      it("should put multiple items") {
        val map = CompactMultiMap[String, String]()
        val n = 100
        val keyvalues = 1.to(n).map { idx =>
          val k = key + idx
          val vs = 1.to(idx).map { idx =>
            val v = value + idx
            map.put(k, v)
            v
          }
          (k, vs)
        }
        val keys = keyvalues.map(_._1)

        map.size() shouldBe (n * (n + 1)) /2
        map.keys().size() shouldBe n
        map.keys().asScala shouldBe keys.toSet
        keys.forall(map.contains) shouldBe true

        keyvalues.forall { case (k, vs) => vs.forall(map.contains(k, _)) } shouldBe true
        keyvalues.forall { case (k, vs) => map.get(k).asScala == vs.toList } shouldBe true

      }
    }

    describe("remove key tests") {

      it("should not remove from empty map") {
        val map = CompactMultiMap[String, String]()
        map.remove(key)
        assertEmptyMap(map)
      }

      it("should remove a single item") {
        val map = CompactMultiMap[String, String]()
        map.put(key, value)
        map.remove(key)
        assertEmptyMap(map)
      }

      it("should remove multiple items") {
        val map = CompactMultiMap[String, String]()
        map.put(key, value)
        map.put(key, value)
        map.put(key, value)

        map.remove(key)
        assertEmptyMap(map)
      }

      it("should remove multiple distinct keyed items") {
        val map = CompactMultiMap[String, String]()
        val n = 100
        val keys = 1.to(n).map(idx => key + idx)
        val values = 1.to(n).map(idx => value + idx)

        val keyvalues = keys zip values
        keyvalues.foreach { case (k, v) => map.put(k, v) }

        keys.foreach(map.remove)

        assertEmptyMap(map)
      }

      it("should remove multiple items (complex)") {
        val map = CompactMultiMap[String, String]()
        val n = 100
        val keyvalues = 1.to(n).map { idx =>
          val k = key + idx
          val vs = 1.to(idx).map { idx =>
            val v = value + idx
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
        val map = CompactMultiMap[String, String]()
        map.remove(key)
        assertEmptyMap(map)
      }

      it("should remove a single item") {
        val map = CompactMultiMap[String, String]()
        map.put(key, value)
        map.remove(key, value)
        assertEmptyMap(map)
      }

      it("should remove multiple items") {
        val map = CompactMultiMap[String, String]()
        map.put(key, value)
        map.put(key, value)
        map.put(key, value)

        map.remove(key, value)
        assertEmptyMap(map)
      }

      it("should remove multiple distinct keyed items") {
        val map = CompactMultiMap[String, String]()
        val n = 100
        val keys = 1.to(n).map(idx => key + idx)
        val values = 1.to(n).map(idx => value + idx)

        val keyvalues = keys zip values
        keyvalues.foreach { case (k, v) => map.put(k, v) }

        keyvalues.foreach { case (k, v) => map.remove(k, v) }

        assertEmptyMap(map)
      }

      it("should remove multiple items (complex)") {
        val map = CompactMultiMap[String, String]()
        val n = 2
        val keyvalues = 1.to(n).map { idx =>
          val k = key + idx
          val vs = 1.to(idx).map { idx =>
            val v = value + idx
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
}
