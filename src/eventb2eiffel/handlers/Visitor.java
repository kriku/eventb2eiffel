package eventb2eiffel.handlers;

import java.util.HashMap;

import org.eventb.core.ast.Assignment;
import org.eventb.core.ast.AssociativeExpression;
import org.eventb.core.ast.AssociativePredicate;
import org.eventb.core.ast.AtomicExpression;
import org.eventb.core.ast.BecomesEqualTo;
import org.eventb.core.ast.BecomesMemberOf;
import org.eventb.core.ast.BecomesSuchThat;
import org.eventb.core.ast.BinaryExpression;
import org.eventb.core.ast.BinaryPredicate;
import org.eventb.core.ast.BoolExpression;
import org.eventb.core.ast.BoundIdentDecl;
import org.eventb.core.ast.BoundIdentifier;
import org.eventb.core.ast.Expression;
import org.eventb.core.ast.ExtendedExpression;
import org.eventb.core.ast.ExtendedPredicate;
import org.eventb.core.ast.Formula;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.FreeIdentifier;
import org.eventb.core.ast.IParseResult;
import org.eventb.core.ast.ISimpleVisitor2;
import org.eventb.core.ast.IntegerLiteral;
import org.eventb.core.ast.LiteralPredicate;
import org.eventb.core.ast.MultiplePredicate;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.PredicateVariable;
import org.eventb.core.ast.QuantifiedExpression;
import org.eventb.core.ast.QuantifiedPredicate;
import org.eventb.core.ast.RelationalPredicate;
import org.eventb.core.ast.SetExtension;
import org.eventb.core.ast.SimplePredicate;
import org.eventb.core.ast.UnaryExpression;
import org.eventb.core.ast.UnaryPredicate;

public class Visitor implements ISimpleVisitor2 {
	private FormulaFactory ff = FormulaFactory.getDefault();
	private StringBuilder writer;
	private String predicate;
	private boolean getType;
	boolean generateStubs;
	private String where = "";

	private void write(String... words) {
		for (String w : words) writer.append(w);
	}

//	HashMap<String,String> storedVarTypes;
//	HashMap<String,String> storedConstTypes;

	private void print(Object message) {
		System.out.println(where + " -- " + message.toString());
	}

	Visitor() {
		getType = false;
		generateStubs = true;
		predicate = "";
//		storedVarTypes = new HashMap<String,String>();
//		storedConstTypes = new HashMap<String,String>();
	}

	public String type(String var) {
		writer = new StringBuilder();
		where = "type";
		boolean temp = getType;
		getType = true;
		predicate = "";
		generateStubs = false;

		Exp(var);

		getType = temp;
		String res = predicate;
		predicate = "";
		generateStubs = false;
		where = "";
		return res;
	}	

	public String Predicate(String pred, boolean bstub) {
		boolean temp = getType;
		where = "predicate";
		getType = false;
		predicate=""; 
		generateStubs = bstub;

		Pre(pred);

		getType = temp;
		String res = predicate;
		predicate = "";
		generateStubs = false;
		where = "";
		return res;
	}

	public void Pre(String predicate) {
		print(predicate);
		final FormulaFactory ff = FormulaFactory.getDefault();
		final IParseResult result = ff.parsePredicate(predicate, null);
		final Predicate p = result.getParsedPredicate();
		print("PRE " + p.toString());
		p.accept(this);
	}
	
	public String assignment(String assignment, Object origin) {
		boolean temp = getType;
		writer = new StringBuilder();
		where = "assignment";
		getType = false;
		predicate=""; 

		parse(assignment, origin);

		getType = temp;
		String res = predicate;
		predicate = "";
		generateStubs = false;
		where = "";
		return writer.toString();
	}

	private void parse(String assignment, Object origin) {
		print(assignment);
		ff.parseAssignment(assignment, null);
		IParseResult result = ff.parseAssignment(assignment, origin);
		Assignment a = result.getParsedAssignment();
		print("ASS " + a.toString());
		a.accept(this);
	}

	public void Exp(String expression){
		print(expression);
		final FormulaFactory ff = FormulaFactory.getDefault();
		final IParseResult result = ff.parseExpression(expression, null);
		final Expression e = result.getParsedExpression();
		print("EXP " + e.toString());
		e.accept(this);
	}
	
	@Override
	public void visitBecomesEqualTo(BecomesEqualTo assignment) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitBecomesEqualTo");
		print(" " + assignment.toString());
		print(" -------------------------");

