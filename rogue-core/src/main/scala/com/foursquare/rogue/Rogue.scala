// Copyright 2011 Foursquare Labs Inc. All Rights Reserved.

package com.foursquare.rogue

import com.foursquare.field.{
    Field => RField,
    OptionalField => ROptionalField,
    RequiredField => RRequiredField}
import com.mongodb.DBObject
import java.util.{Calendar, Date}
import org.bson.types.ObjectId
import com.foursquare.rogue.MongoHelpers.MongoModify

/**
 * A utility trait containing typing shorthands, and a collection of implicit conversions that make query
 * syntax much simpler.
 *
 *@see AbstractQuery for an example of the use of implicit conversions.
 */
trait Rogue {

  /* A couple of implicit conversions that take a query builder, and convert it to a modify. This allows
   * users to write "RecordType.where(...).modify(...)".
   */
  implicit def queryBuilderToModifyQuery[M, State <: Unordered with Unselected with Unlimited with Unskipped]
    (query: Query[M, M, State])
    (implicit ev: ShardingOk[M, State]): ModifyQuery[M, State] = {
    new ModifyQuery[M, State](query, MongoModify(Nil))
  }

  implicit def queryBuilderToFindAndModifyQuery[M, R, State <: Unlimited with Unskipped]
    (query: Query[M, R, State])
    (implicit ev: RequireShardKey[M, State]): FindAndModifyQuery[M, R] = {
    FindAndModifyQuery[M, R](query, MongoModify(Nil))
  }


  // QueryField implicits
  implicit def rbooleanFieldtoQueryField[M](f: RField[Boolean, M]): QueryField[Boolean, M] = new QueryField(f)
  implicit def rcharFieldtoQueryField[M](f: RField[Char, M]): QueryField[Char, M] = new QueryField(f)

  implicit def rbyteFieldtoNumericQueryField[M](f: RField[Byte, M]): NumericQueryField[Byte, M] = new NumericQueryField(f)
  implicit def rshortFieldtoNumericQueryField[M](f: RField[Short, M]): NumericQueryField[Short, M] = new NumericQueryField(f)
  implicit def rintFieldtoNumericQueryField[M](f: RField[Int, M]): NumericQueryField[Int, M] = new NumericQueryField(f)
  implicit def rlongFieldtoNumericQueryField[M](f: RField[Long, M]): NumericQueryField[Long, M] = new NumericQueryField(f)
  implicit def rfloatFieldtoNumericQueryField[M](f: RField[Float, M]): NumericQueryField[Float, M] = new NumericQueryField(f)
  implicit def rdoubleFieldtoNumericQueryField[M](f: RField[Double, M]): NumericQueryField[Double, M] = new NumericQueryField(f)

  implicit def rstringFieldToStringQueryField[M](f: RField[String, M]): StringQueryField[M] = new StringQueryField(f)
  implicit def robjectIdFieldToObjectIdQueryField[F <: ObjectId, M](f: RField[F, M]): ObjectIdQueryField[F, M] = new ObjectIdQueryField[F, M](f)
  implicit def rdateFieldToQueryField[M](f: RField[Date, M]): QueryField[Date, M] = new QueryField(f)
  implicit def rcalendarFieldToCalendarQueryField[M](f: RField[Calendar, M]): CalendarQueryField[M] = new CalendarQueryField(f)
  implicit def rdbobjectFieldToQueryField[M](f: RField[DBObject, M]): QueryField[DBObject, M] = new QueryField(f)

