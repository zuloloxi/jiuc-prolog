%lab3_find_set_bagof
/*
Predicate care genereaza liste
In Prolog exista cateva predicate predefinite, descrise mai jos, care genereaza liste.

bagof(?Element, :Scop, ?Lista)
Va pune in Lista toate valorile lui Element (reprezentat in bagof ca variabila sau ca termen compus 
ce contine una sau mai multe variabile) care ideplinesc Scop. Daca in Scop exista variabile care nu 
se regasesc in Element, atunci pentru fiecare combinatie de valori posibile ale acestor variabile 
astfel incat Scop sa fie indeplinit se va calcula o Lista corespunzatoare. In cazul in care nu exista 
solutii ce pot fi puse in Lista, bagof returneaza no.

setof(?Element, :Scop, ?Lista)
Va pune in Lista toate valorile lui Element care ideplinesc Scop, insa Lista va fi ordonata si fara 
duplicate (elementele egale sunt eliminate). In cazul variabilelor libere din Scop, setof are 
comportament similar cu bagof.

findall(?Element, :Scop, ?Lista)
Va pune in Lista toate valorile lui Element care indeplinesc Scop. Spre deosebire de bagof, 
findall va pune toate solutiile in lista indiferent de variabilele libere existente in Scop, 
oferind astfel o singura solutie. In cazul in care nu exista solutii ce pot fi puse in Lista, 
findall spre deosebire de bagof se termina cu succes (returneaza yes) iar lista calculata 
va fi vida.

^ cuantificatorul existential
Variabila ^ predicat(...)
Este adevarat daca exista o valoare pentru variabila astfel incat predicatul sa aiba valoarea true
Se foloseste in general in parametrul al doilea al predicatelor bagof ori setof.
Exemplu simplu bagof, setof, findall
Mai jos aveti intreg programul cu toate exemplele. Sub acest cod va fi explicat fiecare 
exemplu(predicat) in parte continand setof, bagof, findall. Incercati sa cititi explicatiile 
si sa faceti interogarile pentru fiecare exemplu.
*/

a(7).
a(0).
a(2).
a(10).
a(7).

%toate solutiile lui a
sol_a(L):-bagof(X,a(X),L).

%sa variem forma elementelor din lista( sa apara structuri de forma nr(X))
sol_a_nr(L):-bagof(nr(X),a(X),L).
sol_a_1(L):-bagof(X+1,a(X),L).

all_a(L):-findall(X,a(X),L).

sol_a_m100(L):-bagof(X,(a(X),X>100),L).
all_m100(L):-findall(X,(a(X),X>100),L).

%solutiile lui a mai mici de 5 (conditie compusa)
sol_a_m5(L):-bagof(X,(a(X),X<5),L).

mult_sol_a(L):-setof(X,a(X),L).

%lista cu toate expresiile de forma X+Y=Rez cu X si Y solutii ale lui a. Am folosit alt predicat (auxiliar) 
%ca sa nu complic prea mult conditia compusa
cond(X,Y,R):-a(X),a(Y),R is X+Y.
exp_a(L):-bagof(X+Y=R,(cond(X,Y,R)),L).
mult_exp_a(L):-setof(X+Y=R,(cond(X,Y,R)),L).

take([H|T],H,T).
take([H|T],R,[H|S]):- take(T,R,S).
