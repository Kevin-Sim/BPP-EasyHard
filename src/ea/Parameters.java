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
	public static int BIN_CAPACITY = 150;
	public static Init INIT_METHOD = Init.UNIFORM;
	public static int MIN_ITEM_SIZE = 20;
	public static int MAX_ITEM_SIZE = 100;
	public static int NUM_ITEMS = 250;
	/**
	 * Used for Gaussian distribution during init with above as cut off
	 */
	private static int MEAN_ITEM_SIZE = MIN_ITEM_SIZE + (MAX_ITEM_SIZE - MIN_ITEM_SIZE) / 2;
	private static int STANDARD_DEVIATION = (MAX_ITEM_SIZE - MIN_ITEM_SIZE) / 2;	
	
	
	public static int getMEAN_ITEM_SIZE() {
		MEAN_ITEM_SIZE = MIN_ITEM_SIZE + (MAX_ITEM_SIZE - MIN_ITEM_SIZE) / 2;
		return MEAN_ITEM_SIZE;
	}

	public static int getSTANDARD_DEVIATION() {
		STANDARD_DEVIATION = (MAX_ITEM_SIZE - MIN_ITEM_SIZE) / 2;	
		return STANDARD_DEVIATION;
	}

	/*
	 *passed in from command line potentially 
	 */
	public static final int POP_SIZE = 500;
	public static Class evolovedForAlgorithm = FF.class;
	public static final int tournamentSize = 2;
	public static final int MAX_ITERATIONS = 50000;//was 1000000

	//only valid for mutation1 which isnt used
	public static final int MAX_MUTATION = 4;
	public static final double CROSSOVER_PROBABILITY = 0.99;
	public static final double MUTATION_PROBABILITY = 0.02;
	
	public static boolean EASY = true;
	private static String PREFIX = getPrefix();
	//used in replace
	public static final double DIVERSITY = 1;
	public static final boolean breakOnDiversity = false;
	public static final boolean breakOnBestUnique = true;
	//set to true true to save top best equal
	public static final boolean breakOnBestEqual = false;
	//rather than everything good which is bad as no diversity
	public static final boolean onlySaveBest = true;
	//allows to continue for maxiters if breakOnBestEqual false but still save something 
	//if no unique on bins but best falk
	public static final boolean saveBestEqual = false;
	
	public static String getPrefix() {
		String str = "";
		if(EASY) {
			str += "E_";
		}else {
			str += "H_";
		}
		str += NUM_ITEMS + "_";
//		str += BIN_CAPACITY + "_";
		if(INIT_METHOD == INIT_METHOD.UNIFORM) {
			str += "U_";
		}else {
			str += "N_";
		}
		str += "" + MIN_ITEM_SIZE + "_" + MAX_ITEM_SIZE + "_";
		return str;
	}
}
