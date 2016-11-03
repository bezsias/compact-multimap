package com.github.bezsias.multimap.scala

import org.scalatest.{FunSpecLike, Matchers}
import scala.util.Random

class BytePackagerBenchmarks extends FunSpecLike with Matchers {

  class BytePackagerTester[T](packager: com.github.bezsias.multimap.BytePackager[T], generator: => T, expectedBytes: Int, n: Int) {

    def test(n: Int): String = {
      val time = System.currentTimeMillis()
      val bytes = packager.init()
      val items = 1.to(n).map(_ => generator).toList
      val packed = items.foldLeft(bytes)((bs, i) => packager.pack(bs, i))

      val expectedLength = (expectedBytes * n) + 2
      val inflation = packed.length.toDouble / expectedLength

      val elapsed = System.currentTimeMillis() - time

      s"raw: $expectedLength, packed: ${packed.length} bytes, inflation: ${"%02.2f".format(inflation * 100)}%, $elapsed ms"
    }

    it(s"${test(n)}") {}
  }

  def stringTests(blockSizeKb: Int, length: Int, n: Int = 1000): Unit = {
    val stringLengthOverhead = 4

    describe(s"string byte packager (${blockSizeKb}K buffer, $length bytes, $n items)") {
      val expectedSize = Random.nextString(length).getBytes("UTF-8").length + stringLengthOverhead
      new BytePackagerTester[String](ObjectBytePackager[String](blockSizeKb), Random.nextString(length), expectedSize, n)
    }
  }

  stringTests(1, 5, 1000)
  stringTests(1, 5, 5000)
  stringTests(1, 5, 10000)

  stringTests(2, 5)

  stringTests(4, 5)

  stringTests(8, 5)

  stringTests(16, 5)

  stringTests(1, 25)

  stringTests(2, 50)

  stringTests(4, 100)

  stringTests(8, 100)

  stringTests(16, 100)

  stringTests(32, 100)

  stringTests(64, 100)

}
