package com.github.bezsias.multimap

import com.github.bezsias._

package object scala {

  type ObjectBytePackager[T <: java.io.Serializable] = multimap.ObjectBytePackager[T]
  def ObjectBytePackager[T <: java.io.Serializable](blockSizeKb: Int = 64) = new multimap.ObjectBytePackager[T](blockSizeKb)

  type IntBytePackager = multimap.IntBytePackager
  def IntBytePackager(blockSizeKb: Int = 64) = new multimap.IntBytePackager(blockSizeKb)
}
