package com.github.bezsias.multimap.scala

import com.github.bezsias.multimap.{BytePack, BytePackager, PrimitiveBytePackager}
import org.scalatest.{FunSpecLike, Matchers}

import scala.util.Random

class BytePackagerBenchmarks extends FunSpecLike with Matchers {

  class BytePackagerTester[T](packager: BytePackager[T], generator: => T, expectedBytes: Int, n: Int, blockSizeKb: Int) {

    def test(n: Int): String = {
      val time = System.currentTimeMillis()
      val bytes = new BytePack()
      val items = 1.to(n).map(_ => generator).toList
      val packed = items.foldLeft(bytes)((bs, i) => packager.pack(bs, i))

      val expectedLength = expectedBytes * n
      val packedSize = packed.size
      val inflation = packedSize / expectedLength.toDouble

      val elapsed = System.currentTimeMillis() - time

      s"buffer: ${blockSizeKb}K, items: $n, data: $expectedLength, packed: $packedSize bytes, inflation: ${"%02.2f".format(inflation * 100)}%, $elapsed ms"
    }

    it(s"${test(n)}") {}
  }

  def desc(name: String, count: Int, init: (Int, Int) => Unit) = {
    describe(s"$name byte packager ") {
      0.to(6).foreach(p => init(Math.pow(2, p).toInt, count))
    }
  }

  desc("string", 1000, (blockSizeKb, n) => {
    val length = 100
    new BytePackagerTester[String](ObjectBytePackager[String](blockSizeKb), randomString(length), length, n, blockSizeKb)
  })

  desc("int", 5000, (blockSizeKb, n) => {
    new BytePackagerTester[Integer](
        PrimitiveBytePackager.intBytePackager(blockSizeKb), Random.nextInt(100), 4, n, blockSizeKb)
  })

  desc("short", 10000, (blockSizeKb, n) => {
    val packager: BytePackager[Short] = PrimitiveBytePackager.shortBytePackager(blockSizeKb).asInstanceOf[BytePackager[Short]]
    new BytePackagerTester[Short](packager, Random.nextInt(100).toShort, 2, n, blockSizeKb)
  })

}
