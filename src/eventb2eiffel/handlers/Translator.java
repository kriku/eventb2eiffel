package eventb2eiffel.handlers;

import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ISCAction;
import org.eventb.core.ISCConstant;
import org.eventb.core.ISCContextRoot;
import org.eventb.core.ISCEvent;
import org.eventb.core.ISCGuard;
import org.eventb.core.ISCIdentifierElement;
import org.eventb.core.ISCInvariant;
import org.eventb.core.ISCMachineRoot;
import org.eventb.core.ISCParameter;
import org.eventb.core.ISCRefinesMachine;
import org.eventb.core.ISCVariable;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.ITypeEnvironment;
import org.eventb.core.ast.Type;
import org.rodinp.core.RodinDBException;

public class Translator {
	private FormulaFactory ff = FormulaFactory.getDefault();
	private ITypeEnvironment typenv = ff.makeTypeEnvironment();
	private Visitor visit = new Visitor();
	private StringBuilder writer;
	private int level;
	private boolean inline = false;
	
	private static void print(Object message) {
		System.out.println(message.toString());
	}

	private void write(String... words) {
		if (!inline) {
			writer.append("\n");
			for (int l=0; l<level; l++) writer.append("\t");
		}
		for (String w : words) writer.append(w);
	}

	public String machine(ISCMachineRoot m) throws CoreException {
		writer = new StringBuilder();

		level = 0;
		write("class ", m.getElementName().toUpperCase());
		write("create ");
		write("\tINITIALISATION");

		//		line("inherit");
		//		ISCRefinesMachine [] refines = m.getSCRefinesClauses();
		//		for (ISCRefinesMachine refine : refines) {
		//			line(refine.getElementName());
		//		}

		write("feature --variables");
		level++;
		ISCVariable [] variables = m.getSCVariables();
		for (ISCVariable v : variables) {
			identifier(v);
			write();
		}
		ISCEvent [] events = m.getSCEvents();
		for (ISCEvent e : events) {
			event(e);
			write();
		}
		ISCInvariant [] invariants = m.getSCInvariants();
		for (ISCInvariant i : invariants) {
			invariant(i);
			write();
		}
		level--;
		write("end");
		return writer.toString();
	}

	public String context(ISCContextRoot c) throws CoreException {
		writer = new StringBuilder();
		level = 0;
		write("class ", c.getElementName().toUpperCase());
		write("create ");
		write("\tINITIALISATION");

		write("feature --constants");
		level++;
		ISCConstant [] constants = c.getSCConstants();
		for (ISCConstant constant : constants) {
			identifier(constant);
			write();
		}
//		ISCVariable [] variables = m.getSCVariables();
//		for (ISCVariable v : variables) {
//			identifier(v);
//			write();
//		}
		level--;
		write("end");
		return writer.toString();
	}

	private void event(ISCEvent e) throws CoreException {
		ISCParameter [] args = e.getSCParameters();
		write(e.getLabel());
		inline = true;
		if (args.length > 0) {
			write(" (");
			for (int i=0; i < args.length; i++) {
				if (i != 0) write("; ");
				identifier(args[i]);
			}
			write(") ");
		}
		write(" is");
		inline = false;
		ISCGuard [] guards = e.getSCGuards();
		if (guards.length > 0) {
			write("require");
			level++;
			for (ISCGuard guard : guards) {
				write(" -- ", guard.getPredicateString());
				String g = visit.Predicate(guard.getPredicateString(), true);
				write(g, "\t -- ", guard.getLabel());
			}
			level--;
		}

		ISCAction [] actions = e.getSCActions();
		write("do");
		level++;
		for (ISCAction action : actions) {
			write(" -- ", action.getAssignmentString());

			write(visit.assignment(action.getAssignmentString(), null));
//			write(visit.assignment(action.getAssignmentString(), true));
//			String a = visit.Predicate(action.getAssignmentString(), true);
//			write(a, "\t -- ", action.getLabel());
		}
		level--;
		write("end");
	}
	
//	private void assignment(ISC)

	private void identifier(ISCIdentifierElement identifier) throws CoreException {
		String name = identifier.getIdentifierString();
		Type type = null;
		type = identifier.getType(ff);
//		write(" -- ", type.toString());
		write(identifier.getIdentifierString(), ": ", visit.type(type.toString()));
	}
	
	private void invariant(ISCInvariant invariant) throws CoreException {
		
	}
	
	private void constant(ISCConstant c) throws CoreException {
		Type varType = null;
		varType = c.getType(ff);
		write(c.getIdentifierString(), ": ", visit.type(varType.toString()));
		if (varType != null) {

		}
	}

	private void variable(ISCVariable v) {
		Type varType = null;
		try {
			varType = v.getType(ff);
			write(v.getIdentifierString(), ": ", visit.type(varType.toString()));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (varType != null) {

		}
	}

}
