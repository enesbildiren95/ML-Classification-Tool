import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class CME4432 {

	private JFrame frmSadas;
	private JTextField textField; // input dosyasının yazıdırıldığı textField
	int cross_validation = 10; // Evaluation metriclerini kullanırken kullandığım cross validation değeri
	List<JPanel> listOfPanel = new ArrayList<JPanel>();
	private JPanel panel_1; // kullanıcı inputlarının gireceği panel
	private JLabel lbltextField, lblcomboBox; // combobox ve textField'lerin her biri için birer label
	Dataset Naive_Bayes, KNN, Decision_Tree, Support_Vector, Artficial_Neural; // Classification algoritmaları için
																				// Dataset objesi
	private JTextField textField_new; // numeric attribute'ler için birer textField
	private JComboBox drop_down; // nominal attribute'ler için birer combobox
	ArrayList<Attribute> list = new ArrayList<Attribute>(); // attribute isimlerinin tutulduğu ArrayList
	ArrayList<JTextField> list2 = new ArrayList<JTextField>(); // numeric attribute'lerin tutulduğu ArrayList
	ArrayList<JComboBox> list3 = new ArrayList<JComboBox>(); // nominal attribute'lerin tutulduğu ArrayList
	private int check = 0; // 5 değer alıyor. Classification algoritmaların kararı için
	Instance new_inst; // kullanıcının girdiği değerlerden yeni bir instance yaratmak için kullanılıyor
	private JLabel lblout; // her bir algoritma için successlerin ekrana yazdırıldığı label
	String classname = null; // Son işlemden sonra ekrana yazdırılan target attribute
	private boolean flag = false; // Success değeri max ve aynı olan classification algoritmalarından yalnız
									// birine girmesini sağlayan değişken

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CME4432 window = new CME4432();
					window.frmSadas.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CME4432() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSadas = new JFrame();
		frmSadas.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmSadas.setFont(new Font("Dialog", Font.BOLD, 12));
		frmSadas.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 11));
		frmSadas.getContentPane().setBackground(Color.WHITE);

		frmSadas.setBackground(Color.WHITE);
		frmSadas.setTitle("DEUCENG - ML Classification Tool");
		frmSadas.setBounds(100, 100, 850, 617);
		frmSadas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Management m = new Management();

		JButton btnNewButton = new JButton("BROWSE");
		btnNewButton.setBounds(522, 22, 89, 23);
		JButton btnNewButton_1 = new JButton("DISCOVER");
		JLabel lblResult = new JLabel("");
		btnNewButton_1.setVisible(false);
		lblResult.setVisible(false);

		btnNewButton.addActionListener(new ActionListener() { // BROWSE butonuna basıldığında yapılacak işlemler
			public void actionPerformed(ActionEvent arg0) {

				flag = false;
				list.clear();
				list2.clear();
				list3.clear();
				panel_1.removeAll();
				SwingUtilities.updateComponentTreeUI(frmSadas);
				lblResult.setText(null);
				lblout.setText(null);
				textField.setText(null);
				btnNewButton_1.setVisible(false);
				JFileChooser chooser = new JFileChooser();

				chooser.setCurrentDirectory(new File("data")); // input dosyasının arandığı ilk konum
				chooser.setFileFilter(new FileNameExtensionFilter("arff file", "arff"));
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				String filename = f.getName();
				textField.setText(filename);
				lblout.setVisible(true);
				try {
					Decision_Tree = m.DecisionTree(filename);
					Naive_Bayes = m.NaiveBayes(filename);
					KNN = m.K_NearestNeighbour(filename);
					Artficial_Neural = m.ArtificialNeuralNetwork(filename);
					Support_Vector = m.SupportVectorMachine(filename);
					double max1 = Math.max(Decision_Tree.getSuccess(), Naive_Bayes.getSuccess());
					double max2 = Math.max(KNN.getSuccess(), Artficial_Neural.getSuccess());
					double max3 = Math.max(max1, max2);
					double max4 = Math.max(max3, Support_Vector.getSuccess());

					// Success'i en fazla olan değeri bulmak
					// maximum değere göre kullanılacak algortmanın belirlenmesi için if
					// statement'lar

					if (max4 == Decision_Tree.getSuccess() && flag == false) {
						check = 1;
						DecimalFormat df = new DecimalFormat("#.##"); //
						String dx = df.format(Decision_Tree.getSuccess()); // virgülden sonra iki basamak olsun
						Decision_Tree.setSuccess(Double.valueOf(dx)); //
						lblout.setText("Decision Tree is the most successful algorithm for this data set (%"
								+ Decision_Tree.getSuccess() + ")");

						JPanel panel[] = new JPanel[Decision_Tree.getData2().numAttributes()];

						new_inst = new DenseInstance(Decision_Tree.getData2().numAttributes()); // yaratılan yeni
																								// instance

						for (int j = 0; j < Decision_Tree.getData2().numAttributes() - 1; j++) {

							panel[j] = new JPanel();

							list.add(Decision_Tree.getData2().attribute(j));
							if (Decision_Tree.getData2().attribute(j).isNumeric() == true) {

								textField_new = new JTextField();
								textField_new.setBounds(128, 10, 98, 20);
								lbltextField = new JLabel("");
								lbltextField.setBounds(40, 11, 88, 14);

								panel[j].add(textField_new);
								// Bir büyük panel var. Bu panelin üstünde sayısı datasetlere göre değişen
								// attribute'lar var. Bu attribute'ların her biri bir panel. Bu küçük paneller
								// label ve textField(numeric attributes)-combobox(nominal attributes)'lardan
								// oluşuyor.
								list2.add(textField_new);
								panel[j].add(lbltextField);
								lbltextField.setText(Decision_Tree.getData2().attribute(j).name());
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);
							}
							if (Decision_Tree.getData2().attribute(j).isNominal() == true) {

								String[] content = new String[Decision_Tree.getData2().attribute(j).numValues()];
								for (int i = 0; i < content.length; i++) {
									content[i] = Decision_Tree.getData2().attribute(j).value(i);
								}

								drop_down = new JComboBox(content);
								drop_down.setBounds(128, 10, 98, 20);
								lblcomboBox = new JLabel("");
								lblcomboBox.setBounds(40, 11, 88, 14);
								panel[j].add(lblcomboBox);
								list3.add(drop_down);
								lblcomboBox.setText(Decision_Tree.getData2().attribute(j).name());
								panel[j].add(drop_down);
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);

							}

						}
						flag = true;
					}
					if (max4 == Naive_Bayes.getSuccess() && flag == false) {
						check = 2;
						DecimalFormat df = new DecimalFormat("#.##");
						String dx = df.format(Naive_Bayes.getSuccess());
						Naive_Bayes.setSuccess(Double.valueOf(dx));
						lblout.setText("Naive Bayes is the most successful algorithm for this data set (%"
								+ Naive_Bayes.getSuccess() + ")");

						JPanel panel[] = new JPanel[Naive_Bayes.getData2().numAttributes()];

						new_inst = new DenseInstance(Naive_Bayes.getData2().numAttributes());

						for (int j = 0; j < Naive_Bayes.getData2().numAttributes() - 1; j++) {

							panel[j] = new JPanel();
							list.add(Naive_Bayes.getData2().attribute(j));
							if (Naive_Bayes.getData2().attribute(j).isNumeric() == true) {

								textField_new = new JTextField();
								textField_new.setBounds(128, 10, 98, 20);
								lbltextField = new JLabel("");
								lbltextField.setBounds(40, 11, 88, 14);

								panel[j].add(textField_new);

								list2.add(textField_new);
								panel[j].add(lbltextField);
								lbltextField.setText(Naive_Bayes.getData2().attribute(j).name());
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);

							}
							if (Naive_Bayes.getData2().attribute(j).isNominal() == true) {

								String[] content = new String[Naive_Bayes.getData2().attribute(j).numValues()];
								for (int i = 0; i < content.length; i++) {
									content[i] = Naive_Bayes.getData2().attribute(j).value(i);
								}
								drop_down = new JComboBox(content);
								drop_down.setBounds(128, 10, 98, 20);
								lblcomboBox = new JLabel("");
								lblcomboBox.setBounds(40, 11, 88, 14);
								panel[j].add(lblcomboBox);
								list3.add(drop_down);
								lblcomboBox.setText(Naive_Bayes.getData2().attribute(j).name());
								panel[j].add(drop_down);
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);

							}

						}

						flag = true;

					}
					if (max4 == KNN.getSuccess() && flag == false) {
						check = 3;
						DecimalFormat df = new DecimalFormat("#.##");
						String dx = df.format(KNN.getSuccess());
						KNN.setSuccess(Double.valueOf(dx));
						lblout.setText("k-Nearest Neighbor is the most successful algorithm for this data set (%"
								+ KNN.getSuccess() + ")");

						JPanel panel[] = new JPanel[KNN.getData2().numAttributes()];

						new_inst = new DenseInstance(KNN.getData2().numAttributes());

						for (int j = 0; j < KNN.getData2().numAttributes() - 1; j++) {

							panel[j] = new JPanel();
							list.add(KNN.getData2().attribute(j));
							if (KNN.getData2().attribute(j).isNumeric() == true) {

								textField_new = new JTextField();
								textField_new.setBounds(128, 10, 98, 20);
								lbltextField = new JLabel("");
								lbltextField.setBounds(40, 11, 88, 14);

								panel[j].add(textField_new);

								list2.add(textField_new);
								panel[j].add(lbltextField);
								lbltextField.setText(KNN.getData2().attribute(j).name());
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);
							}
							if (KNN.getData2().attribute(j).isNominal() == true) {

								String[] content = new String[KNN.getData2().attribute(j).numValues()];
								for (int i = 0; i < content.length; i++) {
									content[i] = KNN.getData2().attribute(j).value(i);
								}
								drop_down = new JComboBox(content);
								drop_down.setBounds(128, 10, 98, 20);
								lblcomboBox = new JLabel("");
								lblcomboBox.setBounds(40, 11, 88, 14);
								panel[j].add(lblcomboBox);
								list3.add(drop_down);
								lblcomboBox.setText(KNN.getData2().attribute(j).name());
								panel[j].add(drop_down);
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);

							}

						}
						flag = true;
					}
					if (max4 == Artficial_Neural.getSuccess() && flag == false) {
						check = 4;
						DecimalFormat df = new DecimalFormat("#.##");
						String dx = df.format(Artficial_Neural.getSuccess());
						Artficial_Neural.setSuccess(Double.valueOf(dx));
						lblout.setText("Artificial Neural Network is the most successful algorithm for this data set (%"
								+ Artficial_Neural.getSuccess() + ")");

						JPanel panel[] = new JPanel[Artficial_Neural.getData2().numAttributes()];

						new_inst = new DenseInstance(Artficial_Neural.getData2().numAttributes());

						for (int j = 0; j < Artficial_Neural.getData2().numAttributes() - 1; j++) {

							panel[j] = new JPanel();
							list.add(Artficial_Neural.getData2().attribute(j));
							if (Artficial_Neural.getData2().attribute(j).isNumeric() == true) {

								textField_new = new JTextField();
								textField_new.setBounds(128, 10, 98, 20);
								lbltextField = new JLabel("");
								lbltextField.setBounds(40, 11, 88, 14);

								panel[j].add(textField_new);

								list2.add(textField_new);
								panel[j].add(lbltextField);
								lbltextField.setText(Artficial_Neural.getData2().attribute(j).name());
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);
							}
							if (Artficial_Neural.getData2().attribute(j).isNominal() == true) {

								String[] content = new String[Artficial_Neural.getData2().attribute(j).numValues()];
								for (int i = 0; i < content.length; i++) {
									content[i] = Artficial_Neural.getData2().attribute(j).value(i);
								}
								drop_down = new JComboBox(content);
								drop_down.setBounds(128, 10, 98, 20);
								lblcomboBox = new JLabel("");
								lblcomboBox.setBounds(40, 11, 88, 14);
								panel[j].add(lblcomboBox);
								list3.add(drop_down);
								lblcomboBox.setText(Artficial_Neural.getData2().attribute(j).name());
								panel[j].add(drop_down);
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);

							}

						}
						flag = true;
					}
					if (max4 == Support_Vector.getSuccess() && flag == false) {
						check = 5;
						DecimalFormat df = new DecimalFormat("#.##");
						String dx = df.format(Support_Vector.getSuccess());
						Support_Vector.setSuccess(Double.valueOf(dx));
						lblout.setText("Support Vector Machine is the most successful algorithm for this data set (%"
								+ Support_Vector.getSuccess() + ")");

						JPanel panel[] = new JPanel[Support_Vector.getData2().numAttributes()];

						new_inst = new DenseInstance(Support_Vector.getData2().numAttributes());

						for (int j = 0; j < Support_Vector.getData2().numAttributes() - 1; j++) {

							panel[j] = new JPanel();
							list.add(Support_Vector.getData2().attribute(j));
							if (Support_Vector.getData2().attribute(j).isNumeric() == true) {

								textField_new = new JTextField();
								textField_new.setBounds(128, 10, 98, 20);
								lbltextField = new JLabel("");
								lbltextField.setBounds(10, 11, 88, 14);

								panel[j].add(textField_new);

								list2.add(textField_new);
								panel[j].add(lbltextField);
								lbltextField.setText(Support_Vector.getData2().attribute(j).name());
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);
							}
							if (Support_Vector.getData2().attribute(j).isNominal() == true) {

								String[] content = new String[Support_Vector.getData2().attribute(j).numValues()];
								for (int i = 0; i < content.length; i++) {
									content[i] = Support_Vector.getData2().attribute(j).value(i);
								}
								drop_down = new JComboBox(content);

								drop_down.setBounds(128, 10, 98, 20);
								lblcomboBox = new JLabel("");
								lblcomboBox.setBounds(10, 11, 88, 14);
								panel[j].add(lblcomboBox);
								list3.add(drop_down);
								lblcomboBox.setText(Support_Vector.getData2().attribute(j).name());
								panel[j].add(drop_down);
								panel[j].setLayout(null);
								panel[j].setBackground(new Color(255, 218, 185));
								panel_1.add(panel[j]);

							}

						}
						flag = true;
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				panel_1.setVisible(true);
				btnNewButton_1.setVisible(true);
				lblResult.setVisible(true);
			}
		});

		frmSadas.getContentPane().setLayout(null);
		frmSadas.getContentPane().add(btnNewButton);
		frmSadas.getContentPane().setPreferredSize(new Dimension(1366, 768));
		frmSadas.pack();

		textField = new JTextField();
		textField.setBounds(34, 23, 478, 20);
		frmSadas.getContentPane().add(textField);
		textField.setColumns(10);

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 218, 185));
		panel_1.setBounds(10, 95, 1346, 506);
		frmSadas.getContentPane().add(panel_1);
		panel_1.setLayout(new GridLayout(8, 1, 0, 0));
		panel_1.setVisible(false);

		btnNewButton_1.setBounds(34, 633, 121, 38);
		frmSadas.getContentPane().add(btnNewButton_1);

		lblResult.setBounds(200, 633, 283, 38);
		frmSadas.getContentPane().add(lblResult);
		lblResult.setBackground(new Color(255, 218, 185));
		lblResult.setFont(new Font("Dialog", Font.BOLD, 15));

		lblout = new JLabel("");
		lblout.setBounds(34, 54, 762, 30);
		frmSadas.getContentPane().add(lblout);
		lblout.setVisible(false);
		lblout.setFont(new Font("Dialog", Font.BOLD, 15));

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Classifier nb = new weka.classifiers.bayes.NaiveBayes();
				Classifier j48 = new J48();
				Classifier knn = new IBk(); // DISCOVER butonuna basıldığında tekrar algoritma objeleri oluşturuluyor
				Classifier nn = new MultilayerPerceptron();
				Classifier svm = new SMO();
				int k = 0;
				int l = 0;
				for (int i = 0; i < list.size(); i++) {

					if (list.get(i).isNumeric() == true) {
						new_inst.setValue(list.get(i), Double.valueOf(list2.get(k).getText())); // yeni instance
																								// attribute tiplerine
																								// göre yaratılıyor.
						k++; //
					} //
					else if (list.get(i).isNominal() == true) { //
						new_inst.setValue(list.get(i), list3.get(l).getSelectedItem().toString()); //
						l++;
					}

				}
				if (check == 1) {
					try {

						Decision_Tree.getData2().add(new_inst);

						Evaluation eval = new Evaluation(Decision_Tree.getData2());
						eval.crossValidateModel(j48, Decision_Tree.getData2(), cross_validation, new Random(1));
						j48.buildClassifier(Decision_Tree.getData2());

						for (int j = 0; j < Decision_Tree.getData2().numInstances(); j++) { // target attribute bulma
																							// işlemi. Yeni yaratılan
																							// instance en alta gidiyor
																							// ve classname sürekli
																							// güncelleniyor.
							double index = j48.classifyInstance(Decision_Tree.getData2().instance(j));
							classname = Decision_Tree.getData2().attribute(Decision_Tree.getData2().numAttributes() - 1)
									.value((int) index);

						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (check == 2) {
					try {

						Naive_Bayes.getData2().add(new_inst);

						Evaluation eval = new Evaluation(Naive_Bayes.getData2());
						eval.crossValidateModel(nb, Naive_Bayes.getData2(), cross_validation, new Random(1));
						nb.buildClassifier(Naive_Bayes.getData2());

						for (int j = 0; j < Naive_Bayes.getData2().numInstances(); j++) {

							double index = nb.classifyInstance(Naive_Bayes.getData2().instance(j));
							classname = Naive_Bayes.getData2().attribute(Naive_Bayes.getData2().numAttributes() - 1)
									.value((int) index);

						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (check == 3) {
					try {

						KNN.getData2().add(new_inst);

						Evaluation eval = new Evaluation(KNN.getData2());
						eval.crossValidateModel(knn, KNN.getData2(), cross_validation, new Random(1));
						knn.buildClassifier(KNN.getData2());

						for (int j = 0; j < KNN.getData2().numInstances(); j++) {

							double index = knn.classifyInstance(KNN.getData2().instance(j));
							classname = KNN.getData2().attribute(KNN.getData2().numAttributes() - 1).value((int) index);

						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (check == 4) {
					try {

						Artficial_Neural.getData2().add(new_inst);

						Evaluation eval = new Evaluation(Artficial_Neural.getData2());
						eval.crossValidateModel(nn, Artficial_Neural.getData2(), cross_validation, new Random(1));
						nn.buildClassifier(Artficial_Neural.getData2());

						for (int j = 0; j < Artficial_Neural.getData2().numInstances(); j++) {

							double index = nn.classifyInstance(Artficial_Neural.getData2().instance(j));
							classname = Artficial_Neural.getData2()
									.attribute(Artficial_Neural.getData2().numAttributes() - 1).value((int) index);

						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (check == 5) {
					try {

						Support_Vector.getData2().add(new_inst);

						Evaluation eval = new Evaluation(Support_Vector.getData2());
						eval.crossValidateModel(svm, Support_Vector.getData2(), cross_validation, new Random(1));
						svm.buildClassifier(Support_Vector.getData2());

						for (int j = 0; j < Support_Vector.getData2().numInstances(); j++) {

							double index = svm.classifyInstance(Support_Vector.getData2().instance(j));
							classname = Support_Vector.getData2()
									.attribute(Support_Vector.getData2().numAttributes() - 1).value((int) index);

						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				lblResult.setText("RESULT : " + classname); // target attribute yazdırma işlemi
			}
		});
	}

}
