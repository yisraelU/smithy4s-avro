package io.github.yisraelu.smithy4.avro

import cats.data.Chain
import cats.free.FreeApplicative
import cats.implicits.*
import smithy4s.schema.*
import smithy4s.{Schema, *}
import vulcan.{AvroError, Codec, Prism}

//todo what configurations do we want to pass in here?
// default for precision and scale Decimal


object AvroCodecSchemaVisitor extends CachedSchemaCompiler.Impl[Codec] {

  protected type Aux[A] = Codec[A]



  def fromSchema[A](
      schema: Schema[A],
      cache: Cache
  ): Codec[A] = {
    schema.compile(new AvroCodecSchemaVisitor(cache))
  }

  private final class AvroCodecSchemaVisitor(val cache: Cache) extends SchemaVisitor.Cached[Codec] {
    self =>
    override def primitive[P](shapeId: ShapeId, hints: Hints, tag: Primitive[P]): Codec[P] =
      CodecPrimitives.primSchemaPf(tag, hints)

    override def collection[C[_], A](
        shapeId: ShapeId,
        hints: Hints,
        tag: CollectionTag[C],
        member: Schema[A]
    ): Codec[C[A]] = {
      implicit val memberSchema: Codec[A] = member.compile(this)
      tag match {
        case CollectionTag.ListTag       =>vulcan.Codec.list[A]
        case CollectionTag.SetTag        => vulcan.Codec.set[A]
        case CollectionTag.VectorTag     => vulcan.Codec.vector[A]
        case CollectionTag.IndexedSeqTag => vulcan.Codec.vector[A].imap(_.toIndexedSeq)(_.toVector)
      }
    }

    override def map[K, V](
        shapeId: ShapeId,
        hints: Hints,
        key: Schema[K],
        value: Schema[V]
    ): Codec[Map[K, V]] = {
      implicit val valueSchema: Codec[V] = value.compile(this)
      vulcan.Codec.map[V].asInstanceOf[Codec[Map[K, V]]]
    }

    override def enumeration[E](
        shapeId: ShapeId,
        hints: Hints,
        tag: EnumTag[E],
        values: List[EnumValue[E]],
        total: E => EnumValue[E]
    ): Codec[E] = {
      //todo which annotations are allowed on an enum
      // look into the necessity of props

      def findE(value: String): Either[AvroError, E] = {
        values
          .find(_.stringValue == value)
          .map(_.value)
          .toRight(AvroError(s"$value is not a valid Enum value "))
      }
      Codec.enumeration(
        shapeId.name,
        shapeId.namespace,
        values.map(_.name),
        total(_).stringValue,
        findE
      )
    }

    override def struct[S](
        shapeId: ShapeId,
        hints: Hints,
        fields: Vector[Field[S, _]],
        make: IndexedSeq[Any] => S
    ): Codec[S] = {
      type CodecField[A] = Codec.Field[S, A]
      // todo add handling of field level hints
      def compileField[A](
          field: Field[S, A]
      ): Codec.FieldBuilder[S] => FreeApplicative[CodecField, A] = {
        implicit val compiled: Codec[A] = self(field.schema)
        fieldBuilder => fieldBuilder(field.label, field.get)
      }

      val decoded = fields.map(f => compileField(f))
      vulcan.Codec.record(shapeId.name, shapeId.namespace) { field =>
        {
          decoded
            .map(_.apply(field).asInstanceOf[cats.free.FreeApplicative[CodecField, Any]])
            .traverse(identity)
            .map(v => make(v))
        }
      }
    }

    override def union[U](
        shapeId: ShapeId,
        hints: Hints,
        alternatives: Vector[Alt[U, _]],
        dispatch: Alt.Dispatcher[U]
    ): Codec[U] = {

      case class CodecWithPrism[A](codec: Codec[A], prism: Prism[U, A])

      def compileAlt[A](label:String): CodecWithPrism[A] = {
        val alt = alternatives.find(_.label == label).get.asInstanceOf[Alt[U, A]]
        val codec = self(alt.schema)
        val prism = Prism.instance[U, A](alt.project.lift)(alt.inject)
        CodecWithPrism(codec, prism)
      }

      Codec.union[U](cb => {
        Chain.fromSeq(alternatives).flatMap(
          alt => {
            val codecWithPrism = compileAlt(alt.label)
            cb(codecWithPrism.codec, codecWithPrism.prism)
          }
        )})

    }

    override def biject[A, B](schema: Schema[A], bijection: Bijection[A, B]): Codec[B] = {
      val memberSchema: Codec[A] = schema.compile(this)
      memberSchema.imap(bijection.apply)(bijection.from)
    }

    override def refine[A, B](schema: Schema[A], refinement: Refinement[A, B]): Codec[B] = {
      val memberSchema: Codec[A] = schema.compile(this)
      memberSchema.imapError(refinement.apply(_).left.map(AvroError(_)))(refinement.from)
    }

    override def lazily[A](suspend: Lazy[Schema[A]]): Codec[A] = {
      println(suspend.value)
      self(suspend.value)
    }

    override def option[A](schema: Schema[A]): Codec[Option[A]] = {
      implicit val memberSchema: Codec[A] = schema.compile(this)
      vulcan.Codec.option[A]
    }
  }

}
