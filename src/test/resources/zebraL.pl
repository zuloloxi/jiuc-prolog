% ####################################################################
% Zebra puzzle solver. A house is a list with 6 attributes:
% 1. Color
% 2. Nationality
% 3. Drink
% 4. Cigarettes
% 5. Pet
% 6. The housenumber (fixed by hand to avoid redundant solutions)
%
% Steps to obtain the solution:
%
% 1) Read definition to prolog:
%   ['zebra_puzzle.pl'].
%
% 2) Ask for solutions:
%   solve(H1,H2,H3,H4,H5).
%
% Rainhard Findling
% 09/2015
% ####################################################################
% abs2/2
abs2(X,Y) :- X < 0 -> Y is -X ; Y = X.
% member 
all_members([H],L2) :- member(H,L2).
all_members([H|T],L2) :- member(H,L2), all_members(T, L2).
 
% ####################################################################
% RULES
% ####################################################################
 
% rule 1 (5 houses) is implicit for our implementation
 
rule2(H1,H2,H3,H4,H5) :-
    member(H, [H1,H2,H3,H4,H5]),
    H = [red,england,_,_,_,_].
 
rule3(H1,H2,H3,H4,H5) :-
    member(H, [H1,H2,H3,H4,H5]),
    H = [_,spain,_,_,dog,_].
 
rule4(H1,H2,H3,H4,H5) :-
    member(H, [H1,H2,H3,H4,H5]),
    H = [green,_,coffee,_,_,_].
 
rule5(H1,H2,H3,H4,H5) :-
    member(H, [H1,H2,H3,H4,H5]),
    H = [_,ukraine,tea,_,_,_].
 
rule6(H1,H2,H3,H4,H5) :-
    member(HL,[H1,H2,H3,H4,H5]),
    member(HR,[H1,H2,H3,H4,H5]),
    HL = [white,_,_,_,_,NrL],
    HR = [green,_,_,_,_,NrR],
    NrR-NrL =:= 1.
 
rule7(H1,H2,H3,H4,H5) :-
    member(H, [H1,H2,H3,H4,H5]),
    H = [_,_,_,altemgold,snails,_].
 
rule8(H1,H2,H3,H4,H5) :-
    member(H, [H1,H2,H3,H4,H5]),
    H = [yellow,_,_,kools,_,_].
 
rule9(_,_,H3,_,_) :-
    H3 = [_,_,milk,_,_,3].
 
rule10(H1,_,_,_,_) :-
    H1 = [_,norway,_,_,_,1].
 
rule11(H1,H2,H3,H4,H5) :-
    member(HL,[H1,H2,H3,H4,H5]),
    member(HR,[H1,H2,H3,H4,H5]),
    HL = [_,_,_,chesterfield,_,NrL],
    HR = [_,_,_,_,fox,NrR],
    abs2(NrL-NrR,1).
 
rule12(H1,H2,H3,H4,H5) :-
    member(HL,[H1,H2,H3,H4,H5]),
    member(HR,[H1,H2,H3,H4,H5]),
    HL = [_,_,_,kools,_,NrL],
    HR = [_,_,_,_,horse,NrR],
    abs2(NrL-NrR,1).
 
rule13(H1,H2,H3,H4,H5) :-
    member(H, [H1,H2,H3,H4,H5]),
    H = [_,_,orange,luckystrike,_,_].
 
rule14(H1,H2,H3,H4,H5) :-
    member(H, [H1,H2,H3,H4,H5]),
    H = [_,japan,_,parliament,_,_].
 
rule15(H1,H2,H3,H4,H5) :-
    member(HL,[H1,H2,H3,H4,H5]),
    member(HR,[H1,H2,H3,H4,H5]),
    HL = [_,norway,_,_,_,NrL],
    HR = [blue,_,_,_,_,NrR],
    abs2(NrL-NrR,1).
 
% ####################################################################
% solve the puzzle
% ####################################################################
 
solve(H1,H2,H3,H4,H5) :-
    % bind houses to position to avoid multiple solutions with switched variables
    H1 = [H1_col,H1_nat,H1_drink,H1_cig,H1_pet,1],
    H2 = [H2_col,H2_nat,H2_drink,H2_cig,H2_pet,2],
    H3 = [H3_col,H3_nat,H3_drink,H3_cig,H3_pet,3],
    H4 = [H4_col,H4_nat,H4_drink,H4_cig,H4_pet,4],
    H5 = [H5_col,H5_nat,H5_drink,H5_cig,H5_pet,5],
    % the rules
    rule2(H1,H2,H3,H4,H5),
    rule3(H1,H2,H3,H4,H5),
    rule4(H1,H2,H3,H4,H5),
    rule5(H1,H2,H3,H4,H5),
    rule6(H1,H2,H3,H4,H5),
    rule7(H1,H2,H3,H4,H5),
    rule8(H1,H2,H3,H4,H5),
    rule9(H1,H2,H3,H4,H5),
    rule10(H1,H2,H3,H4,H5),
    rule12(H1,H2,H3,H4,H5),
    rule13(H1,H2,H3,H4,H5),
    rule14(H1,H2,H3,H4,H5),
    rule15(H1,H2,H3,H4,H5),
    % for all variable ensure that all values exist
    all_members([white, green, red, blue, yellow], [H1_col,H2_col,H3_col,H4_col,H5_col]),
    all_members([spain, japan, england, ukraine, norway], [H1_nat,H2_nat,H3_nat,H4_nat,H5_nat]),
    all_members([orange, coffee, milk, tea, water], [H1_drink,H2_drink,H3_drink,H4_drink,H5_drink]),
    all_members([luckystrike, parliament, altemgold, chesterfield, kools], [H1_cig,H2_cig,H3_cig,H4_cig,H5_cig]),
    all_members([dog, zebra, snails, horse, fox], [H1_pet,H2_pet,H3_pet,H4_pet,H5_pet]).
	
	/*
	If executed, two possible solutions are printed:

1
2
3
4
5
6
7
8
9
10
11
12
13
14
['zebra_puzzle.pl'].
solve(H1,H2,H3,H4,H5).
 
H1 = [yellow, norway, water, kools, zebra, 1],
H2 = [blue, ukraine, tea, chesterfield, horse, 2],
H3 = [red, england, milk, altemgold, snails, 3],
H4 = [white, spain, orange, luckystrike, dog, 4],
H5 = [green, japan, coffee, parliament, fox, 5] ;
 
H1 = [yellow, norway, water, kools, fox, 1],
H2 = [blue, ukraine, tea, chesterfield, horse, 2],
H3 = [red, england, milk, altemgold, snails, 3],
H4 = [white, spain, orange, luckystrike, dog, 4],
H5 = [green, japan, coffee, parliament, zebra, 5] ;
That’s it, puzzle solved! 
*/