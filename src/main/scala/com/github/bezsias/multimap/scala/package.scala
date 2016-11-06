package com.github.bezsias.multimap

import java.math.BigInteger
import java.security.SecureRandom

import com.github.bezsias._

package object scala {
  private val random = new SecureRandom()

  def randomString(length: Int): String = {
    new BigInteger(8192, random).toString(32).take(length)
  }

  type ObjectBytePackager[T <: java.io.Serializable] = multimap.ObjectSimpleBytePackager[T]
  def ObjectBytePackager[T <: java.io.Serializable](blockSizeKb: Int = 64) = new multimap.ObjectSimpleBytePackager[T](blockSizeKb)

  type IntBytePackager = multimap.SimpleIntBytePackager
  def IntBytePackager(blockSizeKb: Int = 64) = new multimap.SimpleIntBytePackager(blockSizeKb)
}
