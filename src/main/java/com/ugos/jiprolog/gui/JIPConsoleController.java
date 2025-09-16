/*
 * 09/19/2002
 *
 * Copyright (C) 1999-2005 Ugo Chirico
 *
 * This is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


package com.ugos.jiprolog.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPDebugger;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPErrorEvent;
import com.ugos.jiprolog.engine.JIPEvent;
import com.ugos.jiprolog.engine.JIPEventListener;
import com.ugos.jiprolog.engine.JIPRuntimeException;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTraceEvent;
import com.ugos.jiprolog.engine.JIPTraceListener;
import com.ugos.jiprolog.engine.JIPVariable;
import com.ugos.jiprolog.igui.IJIPConsoleController;
import com.ugos.jiprolog.igui.IJIPConsoleView;

public class JIPConsoleController implements IJIPConsoleController, JIPEventListener, JIPTraceListener
{
    //public static final String TITLE   = "JIProlog - Java Internet Prolog";
	public static final String TITLE   = "JI Prolog "+ JIPEngine.getVersion();
    public static final String VERSION = JIPEngine.getVersion();
    public static final String PROMPT  = "| ?- "; //"JI?- "
	
    private Frame m_mainFrame;

    // Prolog Engine
    private JIPEngine        m_prolog;

    // Dialog for Tracing
    private TraceDialog      m_traceDlg;

    private IJIPConsoleView  m_consoleView;

    // Query Handle
    private int              m_nQueryHandle = -1;

    // Stream
    private PrintStream      m_outs = null;
    
    // Trace PrintStrem
    private PrintStream      m_traceOuts = null;
    

    // Constructor
    public JIPConsoleController(IJIPConsoleView consoleView, String strCodeBase)
    {
        consoleView.setController(this);

        m_mainFrame = consoleView.getMainFrame();

        m_consoleView = consoleView;

        try
        {            
			Thread.sleep(1500);
        }
        catch(InterruptedException ex)
        {}

        // New instance of prolog engine
        m_prolog = new JIPEngine();

//        // env var
//        Map<String, String> env = System.getenv();
//        for (String envName : env.keySet())
//        {
//        	String val = env.get(envName);
//        	System.out.println(envName + "="+val);
//        	m_prolog.setEnvVariable(envName, val);
//        }
//
//        Properties props = System.getProperties();
//        for (Object envName : props.keySet())
//        {
//        	String val = props.getProperty((String)envName);
//        	System.out.println("props " + envName + "="+val);
//        	if(val != null)
//        		m_prolog.setEnvVariable((String)envName, val);
//        }

        // Set OutputStream
        try
        {
            m_outs = m_consoleView.getPrintStream(m_prolog.getEncoding());
        }
        catch (UnsupportedEncodingException e)
        {

        }

        m_prolog.setUserOutputStream(m_outs);
		m_prolog.setUserInputStream(new InputStreamReader(m_consoleView.getInputStream()));

        // Add EventListener
        m_prolog.addEventListener(this);

         // Trace Dialog
        m_traceDlg = new TraceDialog(m_mainFrame, m_prolog);

        m_prolog.addTraceListener(m_traceDlg);

        EditFrame.setMainFrame(m_mainFrame);

//        // load autoload file
//        String strKey;
//        String strValue = null;
//        try
//        {
//            InputStream ins = null;
//            if(strCodeBase != null)
//            {
//                m_prolog.setSearchPath(strCodeBase);
//                ins = new URL(strCodeBase + "jipautoload.ini").openStream();
//            }
//            else
//            {
//                ins = new FileInputStream("jipautoload.ini");
//            }
//
//            //FileInputStream ins = new FileInputStream("autoload.ini");
//            Properties props = new Properties();
//            props.load(ins);
//            Enumeration en = props.keys();
//            while(en.hasMoreElements())
//            {
//                strKey = (String)en.nextElement();
//                strValue = (String)props.get(strKey);
//                try
//                {
//                    if(strKey.startsWith("plfile"))
//                    {
//                        m_prolog.consultFile(strValue);
//                        System.out.println(strValue + " consulted");
//                    }
//                    else if(strKey.startsWith("cplfile"))
//                    {
//                        m_prolog.loadFile(strValue);
//                        System.out.println(strValue + " loaded");
//                    }
//                    else if(strKey.startsWith("xlibrary"))
//                    {
//                        m_prolog.loadLibrary(strValue);
//                        System.out.println(strValue + " xloaded");
//                    }
//                }
//                catch(IOException ex)
//                {
//                    System.out.println("WARNING, " + (strValue != null ? strValue : "") + " in autoload not completed: " + ex.toString() );
//                }
//                catch(JIPSyntaxErrorException ex)
//                {
//                    System.out.println("WARNING, autoload not completed: " + ex.toString() + " in " + ex.getFileName());
//                }
//                catch(JIPRuntimeException ex)
//                {
//                    System.out.println("WARNING, " + (strValue != null ? strValue : "") + " in autoload not completed: " + ex.toString() );
//                    ex.printStackTrace();
//                }
//            }
//
//            ins.close();
//        }
//        catch(IOException ex)
//        {
//            System.out.println("WARNING, autoload not completed: " + ex.toString() );
//        }
//        catch(JIPRuntimeException ex)
//        {
//            System.out.println("WARNING, autoload not completed: " + ex.toString() );
//        }
    }

    public void setSearchPath(String strSearchPath)
    {
        try
        {
            m_prolog.setSearchPath(strSearchPath);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        catch(JIPRuntimeException ex)
        {
            ex.printStackTrace();
        }
    }

    public void start()
    {
        m_consoleView.setVisible(true);
        m_consoleView.printHeader();
    }

    public void onDestroy()
    {
    	InputStream ins = m_consoleView.getInputStream();
    	try {
			ins.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        m_prolog.closeAllQueries();
        m_consoleView.onDestroy();
    }

    public void openFile(String strFileName)
    {
        EditFrame.openFile(m_prolog, strFileName);
    }

    public void compileFile(String strFileName)
    {
    	m_prolog.compileFile(strFileName);
    }

    public void consultFile(String strFileName)
    {
    	m_outs.println("consult('" + strFileName + "').");
    	m_consoleView.editable(false);//m_mainArea.setEnabled(false);
    	try {
			m_prolog.consultFile(strFileName);
		} catch (JIPSyntaxErrorException ex) {
			ex.printStackTrace();
			m_outs.println("Error:");
			m_outs.println(ex.getMessage());
		} catch (IOException ex) {
			ex.printStackTrace();
			m_outs.println(ex.getMessage());
	    } catch (Exception ex) {
			ex.printStackTrace();
			m_outs.println(ex.getMessage());
		}

    	m_consoleView.editable(true);
    	m_consoleView.prompt();
    }

    public void runGoal(String goal) throws JIPSyntaxErrorException, IOException
    {
    	m_outs.println(goal);
    	onQuery(goal);
    }

    public void onQuery(String strQuery)
    {
//    	System.out.println("query handle " + m_nQueryHandle);
    	System.out.println("query handle " + m_nQueryHandle);

        if(m_nQueryHandle != -1 && m_prolog.hasMoreChoicePoints(m_nQueryHandle))
        {
            m_outs.println();

            if(strQuery.equals("y") || strQuery.equals(";"))
            {
                // New
                m_consoleView.enableNew(false);
                // Open
                m_consoleView.enableOpen(false);
                // Reset
                m_consoleView.enableReset(false);
                // Stop
                m_consoleView.enableStop(true);

                m_consoleView.editable(false);

                m_consoleView.waitCursor(true);

                m_prolog.nextSolution(m_nQueryHandle);
            }
            else
            {
            	m_outs.println("Yes");
            	m_prolog.closeQuery(m_nQueryHandle);
                m_consoleView.waitCursor(false);
            }
        }
        else
        {
            //System.out.println("strQuery " + strQuery);
        	System.out.println("strQuery " + strQuery);
        	strQuery = strQuery.trim();

            if(strQuery.equals(""))
            {
                m_consoleView.prompt();
                return;
            }

            m_outs.println();

            JIPTerm query = null;
            try
            {
                query = m_prolog.getTermParser().parseTerm(strQuery);
            }
            catch(JIPSyntaxErrorException ex)
            {
                m_outs.println(ex.getMessage());
                m_consoleView.prompt();
                return;
            }

            m_consoleView.waitCursor(true);

            m_consoleView.editable(false);

            // New
            m_consoleView.enableNew(false);
            // Open
            m_consoleView.enableOpen(false);
            // Reset
            m_consoleView.enableReset(false);
            // Stop
            m_consoleView.enableStop(true);

            synchronized(m_prolog)
            {
                m_nQueryHandle = m_prolog.openQuery(query);
//                System.out.println("query handle 2 " + m_nQueryHandle);
                m_consoleView.requestFocus();
            }
        }
    }

    public void onNewFile()
    {
        EditFrame.newFile(m_prolog);
    }

    public void onOpenFile()
    {
        EditFrame.openFile(m_prolog);
    }
    
    public void onExit()
    {
        onDestroy();
    }

    public void onReset()
    {
        m_prolog.reset();
        m_outs.println();
        m_outs.println("Reset OK");

        m_consoleView.prompt();

        m_consoleView.requestFocus();
    }

    public void onRedirectTrace()
    {
    	if(m_traceOuts == null)
    	{
    		try {
				m_traceOuts = new PrintStream("./trace-" + System.currentTimeMillis() + ".log");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		m_prolog.removeTraceListener(m_traceDlg);
    		m_prolog.addTraceListener(this);
    	}
    	else
    	{
    		m_traceOuts.close();
    		m_traceOuts = null;
    		m_prolog.removeTraceListener(this);
    		m_prolog.addTraceListener(m_traceDlg);
    	}
    }

    
    public void onStop()
    {
        if(m_nQueryHandle > -1)
            m_prolog.closeQuery(m_nQueryHandle);

        m_consoleView.requestFocus();
    }

    public JIPEngine getJIPEngine()
    {
        return m_prolog;
    }

    // A Start event occurred
    public void openNotified(JIPEvent e)
    {
        if(m_nQueryHandle == e.getQueryHandle())
        {
            System.out.println("Open");
        }
    }

    public void moreNotified(JIPEvent e)
    {
        if(m_nQueryHandle == e.getQueryHandle())
        {
            System.out.println("More");
        }
    }

    // A solution event occurred
    public void solutionNotified(JIPEvent e)
    {
        synchronized(m_prolog)
        {
            if(m_nQueryHandle == e.getQueryHandle())
            {
                // Show Solution
                //m_outs.println("Yes");
                boolean lm=false; //local more
                JIPTerm term = e.getTerm();
                JIPVariable[] vars = term.getVariables();

                for(int i = 0; i < vars.length; i++)
                {
                    if(!vars[i].isAnonymous())
                    {   
                    	if (i!=0) {
                    	m_outs.println();
                    	} 
                    	m_outs.print(vars[i].getName() + " = " + vars[i].toStringq(m_prolog));
                    	lm=true;
                    }
                }

                m_consoleView.waitCursor(false);

                if(!e.getSource().hasMoreChoicePoints(e.getQueryHandle()))
                {
                    m_prolog.closeQuery(e.getQueryHandle());
                }
                else
                {
 //                   m_outs.print("more? ");
                	if(!lm) 
                	{
                		m_outs.println("Yes");
                		m_prolog.closeQuery(e.getQueryHandle());
                	}
                	else
                	{
	              		m_outs.print(" ? ");
	               		m_outs.flush();
	
	               		m_consoleView.recordHistory(false);
	
	                    // Stop
	                    m_consoleView.enableStop(false);
	
	                    m_consoleView.updatePrompt();
                	}
                }

                m_consoleView.editable(true);

                m_consoleView.requestFocus();
            }
        }
    }

    // A Term has been notified with notify/2
    public void termNotified(JIPEvent e)
    {
        synchronized(m_prolog)
        {
            if(m_nQueryHandle == e.getQueryHandle())
            {
                if(e.getID() == JIPEvent.ID_WAITFORUSERINPUT)
                {
                    m_consoleView.startUserInput();
                }
                else if(e.getID() == JIPEvent.ID_USERINPUTDONE)
                {
                    m_consoleView.stopUserInput();
                }
                else if(e.getID() == JIPEvent.ID_ENCODINGCHANGED)
                {
//                  Set OutputStream
                    try
                    {
                        m_outs = m_consoleView.getPrintStream(m_prolog.getEncoding());
                    }
                    catch (UnsupportedEncodingException ex)
                    {
                        m_outs.println("- Error, invalid encoding: " + e.getTerm().toString(m_prolog));
                    }
                }
                else if(e.getID() == JIPEvent.ID_UNDEFPREDICATE)
                {
//                    String strTerm = "";
//                    if(e.getTerm() instanceof JIPFunctor)
//                        strTerm = ((JIPFunctor)e.getTerm()).getName() + "/" + ((JIPFunctor)e.getTerm()).getArity();
//                    else
                        String strTerm = e.getTerm().toString(m_prolog);

                    m_outs.println("- Warning, the predicate " + strTerm + " is undefined.");
                }
                else if(e.getID() == JIPEvent.ID_SINGLETONVARS)
                {
                    JIPCons cons = (JIPCons)e.getTerm();
                    String strVars = cons.getTail().toString(m_prolog);
                    m_outs.println("- Warning, singleton variables found at line " + cons.getHead() + ": " + strVars);
                }
                else
                {
                    m_outs.print("Term notified:\nID = ");
                    m_outs.println(e.getID());
                    m_outs.print("Term = ");
                    m_outs.println(e.getTerm().toString(m_prolog));
                }
            }
        }
    }

    // The end has been reached because there wasn't more solutions
    public void endNotified(JIPEvent e)
    {
        synchronized(m_prolog)
        {
            if(m_nQueryHandle == e.getQueryHandle())
            {
                m_outs.println("No");

                m_prolog.closeQuery(e.getQueryHandle());
            }
        }
    }

    public void closeNotified(JIPEvent e)
    {
        synchronized(m_prolog)
        {
            if(m_nQueryHandle == e.getQueryHandle())
            {
                m_consoleView.waitCursor(false);
                m_consoleView.prompt();

                // New
                m_consoleView.enableNew(true);
                // Open
                m_consoleView.enableOpen(true);
                // Reset
                m_consoleView.enableReset(true);
                // Stop
                m_consoleView.enableStop(false);

                m_consoleView.editable(true);

                m_consoleView.recordHistory(true);
                m_consoleView.requestFocus();

                m_nQueryHandle = -1;
            }
        }
    }

    // An error (exception) has been raised up by prolog engine
    public void errorNotified(JIPErrorEvent e)
    {
        synchronized(m_prolog)
        {
            if(m_nQueryHandle == e.getQueryHandle())
            {
                m_outs.println(e.getError());
//                if(e.getFileName() != null)
//                {
//                    m_outs.println("File: " + e.getFileName());
//                    m_outs.println("At line: " + e.getLineNumber());
//                }

                m_outs.print("Stack Trace? (y/n)");
                m_outs.flush();

                m_consoleView.waitCursor(false);
                m_consoleView.recordHistory(false);

                InputStream ins = m_consoleView.getInputStream();

                try
                {
                    int c = ins.read();
                    m_outs.println((char)c);
                    m_outs.flush();
                    if(c == 'y')
                    {
                        e.getException().printPrologStackTrace(m_outs);
                        //e.getException().printPrologStackTrace(System.out);
                    }
                }
                catch(IOException ex)
                {
                }

                m_consoleView.recordHistory(true);

                // New
                m_consoleView.enableNew(true);
                // Open
                m_consoleView.enableOpen(true);
                // Reset
                m_consoleView.enableReset(true);
                // Stop
                m_consoleView.enableStop(false);

                m_consoleView.editable(true);

                m_consoleView.requestFocus();

                m_prolog.closeQuery(e.getQueryHandle());
            }
            else
            {
                m_outs.println(e.getError());
                m_outs.flush();

                m_consoleView.prompt();
                m_consoleView.requestFocus();
            }
        }
    }

    
    public void callNotified(JIPTraceEvent e)
    {
        m_traceOuts.print("[" + e.getLevel() + "]");
//        m_outs.print("> ");
//        m_outs.print(strSpace);
        m_traceOuts.print("CALL:  ");
        m_traceOuts.println(e.getTerm().toString(m_prolog));
        m_traceOuts.flush();
        
        Thread.currentThread().yield();
                
       
        // Don't wait for user input
        e.nextStep();
 
    }

    public void foundNotified(JIPTraceEvent e)
    {
    	m_traceOuts.print("[" + e.getLevel() + "]");
//      m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
    	m_traceOuts.print("FOUND: ");
                
    	m_traceOuts.println(e.getTerm().toString(m_prolog));
        
        Thread.currentThread().yield();
        
       
        // Don't wait for user input
        e.nextStep();

    }
    
    public void bindNotified(JIPTraceEvent e)
    {
    	m_traceOuts.print("[" + e.getLevel() + "]");
//        m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
    	m_traceOuts.print("BIND:  ");
    	m_traceOuts.println(e.getTerm().toString(m_prolog));
                
        Thread.currentThread().yield();
        
        //dont waiy
        e.nextStep();
    }

    public void failNotified(JIPTraceEvent e)
    {
        
    	m_traceOuts.print("[" + e.getLevel() + "]");
//        m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
    	m_traceOuts.print("FAIL:  ");
    	m_traceOuts.println(e.getTerm().toString(m_prolog));
                        
    
        Thread.currentThread().yield();
        
        // Don't wait for user input
        e.nextStep();
    }

    public void redoNotified(JIPTraceEvent e)
    {
       
    	m_traceOuts.print("[" + e.getLevel() + "]");
//        m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
    	m_traceOuts.print("REDO:  ");
        
    	m_traceOuts.println(e.getTerm().toString(m_prolog));
                
       
               
        Thread.currentThread().yield();
        
        // Don't wait for user input
        e.nextStep();
    }
    
    public void exitNotified(JIPTraceEvent e)
    {
       
    	m_traceOuts.print("[" + e.getLevel() + "]");
//        m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
    	m_traceOuts.print("EXIT:  ");
    	m_traceOuts.println(e.getTerm().toString(m_prolog));
                
        Thread.currentThread().yield();
        
      
        // Don't wait for user input
        e.nextStep();
    }
    
    // A Start event occurred
    public void startNotified(JIPTraceEvent e)
    {
       
        m_traceOuts.println();
        m_traceOuts.println("-----START TRACING-----");
        m_traceOuts.println();
        
        e.nextStep();
    }
    
    // A Stop event occurred
    public void stopNotified(JIPTraceEvent e)
    {
        m_traceOuts.println();
        m_traceOuts.println("-----STOP TRACING-----");
        m_traceOuts.println();
   
        e.nextStep();
    }
    
  
}
