package com.ugos.jiprolog.example;

import java.io.IOException;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;

//import static org.junit.Assert.*;

//import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

//public class AATest {//extent JunitTest{
	//static AAtest m = new AATest();
public class SynchronousQueryTest //implements JIPEventListener
{	
	@BeforeClass 
	public static void initialize() {
		System.out.println("This is printed only once at the SynchronousQueryTest beginning");
	}
	@AfterClass 
	public static void destroy() {
		System.out.println("SynchronousQueryTest - last thing printed");
	}
/*	@Before
	public void setUp() throws Exception {
		Compile.init();
	}
*/
	@Test
	public void test() {
/*		fail("Not yet implemented");
		randomNumbers();
		testBoolean();

		algorTestWhile1();
		algorTestRepeat1();
		algorTestFor1();
		algorTestFor2();
		algorTest2();
		test5a();
		compareWithJava0();
		compareWithJava1();
		compareWithJava2();
		//assertFalse() ;
		fail(" fail ok ");*/
		try {
		  SynchronousQueryTest.main(new String[] {"arg1", "arg2", "arg3"});
	    } catch (Exception exception) {
	    exception.printStackTrace();
	    }
		System.out.println("######### PROGRAM FINISHED ###############");
	}

//public class SynchronousQuery {
    // main
    public static void main(String args[])
    {
        // New instance of prolog engine
        JIPEngine jip = new JIPEngine();

        JIPTerm queryTerm = null;

        // parse query
        try
        {
            // consult file
            jip.consultFile("familyrelationships.pl");

            queryTerm = jip.getTermParser().parseTerm("father(Father, Child)");
			System.out.println("?- father(Father, Child). -->"  + queryTerm.toString());
			System.out.println("jip ?- father(Father, Child). -->"  + queryTerm.toString(jip));
			System.out.println();
        }
        catch(JIPSyntaxErrorException ex)
        {
            ex.printStackTrace();
            System.exit(0); // needed to close threads by AWT if shareware
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            System.exit(0); // needed to close threads by AWT if shareware
        }

        // open Query
        JIPQuery jipQuery = jip.openSynchronousQuery(queryTerm);
        JIPTerm solution;
		boolean s_found = false;
        // Loop while there is another solution
        while (jipQuery.hasMoreChoicePoints() && !s_found)
        {
            solution = jipQuery.nextSolution();
			if(solution == null)
				{
					s_found = true;
				}
		    else
				{
					System.out.println("next solution: " + solution);

					JIPVariable[] vars = solution.getVariables();
					for (JIPVariable var : vars) {
						if (!var.isAnonymous()) {
							System.out.print(var.getName() + " = " + var.toString(jip) + " ");
							System.out.println();
						}
					}
				}
        }
    }
}
