package sunset.gui.debugger;

import ffapl.FFaplInterpreter;
import ffapl.ParseException;
import ffapl.ast.nodes.ASTStatement;
import ffapl.debugger.*;
import ffapl.exception.FFaplException;
import ffapl.exception.FFaplTerminatedException;
import ffapl.java.logging.FFaplLogger;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.visitor.FFaplJavaInterpreterVisitor;
import ffapl.visitor.FFaplSymbolTypeCheckingVisitor;
import sunset.gui.FFaplJFrame;
import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.enums.LineState;
import sunset.gui.listener.DebuggerTreeModelListener;
import sunset.gui.listener.DebuggerTreeMouseListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.*;
import java.util.List;

import static sunset.gui.lib.ExecuteThread.setEnabled;

public class FFaplDebugger implements IFFaplDebugger {
    private final Object lock = new Object();
    private long execution_time = -1;
    private long latestExecutionTime;
    private StepKind UIResponse;

    private final List<Breakpoint> breakpoints = new ArrayList<>();
    private final List<CustomExpression> custom_expressions = new ArrayList<>();

    private final List<Component> breakpointListeners = new ArrayList<>();

    private final FFaplCodeTextPane textPane;
    private FFaplJavaInterpreterVisitor interpreter_visitor;
    private ISymbolTable symbolTable;

    private boolean isRunning = false;
    private boolean isPaused = false;
    private VariableJTree variableJTree;
    private JList<StackFrame> stackFrameList;
    private Vector<Component> _startComp;
    private Vector<Component> _stopComp;
    private Vector<Component> _stepOverComp;
    private Vector<Component> _stepIntoComp;
    private Vector<Component> _resumeComp;

    public FFaplDebugger(FFaplCodeTextPane textPane, Vector<Component> startComp, Vector<Component> stopComp, Vector<Component> stepOverComp, Vector<Component> stepIntoComp, Vector<Component> resumeComp) {
        this.textPane = textPane;
        this._startComp = startComp;
        this._stopComp = stopComp;
        this._stepOverComp = stepOverComp;
        this._stepIntoComp = stepIntoComp;
        this._resumeComp = resumeComp;
    }

    private boolean isActivePanel() {
        return FFaplJFrame.getCurrentCodePanel().getCodePane() == textPane;
    }

    @Override
    public void setInterpreterVisitor(FFaplJavaInterpreterVisitor interpreter_visitor) {
        this.interpreter_visitor = interpreter_visitor;
    }

