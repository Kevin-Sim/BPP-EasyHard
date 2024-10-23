package io;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bppModel.AbstractAlgorithm;
import bppModel.BF;
import bppModel.FF;
import bppModel.Item;
import bppModel.NF;
import bppModel.Problem;
import bppModel.Solution;
import bppModel.WF;
import ea.Individual;
import ea.Parameters;
import ea.Parameters.Init;


public class ProblemReader {
	
	public static void main(String[] args) {
		
		HashMap<String, Integer> map = new HashMap<>();
		map.put("FF", 0);
		map.put("BF", 0);
		map.put("WF", 0);
		map.put("NF", 0);
		int count = 0;
		for(String str : StringIO.readStringsFromFile("poisson50.csv")) {
			count++;
			if(count == 1) {
				continue;
			}
			String key = str.split(",")[4];
			int value = map.get(key);
			value++;
			map.put(key, value);
		}
		for(String key : map.keySet()) {
			System.out.println(key + "\t" + map.get(key));
		}
		
		System.exit(-1);

		
//		for(int i = 0; i < 1000; i++) {
//			Individual individual = new Individual();
//			StringIO.writeStringToFile(i + ".bpp", individual.problem.toString() , false);
//		}
//		
//		System.exit(-1);
		
		StringBuilder sb = new StringBuilder();
		
		String path = "./InstancesNew/poisson50/";
		String prefix = "";
//		String path = "";
//		String prefix = "";
		
		
		/**
		 * Output instance data for DL or other classifier as csv
		 */
		
		ArrayList<AbstractAlgorithm> algs = new ArrayList<>();
		algs.add(new FF());
		algs.add(new BF());
		algs.add(new WF());
		algs.add(new NF());
		
		for(String filename : FileGetter.getFileNames(path, prefix, ".bpp")) {			
			if(filename.contains("AWF")) {
				continue;
			}
			Problem p = getProblem(path, filename);
//			sb.append(p.getInstanceData(algs) + "\r\n");
			sb.append(analyzeProblem(p));
		}
		
		StringIO.writeStringToFile("poisson50.csv", sb.toString(), false);
		
		
		System.exit(-1);
		/**
		 * histogram data
		 */
		sb = new StringBuilder();
		sb.append("len\r\n");
		int bins = 0;
		//173692	167283
		for(String filename : FileGetter.getFileNames(path, prefix, ".bpp")) {			
			if(filename.contains("AWF")) {
				continue;
			}
			Problem p = getProblem(path, filename);
			BF alg = new BF();
			Solution s = new Solution(p);
			alg.packRemainingItems(s);
			bins += s.getBins().size();
			
			for(Item item : p.getItems()) {
				sb.append(item.getLength() + "\r\n");
			}			
		}
		System.out.println(bins);
//		System.exit(-1);
		StringIO.writeStringToFile("dist.csv", sb.toString(), false);
//		System.exit(-1);

		/**
		 * tsne on 4000 X 251 instances 1..250,Best
		 */
		sb = new StringBuilder();	
		for(int i = 1; i <= 120; i++) {
			sb.append("" + i + ",");
		}
		sb.append("Best\r\n");
		for(String filename : FileGetter.getFileNames(path, prefix, ".bpp")) {			
			if(filename.contains("AWF")) {
				continue;
			}
			Problem p = getProblem(path, filename);
			for(Item item : p.getItems()) {
				sb.append(item.getLength() + ",");
			}	
			if(filename.contains("_BF")) {
				sb.append("BF");
			}else if(filename.contains("_FF")) {
				sb.append("FF");
			}else if(filename.contains("_NF")) {
				sb.append("NF");
			}else if(filename.contains("_WF")) {
				sb.append("WF");
			}			
			System.out.println(filename);
			sb.append("\r\n");
		}
		StringIO.writeStringToFile("tsne.csv", sb.toString(), false);
//		System.exit(-1);
		
		/**
		 * BoxPlot Data
		 * cols = alg, fitness per problem with best at row end
		 */
		sb = new StringBuilder();
		/**
		 * FF BF WF NF AWF
		 */
//		for(AbstractAlgorithm alg : Individual.getAlgs()) {
//			if(alg.getClass().getSimpleName().contains("AWF")) {
//				continue;
//			}
//			sb.append(alg.getClass().getSimpleName() + ",");
//		}		
//		sb.append("Greedy,Best\r\n");
		
		ArrayList<Problem> problems = new ArrayList<>();
		for(String filename : FileGetter.getFileNames(path, prefix, ".bpp")) {
//			System.out.println(count + "\t" + filename + "\t");
			if(filename.contains("AWF")) {
				continue;
			}
			Problem p = getProblem(path, filename);
			
//			p.shuffle(Parameters.random);
			
			sb.append(analyzeProblem(p));
			problems.add(p);			
		}
		StringIO.writeStringToFile(prefix + ".csv", sb.toString(), true);
		
		System.out.println("greedy " + sum);
		
//		generateHeatMapData(problems);
	}
	
