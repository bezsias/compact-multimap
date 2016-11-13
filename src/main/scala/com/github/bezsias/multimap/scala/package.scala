package com.github.bezsias.multimap

import java.math.BigInteger
import java.security.SecureRandom

package object scala {
  private val random = new SecureRandom()

  def randomString(length: Int): String = {
    new BigInteger(8192, random).toString(32).take(length)
  }

}
