package gpNodesArchive;

import io.Execute;
import io.Serialize;
import io.StringIO;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;



public class GP extends Observable implements Runnable{

	ArrayList<Node> population = null;
	Node bestInRun = null;
	boolean running = false;
	public int iteration = 0;

	public static void main(String[] args) {				
		GP gp = new GP();
		gp.run();
	}

	public GP() {

		
	}

	public boolean isRunning() {
		return running;
	}
	public void stop() {
		running = false;
	}

	private ArrayList<Node> reproduction(ArrayList<Node> newPopulation) {
		Node aClone = rouletteSelection();
		newPopulation.add(aClone);
		return newPopulation;
	}

	

	/**
	 * Does crossover from current pop to new pop (passed as arg)
	 * @param newPopulation
	 * @return
	 */
	private ArrayList<Node> crossover(ArrayList<Node> newPopulation) {
		ArrayList<Node> children = new ArrayList<Node>();
		Node parent1 = rouletteSelection();
		Node parent2 = rouletteSelection();
				
//		generateOutput(parent1, "parent1");
//		generateOutput(parent2, "parent2");
		
		Node child1 = null;
		Node child2 = null;
		Node point1 = parent1.getAllNodes().get(
				GpParameters.random.nextInt(parent1.getAllNodes().size()));
		Node point2 = parent2.getAllNodes().get(
				GpParameters.random.nextInt(parent2.getAllNodes().size()));

		// if the chosen point in child 1 happens to be a top node the portion to be added
		// from the second parent becomes the complete child 
		// Note Ids are only used for GraphViz atm so only need be unique for 
		// all nodes for a particular member in the population 
		if (point1.getParentNode() == null) {
			child1 = point2.copy(null);			
			child1.reset();
		}else{
			child1 = parent1;
			Node parentNode = point1.getParentNode();								
			int point1Idx = parentNode.getChildNodes().indexOf(point1);
			parentNode.getChildNodes().set(point1Idx, point2);					
			child1.reset();
		}		
		if(child1.getMaxDepth() <= GpParameters.maximumRecombinationDepth){
			children.add(child1);
		}
		
		//repeat for other child
		if (point2.getParentNode() == null) {
			child2 = point1.copy(null);			
			child2.reset();
		}else{
			child2 = parent2;
			Node parentNode = point2.getParentNode();								
			int point2Idx = parentNode.getChildNodes().indexOf(point2);
			parentNode.getChildNodes().set(point2Idx, point1);					
			child2.reset();
		}		
		
		if(child2.getMaxDepth() <= GpParameters.maximumRecombinationDepth){
			children.add(child2);
		}		
		
		newPopulation.addAll(children);	
		
		//generateOutput(child1, "child1");
		//generateOutput(child1, "child2");
		//System.exit(-1);
		return newPopulation;
	}

	private void generateOutput(Node node, String filePrefix) {
		
		Serialize.saveNode(node, filePrefix + ".node");
		String fileName = filePrefix + ".gv";
		String svgFilename = filePrefix + ".svg";
		String pdfFilename = filePrefix + ".pdf";
		String pngFilename = filePrefix + ".png";
		StringIO.writeStringToFile(fileName, node.toGraphviz(), false);
		// dot -Tsvg -o 1.svg 1.gv
		// dot -Tpdf -o 1.pdf 1.gv
//		Execute.startApp("dot", null, false, new String[] { "-Tsvg", "-o",
//				svgFilename, fileName });
//		Execute.startApp("dot", null, true, new String[] { "-Tpdf", "-o",
//				pdfFilename, fileName });
//		Execute.startApp("dot", null, true, new String[] { "-Tpng", "-o",
//				pngFilename, fileName });
//		Execute.openFile(pdfFilename);
		
	}

	private Node getBest() {
		Node best = population.get(0);
		double bestFitness = best.getFitness();
		for (Node n : population) {
			if (n.getFitness() > bestFitness) {
				bestFitness = n.getFitness();
				best = n;
			}
		}
		return best;
	}

