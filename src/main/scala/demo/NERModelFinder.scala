//package demo
//
//import com.johnsnowlabs.nlp.{DocumentAssembler, LightPipeline}
//import com.johnsnowlabs.nlp.embeddings.BertSentenceEmbeddings
//import org.apache.spark.ml.PipelineModel
//
//class NERModelFinder extends App {
//
//  val documentAssembler = new DocumentAssembler()
//    .setInputCol("text")
//    .setOutputCol("ner_chunk")
//
//  val sbert_embedder = BertSentenceEmbeddings
//    .pretrained("sbert_jsl_medium_uncased", "en", "clinical/models")
//    .setInputCols(Array("ner_chunk"))
//    .setOutputCol("sbert_embeddings")
//
//  val ner_model_finder = new SentenceEntityResolverModel()
//    .pretrained("sbertresolve_ner_model_finder", "en", "clinical/models")
//    .setInputCols(Array("ner_chunk", "sbert_embeddings"))
//    .setOutputCol("model_names")
//    .setDistanceFunction("EUCLIDEAN")
//
//
//  val ner_model_finder_pipelineModel = new PipelineModel().setStages(Array(documentAssembler, sbert_embedder, ner_model_finder))
//
//  val light_pipeline = new LightPipeline(ner_model_finder_pipelineModel)
//
//  val annotations = light_pipeline.fullAnnotate("medication")
//}
