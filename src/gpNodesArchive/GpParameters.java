package gpNodesArchive;

import java.util.ArrayList;
import java.util.Random;

import bppModel.AbstractAlgorithm;
import bppModel.BF;
import bppModel.FF;
import bppModel.NF;
import bppModel.Problem;
import bppModel.Solution;
import bppModel.WF;
import ea.Parameters;
import io.FileGetter;
import io.Jar;
import io.ProblemReader;
import io.StringIO;

public class GpParameters {
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 1000000; i ++) {
			int val = (int) (Parameters.getMEAN_ITEM_SIZE() + Parameters.random.nextGaussian() * Parameters.getSTANDARD_DEVIATION());
			//System.out.println(val);
			if(val < Parameters.MIN_ITEM_SIZE || val > Parameters.MAX_ITEM_SIZE) {
				continue;
			}
			sb.append("" + val + "\r\n");
		}
		StringIO.writeStringToFile("normal.csv", sb.toString(), false);
	}

	public static Random random = new Random();
	public static int maximumRecombinationDepth = 17;
	public static int popSize = 100;
	public static int maximumInitialDepth = 6;
	public static int numberOfGenerations = 1000000;
	public static double crossoverProbability = 0.99;
	public static String jarName = getJarName();
	
	private static String getJarName() {
		return Jar.getName();
	}
	
	
	/**
	 * The class that the problems were evolved for
	 */
	public static Class algorithmClass = BF.class;	
	public static ArrayList<Problem> problems = getProblems();
	public static double totalBenchmarkFitness = getBenchMarkFitness();
	
	private static ArrayList<Problem> getProblems() {
		ArrayList<Problem> problems = new ArrayList<>();
		String path = "./google/";
		String prefix = "E_120_" + algorithmClass.getSimpleName();	
		prefix = "";
//		String path = "";
//		String prefix = "";			
		
		for(String filename : FileGetter.getFileNames(path, prefix, ".bpp")) {				
			Problem p = ProblemReader.getProblem(path, filename);				
			problems.add(p);
			if(problems.size() == 20) {
				break;
			}
		}
		return problems;
	}
	private static double getBenchMarkFitness() {		
		double sum = 0;
		try {
			AbstractAlgorithm alg = (AbstractAlgorithm) algorithmClass.newInstance();
			for(Problem p : problems) {
				Solution s = new Solution(p);
				alg.packRemainingItems(s);
				sum += s.getFitness();
			}
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sum;
	}
}
