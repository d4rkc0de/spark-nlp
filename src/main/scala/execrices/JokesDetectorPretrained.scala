package execrices

import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession

object JokesDetectorPretrained extends App {

  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("JokesApp")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  val pipelineModel: PipelineModel = PipelineModel.load("src/main/resources/models/jokes")

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
