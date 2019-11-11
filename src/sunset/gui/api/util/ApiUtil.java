package sunset.gui.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.tree.MutableTreeNode;

import sunset.gui.api.MutableTreeNodeApiEntry;
import sunset.gui.api.spec.ApiEntry;
import sunset.gui.api.spec.Function;
import sunset.gui.api.spec.FunctionList;
import sunset.gui.api.spec.Parameter;
import sunset.gui.api.spec.Procedure;
import sunset.gui.api.spec.ProcedureList;
import sunset.gui.api.spec.Snippet;
import sunset.gui.api.spec.SnippetList;
import sunset.gui.api.spec.Type;
import sunset.gui.api.spec.TypeList;
import sunset.gui.util.StringUtil;
import sunset.gui.util.SunsetBundle;

public class ApiUtil {

	/**
	 * Representation of the entry for Tree
	 * 
	 * @param entry
	 * @return
	 */
	public static String getRepresentation(ApiEntry entry) {
		if (entry instanceof Type) {
			return entry.getName();
		} else if (entry instanceof Snippet) {
			return entry.getName();
		} else {
			Procedure proc = (Procedure) entry;
			String result = proc.getName() + "(";
			int i = 0;
			int k = 1;
			if (proc.getParameterList() != null) {
				for (Parameter param : proc.getParameterList().getParameter()) {

					if (i > 0) {
						result = result + ", ";
					}

					// j = 0;
					// types = "";
					if (param.getType().size() == 1) {
						result = result + param.getType().get(0);
					} else if (param.getType().size() > 1) {
						result = result + "Type" + k;
						k++;
					}
					i++;
				}
				if (k == 2) {
					result = result.replace("Type1", "Type");
				}
			}
			return result + ")";
		}
	}

	/**
	 * Retruns the Html Info of the entry
	 * 
	 * @param entry
	 * @return
	 */
	public static String getHTMLInfo(ApiEntry entry) {
		String def = "";
		if (entry instanceof Type) {
			def = entry.getName();
		} else if (entry instanceof Function) {
			def = getHTMLUsage((Procedure) entry,
					" : <b>" + ((Function) entry).getReturnType() + "</b>");
		} else if (entry instanceof Procedure) {
			def = getHTMLUsage((Procedure) entry, "");
		} else {
			def = entry.getName();
		}
		return "<html>"
				+ def
				+ "<br/><br/>"
				+ SunsetBundle.getInstance()
						.getProperty(entry.getDescription()) + "</html>";
	}

	/**
	 * 
	 * @param entry
	 * @return
	 */
	public static Object getTransferData(ApiEntry entry) {
		String transferData = "";
		if (entry instanceof Type) {
			transferData = " : " + entry.getName() + ";";
		} else if (entry instanceof Function) {
			transferData = getUsageCode((Procedure) entry);
		} else if (entry instanceof Procedure) {
			transferData = getUsageCode((Procedure) entry);
		} else if (entry instanceof Snippet) {
			transferData = ((Snippet) entry).getBody();
		}
		return transferData;
	}

	/**
	 * Get Usage Code
	 * 
	 * @param entry
	 * @return
	 */
	private static String getUsageCode(Procedure entry) {
		String result;
		int i = 0;
		if (StringUtil.getInstance().isBlank(entry.getBody())) {
			result = entry.getName() + "(";
			for (Parameter param : entry.getParameterList().getParameter()) {
				if (i > 0) {
					result = result + ", ";
				}
				result = result + param.getName();

				i++;
			}
			result = result + ")";
			if (!(entry instanceof Function)) {
				result = result + ";";
			}
			return result;
		} else {
			return entry.getBody();
		}
	}

	public static String getHTMLUsage(Procedure proc, String txt) {
		String result, types;
		int i = 0, j;
		int k = 1;
		List<String> typeAcronym = new ArrayList<String>();
		if (proc instanceof Function) {
			result = "<b>function</b> ";
		} else {
			result = "<b>procedure</b> ";
		}
		result = result + "<i>" + proc.getName() + "</i>(";
		if (proc.getParameterList() != null) {
			for (Parameter param : proc.getParameterList().getParameter()) {
				if (i > 0) {
					result = result + ", ";
				}
				j = 0;
				types = "";
				for (String type : param.getType()) {
					if (j == 1) {
						types = "[" + types;
					}
					if (j > 0) {
						types = types + " | ";
					}
					types = types + "<i>" + type + "</i>";
					j++;
				}
				if (j > 1) {
					types = types + "]";
				}

				result = result + "<i>" + param.getName() + "</i> : ";

				if (param.getType().size() == 1) {
					result = result + types;
				} else if (param.getType().size() > 1) {
					result = result + "<i>Type" + k + "</i>";
					typeAcronym.add("<i>Type" + k + "</i> := " + types);
					k++;
				}

				i++;
			}
		}
		result = result + ")" + txt;
		if (k > 1) {
			result = result + "<br/>";
		}
		for (String type : typeAcronym) {
			result = result + "<br/>" + type;
		}
		if (k == 2) {
			result = result.replace("Type1", "Type");
		}
		return result;
	}

	public static int addToTreeNode(MutableTreeNode node, TypeList typeList,
			int i) {
		if (typeList != null) {
			for (Type type : typeList.getType()) {
				node.insert(new MutableTreeNodeApiEntry(type), i);
				i++;
			}
		}
		return i;
	}

	public static int addToTreeNode(MutableTreeNode node,
			FunctionList functionList, int i, boolean isCustom) {
		if (functionList != null) {
			TreeMap<String, Function> map = new TreeMap<String, Function>();
			int d = 0;
			for (Function func : functionList.getFunction()) {
				map.put(func.getName() + d, func);
				d++;
			}
			for (String key : map.keySet()) {
				node.insert(
						new MutableTreeNodeApiEntry(map.get(key), isCustom), i);
				i++;
			}

		}
		return i;
	}

	public static int addToTreeNode(MutableTreeNode node,
			ProcedureList procedureList, int i, boolean isCustom) {
		if (procedureList != null) {
			TreeMap<String, Procedure> map = new TreeMap<String, Procedure>();
			int d = 0;
			for (Procedure proc : procedureList.getProcedure()) {
				map.put(proc.getName() + d, proc);
				d++;
			}
			for (String key : map.keySet()) {
				node.insert(
						new MutableTreeNodeApiEntry(map.get(key), isCustom), i);
				i++;
			}
		}
		return i;
	}

	public static int addToTreeNode(MutableTreeNode node,
			FunctionList functionList, int i) {
		return addToTreeNode(node, functionList, i, false);
	}

	public static int addToTreeNode(MutableTreeNode node,
			ProcedureList procedureList, int i) {
		return addToTreeNode(node, procedureList, i, false);
	}

	public static int addToTreeNode(MutableTreeNode node,
			SnippetList snippetList, int i) {
		return addToTreeNode(node, snippetList, i, false);
	}

	public static int addToTreeNode(MutableTreeNode node,
			SnippetList snippetList, int i, boolean isCustom) {
		if (snippetList != null) {
			for (Snippet snippet : snippetList.getSnippet()) {
				node.insert(new MutableTreeNodeApiEntry(snippet, isCustom), i);
				i++;
			}
		}
		return i;
	}

}
