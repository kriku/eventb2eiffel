package eventb2eiffel.handlers;

import java.util.ArrayList;

import org.eventb.core.ISCContextRoot;
import org.eventb.core.ISCMachineRoot;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinFile;
import org.rodinp.core.IRodinProject;
import org.rodinp.core.RodinDBException;

public class Utils {

	public static ISCMachineRoot[] getMachineRootChildren(IRodinProject project) throws RodinDBException {
		ArrayList<ISCMachineRoot> result = new ArrayList<ISCMachineRoot>();
		for (IRodinElement element : project.getChildren()) {
			if (element instanceof IRodinFile) {
				IInternalElement root = ((IRodinFile) element).getRoot();
				if (root instanceof ISCMachineRoot) {
					result.add((ISCMachineRoot) root);
				}
			}
		}
		return result.toArray(new ISCMachineRoot[result.size()]);
	}

	public static ISCContextRoot[] getContextRootChildren(IRodinProject project) throws RodinDBException {
		ArrayList<ISCContextRoot> result = new ArrayList<ISCContextRoot>();
		for (IRodinElement element : project.getChildren()) {
			if (element instanceof IRodinFile) {
				IInternalElement root = ((IRodinFile) element).getRoot();
				if (root instanceof ISCContextRoot) {
					result.add((ISCContextRoot) root);
				}
			}
		}
		return result.toArray(new ISCContextRoot[result.size()]);
	}

}
