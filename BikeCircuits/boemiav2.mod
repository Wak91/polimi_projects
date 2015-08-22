#UTILITY
#-----------
#http://www.ampl.com/FAQ/tsp.mod

#INFO
#This is the model version with the costraint of no sub cycle in the .run with an algorithm that breaks the cycle that aren't connected to the destination 

# SETS

param begin;
param end > begin;
set City := begin .. end; #City in Boemia

set Arcs within {City,City};
set Edge within {City,City} := {(i,j) in Arcs : i<j}; #The set Edge contains the arcs (i,j) in Arcs and its reverse

set Nst default {} ;
set Nstn default {} ;
set ENstn dimen 2 default {(1,2)};

param nc >=0, integer default 0; #Number of cuts added 
set nst_cut{1..nc} within City; #Vector of set that represent the cuts Nst
set nstn_cut{1..nc} within City; #Vector of set that represent the cuts Nstn

param edges{1..nc}, integer default 1;

#PARAM

param cost{Edge}>=0 ; #Cost of edge's recovery ( According to the paper  the cost is defined on edge, not on arcs )  
param g{Edge}>=0 ; #Attractiveness of the edge the first time  Oij(1) 
param k{City}>=0 ; #Attractiveness of City 

param f{Edge}; #According to the paper this is the difference of attractiveness from the first visit to the second.
param tm {Arcs}>=0; #The time spent to cross an arc

param T; #Max duration of a trip
param B; #Max budget

param s symbolic in City; #This is the source of the bike ride
param t symbolic in City; #The destination of the bike ride, s and t must be in the Nst set according to the defined cut


#VARS

var x{Arcs}>=0 integer; #Number of times that i cross arc (i,j)
var y{Edge} binary; #This tell us if the edge is crossed exactly 2 times 
var z{Edge} binary; #This tell us if the edge is used or not in the path from s to t


#OBJECTIVE FUNCTION
maximize pleasure:
				sum{(i,j) in Edge} ( g[i,j]*x[i,j] + g[i,j]*x[j,i] - f[i,j]*y[i,j] );

#COSTRAINTS

#Costraint on graph 
subject to flow_costraint_1:
				sum{(s,j) in Arcs } x[s,j] = 1;  #Only one arc goes out from the source 
				
subject to flow_constraint_1_bis:
				sum{(s,j) in Arcs } x[j,s] = 0;  #No arc goes into the source 

subject to flow_costraint_2:
				sum{(i,t) in Arcs } x[i,t] = 1;  #Only one arc goes into the destination
				
subject to flow_costraint_2_bis:
				sum{(i,t) in Arcs } x[t,i] = 0;  #No arc goes goes out from the destination
				
subject to flow_costraint_3 {i in City: i!=s and i!=t}:  #The number of arcs that goes into an intermediate node is equal to the number of arcs that goes out the node
				sum{(h,i) in Arcs} x[h,i] = sum{(i,j) in Arcs} x[i,j];	
				

#Balance costraint 

subject to balance_5 { (i,j) in Edge }:
				 x[i,j] <= 2*z[i,j];
subject to balance_6 { (i,j) in Edge }: 
				 x[j,i] <= 2*z[i,j];
subject to balance_7 { (i,j) in Edge }:
				 y[i,j] >= x[i,j] + x[j,i] -1;
subject to balance_8 { (i,j) in Arcs }:
				 x[i,j] + x[j,i] <= 2;

#No sub-cycle not connected to the destination 
subject to cyclecostraint {e in 1..nc}:
				 sum{ i in nstn_cut[e], j in nstn_cut[e]: (i,j) in Edge } z[i,j] <=  ( ( sum{ (i,j) in Arcs: i in nstn_cut[e] 
				 and j in nst_cut[e] } x[i,j] ) * edges[e] );

#Resource costraint

subject to resource_9:
				sum{(i,j) in Edge } ( cost[i,j]*z[i,j] ) <= B ;
subject to resource_10:
				sum{(i,j) in Arcs } ( tm[i,j] * x[i,j] ) <= T;

#Domain costraint 

subject to domain_11 {(i,j) in Edge }:
				 z[i,j] = x[i,j] + x[j,i] - y[i,j];


