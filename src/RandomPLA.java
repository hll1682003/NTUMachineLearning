import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPLA {

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(double[][] ar)
    {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            double[] a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private static int lineCounter(String filename) {
        int lines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((br.readLine()) != null) {
                lines++;
            }
        } catch (IOException exc) {
            System.out.println("I/O error:" + exc);
        }
        return lines;
    }

    private static int randomPLA(double data[][]) {
        double[] weights = new double[data[0].length-1];
        double sum = 0;
        boolean done = false;
        int updateCount=0;
            Arrays.fill(weights, 0);
            while (!done) {
                done = true;
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[i].length - 1; j++) {
                        sum += weights[j] * data[i][j];
                    }
                    //An error is encountered and needs improvement
                    if (sum * data[i][data[i].length - 1] <= 0) {
                        for (int k = 0; k < weights.length; k++) {
                            //update weights: w=w+yx
                            weights[k] += 0.5*data[i][data[i].length - 1] * data[i][k];
                        }
                        done = false;
                        updateCount++;
                    }
                    sum = 0;
                }
            }
            System.out.println("count: "+updateCount);

        System.out.println("Average Updates: "+updateCount/2000);
        return updateCount;
    }

    public static void main(String[] args) {
        String s;
        int pos;
        int arrayTrial = 0;
        int sum=0;
        double trainingData[][] = new double[lineCounter("hw1_15_train.dat")][6];
        //read in the training data
        try (BufferedReader br = new BufferedReader(new FileReader("hw1_15_train.dat"))) {
            while ((s = br.readLine()) != null) {
                pos = s.indexOf("\t", 0);
                //the label is saved in the last position of each row
                trainingData[arrayTrial][trainingData[arrayTrial].length - 1] = Double.parseDouble(s.substring(pos));
                s = s.substring(0, pos);
                for (int i = 0; i < 5; i++) {
                    if (i == 0) {
                        trainingData[arrayTrial][i] = 1;
                        continue;
                    }
                    pos = s.indexOf(" ");
                    if (pos != -1) {
                        trainingData[arrayTrial][i] = Double.parseDouble(s.substring(0, pos));
                        s = s.substring(pos + 1);
                    } else {
                        trainingData[arrayTrial][i] = Double.parseDouble(s);
                    }
                }
                arrayTrial++;
            }
        } catch (IOException exc) {
            System.out.println("I/O error:" + exc);
        }

       for (int i=0;i<2000;i++)
       {
           shuffleArray(trainingData);
           sum+=randomPLA(trainingData);
       }
        System.out.println("Average updates:"+(sum/2000));


    }
}
