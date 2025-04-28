package me.zoulei.expDesign.test;


import java.util.*;

/**
 * 朴素贝叶斯分类器实现（非对数概率版本）
 * 数据结构要求：[类别标签, 特征1, 特征2, ..., 特征n]
 */
public class NaiveBayesClassifier2 {
    // 类先验概率存储：类别 -> P(类别)
    private Map<String, Double> classPriors;
    
    // 特征似然概率三维存储：类别 -> (特征索引 -> (特征值 -> 概率))
    private Map<String, Map<Integer, Map<String, Double>>> featureLikelihoods;
    
    // 记录每个特征可能的取值（用于处理未见特征值）
    private Map<Integer, Set<String>> featureValueCounts;
    
    // 类别计数：类别 -> 出现次数
    private Map<String, Integer> classCounts;
    
    // 平滑系数（拉普拉斯平滑）
    private double smoothingAlpha;

    /**
     * 构造方法
     * @param smoothingAlpha 平滑系数（推荐1.0为拉普拉斯平滑）
     */
    public NaiveBayesClassifier2(double smoothingAlpha) {
        this.smoothingAlpha = smoothingAlpha;
        this.classPriors = new HashMap<>();
        this.featureLikelihoods = new HashMap<>();
        this.featureValueCounts = new HashMap<>();
        this.classCounts = new HashMap<>();
    }

    /**
     * 训练方法
     * @param trainingData 训练数据集，格式为[[类别, 特征1, 特征2, ...], ...]
     */
    public void train(List<List<String>> trainingData) {
        if (trainingData.isEmpty()) return;
        int featureCount = trainingData.get(0).size() - 1; // 特征数量 = 总列数 - 类别列

        // 初始化特征值存储结构（记录每个特征的可能取值）
        for (int i = 0; i < featureCount; i++) {
            featureValueCounts.put(i, new HashSet<>());
        }

        // 第一阶段：统计类别和特征值 ------------------------------------------
        for (List<String> sample : trainingData) {
            String cls = sample.get(0);
            // 更新类别计数器
            classCounts.put(cls, classCounts.getOrDefault(cls, 0) + 1);
            
            // 记录每个特征的可能取值
            for (int i = 0; i < featureCount; i++) {
                String value = sample.get(i + 1); // i+1因为第0位是类别
                featureValueCounts.get(i).add(value);
            }
        }

        // 第二阶段：统计特征频率（带平滑准备）-----------------------------------
        // 临时存储结构：类别 -> (特征索引 -> (特征值 -> 出现次数))
        Map<String, Map<Integer, Map<String, Integer>>> featureFrequencies = new HashMap<>();
        
        for (List<String> sample : trainingData) {
            String cls = sample.get(0);
            // 获取该类别对应的特征计数器
            Map<Integer, Map<String, Integer>> clsFeatures = featureFrequencies
                .computeIfAbsent(cls, k -> new HashMap<>());

            // 遍历每个特征
            for (int i = 0; i < featureCount; i++) {
                String value = sample.get(i + 1);
                // 获取该特征索引对应的值计数器
                Map<String, Integer> valueCounts = clsFeatures
                    .computeIfAbsent(i, k -> new HashMap<>());
                // 更新特征值计数
                valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
            }
        }

        // 第三阶段：计算先验概率 ----------------------------------------------
        int totalSamples = trainingData.size();
        for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
            // P(类别) = 类别出现次数 / 总样本数
            classPriors.put(entry.getKey(), (double) entry.getValue() / totalSamples);
        }

        // 第四阶段：计算似然概率（带拉普拉斯平滑）-------------------------------
        for (String cls : classCounts.keySet()) {
            int clsCount = classCounts.get(cls); // 当前类别的样本数
            
            // 初始化该类别对应的特征概率存储
            Map<Integer, Map<String, Double>> clsLikelihoods = new HashMap<>();
            featureLikelihoods.put(cls, clsLikelihoods);

            // 遍历每个特征
            for (int i = 0; i < featureCount; i++) {
                Set<String> possibleValues = featureValueCounts.get(i); // 该特征所有可能取值
                Map<String, Double> probMap = new HashMap<>();
                
                // 获取该类别该特征的实际计数（可能为空）
                Map<String, Integer> counts = featureFrequencies.get(cls)
                    .getOrDefault(i, new HashMap<>());
                
                // 计算每个特征值的概率（带平滑）
                for (String value : possibleValues) {
                    int count = counts.getOrDefault(value, 0); // 实际出现次数
                    // 平滑公式：P(特征值|类别) = (count + α) / (N + α*K)
                    // 其中：
                    // α = 平滑系数
                    // N = 类别样本总数
                    // K = 该特征的可能取值数量
                    double probability = (count + smoothingAlpha) / 
                        (clsCount + smoothingAlpha * possibleValues.size());
                    probMap.put(value, probability);
                }
                clsLikelihoods.put(i, probMap); // 存储该特征的概率分布
            }
        }
    }

    /**
     * 预测方法
     * @param features 特征值列表（不包含类别）
     * @return 预测的类别
     */
    public String predict(List<String> features) {
        String bestClass = null;
        double maxProbability = Double.NEGATIVE_INFINITY;

        // 遍历所有可能的类别
        for (String cls : classPriors.keySet()) {
            // 初始化为先验概率
            double probability = classPriors.get(cls);
            
            // 遍历每个特征进行概率累乘
            for (int i = 0; i < features.size(); i++) {
                String value = features.get(i);
                Map<String, Double> probMap = featureLikelihoods.get(cls).get(i);
                
                if (probMap.containsKey(value)) {
                    // 如果特征值存在，直接使用存储的概率
                    probability *= probMap.get(value);
                } else {
                    // 处理未见过的特征值（使用平滑计算）
                    int possibleValues = featureValueCounts.get(i).size();
                    // 平滑概率公式：P = α / (N + α*K)
                    double smoothedProb = smoothingAlpha / 
                        (classCounts.get(cls) + smoothingAlpha * possibleValues);
                    probability *= smoothedProb;
                }
            }

            // 保留最大概率对应的类别
            if (probability > maxProbability) {
                maxProbability = probability;
                bestClass = cls;
            }
        }
        return bestClass;
    }

    /**
     * 示例使用
     */
    public static void main(String[] args) {
        // 示例数据集：判断是否适合户外活动
        // 特征说明：[是否适合, 天气, 温度]
        List<List<String>> trainingData = Arrays.asList(
            Arrays.asList("是", "晴", "热"),  // 样本1
            Arrays.asList("是", "晴", "温"),  // 样本2
            Arrays.asList("否", "雨", "冷"),  // 样本3
            Arrays.asList("否", "雨", "冷")   // 样本4
        );

        // 创建分类器（使用拉普拉斯平滑）
        NaiveBayesClassifier2 classifier = new NaiveBayesClassifier2(1.0);
        classifier.train(trainingData);

        // 测试样本：天气=雨，温度=冷
        List<String> testSample = Arrays.asList("雨", "冷");
        System.out.println("预测结果: " + classifier.predict(testSample)); // 应输出"否"
    }
}