    @Override
    public void setSymbolTable(ISymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public void setBreakpoint(int line) {
        if (textPane.getDocument().getDefaultRootElement().getElementCount() >= line) {
            breakpoints.add(new Breakpoint(line, textPane.getDocument()));
            textPane.setBreakPointLine(line - 1);
            breakpointListeners.forEach(Component::repaint);
        }
    }

    @Override
    public void removeBreakpoint(int line) {
        breakpoints.removeIf(bp -> bp.getLine() + 1 == line);
        textPane.removeBreakPointLine(line - 1);
        breakpointListeners.forEach(Component::repaint);
    }

    @Override
    public void toggleBreakpoint(int line) {
        if (breakpoints.stream().anyMatch(bp -> bp.getLine() + 1 == line)) {
            removeBreakpoint(line);
        } else {
            setBreakpoint(line);
        }
    }

    @Override
    public void removeAllBreakpoints() {
        breakpoints.clear();
        textPane.getLineHandler().removeAll(LineState.BREAKPOINT);
        breakpointListeners.forEach(Component::repaint);
    }

    @Override
    public List<Breakpoint> getBreakpoints() {
        return breakpoints;
    }

    @Override
    public void addCustomExpression(CustomExpression expression) {
        custom_expressions.add(expression);
    }

    @Override
    public void removeCustomExpression(CustomExpression expression) {
        custom_expressions.removeIf(s -> s.equals(expression));
    }

    @Override
    public List<CustomExpression> getCustomExpressions() {
        return custom_expressions;
    }

    @Override
    public void registerBreakpointComponent(Component component) {
        breakpointListeners.add(component);
    }

    @Override
    public void startExecution() {
        execution_time = System.nanoTime();
        isRunning = true;
        isPaused = false;
    }

    @Override
    public void endExecution() {
        final long time_delta = System.nanoTime() - execution_time;
        latestExecutionTime = time_delta;
        isRunning = false;
        isPaused = false;


        textPane.removeActiveDebugLines();
        textPane.repaint();
        stackFrameList = null;
        variableJTree = null;

        if (isActivePanel()) {
            FFaplJFrame.getDebuggerPanel().setVariableTree(null);
            FFaplJFrame.getStackFramePanel().setStackFrameList(null);
            FFaplJFrame.getExpressionField().setEnabled(false);
            FFaplJFrame.showConsolePanel();
            FFaplJFrame.setLatestExecutionDelta(time_delta);
        }

        interpreter_visitor = null;
        symbolTable = null;
    }

    @Override
    public void setStep(StepKind stepKind) {
        isPaused = false;
        if (isActivePanel()) {
            FFaplJFrame.getExpressionField().setEnabled(false);
            unPauseExecutionUI();
        }

        textPane.removeActiveDebugLines();
        textPane.repaint();
        UIResponse = stepKind;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void initTree(VariableJTree tree) {
        TreeModel model = tree.getModel();

        // setting this to zero and making the cellrenderer take care of it, otherwise tree has buggy display
        tree.setRowHeight(0);

        //newTree.setPreferredSize(new Dimension(FFaplJFrame.getDebuggerPanel().getWidth() / 2, FFaplJFrame.getDebuggerPanel().getHeight()));
        tree.setEditable(true);
        tree.setInvokesStopCellEditing(true);
        tree.setToggleClickCount(3); // disables expansion on double-click

        tree.addMouseListener(new DebuggerTreeMouseListener(tree));


        DebuggerTreeCellRenderer cellRenderer = new DebuggerTreeCellRenderer();
        tree.setCellRenderer(cellRenderer);
        tree.setCellEditor(new DebuggerTreeCellEditor(tree, cellRenderer));
        model.addTreeModelListener(new DebuggerTreeModelListener(this));
    }

    private void initStackFrameList(JList<StackFrame> list) {
        list.setCellRenderer(new StackFrameCellRenderer());
        list.addMouseListener(new CallStackMouseListener(stackFrameList));
    }


    @Override
    public StepKind getStep(int breakLine, TreeNode variableRootNode, Stack<StackFrame> stackFrames) throws FFaplTerminatedException {
        final long time_delta = System.nanoTime() - execution_time;
        isPaused = true;

        // give tabbedpane currentdebugvariables
        variableJTree = new VariableJTree(variableRootNode, this);
        stackFrameList = new JList<>(stackFrames.toArray(new StackFrame[0]));
        initTree(variableJTree);
        initStackFrameList(stackFrameList);
        latestExecutionTime = time_delta;
        if (isActivePanel()) {
            FFaplJFrame.getDebuggerPanel().setVariableTree(variableJTree);
            FFaplJFrame.getStackFramePanel().setStackFrameList(stackFrameList);
            FFaplJFrame.getExpressionField().setEnabled(true);
            FFaplJFrame.setLatestExecutionDelta(time_delta);
            FFaplJFrame.showDebuggerPanel();
            pauseExecutionUI();
        }

        // give linemanager the information that this line is now broken on
        textPane.setActiveDebugStepLine(breakLine - 1);

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // throwing an exception here is important to ensure that this thread terminates.
                throw new FFaplTerminatedException();
            }
        }

        execution_time = System.nanoTime();
        return UIResponse;
    }

    public void putIntoFocus() {
        FFaplJFrame.getDebuggerPanel().setVariableTree(variableJTree);
        FFaplJFrame.getStackFramePanel().setStackFrameList(stackFrameList);
        FFaplJFrame.getExpressionField().setEnabled(!isRunning);
        FFaplJFrame.setLatestExecutionDelta(latestExecutionTime);
        if (isRunning) {
            if (isPaused) {
                FFaplJFrame.showDebuggerPanel();
                pauseExecutionUI();
            } else { //isPaused == false
                FFaplJFrame.showConsolePanel();
                startExecutionUI();
            }
        } else {
            FFaplJFrame.showConsolePanel();
            endExecutionUI();
        }
    }

    @Override
    public void evaluateAssignment(String expression, ISymbol symbol) {
        expression = symbol.getName() + ":=" + expression + ";";

        evaluateStatement(expression, symbol);
    }

