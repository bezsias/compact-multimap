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

  describe("ObjectBytePackager Tests") {
    new BytePackagerTester[String](ObjectBytePackager[String](1), randomString(5))
  }

  describe("IntBytePackager Tests") {
    new BytePackagerTester[Integer](IntBytePackager(1), Random.nextInt(100))
  }

  describe("ShortBytePackager Tests") {
    new BytePackagerTester[Short](ShortBytePackager(2).asInstanceOf[BytePackager[Short]], Random.nextInt(100).toShort)
  }

}
