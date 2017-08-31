package ffapl.ast;

/**
 * Tree constants
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface FFaplASTreeConstants
{
  public final int FFT_RELOP = 0;
  public final int FFT_EQUALOP = 1;
  public final int FFT_ADDOP = 2;
  public final int FFT_MULOP = 3;
  public final int FFT_RANDOM = 4;
  public final int FFT_RANDOMGENERATOR = 5;
  public final int FFT_LITERAL = 6;
  public final int FFT_SELECTOR = 7;
  public final int FFT_ARRAYLEN = 8;
  public final int FFT_PRIMARYEXPR = 9;
  public final int FFT_UNARYEXPR = 10;
  public final int FFT_POWEXPR = 11;
  public final int FFT_MULEXPR = 12;
  public final int FFT_ADDEXPR = 13;
  public final int FFT_RELEXPR = 14;
  public final int FFT_EQUALEXPR = 15;
  public final int FFT_CONDANDEXPR = 16;
  public final int FFT_CREATIONEXPR = 17;
  public final int FFT_EXPR = 18;
  public final int FFT_VOID = 19;
  public final int FFT_PROCFUNCCALL = 20;
  public final int FFT_ASSIGNMENT = 21;
  public final int FFT_CONDITION = 22;
  public final int FFT_IFSTATEMENT = 23;
  public final int FFT_WHILESTATEMENT = 24;
  public final int FFT_FORSTATEMENT = 25;
  public final int FFT_RETURNSTATEMENT = 26;
  public final int FFT_PRIMITIVETYPE = 27;
  public final int FFT_COMPLEXALGEBRAICTYPE = 28;
  public final int FFT_TYPE = 29;
  public final int FFT_DECL = 30;
  public final int FFT_IDTERM = 31;
  public final int FFT_TERM = 32;
  public final int FFT_POLYNOMIAL = 33;
  public final int FFT_GF = 34;
  public final int FFT_RECORD = 35;
  public final int FFT_PSRANDOMGENERATOR = 36;
  public final int FFT_FORMALPARAM = 37;
  public final int FFT_PROGRAM = 38;
  public final int FFT_PROC = 39;
  public final int FFT_FUNC = 40;
  public final int FFT_ARGUMENTLIST = 41;
  public final int FFT_BLOCK = 42;
  public final int FFT_FUNCBLOCK = 43;
  public final int FFT_ELSEBLOCK = 44;
  public final int FFT_STATEMENTLIST = 45;
  public final int FFT_STATEMENT = 46;
  public final int FFT_DECLTYPE = 47;
  public final int FFT_FORMALPARAMLIST = 48;
  public final int FFT_CONSTDECL = 49;
  public final int FFT_EXPRCOMPLEXATYPE = 50;
  public final int FFT_CONTAINERTYPE = 51;
  public final int FFT_ALGEBRAICTYPE = 52;
  public final int FFT_PARAMTYPE = 53;
  public final int FFT_CONSTTYPE = 54;
  public final int FFT_ARRAYTYPE = 55;
  public final int FFT_RANDOMGENERATORTYPE = 56;
  public final int FFT_EXPRRANDOMGTYPE = 57;
  public final int FFT_BLOCKSTATEMENT = 58;
  public final int FFT_BREAK = 59;
  public final int FFT_EC = 60;
  public final int FFT_ECPOINT = 61;
  public final int FFT_SAMEAS = 62;
  public final int FFT_ECBASEFIELD = 63;
  public final int FFT_ECASSIGNMENT = 64;
  public final int FFT_CONDOREXPR = 65;


  public String[] FFT_NodeName = {
    "RelOp",
    "EqualOp",
    "AddOp",
    "MulOp",
    "Random",
    "RandomGenerator",
    "Literal",
    "Selector",
    "ArrayLen",
    "PrimaryExpr",
    "UnaryExpr",
    "PowExpr",
    "MulExpr",
    "AddExpr",
    "RelExpr",
    "EqualExpr",
    "CondAndExpr",
    "CreationExpr",
    "Expr",
    "void",
    "ProcFuncCall",
    "Assignment",
    "Condition",
    "IfStatement",
    "WhileStatement",
    "ForStatement",
    "ReturnStatement",
    "PrimitiveType",
    "ComplexAlgebraicType",
    "Type",
    "Decl",
    "IdTerm",
    "Term",
    "Polynomial",
    "GF",
    "Record",
    "PsRandomGenerator",
    "FormalParam",
    "Program",
    "Proc",
    "Func",
    "ArgumentList",
    "Block",
    "FuncBlock",
    "ElseBlock",
    "StatementList",
    "Statement",
    "DeclType",
    "FormalParamList",
    "ConstDecl",
    "ExprComplexAType",
    "ContainerType",
    "AlgebraicType",
    "ParamType",
    "ConstType",
    "ArrayType",
    "RandomGeneratorType",
    "ExprRandomGType",
    "BlockStatement",
    "Break",
    "EllipticCurve",
    "EllipticCurvePoint"
  };
}

