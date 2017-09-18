package ptit.nhunh.naivebayes;

import java.util.Arrays;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

public class RunnableExample1 {

    public static void main(String[] args) {

        final Classifier<String, String> bayes =
                new BayesClassifier<String, String>();

        final String[] positiveText = "tôi đồng ý".split("\\s");
        bayes.learn("positive", Arrays.asList(positiveText));

        final String[] negativeText = "thật là ngớ ngẩn".split("\\s");
        bayes.learn("negative", Arrays.asList(negativeText));

        final String[] unknownText1 = "không đồng ý".split("\\s");
        final String[] unknownText2 = "chính sách ngớ ngẩn".split("\\s");

        System.out.println( // will output "positive"
                bayes.classify(Arrays.asList(unknownText1)).getCategory());
        System.out.println( // will output "negative"
                bayes.classify(Arrays.asList(unknownText2)).getCategory());

    }

}
