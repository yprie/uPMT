package components.interviewPanel.appCommands;

import utils.command.Executable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


public class CopyToClipboardCommand implements Executable<Void> {
    String selectedText;

    public CopyToClipboardCommand(String selectedText) {
        this.selectedText = selectedText;
    }

    @Override
    public Void execute() {
        StringSelection selection = new StringSelection(selectedText);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        return null;
    }
}
