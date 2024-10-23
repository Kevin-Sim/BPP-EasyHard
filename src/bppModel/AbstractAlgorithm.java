package bppModel;

public abstract class AbstractAlgorithm {
	
	public Solution packRemainingItems(Solution solution) {
		while (solution.hasRemainingItems()) {
			packNextItem(solution);
		}
		return solution;
	}

	/**
	 * Abstract Method implemented by each heuristic that is responsible for
	 * creating a new bin via the {@link #addBin(Solution)} Method, packing
	 * items from the {@link Solution} remainingItems Array List
	 * 
	 * @param solution
	 * @return
	 */
	public abstract void packNextItem(Solution solution);

	/**
	 * Bins should be added to a {@link Solution} via the {@code public Bin
	 * packNextBin(Solution solution)} method inherited by all heuristics from
	 * the {@link AbstractAlgorithm} class
	 * 
	 * @param solution
	 *            The {@link Solution to be added to}
	 * @return The newly added (empty) {@link Bin}
	 */
	public Bin addBin(Solution solution) {
		Bin bin = new Bin(solution.getProblem().getPr_capacity());
		solution.getBins().add(bin);		
		return bin;
	}
}
