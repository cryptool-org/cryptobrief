# What is Sunset/FFapl?
**Sunset** is the name of the integrated development enviroment that embodies
the compiler for the **finite field application language (FFapl)**.

Sunset/FFapl is an eLearning tool, following a simple philosophy: *implementing
cryptographic protocols and algorithms should be easy!*

To this end, FFapl supports algebraic structures commonly appearing in cryptographic
schemes as *native data types*. For example, to work in the group of residuals
modulo some prime number p, one simply declares the prime as a constant and the
variables to work with in the group.
```Java
const: p: Prime := 13;
g: Z(p);  // this puts all computations on g into the finite field of size (and characteristic) p
g := 7^(-1);  // compute the inverse of 7 mod 13 directly
g := 10^246434565635423565234454352; // do fast exponentiation
```

Further examples are found in [here](https://github.com/stefan-rass/sunset-ffapl/tree/master/examples), including an implementations of RSA, ElGamal (signatures and encryptions), identity based encryption, and many more.

Likewise, more complex algebraic structures like polynomial rings, finite fields
and even elliptic curves are supported by a simple syntax that attempts to resemble
the way in which these objects appear in scientific papers on cryptography.

The following structures are natively supported by now:
1. Integers (signed but without numerical limits)
1. Booleans
1. Strings
1. Residue class groups (requiring a positive modulus > 1, not necessarily prime)
1. Polynomial rings over Z(p)
1. Galois fields
1. Elliptic curves (including pairings on them)
1. Random number generators: those appear as simple data types, with a variable
of this type returning a fresh random value upon each read access to it.

A basic design paradigm is: *there are no libraries*
Libraries typically require a user to go through potentially complicated installation
(even compilation) chains, followed by learning an API to access the library's functions.
We created FFapl to support everything natively, so that there is never a question
on which package, plugin or library to get where from or how to properly include
it in some project.

Sunset/FFapl is intensively being used in security courses at the Universitaet Klagenfurt
(www.aau.at, www.syssec.at), to help students get the grips on cryptographic
protocols without needing to worry too much about the underlying complex mathematics.

Have fun with the tool, and if you like it, check out the list of features that
could be added. We appreciate your contribution to our project of making
cryptography more accessible in an easy way!

Acknowledgement:
The initial version was pushed on August 31st, 2017 with friendly permission of the Author Alexander O. Ortner (who created the system during his master thesis) and Prof.em. Dr. Patrick Horster (Universitaet Klagenfurt).

Contributions by the following people so far is thankfully acknowledged:
* Alexander Oskar Ortner: initial implementation of IDE and FFapl interpreter
* Johannes Winkler: elliptic curve support (including pairings)
* Volker Bugl: I/O support and IDE additions
* Markus Wiltsche: API extensions

For questions and other inquiries, feel free to send an email to the repository maintainer [Stefan Rass](mailto:stefan.rass@aau.at?subject=Sunset-FFapl)

# Installation Instructions
The folder [nsis_installer](https://github.com/stefan-rass/sunset-ffapl/tree/master/nsis_installer) contains a Windows installer that can be downloaded and installed as it is. For all other platforms, feel free to copy the subfolder [nsis_installer/sunset](https://github.com/stefan-rass/sunset-ffapl/tree/master/nsis_installer/sunset) to any place on your computer and run the JAR-file in it.

<span color="red">**Attention:**</span> Sunset/FFapl requires libraries that 
will be deprecated as of Java 1.9. So, to run the system **either** use a JRE version 1.8, 
**or** use the [portable version](https://github.com/stefan-rass/sunset-ffapl/tree/master/nsis_installer/PortableInstallation_for_Java9.zip) 
in the [same folder as the nsis installer](https://github.com/stefan-rass/sunset-ffapl/tree/master/nsis_installer). To run the 
program, simply extract the ZIP-file and run the JAR file in it on a **JRE version 1.9** (the jar 
has the deprecated libraries packed into it, but will not run under Java 1.8).