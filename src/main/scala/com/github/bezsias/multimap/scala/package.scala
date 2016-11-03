package com.github.bezsias.multimap

import com.github.bezsias._

package object scala {

  type BytePackager[T <: java.io.Serializable] = multimap.BytePackager[T]
  def BytePackager[T <: java.io.Serializable](blockSizeKb: Int = 64) = new multimap.BytePackager[T](blockSizeKb)

  type IntBytePackager = multimap.IntBytePackager
  def IntBytePackager(blockSizeKb: Int = 64) = new multimap.IntBytePackager(blockSizeKb)
}
