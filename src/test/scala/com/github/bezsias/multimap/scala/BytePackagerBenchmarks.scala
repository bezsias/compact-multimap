package com.github.bezsias.multimap.scala

import com.github.bezsias.multimap.{BytePack, BytePackager}
import org.scalatest.{FunSpecLike, Matchers}

import scala.util.Random

class BytePackagerBenchmarks extends FunSpecLike with Matchers {

  class BytePackagerTester[T](packager: BytePackager[T], generator: => T, expectedBytes: Int, n: Int, blockSizeKb: Int) {

    def test: String = {
      val time = System.currentTimeMillis()
      val bytes = new BytePack()
      val items = 1.to(n).map(_ => generator).toList
      val packed = items.foldLeft(bytes)((bs, i) => packager.pack(bs, i))

      val expectedLength = expectedBytes * n
      val packedSize = packed.size
      val inflation = packedSize / expectedLength.toDouble

      val elapsed = System.currentTimeMillis() - time

      s"buffer: ${blockSizeKb}K, items: $n, data: $expectedLength, packed: $packedSize bytes, inflated: ${"%02.2f".format(inflation * 100)}%, $elapsed ms"
    }

    it(s"$test") {}
  }

  def desc(name: String, count: Int, init: (Int, Int) => Unit) = {
    describe(s"$name byte packager ") {
      0.to(6).foreach(p => init(Math.pow(2, p).toInt, count))
    }
  }

  desc("boolean", 100000, (blockSizeKb, n) => {
    val packager: BytePackager[Boolean] = BytePackager.booleanBytePackager(blockSizeKb).asInstanceOf[BytePackager[Boolean]]
    new BytePackagerTester[Boolean](packager, Random.nextBoolean(), 1, n, blockSizeKb)
  })

  desc("byte", 100000, (blockSizeKb, n) => {
    val packager: BytePackager[Byte] = BytePackager.byteBytePackager(blockSizeKb).asInstanceOf[BytePackager[Byte]]
    new BytePackagerTester[Byte](packager, Random.nextInt(100).toByte, 1, n, blockSizeKb)
  })

  desc("short", 50000, (blockSizeKb, n) => {
    val packager: BytePackager[Short] = BytePackager.shortBytePackager(blockSizeKb).asInstanceOf[BytePackager[Short]]
    new BytePackagerTester[Short](packager, Random.nextInt(100).toShort, 2, n, blockSizeKb)
  })

  desc("int", 25000, (blockSizeKb, n) => {
    new BytePackagerTester[Int](BytePackager.intBytePackager(blockSizeKb).asInstanceOf[BytePackager[Int]], Random.nextInt(100), 4, n, blockSizeKb)
  })

  desc("long", 12500, (blockSizeKb, n) => {
    new BytePackagerTester[Long](BytePackager.longBytePackager(blockSizeKb).asInstanceOf[BytePackager[Long]], Random.nextInt(100).toLong, 8, n, blockSizeKb)
  })

  desc("float", 25000, (blockSizeKb, n) => {
    new BytePackagerTester[Float](BytePackager.floatBytePackager(blockSizeKb).asInstanceOf[BytePackager[Float]], Random.nextFloat(), 4, n, blockSizeKb)
  })

  desc("double", 12500, (blockSizeKb, n) => {
    new BytePackagerTester[Double](BytePackager.doubleBytePackager(blockSizeKb).asInstanceOf[BytePackager[Double]], Random.nextDouble(), 8, n, blockSizeKb)
  })

  desc("string", 1000, (blockSizeKb, n) => {
    val length = 100
    new BytePackagerTester[String](BytePackager.objBytePackager(blockSizeKb), Random.nextString(length), 3 * length, n, blockSizeKb)
  })
}
