
/* 
 I, Shahar Azoulai (313612897), assert that the work I submitted is entirely my own.
 I have not received any part from any other student in the class, nor did I give parts of it for use to others.
 I realize that if my work is found to contain code that is not originally my own, a
 formal case will be opened against me with the BGU disciplinary committee.
*/

// Last Update: 02/12/19

public class Assignment2 {

	
	


    /*--------------------------------------------------------
	   Part a: instance representation & Solution verification
	  -------------------------------------------------------
	 */

    public static boolean isContaining(int[] arr, int x) {                           //gets: "arr" - array of integeres; "x" - integer.
        boolean output=false;                                                       //returns true if "arr" contains the value of "x". otherwise returns false(does not contains).
        for (int i = 0; i < arr.length & !output; i=i+1)
            output=arr[i]==x;


        return output;
    }

    public static int numberOfShows(int[] arr, int x){                      //gets: "arr" - array of integeres; "x" - integer.
        int counter=0;                                                      //returns: the number of times "x" is in "arr".
        for (int i = 0; i < arr.length; i=i+1)
            if(arr[i]==x)
                counter=counter+1;

        return counter;
    }

    // Task 1
    public static boolean hasFlight(int[][] flights, int i, int j) {        //gets: "flights" - 2D array of integeres; "i", "j" - integers.
        return isContaining(flights[i],j);                                   //returns: "true" if there is a flight between i and j. "false" if not. e.g: if flights[i](=<int[]>) contains j.
    }

    // Task 2
    public static boolean isLegalInstance(int[][] flights) {                //gets: "flights" - 2D array of integeres.
        boolean output=true;                                                //returns: "true" if flights is a legal instance according to a,b,c,d.

        if(flights==null||flights.length<=1)                    //a. flights[][] is null or shorter than 2(exc 2)
            output= false;

        for(int i = 0; output && i < flights.length; i=i+1) {
            if((flights[i]==null)||(flights[i].length<1))                                //b. check if flights[i] is null or empty
                output= false;

            for (int j = 0; output &&  j < flights[i].length; j = j + 1) {
                if((flights[i][j]<0)||(flights[i][j]>flights.length-1)||(flights[i][j]==i))         //b+d. checks that value is in range(0<=x<=n-1) + flights[i] doesn't contain i
                    output= false;

                if (i!=j)
                    if (isContaining(flights[i],j)!=isContaining(flights[j],i))                     //c. flights[i] contains j iff flight[j] contains i
                        output= false;
            }
        }

        return output;
    }

    // Task 3
    public static boolean isSolution(int[][] flights, int[] tour) {         //gets: "flights" - 2D array of integeres; "tour" - array integers.
        boolean output=true;                                                //returns: "true" if "tour" is a possible solution given the 2D array "flights".
        int n= flights.length;

         if((tour==null)||(tour.length!=n))                                 //throw exception if tour[] is null or its length different from n
            throw new IllegalArgumentException("boolean isSolution(int[][], int[]): tour length is different from flights length or is null");

         for (int i=0; i < n; i=i+1)
             if (tour[i] >= n | tour[i]<0)                                           //throw exception if tour contains values out of range
                 throw new IllegalArgumentException("boolean isSolution(int[][], int[]): tour contains values out of range");

        for (int i = 0; i < n & output; i=i+1)               //a. checks each city exists only 1 time
            if(numberOfShows(tour,i)!=1)
                output=false;

         if((!hasFlight(flights,tour[0],tour[tour.length-1])) & output)   //b. (*) checks flight between flights[n-1] and flights[0].
            output =false;


         for (int i = 0; i < tour.length-1 & output; i=i+1)                 //b.  checks flight between flights[i] and flights[i+1].
             if (!hasFlight(flights, tour[i], tour[i + 1]))
                 output = false;


        return output;
    }

    /*------------------------------------------------------
	  Part b: Express the problem as a CNF formula, solve
	  it with a SAT solver, and decode the solution
	  from the satisfying assignment
	  -----------------------------------------------------
	 */

