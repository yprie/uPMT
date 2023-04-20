/*****************************************************************************
 * Main.java
 *****************************************************************************
 * Copyright � 2017 uPMT
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package application;

import java.io.IOException;

import components.comparison.ComparisonTable;
import components.comparison.ComparisonView;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

	public static void main(String[] args){ launch(args); }

	public void start(Stage primaryStage) throws IOException {
		UPMTApp app = new UPMTApp(primaryStage);

		Stage comparisonStage = new Stage();

		ComparisonView comparisonView = new ComparisonView(app.getCurrentProjectPath());
		comparisonView.start(comparisonStage);
		try {
			String path = app.getCurrentProjectPath();
			ComparisonTable ct = new ComparisonTable(path);
			ct.readTable();
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
