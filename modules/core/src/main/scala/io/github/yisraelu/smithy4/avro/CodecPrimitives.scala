package io.github.yisraelu.smithy4.avro

import smithy4s.schema.Primitive
import smithy4s.{Blob, Document, Hints}
import vulcan.Codec

object CodecPrimitives {

  implicit val bigIntCodec: Codec[BigInt] = Codec.int.imap(BigInt.int2bigInt)(_.intValue)
  implicit def bigDecimalCodec(precision: Int, scale: Int): Codec[BigDecimal] =
    Codec.decimal(precision, scale)

  def primSchemaPf[T](tag: Primitive[T], hints: Hints): Codec[T] =
    tag match {
      case Primitive.PShort  => Codec.short
      case Primitive.PInt    => Codec.int
      case Primitive.PFloat  => Codec.float
      case Primitive.PLong   => Codec.long
      case Primitive.PDouble => Codec.double
      case Primitive.PBigInt => bigIntCodec
      case Primitive.PBigDecimal => {
        /* if(hints.has[Precision]&& hints.has[Scale])
          bigDecimalCodec(hints.get[Precision],hints.get[Scale])*/
        //else {
        //todo should there be a default for scale and precision?
        ///bigDecimalCodec()
        ???
        //}
      }
      case Primitive.PBoolean  => Codec.boolean
      case Primitive.PString   => Codec.string
      case Primitive.PUUID     => Codec.uuid
      case Primitive.PByte     => Codec.byte
      case Primitive.PBlob     => Codec.bytes.imap(Blob(_))(_.toArray)
      case Primitive.PDocument => Codec.string.imap(Document.fromString)(_.toString())
      case Primitive.PTimestamp =>
        Codec.long.imap(smithy4s.Timestamp.fromEpochSecond)(_.epochSecond)
    }

}
