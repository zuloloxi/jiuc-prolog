compile:-    write('sources .pl or .txt for compile are in /src/main/resources/com/ugos/jiprolog/resources folder'),nl, 
write('compilation start'), nl, compile('src/main/resources/com/ugos/jiprolog/resources/jipkernel.txt'), 
compile('src/main/resources/com/ugos/jiprolog/resources/flags.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/list.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/sys.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/xsets.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/setof.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/xio.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/xdb.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/xexception.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/xreflect.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/xsystem.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/xterm.pl'), 
compile('src/main/resources/com/ugos/jiprolog/resources/xxml.pl'), 
write('compilation done'), nl.

%:- compile.
