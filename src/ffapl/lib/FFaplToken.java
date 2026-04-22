package ffapl.lib;

import ffapl.lib.interfaces.IToken;

/**
 * Represents a lightweight JavaCC Token
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplToken implements IToken {

		private String representation;
		private int column;
		private int line;
		private int kind;
		private int scope;
		
		public FFaplToken (String repr, int column, int line, int kind, int scope){
			this.representation = repr;
			this.column = column;
			this.line = line;
			this.kind = kind;
			this.scope = scope;
		}
		
		public FFaplToken(ffapl.Token token) {
			this.representation = token.image;
			this.column = token.beginColumn;
			this.line = token.beginLine;
			this.kind = token.kind;
			this.scope = token.scope;
		}

		@Override
		public int column() {
			return this.column;
		}

		@Override
		public int getKind() {
			return this.kind;
		}

		@Override
		public int line() {
			return this.line;
		}

		@Override
		public int scope() {return this.scope;}
		
		@Override
		public String toString(){
			return this.representation;
		}

}
