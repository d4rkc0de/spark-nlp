package demo

import com.johnsnowlabs.nlp.DocumentAssembler
import com.johnsnowlabs.nlp.annotator.{NerDLModel, SentenceDetector, WordEmbeddingsModel}
import com.johnsnowlabs.nlp.annotators.Tokenizer
import com.johnsnowlabs.nlp.annotators.ner.NerConverter
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, explode}

object NerConverterTest extends App {

  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("NerConverterTestApp")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  val data = Seq(("My name is Abdennacer and I live in France, but originally from Zarzis, Tunisia"), ("The Santiago Bernabéu Stadium is a football stadium in Madrid, Spain")).toDF("text")

  val documentAssembler = new DocumentAssembler()
    .setInputCol("text")
    .setOutputCol("document")

  val sentenceDetector = new SentenceDetector()
    .setInputCols("document")
    .setOutputCol("sentence")
    .setUseAbbreviations(false)

  val tokenizer = new Tokenizer()
    .setInputCols(Array("sentence"))
    .setOutputCol("token")

  val embeddings = WordEmbeddingsModel.pretrained()
    .setInputCols("sentence", "token")
    .setOutputCol("embeddings")
    .setCaseSensitive(false)

  val ner = NerDLModel.pretrained()
    .setInputCols("sentence", "token", "embeddings")
    .setOutputCol("ner")
    .setIncludeConfidence(true)

  val converter = new NerConverter()
    .setInputCols("sentence", "token", "ner")
    .setOutputCol("entities")
    .setPreservePosition(false)

  val pipeline = new Pipeline()
    .setStages(Array(
      documentAssembler,
      sentenceDetector,
      tokenizer,
      embeddings,
      ner,
      converter
    ))

  val nermodel = pipeline.fit(data).transform(data)

  nermodel.printSchema()
  nermodel.select(explode(col("entities")).as("entities"))
    .select("entities.result", "entities.metadata").show(false)
}
