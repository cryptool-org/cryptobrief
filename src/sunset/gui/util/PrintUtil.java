package sunset.gui.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public class PrintUtil implements Printable {

	private List<AttributedString> textLines;
        private int[] pageBreaks;
        private Font font = new Font("Monospaced", Font.PLAIN, 8);

	public PrintUtil(String text) {
		textLines = new ArrayList<AttributedString>();
		text = text.replace("\t", "    ");
		String[] lines = text.split("\\n");
                int i = 1;
		for (String line : lines) {
			AttributedString attrStr = new AttributedString(addZeros(i, String.valueOf(lines.length-String.valueOf(i).length()).length()) + "   " +line);
			attrStr.addAttribute(TextAttribute.FONT, font);
			textLines.add(attrStr);
                        i++;
		}
	}

	/**
	 * Printing the data to a printer. Initialization done in this method.
	 */
	public static void printToPrinter(String fileName, String text) {
		
		/**
		 * Get a Printer Job
		 */
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		printerJob.setJobName(fileName);

		/**
		 * Create a book. A book contains a pair of page painters called
		 * printables. Also you have different pageformats.
		 */
		//Book book = new Book();
		/**
		 * Append the Printable Object (this one itself, as it implements a
		 * printable interface) and the page format.
		 */
		//book.append(new PrintUtil(text), new PageFormat());
		/**
		 * Set the object to be printed (the Book) into the PrinterJob. Doing
		 * this before bringing up the print dialog allows the print dialog to
		 * correctly display the page range to be printed and to dissallow any
		 * print settings not appropriate for the pages to be printed.
		 */
		printerJob.setPrintable(new PrintUtil(text), new PageFormat());

		/**
		 * Calling the printDialog will pop up the Printing Dialog. If you want
		 * to print without user confirmation, you can directly call
		 * printerJob.print()
		 * 
		 * doPrint will be false, if the user cancels the print operation.
		 */
		boolean doPrint = printerJob.printDialog();

		if (doPrint) {
			try {
				printerJob.print();
			} catch (PrinterException ex) {
				System.err.println("Error occurred while trying to Print: "
						+ ex);
			}
		}
	}

	/**
	 * This method comes from the Printable interface. The method implementation
	 * in this class prints a page of text.
	 */
	public int print(Graphics g, PageFormat format, int pageIndex) {

		Graphics2D graphics2d = (Graphics2D) g;
		/**
		 * Move the origin from the corner of the Paper to the corner of the
		 * imageable area.
		 */
		graphics2d.translate(format.getImageableX(), format.getImageableY());

		/** Setting the text color **/
		graphics2d.setPaint(Color.black);
                
                /**
                 * Calculating all needed parameters for the page breaks.
                 */
                FontMetrics metrics = g.getFontMetrics(font);
                int lineHeight = metrics.getHeight();

                if (pageBreaks == null) {
                    int linesPerPage = (int) (format.getImageableHeight() / lineHeight);
                    int numBreaks = (textLines.size() - 1) / linesPerPage;
                    pageBreaks = new int[numBreaks];
                    for (int b = 0; b < numBreaks; b++) {
                      pageBreaks[b] = (b + 1) * linesPerPage;
                    }
                }
                if (pageIndex > pageBreaks.length) {
                    return NO_SUCH_PAGE;
                }
                
                
		/**
		 * Use a LineBreakMeasurer instance to break our text into lines that
		 * fit the imageable area of the page.
		 */
                int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex - 1];
                int end = (pageIndex == pageBreaks.length) ? textLines.size() : pageBreaks[pageIndex];
		Point2D.Float pen = new Point2D.Float();
		for(int line = start; line < end; line++){
			AttributedCharacterIterator charIterator = ((AttributedString)textLines.toArray()[line]).getIterator();
			LineBreakMeasurer measurer = new LineBreakMeasurer(charIterator,
					graphics2d.getFontRenderContext());
			float wrappingWidth = (float) format.getImageableWidth();
			while (measurer.getPosition() < charIterator.getEndIndex()) {
				TextLayout layout = measurer.nextLayout(wrappingWidth);
				pen.y += layout.getAscent();
				float dx = layout.isLeftToRight() ? 0 : (wrappingWidth - layout
						.getAdvance());
				layout.draw(graphics2d, pen.x + dx, pen.y);
				pen.y += layout.getDescent() + layout.getLeading();
			}
		}
                return Printable.PAGE_EXISTS;		
	}
        
        private static String addZeros(int number, int length) 
        {
            String s = String.valueOf(number);
            while (s.length() < length) s = "0"+s;
            return s;
        }

}
