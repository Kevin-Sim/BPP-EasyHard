package gp;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import bppModel.AbstractAlgorithm;
import bppModel.Problem;
import bppModel.Solution;

/**
 * 
 * @author cs378
 *
 *         Subclasses are terminal and function both also abstract
 * 
 */
public abstract class Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int id = 0;
	public int ID;

	private Node parentNode = null;
	private ArrayList<Node> childNodes = new ArrayList<Node>();
	private int numberOfChildren = -1;
	private String graphSymbol = "?";
	private int level = 0;
	private double fitness = -1.0;

	public Node(Node parentNode) {
		this.parentNode = parentNode;// set in factory or copy operation
		id++;
		this.ID = id;
	}

	public abstract Double eval();

	public String toGraphviz() {
		String result = "";
		if (getParentNode() == null) {
			result += "graph{\r\n";
		}
		result += ID + "[label=\"" + graphSymbol + "\"];\r\n";
		for (int i = 0; i < numberOfChildren; i++) {
			result += ID + " -- " + getChildNodes().get(i).ID + "\r\n";
			result += getChildNodes().get(i).toGraphviz();
		}
		if (getParentNode() == null) {
			result += "}\r\n";
		}
		return result;
	}

	/**
	 * The imidiate children of this node
	 * 
	 * @return
	 */
	public ArrayList<Node> getChildNodes() {
		return childNodes;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public void setChildNodes(ArrayList<Node> childNodes) {
		this.childNodes = childNodes;
	}

	public static ArrayList<Class> allNodeTypes() {
		ArrayList<Class> result = new ArrayList<Class>();
		result.addAll(getTerminalNodeTypes());
		result.addAll(getFunctionNodeTypes());
		return result;
	}

	public static ArrayList<Class> getFunctionNodeTypes() {
		ArrayList<Class> result = new ArrayList<Class>();
		try {
			Class[] classes = getClasses("gp");
			for (Class c : classes) {
				if (c.getSuperclass() == FunctionNode.class) {
					result.add(c);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<Class> getTerminalNodeTypes() {
		ArrayList<Class> result = new ArrayList<Class>();
		try {
			Class[] classes = getClasses("gp");
			for (Class c : classes) {
				if (c.getSuperclass() == TerminalNode.class) {
					result.add(c);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		// if eclipse
		if (resources.hasMoreElements() && GpParameters.jarName.equals("")) {
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			ArrayList<Class> classes = new ArrayList<Class>();
			for (File directory : dirs) {
				// System.out.println("hi " + directory);
				classes.addAll(findClasses(directory, packageName));
			}
			Class[] result = classes.toArray(new Class[classes.size()]);
			return result;
		} else {
			// Its a Jar
			URL currentPath = new URL(
					"jar:file:" + System.getProperty("user.dir") + "\\" + GpParameters.jarName + "!/");

			// System.out.println("Its a Jar -- " + currentPath);
			URL[] urls = { currentPath };
			URLClassLoader urlClassLoader = new URLClassLoader(urls, classLoader);
			JarFile jf = new JarFile(GpParameters.jarName);

			Enumeration allEntries = jf.entries();
			ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
			int count = 0;
			while (allEntries.hasMoreElements()) {
				JarEntry entry = (JarEntry) allEntries.nextElement();
				if (entry.getName().startsWith("gp/") && entry.getName().endsWith(".class")) {
					Class<?> c = urlClassLoader.loadClass(entry.getName().replace('/', '.').replace(".class", ""));
					classes.add(c);
					count++;
				}

			}
			Class[] result = classes.toArray(new Class[classes.size()]);
			return result;
		}
	}

	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			System.out.println("No dir " + directory);
			System.exit(-1);
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		// System.out.println("class size " + classes.size());
		return classes;
	}

//	public static ArrayList<Class> getFunctionNodeTypes() {
//		ArrayList<Class> result = new ArrayList<Class>();
//		result.add(AddFunctionNode.class);	
//		return result;
//	}
//
//	
//	public static ArrayList<Class> getTerminalNodeTypes() {
//		ArrayList<Class> result = new ArrayList<Class>();		
//		result.add(BinSizeTerminalNode.class);
//		return result;
//	}	

	/**
	 * All nodes below this node
	 * 
	 * @return
	 */
	public ArrayList<Node> getAllSubNodes() {
		ArrayList<Node> subNodes = new ArrayList<Node>();
		for (Node childNode : getChildNodes()) {
			subNodes.add(childNode);
			subNodes.addAll(childNode.getAllSubNodes());
		}
		return subNodes;
	}

	/**
	 * All nodes including this node
	 * 
	 * @return
	 */
	public ArrayList<Node> getAllNodes() {
		ArrayList<Node> allNodes = new ArrayList<Node>();
		allNodes.add(this);
		for (Node childNode : getChildNodes()) {
			allNodes.add(childNode);
			allNodes.addAll(childNode.getAllSubNodes());
		}
		return allNodes;
	}

	public int getMaxDepth() {
		int maxDepth = 0;
		for (Node node : getAllSubNodes()) {
			if (node.level > maxDepth) {
				maxDepth = node.level;
			}
		}
		return maxDepth;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		String str = getClass().getSimpleName() + " : " + getLevel() + "\r\n";
		for (int i = 0; i < numberOfChildren; i++) {
			str += getChildNodes().get(i).toString();
		}
		return str;
	}

	public String getGraphSymbol() {
		return graphSymbol;
	}

	public void setGraphSymbol(String graphSymbol) {
		this.graphSymbol = graphSymbol;
	}

	public int getNewID() {
		id++;
		this.ID = id;
		return id;
	}

	public void reset() {
		for (Node node : getAllNodes()) {
			node.getNewID();
		}
		resetDepth(0);
	}

	private void resetDepth(int depth) {
		if (depth == 0) {
			setParentNode(null);
		}
		setLevel(depth);
		for (Node n : getChildNodes()) {
			n.resetDepth(depth + 1);
			n.setParentNode(this);
		}
	}

	public Node copy(Node parentNode) {
		Node copiedNode = null;

		try {
			Constructor c = this.getClass().getConstructor(Node.class);
			copiedNode = (Node) c.newInstance(new Object[] { parentNode });
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		copiedNode.fitness = fitness;
		copiedNode.setParentNode(parentNode);
		if (parentNode != null) {
			copiedNode.setLevel(parentNode.getLevel() + 1);
		}
		for (Node node : childNodes) {
			copiedNode.getChildNodes().add(node.copy(copiedNode));
		}
		return copiedNode;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		if (fitness == -1) {
			double d = 0;
			AbstractAlgorithm alg = new GpAlg(this);
			for (Problem p : GpParameters.problems) {
				Solution s = new Solution(p);
				alg.packRemainingItems(s);
				d += s.getFitness();
			}
			fitness = d;
		}
		return fitness;
	}

	public int getTotalBins() {

		int d = 0;
		AbstractAlgorithm alg = new GpAlg(this);
		for (Problem p : GpParameters.problems) {
			Solution s = new Solution(p);
			alg.packRemainingItems(s);
			d += s.getBins().size();
		}
		return d;
	}
}