    // Task 4
    public static int[][] atLeastOne(int[] vars) {              //gets: "vars" - array of integers representing cnf variables.
        int [][] cnf =new int[1][vars.length];                  //returns: 2D array of integers, representing a cnf clause. The clause will be satisfied iff at least one of the variables is satisfied.
        for (int i = 0; i < vars.length; i=i+1)
            cnf[0][i]=vars[i];
        return cnf;
    }

    // Task 5
    public static int[][] atMostOne(int[] vars){            //gets: "vars" - array of integers representing cnf variables.
        int ncr=1;                                          //returns: 2D array of integers, representing a cnf clause. The clause will be satisfied iff at most one of the variables is satisfied.

        for (int i = 2; i < vars.length; i=i+1)             //calculates the number of possible combinations of vars.length over 2(nCr)
            ncr=ncr+i;


        int[][] cnf=new int[ncr][2];
        int cnfNumber = 0;

        while(cnfNumber<ncr)
        for (int i = 0; i < vars.length-1; i=i+1)
            for (int j = i+1; j < vars.length; j=j+1) {
                cnf[cnfNumber][0] = (-1)* vars[i];
                cnf[cnfNumber][1] = (-1)* vars[j];
                cnfNumber=cnfNumber+1;
            }

        return cnf;
    }

    // Task 6
    public static int[][] append(int[][] arr1, int[][] arr2) {              //gets: "arr1", "arr2" - 2D arrays of integers representing cnf formulas.
        int[][] cnf = new int[arr1.length+arr2.length][];                   //returns: 2D array of integers, containing all clauses of "arr1" and then of "arr2".

        for (int i = 0; i < arr1.length; i=i+1) {                   //adds arr1 clauses
            cnf[i]=new int[arr1[i].length];
            for (int j = 0; j < arr1[i].length; j = j + 1)
                cnf[i][j] = arr1[i][j];
        }

        int secondCnfIndex =arr1.length;

        for (int i = 0; i < arr2.length; i=i+1) {                   //adds arr2 clauses
            cnf[i+secondCnfIndex]=new int[arr2[i].length];
            for (int j = 0; j < arr2[i].length; j = j + 1)
                cnf[i+secondCnfIndex][j] = arr2[i][j];
        }

        return cnf;
    }

    // Task 7
    public static int[][] exactlyOne(int[] vars){                              //gets: "vars" - array integers representing cnf variables.
        return append(atMostOne(vars),atLeastOne(vars));                        //returns: 2D array of integers, representing a cnf clause. The clause will be satisfied iff at exactly one of the variables is satisfied.(at least one & at most one).
    }

    // Task 8
    public static int[][] diff(int[] I1, int[] I2){                             //gets: "I1", "I2" - arrays of integers representing cnf variables. Each array represents a number.
        int[][] cnf = new int[I1.length][2];                                    //returns: a cnf formula forcing the numbers represented by "I1" and "I2" to be different.(!XI1 V !XI2)

        for (int i = 0; i < I1.length ; i++) {
            cnf[i][0] = (-1) * I1[i];
            cnf[i][1] = (-1) * I2[i];
         }

        return cnf;
    }

    // Task 9
    public static int[][] createVarsMap(int n) {                                //gets: "n" - an integer representing the number of cities.
        int [][] map =new int[n][n];                                            //returns: squared 2D array of integers by the size nXn containing cnf variables from 1 to n*n, sorted in an increasing order in flights[i][j]   0<=i<=n-1: 0<=j<=n-1
        for (int i = 0; i < n*n; i=i+1)
            map[i/n][i%n]=i+1;

        return map;
    }

    // Task 10
    public static int[][] declareInts(int[][] map) {                                //gets: "map" - 2D array of integers representing cnf variables.
        int [][] cnf = new int [0][];                                               //returns: a cnf formula(2D array of integers) forcing each array in map to represent exactly one number.

        for (int i = 0; i < map.length; i=i+1)
            cnf = append(cnf,exactlyOne(map[i]));

        return cnf;
    }

