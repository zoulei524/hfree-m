package me.zoulei.expDesign.test;
import java.util.*;

/**
 * （NBC）‌是一种基于贝叶斯定理的概率分类算法，适用于解决各种机器学习中的分类问题。其核心思想是通过先验概率和条件概率，
 * 计算某个类别下特定特征的后验概率。NBC假设所有特征是相互独立的，尽管这个假设在实际应用中通常不成立，但它使得计算过程大大简化，算法效率较高
 * log(p1 * p2) = log(p1) + log(p2)‌
 */
public class NaiveBayesClassifier1 {
    private Map<String, Double> classPrior;  // 类别先验概率
    private Map<String, Map<String, Double>> featureCondProb;  // 特征条件概率
    private double smoothing = 1.0; // 拉普拉斯平滑参数
    public void train(List<String[]> trainingData, List<String> classNames) {
        // 计算类别先验概率
        classPrior = new HashMap<>();
        int totalSamples = trainingData.size();
        for (String className : classNames) {
            int classCount = 0;
            for (String[] sample : trainingData) {
                if (sample[0].equals(className)) {
                    classCount++;
                }
            }
            classPrior.put(className, (double) classCount / totalSamples);
        }

        // 计算特征条件概率（假设特征独立）
        featureCondProb = new HashMap<>();
        for (String className : classNames) {
            featureCondProb.put(className, new HashMap<>());
            Map<String, Integer> featureCount = new HashMap<>();
            int classSamples = 0;
            for (String[] sample : trainingData) {
                if (sample[0].equals(className)) {
                    classSamples++;
                    for (int i = 1; i < sample.length; i++) {
                        String feature = sample[i];
                        featureCount.put(feature, featureCount.getOrDefault(feature, 0) + 1);
                    }
                }
            }
            // 转换为概率
            for (String feature : featureCount.keySet()) {
                double prob = (double) featureCount.get(feature) / classSamples;
                featureCondProb.get(className).put(feature, prob);
            }
        }
    }

    public String classify(String[] testData) {
        double maxProbability = -1;
        String predictedClass = null;

        for (String className : classPrior.keySet()) {
            double posterior = classPrior.get(className);
            for (int i = 0; i < testData.length; i++) {
                String feature = testData[i];
                double condProb = featureCondProb.get(className).getOrDefault(feature, 0.0);
                posterior *= condProb;
            }

            if (posterior > maxProbability) {
                maxProbability = posterior;
                predictedClass = className;
            }
        }
        return predictedClass;
    }

    public static void main(String[] args) {
        // 示例数据：格式为 [类别, 特征1, 特征2, ...]
        List<String[]> trainingData = Arrays.asList(
            new String[]{"Spam", "buy", "now", "discount"},
            new String[]{"Spam", "free", "offer", "win"},
            new String[]{"Ham", "meeting", "project", "deadline"},
            new String[]{"Ham", "hello", "friend", "chat"}
        );
        List<String> classNames = Arrays.asList("Spam", "Ham");

        NaiveBayesClassifier1 nb = new NaiveBayesClassifier1();
        nb.train(trainingData, classNames);

        // 测试数据
        String[] testSample = new String[]{"free", "offer", "click"};
        System.out.println("预测类别: " + nb.classify(testSample));  // 应输出 Spam
    }
}