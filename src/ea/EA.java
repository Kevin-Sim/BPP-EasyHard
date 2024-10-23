package ea;

import java.io.File;
import java.util.ArrayList;
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
	static int numProblems = 1;
	int fileNum = 0;

	/**
	 * start with 4 args -- AlgName (WF BF FF NF AWF OR3, Weib, ORH), startProblemNumber,
	 * offset (processNumber), numProblemsPerProcess
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[] { "BF", "0", "0", "100" };
		}
		try {
			Class c = Class.forName("bppModel." + args[0]);
			Parameters.evolovedForAlgorithm = c;
			/**
			 * generate numProbs starting at startNumber + offSet * numProblems -- offset =
			 * condor process No
			 */
			numProblems = Integer.parseInt(args[3]);
			start = Integer.parseInt(args[1]) + Integer.parseInt(args[2]) * Integer.parseInt(args[3]);

		} catch (Exception e) {
			e.printStackTrace();
		}

		EA ea = new EA();
		ea.run();
	}

	@Override
	public void run() {

		fileNum = 0;
		outer: for (int i = start; i < start + numProblems;) {
			iteration = 0;
			initialise();
			Individual bestInRun = null;
			inner: while (!stopppingCondition()) {
				Individual parent1 = select();
				Individual parent2 = select();
				Individual child = crossover(parent1, parent2);
				// only swap mutation maintains distribution but doesn't evolve
//				Individual child = select();
				mutate2(child);
				calculateFitness(child);
				replace(child);
				iteration++;

				// dont need this but could stop as soon as heuristic is best (or worst)
				// as soon as best is +ve (heuristic is best stop?
				// or when best on bins? better
				// NOte possible to have worse falk but less bins when evolving hard
				if (bestInRun == null || getBest().getFitness() > bestInRun.getFitness()) {
					bestInRun = getBest().copy();
					printOutput();
					ArrayList<AbstractAlgorithm> bestOrWorstAlgs = null;
					if (bestInRun.fitness > 0) {
//						System.out.println("positive");
						if (Parameters.EASY) {
							bestOrWorstAlgs = getBestAlgs(bestInRun);
						} else {
							bestOrWorstAlgs = getWorstAlgs(bestInRun);
						}
						// possible to have worst falk but less bins could break when contains rather
						// than unique
						if (bestOrWorstAlgs.size() == 1
								&& bestOrWorstAlgs.get(0).getClass() == Parameters.evolovedForAlgorithm) {
							// break when unique best
							break inner;
						} else {
							// break when equal best
							for (AbstractAlgorithm alg : bestOrWorstAlgs) {
								if (alg.getClass() == Parameters.evolovedForAlgorithm) {
									break inner;
								}
							}

						}
					}
				}
			}

			// save all in pop that are positive ?
			// replace method does not allow duplicates but
			// this results in all instances very similar
			// Added check to see if best unique on bins
			// better to save only best
			ArrayList<Problem> problems = new ArrayList<Problem>();
			for (Individual individual : population) {
				if (individual.getFitness() > 0) {
					// check best on bins
					ArrayList<AbstractAlgorithm> bestOrWorstAlgs;
					if (Parameters.EASY) {
						bestOrWorstAlgs = getBestAlgs(individual);
					} else {
						bestOrWorstAlgs = getWorstAlgs(individual);
					}

					if (bestOrWorstAlgs.size() == 1
							&& bestOrWorstAlgs.get(0).getClass() == Parameters.evolovedForAlgorithm) {
						// add if best / worst on bins
						problems.add(individual.problem);
					} else {
						// add if best / worst on falk but equal on bins
						for (AbstractAlgorithm alg : bestOrWorstAlgs) {
							if (alg.getClass() == Parameters.evolovedForAlgorithm) {
								problems.add(individual.problem);
								// System.out.println("didnt uniquely win / lose");
								break;
							}
						}
					}
				}
			}

			// Save all if positive or just best?
			// Just best seems best as otherwise too similar
			if (problems.size() > 0) {
				problems.clear();
				problems.add(getBest().problem);
			}
			for (Problem p : problems) {
				File directory = new File("Evolved2024/" + Parameters.PREFIX + Parameters.evolovedForAlgorithm.getSimpleName());
				if (!directory.exists()) {
					directory.mkdir();
				}
				String path = directory.getAbsolutePath();
				StringIO.writeStringToFile(path + "/" + Parameters.PREFIX + Parameters.evolovedForAlgorithm.getSimpleName()
						+ "_" + NumberFormat.formatNumber(fileNum, 4) + ".bpp", p.toString(), false);
				fileNum++;
//				System.out.println("saved problem " + fileNum);
				if (fileNum == numProblems) {
					break outer;
				}
			}
//			printOutput();
		}
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
		System.out.println(
				fileNum + "\t" + NumberFormat.formatNumber(iteration, 8) + "\t" + getBest() + "\t" + getWorst());
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
				population.set(population.indexOf(individual), child);
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
	 * Swap position of two items (keeps distribution)
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

	private boolean stopppingCondition() {
		if (iteration >= Parameters.MAX_ITERATIONS) {
			return true;
		}
		return false;
	}

	private void initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.POP_SIZE; i++) {
			population.add(new Individual());
		}

	}
}
