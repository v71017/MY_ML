
import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.text.DecimalFormat;

import static java.lang.Math.round;

/**
 * Created by Thpffcj on 2017/8/1.
 */
public class LinearRegressions {

    private static AbstractClassifier classifier;

    private AbstractClassifier trainModel(String arffFile, int classIndex) throws Exception {

        File inputFile = new File(arffFile); //训练文件
        System.out.println("1");
        ArffLoader loader = new ArffLoader();
        System.out.println("2");
        loader.setFile(inputFile);
        Instances insTrain = loader.getDataSet(); // 读入训练文件
        insTrain.setClassIndex(classIndex);
        System.out.println("3");

        weka.classifiers.functions.LinearRegression linear = new weka.classifiers.functions.LinearRegression();
        linear.buildClassifier(insTrain);//根据训练数据构造分类器

        return linear;
    }

    public double getScore(String vector) throws Exception {

        Instance ins = new weka.core.SparseInstance(200);

        //TODO
        String[] s = vector.split(",");
        for(int i=0; i<200; i++) {
            ins.setValue(i, Double.valueOf(s[i]));
        }

        DecimalFormat df = new DecimalFormat("#.0");
        double star = Double.valueOf(df.format(classifier.classifyInstance(ins) - Math.random()*3));
        if(star > 10) {
            return 10;
        }
        if (star < 0) {
            return 0;
        }
        return star;
    }

    public void train() throws Exception {

//        String path = LinearRegressions.class.getResource("/").getPath();
//        System.out.println(path);
        final String arffTrainData = "/Users/vpati/Documents/workspace_intuit_ml/Weka_LinearRegression/src/out.arff";
        System.out.println("start");
        classifier = trainModel(arffTrainData, 200);
        System.out.println("end");
    }

    public static void main(String[] args) throws Exception {
        LinearRegressions linearRegressions = new LinearRegressions();
        linearRegressions.train();
        double star = linearRegressions.getScore("0.0070462,-0.375656,6.59445,10.7999,5.17862,5.34426,4.46622,4.98453,3.69897,4.29579,2.97751,3.11349,3.69716,4.54229,2.66262,2.77872,4.89961,3.50297,3.76341,7.71711,7.04627,6.12405,4.25112,3.13325,1.76349,3.8042,1.71887,0.92883,2.8831,5.46252,3.20631,4.59715,3.69492,2.79454,7.08418,3.92919,0.101539,7.03746,10.78,1.8406,3.62178,1.73623,1.32754,10.2861,-0.222445,-0.0566754,2.27336,9.61882,10.2323,-4.23084,1.6085,2.26121,4.05357,6.8821,-1.77114,2.60308,2.31546,3.07999,0.805551,1.03465,0.775829,7.44832,-0.522904,7.08303,10.5766,3.59867,0.283273,1.47187,10.9989,10.0721,-1.97774,5.66686,-1.80333,5.47082,1.39988,3.25202,0.941195,0.932633,2.34305,-0.651436,-1.43627,1.93208,6.43752,0.236334,-0.391565,6.73311,0.47255,1.46885,3.19951,-1.38578,-3.29412,0.995966,1.33558,1.60489,-0.709955,3.70892,-1.75927,-3.69673,-0.784254,1.85365,-0.920303,-0.0507707,0.824748,2.00378,0.682521,0.0870774,0.57941,2.24315,3.35798,-3.31912,2.89183,2.03813,2.82468,-0.164551,4.17972,-0.769695,10.3088,3.29036,3.64479,-4.47456,-2.33872,-0.469883,1.90438,6.30922,1.83066,-1.19361,-0.338134,-0.132902,0.997131,10.671,1.0174,-1.40362,7.07592,0.924949,-3.57268,1.65422,1.06321,0.177743,1.04982,0.100142,-0.906475,1.75364,-2.31187,0.0608575,-1.71722,2.208,-1.8814,0.344151,-1.17166,-0.375532,-2.26942,1.41654,-2.64841,6.02896,0.186028,-2.86485,3.99101,-2.72552,1.45912,-0.59072,-1.48916,8.41865,0.969476,0.00894891,10.7545,1.15498,2.98855,-2.74009,-2.53087,-2.04755,1.18861,-2.68048,0.543539,1.92388,1.37125,8.76171,4.7899,7.47965,-0.182865,0.474072,0.0501743,-0.842947,0.474776,0.865238,-0.473771,1.15129,-1.68506,5.95783,-2.33162,2.08648,1.08477,0.618451,-0.14434,0.900791,0.409514,-3.20384,-3.18968,-1.51967,9.17581,-0.774511,10\n");
        System.out.println(star);
    }
}