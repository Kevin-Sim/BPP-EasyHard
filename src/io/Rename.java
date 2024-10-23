package io;

import java.io.File;

import bppModel.AbstractAlgorithm;
import ea.Individual;

public class Rename {

	public static void main(String[] args) {
		String path = "./InstancesNew/E_120/";
		for(AbstractAlgorithm alg : Individual.getAlgs()) {			
			String prefix = alg.getClass().getSimpleName();
			for(int i = 0; i < 1000; i++) {
				String oldFilename = path + prefix + NumberFormat.formatNumber(i, 4) + ".bpp";
				File oldFile = new File(oldFilename);
				File newFile = new File(path + "E_120_" + prefix + NumberFormat.formatNumber(i, 4) + ".bpp");				
				oldFile.renameTo(newFile);
			}			
		}
	}
}
