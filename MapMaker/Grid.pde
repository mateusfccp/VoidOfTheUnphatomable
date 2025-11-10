public class Grid { //<>//
  int tileSize = 20;
  int gridSizeX = 32;
  int gridSizeY = 32;
  int startX, startY;
  int offsetX, offsetY;
  boolean wallDetected = false;
  int currentTilex, currentTiley;
  Tile currentTile = new Tile(0, 0, tileSize, tileSize);

  ColourPicker colourPicker = new ColourPicker();

  public Tile[][] gridMatrix;

  public Grid(int gridSizeX, int gridSizeY) {
    this.gridSizeX = gridSizeX;
    this.gridSizeY = gridSizeY;

    gridMatrix = new Tile[gridSizeY][gridSizeX];
    for (int i = 0; i < gridSizeY; i++) {
      for (int j = 0; j < gridSizeX; j++) {
        gridMatrix[i][j] = new Tile(0, 0, tileSize, tileSize);
      }
    }
  }

  public void drawGrid() {
    if (rightClick) {
      offsetX = mouseX - startX;
      offsetY = mouseY - startY;
    }

    for (int i = 0; i < gridSizeY; i++) {
      for (int j = 0; j < gridSizeX; j++) {
        Tile tile = gridMatrix[i][j];
        int currentTileSize = tileSize - scroll;
        int x = (i)*currentTileSize+offsetX+gridSizeX/2*scroll-tileSize*(gridSizeX-32)/2;
        int y = (j)*currentTileSize+offsetY+gridSizeY/2*scroll-tileSize*(gridSizeY-32)/2;

        // Set values in gridMatrix
        tile.x = x;
        tile.y = y;
        tile.w = currentTileSize;
        tile.h = currentTileSize;

        if (!colourPicker.getFill()) {
          tile.clickSetTileType(colourPicker.getColourID());
        }
          currentTiley = j;
          currentTile = tile;
        }

        tile.drawTile();
      }
    }
  }

  void drawColourPicker() {
    colourPicker.drawColourPicker();
  }

  public Tile[][] getGrid() {
    return gridMatrix;
  }

  void setGridSizeX(int number) {
    this.gridSizeX = number;
  }

  void setGridSizeY(int number) {
    this.gridSizeY = number;
  }

  void setGridMatrix(Tile[][] matrix) {
    this.gridMatrix = matrix;
  }

  public void fillGrid(Tile[][] matrix, int i, int j, int originalColourID, int newColourID) {
    ArrayList<Coords> coordinates = new ArrayList<>();
    coordinates.add(new Coords(i, j));

    for (int m = 0; m < coordinates.size(); m++) {
      i = coordinates.get(m).getX();
      j = coordinates.get(m).getY();
      boolean condition = fillTile(matrix, i, j, originalColourID, newColourID);
      if (condition) {
        coordinates.add(new Coords(i+1, j));
        coordinates.add(new Coords(i-1, j));
        coordinates.add(new Coords(i, j+1));
        coordinates.add(new Coords(i, j-1));
      }
    }
  }


  public boolean fillTile(Tile[][] matrix, int x, int y, int originalColourID, int newColourID) {
    if (x < 0 || x >= matrix.length || y < 0 || y >= matrix[0].length) {
      return false;
    }
    if (matrix[x][y].getTileType() != originalColourID) {
      return false;
    }

    if (matrix[x][y].getTileType() == newColourID) {
      return false;
    }
    matrix[x][y].setTileType(newColourID);
    return true;
  }
}
