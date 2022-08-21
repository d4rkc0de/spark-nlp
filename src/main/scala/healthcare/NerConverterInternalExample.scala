//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//      converter
//      documentAssembler,
//      embeddings,
//      ner,
//      sentenceDetector,
//      tokenizer,
//    ))
//    .appName("test")
//    .builder()
//    .config("spark.driver.memory", "6G")
//    .config("spark.kryoserializer.buffer.max", "1G")
//    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//    .getOrCreate()
//    .master("local[*]")
//    .setCaseSensitive(false)
//    .setIncludeConfidence(true)
//    .setInputCol("text")
//    .setInputCols("document")
//    .setInputCols("document", "token")
//    .setInputCols("sentence", "token", "embeddings")
//    .setInputCols("sentence", "token", "ner")
//    .setInputCols(Array("sentence"))
//    .setOutputCol("document")
//    .setOutputCol("embeddings")
//    .setOutputCol("entities")
//    .setOutputCol("ner")
//    .setOutputCol("sentence")
//    .setOutputCol("token")
//    .setPreservePosition(false)
//    .setStages(Array(
//    .setThreshold(9900e-4f)
//    .setUseAbbreviations(false)
//  implicit val session = spark
//  import spark.implicits._
//  nermodel.select("embeddings.result").show(1, false)
//  nermodel.select("entities").show(1, false)
//  nermodel.select("entities.result").show(1, false)
//  nermodel.select("token.result").show(1, false)
//  spark.stop
//  val converter = new NerConverterInternal()
//  val data = ResourceHelper.spark.createDataFrame(Seq(Tuple1("My name is Andres and I live in Colombia"))).toDF("text")
//  val documentAssembler = new DocumentAssembler()
//  val embeddings = WordEmbeddingsModel.pretrained()
//  val ner = NerDLModel.pretrained()
//  val nermodel = pipeline.fit(data).transform(data)
//  val pipeline = new Pipeline()
//  val sentenceDetector = new SentenceDetector()
//  val spark: SparkSession = SparkSession
//  val tokenizer = new Tokenizer()
//import AssertionDLApproachExample.spark
//import com.johnsnowlabs.nlp.DocumentAssembler
//import com.johnsnowlabs.nlp.annotator.{NerDLModel, SentenceDetector, WordEmbeddingsModel}
//import com.johnsnowlabs.nlp.annotators.Tokenizer
//import com.johnsnowlabs.nlp.annotators.ner.NerConverterInternal
//import com.johnsnowlabs.nlp.util.io.ResourceHelper
//import org.apache.spark.ml.Pipeline
//import org.apache.spark.sql.SparkSession
//object NerConverterInternalExample extends App {
//package healthcare
//}
