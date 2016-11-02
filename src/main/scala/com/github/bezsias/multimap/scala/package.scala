package com.github.bezsias.multimap

import com.github.bezsias._

package object scala {

  type BytePackager[T <: java.io.Serializable] = multimap.BytePackager[T]
  def BytePackager[T <: java.io.Serializable](blockSizeKb: Int = 64) = new multimap.BytePackager[T](blockSizeKb)
}