    // Task 11
    public static int[][] allDiff(int[][] map) {                                    //gets: "map" - 2D array of integers representing cnf variables.
        int cnf[][] = new int[0][];                                                 //returns: a cnf formula(2D array of integers) forcing all numbers represented by the arrays in "map" to be different from each other.
        for (int i = 0; i <map.length-1 ; i=i+1)
            for (int j = i+1; j < map.length; j=j+1) {

            int [][] temp={map[i],map[j]};
            cnf = append(cnf,temp);
            cnf = append(cnf,diff(map[i],map[j]));
        }
        return cnf;

    }

    // Task 12
    public static int[][] allStepsAreLegal(int[][] flights, int[][] map) {                      //gets: "flights", "map" - 2D arrays of integers.
        int [][] cnf = new int [0][];                                                           //returns: cnf formula forcing each solution to hold valid flights.
        int n=map.length;                                                                       //(from every city 'j' in stage 'i' there is a flight to at least one city 'k' in stage 'i+1')

        for (int i = 0; i < map.length; i=i+1)
            for (int j = 0; j <n ; j=j+1)
                for (int k = 0; k < n; k=k+1)                                                   //the nested loops are running over 'flights' and checking: ∀i<n: ∀j∈ flights[i], ∃k∈flights[i+1] such that hasFlight(flights,j,k)(==true)
                    if (!hasFlight(flights,j,k)) {
                        int[][] temp={{ (-1)*(j + 1 + (i*n)), (-1)*(k + 1 + ((i+1)%n)*n)}};             //using modulo we are checking the condition between the last stage(n-1) and the first stage(0)
                        cnf = append(cnf, temp);
                    }

        return cnf;
    }

    public static boolean isMapLegal(int[][] map){                                               //gets: "map" - 2D array of integers.
        boolean output=true;                                                                     //returns: 'true' if the map is valid and 'false' if not.

        if(map==null||map.length<=1)                                                                             //checking if map is null or too short
            output=false;

        int n = map.length;

        if(output)
            for (int i = 0; i < map.length & output; i=i+1)
                output=map[i]!=null && map.length>1 && output && (n==map[i].length);                            //checking that the arrays in 'map' are not null, too short and that the matrix is squared(n*n)

        if(output) {
            int cnfVar = 1;
            for (int i = 0; i < map.length & output; i = i + 1)
                for (int j = 0; j < map[i].length & output; j = j + 1) {                                       //checking that all cnf variables are present and organized in the right order
                    output = output & (map[i][j] == cnfVar);
                    cnfVar = cnfVar + 1;
                }
            output = output & ((cnfVar-1)==n*n);
        }

        return output;

    }

    // Task 13
    public static void encode(int[][] flights, int[][] map) {                                   //gets: "flights", "map" - 2D arrays of integers.
        if(flights==null || !isLegalInstance(flights) || !isMapLegal(map))                      //the function encodes the SATSolver with the relevant cnf formulas and returns void.
            throw new IllegalArgumentException("void encode(int[][], int[][]): flights or map is not legal");         //checking that 'flights' is not null and that 'flights' and 'map' are legal according to each one's definition.

        SATSolver.addClauses(declareInts(map));                                                     //for more information about these 3 lines please see the next functions' comments:
        SATSolver.addClauses(allDiff(map));                                                         // : declareInts(int[][]), allDiff(int[][]), allStepsAreLegal(int[][], int[][]).
        SATSolver.addClauses(allStepsAreLegal(flights,map));
    }

    // Task 14
    public static int[] decode(boolean[] assignment, int[][] map) {                             //gets: "assignment" - array of booleans representing a possible solution for an instance of the problem, "map" - 2D array of integers.
        int n=map.length;                                                                       //returns: 'tour' - an array of integers representing a possible tour. each city of stage 'i' is being stored in cell [i-1].

        if (assignment.length!= (n*n)+1)                                                        //checking that assignment has a valid length for the given instance.
            throw new IllegalArgumentException("int[] decode(boolean[], int[][]): assignment[] length is not valid");

        int[] tour = new int[n];

        for (int i = 1; i < assignment.length ; i++)                                            //convertion of 'assignment' to 'tour'.
            if(assignment[i])
                tour[(i-1)/n]=(i-1)%n;

        return tour;
    }

