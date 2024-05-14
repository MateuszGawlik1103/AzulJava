package pl.edu.pw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * pattern lines
 * wall
 * floor line (minus points)
 */
public class PlayerBoard implements Serializable {
	private final Tile[][] wall = new Tile[5][5];
	private final List<Tile>[] patternLines;
	private final List<Tile> floorLine;
	private int score;
	private boolean hasToPick = false;
	private Tile pickedTile = null;

	public FactoryDisplay getPickedFactory() {
		return pickedFactory;
	}

	public void setPickedFactory(FactoryDisplay pickedFactory) {
		this.pickedFactory = pickedFactory;
	}

	private FactoryDisplay pickedFactory = null;

	public boolean isHasToPick() {
		return hasToPick;
	}

	public void setHasToPick(boolean hasToPick) {
		this.hasToPick = hasToPick;
	}

	public Tile getPickedTile() {
		return pickedTile;
	}

	public void setPickedTile(Tile pickedTile) {
		this.pickedTile = pickedTile;
	}




	public PlayerBoard() {
		score = 0;
		floorLine = new ArrayList<>();
		patternLines = new List[5];
		for (int i = 0; i < 5; i++) {
			patternLines[i] = new ArrayList<>();
		}
	}

	/**
	 * Add a tile to one of the pattern line
	 * @param tile to add
	 * @param lineIndex index of the pattern line (0-indexed)
	 */
	public void addToPatternLines(Tile tile, int lineIndex) {
		// each line starts one tile further from the left - hence the arithmetic
		int tileIndex = (5 - lineIndex + Tile.Color.indexOf(tile.getColor())) % 5;
		// if tile of a given color is already on the wall, it's dropped to the floor instead
		if (wall[lineIndex][tileIndex] != null) {
			addToFloorLine(tile);
		}
		// if line is full (width is progressing with the index), drop the tile to the floor
		else if (patternLines[lineIndex].size() == lineIndex + 1) {
			addToFloorLine(tile);
		}
		// if the line is not full and doesn't contain a different color, add the tile to it
		else if (patternLines[lineIndex].isEmpty() || patternLines[lineIndex].get(
				patternLines[lineIndex].size() - 1).equals(tile)) {
			patternLines[lineIndex].add(tile);
		} else {
			// the floor is the fallback too
			addToFloorLine(tile);
		}
	}

	/**
	 * Move tiles from completed pattern lines to the wall, scoring for appropriate connections (after a round)
	 */
	public void moveTilesToWall() throws Exceptions.AzulLineException {
		for (int lineIndex = 0; lineIndex < patternLines.length; lineIndex++) {
			if (patternLines[lineIndex].size() != lineIndex + 1) {
				continue;
			}
			if (patternLines[lineIndex].stream().distinct().count() > 1) {
				throw new Exceptions.AzulLineDifferentColorsException();
			}

			Tile.Color color = patternLines[lineIndex].get(0).getColor();
			int tileIndex = (5 - lineIndex + Tile.Color.indexOf(color)) % 5;
			if (wall[lineIndex][tileIndex] != null) {
				throw new Exceptions.AzulLineAlreadyFilledException();
			}
			wall[lineIndex][tileIndex] = patternLines[lineIndex].remove(0);
			TileDispenser.getInstance().discardTiles(patternLines[lineIndex]); // discard remaining tiles
			patternLines[lineIndex].clear();

			// scoring
			int horizontalPoints = 0;
			int verticalPoints = 0;
			// check for horizontal connections
			for (int i = 1; i < 5-lineIndex; i++) {
				if (tileIndex + i < 5 && wall[lineIndex][tileIndex + i] != null) {
					horizontalPoints++;
				} else {
					break;
				}
			}
			for (int i = 0; i > -lineIndex-1; i--) {
				if (tileIndex + i >= 0 && wall[lineIndex][tileIndex + i] != null) {
					horizontalPoints++;
				} else {
					break;
				}
			}
			// check for vertical connections
			for (int i = 1; i < 5-lineIndex; i++) {
				if (lineIndex + i < 5 && wall[lineIndex + i][tileIndex] != null) {
					verticalPoints++;
				} else {
					break;
				}
			}
			for (int i = 0; i > -lineIndex-1; i--) {
				if (lineIndex + i >= 0 && wall[lineIndex + i][tileIndex] != null) {
					verticalPoints++;
				} else {
					break;
				}
			}
			// add points
			if (horizontalPoints == 1 && verticalPoints == 1) {
				score += 1;
			} else {
				score += horizontalPoints + verticalPoints;
			}
		}
		// remove points based on floor line length
		score -= floorLine.size() + Math.max(floorLine.size()-2, 0) + Math.max(floorLine.size()-5, 0);
		// ensure score is not negative
		score = Math.max(score, 0);
		// discard floor line
		for (Tile tile : floorLine) {
			if (!tile.equals(FirstPlayerToken.getInstance())) {
				TileDispenser.getInstance().discardTiles(tile);
			}
		}
	}

	/**
	 * Add tile to the floor line
	 */
	public void addToFloorLine(Tile tile) {
		int size = floorLine.size();
		if (size >= 7) {
			TileDispenser.getInstance().discardTiles(tile);
			return;
		}
		floorLine.add(tile);
	}

	/**
	 * Remove tiles from pattern lines.
	 */
	public void clearPatternLines() {
		for (int i = 0; i < 5; i++) {
			patternLines[i] = new ArrayList<>();
		}
	}

	/**
	 * Remove tiles from the floor line.
	 */
	public void clearFloorLine() {
		floorLine.clear();
	}

	/**
	 * Check if the floor line contains the first player token
	 * @return true if the floor line contains the first player token, false otherwise
	 */
	public boolean hasFirstPlayerToken() {
		return floorLine.stream().anyMatch(tile -> tile.equals(FirstPlayerToken.getInstance()));
	}

	/**
	 * Add bonus points
	 ** @param points number of points to add
	 */
	public void updateScore(int points) {
		score += points;
	}

	public int getScore() {
		return score;
	}

	public List<Tile> getFloorLine() {
		return floorLine;
	}


	public Tile[][] getWall() {
		return wall;
	}


	public List<Tile>[] getPatternLines() {
		return patternLines;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PlayerBoard that)) return false;

		if (score != that.score) return false;
		if (!Arrays.deepEquals(wall, that.wall)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(patternLines, that.patternLines)) return false;
		return floorLine.equals(that.floorLine);
	}

	@Override
	public int hashCode() {
		int result = Arrays.deepHashCode(wall);
		result = 31 * result + Arrays.hashCode(patternLines);
		result = 31 * result + floorLine.hashCode();
		result = 31 * result + score;
		return result;
	}
}
