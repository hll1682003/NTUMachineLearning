import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PocketPLA {

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

    private static double[] PocketPLA(double data[][]) {
        double[] weights = new double[data[0].length-1];
        double[] pocket=Arrays.copyOf(weights,weights.length);
        double previousErrorRate=1;
        double currentErrorRate;
        double sum = 0;
        int updateCount=0;
        Arrays.fill(weights, 0);
        //Halt when there're 50 updates
        while (updateCount<50) {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length - 1; j++) {
                    sum += weights[j] * data[i][j];
                }
                //An error is encountered and needs improvement
                if (sum * data[i][data[i].length - 1] <= 0) {
                    for (int k = 0; k < weights.length; k++) {
                        //update weights: w=w+yx
                        weights[k] += data[i][data[i].length - 1] * data[i][k];
                    }
                    updateCount++;
                    if (updateCount>=50)
                    {
                        break;
                    }
                    currentErrorRate=verification(weights,"hw1_18_train.dat");
                    if (previousErrorRate>currentErrorRate)
                    {
                        previousErrorRate=currentErrorRate;
                        pocket=Arrays.copyOf(weights,weights.length);
                    }
                }
                sum = 0;
            }
        }
        return pocket;
    }

    //verify the trained weight on given training data and output error rate
    private static double verification(double[] weights, String verifyData)
    {
        double[][] v=readinData(verifyData);
        double sum;
        int count=0;
        double output;
        for (double[] i:v)
        {
            sum=0;
            for (int j=0;j<i.length-1;j++)
            {
                sum+=weights[j]*i[j];
            }
            if (sum*i[i.length-1]<=0)
            {
                count++;
            }
        }
        output=1.0*count/v.length;
        return output;
    }

    private static double[][] readinData(String filename)
    {
        String s;
        int pos;
        int arrayTrial = 0;
        double trainingData[][] = new double[lineCounter(filename)][6];
        //read in the training data
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
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
        return trainingData;
    }

    public static void main(String[] args) {
        int sum=0;
        double[][] data=readinData("hw1_18_train.dat");
        double[] trainedWeight;
        double errorRate;
        double errorRatesum=0;
        for (int i=0;i<2000;i++)
        {
            shuffleArray(data);
            trainedWeight=PocketPLA(data);
            errorRate=verification(trainedWeight,"hw1_18_test.dat");
            errorRatesum+=errorRate;
        }
        System.out.println("The average Error rate is: "+errorRatesum/2000.0);


    }
}
