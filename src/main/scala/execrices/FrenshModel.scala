package execrices

import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, explode}

object FrenshModel extends App {
  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("JokesApp")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  val pipeline = new PretrainedPipeline("explain_document_md", "fr")
  // pipeline_asr_exp_w2v2t_vp_s438

  val data = Seq("Bonjour de John Snow Labs !", "Le géant français a conquis l'Amérique",
  "Rien ne va plus pour la patronne de Shauna Events", "Un coup de sang qui fait beaucoup de bruit en NBA").toDF("text")

  val prediction = pipeline.transform(data)
  prediction.show(false)
  prediction.select(explode(col("ner"))).show(200,false)
  prediction.select(explode(col("entities"))).show(false)
}
