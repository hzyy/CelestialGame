package data;

import co.hzyy.celestial.screens.Game;

import java.util.ArrayList;
public class Inventory<Item> extends ArrayList<Item> {

	private int numOfItem;
	private int[][][] slot = new int [9][22][2];

	public void addItemByID(int itemID) {
		for (data.Item t : Game.itemCollection) {
			if (t!=null) {
				if (t.itemID == itemID) {
					add((Item) t);

				}
			}
		}
	}
	
}