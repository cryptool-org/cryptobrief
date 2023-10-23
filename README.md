# What is Sunset/FFapl?
**Sunset** is the name of the integrated development enviroment that embodies
the compiler for the **finite field application language (FFapl)**.

Why? ...try locating yourself on any of the four grid places, to get an answer as to *why should I be interested?*

![Where would you put yourself in terms of programming/number theory skills?](https://github.com/stefan-rass/sunset-ffapl/blob/master/why-sunset.png)

Sunset/FFapl is an eLearning tool, following a simple philosophy: *implementing
cryptographic protocols and algorithms should be easy!*

To this end, FFapl supports algebraic structures commonly appearing in cryptographic
schemes as *native data types*. For example, to work in the group of residuals
modulo some prime number p, one simply declares the prime as a constant and the
variables to work with in the group.
```Java
const p: Prime := 13;
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
* Max-Julian Jakobitsch: Implemented random points on elliptic curves over fields of degree 2, plus fixing various bugs 
* Manuel Langer: fixed compatibility issues with Java 9 (removal of deprecated classes like JAXB), numerous bugfixes and integration of advanced search & replace function in the GUI
* Dominic Weinberger: support for typecasts by isomorphism between Galois fields of the same order

For questions and other inquiries, feel free to send an email to the repository maintainer [Stefan Rass](mailto:stefan.rass@aau.at?subject=Sunset-FFapl)

# Installation Instructions
The folder [nsis_installer](https://github.com/stefan-rass/sunset-ffapl/tree/master/nsis_installer) contains a Windows installer that can be downloaded and installed as it is. For all other platforms, feel free to copy the subfolder [nsis_installer/sunset](https://github.com/stefan-rass/sunset-ffapl/tree/master/nsis_installer/sunset) to any place on your computer and run `sunset.jar` (requires Java 9 Runtime or later).

**Note**: to avoid a bug in the JDK Swing classes [reported here](https://stackoverflow.com/questions/13575224/comparison-method-violates-its-general-contract-timsort-and-gridlayout), make sure to add the `-Djava.util.Arrays.useLegacyMergeSort=true` option when starting Sunset/FFapl, i.e., run `sunset.jar` on the command line as `java -jar -Djava.util.Arrays.useLegacyMergeSort=true -jar Sunset.jar`, for otherwise, the "File open" dialog may not work. Opening files by dragging and dropping them into the IDE will alternatively work too.

## Compiling from source
Alternatively, the project can also be compiled from source. 
This repository contains IDE project configurations for Eclipse and IntelliJ 
(not on main branch yet). After a fresh Import, binaries (`sunset-ffapl/bin`) 
should be rebuilt before trying to execute, as they are not updated as frequently 
as the sources.

## Editing the GUI's API
When adding or changing features of the language, please make sure to update 
Sunset's API listing (right part of the editor window) accordingly. There is a 
convenient editor available for that purpose that comes 
with a user manual and also provides consistency checks to avoid errors.

Check out the [FFapl API Editor project on github](https://github.com/manlanger/FFaplAPIEditor)
