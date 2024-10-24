package ea;

import java.util.ArrayList;
import java.util.Random;

import bppModel.AWF;
import bppModel.AbstractAlgorithm;
import bppModel.BF;
import bppModel.FF;
import bppModel.NF;
import bppModel.WF;

public class Parameters {
	public enum Init{
		UNIFORM, GAUSSIAN 
	}
	public static final Random random = new Random();
	public static final int BIN_CAPACITY = 150;
	public static final Init INIT_METHOD = Init.GAUSSIAN;
	public static final int MIN_ITEM_SIZE = 40;
	public static final int MAX_ITEM_SIZE = 60;
	/**
	 * Used for Gaussian distribution during init with above as cut off
	 */
	public static final int MEAN_ITEM_SIZE = MIN_ITEM_SIZE + (MAX_ITEM_SIZE - MIN_ITEM_SIZE) / 2;
	public static final int STANDARD_DEVIATION = (MAX_ITEM_SIZE - MIN_ITEM_SIZE) / 2;	
	public static final int NUM_ITEMS = 120;
	/*
	 *passed in from command line potentially 
	 */
	public static final int POP_SIZE = 500;
	public static Class evolovedForAlgorithm = FF.class;
	public static final int tournamentSize = 2;
	public static final int MAX_ITERATIONS = 1000000;//was 1000000

	//only valid for mutation1 which isnt used
	public static final int MAX_MUTATION = 4;
	public static final double CROSSOVER_PROBABILITY = 0.99;
	public static final double MUTATION_PROBABILITY = 0.02;
	
	public static final boolean EASY = true;
	public static final String PREFIX = getPrefix();
	//used in replace
	public static final double DIVERSITY = 1;
	public static final boolean breakOnDiversity = false;
	public static final boolean breakOnBestUnique = true;
	//set to true true to save top best equal
	public static final boolean breakOnBestEqual = false;
	public static final boolean onlySaveBest = true;
	//allows to continue for maxiters but still save something if no unique on bins
	public static final boolean saveBestEqual = true;
	
	private static String getPrefix() {
		String str = "";
		if(EASY) {
			str += "E_";
		}else {
			str += "H_";
		}
		str += NUM_ITEMS + "_";
		if(INIT_METHOD == INIT_METHOD.UNIFORM) {
			str += "U_";
		}else {
			str += "N_";
		}
		str += "" + MIN_ITEM_SIZE + "_" + MAX_ITEM_SIZE + "_";
		return str;
	}
}
