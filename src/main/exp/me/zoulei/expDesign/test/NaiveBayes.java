package me.zoulei.expDesign.test;
import java.util.*;
import java.util.stream.Collectors;

class Instance {
    private List<String> features;
    private String label;

    public Instance(List<String> features, String label) {
        this.features = features;
        this.label = label;
    }

    public List<String> getFeatures() { return features; }
    public String getLabel() { return label; }
}

public class NaiveBayes {
    private Map<String, Double> priorLogProb;
    private Map<String, Map<Integer, Map<String, Double>>> condLogProb;
    private Map<Integer, Set<String>> featureValues;
    private double smoothing = 1.0; // 拉普拉斯平滑参数

    public void train(List<Instance> trainData) {
        priorLogProb = new HashMap<>();
        condLogProb = new HashMap<>();
        featureValues = new HashMap<>();

        Map<String, Integer> classCounts = new HashMap<>();
        Map<String, Map<Integer, Map<String, Integer>>> featureCounts = new HashMap<>();

        // 第一遍遍历：统计类别和特征值
        for (Instance inst : trainData) {
            String label = inst.getLabel();
            List<String> features = inst.getFeatures();
            
            // 更新类别计数
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
            
            // 更新特征值集合
            for (int i = 0; i < features.size(); i++) {
                featureValues.computeIfAbsent(i, k -> new HashSet<>()).add(features.get(i));
            }
        }

        // 第二遍遍历：统计特征计数
        for (Instance inst : trainData) {
            String label = inst.getLabel();
            List<String> features = inst.getFeatures();

            Map<Integer, Map<String, Integer>> featMap = featureCounts
                .computeIfAbsent(label, k -> new HashMap<>());

            for (int i = 0; i < features.size(); i++) {
                Map<String, Integer> countMap = featMap
                    .computeIfAbsent(i, k -> new HashMap<>());
                
                String value = features.get(i);
                countMap.put(value, countMap.getOrDefault(value, 0) + 1);
            }
        }

        // 计算先验概率（对数）
        int total = trainData.size();
        for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
            String label = entry.getKey();
            double prob = (double) entry.getValue() / total;
            priorLogProb.put(label, Math.log(prob));
        }

        // 计算条件概率（对数）
        for (String label : classCounts.keySet()) {
            int classTotal = classCounts.get(label);
            Map<Integer, Map<String, Double>> featProbs = new HashMap<>();
            condLogProb.put(label, featProbs);

            for (int i = 0; i < featureValues.size(); i++) {
                Set<String> values = featureValues.get(i);
                int numValues = values.size();
                Map<String, Integer> counts = featureCounts.get(label)
                    .getOrDefault(i, Collections.emptyMap());
                
                Map<String, Double> probMap = new HashMap<>();
                for (String value : values) {
                    int count = counts.getOrDefault(value, 0);
                    double prob = (count + smoothing) / 
                                 (classTotal + smoothing * numValues);
                    probMap.put(value, Math.log(prob));
                }
                featProbs.put(i, probMap);
            }
        }
    }

    public String predict(List<String> features) {
        String bestLabel = null;
        double maxLogProb = Double.NEGATIVE_INFINITY;

        for (String label : priorLogProb.keySet()) {
            double logProb = priorLogProb.get(label);

            for (int i = 0; i < features.size(); i++) {
                String value = features.get(i);
                Map<String, Double> probMap = condLogProb.get(label).get(i);
                
                if (probMap.containsKey(value)) {
                    logProb += probMap.get(value);
                } else {
                    // 处理未见过的特征值
                    int numValues = featureValues.get(i).size();
                    double smoothedProb = (0 + smoothing) / 
                                        (condLogProb.get(label).size() + smoothing * numValues);
                    logProb += Math.log(smoothedProb);
                }
            }

            if (logProb > maxLogProb) {
                maxLogProb = logProb;
                bestLabel = label;
            }
        }
        return bestLabel;
    }

    public static void main(String[] args) {
        // 训练数据：天气（0）、温度（1）、是否出去玩
        List<Instance> trainData = Arrays.asList(
            new Instance(Arrays.asList("sunny", "hot"), "no"),
            new Instance(Arrays.asList("sunny", "hot"), "no"),
            new Instance(Arrays.asList("overcast", "hot"), "yes"),
            new Instance(Arrays.asList("rainy", "mild"), "yes"),
            new Instance(Arrays.asList("rainy", "cool"), "yes"),
            new Instance(Arrays.asList("rainy", "cool"), "no"),
            new Instance(Arrays.asList("overcast", "cool"), "yes"),
            new Instance(Arrays.asList("sunny", "mild"), "no"),
            new Instance(Arrays.asList("sunny", "cool"), "yes"),
            new Instance(Arrays.asList("rainy", "mild"), "yes"),
            new Instance(Arrays.asList("sunny", "mild"), "yes"),
            new Instance(Arrays.asList("overcast", "mild"), "yes"),
            new Instance(Arrays.asList("overcast", "hot"), "yes"),
            new Instance(Arrays.asList("rainy", "mild"), "no")
        );

        NaiveBayes nb = new NaiveBayes();
        nb.train(trainData);

        // 测试数据
        List<String> testFeatures = Arrays.asList("sunny", "mild");
        String result = nb.predict(testFeatures);
        System.out.println("预测结果: " + result); // 应该输出 "yes"
    }
}