	private static void generateHeatMapData(ArrayList<Problem> problems) {

		/**
		 * avg for simple heatmap note problems read in alphabetical order
		 */
		String str = "";
		for(AbstractAlgorithm alg : Individual.getAlgs()) {
			str += alg.getClass().getSimpleName() + ",";
		}
		str += "Best" + "\r\n";
		String[] alphabet = new String[]{"AWF","BF","FF","NF","WF"};
		for(int i = 0; i < 5; i++) {
			List<Problem> probs = problems.subList(i * 1000, (i + 1) * 1000 - 1);
			Class c = null;
			try {
				c  =Class.forName("bppModel." + alphabet[i]);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			str += analyzeAverageProblems(probs, c);
			str += "\r\n";
		}		
		System.out.println(str);
	}

	public static Problem getProblem(String path, String filename) {
		String[] data = StringIO.readStringsFromFile(path + filename);
		int capacity = Integer.parseInt(data[1]);
		ArrayList<Item> items = new ArrayList<>();
		for(int i = 2; i < data.length; i++) {
			Item item = new Item(Integer.parseInt(data[i]));
			items.add(item);
		}
		Problem p = new Problem(capacity, filename, items);	
		return p;
	}
	
	static double sum = 0;
	
	public static String analyzeProblem(Problem p) {
		AbstractAlgorithm bestAlg = null;
		Solution bestSolution = null;
		String str = "";
		for(AbstractAlgorithm alg : Individual.getAlgs()) {
			Solution s = new Solution(p);
			alg.packRemainingItems(s);
			double f = s.getFitness();
			if(bestAlg == null || bestSolution == null || f > bestSolution.getFitness()) {
				bestAlg = alg;
				bestSolution = s;
			}
			str += f + ",";			
//			System.out.print(s.getAverageBinWaste() + ",");	
		}
		
//		sum+=bestSolution.getFitness();
		sum+=bestSolution.getBins().size();
		
		//add greedy
		str += bestSolution.getFitness() + ",";
		
		//uncomment for class at end of row
		str = str + bestAlg.getClass().getSimpleName() + "\r\n";
		//uncomment if no best class
//		str = str.substring(0, str.length() - 1) + "\r\n";
		return (str );
	}
	
	/**
	 * returns a line averaging all the problems supplied
	 * @param PREFIX
	 * @param probs
	 * @return
	 */
	public static String analyzeAverageProblems(List<Problem> probs, Class algClass) {
		String str = "";			
		for(AbstractAlgorithm alg : Individual.getAlgs()) {			
			double avg = 0;			
			for(Problem p : probs) {
				Solution s = new Solution(p);
				alg.packRemainingItems(s);
				avg += s.getFitness();
			}
			avg /= probs.size();
			str += avg + ",";
		}
		str += algClass.getSimpleName();
		return str;
	}
}
