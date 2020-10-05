import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

// Driver class that takes in training text file and predicts sentiment on given reviews

public class NaiveBayesClassifier {
            public static long posTotal =0;
            public static long negTotal =0;
            public static long numPosRev =0;
            public static long numNegRev =0;
            public static Instant start;
            public static Instant end;
            public static HashMap<String, Integer> stopWords = new HashMap<>();

        // Adds training and testing words into a HashMap to count frequencies
            
        public static void addToMap(ArrayList<String> arr,HashMap<String, fraction> m, int isPos){
            double posNum = 0;
            double negNum = 0;
            double alpha = 1;
            String wordPair = "";
            ArrayList<String> temp = arr;


            if(isPos == 1){
                posNum = 1 + alpha;
                negNum = alpha;
            }
            else{
                posNum = alpha;
                negNum = 1 + alpha;
            }
 
  
            int s = temp.size();    
            for(int i=0; i < s; i++){    
                if(i > 0){
                     temp.add(arr.get(i-1) + arr.get(i));
                }

            }
             arr = temp;
           
            for(int i=0; i < arr.size(); i++){
                

                if(isPos ==1){
                    posTotal++;
                }
                else{
                    negTotal++;
                }


                if(!m.containsKey(arr.get(i))){
                    m.put(arr.get(i), new fraction(posNum,0,negNum,0));
                }
                else{
                    if(isPos == 1){
                        fraction tmp = m.get(arr.get(i));
                        tmp.posNum++;
                        m.put(arr.get(i), tmp);
                    }
                    else if(isPos == 0){
                        fraction tmp = m.get(arr.get(i));
                        tmp.negNum++;
                        m.put(arr.get(i), tmp);
                    }
                }
             

                
            }
        }
         
        // Calculates negative and postive rating scores and returns the higher value
            
        public static int classifyRev(String[] arr, HashMap<String, fraction> m){
            
            double posRating = 0;
            double negRating = 0;
            long total  = posTotal + negTotal;
            double alpha = .5;
            double temp = 0;
            double lastWord = 5;
            String wordPair = "";
            ArrayList<String> tmp = new ArrayList<String>(Arrays.asList(arr));

            int s = tmp.size();    
            for(int i=0; i < s; i++){    
                if(i > 0){
                     tmp.add(tmp.get(i-1) + tmp.get(i));
                }

            }

            for(int i =0; i < tmp.size(); i++){
                
                if(i == arr.length -2) {
                    lastWord = 1;
                }
                else{
                    lastWord = 1;
                }

                if(m.containsKey(tmp.get(i))){
                  fraction f = m.get(tmp.get(i));
                  
                  temp = (double)f.posNum / posTotal;
                  temp = temp * lastWord;
                  if(temp != 0 ){
                    posRating = posRating + Math.log(temp);
                  } 
                  temp = (double)f.negNum / negTotal;
                  temp = temp * lastWord;
                  if(temp != 0 ){
                  negRating = negRating + Math.log(temp);
                  }
                } 
                else{
    
                    posRating = posRating + Math.log(alpha/posTotal) * lastWord;
                    negRating = negRating + Math.log(alpha/negTotal) * lastWord;

                }
            }
            
            posRating = posRating + Math.log((double)posTotal/ total);
            negRating = negRating + Math.log((double)negTotal/ total);

          
            
            if(posRating > negRating){
                return 1;
            }
            else{
                return 0;
            }

        }
        public static int ab(String[] arr){
            if(arr[arr.length-1].equals(",0")){
                return 0;
            }
            else 
                return 1;
        }

        public static void main(String[] args) throws Exception{
            // error handling
                    
            if(args.length != 2){
                System.out.println("Please enter two files");
                System.exit(0);
            }
            
            // Takes in testing and training files
                    
            Scanner scannerTrain = new Scanner(new File(args[0]));
            Scanner scannerTrain2 = new Scanner(new File(args[0]));
            Scanner scannerTest = new Scanner(new File(args[1])); 

            String[] wordsTemp;
            ArrayList<String> wordsList = new ArrayList<>();
            HashMap<String, fraction> wordMap = new HashMap<>();
            ArrayList<Integer> results = new ArrayList<>();
            ArrayList<Integer> resultsCheck = new ArrayList<>();
            String line;
            int isPos = -1;
            
            start = Instant.now();
            
            // Adds the words of the training data into a Map for calculations
                    
            while(scannerTrain.hasNextLine()){
                
                line  = scannerTrain.nextLine();
                wordsTemp= line.split(" ");

                for(int i = 0; i < wordsTemp.length;i++){
                    if(wordsTemp[i].equals(",0" )){
                        isPos = 0;
                        addToMap(wordsList, wordMap, isPos);
                        negTotal++;
                        numNegRev++;
                        wordsList.clear();
                    }
                    else if(wordsTemp[i].equals(",1")) {
                        isPos = 1;
                        addToMap(wordsList, wordMap, isPos);
                        posTotal++;
                        numPosRev++;
                        wordsList.clear();
                    }
                    else{
                        wordsList.add(wordsTemp[i]);
                    }
                    
                }

        
            }
            end = Instant.now();
          

            java.time.Duration betweenTrain = java.time.Duration.between(start, end);
            
            

            start = Instant.now();
                    
            // takes in the testing data and uses training data to predict the sentiment
                    
            while(scannerTrain2.hasNextLine()){
                line  = scannerTrain2.nextLine();
                wordsTemp= line.split(" ");
                int temp = classifyRev(wordsTemp, wordMap);
                int temp2 = ab(wordsTemp);
                results.add(temp);
                resultsCheck.add(temp2);
            }
            double a =0;
            double b=0;

            for(int i =0; i < results.size(); i++){
                if(results.get(i) != resultsCheck.get(i)){
                    a++;
                }
                else{
                    b++;
                }
            }
            double timeFormat = (double)Math.round((b/(a+b)) * 1000d)/1000d;
          


            results.clear();
            resultsCheck.clear();

            while(scannerTest.hasNextLine()){
                line  = scannerTest.nextLine();
                wordsTemp= line.split(" ");
                int temp = classifyRev(wordsTemp, wordMap);
                int temp2 = ab(wordsTemp);
                results.add(temp);
                resultsCheck.add(temp2);
        
            }
             a=0;
             b=0;

            for(int i =0; i < results.size(); i++){
                if(results.get(i) != resultsCheck.get(i)){
                    a++;
                }
                else{
                    b++;
                }
            }
            end = Instant.now();

            java.time.Duration betweenTest = java.time.Duration.between(start, end);

            for(int i =0; i< results.size();i++){
                System.out.println(results.get(i));
            }
            System.out.println(betweenTrain.getSeconds() + " seconds (training)");
            System.out.println(betweenTest.getSeconds() + " seconds (labeling)");
            System.out.println(timeFormat + " (training)");
            timeFormat = (double)Math.round((b/(a+b)) * 1000d)/1000d;
            System.out.println(timeFormat + " (testing)");
   
           



        }


}
