package gp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import ea.Parameters;



public class NodeFactory {
	
	public static void main(String[] args) {
		for(Class c : Node.allNodeTypes()) {
			System.out.println(c);
		}
	}

	static Random rnd = GpParameters.random;

	/**
	 * Return a random node from the joined set of terminal and function nodes
	 * 
	 * @return
	 */
	
	private static Node getRandomNode() {
		Node node = null;
		Class clazz = Node.allNodeTypes().get(
				rnd.nextInt(Node.allNodeTypes().size()));

		try {
			Constructor c = clazz.getConstructor(Node.class);
			node = (Node) c.newInstance(new Object[] { null });
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

		return node;
	}

	private static Node getRandomFunctionNode() {
		Node node = null;
		Class clazz = Node.getFunctionNodeTypes().get(
				rnd.nextInt(Node.getFunctionNodeTypes().size()));
		try {
			// String className = "gpTree." + type.getClass().toString();
			// Class clazz = Class.forName(className);
			Constructor c = clazz.getConstructor(Node.class);
			node = (Node) c.newInstance(new Object[] { null });
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

		return node;
	}

	
	private static Node getRandomTerminalNode() {
		Node node = null;
		Class clazz = Node.getTerminalNodeTypes().get(
				rnd.nextInt(Node.getTerminalNodeTypes().size()));

		try {
			// String className = "gpTree." + type.getClass().toString();
			// Class clazz = Class.forName(className);
			Constructor c = clazz.getConstructor(Node.class);
			node = (Node) c.newInstance(new Object[] { null });
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
		return node;
	}

	/**
	 * Recursive automatically terminates when parentNode has no children...
	 * Terminal node
	 * 
	 * @param parentNode
	 * @param depth 
	 * @param depth 
	 * @return
	 */
	public static Node buildSubTreeGrowMethod(Node parentNode, int depth) {
		int level = parentNode.getLevel() + 1;
		for (int i = 0; i < parentNode.getNumberOfChildren(); i++) {
			Node childNode = null;
			if (level < depth) {
				childNode = getRandomNode();
			} else {
				childNode = getRandomTerminalNode();
			}
			childNode.setLevel(parentNode.getLevel() + 1);
			childNode.setParentNode(parentNode);
			parentNode.getChildNodes().add(childNode);
			childNode = buildSubTreeGrowMethod(childNode, depth);
		}
		//System.out.println(parentNode);
		return parentNode;
	}

	/**
	 * Generates a node with variable length branches up 
	 * to the maximum length set in Parameters
	 * @param depth 
	 * 
	 * @return
	 */
	public static Node buildIndividualGrowMethod(int depth) {
		Node topNode = getRandomFunctionNode();
		topNode.setLevel(0);
		topNode.setParentNode(null);
		topNode = buildSubTreeGrowMethod(topNode, depth);
		return topNode;
	}
	
	/**
	 * Generates a node with set length branches of 
	 * length as specified in Parameters
	 * @param depth 
	 * 
	 * @return
	 */
	public static Node  buildIndividualFullMethod(int depth) {
		Node topNode = getRandomFunctionNode();
		topNode.setLevel(0);
		topNode.setParentNode(null);
		topNode = buildSubTreeFullMethod(topNode, depth);
		return topNode;
	}
	
	
	/**
	 * Generates a subtree from the given node 
	 * with variable length branches up 
	 * to the maximum length given by depth
	 * 
	 * @param depth 
	 * 
	 * @return
	 */
	private static Node buildSubTreeFullMethod(Node parentNode, int depth) {
		int level = parentNode.getLevel() + 1;
		for (int i = 0; i < parentNode.getNumberOfChildren(); i++) {
			Node childNode = null;
			if (level < depth) {
				childNode = getRandomFunctionNode();
			} else {
				childNode = getRandomTerminalNode();
			}
			childNode.setLevel(parentNode.getLevel() + 1);
			childNode.setParentNode(parentNode);
			parentNode.getChildNodes().add(childNode);
			childNode = buildSubTreeFullMethod(childNode, depth);
		}
		//System.out.println(parentNode);
		return parentNode;
	}
}
