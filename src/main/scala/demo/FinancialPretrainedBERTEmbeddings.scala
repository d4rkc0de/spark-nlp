package demo

import com.johnsnowlabs.nlp.DocumentAssembler
import com.johnsnowlabs.nlp.annotators.Tokenizer
import com.johnsnowlabs.nlp.embeddings.BertEmbeddings
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.SparkSession

object FinancialPretrainedBERTEmbeddings extends App {

  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("JokesApp")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._
  val documentAssembler = new DocumentAssembler()
    .setInputCol("text")
    .setOutputCol("document")

  val tokenizer = new Tokenizer()
    .setInputCols(Array("document"))
    .setOutputCol("token")

  val embeddings = BertEmbeddings.pretrained("bert_embeddings_sec_bert_base","en")
    .setInputCols(Array("document", "token"))
    .setOutputCol("embeddings")

  val pipeline = new Pipeline().setStages(Array(documentAssembler, tokenizer, embeddings))

  val data = Seq("I love Spark NLP").toDF("text")

  val result = pipeline.fit(data).transform(data)

  result.printSchema()
  result.select("text", "embeddings.result", "embeddings.metadata").show(false)
}