		for(FreeIdentifier identifier : assignment.getAssignedIdentifiers())
			write(identifier.toString());
		write(" := ");
		for (Expression expression : assignment.getExpressions()) 
			write(expression.toString());
	}

	@Override
	public void visitBecomesMemberOf(BecomesMemberOf assignment) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitBecomesMemberOf");
		print(" " + assignment.toString());
		print(" -------------------------");
	}

	@Override
	public void visitBecomesSuchThat(BecomesSuchThat assignment) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitBecomesSuchThat");
		print(" " + assignment.toString());
		print(" -------------------------");
	}

	@Override
	public void visitBoundIdentDecl(BoundIdentDecl boundIdentDecl) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitBoundIdentDecl");
		print(" " + boundIdentDecl.toString());
		print(" -------------------------");
	}

	@Override
	public void visitAssociativeExpression(AssociativeExpression expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitAssociativeExpression");
		print(" " + expression.toString());
		print(" -------------------------");
	}

	@Override
	public void visitAtomicExpression(AtomicExpression expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitAtomicExpression");
		print(" " + expression.toString());
		print(" -------------------------");
		switch (expression.getTag()) {
		case	Formula.INTEGER:
			//			print("This is an Integer ");			
			if (getType)
				predicate += "Integer";
			if (generateStubs)
				predicate += "Integer";
			break;
		case	Formula.BOOL:
			//			print("This Bool ");
			predicate += "bool";
			break;
		case	Formula.TRUE:
			print("This is True");
			if(getType)
				predicate += "Bool";
			if(generateStubs)
				predicate += "Bool.True";
			break;
		case	Formula.FALSE:
			print("This is False");
			if(getType)
				predicate += "Bool";
			else 
				predicate += "Bool.False";
			break;
		case	Formula.NATURAL:
			print("This is a NAT ");
			if(getType)
				predicate += "Nat";
			if(generateStubs)
				predicate += "Nat";
			break;
		case	Formula.NATURAL1:
			print("This is a NAT1 ");
			if(getType)
				predicate += "Nat1";
			if(generateStubs)
				predicate += "Nat1";
			break;
		case	Formula.EMPTYSET:
			print("This is an emptyset or emptyrelation"); 
			if(getType)
				predicate += "Set";
			if(generateStubs) 
				predicate += "Set.emptySet()";
			break;
		case	Formula.KPRED:
			print("This is a Pred ");
			if(getType) 
				predicate += "predecessorType";
			if(generateStubs)
				predicate += "predecessor";
			break;
		case	Formula.KSUCC:
			print("This is a Succ ");
			if(getType) 
				predicate += "successorType";
			if(generateStubs)
				predicate += "successor";
			break;
		case	Formula.KPRJ1_GEN:
			print("This is a PRJ1 ");
			if(getType) 
				predicate += "projection1Type";
			if(generateStubs)
				predicate += "projection1";
			break;
		case	Formula.KPRJ2_GEN:
			print("This is a PRJ2 ");
			if(getType) 
				predicate += "projection2Type";
			if(generateStubs)
				predicate += "projection2";
			break;
		case	Formula.KID_GEN:
			print("This is an ID ");
			if(getType) {
				//ncc???
			}
			if(generateStubs)
				predicate += expression.toString();
			break;
		}
	}

	@Override
	public void visitBinaryExpression(BinaryExpression expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitBinaryExpression");
		print(" " + expression.toString());
		print(" -------------------------");
	}

	@Override
	public void visitBoolExpression(BoolExpression expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitBoolExpression");
		print(" " + expression.toString());
		print(" -------------------------");
	}

	@Override
	public void visitIntegerLiteral(IntegerLiteral expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitIntegerLiteral");
		print(" " + expression.toString());
		print(" -------------------------");
	}

	@Override
	public void visitQuantifiedExpression(QuantifiedExpression expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitQuantifiedExpression");
		print(" " + expression.toString());
		print(" -------------------------");
	}

	@Override
	public void visitSetExtension(SetExtension expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitSetExtension");
		print(" " + expression.toString());
		print(" -------------------------");
	}

	@Override
	public void visitUnaryExpression(UnaryExpression expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitUnaryExpression");
		print(" -------------------------");
	}

	@Override
	public void visitBoundIdentifier(BoundIdentifier identifierExpression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitBoundIdentifier");
		print(" -------------------------");
	}

	@Override
	public void visitFreeIdentifier(FreeIdentifier identifierExpression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitFreeIdentifier");
		print(" " + identifierExpression.toString());
		print(" " + identifierExpression.getType());
		print(" -------------------------");
	}

	@Override
	public void visitAssociativePredicate(AssociativePredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitAssociativePredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitBinaryPredicate(BinaryPredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitBinaryPredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitLiteralPredicate(LiteralPredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitLiteralPredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitMultiplePredicate(MultiplePredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitMultiplePredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitQuantifiedPredicate(QuantifiedPredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitQuantifiedPredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitRelationalPredicate(RelationalPredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitRelationalPredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitSimplePredicate(SimplePredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitSimplePredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitUnaryPredicate(UnaryPredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitUnaryPredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitExtendedExpression(ExtendedExpression expression) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitExtendedExpression");
		print(" " + expression.toString());
		print(" -------------------------");
	}

	@Override
	public void visitExtendedPredicate(ExtendedPredicate predicate) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitExtendedPredicate");
		print(" " + predicate.toString());
		print(" -------------------------");
	}

	@Override
	public void visitPredicateVariable(PredicateVariable predVar) {
		// TODO Auto-generated method stub
		print(" -------------------------");
		print(" visitPredicateVariable");
		print(" " + predVar.toString());
		print(" -------------------------");
	}

}
