package ffapl.utils;

import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;

public class FFaplSymbolTablePrinter {

	
	public static void printSymbolTable(ISymbolTable symbolTable){
		
		print(symbolTable.root());
	}
	
	private static void print(ISymbol symbol){
		ISymbol tmp = symbol;
		while(tmp != null){
			printout(tmp);
			if(tmp.local() != null){
				print(tmp.local());
			}
			
			tmp = tmp.next();
		}
	}
	
	private static void printout(ISymbol symbol){
		String temp ="";
		for(int i = symbol.level(); i > 0; i--){
			temp = temp + ".";
		}
		System.out.println(temp + symbol.getName() + " - " + symbol.getKindStr()
				 + " - " + symbol.isGlobal() + " - " + symbol.getType() + " - " + symbol.scope());
	}
	
}
