package it.unitn.disi.smatch.deciders;

import it.unitn.disi.common.components.Configurable;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * SAT4J-based Solver.
 *
 * @author Mikalai Yatskevich mikalai.yatskevich@comlab.ox.ac.uk
 * @author <a rel="author" href="http://autayeu.com/">Aliaksandr Autayeu</a>
 */
public class SAT4J extends Configurable implements ISATSolver {

    private Reader reader;

    public SAT4J() {
        ISolver solver = SolverFactory.newLight();
        solver.setTimeout(3600); // 1 hour timeout
        reader = new DimacsReader(solver);
    }

    public boolean isSatisfiable(String input) throws SATSolverException {
        boolean result;
        try {
            IProblem problem = reader.parseInstance(new ByteArrayInputStream(input.getBytes()));
            result = problem.isSatisfiable();
        } catch (ParseFormatException e) {
            throw new SATSolverException(e.getClass().getSimpleName() + ": " + e.getMessage() + " on input: " + input, e);
        } catch (ContradictionException e) {
            result = false;
        } catch (TimeoutException e) {
            throw new SATSolverException(e.getClass().getSimpleName() + ": " + e.getMessage() + " on input: " + input, e);
        } catch (IOException e) {
            throw new SATSolverException(e.getClass().getSimpleName() + ": " + e.getMessage() + " on input: " + input, e);
        }
        return result;

    }
}