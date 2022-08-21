package execrices

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.lit
import com.johnsnowlabs.nlp.annotator._
import com.johnsnowlabs.nlp.base._
import org.apache.spark.ml.Pipeline

object JokesDetector extends App {

  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("JokesApp")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  val trainDataset1 = spark.read.option("header", "true").csv("src/main/resources/shortjokes.csv")
    .withColumnRenamed("ID", "category")
    .withColumnRenamed("Joke", "description")
    .withColumn("category", lit("Jokes"))


  val trainDataset2 = spark.read.option("header", "true").csv("src/main/resources/news_category_train.csv")
    .withColumn("category", lit("News"))

  val trainDataset = trainDataset1.unionByName(trainDataset2)

  val documentAssembler = new DocumentAssembler()
    .setInputCol("description")
    .setOutputCol("document")

  val token = new Tokenizer()
    .setInputCols("document")
    .setOutputCol("token")

  val embeddings = WordEmbeddingsModel.pretrained("glove_100d", lang = "en")
    .setInputCols("document", "token")
    .setOutputCol("embeddings")
    .setCaseSensitive(false)

  //convert word embeddings to sentence embeddings
  val sentenceEmbeddings = new SentenceEmbeddings()
    .setInputCols("document", "embeddings")
    .setOutputCol("sentence_embeddings")
    .setStorageRef("glove_100d")

  //ClassifierDL accepts SENTENCE_EMBEDDINGS
  //UniversalSentenceEncoder or SentenceEmbeddings can produce SENTECE_EMBEDDINGS
  val docClassifier = new ClassifierDLApproach()
    .setInputCols("sentence_embeddings")
    .setOutputCol("class")
    .setLabelColumn("category")
    .setBatchSize(64)
    .setMaxEpochs(20)
    .setLr(5e-3f)
    .setDropout(0.5f)

  val pipeline = new Pipeline()
    .setStages(
      Array(
        documentAssembler,
        token,
        embeddings,
        sentenceEmbeddings,
        docClassifier
      )
    )

  // Let's train our multi-class classifier
  val pipelineModel = pipeline.fit(trainDataset)

  val testDataset = spark.createDataFrame(Seq(
    (0, "Unions representing workers at Turner   Newall say they are 'disappointed' after talks with stricken parent firm Federal Mogul."),
    (1, "Scientists have discovered irregular lumps beneath the icy surface of Jupiter's largest moon, Ganymede. These irregular masses may be rock formations, supported by Ganymede's icy shell for billions of years..."),
    (2, "John snow labs is a mysterious lab that train AI robots to transform them to rule over the whole world!"),
    (3, "I couldn't figure out why the baseball kept getting larger. Then it hit me.")
  )).toDF("id", "description")

  val prediction = pipelineModel.transform(testDataset)

  //actual predicted classes
  prediction.select("class.result").show(false)
  //metadata related to scores of all classes
  prediction.select("class.metadata").show(false)

}
