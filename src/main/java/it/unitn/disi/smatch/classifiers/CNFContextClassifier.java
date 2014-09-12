package it.unitn.disi.smatch.classifiers;

import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.ConvertToCNF;
import it.unitn.disi.smatch.data.trees.IContext;
import it.unitn.disi.smatch.data.trees.INode;
import it.unitn.disi.smatch.data.trees.INodeData;

/**
 * Create concept at node formulas for each node of the context. Converts
 * concept at node formula into CNF.
 *
 * @author <a rel="author" href="http://autayeu.com/">Aliaksandr Autayeu</a>
 */
public class CNFContextClassifier implements IContextClassifier {

    public void buildCNodeFormulas(IContext context) throws ContextClassifierException {
        for (INode node : context.getNodesList()) {
            buildCNode(node);
        }
    }

    /**
     * Constructs c@node formula for the concept.
     *
     * @param in node to process
     * @throws ContextClassifierException ContextClassifierException
     */
    protected void buildCNode(INode in) throws ContextClassifierException {
        StringBuilder path = new StringBuilder();
        INodeData nd = in.getNodeData();
        String formula = toCNF(in, nd.getcLabFormula());
        if (formula != null && !formula.isEmpty() && !formula.equals(" ")) {
            if (formula.contains(" ")) {
                formula = "(" + formula + ")";
            }
            path.append(formula);
        }
        if (in.hasParent()) {
            formula = in.getParent().getNodeData().getcNodeFormula();
            if (formula != null && !formula.isEmpty() && !formula.equals(" ")) {
                if (2 < path.length()) {
                    path.append(" & ").append(formula);
                } else {
                    path.append(formula);
                }
            }
        }

        nd.setcNodeFormula(path.toString());
    }

    /**
     * Converts the formula into CNF.
     *
     * @param in      the owner of the formula
     * @param formula the formula to convert
     * @return formula in CNF form
     * @throws ContextClassifierException ContextClassifierException
     */
    public static String toCNF(INode in, String formula) throws ContextClassifierException {

        PLParser parser = new PLParser();

        String result = formula;
        if ((formula.contains("&") && formula.contains("|")) || formula.contains("~")) {
            String tmpFormula = formula.trim();

            if (!tmpFormula.isEmpty()) {
                Sentence f = parser.parse(tmpFormula);
                Sentence cnf = ConvertToCNF.convert(f);
                result = cnf.toString();
            } else {
                result = tmpFormula;
            }

        }
        return result;
    }
}
