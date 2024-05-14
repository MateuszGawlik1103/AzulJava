package pl.edu.pw;

import java.util.ArrayList;
import java.util.List;

public class FirstPlayerToken extends Tile {
	private static FirstPlayerToken instance;

	private FirstPlayerToken() {
		super(Color.FIRSTPLAYERTOKEN);
	}

	//there is only one instance of object FirstPlayerToken
	public static FirstPlayerToken getInstance() {
		if (instance == null) {
			instance = new FirstPlayerToken();
		}
		return instance;
	}

	@Override
	public String toString() {
		return "FirstPlayerToken";
	}

}