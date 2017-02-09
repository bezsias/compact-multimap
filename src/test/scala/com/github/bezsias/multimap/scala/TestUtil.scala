package com.github.bezsias.multimap.scala

import scala.util.Random

object TestUtil {

  def randomByteArray(length: Int): Array[Byte] = {
    val bytes = Array.fill(length)(0.toByte)
    Random.nextBytes(bytes)
    bytes
  }
}
