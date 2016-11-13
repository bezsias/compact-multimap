package com.github.bezsias.multimap

import java.math.BigInteger
import java.security.SecureRandom

import com.github.bezsias.{multimap, _}

package object scala {
  private val random = new SecureRandom()

  def randomString(length: Int): String = {
    new BigInteger(8192, random).toString(32).take(length)
  }

  type ObjectBytePackager[T <: java.io.Serializable] = multimap.ObjectBytePackager[T]
  def ObjectBytePackager[T <: java.io.Serializable](blockSizeKb: Int = 64) = new multimap.ObjectBytePackager[T](blockSizeKb)

}
