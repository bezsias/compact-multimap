package com.github.bezsias.multimap.scala

import com.github.bezsias.multimap.{BytePack, BytePackager}
import org.scalatest._

import scala.collection.JavaConverters._
import scala.util.Random

class BytePackagerTests extends FunSpecLike with Matchers {

  class BytePackagerTester[T](packager: BytePackager[T], generator: => T) {

    def testPackSingle(n: Int): Unit = {
      val bytes = new BytePack()
      val items = 1.to(n).map(_ => generator).toList
      val packed = items.foldLeft(bytes)( (bs, i) => packager.pack(bs, i))
      packager.unpack(packed).asScala shouldBe items
    }

    def testPackBatch(n: Int, m: Int = 1): Unit = {
      val bytes = new BytePack()
      val length = n / m
      val items = 1.to(m).map { row =>
        (length * (row - 1) + 1).to(length * row).map(_ => generator).toList
      }.toList

      val packed = items.foldLeft(bytes)((bs, is) => packager.pack(bs, is.asJava))
      packager.unpack(packed).asScala shouldBe items.flatten
    }

    it("should init byte[] as expected") {
      val bytes = new BytePack()
      bytes.size shouldBe 0
    }

    it("should pack/unpack single item") {
      testPackSingle(1)
    }

    it("should pack/unpack single item (batch)") {
      testPackBatch(1)
    }

    it("should pack/unpack multiple items") {
      testPackSingle(10)
    }

    it("should pack/unpack multiple items (batch)") {
      testPackBatch(10)
    }

    it("should pack/unpack multiple items with compression") {
      testPackSingle(2000)
    }

    it("should pack/unpack multiple items with compression (batch)") {
      testPackBatch(2000)
    }

    it("should pack/unpack multiple compressed item (batch)") {
      testPackBatch(10000, 5)
    }

  }

  describe("Boolean BytePackager Tests") {
    new BytePackagerTester[Boolean](BytePackager.booleanBytePackager(1).asInstanceOf[BytePackager[Boolean]], Random.nextBoolean)
  }

  describe("Byte BytePackager Tests") {
    new BytePackagerTester[Byte](BytePackager.byteBytePackager(1).asInstanceOf[BytePackager[Byte]], Random.nextInt(100).toByte)
  }

  describe("Short BytePackager Tests") {
    new BytePackagerTester[Short](BytePackager.shortBytePackager(1).asInstanceOf[BytePackager[Short]], Random.nextInt(100).toShort)
  }

  describe("Int BytePackager Tests") {
    new BytePackagerTester[Int](BytePackager.intBytePackager(1).asInstanceOf[BytePackager[Int]], Random.nextInt(100))
  }

  describe("Long BytePackager Tests") {
    new BytePackagerTester[Long](BytePackager.longBytePackager(1).asInstanceOf[BytePackager[Long]], Random.nextInt(100).toLong)
  }

  describe("Float BytePackager Tests") {
    new BytePackagerTester[Float](BytePackager.floatBytePackager(1).asInstanceOf[BytePackager[Float]], Random.nextFloat())
  }

  describe("Double BytePackager Tests") {
    new BytePackagerTester[Double](BytePackager.doubleBytePackager(1).asInstanceOf[BytePackager[Double]], Random.nextDouble())
  }

  describe("Object BytePackager Tests") {
    new BytePackagerTester[String](BytePackager.objBytePackager(1), randomString(5))
  }

}
