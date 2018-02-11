package model;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class copyAMoment implements Transferable, ClipboardOwner {

	public static final DataFlavor rangeFlavor = new DataFlavor(MomentExperience.class,"copy a moment");
	
	private static final DataFlavor[] flavors = {rangeFlavor};
	private MomentExperience moment;
	
	
	public copyAMoment(MomentExperience moment) {
		this.moment = moment;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return (DataFlavor[]) flavors.clone();
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// TODO Auto-generated method stub
		for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		if (flavor.equals(flavors[flavors.length])) {
            return moment;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
	}
	
	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}

}
