package sunset.gui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import sunset.gui.comparator.LineStateComparator;
import sunset.gui.enums.LineState;

import javax.sound.sampled.Line;

/**
 * manages the state of the Editor line
 *
 * @author Alexander Ortner
 * @version 1.0
 */
public class LineHandler {

    List<FFaplStatusLine> statusLineList;

    public LineHandler() {
        statusLineList = new ArrayList<FFaplStatusLine>();
    }

    public void addLineState(FFaplStatusLine statusLine) {
        statusLineList.add(statusLine);
        Collections.sort(statusLineList, new LineStateComparator());
    }

    public FFaplStatusLine getStateOfLine(int line) {
        for (FFaplStatusLine sLine : statusLineList) {
            if (sLine.getLine() == line) {
                return sLine;
            }
        }
        return null;
    }

    public List<LineState> getStatesofLine(int line) {
        final ArrayList<LineState> result = new ArrayList<>();
        for (FFaplStatusLine sLine : statusLineList) {
            if (sLine.getLine() == line) {
                result.add(sLine.getState());
            }
        }
        return result;
    }

    public List<Integer> getLinesofState(LineState state) {
       return statusLineList.stream().filter(line -> line.getState() == state).map(line -> line.getLine()).collect(Collectors.toList());
    }

    public void resetHandler() {
        statusLineList.clear();
    }

    public void resetWarningLines() {
        removeAll(LineState.ERROR);
        removeAll(LineState.WARNING);

    }

    public void removeAll(LineState lineState) {
        statusLineList.removeIf(line -> line.getState() == lineState);
    }

    public void removeAtLine(int lineNumber, LineState lineState) {
        statusLineList.removeIf(line -> line.getState() == lineState && line.getLine() == lineNumber);
    }
}
