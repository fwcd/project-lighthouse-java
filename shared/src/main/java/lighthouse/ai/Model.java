package lighthouse.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lighthouse.ai.layers.Conv2d;
import lighthouse.ai.layers.Dense;

public class Model implements WeightIterator, Comparable<Model> {

    private Random r = new Random();
    private List<Double> weights = new ArrayList<>();
    private int weightCounter = 0;
    public double fitness = 0d;
    private List<int[]> args = new ArrayList<>();
    private Dense dense;
    private Conv2d conv;

    public Model(){
        this.dense = new Dense(this);
        this.conv = new Conv2d(this);
    }

    public Model(List<Double> weights){
        this.weights = weights;
    }

    @Override
    public double getNextWeight(){
        if (this.weights.size() <= weightCounter){
            this.weights.add(r.nextDouble());
        }
        return weights.get(weightCounter++);
    }

    public void addDense(int size){
        int[] i = {size};
        this.args.add(i);
    }

    public void addConv(int height, int width){
        int[] i = {height, width};
        this.args.add(i);
    }

    public double feed(double[][] in, double[][] goal){
        for (int [] arg : args){
            if (arg[0] == -1){
                double[][] nin = new double[in.length* 2][];
                for (int i = 0; i < in.length; i++){
                    nin[i] = new double[1];
                    nin[i+1] = new double[1];
                    nin[i][0] = in[i][0];
                    nin[i+1][0] = goal[i][0];
                }
                in = nin;
            }
            else if (arg.length == 1){
                in = dense.calculate(in, arg[0]);
            }else{
                in = conv.calculate(in, arg[0], arg[1]);
                goal = conv.calculate(goal, arg[0], arg[1]);
            }
            for(int i = 0; i < in.length; i++){
                for(int o = 0; o < in[i].length; o++){
                    in[i][o] = Math.max(0, in[i][o]);
                }
            }
        }
        this.weightCounter = 0;
        return in[0][0];
    }

    public List<Double> getWeights(){
        return weights;
    }

    public void setWeights(List<Double> weights){
        this.weights = weights;
    }

    public void mutateWeights(){
        for (int i = 0; i < weights.size(); i++){
            weights.set(i, weights.get(i) +  r.nextGaussian());
        }
    }

    @Override
    public int compareTo(Model other) {
        return fitness > other.fitness ? 1 : (fitness == other.fitness ? 0 : -1);
    }
    
    @Override
    public String toString() {
        return "fitness=" + fitness;
    }
}
