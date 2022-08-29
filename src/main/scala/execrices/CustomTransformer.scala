package execrices

import com.johnsnowlabs.nlp.Annotation
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.types.StructType

class CustomTransformer(override val uid: String) extends Transformer with DefaultParamsWritable {

  def this() = this(Identifiable.randomUID("CustomTransformer"))

  def setInputCol(value: String): this.type = set(inputCol, value)

  def setOutputCol(value: String): this.type = set(outputCol, value)

  def getOutputCol: String = getOrDefault(outputCol)

  val inputCol = new Param[String](this, "inputCol", "input column")
  val outputCol = new Param[String](this, "outputCol", "output column")

  override def transform(dataset: Dataset[_]): DataFrame = {
    val outCol = extractParamMap.getOrElse(outputCol, "output")
    val inCol = extractParamMap.getOrElse(inputCol, "input")

    val poncts = Array(".", ",", "!")
    def ponctRemover(array: Array[Annotation]) : Array[Annotation] = {
      array
        .map(value => {
          val updatedResult = if (poncts.contains(value.result)) "" else value.result
          Annotation(value.annotatorType, value.begin, value.end, updatedResult, value.metadata, value.embeddings)
        })
    }
    dataset.withColumn(outCol, regexp_replace(col(inCol), """[\p{Punct}]""", ""))
  }

  override def copy(extra: ParamMap): CustomTransformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = schema
}

object CustomTransformer extends DefaultParamsReadable[CustomTransformer] {
  override def load(path: String): CustomTransformer = super.load(path)
}