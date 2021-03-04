package ffapl.java.classes;

import ffapl.FFaplInterpreter;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

import static java.math.BigInteger.*;

public class EllipticCurve implements IJavaType<EllipticCurve>, Comparable<EllipticCurve>{

	private Thread _thread;
	private GaloisField _gf;
	private ResidueClass _rc = null;
	private boolean _isGf = false;
	private boolean _isPAI = false;
	private boolean _isRandom = false;
	private boolean _isRandomSub = false;

	private Polynomial _a1_gf = new Polynomial(0,0, _thread);
	private Polynomial _a2_gf = new Polynomial(0,0, _thread);
	private Polynomial _a3_gf = new Polynomial(0,0, _thread);
	private Polynomial _a4_gf = new Polynomial(0,0, _thread);
	private Polynomial _a6_gf = new Polynomial(0,0, _thread);

	private BInteger _a1_rc = new BInteger(ZERO,_thread);
	private BInteger _a2_rc = new BInteger(ZERO,_thread);
	private BInteger _a3_rc = new BInteger(ZERO,_thread);
	private BInteger _a4_rc = new BInteger(ZERO,_thread);
	private BInteger _a6_rc = new BInteger(ZERO,_thread);

	private Polynomial _x_gf = new Polynomial(0,0, _thread);
	private Polynomial _y_gf = new Polynomial(0,0, _thread);


	private BInteger _x_rc = new BInteger(ZERO,_thread);
	private BInteger _y_rc = new BInteger(ZERO,_thread);




	public Polynomial get_a1() { return _a1_gf; }
	public Polynomial get_a2() { return _a2_gf; }
	public Polynomial get_a3() { return _a3_gf; }
	public Polynomial get_a4() { return _a4_gf; }
	public Polynomial get_a6() { return _a6_gf; }


	public GaloisField getGF()
	{
		return this._gf;
	}

	public ResidueClass getRC()
	{
		return this._rc;
	}


	public EllipticCurve(Thread thread)
	{
		_thread = thread;
	}

	public EllipticCurve(BInteger x, BInteger y, Thread thread) throws FFaplAlgebraicException
	{
		_thread = thread;
		_x_rc = x;
		_y_rc = y;

		this.mod();
		//this.pointIsOnCurve();

	}


	public EllipticCurve(Polynomial x, Polynomial y, Thread thread) throws FFaplAlgebraicException
	{
		_thread = thread;
		_x_gf = x;
		_y_gf = y;
		_isGf = true;

		this.mod();
		//this.pointIsOnCurve();

	}

        public EllipticCurve(String s, Thread thread) throws FFaplAlgebraicException{
                this(thread);
                String withoutB = s.replaceAll("<<|>>", "");

                String[] d = withoutB.trim().split(",");
                //
                if(d[0].startsWith("[") && d[0].endsWith("]") && d[1].startsWith("[") && d[1].endsWith("]")){
                        this._x_gf = new Polynomial(d[0], new Thread());
                        this._y_gf = new Polynomial(d[1], new Thread());
                        this._isGf = true;
                }else if(d[0].equals("PAI")){
                        this._isPAI = true;
                }else{
                        this._x_rc = new BInteger(BigInteger.valueOf(Long.parseLong(d[0])),_thread);
                        this._y_rc = new BInteger(BigInteger.valueOf(Long.parseLong(d[1])),_thread);
                }

                this.mod();
        }




	public Thread getThread(){
		return _thread;
	}
	
	private boolean weierstrassEquationIsValid() throws FFaplAlgebraicException
	{
		if (this._isPAI) return true;

		if (this.parametersSet())
		{
			if (this._isGf) //GF
			{
				Polynomial lvalue, rvalue, foo;
				GaloisField test1, test2;

				foo = _y_gf.clone();
				lvalue = foo.pow(2);
				foo = _x_gf.clone();
				foo.multiply(_y_gf);
				foo.multiply(_a1_gf);
				lvalue.add(foo);
				foo = _y_gf.clone();
				foo.multiply(_a3_gf);
				lvalue.add(foo);

				foo = _x_gf.clone();
				rvalue = foo.pow(3);
				foo = _x_gf.clone();
				foo.pow(2);
				foo.multiply(_a2_gf);
				rvalue.add(foo);
				foo = _x_gf.clone();
				foo.multiply(_a4_gf);
				rvalue.add(foo);
				rvalue.add(_a6_gf);

				if (_gf != null)
				{
					test1 = _gf.clone();
					test2 = _gf.clone();

					test1.setValue(rvalue);
					rvalue  = test1.value();

					test2.setValue(lvalue);
					lvalue  = test2.value();


					return lvalue.equals(rvalue);

				}


			}
			else //RC
			{
				BigInteger lvalue, rvalue;
				lvalue = _y_rc.pow(2).add(_a1_rc.multiply(_x_rc).multiply(_y_rc)).add(_a3_rc.multiply(_y_rc));
				rvalue = _x_rc.pow(3).add(_a2_rc.multiply(_x_rc.pow(2))).add(_a4_rc.multiply(_x_rc)).add(_a6_rc);

				if (_rc != null)
				{
					lvalue = lvalue.mod(_rc.modulus());
					rvalue = rvalue.mod(_rc.modulus());
				}

				if (lvalue.equals(rvalue)) return true;
				else return false;
			}
		}
		return true;
	}

	private void pointIsOnCurve() throws FFaplAlgebraicException
	{
		if (!this.weierstrassEquationIsValid())
		{
			Object[] arguments ={};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.EC_POINT_ERROR);
		}

