package sunset.gui.debugger;

import ffapl.debugger.Breakpoint;
import ffapl.debugger.CustomExpression;
import ffapl.debugger.StackFrame;
import ffapl.debugger.StepKind;
import ffapl.exception.FFaplException;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.visitor.FFaplJavaInterpreterVisitor;

import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public interface IFFaplDebugger {

    void setInterpreterVisitor(FFaplJavaInterpreterVisitor interpreter_visitor);

    void setSymbolTable(ISymbolTable symbolTable);

    void setBreakpoint(int line);

    void removeBreakpoint(int line);

    void toggleBreakpoint(int line);

    void removeAllBreakpoints();

    List<Breakpoint> getBreakpoints();

    void addCustomExpression(CustomExpression expression);

    void removeCustomExpression(CustomExpression expression);

    List<CustomExpression> getCustomExpressions();

    void registerBreakpointComponent(Component component);

    void startExecution();

    void endExecution();

    void setStep(StepKind stepKind);

    StepKind getStep(int breakLine, TreeNode variableRootNode, Stack<StackFrame> stackFrames) throws FFaplException;

    void evaluateAssignment(String expression, ISymbol symbol);

    boolean evaluateStatement(String statement, ISymbol symbol);

    String evaluateExpression(String expr);

}
