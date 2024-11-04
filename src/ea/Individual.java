package ea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import bppModel.AWF;
import bppModel.AbstractAlgorithm;
import bppModel.BF;
import bppModel.EoC;
import bppModel.EoH;
import bppModel.EoH_IR;
import bppModel.FF;
import bppModel.Item;
import bppModel.NF;
import bppModel.FS1;
import bppModel.FS1_IR;
import bppModel.FS2;
import bppModel.FS2_IR;
import bppModel.Problem;
import bppModel.Solution;
import bppModel.WF;
import bppModel.FSW;
import bppModel.FSW_IR;
import ea.Parameters.Init;
import gp.GpAlg;
import io.ProblemReader;
import io.Serialize;
import io.StringIO;

public class Individual {
//	static ArrayList<AbstractAlgorithm> algs = getAlgs();
	public Problem problem = null;
	Class evolvedFor = null;
	Class actualBest = null;
	double fitness = -1;
	private Class actualWorst;
	
	public Individual(boolean calcFitness) {
		ArrayList<Item> items = new ArrayList<>();
		for (int i = 0; i < Parameters.NUM_ITEMS; i++) {
			Item item = null;
			if (Parameters.INIT_METHOD == Init.UNIFORM) {
				item = new Item(Parameters.MIN_ITEM_SIZE
						+ Parameters.random.nextInt(Parameters.MAX_ITEM_SIZE - Parameters.MIN_ITEM_SIZE + 1));
			} else if (Parameters.INIT_METHOD == Init.GAUSSIAN) {
				int val = -1;
				while (val < Parameters.MIN_ITEM_SIZE || val > Parameters.MAX_ITEM_SIZE) {
					val = (int) (Parameters.getMEAN_ITEM_SIZE()
							+ Parameters.getSTANDARD_DEVIATION() * Parameters.random.nextGaussian());
				}
				item = new Item(val);
			}
			items.add(item);
		}

		problem = new Problem(Parameters.BIN_CAPACITY, items);
		if(calcFitness) {
			evolvedFor = Parameters.evolovedForAlgorithm;
			getFitness();
		}
	}

	private Individual() {}

	/**
	 * used with null to create empty
	 * 
	 * @param object
	 */
	public Individual(Object object) {

	}

	public static ArrayList<AbstractAlgorithm> getAlgs() {

		ArrayList<AbstractAlgorithm> algs = new ArrayList<>();
		algs.add(new FF());
		algs.add(new BF());
		algs.add(new WF());
		algs.add(new NF());
		algs.add(new AWF());
		algs.add(new FS1());
		algs.add(new FS2());
		algs.add(new FSW());
		algs.add(new EoC());
		algs.add(new EoH());
		algs.add(new FS1_IR());
		algs.add(new FS2_IR());
		algs.add(new FSW_IR());
		algs.add(new EoH_IR());
//			algs.add(new GpAlg(Serialize.loadNode("bestInRun.node")));			

		return algs;
	}

	public Class getActualBest() {
		if (actualBest == null) {
			double best = 0;
			for (AbstractAlgorithm alg : getAlgs()) {
				Solution s = new Solution(this.problem);
				alg.packRemainingItems(s);
				double f = s.getFitness();

				if (f > best) {
					actualBest = alg.getClass();
					best = f;
				}
			}
		}
		return actualBest;
	}

	public Class getActualWorst() {
		if (actualWorst == null) {
			double worst = 1;
			for (AbstractAlgorithm alg : getAlgs()) {
				Solution s = new Solution(this.problem);
				alg.packRemainingItems(s);
				double f = s.getFitness();

				if (f < worst) {
					actualWorst = alg.getClass();
					worst = f;
				}
			}
		}
		return actualWorst;
	}

	double getFitness() {

		if (fitness == -1) {
			if (Parameters.EASY) {
				HashMap<Class, Solution> solutions = new HashMap<>();
				ArrayList<Solution> orderedSolutions = new ArrayList<>();
				double best = 0;
				for (AbstractAlgorithm alg : getAlgs()) {
					Solution s = new Solution(this.problem);
					alg.packRemainingItems(s);
					solutions.put(alg.getClass(), s);
					double f = s.getFitness();
					if (f > best) {
						actualBest = alg.getClass();
						best = f;
					}
					orderedSolutions.add(s);
				}
				Collections.sort(orderedSolutions);
				if (getActualBest() == Parameters.evolovedForAlgorithm) {
					fitness = orderedSolutions.get(0).getFitness() - orderedSolutions.get(1).getFitness();
					// StringIO.writeStringToFile("test.bpp", problem.toString(), false);
				} else {
					fitness = solutions.get(Parameters.evolovedForAlgorithm).getFitness()
							- orderedSolutions.get(0).getFitness();
				}
			} else {

				HashMap<Class, Solution> solutions = new HashMap<>();
				ArrayList<Solution> orderedSolutions = new ArrayList<>();
				double worst = 1;
				for (AbstractAlgorithm alg : getAlgs()) {
					Solution s = new Solution(this.problem);
					alg.packRemainingItems(s);
					solutions.put(alg.getClass(), s);
					double f = s.getFitness();
					if (f < worst) {
						actualWorst = alg.getClass();
						worst = f;
					}
					orderedSolutions.add(s);
				}
				Collections.sort(orderedSolutions);
				if (getActualWorst() == Parameters.evolovedForAlgorithm) {
					fitness = orderedSolutions.get(orderedSolutions.size() - 2).getFitness()
							- orderedSolutions.get(orderedSolutions.size() - 1).getFitness();
					// StringIO.writeStringToFile("test.bpp", problem.toString(), false);
				} else {
					fitness = orderedSolutions.get(orderedSolutions.size() - 1).getFitness()
							- solutions.get(Parameters.evolovedForAlgorithm).getFitness();
				}
				//
			}

		}
		return fitness;
	}

	@Override
	public String toString() {
		String str = "";
		if (problem == null || evolvedFor == null) {
			return str;
		}
		str += evolvedFor.getSimpleName() + ",";
		if (Parameters.EASY) {
			str += actualBest.getSimpleName() + ",";
		} else {
			str += actualWorst.getSimpleName() + ",";
		}
		str += fitness + ",";
//		for(Item i : problem.getItems()) {
//			str += i.getLength() + ","; 
//		}		
		return str;
	}

	public Individual copy() {
		Individual individual = new Individual(null);
		individual.actualBest = this.actualBest;
		individual.actualWorst = this.actualWorst;
		individual.evolvedFor = this.evolvedFor;
		individual.fitness = this.fitness;
		individual.problem = this.problem.copy();
		return individual;
	}

	public static void main(String[] args) {
		Problem p = ProblemReader.getProblem("C:\\Users\\Kev\\Dropbox\\Laptop\\BPPWS\\BPP-EasyHard\\WeibullNew\\WC_100N_100000\\", "WC_100N_100000_00.bpp");
		for(AbstractAlgorithm alg : getAlgs()) {
			System.out.print(alg.getClass().getSimpleName() + ", ");
		}
//		for(AbstractAlgorithm alg : getAlgs()) {
//			Solution s = new Solution(p);
//			long start = System.currentTimeMillis();
//			alg.packRemainingItems(s);
//			System.out.println(alg.getClass().getSimpleName() + "\t" + s.getBins().size() + "\t"  + (System.currentTimeMillis() - start));
//		}
	}
}
