package ffapl.debugger;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import java.util.Objects;

public class Breakpoint {
    final private Position position;
    final private Document document;

    public Breakpoint(int line, Document document) {
        Element root = document.getDefaultRootElement();
        try {
            this.position = document.createPosition(root.getElement(line - 1).getStartOffset());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        this.document = document;
    }

    public int getLine() {
        return document.getDefaultRootElement()
                .getElementIndex(position.getOffset());
    }

    public Position getPosition(){
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Breakpoint that = (Breakpoint) o;
        return Objects.equals(getLine(), that.getLine()) && Objects.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLine(), document);
    }
}