package readability;

import java.io.File;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    static double polysyllablesNum;
    public static void main(String[] args) {
        File file=new File(args[0]);
        String text=null;
        try(Scanner scanner=new Scanner(file)){
            while (scanner.hasNext()){
                text=scanner.nextLine();
            }
        }catch (Exception e){
            System.out.println(e.getClass().getSimpleName());
        }

        double sentencesNum=text.split("[.?!]").length;
        String [] words=text.split("\\s+");
        double wordsNum=words.length;
        double charsNum=text.replaceAll("\\s","").length();
        double syllablesNum=countSyllables(text)+wordsNum;

        System.out.println("The text is: \n"+text);
        System.out.println("\nWords: "+wordsNum);
        System.out.println("Sentences: "+sentencesNum);
        System.out.println("Characters: "+charsNum);
        System.out.println("Syllables: "+syllablesNum);
        System.out.println("Polysyllables: "+polysyllablesNum);

        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        Scanner scanner=new Scanner(System.in);
        String method=scanner.nextLine();
        switch (method){
            case "ARI":
            applyARI(sentencesNum,wordsNum,charsNum);
                break;
            case "FK":
            applyFK(sentencesNum,wordsNum,syllablesNum);
                break;
            case "SMOG":
            applySMOG(sentencesNum,polysyllablesNum);
                break;
            case "CL":
            applyCL(sentencesNum,wordsNum,charsNum);
                break;
            case "all":
            double ageARI= applyARI(sentencesNum,wordsNum,charsNum);
            double ageFK=applyFK(sentencesNum,wordsNum,syllablesNum);
            double ageSMOG=applySMOG(sentencesNum,polysyllablesNum);
            double ageCL=applyCL(sentencesNum,wordsNum,charsNum);
            System.out.println("This text should be understood in average by "+(ageARI+ageFK+ageSMOG+ageCL)/4+"-year-olds.");
                break;
        }
    }

    private static double applyCL(double sentencesNum, double wordsNum, double charactersNum) {
        double L=(charactersNum/wordsNum)*100;
        double S=(sentencesNum/wordsNum)*100;
        double score=0.0588*L-0.296*S-15.8;
        int roundScore= (int) Math.round(score);
        System.out.printf("Coleman–Liau index: %.2f ", score);
        return showAge(roundScore);
    }

    private static double applySMOG(double sentencesNum, double polysyllablesNum) {
        double score=  1.043*Math.sqrt(polysyllablesNum*(30/sentencesNum))+3.1291;
        int roundScore= (int) Math.round(score);
        System.out.printf("Simple Measure of Gobbledygook: %.2f ", score);
        return showAge(roundScore);
    }

    private static double applyFK(double sentencesNum, double wordsNum, double syllablesNum) {
        double score=  (0.39*(wordsNum/sentencesNum)+11.8*(syllablesNum/wordsNum)-15.59);
        int roundScore= (int) Math.round(score);
        System.out.printf("Flesch–Kincaid readability tests: %.2f ", score);
        return showAge(roundScore);
    }

    private static double applyARI(double sentencesNum, double wordsNum, double charsNum) {
        double score=  (4.71*(charsNum/wordsNum)+0.5*(wordsNum/sentencesNum)-21.43);
        int roundScore= (int) Math.round(score);
        System.out.printf("Automated Readability Index: %.2f  ", score);
        return showAge(roundScore);
    }

    private static double countSyllables(String text) {
        String[] words = text.toLowerCase(Locale.ROOT).replaceAll("[aeiouy]{2,}", "a").replaceAll("e\\b", "").split("\\s");
        double count = 0;

        for (String word : words) {
            int helper = 0;
            for (int j = 0; j < word.length(); j++) {
                if (j == 0 && (word.charAt(0) == 'a' || word.charAt(0) == 'e' || word.charAt(0) == 'i' || word.charAt(0) == 'o' || word.charAt(0) == 'u' || word.charAt(0) == 'y')) {
                    helper++;
                    continue;}
                if (word.charAt(j) == 'a' || word.charAt(j) == 'e' || word.charAt(j) == 'i' || word.charAt(j) == 'o' || word.charAt(j) == 'u' || word.charAt(j) == 'y')
                    helper++;
                if (j == word.length() - 1 && helper > 2) {
                    polysyllablesNum++;}
                if (j == word.length() - 1 && helper > 1) {
                    count += (helper - 1);
                }
            }
        }
        return count;
    }

    private static double showAge(int roundScore) {
        int [] age={6,7,9,10,11,12,13,14,15,16,17,18,24,25};
        System.out.println("(about "+age[roundScore-1]+"-year-olds).");
        return age[roundScore-1];
    }
}
