package com.github.bezsias.multimap.scala

import java.math.BigInteger
import java.security.SecureRandom

import org.scalatest._

import scala.collection.JavaConverters._
import scala.util.Random

class BytePackagerTests extends FunSpecLike with Matchers {

  private val random = new SecureRandom()

  private def randomString(length: Int): String = {
    new BigInteger(1024, random).toString(32).take(length)
  }

  class BytePackagerTester[T](packager: com.github.bezsias.multimap.BytePackager[T], generator: => T) {

    def testPackSingle(n: Int): Unit = {
      val bytes = packager.init()
      val items = 1.to(n).map(_ => generator).toList
      val packed = items.foldLeft(bytes)( (bs, i) => packager.pack(bs, i))
      packager.unpack(packed).asScala shouldBe items
    }

    def testPackBatch(n: Int, m: Int = 1): Unit = {
      val bytes = packager.init()
      val length = n / m
      val items = 1.to(m).map { row =>
        (length * (row - 1) + 1).to(length * row).map(_ => generator).toList
      }.toList

      val packed = items.foldLeft(bytes)((bs, is) => packager.pack(bs, is.asJava))
      packager.unpack(packed).asScala shouldBe items.flatten
    }

    it("should init byte[] as expected") {
      val bytes = packager.init()
      bytes.length shouldBe 2
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

    it("should pack/unpack multiple compressed item (batch") {
      testPackBatch(10000, 5)
    }

  }

  describe("ObjectBytePackager Tests") {
    new BytePackagerTester[String](ObjectBytePackager[String](1), randomString(5))
  }

  describe("IntBytePackager Tests") {
    new BytePackagerTester[Integer](IntBytePackager(1), Random.nextInt)
  }

}