		/*
		if (this._isPAI)
		{
			return;
		}
		if (this.parametersSet())
		{
			if (!this._isGf)
			{
				BigInteger lvalue, rvalue;
				lvalue = _y_rc.pow(2).add(_a1_rc.multiply(_x_rc).multiply(_y_rc)).add(_a3_rc.multiply(_y_rc));
				rvalue = _x_rc.pow(3).add(_a2_rc.multiply(_x_rc.pow(2))).add(_a4_rc.multiply(_x_rc)).add(_a6_rc);

				if (_rc != null)
				{
					lvalue = lvalue.mod(_rc.modulus());
					rvalue = rvalue.mod(_rc.modulus());
				}

				if (!lvalue.equals(rvalue))
				{
					Object[] arguments ={};
					throw new FFaplAlgebraicException(arguments, IAlgebraicError.EC_POINT_ERROR);
				}
			}
			else
			{
				Polynomial lvalue, rvalue, foo;
				GaloisField test1, test2;

				foo = _y_gf.clone();
				lvalue = foo.pow(2);
				foo = _x_gf.clone();
				foo.multiply(_y_gf);
				foo.multiply(_a1_gf);
				lvalue.add(foo);
				foo = _y_gf.clone();
				foo.multiply(_a3_gf);
				lvalue.add(foo);

				foo = _x_gf.clone();
				rvalue = foo.pow(3);
				foo = _x_gf.clone();
				foo.pow(2);
				foo.multiply(_a2_gf);
				rvalue.add(foo);
				foo = _x_gf.clone();
				foo.multiply(_a4_gf);
				rvalue.add(foo);
				rvalue.add(_a6_gf);

				if (_gf != null)
				{
					test1 = _gf.clone();
					test2 = _gf.clone();

					test1.setValue(rvalue);
					rvalue  = test1.value();

					test2.setValue(lvalue);
					lvalue  = test2.value();


					if (!lvalue.equals(rvalue))
					{
						Object[] arguments ={};
						throw new FFaplAlgebraicException(arguments, IAlgebraicError.EC_POINT_ERROR);
					}

				}
			}
		}
		*/
	}




	public boolean parametersSet() throws FFaplAlgebraicException
	{
		if (this._isGf)
		{
			Polynomial nullPolynom = new Polynomial(0,0, _thread);
			return (!_a1_gf.equals(nullPolynom)) || (!_a2_gf.equals(nullPolynom)) || (!_a3_gf.equals(nullPolynom)) || (!_a4_gf.equals(nullPolynom)) || (!_a6_gf.equals(nullPolynom));
		}
		else
		{
			return (!_a1_rc.equals(ZERO)) || (!_a2_rc.equals(ZERO)) || (!_a3_rc.equals(ZERO)) || (!_a4_rc.equals(ZERO)) || (!_a6_rc.equals(ZERO));
		}
	}




	public void sub(EllipticCurve ec) throws FFaplAlgebraicException
	{
		GaloisField aux1, aux2, aux3, aux4;

		if(! this.equalEC(ec)){
			Object[] arguments ={this.classInfo(), ec.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.TYPES_INCOMPATIBLE);
		}

		if(!this.parametersSet() && !ec.parametersSet()){
			Object[] arguments ={this.classInfo(), ec.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.TYPES_INCOMPATIBLE); //Addition ohne Parameter in Print-Anweisung
		}

		this.mod();
		ec.mod();

		if (!ec._isGf)
		{
			if (ec.parametersSet())
			{
				//for << x, y >> - ec
				ec._y_rc = new BInteger(ZERO.subtract(ec._y_rc),_thread);
				ec._y_rc = new BInteger(ec._y_rc.subtract(ec._a1_rc.multiply(ec._x_rc)),_thread);
				ec._y_rc = new BInteger(ec._y_rc.subtract(ec._a3_rc),_thread);
			}
			else
			{
				//for ec - << x, y >>
				ec._y_rc = new BInteger(ZERO.subtract(ec._y_rc),_thread);
				ec._y_rc = new BInteger(ec._y_rc.subtract(this._a1_rc.multiply(ec._x_rc)),_thread);
				ec._y_rc = new BInteger(ec._y_rc.subtract(this._a3_rc),_thread);
			}
		}
		else //GF-Version
		{
			if (ec.parametersSet())
			{
				//for << x, y >> - ec
				aux1 = ec._gf.clone();
				aux2 = ec._gf.clone();
				aux3 = ec._gf.clone();
				aux4 = ec._gf.clone();

				aux1.setValue(ec._a1_gf);
				aux2.setValue(ec._x_gf);
				aux3.setValue(ec._a3_gf);
				aux4.setValue(ec._y_gf);

				aux1.multiply(aux2); //a1*x1
				aux1.negate();
				aux1.subtract(aux3); //-a1*x1-a3
				aux1.subtract(aux4); //-y1-a1*x1-a3

				ec._y_gf = aux1.value();
			}
			else
			{
				//for ec - << x, y >>
				aux1 = this._gf.clone();
				aux2 = this._gf.clone();
				aux3 = this._gf.clone();
				aux4 = this._gf.clone();

				aux1.setValue(this._a1_gf);
				aux2.setValue(ec._x_gf);
				aux3.setValue(this._a3_gf);
				aux4.setValue(ec._y_gf);

				aux1.multiply(aux2); //a1*x1
				aux1.negate();
				aux1.subtract(aux3); //-a1*x1-a3
				aux1.subtract(aux4); //-y1-a1*x1-a3

				ec._y_gf = aux1.value();
			}
		}

		this.add(ec);

	}


	public void add(EllipticCurve ec) throws FFaplAlgebraicException
	{
		if(! this.equalEC(ec)){
			Object[] arguments ={this.classInfo(), ec.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.TYPES_INCOMPATIBLE);
		}

		if(!this.parametersSet() && !ec.parametersSet()){
			Object[] arguments ={this.classInfo(), ec.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.TYPES_INCOMPATIBLE); //Addition ohne Parameter in Print-Anweisung
		}

		this.mod();
		ec.mod();


		if (this._isRandom)
		{
			ec.setRandomPoint(false);
			return;
		}
		else if (ec._isRandom)
		{
			this.setRandomPoint(false);
			return;
		}




		if (this.isGf())
		{
			if (!this.parametersSet())
			{
				this._gf = ec._gf;
				this._a1_gf = ec._a1_gf;
				this._a2_gf = ec._a2_gf;
				this._a3_gf = ec._a3_gf;
				this._a4_gf = ec._a4_gf;
				this._a6_gf = ec._a6_gf;
			}


			if (ec._isPAI)
			{
				return;
			}
			else if (this._isPAI)
			{
				if (!ec._isPAI)
				{
					this._isPAI = false;
				}


				this._x_gf = ec._x_gf;
				this._y_gf = ec._y_gf;


				return;
			}



			Polynomial bar;
			GaloisField gf = this._gf.clone();
			Polynomial nu = new Polynomial(0,0,_thread);
			Polynomial nu1 = new Polynomial(0,0,_thread);
			Polynomial l = new Polynomial(0,0,_thread);
			Polynomial l1 = new Polynomial(0,0,_thread);

			Polynomial x3, y3;

			if (this._x_gf.equals(ec._x_gf))
			{
				Polynomial ply;

				bar = this._y_gf.clone(); //y1
				bar.add(ec._y_gf); //y1+y2
				ply = this._a1_gf.clone(); //a1
				ply.multiply(this._x_gf); //a1 x1
				bar.add(ply); //y1+y2+a1*x1
				bar.add(_a3_gf); //y1+y2+a1*x1+a3

				gf.setValue(bar);
				bar = gf.value();

				if (bar.equals(new Polynomial(0,0,_thread)))
				{
					this._isPAI = true;
					return;
				}




				//Andernfalls Berechnung von mu und lambda mit x1 = x2


				bar = new Polynomial(0,0,_thread);

				bar.add(this._x_gf); //x1
				bar.pow(TWO); //x1^2
				bar.multiply(new Polynomial(3,0,_thread)); //3*x1^2


				l.add(bar); //3*x1^2

				bar = new Polynomial(0,0,_thread); //0
				bar.add(this._x_gf); //x1
				bar.multiply(this._a2_gf); //a2*x1
				bar.multiply(new Polynomial(2,0,_thread)); //2*a2*x1


				l.add(bar);//3*x1^2+2*a2*x1

				l.add(this._a4_gf);//3*x1^2 + 2*a2*x1 + a4


				bar = new Polynomial(0,0,_thread);

				bar.add(this._a1_gf); //a1
				bar.multiply(this._y_gf); //a1*y1

				l.subtract(bar); //3*x1^2 + 2*a2*x1 + a4 - a1*y1




				l1 = new Polynomial(0,0,_thread);

				bar = this._y_gf.clone(); //y1
				bar.add(bar); //2*y1

				l1.add(bar); //2*y1

				bar = this._x_gf.clone(); // x1
				bar.multiply(this._a1_gf); // a1*x1
				l1.add(bar); // 2y1 + a1*x1
				l1.add(this._a3_gf); // 2y1 + a1*x1 + a3
				gf.setValue(l1);
				gf = GaloisField.inverse(gf); // (2y1 + a1*x1 + a3)^-1

				l1 = gf.value();
				l.multiply(l1);








				bar = new Polynomial(0,0,_thread);

				bar = this._x_gf.clone();
				bar.pow(valueOf(3));
				bar.multiply(valueOf(-1), ZERO);

				nu.add(bar);
				bar = this._a4_gf.clone();
				bar.multiply(this._x_gf);
				nu.add(bar);
				bar = this._a6_gf.clone();
				bar.multiply(TWO, ZERO);
				nu.add(bar);
				bar = this._a3_gf.clone();
				bar.multiply(this._y_gf);
				nu.subtract(bar);


				nu1 = this._y_gf.clone();
				nu1.multiply(TWO, ZERO);
				bar = this._a1_gf.clone();
				bar.multiply(this._x_gf);
				nu1.add(bar);
				nu1.add(this._a3_gf);

				gf.setValue(nu1);
				gf = GaloisField.inverse(gf);

				nu1 = gf.value();

				nu.multiply(nu1);



			}
			else if (!this._x_gf.equals(ec._x_gf)) // dritte möglichkeit
			{
				bar = new Polynomial(0,0,_thread);


				l = ec._y_gf.clone();
				l.subtract(this._y_gf);

				l1 = ec._x_gf.clone();
				l1.subtract(this._x_gf);

				gf.setValue(l1);
				gf = GaloisField.inverse(gf);

				l1 = gf.value();

				l.multiply(l1);



				nu = this._y_gf.clone();
				nu.multiply(ec._x_gf);

				bar = this._x_gf.clone();
				bar.multiply(ec._y_gf);

				nu.subtract(bar);
				nu.multiply(l1);

			}


			x3 = l.clone();
			x3.pow(TWO);

			bar = l.clone();
			bar.multiply(this._a1_gf);

			x3.add(bar);
			x3.subtract(this._a2_gf);
			x3.subtract(this._x_gf);
			x3.subtract(ec._x_gf);


			y3 = l.clone();
			y3.add(this._a1_gf);
			y3.multiply(valueOf(-1), ZERO);
			y3.multiply(x3);
			y3.subtract(nu);
			y3.subtract(this._a3_gf);

			gf.setValue(x3);
			this._x_gf = gf.value();

			gf.setValue(y3);
			this._y_gf = gf.value();


		}
		else // Z(p)-Version
		{
			if (!this.parametersSet())
			{
				this._rc = ec._rc;
				this._gf = ec._gf;
				this._a1_rc = ec._a1_rc;
				this._a2_rc = ec._a2_rc;
				this._a3_rc = ec._a3_rc;
				this._a4_rc = ec._a4_rc;
				this._a6_rc = ec._a6_rc;
			}

			if (ec._isPAI)
			{
				return;
			}
			else if (this._isPAI)
			{
				if (!ec._isPAI)
				{
					this._isPAI = false;
				}


				this._x_rc = ec._x_rc;
				this._y_rc = ec._y_rc;

				return;
			}

			BigInteger modulus = this._rc.modulus();



			BigInteger nu = ZERO;
			BigInteger nu1 = ZERO;
			BigInteger l = ZERO;
			BigInteger l1 = ZERO;


			BigInteger x3 = ZERO;
			BigInteger y3 = ZERO;




			if (this._x_rc.equals(ec._x_rc) && this._y_rc.add(ec._y_rc).add(this._a1_rc.multiply(this._x_rc)).add(_a3_rc).mod(this._rc._modulus).equals(ZERO))
			{
				this._isPAI = true;
			}
			else if (this._x_rc.equals(ec._x_rc))
			{
				l = l.add(this._x_rc).pow(2).multiply(valueOf(3));
				l = l.add(this._x_rc.multiply(_a2_rc).multiply(TWO));
				l = l.add(this._a4_rc);
				l = l.subtract(this._a1_rc.multiply(this._y_rc));

				l1 = l1.add(this._y_rc.multiply(TWO));
				l1 = l1.add(this._a1_rc.multiply(this._x_rc));
				l1 = l1.add(this._a3_rc);

				l1 = l1.modInverse(modulus);

				l = l1.multiply(l);
				l = l.mod(modulus);

				nu = nu.subtract(this._x_rc.pow(3));
				nu = nu.add(this._a4_rc.multiply(this._x_rc));
				nu = nu.add(this._a6_rc.multiply(TWO));
				nu = nu.subtract(this._a3_rc.multiply(this._y_rc));

				nu1 = nu1.add(this._y_rc.multiply(TWO));
				nu1 = nu1.add(this._a1_rc.multiply(this._x_rc));
				nu1 = nu1.add(this._a3_rc);

				nu1 = nu1.modInverse(modulus);

				nu = nu1.multiply(nu);
				nu = nu.mod(modulus);

			}
			else if (!this._x_rc.equals(ec._x_rc))
			{
				l = l.add(ec._y_rc);
				l = l.subtract(this._y_rc);

				l1 = l1.add(ec._x_rc);
				l1 = l1.subtract(this._x_rc);

				l1 = l1.modInverse(modulus);

				l = l1.multiply(l);
				l = l.mod(modulus);


				nu = nu.add(this._y_rc.multiply(ec._x_rc));
				nu = nu.subtract(ec._y_rc.multiply(this._x_rc));

				nu1 = nu1.add(ec._x_rc);
				nu1 = nu1.subtract(this._x_rc);

				nu1 = nu1.modInverse(modulus);

				nu = nu1.multiply(nu);

				nu = nu.mod(modulus);

			}

			x3 = x3.add(l.pow(2));
			x3 = x3.add(this._a1_rc.multiply(l));
			x3 = x3.subtract(this._a2_rc);
			x3 = x3.subtract(this._x_rc);
			x3 = x3.subtract(ec._x_rc);

			y3 = y3.subtract(x3);
			y3 = y3.multiply(l.add(this._a1_rc));
			y3 = y3.subtract(nu);
			y3 = y3.subtract(this._a3_rc);

			this._x_rc = new BInteger(x3.mod(modulus),_thread);
			this._y_rc = new BInteger(y3.mod(modulus),_thread);

		}

	}








	public void multiply(BigInteger factor) throws FFaplAlgebraicException
	{
		EllipticCurve ec = this;
		if (factor.max(ZERO).equals(ZERO))
		{
			factor = factor.multiply(valueOf(-1));
			ec = this.clone();
			ec.setPAI(true);
			ec.sub(this);
		}



		EllipticCurve e = Algorithm.squareAndMultiply(ec, factor);



		if (!this.isGf())
		{
			this._isPAI = e._isPAI;
			this._x_rc = e._x_rc;
			this._y_rc = e._y_rc;
		}
		else
		{
			this._isPAI = e._isPAI;
			this._x_gf = e._x_gf;
			this._y_gf = e._y_gf;
		}
	}








	private void curveIsValid() throws FFaplAlgebraicException
	{
		if (!this._isGf && this.parametersSet() && _rc != null)
		{
			BigInteger d, b2, b4, b6, b8;

			b2 = ZERO;
			b2 = b2.add(this._a1_rc);
			b2 = b2.pow(2);
			b2 = b2.add(this._a2_rc.multiply(valueOf(4)));

			b4 = ZERO;
			b4 = b4.add(this._a4_rc).add(this._a4_rc);
			b4 = b4.add(this._a1_rc.multiply(this._a3_rc));

			b6 = ZERO;
			b6 = b6.add(this._a3_rc);
			b6 = b6.pow(2);
			b6 = b6.add(this._a6_rc.multiply(valueOf(4)));

			b8 = ZERO;
			b8 = b8.add(this._a1_rc);
			b8 = b8.pow(2);
			b8 = b8.multiply(this._a6_rc);
			b8 = b8.add(this._a2_rc.multiply(this._a6_rc).multiply(valueOf(4)));
			b8 = b8.subtract(this._a1_rc.multiply(this._a3_rc).multiply(this._a4_rc));
			b8 = b8.add(this._a3_rc.multiply(this._a3_rc).multiply(this._a2_rc));
			b8 = b8.subtract(this._a4_rc.multiply(this._a4_rc));

			d = ZERO;
			d = d.subtract(b2.pow(2).multiply(b8));
			d = d.subtract(b4.pow(3).multiply(valueOf(8)));
			d = d.subtract(b6.pow(2).multiply(valueOf(27)));
			d = d.add(b2.multiply(b4).multiply(b6).multiply(valueOf(9)));

			d = d.mod(_rc.modulus());

			if (d.compareTo(ZERO) == 0)
			{
				Object[] arguments ={};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.WEIERSTRASS_SINGULAR);
			}

		}
		if (this._isGf && this.parametersSet() && _gf != null)
		{
			Polynomial d, b2, b4, b6, b8;
			Polynomial foo;


			b2 = this._a1_gf.clone();
			b2.pow(2);
			b2.add(this._a2_gf);
			b2.add(this._a2_gf);
			b2.add(this._a2_gf);
			b2.add(this._a2_gf);


			b4 = this._a1_gf.clone();
			b4.multiply(this._a3_gf);
			b4.add(this._a4_gf);
			b4.add(this._a4_gf);

			b6 = this._a3_gf.clone();
			b6.pow(2);
			b6.add(this._a6_gf);
			b6.add(this._a6_gf);
			b6.add(this._a6_gf);
			b6.add(this._a6_gf);

			b8 = this._a1_gf.clone();
			b8.pow(2);
			b8.multiply(this._a6_gf);

			foo = this._a2_gf.clone();
			foo.multiply(this._a6_gf);
			foo.multiply(new Polynomial(4,0,_thread));

			b8.add(foo);

			foo = this._a1_gf.clone();
			foo.multiply(this._a3_gf);
			foo.multiply(this._a4_gf);

			b8.subtract(foo);

			foo = this._a3_gf.clone();
			foo.pow(2);
			foo.multiply(this._a2_gf);

			b8.add(foo);

			foo = this._a4_gf.clone();
			foo.pow(2);

			b8.subtract(foo);






			d = new Polynomial(0,0,_thread);

			foo = b2.clone();
			foo.pow(2);
			foo.multiply(b8);

			d.subtract(foo);

			foo = b4.clone();
			foo.pow(3);
			foo.multiply(new Polynomial(8,0,_thread));

			d.subtract(foo);

			foo = b6.clone();
			foo.pow(2);
			foo.multiply(new Polynomial(27,0,_thread));

			d.subtract(foo);

			foo = b2.clone();
			foo.multiply(b4);
			foo.multiply(b6);
			foo.multiply(new Polynomial(9,0,_thread));

			d.add(foo);

			GaloisField gf = this._gf.clone();
			gf.setValue(d);
			d = gf.value();

			if (d.isZero())
			{
				Object[] arguments ={};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.WEIERSTRASS_SINGULAR);
			}

		}
	}




	public EllipticCurve(GaloisField gf, Polynomial a1, Polynomial a2, Polynomial a3, Polynomial a4, Polynomial a6, Thread thread) throws FFaplAlgebraicException
	{
		_thread = thread;
		_gf = gf;
		_isGf = true;
		_a1_gf = a1;
		_a2_gf = a2;
		_a3_gf = a3;
		_a4_gf = a4;
		_a6_gf = a6;
		this.mod();
		this.curveIsValid();
	}





	public EllipticCurve(ResidueClass rc, BInteger a1, BInteger a2, BInteger a3, BInteger a4, BInteger a6, Thread thread) throws FFaplAlgebraicException
	{
		_thread = thread;
		_rc = rc;
		_a1_rc = a1;
		_a2_rc = a2;
		_a3_rc = a3;
		_a4_rc = a4;
		_a6_rc = a6;
		this.mod();
		this.curveIsValid();

	}

	public EllipticCurve(ResidueClass rc, BInteger a1, BInteger a2, BInteger a3, BInteger a4, BInteger a6, BInteger x, BInteger y, Thread thread) throws FFaplAlgebraicException
	{
		_thread = thread;
		_rc = rc;


		_a1_rc = a1;
		_a2_rc = a2;
		_a3_rc = a3;
		_a4_rc = a4;
		_a6_rc = a6;

		_x_rc = x;
		_y_rc = y;
		this.mod();
		this.curveIsValid();
		//this.pointIsOnCurve();


	}

	public EllipticCurve(GaloisField gf, Polynomial a1, Polynomial a2, Polynomial a3, Polynomial a4, Polynomial a6, Polynomial x, Polynomial y, Thread thread)throws FFaplAlgebraicException
	{
		_thread = thread;
		_gf = gf;
		_isGf = true;


		_a1_gf = a1;
		_a2_gf = a2;
		_a3_gf = a3;
		_a4_gf = a4;
		_a6_gf = a6;

		_x_gf = x;
		_y_gf = y;
		this.mod();
		this.curveIsValid();
		//this.pointIsOnCurve();

	}


	public BInteger getX_rc()
	{
		return _x_rc;
	}

	public BInteger getY_rc()
	{
		return _y_rc;
	}


	public Polynomial getX_gf()
	{
		return _x_gf;
	}

	public Polynomial getY_gf()
	{
		return _y_gf;
	}

	public boolean isGf ()
	{
		return _isGf;
	}

	public void setValue_rc(BInteger x, BInteger y) throws FFaplAlgebraicException
	{
		_x_rc = x;
		_y_rc = y;

		this.mod();
		this.pointIsOnCurve();
	}

	public void setValue_gf(Polynomial x, Polynomial y) throws FFaplAlgebraicException
	{
		_x_gf = x;
		_y_gf = y;

		this.mod();
		this.pointIsOnCurve();

	}


	public void setRandom(boolean rand, boolean randSub)
	{
		this._isRandom = rand;
		this._isRandomSub = randSub;
	}
	public boolean getRandom()
	{
		return this._isRandom;
	}
	public boolean getRandomSubfield()
	{
		return this._isRandomSub;
	}
	public void setPAI(boolean pai)
	{
		this._isPAI = pai;
	}
	public boolean getPAI()
	{
		return this._isPAI;
	}


	private void mod() throws FFaplAlgebraicException
	{
		if (this.isGf())
		{
			if (this._gf != null)
			{
				GaloisField g = this._gf.clone();

				g.setValue(this._a1_gf);
				this._a1_gf = g.value();

				g.setValue(this._a2_gf);
				this._a2_gf = g.value();

				g.setValue(this._a3_gf);
				this._a3_gf = g.value();

				g.setValue(this._a4_gf);
				this._a4_gf = g.value();

				g.setValue(this._a6_gf);
				this._a6_gf = g.value();

				g.setValue(this._x_gf);
				this._x_gf = g.value();

				g.setValue(this._y_gf);
				this._y_gf = g.value();

			}
		}
		else
		{
			if (this._rc != null)
			{
					this._a1_rc = new BInteger(this._a1_rc.mod(this._rc._modulus),_thread);
					this._a2_rc = new BInteger(this._a2_rc.mod(this._rc._modulus),_thread);
					this._a3_rc = new BInteger(this._a3_rc.mod(this._rc._modulus),_thread);
					this._a4_rc = new BInteger(this._a4_rc.mod(this._rc._modulus),_thread);
					this._a6_rc = new BInteger(this._a6_rc.mod(this._rc._modulus),_thread);
					this._x_rc = new BInteger(this._x_rc.mod(this._rc._modulus),_thread);
					this._y_rc = new BInteger(this._y_rc.mod(this._rc._modulus),_thread);
			}
		}
	}


	public EllipticCurve clone(){
		EllipticCurve ec = null;
		try {
			if (_isGf)
			{
				if (_gf != null)
				{
					ec = new EllipticCurve(_gf.clone(), _a1_gf, _a2_gf, _a3_gf, _a4_gf, _a6_gf, _x_gf, _y_gf, _thread);
				}
				else
				{
					ec = new EllipticCurve(_x_gf, _y_gf, _thread);
				}

			}
			else {
				if (_rc != null)
				{
					ec = new EllipticCurve(_rc.clone(), _a1_rc, _a2_rc, _a3_rc, _a4_rc, _a6_rc, _x_rc, _y_rc, _thread);
				}
				else
				{
					ec = new EllipticCurve(_x_rc, _y_rc, _thread);
				}
			}

		ec.setPAI(this._isPAI);

		return ec;

		} catch (FFaplAlgebraicException e) {
			e.printStackTrace();
			return null;
		}
	}


	public String toString() {
		if (_isPAI)
		{
			return "<< Point at Infinity >>";
		}


		if (this._isGf)
		{
			return "<< " + _x_gf + " , " + _y_gf + " >>";

		}
		return "<< " + _x_rc + " , " + _y_rc + " >>";

	}

	public EllipticCurve negate() throws FFaplAlgebraicException
	{
		EllipticCurve ec = this.clone();
		ec.multiply(ONE.negate());
		return ec;
	}


	@Override
	public int compareTo(EllipticCurve ec) {

		return 0;
	}

	@Override
	public int typeID() {

		return IJavaType.ELLIPTICCURVE;
	}

	@Override
	public String classInfo(){
		String weierstrass = "y^2";

		if(_isGf)
		{
			if(!_a1_gf.isZero())
			{
				weierstrass += " + [" + _a1_gf.toString();
				weierstrass += "] xy";
			}

			if(!_a3_gf.isZero())
			{
				weierstrass += " + [" + _a3_gf.toString();
				weierstrass += "] y";
			}

				weierstrass += " = x^3";

			if(!_a2_gf.isZero())
			{
				weierstrass += " + [" + _a2_gf.toString();
				weierstrass += "] x^2";
			}

			if(!_a4_gf.isZero())
			{
				weierstrass += " + [" + _a4_gf.toString();
				weierstrass += "] x";
			}

			if(!_a6_gf.isZero())
			{
				weierstrass += " + [" + _a6_gf.toString() + "]";
			}

			if (_gf != null)
			{
				return "EC(".concat(_gf.classInfo()).concat(", " + weierstrass + ")");
			}
			return "EC(".concat(weierstrass + ")");

		}
		else {
			if (_a1_rc != null && _a1_rc.compareTo(ZERO) == 0)
			{
				//nichts passiert
			}
			else if (_a1_rc != null && _a1_rc.compareTo(ZERO) != 0)
			{
				if (_a1_rc.toString().charAt(0) == '-')
				{
					weierstrass += " " + _a1_rc.toString();
				}
				else
				{
					weierstrass += " +" + _a1_rc.toString();
				}
				weierstrass += " xy";

			}




			if (_a3_rc != null && _a3_rc.compareTo(ZERO) == 0)
			{
				//nichts passiert
			}
			else if (_a3_rc != null && _a3_rc.compareTo(ZERO) != 0)
			{
				if (_a3_rc.toString().charAt(0) == '-')
				{
					weierstrass += " " + _a3_rc.toString();
				}
				else
				{
					weierstrass += " +" + _a3_rc.toString();
				}
				weierstrass += " y";

			}



			weierstrass += " = x^3";


			if (_a2_rc != null && _a2_rc.compareTo(ZERO) == 0)
			{
				//nichts passiert
			}
			else if (_a2_rc != null && _a2_rc.compareTo(ZERO) != 0)
			{
				if (_a2_rc.toString().charAt(0) == '-')
				{
					weierstrass += " " + _a2_rc.toString();
				}
				else
				{
					weierstrass += " +" + _a2_rc.toString();
				}
				weierstrass += " x^2";

			}



			if (_a4_rc != null && _a4_rc.compareTo(ZERO) == 0)
			{
				//nichts passiert
			}
			else if (_a4_rc != null && _a4_rc.compareTo(ZERO) != 0)
			{
				if (_a4_rc.toString().charAt(0) == '-')
				{
					weierstrass += " " + _a4_rc.toString();
				}
				else
				{
					weierstrass += " +" + _a4_rc.toString();
				}
				weierstrass += " x";

			}




			if (_a6_rc != null && _a6_rc.compareTo(ZERO) == 0)
			{
				//nichts passiert
			}
			else if (_a6_rc != null && _a6_rc.compareTo(ZERO) != 0)
			{
				if (_a6_rc.toString().charAt(0) == '-')
				{
					weierstrass += " " + _a6_rc.toString();
				}
				else
				{
					weierstrass += " +" + _a6_rc.toString();
				}
			}


			if (_rc != null)
			{
				return "EC(".concat(_rc.classInfo()).concat(", " + weierstrass + ")");
			}
			return "EC(".concat(weierstrass + ")");
		}
	}

	public boolean equalEC(EllipticCurve ec) throws FFaplAlgebraicException{
		// if one of the values is a literal with no associated curve,
		// the values are regarded as compatible
		if (!this.parametersSet() || !ec.parametersSet())
			return true;

		if (this.isGf() != ec.isGf())
		{
			return false;
		}
		

		/*
		if (this._rc == null && ec._rc == null)
		{
			return true; //Gleich wenn noch nichts definiert ist
		}
		
		if (this._gf == null && ec._gf == null)
		{
			return true; // Gleich ...
		}
		*/


		if (this.isGf())
		{
			if (!this._gf.equalGF(ec._gf))
			{
				return false;
			}

			if ((this._a1_gf == null && ec._a1_gf != null) || (this._a1_gf != null && ec._a1_gf == null) || (this._a1_gf != null && !this._a1_gf.plyequal((ec._a1_gf))))
			{
				return false;
			}
			if ((this._a2_gf == null && ec._a2_gf != null) || (this._a2_gf != null && ec._a2_gf == null) || (this._a2_gf != null && !this._a2_gf.plyequal(ec._a2_gf)))
			{
				return false;
			}
			if ((this._a3_rc == null && ec._a3_rc != null) || (this._a3_rc != null && ec._a3_rc == null) || (this._a3_gf != null && !this._a3_gf.plyequal(ec._a3_gf)))
			{
				return false;
			}
			if ((this._a4_rc == null && ec._a4_rc != null) || (this._a4_rc != null && ec._a4_rc == null) || (this._a4_gf != null && !this._a4_gf.plyequal(ec._a4_gf)))
			{
				return false;
			}
			if ((this._a6_rc == null && ec._a6_rc != null) || (this._a6_rc != null && ec._a6_rc == null) || (this._a6_gf != null && !this._a6_gf.plyequal(ec._a6_gf)))
			{
				return false;
			}




		}
		else
		{
			if (_rc != null && this._rc.modulus().compareTo(ec._rc.modulus()) != 0)
			{
				return false;
			}

			if ((this._a1_rc == null && ec._a1_rc != null) || (this._a1_rc != null && ec._a1_rc == null) || (this._a1_rc != null && this._a1_rc.compareTo(ec._a1_rc) != 0))
			{
				return false;
			}
			if ((this._a2_rc == null && ec._a2_rc != null) || (this._a2_rc != null && ec._a2_rc == null) || (this._a2_rc != null && this._a2_rc.compareTo(ec._a2_rc) != 0))
			{
				return false;
			}
			if ((this._a3_rc == null && ec._a3_rc != null) || (this._a3_rc != null && ec._a3_rc == null) || (this._a3_rc != null && this._a3_rc.compareTo(ec._a3_rc) != 0))
			{
				return false;
			}
			if ((this._a4_rc == null && ec._a4_rc != null) || (this._a4_rc != null && ec._a4_rc == null) || (this._a4_rc != null && this._a4_rc.compareTo(ec._a4_rc) != 0))
			{
				return false;
			}
			if ((this._a6_rc == null && ec._a6_rc != null) || (this._a6_rc != null && ec._a6_rc == null) || (this._a6_rc != null && this._a6_rc.compareTo(ec._a6_rc) != 0))
			{
				return false;
			}




		}


		return true;
	}



	public boolean setRandomPoint(boolean subfield) throws FFaplAlgebraicException
	{
		if (!this.parametersSet()) return false;
		this._isPAI = false;

		if (this.isGf())
			return this.setRandomPointGF(subfield);
		else //RC
			return this.setRandomPointRC();
	}

	private boolean setRandomPointGF(boolean subfield) throws FFaplAlgebraicException {

		BigInteger degree = _gf.irrPolynomial().degree().subtract(ONE);
		if (subfield)
			degree = ZERO;
		long n = degree.longValueExact() + 1;
		boolean gf2 = this._gf.characteristic().equals(TWO);

		// in case of GF(2^1) = Z_2
		if (gf2 && n == 1)
			return setRandomPointGF2();

		// create galois field element with values zero and one, needed later
		GaloisField zero = _gf.clone();
		zero.setValue(new Polynomial(_thread));
		GaloisField one = _gf.clone();
		one.setValue(new Polynomial(1, 0, _thread));

		// precalculate values for char 2, so they dont have to be calculated each loop cycle
		Matrix<ResidueClass> base = null;

		// loop until a valid point is found
		long tries = 0;
		do {
			++tries;

			this._x_gf = Algorithm.getRandomPolynomial(new BInteger(degree, _thread), new BInteger(_gf.characteristic(), _thread), false);
			GaloisField b = _gf.clone();
			GaloisField c = _gf.clone();
			// temporary value
			GaloisField foo = _gf.clone();

			c.setValue(new Polynomial(0,0, _thread));

			foo.setValue(_x_gf);
			foo.pow(3);
			c.subtract(foo); //-x^3

			foo.setValue(_x_gf);
			foo.pow(2); //x^2
			foo.multiply(_a2_gf); //a_2*x^2
			c.subtract(foo); //-x^3-a_2*x^2

			foo.setValue(_x_gf);
			foo.multiply(_a4_gf);
			c.subtract(foo); //-x^3-a_2*x^2-a_4*x

			c.subtract(_a6_gf); //-x^3-a_2*x^2-a_4*x-a_6

			b.setValue(_x_gf);
			b.multiply(_a1_gf);
			b.add(_a3_gf);

			// char 2
			if (gf2) {
				if (b.equals(zero)) { // B(x) == 0

					// thus y = +- sqrt( -C(x) )
					// however, in GF(2^n) these two solutions are identical
					_y_gf = Algorithm.sqrt(c,null).value();

				} else { // B(x) != 0

					// lowercase b variable used in the thesis
					GaloisField b_ = c.divR(b.powR(TWO));

					if (base == null) {
						base = _gf.getNormalBase();
						Matrix.prepareForSolving(base);
					}

                    // solve base * bBaseForm = bVector (as a classic Ax=b system of linear equations)
					// to express b as linear combination of base elements
					// (base is pre-calculated, it does not need to change over subsequent iterations)
					TreeMap<Long, ResidueClass> bBaseForm = Matrix.solve(base, convertToVector(b_), false);

					// for a solution to exist, the sum over the coefficients of the base form of b has to be zero
					boolean hasSolution = true;
					for (Map.Entry<Long, ResidueClass> e :
							bBaseForm.entrySet()) {
						hasSolution ^= e.getValue()._value.equals(ONE);
					}

					// skip, if test fails
					if (!hasSolution)
						continue;

					// the polynomial t, expressed through the base of the powers of alpha
					TreeMap<Long, ResidueClass> tBaseForm = new TreeMap<>();
					// t_0 is in {0,1}, choose one at random
					ResidueClass t_i = new ResidueClass(new RNG_Placebo(ZERO, ONE, _thread).next(), TWO);
					if (!t_i._value.equals(ZERO))
						// indexing begins at 1
						tBaseForm.put(1L, t_i);

					// t_i+1 = t_i + b_i+1
					for (long i = 2; i <= n; i++) {
						ResidueClass b_ip1 = bBaseForm.get(i);
						if (b_ip1 != null)
							t_i = t_i.addR(b_ip1);

						if (!t_i._value.equals(ZERO))
							tBaseForm.put(i, t_i);
					}

					// expand t into normal form by multiplying with the base
					// t = t_0 * alpha + t_1 * alpha^2 + t_2 * alpha^4 + .... + t_n-1 * alpha^2^n-1
                    TreeMap<Long, ResidueClass> t = base.multiplyVectorRight(tBaseForm);

                    GaloisField tField = new GaloisField(_gf.characteristic(), _gf.irrPolynomial(), _thread);
					tField.setValue(convertToPolynomial(t, _thread));

					// y = t*B(x)
					_y_gf = tField.multR(b).value();
				}

			} else { //Charakteristik != 2

				GaloisField d = _gf.clone();
				d.setValue(b.value());
				d.pow(2);
				d.divide(valueOf(4));
				d.subtract(c); //b^2/4 - c

				d.setValue(Algorithm.sqrt(d,null).value());
				
				foo.setValue(b.value());
				foo.divide(TWO);
				foo = foo.negate();

				if (new RNG_Placebo(ZERO, ONE, _thread).next().equals(ONE))
				{
					foo.add(d);
				}
				else
				{
					foo.subtract(d);
				}

				this._y_gf = foo.value();
			}

			// after 1000 tries, warn user of slow operation
			if (tries == 1000 && (Thread.currentThread() instanceof FFaplInterpreter))
				((FFaplInterpreter) (Thread.currentThread())).getLogger().displaySlowOperationWarning();

		} while (!weierstrassEquationIsValid() || (subfield && !this._y_gf.isConstant()));

		return true;
	}

    private boolean setRandomPointGF2() throws FFaplAlgebraicException {
		// check the four trivial cases (those are the only possible ones)
		Polynomial[] p = {new Polynomial(_thread),
				new Polynomial(1, 0, _thread)};

		for (int i = 0; i <= 1; i++) {
			for (int j = 0; j <= 1; j++) {
				_x_gf = p[i];
				_y_gf = p[j];
				if (weierstrassEquationIsValid())
					return true;
			}
		}

		throw new FFaplAlgebraicException(new String[]{_gf.toString()}, IAlgebraicError.EC_NO_POINTS);
		// return false; // as all possible values have been checked
	}

	private boolean setRandomPointRC() throws FFaplAlgebraicException {
		do {
			BigInteger b, c, d;
			this._x_rc = new BInteger(new RNG_Placebo(this._rc.modulus(), _thread).next(), _thread);
			b = _a1_rc.multiply(_x_rc).add(_a3_rc);
			c = _x_rc.pow(3).add(_a2_rc.multiply(_x_rc.pow(2))).add(_a4_rc.multiply(_x_rc)).add(_a6_rc);

			d = b.pow(2).divide(valueOf(4)).add(c);
			d = d.mod(_rc.modulus());

			if (new RNG_Placebo(ZERO, ONE, _thread).next().equals(ONE)) {
				this._y_rc = new BInteger(b.divide(TWO).negate().add(Algorithm.sqrtMod(d, _rc.modulus(), false)), _thread);
			} else {
				this._y_rc = new BInteger(b.divide(TWO).negate().subtract(Algorithm.sqrtMod(d, _rc.modulus(), false)), _thread);
			}
			this._y_rc = new BInteger(this._y_rc.mod(_rc.modulus()), _thread);
		} while (!weierstrassEquationIsValid());

		return true;
	}

    public static TreeMap<Long, ResidueClass> convertToVector(GaloisField gf) {
        return convertToVector(gf.value(), gf.characteristic());
    }

    public static TreeMap<Long, ResidueClass> convertToVector(Polynomial poly, BigInteger modulus) {
        // prepare the polynomial as "right side" of Ax=b system of linear equations or for matrix multiplications
        TreeMap<Long, ResidueClass> vector = new TreeMap<>();

        for (Map.Entry<BigInteger, BigInteger> entry : poly._polynomialMap.entrySet()) {
            // offset +1 for keys, as matrix indices start at one
            vector.put(entry.getKey().longValue() + 1, new ResidueClass(entry.getValue(), modulus));
        }

        return vector;
    }

    public static Polynomial convertToPolynomial(TreeMap<Long, ResidueClass> t, Thread _thread) {
        TreeMap<BigInteger, BigInteger> tMap = new TreeMap<>();

        // transform t into needed format for cast to polynomial
        // (matrix indices start at one)
        t.forEach((exponent, coefficient) -> tMap.put(valueOf(exponent - 1), coefficient._value));

        return new Polynomial(tMap, _thread);
    }

	public BigInteger getOrder() throws FFaplAlgebraicException
	{
		if (this.getPAI()) return new BInteger(ZERO,null);

		BigInteger i = ZERO;
		EllipticCurve e = this.clone();
		while (!e.getPAI())
		{
			e.add(this);
			i = i.add(ONE);
		}

		i = i.add(ONE);

		return new BInteger(i,null);
	}



	public GaloisField getSlope(Polynomial x, Polynomial y) throws FFaplAlgebraicException
	{
		if (!this._isGf)
		{
			//TODO: Internal error
			return null;
		}

		GaloisField slope = this._gf.clone();
		GaloisField temp1 = this._gf.clone();
		GaloisField temp2 = this._gf.clone();

		temp1.setValue(x);
		temp1.pow(2);
		temp1.multiply(valueOf(3));
		slope = temp1.clone(); //3x^2

		temp1.setValue(x);
		temp1.multiply(TWO);
		temp1.multiply(_a2_gf);
		slope.add(temp1); //3x^2 + 2*x*a2

		slope.add(_a4_gf); //3x^2 + 2*x*a2 + a4

		temp1.setValue(y);
		temp1.multiply(_a1_gf);
		slope.subtract(temp1); //3x^2 + 2*x*a2 + a4 - a1*y1




		temp1.setValue(y);
		temp1.multiply(TWO);


		temp2.setValue(x);
		temp2.multiply(_a1_gf);
		temp1.add(temp2); //2y + a1*x

		temp1.add(_a3_gf); //2y + a1*x + a3

		if (temp1.value().isZero()) return null;

		slope.divide(temp1); //slope


		return slope;
	}












	public boolean equals(EllipticCurve ec) throws FFaplAlgebraicException
	{
		// note: the following structure guarantees that no literal <<PAI>> values
		// will be handed to .equalEC

		// if one is PAI, it suffices to check if the other is PAI as well (and compatible)
		if (this._isPAI || ec._isPAI) {
			// if the other is not PAI, they are clearly not equal
			if (!(this._isPAI && ec._isPAI))
				return false;

			// if one is a literal (not associated with any specific EC), they are compatible
			if (!this.parametersSet() || !ec.parametersSet())
				return true;

			// otherwise, compare their ECs
			return this.equalEC(ec);
		}

		/*
		 * Ansonsten wird ueberprueft ob es sich um denselben Grundkörper handelt
		 */
		if (! this.equalEC(ec))
		{
			return false;
		}


		if (this._isGf) {
			//GF Koordinaten vergleichen
			return this._x_gf.equals(ec._x_gf) && this._y_gf.equals(ec._y_gf);
		} else {
			//RC Koordinaten vergleichen
			return this._x_rc.equals(ec._x_rc) && this._y_rc.equals(ec._y_rc);
		}


	}

	@Override
	public boolean equalType(Object type) {
		if(type instanceof EllipticCurve){
			try {
				return equalEC((EllipticCurve) type);
			} catch (FFaplAlgebraicException e) {
				//
			}
		}
		return false;
	}





	//For Pairing ONLY!!!!
	public EllipticCurve convertToGF(GaloisField gf) throws FFaplAlgebraicException
	{
		if (this.isGf()) return this;
		if (this._isPAI) return this;

		if (!this.getRC().modulus().equals(gf.characteristic()))
		{
  			throw new FFaplAlgebraicException(null, IAlgebraicError.EC_PAIRING_PARAMETER_NOT_VALID);
		}

		EllipticCurve ec = new EllipticCurve(this._thread);

		ec._gf = gf.clone();
		ec._a1_gf.setPolynomial(this._a1_rc, ZERO);
		ec._a2_gf.setPolynomial(this._a2_rc, ZERO);
		ec._a3_gf.setPolynomial(this._a3_rc, ZERO);
		ec._a4_gf.setPolynomial(this._a4_rc, ZERO);
		ec._a6_gf.setPolynomial(this._a6_rc, ZERO);

		ec._x_gf.setPolynomial(this._x_rc, ZERO);
		ec._y_gf.setPolynomial(this._y_rc, ZERO);

		ec._isGf = true;
		ec.mod();

		return ec;
	}


}