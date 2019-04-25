import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.NominalToBinary;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.RemoveType;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.classifiers.lazy.*;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;

public class Management {

	String file;
	int percentSplit = 66;
	int cross_validation = 10;

	public Dataset DecisionTree(String filename) throws Exception {
		Dataset dataset = new Dataset();
		dataset.setSuccess(0.0);
		try {

			BufferedReader br = null;
			BufferedReader br2 = null;
			br = new BufferedReader(new FileReader("data/" + filename));

			Instances insts = new Instances(br);
			insts.setClassIndex(insts.numAttributes() - 1);
			br.close();

			ArffSaver arffSaverInstance = new ArffSaver();
			arffSaverInstance.setInstances(insts);
			arffSaverInstance.setFile(new File("data/x.arff"));
			arffSaverInstance.writeBatch();

			br2 = new BufferedReader(new FileReader("data/x.arff"));
			Instances insts2 = new Instances(br2);
			insts2.setClassIndex(insts2.numAttributes() - 1);
			br2.close();

			Classifier dt = new weka.classifiers.trees.J48();

			Filter rplmiss = new ReplaceMissingValues();
			rplmiss.setInputFormat(insts);
			insts = Filter.useFilter(insts, rplmiss);

			Filter numerictoNominal = new Discretize();

			numerictoNominal.setInputFormat(insts);
			insts = Filter.useFilter(insts, numerictoNominal);

			Filter myRandomize = new Randomize();
			myRandomize.setInputFormat(insts);
			insts = weka.filters.Filter.useFilter(insts, myRandomize);

			int trainSize = insts.numInstances() * percentSplit / 100;
			int testSize = insts.numInstances() - trainSize;
			Instances train = new Instances(insts, 0, trainSize);

			dt.buildClassifier(train);

			dataset.setData(insts); // işlenmiş veri
			dataset.setData2(insts2); // orijinal veri
			int numCorrect = 0;

			for (int i = trainSize; i < insts.numInstances(); i++) {
				weka.core.Instance currentInst = insts.instance(i);
				double predictedClass = dt.classifyInstance(currentInst);
				if (predictedClass == insts.instance(i).classValue()) {
					numCorrect++;
				}
			}
			dataset.setSuccess((double) ((double) numCorrect / (double) testSize * 100.0));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
	}

	public Dataset NaiveBayes(String filename) throws Exception {
		Dataset dataset = new Dataset();

		dataset.setSuccess(0.0);

		try {
			BufferedReader br1 = null;
			BufferedReader br2 = null;
			br1 = new BufferedReader(new FileReader("data/" + filename));

			Instances insts = new Instances(br1);
			insts.setClassIndex(insts.numAttributes() - 1);

			br1.close();

			ArffSaver arffSaverInstance = new ArffSaver();
			arffSaverInstance.setInstances(insts);
			arffSaverInstance.setFile(new File("data/x.arff"));
			arffSaverInstance.writeBatch();

			br2 = new BufferedReader(new FileReader("data/x.arff"));
			Instances insts2 = new Instances(br2);
			insts2.setClassIndex(insts2.numAttributes() - 1);
			br2.close();

			Classifier nb = new NaiveBayes();
			Filter rplmiss = new ReplaceMissingValues();
			rplmiss.setInputFormat(insts);
			insts = Filter.useFilter(insts, rplmiss);

			Filter useless = new RemoveType();
			useless.setInputFormat(insts);
			insts = Filter.useFilter(insts, useless);

			Filter myDiscretize = new Discretize();
			myDiscretize.setInputFormat(insts);
			insts = weka.filters.Filter.useFilter(insts, myDiscretize);

			Evaluation eval = new Evaluation(insts);
			eval.crossValidateModel(nb, insts, cross_validation, new Random(1));
			nb.buildClassifier(insts);

			dataset.setData(insts); // işlenmiş veri
			dataset.setData2(insts2); // orijinal veri

			dataset.setSuccess(eval.correct() / eval.numInstances() * 100.0);

		}

		catch (Exception e) {
			System.err.println("The File was not found!");
		}

		return dataset;

	}

	public Dataset K_NearestNeighbour(String filename) throws Exception {
		Dataset dataset = new Dataset();
		dataset.setSuccess(0.0);

		try {
			BufferedReader br = null;
			BufferedReader br2 = null;
			br = new BufferedReader(new FileReader("data/" + filename));

			Instances insts = new Instances(br);
			insts.setClassIndex(insts.numAttributes() - 1);

			br.close();

			ArffSaver arffSaverInstance = new ArffSaver();
			arffSaverInstance.setInstances(insts);
			arffSaverInstance.setFile(new File("data/x.arff"));
			arffSaverInstance.writeBatch();

			br2 = new BufferedReader(new FileReader("data/x.arff"));
			Instances insts2 = new Instances(br2);
			insts2.setClassIndex(insts2.numAttributes() - 1);
			br2.close();

			Classifier ibk = new IBk();

			Filter rplmiss = new ReplaceMissingValues();
			rplmiss.setInputFormat(insts);
			insts = Filter.useFilter(insts, rplmiss);

			Filter useless = new RemoveType();
			useless.setInputFormat(insts);
			insts = Filter.useFilter(insts, useless);

			Filter NominalToBinary = new NominalToBinary();
			NominalToBinary.setInputFormat(insts);
			insts = Filter.useFilter(insts, NominalToBinary);

			Filter Normalize = new Normalize();
			Normalize.setInputFormat(insts);
			insts = Filter.useFilter(insts, Normalize);

			Evaluation eval = new Evaluation(insts);
			eval.crossValidateModel(ibk, insts, cross_validation, new Random(1));
			ibk.buildClassifier(insts);
			dataset.setData(insts); // işlenmiş veri
			dataset.setData2(insts2); // orijinal veri

			dataset.setSuccess(eval.correct() / eval.numInstances() * 100.0);
		}

		catch (Exception e) {
			System.err.println("The File was not found!");
		}
		return dataset;

	}

	public Dataset ArtificialNeuralNetwork(String filename) throws Exception {
		Dataset dataset = new Dataset();
		dataset.setSuccess(0.0);

		try {
			BufferedReader br = null;
			BufferedReader br2 = null;
			br = new BufferedReader(new FileReader("data/" + filename));

			Instances insts = new Instances(br);
			insts.setClassIndex(insts.numAttributes() - 1);

			br.close();

			ArffSaver arffSaverInstance = new ArffSaver();
			arffSaverInstance.setInstances(insts);
			arffSaverInstance.setFile(new File("data/x.arff"));
			arffSaverInstance.writeBatch();

			br2 = new BufferedReader(new FileReader("data/x.arff"));
			Instances insts2 = new Instances(br2);
			insts2.setClassIndex(insts2.numAttributes() - 1);
			br2.close();

			Classifier mp = new MultilayerPerceptron();

			Filter rplmiss = new ReplaceMissingValues();
			rplmiss.setInputFormat(insts);
			insts = Filter.useFilter(insts, rplmiss);

			Filter useless = new RemoveType();
			useless.setInputFormat(insts);
			insts = Filter.useFilter(insts, useless);

			Filter NominalToBinary = new NominalToBinary();
			NominalToBinary.setInputFormat(insts);
			insts = Filter.useFilter(insts, NominalToBinary);

			Filter Normalize = new Normalize();
			Normalize.setInputFormat(insts);
			insts = Filter.useFilter(insts, Normalize);

			Evaluation eval = new Evaluation(insts);
			eval.crossValidateModel(mp, insts, cross_validation, new Random(1));
			mp.buildClassifier(insts);
			dataset.setData(insts); // işlenmiş veri
			dataset.setData2(insts2); // orijinal veri
			dataset.setSuccess(eval.correct() / eval.numInstances() * 100.0);
		}

		catch (Exception e) {
			System.err.println("The File was not found!");
		}

		return dataset;
	}

	public Dataset SupportVectorMachine(String filename) throws Exception {
		Dataset dataset = new Dataset();
		dataset.setSuccess(0.0);

		try {
			BufferedReader br = null;
			BufferedReader br2 = null;
			br = new BufferedReader(new FileReader("data/" + filename));

			Instances insts = new Instances(br);
			insts.setClassIndex(insts.numAttributes() - 1);

			br.close();

			ArffSaver arffSaverInstance = new ArffSaver();
			arffSaverInstance.setInstances(insts);
			arffSaverInstance.setFile(new File("data/x.arff"));
			arffSaverInstance.writeBatch();

			br2 = new BufferedReader(new FileReader("data/x.arff"));
			Instances insts2 = new Instances(br2);
			insts2.setClassIndex(insts2.numAttributes() - 1);
			br2.close();

			Classifier svm = new SMO();

			Filter rplmiss = new ReplaceMissingValues();
			rplmiss.setInputFormat(insts);
			insts = Filter.useFilter(insts, rplmiss);

			Filter useless = new RemoveType();
			useless.setInputFormat(insts);
			insts = Filter.useFilter(insts, useless);

			Filter NumerictoNominal = new Discretize();
			NumerictoNominal.setInputFormat(insts);
			insts = Filter.useFilter(insts, NumerictoNominal);

			Evaluation eval = new Evaluation(insts);
			eval.crossValidateModel(svm, insts, cross_validation, new Random(1));
			svm.buildClassifier(insts);
			dataset.setData(insts); // işlenmiş veri
			dataset.setData2(insts2); // orijinal veri

			dataset.setSuccess(eval.correct() / eval.numInstances() * 100.0);
		}

		catch (Exception e) {

			System.err.println("The File was not found!");
		}
		return dataset;

	}

}
