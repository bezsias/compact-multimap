package object smultimap {

  type BytePackager[T <: java.io.Serializable] = jmultimap.BytePackager[T]
  def BytePackager[T <: java.io.Serializable](size: Int = 64) = new jmultimap.BytePackager[T](size)

}
