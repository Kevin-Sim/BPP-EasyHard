package ea;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import bppModel.AbstractAlgorithm;
import bppModel.Item;
import bppModel.Problem;
import bppModel.Solution;
import io.NumberFormat;
import io.ProblemReader;
import io.StringIO;

/**
 * 
 * @author 40004938
 *
 */
public class EA implements Runnable {

	ArrayList<Individual> population = null;
	static int iteration = 0;
	static int start = 0;
	static int end = 0;
	int fileNum = 0;
	Individual bestInRun = null;

	/**
	 * start with 3 args -- AlgName (FF, BF, WF, NF, AWF, FS1, FS2, FSW, EoC, EoH),
	 * startProblemNumber, endProblemNumber + 1
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			//
			args = new String[] { "WF", "9", "100" };
		}
		try {
			Class c = Class.forName("bppModel." + args[0]);
			Parameters.evolovedForAlgorithm = c;
			/**
			 * generate numProbs starting at startNumber + offSet * numProblems -- offset =
			 * condor process No
			 */
			end = Integer.parseInt(args[2]);
			start = Integer.parseInt(args[1]);
	
		} catch (Exception e) {
			e.printStackTrace();
		}

		EA ea = new EA();
		ea.run();
	}

	@Override
	public void run() {

		fileNum = start;
		while (fileNum < end) {
			iteration = 0;
			initialise();
			bestInRun = null;
			while (!stopppingCondition()) {
				Individual parent1 = select();
				Individual parent2 = select();
				Individual child = crossover(parent1, parent2);
//				mutate1(child);
				mutate2(child);
//				mutate3(child);
				calculateFitness(child);
				replace(child);
				iteration++;

				if (bestInRun == null || getBest().getFitness() > bestInRun.getFitness()) {
					bestInRun = getBest().copy();
					printOutput();
				}
			}
			if(saveFiles() >= end) {
				break;
			}
		}
	}

	private int saveFiles() {
		ArrayList<Problem> problems = new ArrayList<Problem>();
		ArrayList<Individual>  individuals = new ArrayList<Individual>();
		for (Individual individual : population) {
			if (individual.getFitness() > 0) {
				// check best on bins
				ArrayList<AbstractAlgorithm> bestOrWorstAlgs;
				if (Parameters.EASY) {
					bestOrWorstAlgs = getBestAlgs(individual);
				} else {
					bestOrWorstAlgs = getWorstAlgs(individual);
				}

				//probably add params ?
				if (bestOrWorstAlgs.size() == 1
						&& bestOrWorstAlgs.get(0).getClass() == Parameters.evolovedForAlgorithm) {
					// add if best / worst on bins
					problems.add(individual.problem);
					individuals.add(individual);
				} else {
					// add if best / worst on falk but equal on bins
					for (AbstractAlgorithm alg : bestOrWorstAlgs) {
						if (alg.getClass() == Parameters.evolovedForAlgorithm) {
							if(Parameters.saveBestEqual) {
								problems.add(individual.problem);
								individuals.add(individual);
							}							
						}
					}
				}
			}
		}

		// Save all if positive or just best?
		// Just best seems best as otherwise too similar
		// However if Diversity < 0.2 OK
		Individual bestIndividual = null;
		for(Individual ind : individuals) {
			if(bestIndividual == null || ind.getFitness() > bestIndividual.getFitness()) {
				bestIndividual = ind;
			}
		}
		if (problems.size() > 0 && Parameters.onlySaveBest) {
			// diversity?
//			Problem p = problems.remove(0);
//			System.out.println(Arrays.toString(p.getDiversity(p, problems)));
			problems.clear();
			problems.add(bestIndividual.problem);
		}
		for (Problem p : problems) {
			File directory = new File(
					"Instances2024\\Evolved\\" + Parameters.getPrefix() + Parameters.evolovedForAlgorithm.getSimpleName());
			if (!directory.exists()) {
				directory.mkdirs();
			}
			String path = directory.getAbsolutePath();
			StringIO.writeStringToFile(path + "/" + Parameters.getPrefix() + Parameters.evolovedForAlgorithm.getSimpleName()
					+ "_" + NumberFormat.formatNumber(fileNum, 4) + ".bpp", p.toString(), false);
			fileNum++;
			if(fileNum >= end) {
				return fileNum;
			}
		}
		return fileNum;
	}

	private ArrayList<Problem> getProblems() {
		ArrayList<Problem> problems = new ArrayList<Problem>();
		for (Individual ind : population) {
			problems.add(ind.problem);
		}
		return problems;
	}

	/**
	 * In terms of bins
	 * 
	 * @param individual
	 * @return
	 */
	private ArrayList<AbstractAlgorithm> getBestAlgs(Individual individual) {
		ArrayList<AbstractAlgorithm> bestAlgs = new ArrayList<AbstractAlgorithm>();
		int bestBins = Integer.MAX_VALUE;
		for (AbstractAlgorithm alg : Individual.getAlgs()) {
			Solution s = new Solution(individual.problem);
			alg.packRemainingItems(s);
			if (s.getBins().size() < bestBins) {
				bestBins = s.getBins().size();
				bestAlgs.clear();
				bestAlgs.add(alg);
			} else if (s.getBins().size() == bestBins) {
				bestAlgs.add(alg);
			}
		}
		return bestAlgs;
	}

	/**
	 * in terms of bins
	 * 
	 * @param individual
	 * @return
	 */
	private ArrayList<AbstractAlgorithm> getWorstAlgs(Individual individual) {
		ArrayList<AbstractAlgorithm> worstAlgs = new ArrayList<AbstractAlgorithm>();
		int worstBins = Integer.MIN_VALUE;
		for (AbstractAlgorithm alg : Individual.getAlgs()) {
			Solution s = new Solution(individual.problem);
			alg.packRemainingItems(s);
//			System.out.println(alg.getClass() + "\t" + s.getBins().size() + "\t" + s.getFitness());
			if (s.getBins().size() > worstBins) {
				worstBins = s.getBins().size();
				worstAlgs.clear();
				worstAlgs.add(alg);
			} else if (s.getBins().size() == worstBins) {
				worstAlgs.add(alg);
			}
		}
		return worstAlgs;
	}

	private void printOutput() {
		ArrayList<Problem> probs = getProblems();
		Problem p = probs.remove(0);
		System.out.println(fileNum + "\t" + NumberFormat.formatNumber(iteration, 8) + "\t"
				+ p.getAverageDiversity(probs) + "\t" + getBest() + "\t" + getWorst());
	}

	/**
	 * changed to not allow duplicates
	 * 
	 * @param child
	 */
	private void replace(Individual child) {
		Individual individual = getWorst();
		if (child.getFitness() > individual.getFitness()) {
			boolean exists = false;
			for (Individual ind : population) {
				if (ind.problem.equals(child.problem)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				if(Parameters.DIVERSITY == 1) {
					population.set(population.indexOf(individual), child);
				}else if(child.problem.getAverageDiversity(getProblems()) < Parameters.DIVERSITY) {
					population.set(population.indexOf(individual), child);
				}
			}
		}
	}

	private Individual getWorst() {
		Individual worst = null;
		for (Individual individual : population) {
			if (worst == null || individual.getFitness() < worst.getFitness()) {
				worst = individual;
			}
		}
		return worst;
	}

	private Individual getBest() {
		Individual best = null;
		for (Individual individual : population) {
			if (best == null || individual.getFitness() > best.getFitness()) {
				best = individual;
			}
		}
		return best;
	}

	/**
	 * adjust one allele by a small value (changes distribution)
	 * 
	 * @param child
	 */
	private void mutate1(Individual child) {
		if (Parameters.random.nextDouble() > Parameters.MUTATION_PROBABILITY) {
			return;
		}
		int idx = Parameters.random.nextInt(Parameters.NUM_ITEMS);
		Item item = child.problem.getItems().get(idx);
		int mutation = Parameters.random.nextInt(Parameters.MAX_MUTATION);
		if (Parameters.random.nextBoolean()) {
			mutation *= -1;
		}
		int len = item.getLength() + mutation;
		if (len > Parameters.MAX_ITEM_SIZE || len <= Parameters.MIN_ITEM_SIZE) {
			len = Parameters.MIN_ITEM_SIZE
					+ Parameters.random.nextInt(Parameters.MAX_ITEM_SIZE - Parameters.MIN_ITEM_SIZE + 1);
		}
		item.setLength(len);
	}

	/**
	 * Swap position of two items (keeps distribution if no crossover)
	 * 
	 * @param child
	 */
	private void mutate2(Individual child) {
		if (Parameters.random.nextDouble() > Parameters.MUTATION_PROBABILITY) {
			return;
		}
		int idx1 = Parameters.random.nextInt(Parameters.NUM_ITEMS);
		int idx2 = Parameters.random.nextInt(Parameters.NUM_ITEMS);
		Item itemTemp = child.problem.getItems().get(idx2);
		child.problem.getItems().set(idx2, child.problem.getItems().get(idx1));
		child.problem.getItems().set(idx1, itemTemp);
	}

	/**
	 * Move random block
	 * (keeps distribution if no crossover)
	 * 
	 * @param child
	 */
	private void mutate3(Individual child) {
		if (Parameters.random.nextDouble() > Parameters.MUTATION_PROBABILITY) {
			return;
		}
		int idx1 = Parameters.random.nextInt(Parameters.NUM_ITEMS);
		int idx2 = Parameters.random.nextInt(Parameters.NUM_ITEMS);
		if (idx1 > idx2) {
			int temp = idx1;
			idx1 = idx2;
			idx2 = temp;
		}
		ArrayList<Item> removedItems = new ArrayList<Item>();
		for (int i = idx1; i < idx2; i++) {
			removedItems.add(child.problem.getItems().get(i));
		}
		
		child.problem.getItems().removeAll(removedItems);
		child.problem.getItems().addAll(Parameters.random.nextInt(child.problem.getItems().size()), removedItems);
	}

	private void calculateFitness(Individual child) {
		child.getFitness();
	}

	private Individual crossover(Individual parent1, Individual parent2) {
		if (Parameters.random.nextDouble() > Parameters.CROSSOVER_PROBABILITY) {
			return parent1.copy();
		}

		Individual child = new Individual(null);
		child.evolvedFor = Parameters.evolovedForAlgorithm;
		child.problem = new Problem(Parameters.BIN_CAPACITY, new ArrayList<>());
		int idx = Parameters.random.nextInt(Parameters.NUM_ITEMS);
		for (int i = 0; i < idx; i++) {
			child.problem.getItems().add(parent1.problem.getItems().get(i));
		}

		for (int i = idx; i < Parameters.NUM_ITEMS; i++) {
			child.problem.getItems().add(parent2.problem.getItems().get(i));
		}

//		for(int i = 0; i < Parameters.NUM_ITEMS; i++) {
//			if(Parameters.random.nextBoolean()) {
//				child.problem.getItems().add(parent1.problem.getItems().get(i));
//			}else {
//				child.problem.getItems().add(parent2.problem.getItems().get(i));
//			}
//		}
		return child;
	}

	private Individual select() {
		Individual individual = population.get(Parameters.random.nextInt(population.size()));
		for (int i = 0; i < Parameters.tournamentSize; i++) {
			Individual candidate = population.get(Parameters.random.nextInt(population.size()));
			if (candidate.getFitness() > individual.getFitness()) {
				individual = candidate;
			}
		}
		return individual.copy();
	}

	// put other criteria in here also
	private boolean stopppingCondition() {
		if (iteration >= Parameters.MAX_ITERATIONS) {
			return true;
		}
		if (bestInRun == null) {
			return false;
		}

		ArrayList<AbstractAlgorithm> bestOrWorstAlgs = null;
		if (bestInRun.fitness > 0) {
//			System.out.println("positive");
			if (Parameters.EASY) {
				bestOrWorstAlgs = getBestAlgs(bestInRun);
			} else {
				bestOrWorstAlgs = getWorstAlgs(bestInRun);
			}
			// possible to have worst falk but less bins could break when contains rather
			// than unique. Havent managed best unique on Hard ORH
			if (Parameters.breakOnBestUnique) {
				if (bestOrWorstAlgs.size() == 1
						&& bestOrWorstAlgs.get(0).getClass() == Parameters.evolovedForAlgorithm) {
//				 break when unique best
					return true;
				}
			}
			if (Parameters.breakOnBestEqual) {
				// break when equal best. Haven't managed best unique on Hard ORH
				for (AbstractAlgorithm alg : bestOrWorstAlgs) {
					if (alg.getClass() == Parameters.evolovedForAlgorithm) {
						return true;
					}
				}
			}
		}

		if (Parameters.DIVERSITY == 1) {
			return false;
		}
		ArrayList<Problem> probs = getProblems();

		if (probs.remove(0).getAverageDiversity(probs) > Parameters.DIVERSITY) {
//			System.out.println("losing diversity");
			if(Parameters.breakOnDiversity) {
				return true;
			}
		}
		return false;
	}

	private void initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.POP_SIZE; i++) {
			population.add(new Individual(false));
		}

	}
}