    @Override
    public boolean evaluateStatement(String statement, ISymbol symbol) {
        FFaplInterpreter token_generator = new FFaplInterpreter(new StringReader(statement));
        token_generator.disable_tracing();
        token_generator.token_source.setDebugStream(new PrintStream(OutputStream.nullOutputStream()));

        ASTStatement expression_node;
        try {
            expression_node = token_generator.Statement();
        } catch (ParseException e) {
            FFaplJFrame.showMessageBox(e.errorType());
            return false;
        } catch (FFaplException e) {
            FFaplJFrame.showMessageBox(e.errorType());
            return false;
        }
        var typeCheckVisitor = new FFaplSymbolTypeCheckingVisitor(symbolTable, null, null, null);

        final ISymbol prev_scope = symbolTable.currentScope();
        synchronized (lock) {
            try {
                if (symbol != null) {
                    symbolTable.setScope(symbol);
                }
                typeCheckVisitor.visit(expression_node, null);
                interpreter_visitor.set_debug(false);
                interpreter_visitor.visit(expression_node, null);
                interpreter_visitor.set_debug(true);
            } catch (FFaplException e) {
                FFaplJFrame.showMessageBox(e.getErrorMessage());
                return false;
            } finally {
                try {
                    symbolTable.setScope(prev_scope);
                } catch (FFaplException e) {
                }

            }
        }
        return true;
    }

    @Override
    public String evaluateExpression(String expr) {
        expr = "println(" + expr + ");";
        FFaplInterpreter token_generator = new FFaplInterpreter(new StringReader(expr));
        token_generator.disable_tracing();
        token_generator.token_source.setDebugStream(new PrintStream(OutputStream.nullOutputStream()));

        ASTStatement expr_node;
        try {
            expr_node = token_generator.Statement();
        } catch (ParseException e) {
            return "INVALID SYNTAX";
        } catch (FFaplException e) {
            return "ERROR";
        }
        FFaplDebuggerOutputLogger outputLogger = new FFaplDebuggerOutputLogger("FakeLogger");
        var typeCheckVisitor = new FFaplSymbolTypeCheckingVisitor(symbolTable, outputLogger, null, null);

        synchronized (lock) {
            try {
                FFaplLogger prev_logger = interpreter_visitor.get_logger();
                interpreter_visitor.set_logger(outputLogger);

                typeCheckVisitor.visit(expr_node, null);

                interpreter_visitor.set_debug(false);
                interpreter_visitor.visit(expr_node, null);
                interpreter_visitor.set_debug(true);

                interpreter_visitor.set_logger(prev_logger);
                return outputLogger.getMessage();
            } catch (FFaplException e) {
                return e.getErrorMessage();
            }
        }
    }

    public void documentChanged(final DocumentEvent event) {
        final int offset = event.getOffset();
        final int length = event.getLength();
        final Document doc = textPane.getDocument();
        final Element root = doc.getDefaultRootElement();
        final int totalLines = root.getElementCount();

        for (Iterator<Breakpoint> iterator = breakpoints.iterator(); iterator.hasNext(); ) {
            final Breakpoint breakpoint = iterator.next();
            final int currentOffset = breakpoint.getPosition().getOffset();
            final int newLineIndex = root.getElementIndex(currentOffset);

            if (newLineIndex >= totalLines) {
                iterator.remove();
                continue;
            }


            if (currentOffset >= offset &&
                    currentOffset < offset + length) {
                iterator.remove();
            }
        }


        textPane.getLineHandler().removeAll(LineState.BREAKPOINT);
        breakpoints.forEach(bp -> textPane.setBreakPointLine(bp.getLine()));
        breakpointListeners.forEach(Component::repaint);
    }

    private void startExecutionUI(){
        setEnabled(_startComp, false);
        setEnabled(_stopComp, true);
        setEnabled(_stepOverComp, false);
        setEnabled(_stepIntoComp, false);
        setEnabled(_resumeComp, false);
    }

    private void endExecutionUI(){
        setEnabled(_stopComp, false);
        setEnabled(_startComp, true);
        setEnabled(_stepOverComp, false);
        setEnabled(_stepIntoComp, false);
        setEnabled(_resumeComp, false);
    }

    private void pauseExecutionUI(){
        setEnabled(_stopComp, true);
        setEnabled(_startComp, false);
        setEnabled(_stepOverComp, true);
        setEnabled(_stepIntoComp, true);
        setEnabled(_resumeComp, true);
    }

    private void unPauseExecutionUI(){
        setEnabled(_startComp, false);
        setEnabled(_stopComp, true);
        setEnabled(_stepOverComp, false);
        setEnabled(_stepIntoComp, false);
        setEnabled(_resumeComp, false);
    }
}