	private void initialise() {
		population = new ArrayList<Node>();
		Node node = null;
		Random rnd = GpParameters.random;
		int probsPerDepth = GpParameters.popSize
				/ ((GpParameters.maximumInitialDepth - 1) * 2);
		for (int depth = 2; depth < GpParameters.maximumInitialDepth; depth++) {
			for (int i = 0; i < probsPerDepth; i++) {
				node = NodeFactory.buildIndividualFullMethod(depth);
				population.add(node);
				node = NodeFactory.buildIndividualGrowMethod(depth);
				population.add(node);
			}
		}
		while (population.size() < GpParameters.popSize) {
			if (rnd.nextDouble() < 0.5) {
				node = NodeFactory.buildIndividualFullMethod(1 + rnd
						.nextInt(GpParameters.maximumInitialDepth));
			} else {
				node = NodeFactory.buildIndividualGrowMethod(1 + rnd
						.nextInt(GpParameters.maximumInitialDepth));
			}
			population.add(node);
		}
	}

	/**
	 * Return a COPY of the selected node from the population
	 * @return
	 */
	private Node rouletteSelection() {
		//need to invert the fitness max = num test carts * max steps
		//raw fitness = number of steps = lowest best
		//use  (2*maxstep) - fitness to give larger for fitness = 0 
		//but still a chance for worst (largest) fitness
		
//		int worstFitness = GpParameters.maximumSteps * Parameters.getTestCarts().size();
		
		Node selectedNode = null;
		double totalFitness = 0;
		for (Node n : population) {
			totalFitness +=  n.getFitness();
		}
		
		int val = GpParameters.random.nextInt((int)totalFitness + 1);
		int cumulativeVal = 0;
		int selectedIdx = -1;
		do {
			selectedIdx++;			
			try {
				cumulativeVal += population.get(selectedIdx).getFitness();
			} catch (Exception e) {
				
				e.printStackTrace();
			}

		} while (cumulativeVal <= val && selectedIdx < population.size() - 1);
		if(selectedIdx >= population.size()){
			selectedIdx = population.size() - 1;
		}
		selectedNode = population.get(selectedIdx);		
		Node copy = selectedNode.copy(null);
		return copy;
	}

	@Override
	public void run() {
		//on google OR3 data set BF 4240	GoogleOR3 4149

		initialise();
		bestInRun = getBest();
		Node bestInGeneration = bestInRun;
		ArrayList<Node> newPopulation = null;
//		generateOutput(bestInRun, "bestInRun");
		running = true;
		for(iteration = 0; iteration < GpParameters.numberOfGenerations; iteration++){
			newPopulation = new ArrayList<Node>();
			while (newPopulation.size() < GpParameters.popSize) {
				if (GpParameters.random.nextDouble() < GpParameters.crossoverProbability) {
					newPopulation = crossover(newPopulation);
				} else {
					newPopulation = reproduction(newPopulation);
				}
			}
			while (newPopulation.size() > GpParameters.popSize) {
				newPopulation.remove(newPopulation.size() - 1);
			}
			population = newPopulation;
			bestInGeneration = getBest();
			
			if (bestInGeneration.getFitness() > bestInRun.getFitness()) {
				bestInRun = bestInGeneration.copy(null);					
			}
			
			setChanged();
			notifyObservers();
			
			System.out.print(iteration + "\tBest = " + bestInRun.getFitness() + "\t" + bestInRun.getTotalBins());								
			System.out.print("\tGeneration Best = " + bestInGeneration.getFitness());	
			System.out.print("\tBenchmark for " + GpParameters.algorithmClass.getSimpleName() + " " + GpParameters.totalBenchmarkFitness);
			System.out.println();
			if(!running) {
				break;
			}
			
		}
		running = false;		
		generateOutput(bestInRun, "bestInRun");
	}
	
	@Override
	public String toString() {
		return "" + iteration + " " + bestInRun.getFitness(); 
	}
}