  implicit def renumNameFieldToEnumNameQueryField[M, F <: Enumeration#Value](f: RField[F, M]): EnumNameQueryField[M, F] = new EnumNameQueryField(f)
  implicit def renumerationListFieldToEnumerationListQueryField[M, F <: Enumeration#Value](f: RField[List[F], M]): EnumerationListQueryField[F, M] = new EnumerationListQueryField[F, M](f)
  implicit def rlatLongFieldToGeoQueryField[M](f: RField[LatLong, M]): GeoQueryField[M] = new GeoQueryField(f)
  implicit def rlistFieldToListQueryField[M, F](f: RField[List[F], M]): ListQueryField[F, M] = new ListQueryField[F, M](f)
  implicit def rseqFieldToSeqQueryField[M, F](f: RField[Seq[F], M]): SeqQueryField[F, M] = new SeqQueryField[F, M](f)
  implicit def rmapFieldToMapQueryField[M, F](f: RField[Map[String, F], M]): MapQueryField[F, M] = new MapQueryField[F, M](f)

  /** ModifyField implicits
    *
    * These are dangerous in the general case, unless the field type can be safely serialized
    * or the field class handles necessary serialization. We specialize some safe cases.
    **/
  implicit def booleanRFieldToModifyField[M](f: RField[Boolean, M]): ModifyField[Boolean, M] = new ModifyField(f)
  implicit def charRFieldToModifyField[M](f: RField[Char, M]): ModifyField[Char, M] = new ModifyField(f)

  implicit def byteRFieldToModifyField[M](f: RField[Byte, M]): NumericModifyField[Byte, M] = new NumericModifyField(f)
  implicit def shortRFieldToModifyField[M](f: RField[Short, M]): NumericModifyField[Short, M] = new NumericModifyField(f)
  implicit def intRFieldToModifyField[M](f: RField[Int, M]): NumericModifyField[Int, M] = new NumericModifyField(f)
  implicit def longRFieldToModifyField[M](f: RField[Long, M]): NumericModifyField[Long, M] = new NumericModifyField(f)
  implicit def floatRFieldToModifyField[M](f: RField[Float, M]): NumericModifyField[Float, M] = new NumericModifyField(f)
  implicit def doubleRFieldToModifyField[M](f: RField[Double, M]): NumericModifyField[Double, M] = new NumericModifyField(f)

  implicit def stringRFieldToModifyField[M](f: RField[String, M]): ModifyField[String, M] = new ModifyField(f)
  implicit def objectidRFieldToModifyField[M](f: RField[ObjectId, M]): ModifyField[ObjectId, M] = new ModifyField(f)
  implicit def dateRFieldToModifyField[M](f: RField[Date, M]): ModifyField[Date, M] = new ModifyField(f)

  implicit def rcalendarFieldToCalendarModifyField[M](f: RField[Calendar, M]): CalendarModifyField[M] =
    new CalendarModifyField(f)

  implicit def renumerationFieldToEnumerationModifyField[M, F <: Enumeration#Value]
      (f: RField[F, M]): EnumerationModifyField[M, F] =
    new EnumerationModifyField(f)

  implicit def renumerationListFieldToEnumerationListModifyField[M, F <: Enumeration#Value]
      (f: RField[List[F], M]): EnumerationListModifyField[F, M] =
    new EnumerationListModifyField[F, M](f)

  implicit def rlatLongFieldToGeoQueryModifyField[M](f: RField[LatLong, M]): GeoModifyField[M] =
    new GeoModifyField(f)

  implicit def rlistFieldToListModifyField[M, F](f: RField[List[F], M]): ListModifyField[F, M] =
    new ListModifyField[F, M](f)

  implicit def rSeqFieldToSeqModifyField[M, F](f: RField[Seq[F], M]): SeqModifyField[F, M] =
    new SeqModifyField[F, M](f)

  implicit def rmapFieldToMapModifyField[M, F](f: RField[Map[String, F], M]): MapModifyField[F, M] =
    new MapModifyField[F, M](f)

  // SelectField implicits
  implicit def roptionalFieldToSelectField[M, V](
      f: ROptionalField[V, M]
  ): SelectField[Option[V], M] = new OptionalSelectField(f)

  class Flattened[A, B]
  implicit def anyValIsFlattened[A <: AnyVal]: Flattened[A, A] = new Flattened[A, A]
  implicit def enumIsFlattened[A <: Enumeration#Value]: Flattened[A, A] = new Flattened[A, A]
  implicit val stringIsFlattened = new Flattened[String, String]
  implicit val objectIdIsFlattened = new Flattened[ObjectId, ObjectId]
  implicit val dateIsFlattened = new Flattened[java.util.Date, java.util.Date]
  implicit def recursiveFlattenList[A, B](implicit ev: Flattened[A, B]) = new Flattened[List[A], B]
  implicit def recursiveFlattenSeq[A, B](implicit ev: Flattened[A, B]) = new Flattened[Seq[A], B]
}

object Rogue extends Rogue
