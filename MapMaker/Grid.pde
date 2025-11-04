public class Grid { //<>//
  int tileSize = 20;
  int gridSize = 32;
  int startX, startY;
  int offsetX, offsetY;
  boolean wallDetected = false;
  int currentTilex, currentTiley;
  Tile currentTile = new Tile(0, 0, tileSize, tileSize);

  ColourPicker colourPicker = new ColourPicker();

  Tile[][] gridMatrix;

  public Grid(int gridSize) {
    this.gridSize = gridSize;
    gridMatrix = new Tile[gridSize][gridSize];
    for (int i = 0; i < gridSize; i++) {
      for (int j = 0; j < gridSize; j++) {
        gridMatrix[i][j] = new Tile(0, 0, tileSize, tileSize);
      }
    }
  }

  public void drawGrid() {
    if (rightClick) {
      offsetX = mouseX - startX;
      offsetY = mouseY - startY;
    }


    for (int i = 0; i < gridSize; i++) {
      for (int j = 0; j < gridSize; j++) {
        Tile tile = gridMatrix[i][j];
        int currentTileSize = tileSize - scroll;
        int x = (i)*currentTileSize+offsetX+gridSize/2*scroll-tileSize*(gridSize-32)/2;
        int y = (j)*currentTileSize+offsetY+gridSize/2*scroll-tileSize*(gridSize-32)/2;

        // Set values in gridMatrix
        tile.x = x;
        tile.y = y;
        tile.w = currentTileSize;
        tile.h = currentTileSize;

        if (!colourPicker.getFill()) {
          tile.clickSetTileType(colourPicker.getColourID());
        }

        if (tile.matrixPosVerifier(x, y)) {
          currentTilex = i;
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


  void setGridSize(int number) {
    this.gridSize = number;
  }


  // UNUSED BUGGY FILL METHOD

  //void fillGrid(Tile[][] matrix) {

  //  boolean wallDetected = false;
  //  for (int i = 0; i <  matrix.length; i++) {
  //    for (int j = 0; j <  matrix.length; j++) {


  //      int aux = -1;

  //      for (int x =  matrix.length - 1; x > -1; x--) {
  //        if (matrix[x][i].getTileType() == 1) {
  //          aux = x;
  //          break;
  //        }
  //      }

  //      if (aux == -1) {
  //        continue;
  //      }

  //      if (matrix[j][i].equals(matrix[aux][i])) {
  //        wallDetected = false;
  //        continue;
  //      }

  //      if (j <  matrix.length - 1 && matrix[j + 1][i].getTileType() == 1) {
  //        if (wallDetected && matrix[j][i].getTileType() != 1) {
  //          matrix[j][i].setTileType(2);
  //        }
  //        continue;
  //      }

  //      if (j > 0 && matrix[j][i].getTileType() == 1) {
  //        wallDetected = !wallDetected;
  //      } else if (wallDetected && matrix[j][i].getTileType() != 1) {
  //        matrix[j][i].setTileType(2);
  //      }
  //    }
  //    wallDetected = false;
  //  }
  //}

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
    // Set Colour
    matrix[x][y].setTileType(newColourID);
    return true;
  }
}
