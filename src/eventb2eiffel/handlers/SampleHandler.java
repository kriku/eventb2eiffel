package eventb2eiffel.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ISCContextRoot;
import org.eventb.core.ISCMachineRoot;
import org.rodinp.core.IRodinDB;
import org.rodinp.core.IRodinProject;
import org.rodinp.core.RodinCore;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
//	private Visitor visitor = new Visitor();
//	private Translator translate;

	private static void print(Object message) {
		System.out.println(message.toString());
	}

//	private String ruleContext(ModelContext context) throws RodinDBException {
//		String result = "\n -- ===== context declaration\n";
//		IContextRoot cr = context.getInternalContext();
//
//		ICarrierSet[] sets = cr.getCarrierSets();
//		for (ICarrierSet set : sets) {
//			result += ruleCSet(set);
//		}
//
//		result += "class CONSTANTS\n";
//		result += "feature --Constants\n";
//		result += "end\n";
//
//		return result;
//	}
//
//	private String ruleCSet(ICarrierSet set) throws RodinDBException {
//		String result = "";
//		String id = set.getIdentifierString().toUpperCase();
//		result += "class " + id + "\n";
//		result += "feature --Constants\n";
//		result += "end\n";
//
//		return result;
//	}


//	private String ruleSCMachine(ISCMachineRoot scMachine) throws RodinDBException {
//		String result = "";
//		ISCVariable [] variables = scMachine.getSCVariables();
//		for (ISCVariable variable : variables) {
//			result += ruleSCVariable(variable);
//		}
//		return result;
//	}
	
//	private String ruleSCVariable(ISCVariable variable) {
//		String result = "";
//		final FormulaFactory f = FormulaFactory.getDefault();
//		Type varType = null;
//		try {
//			varType = variable.getType(f);
//			result += variable.getIdentifierString() + "\n";
//			result += varType.toString() + "\n";
//			result += visitor.type(varType.toString());
//			result += "\n\n";
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (varType != null) {
//
//		}
//		return result;
//	}

//	private String ruleMachine(ModelMachine machine) throws RodinDBException {
//		IMachineRoot mr = machine.getInternalMachine();
//		String result = "-- ===================== Eiffel machine translation\n";
//		result += "class " + mr.getElementName() + "\n";
//		result += "create INITIALISATION \n\n";
//		result += "feature\n";
//
//		IVariable [] variables = mr.getVariables();
//		for (IVariable variable : variables) {
//			result += ruleVariable(variable);
//		}
//
//		IEvent [] events = mr.getEvents();
//		for (IEvent event : events) {
//			result += ruleEvent(event);
//		}
//
//		IInvariant [] invariants = mr.getInvariants();
//		for (IInvariant invariant : invariants) {
//			result += ruleInvariant(invariant);
//		}
//		result += "end";
//		return result;
//	}

//	private String ruleInvariant(IInvariant invariant) throws RodinDBException {
//		String result = "";
//		result += invariant.getLabel() + ": ";
//		result += invariant.getPredicateString() + "\n";
//		visitor.Pre(invariant.getPredicateString());
//		return result;
//	}

//	private String ruleVariable(IVariable variable) throws RodinDBException {
//		String result = "";
//		result += variable.getIdentifierString() + ": Type \n";
//		return result;
//	}
//
//	private String ruleEvent(IEvent event) throws RodinDBException {
//		String result="\n";
//		result += event.getLabel() + "\n";
//
//		IGuard [] guards = event.getGuards();
//		if (guards.length > 0) {
//			result += "\trequire\n";
//		}
//		for (IGuard guard : guards) {
//			result += "\t" + guard.getLabel() + ": " + guard.getPredicateString() + "\n";
//		}
//
//		result += "\tdo\n";
//		IAction [] actions = event.getActions();
//		for (IAction action : actions) {
//			//		result += "\t" + action.getLabel() + ": " + action.getAssignmentString() + "\n";
//			result += "\t" + action.getAssignmentString() + "\n";
//		}
//
//		result += "\tend \n";
//		return result;
//	}
//
//	private void exploreContexts(ModelContext [] contexts) throws RodinDBException {
//		for (ModelContext context : contexts) {
//			print(ruleContext(context));
//		}
//	}

//	private void exploreMachines(ModelMachine [] machines) throws RodinDBException {
//		for (ModelMachine machine : machines) {
//			print(ruleMachine(machine));
//		}
//	}

//		IEventBProject eventBProject = (IEventBProject) project.getAdapter(IEventBProject.class);
//
//		ModelProject modelProject = ModelController.getProcessedProject(project);
//		//				print("model need processing - " + modelProject.needsProcessing);
//		ModelContext[] modelContextArr = modelProject.getRootContexts();
//		ModelMachine[] modelMachineArr = modelProject.getRootMachines();
//		print("{work with statically checked models}");
//		for (ModelMachine machine : modelMachineArr) {
//			ISCMachineRoot scMachine = eventBProject.getSCMachineRoot(machine.getInternalElement().getElementName());
//			print(ruleSCMachine(scMachine));
//		}
//		print("{done}");
//		exploreContexts(modelContextArr);
//		exploreMachines(modelMachineArr);
	
	private void translate(IRodinProject project) throws CoreException {
		Translator translate = new Translator();
		ISCContextRoot [] contexts = Utils.getContextRootChildren(project);
		for (ISCContextRoot context : contexts) {
			print(translate.context(context));
		}
		ISCMachineRoot[] machines = Utils.getMachineRootChildren(project);
		for (ISCMachineRoot machine : machines) {
			print(translate.machine(machine));
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		print("\n\n\nTRAVERSING RODIN PROJECTS ============================================");
		IRodinDB rodinDB = RodinCore.getRodinDB();
		try {
			IRodinProject [] rodinProjects = rodinDB.getRodinProjects();
			for (IRodinProject project : rodinProjects) {
				translate(project);
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
