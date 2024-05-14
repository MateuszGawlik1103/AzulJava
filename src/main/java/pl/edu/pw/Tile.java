package pl.edu.pw;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tile implements Serializable {
	private final Color color;

	/**
	 * WARNING: THIS CONSTRUCTOR IS FOR SERIALIZATION ONLY
	 */
	protected Tile() {
		this.color = null;
	}

	public Tile(Color color) {
		this.color = color;
	}


	public Tile(String color) {
		this.color = Color.from(color);
	}

	@Override
	public String toString() {
		return "Tile{" +
				"color=" + color +
				'}';
	}

	public Color getColor() {
		return color;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tile tile)) return false;

		return Objects.equals(color, tile.color);
	}

	@Override
	public int hashCode() {
		return color != null ? color.hashCode() : 0;
	}

	public enum Color {
		BLUE, YELLOW, RED, BLACK, WHITE, FIRSTPLAYERTOKEN;
		public static final Color[] gameColors = {Color.BLUE, Color.YELLOW, Color.RED, Color.BLACK, Color.WHITE};

		/**
		 * Get a color from a string
		 *
		 * @param name name of a color, case-insensitive
		 * @return color with the given name
		 */
		public static Color from(final String name) {
			return Color.valueOf(name.toUpperCase());
		}

		/**
		 * Get color at given index (same order as the first line on the wall)
		 *
		 * @param i index to find color at
		 * @return color at the given index in the first line on the wall
		 */
		public static Color at(final int i) {
			return Color.values()[i];
		}

		/**
		 * Find the index of a given color (by first line on the wall)
		 *
		 * @param color color to search for
		 * @return index it appears at in the first line on the wall (0-indexed)
		 */
		public static int indexOf(final Color color) {
			return Arrays.binarySearch(Color.values(), color);
		}
	}
}
