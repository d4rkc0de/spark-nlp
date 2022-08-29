package execrices

import com.johnsnowlabs.nlp.{Annotation, AnnotatorModel, HasSimpleAnnotate}
import org.apache.spark.ml.util.Identifiable
import com.johnsnowlabs.nlp.AnnotatorType.DOCUMENT

class DocumentCleaner(override val uid: String) extends AnnotatorModel[DocumentCleaner]
  with HasSimpleAnnotate[DocumentCleaner] {

  def this() = this(Identifiable.randomUID("REGEX_TOKENIZER"))

  override val outputAnnotatorType: AnnotatorType = DOCUMENT

  override val inputAnnotatorTypes: Array[AnnotatorType] = Array(DOCUMENT)

  override def annotate(annotations: Seq[Annotation]): Seq[Annotation] = {
    annotations.map(annotation => {
      Annotation(
        annotation.annotatorType,
        annotation.begin,
        annotation.end,
        annotation.result.replaceAll("""[\p{Punct}]""", ""),
        annotation.metadata,
        annotation.embeddings
      )
    })
  }


}
