package ffapl.types;

/**
 * 
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface FFaplTypeCrossTable {
 
	public static final byte FFAPLINTEGER = 0;
	public static final byte FFAPLBOOLEAN = 1;
	public static final byte FFAPLRESIDUECLASS = 2;
	public static final byte FFAPLPOLYNOMIAL = 3;
	public static final byte FFAPLGF = 4;
	public static final byte FFAPLPRIME = 5;
	public static final byte FFAPLPSRANDOMG = 6;
	public static final byte FFAPLRANDOM = 7;
	public static final byte FFAPLRANDOMG = 8;
	public static final byte FFAPLRECORD = 9;
	public static final byte FFAPLARRAY = 10;
	public static final byte FFAPLPOLYNOMIALRESIDUE = 11;
	public static final byte FFAPLSTRING = 12;
	public static final byte FFAPLEC = 13;
	public static final byte FFAPLPRIMITIVETYPE = 14;
	public static final byte FFAPLCOMPLEXTYPE = 15;
	public static final byte FFAPLALGEBRAICTYPE = 16;
	public static final byte FFAPLCONTAINERTYPE = 17;
	public static final byte FFAPLTYPE = 18;
	

	public String[] TYPE_Name = {
			"Integer",
			"Boolean",
			"Z()",
			"Polynomial",
			"GaloisField",
			"Prime",
			"PseudoRandomGenerator",
			"Random",
			"RandomGenerator",
			"Record",
			"Array",
			"Z()[x]",
			"String",
			"EllipticCurve",
			"PrimitiveType",
			"ComplexType",
			"AlgebraicType",
			"ContainerType",
			"Type"
	 };
	
	/**
	 * Cross Table for Relational Operation
	 */
	public boolean[][] RELOP_compatibility = {
			      //Integer, Boolean, Z()      , Polynom, GaloisF, Prime, PseudorandomG, Random, RandomG, Record, Array, PolynomialResidue ,String,	 EC 
 /*Integer*/       {true   , false  , true     , false  , false  , true , true         , true  , true   , false , false, false			   , false, false},
 /*Boolean*/       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false, false    		   , false, false},
 /*Z()      */     {true   , false  , true     , false  , false  , true , true         , true  , true   , false , false, false    		   , false, false},
 /*Polynom*/       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false, false    		   , false, false},
 /*GF*/            {false  , false  , false    , false  , false  , false, false        , false , false  , false , false, false    		   , false, false},
 /*Prime*/         {true   , false  , true     , false  , false  , true , true         , true  , true   , false , false, false    		   , false, false},
 /*PseudorandomG*/ {true   , false  , true     , false  , false  , true , true         , true  , true   , false , false, false    		   , false, false},
 /*Random*/        {true   , false  , true     , false  , false  , true , true         , true  , true   , false , false, false    		   , false, false},
 /*RandomG*/       {true   , false  , true     , false  , false  , true , true         , true  , true   , false , false, false    		   , false, false},
 /*Record*/        {false  , false  , false    , false  , false  , false, false        , false , false  , false , false, false     		   , false, false},
 /*Array*/         {false  , false  , false    , false  , false  , false, false        , false , false  , false , false, false    		   , false, false},
 /*PolynomialRes*/ {false  , false  , false    , false  , false  , false, false        , false , false  , false , false, false    		   , false, false},
 /*String */       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false, false    		   , false, false},
 /*EC */ 	       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false, false    		   , false, false}
	};
	
	/**
	 * Cross Table for Equality Operation
	 */
	public boolean[][] EQUALOP_compatibility = {
	             //Integer, Boolean, Z()      , Polynom, GaloisF, Prime, PseudorandomG, Random, RandomG, Record, Array,  PolynomialResidue , String, EC    
/*Integer*/       {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false , true    		   , false, false},
/*Boolean*/       {false  , true   , false    , false  , false  , false, false        , false , false  , false , false , false   		   , false, false},
/*Z()      */     {true   , false  , true     , false  , true   , true , true         , true  , true   , false , false , true     		   , false, false},
/*Polynom*/       {true   , false  , false    , true   , true   , true , true         , true  , true   , false , false , true    		   , false, false},
/*GF*/            {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false , true    		   , false, false},
/*Prime*/         {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false , true    		   , false, false},
/*PseudorandomG*/ {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false , true    		   , false, false},
/*Random*/        {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false , true    		   , false, false},
/*RandomG*/       {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false , true    		   , false, false},
/*Record*/        {false  , false  , false    , false  , false  , false, false        , false , false  , false , false , false   		   , false, false},
/*Array*/         {false  , false  , false    , false  , false  , false, false        , false , false  , false , false , false   		   , false, false},
/*PolynomialRes*/ {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false , true    		   , false, false},
/*String */       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false , false    		   , false, false},
/*EC */ 	      {false  , false  , false    , false  , false  , false, false        , false , false  , false , false , false    		   , false, true}
};
	
	/**
	 * Cross Table for Binary Operation
	 */
	public boolean[][] OP2_compatibility = {
		         //Integer, Boolean, Z()      , Polynom, GaloisF, Prime, PseudorandomG, Random, RandomG, Record, Array,  PolynomialResidue , String, EC 
/*Integer*/       {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false,  true      		   , true, true },
/*Boolean*/       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , true, false },
/*Z()      */     {true   , false  , true     , false  , true   , true , true         , true  , true   , false , false,  true      		   , true, false },
/*Polynom*/       {true   , false  , false    , true   , true   , true , true         , true  , true   , false , false,  true     		   , true, false },
/*GF*/            {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false,  true     		   , true, false },
/*Prime*/         {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false,  true     		   , true, true },
/*PseudorandomG*/ {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false,  true     		   , true, false },
/*Random*/        {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false,  true     		   , true, false },
/*RandomG*/       {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false,  true     		   , true, false },
/*Record*/        {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false },
/*Array*/         {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , true, false },
/*PolynomialRes*/ {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false,  true     		   , true, false },
/*String */       {true   , true   , true     , true   , true   , true , true         , true  , true   , false , true  , true     		   , true, true },
/*EC */  	      {true  , false  , false	  , false  , false	, true, false		  , false , false  , false , false , false			   , true, true}
    };
	
	/**
	 * Cross Table for Binary Operation
	 */
	public boolean[][] POW_compatibility = {
		         //Integer, Boolean, Z()      , Polynom, GaloisF, Prime, PseudorandomG, Random, RandomG, Record, Array,  PolynomialResidue , String , EC  
/*Integer*/       {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false     		   , false, false},
/*Boolean*/       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Z()      */     {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},	
/*Polynom*/       {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*GF*/            {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*Prime*/         {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*PseudorandomG*/ {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*Random*/        {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*RandomG*/       {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*Record*/        {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Array*/         {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*PolynomialRes*/ {true   , false  , true    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*String */       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false , false    		   , false, false},
/*EC */   	      {false  , false  , false    , false  , false  , false, false        , false , false  , false , false , false    		   , false, false}
    };
	
	/**
	 * Cross Table for Assignment
	 */
	public boolean[][] ASSIGN_compatibility = {
		         //Integer, Boolean, Z()      , Polynom, GaloisF, Prime, PseudorandomG, Random, RandomG, Record, Array,  PolynomialResidue , String    ,EC  
/*Integer*/       {true   , false  , false    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*Boolean*/       {false  , true   , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Z()      */     {true   , false  , true     , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},	
/*Polynom*/       {true   , false  , false    , true   , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*GF*/            {true   , false  , true     , true   , true   , true , true         , true  , true   , false , false,  true     		   , false, false},
/*Prime*/         {true   , false  , false    , false  , false  , true , true         , true  , true   , false , false,  false    		   , false, false},
/*PseudorandomG*/ {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Random*/        {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*RandomG*/       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Record*/        {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Array*/         {false  , false  , false    , false  , false  , false, false        , false , false  , false , true ,  false    		   , false, false},
/*PolynomialRes*/ {true   , false  , true     , true   , false  , true , true         , true  , true   , false , false,  true     		   , false, false},
/*String */       {true   , true   , true     , true   , true   , true , true         , true  , true   , true  , true  , true     		   , true,  false },
/*EC */    		  {false, false  , false	  , false  , false, false, false		  , false , false  , false , false , false			   , false, true }
    };
	
	/**
	 * Cross Table for Assignment
	 */
	public boolean[][] FORSTATEMENT_compatibility = {
		         //Integer, Boolean, Z()      , Polynom, GaloisF, Prime, PseudorandomG, Random, RandomG, Record, Array,  PolynomialResidue , String  ,EC 
/*Integer*/       {true   , false  , false    , false  , false  , true , false        , false , false  , false , false,  false    		   , false, false},
/*Boolean*/       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Z()      */     {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},	
/*Polynom*/       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*GF*/            {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Prime*/         {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*PseudorandomG*/ {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Random*/        {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*RandomG*/       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Record*/        {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*Array*/         {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*PolynomialRes*/ {false  , false  , false    , false  , false  , false, false        , false , false  , false , false,  false    		   , false, false},
/*String */       {false  , false  , false    , false  , false  , false, false        , false , false  , false , false , false    		   , false, false},
/*EC */      	  {false  , false  , false    , false  , false  , false, false        , false , false  , false , false , false    		   , false, false}
    };

    /**
     * Cross table for Unary Operation
     */
	public boolean[] OP1_compatibility = {
				   //Integer, Boolean, Z()      , Polynom, GaloisF, Prime, PseudorandomG, Random, RandomG, Record, Array,  PolynomialResidue , String  , EC
                     true   , false  , true     , true   , true   , true , true         , true  , true   , false , false , true 			 , false   , true   
     };

}