    // Task 15
    public static int[] solve(int[][] flights) {                                                                                         //gets: "flights" - 2D array of integers representing an instance of "The big trip" problem.
        if(flights==null || !isLegalInstance(flights))      //checking that 'flights is a legal instance and not null                 //returns: 'tour' - an array of integers representing a possible tour. each city of stage 'i' is being stored in cell 'i-1'.
            throw new IllegalArgumentException("int[] solve(int[][]): flights is not a legal instance");

        int[] s=null;
        int n = flights.length;                             //n= number of cities
        int[][] map = createVarsMap(n);                     //creating the map array representing all stages in every possible city


        SATSolver.init(n*n);                        //initializing the SATSolver
        encode(flights, map);                               //adding to the SATSolver the relevant cnf formulas for the problem
        boolean[] assignment = SATSolver.getSolution();

        if(assignment==null)                                    //according to the SATSolver's API: null ==> TIMEOUT
            throw new IllegalArgumentException("int[] solve(int[][]): TIMEOUT");

        if(assignment.length!=0) {
            s = decode(assignment, map);

            if (!isSolution(flights, s))
                throw new IllegalArgumentException("nt[] solve(int[][]): Solution is not legal.(check constraints)");
        }


        return s;
    }

    
        // Task 16
    public static boolean solve2(int[][] flights, int s, int t) {
           if(flights==null || !isLegalInstance(flights))      //checking that 'flights is a legal instance and not null                 //returns: 'tour' - an array of integers representing a possible tour. each city of stage 'i' is being stored in cell 'i-1'.
            throw new IllegalArgumentException("int[] solve2(int[][] ,int ,int): flights is not a legal instance");

        int n = flights.length;                             //n= number of cities
        int[][] map = createVarsMap(n);                     //creating the map array representing all stages in every possible city
        boolean hasTwo=false;
        int[] tour=null;

        SATSolver.init(n*n);                        //initializing the SATSolver
        encode(flights, map);                               //adding to the SATSolver the relevant cnf formulas for the problem
        int[][] st={{map[0][s]},{map[n-1][t]}};
        SATSolver.addClauses(st);                           //forcing the solution to start from city s and finish in city t
        boolean[] assignment = SATSolver.getSolution();

        if(assignment==null)                                    //according to the SATSolver's API: null ==> TIMEOUT
            throw new IllegalArgumentException("int[] solve2(int[][] ,int ,int): TIMEOUT");

        if(assignment.length!=0) {
            tour = decode(assignment, map);

            if (!isSolution(flights, tour))
                throw new IllegalArgumentException("int[] solve2(int[][] ,int ,int): Solution is not legal.(check constraints)");

            else {                                                      //in case there is at least 1 solution we will perform the else's block of commands
                int[] vars = new int[tour.length - 2];
                for (int i = 1; i < tour.length - 1; i = i + 1)
                    vars[i - 1] = (-1)*map[i][tour[i]];                 //putting in vars[] negative cnf variables representing the cities visited in the first solution, except the first and the last.

                SATSolver.init(n*n);					//initializing the SATSolver
                encode(flights, map);					//adding to the SATSolver the relevant cnf formulas for the problem
                SATSolver.addClauses(st);				//forcing the solution to start from city s and finish in city t
                SATSolver.addClauses(atLeastOne(vars));			//forcing the solution to be without at least one of the cnf vaiables from the previous solution
                assignment = SATSolver.getSolution();

                if(assignment==null)                                    //according to the SATSolver's API: null ==> TIMEOUT
                    throw new IllegalArgumentException("int[] solve2(int[][] ,int ,int): TIMEOUT");

                if(assignment.length!=0) {
                    hasTwo=true;

                    if (!isSolution(flights, tour))
                        throw new IllegalArgumentException("int[] solve2(int[][] ,int ,int): Solution is not legal.(check constraints)");
                }
            }
        }

        return hasTwo;
    }


}
