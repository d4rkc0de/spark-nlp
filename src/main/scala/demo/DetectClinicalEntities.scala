package demo

import com.johnsnowlabs.nlp.DocumentAssembler
import com.johnsnowlabs.nlp.annotator.{SentenceDetectorDLModel, WordEmbeddingsModel}
import com.johnsnowlabs.nlp.annotators.Tokenizer
import com.johnsnowlabs.nlp.annotators.ner.NerConverter
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, explode}

object DetectClinicalEntities extends App {
  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("DetectClinicalEntitiesApp")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._



  val documentAssembler = new DocumentAssembler()
    .setInputCol("text")
    .setOutputCol("document")

  val sentenceDetector = SentenceDetectorDLModel.pretrained("sentence_detector_dl_healthcare", "en", "clinical/models")
    .setInputCols("document")
    .setOutputCol("sentence")

  val tokenizer = new Tokenizer()
    .setInputCols("sentence")
    .setOutputCol("token")

  val embeddings = WordEmbeddingsModel.pretrained("embeddings_clinical", "en", "clinical/models")
    .setInputCols(Array("sentence", "token"))
    .setOutputCol("embeddings")

//  val jsl_ner = MedicalNerModel.pretrained("ner_jsl_enriched", "en", "clinical/models")
//    .setInputCols(Array("sentence", "token", "embeddings"))
//    .setOutputCol("jsl_ner")

  val jsl_ner_converter = new NerConverter()
    .setInputCols(Array("sentence", "token", "jsl_ner"))
    .setOutputCol("ner_chunk")

  val jsl_ner_pipeline = new Pipeline().setStages(Array(
    documentAssembler,
    sentenceDetector,
    tokenizer,
    embeddings,
//    jsl_ner,
    jsl_ner_converter))


  val data = Seq("""The patient is a 21-day-old Caucasian male here for 2 days of congestion - mom has been suctioning yellow discharge from the patient's nares, plus she has noticed some mild problems with his breathing while feeding (but negative for any perioral cyanosis or retractions). One day ago, mom also noticed a tactile temperature and gave the patient Tylenol. Baby also has had some decreased p.o. intake. His normal breast-feeding is down from 20 minutes q.2h. to 5 to 10 minutes secondary to his respiratory congestion. He sleeps well, but has been more tired and has been fussy over the past 2 days. The parents noticed no improvement with albuterol treatments given in the ER. His urine output has also decreased; normally he has 8 to 10 wet and 5 dirty diapers per 24 hours, now he has down to 4 wet diapers per 24 hours. Mom denies any diarrhea. His bowel movements are yellow colored and soft in nature.""").toDS.toDF("text")
  val data2 = Seq(
    "My name is Abdennacer and I live in France, but originally from Zarzis, Tunisia",
    "The Santiago Bernabéu Stadium is a football stadium in Madrid, Spain"
  ).toDF("text")

  val result = jsl_ner_pipeline.fit(data).transform(data)
  val result2 = jsl_ner_pipeline.fit(data2).transform(data2)
  result.select(explode(col("ner_chunk")).as("ner_chunk"))
    .select("ner_chunk.result", "ner_chunk.metadata").show(false)
  result2.select(explode(col("ner_chunk")).as("ner_chunk"))
    .select("ner_chunk.result", "ner_chunk.metadata").show(false)
}
