package com.github.bezsias.multimap.scala

import org.scalatest._

import scala.collection.JavaConverters._

class IntBytePackagerTest extends FunSpecLike with Matchers {

  describe("IntBytePackager Tests") {

    val packager = IntBytePackager(1)

    it("should init byte[] as expected") {
      val bytes = packager.init()
      bytes.length shouldBe 2
    }

    it("should pack/unpack a single item") {
      val bytes = packager.init()
      packager.unpack(packager.pack(bytes, 1)).asScala shouldBe List(1)
    }

    it("should pack/unpack multiple items") {
      val bytes = packager.init()
      val ints = 1.to(5).toList
      val packed = ints.foldLeft(bytes)( (bs, i) => packager.pack(bs, i))
      packager.unpack(packed).asScala shouldBe ints
    }

    it("should pack/unpack large multiple items") {
      val ints = 1.to(1000).toList
      val bytes = packager.init()
      val packed = ints.foldLeft(bytes)( (bs, i) => packager.pack(bs, i))
      packager.unpack(packed).asScala shouldBe ints
    }

    it("should pack/unpack multiple compressed item") {
      val ints = 1.to(2000).toList
      val bytes = packager.init()
      val packed = ints.foldLeft(bytes)( (bs, i) => packager.pack(bs, i))
      packager.unpack(packed).asScala shouldBe ints
    }

    it("should pack/unpack multiple compressed item with pack all") {
      val ints = 1.to(2000).toList
      val bytes = packager.init()
      packager.unpack(packager.pack(bytes, ints.map(new Integer(_)).asJava)).asScala shouldBe ints
    }

    it("should pack/unpack multi compressed item") {
      val bytes = packager.init()
      val ints = List(
        1.to(2000).toList,
        2000.to(4000).toList,
        4000.to(6000).toList,
        6000.to(8000).toList,
        8000.to(10000).toList
      )
      val packed = ints.foldLeft(bytes)( (bs, ints) => packager.pack(bs, ints.map(new Integer(_)).asJava))
      packager.unpack(packed).asScala shouldBe ints.flatten
    }
  }
}
