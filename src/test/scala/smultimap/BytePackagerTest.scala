package smultimap

import org.scalatest._
import collection.JavaConverters._

class BytePackagerTest extends FunSpecLike with Matchers {

  describe("BytePackager Tests") {

    val packager = BytePackager[String](1)

    it("should init byte[] as expected") {
      val bytes = packager.init()
      bytes.length shouldBe 2
    }

    it("should pack/unpack a single item") {
      val bytes = packager.init()
      val str = "some test string"
      packager.unpack(packager.pack(bytes, str)).asScala shouldBe List(str)
    }

    it("should pack/unpack multiple items") {
      val bytes = packager.init()
      val strs = List("a", "b", "c", "d")
      val packed = strs.foldLeft(bytes)(packager.pack)
      packager.unpack(packed).asScala shouldBe strs
    }

    it("should pack/unpack large item") {
      val str = Array.fill(1000)("a").mkString
      val bytes = packager.init()
      packager.unpack(packager.pack(bytes, str)).asScala shouldBe List(str)

    }

    it("should pack/unpack single compressed item") {
      val str = Array.fill(2000)("a").mkString
      val bytes = packager.init()
      packager.unpack(packager.pack(bytes, str)).asScala shouldBe List(str)
    }

    it("should pack/unpack multi compressed item") {
      val bytes = packager.init()
      val strs = List(
        Array.fill(2000)("a").mkString,
        Array.fill(2000)("b").mkString,
        Array.fill(2000)("c").mkString,
        Array.fill(2000)("d").mkString
      )

      val packed = strs.foldLeft(bytes)(packager.pack)
      packager.unpack(packed).asScala shouldBe strs
    }

  }

}
