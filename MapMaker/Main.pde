import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;


int scroll = 0;
boolean leftClick = false, rightClick = false;


boolean reset = true;
boolean s, p, f;
boolean loadMap = true;

Grid grid;
ArrayList<Integer> GridSizeX = new ArrayList();
ArrayList<Integer> GridSizeY = new ArrayList();
long gridWidth, gridHeight;

int state = 1;

public void setup() {
  size(640, 700);
  background(0);
  frameRate(60);
}



public void draw() {
  if (state == 1 || state == 2) {
    if (mousePressed && mouseButton == LEFT && mouseX > width/2-150 && mouseX < width/2+150 && mouseY > height-75 && mouseY < height+75) {
      grid = loadMap("map");
      state = 0;
    }
  }


  if (state == 0) {
    background(0);
    scroll = constrain(scroll, -100, 12);
    grid.drawGrid();
    grid.drawColourPicker();

    if (s) {
      saveMap(grid.getGrid(), "map");
      s = false;
    }

    if (p) {
      printMap(grid.getGrid());
      p = false;
    }

    if (leftClick && !(mouseX > width/2-width/2 && mouseX < width/2+width/2 && mouseY > height-30-60/2 && mouseY < height-30+60/2) && grid.colourPicker.getFill()) {
      f = true;
    }


    if (f) {
      int originalColour = grid.currentTile.getTileType();
      int newColour = grid.colourPicker.getColourID();
      if (originalColour != newColour) {
        grid.fillGrid(grid.getGrid(), grid.currentTilex, grid.currentTiley, originalColour, newColour);
      }
      f = false;
    }
  }


  if (state == 1) {
    background(0);
    fill(255);
    textSize(70);
    textAlign(CENTER, CENTER);
    text("MAP EDITOR", width/2, height/2-250);

    textSize(50);
    textAlign(LEFT, CENTER);
    text("-------------------------------------------------", 0, height/2-20);

    gridWidth = 0;
    for (int element : GridSizeX) {
      String elementString = String.valueOf(element);
      int elementLength = elementString.length();
      gridWidth *= pow(10, elementLength);
      gridWidth += element;
    }

    textSize(30);
    textAlign(LEFT, CENTER);
    text("Insert grid width: " + String.valueOf(gridWidth), width/2-270, height/2-70);

    textSize(50);
    textAlign(LEFT, CENTER);
    text("-------------------------------------------------", 0, height/2-120);

    textSize(50);
    textAlign(CENTER, CENTER);
    text("HOTKEYS", width/2, height/2+40);
    textSize(30);
    textAlign(LEFT, CENTER);
    text("R - Reset Pan                                      Left Click - Draw", width/2-300, height/2+110);
    text("S - Save and Export Map               Right Click - Pan", width/2-300, height/2+150);
    text("P - Print Map in Console               Scroll - Zoom", width/2-300, height/2+190);

    rectMode(CENTER);
    fill(255);
    rect(width/2, height-50, 300, 50);
    fill(0);
    textSize(40);
    textAlign(CENTER, CENTER);
    text("Load Map", width/2, height-50);
  }

  if (state == 2) {
    background(0);
    fill(255);
    textSize(70);
    textAlign(CENTER, CENTER);
    text("MAP EDITOR", width/2, height/2-250);

    textSize(50);
    textAlign(LEFT, CENTER);
    text("-------------------------------------------------", 0, height/2-20);

    gridHeight = 0;
    for (int element : GridSizeY) {
      String elementString = String.valueOf(element);
      int elementLength = elementString.length();
      gridHeight *= pow(10, elementLength);
      gridHeight += element;
    }

    textSize(30);
    textAlign(LEFT, CENTER);
    text("Insert grid height: " + String.valueOf(gridHeight), width/2-270, height/2-70);

    textSize(50);
    textAlign(LEFT, CENTER);
    text("-------------------------------------------------", 0, height/2-120);

    textSize(50);
    textAlign(CENTER, CENTER);
    text("HOTKEYS", width/2, height/2+40);
    textSize(30);
    textAlign(LEFT, CENTER);
    text("R - Reset Pan                                      Left Click - Draw", width/2-300, height/2+110);
    text("S - Save and Export Map               Right Click - Pan", width/2-300, height/2+150);
    text("P - Print Map in Console               Scroll - Zoom", width/2-300, height/2+190);

    rectMode(CENTER);
    fill(255);
    rect(width/2, height-50, 300, 50);
    fill(0);
    textSize(40);
    textAlign(CENTER, CENTER);
    text("Load Map", width/2, height-50);


    // TODO implement back button
  }
}

void mouseWheel(MouseEvent event) {
  scroll += event.getCount();
}

void mousePressed() {
  if (mouseButton == LEFT) {
    leftClick = true;
  } else if (mouseButton == RIGHT) {
    rightClick = true;
    if (reset) {
      grid.startX = mouseX;
      grid.startY = mouseY;
      reset = false;
    } else {
      grid.startX = mouseX - grid.offsetX;
      grid.startY = mouseY - grid.offsetY;
    }
  }
}

void mouseReleased() {
  if (leftClick) {
    leftClick = false;
  } else if (rightClick) {
    rightClick = false;
  }
}

void keyPressed() {
  if (state == 0) {
    if (key == 'r') reset = true;
    if (key == 'p') p = true;
    if (key == 's') s = true;
  } else if (state == 1) {
    if (key >= '0' && key <= '9') {
      GridSizeX.add(key - '0');
    } else if (key == BACKSPACE && GridSizeX.size() > 0) {
      GridSizeX.remove(GridSizeX.size() - 1);
    } else if (key == ENTER || key == RETURN) {
      if (gridWidth > 0) {
        state = 2;
      }
    }
  } else if (state == 2) {
    if (key >= '0' && key <= '9') {
      GridSizeY.add(key - '0');
    } else if (key == BACKSPACE) {
      if (GridSizeY.size() > 0) {
        GridSizeY.remove(GridSizeX.size() - 1);
      } else state = 1;
    } else if (key == ENTER || key == RETURN) {
      if (gridHeight > 0) {
        grid = new Grid((int)gridHeight, (int)gridWidth);
        state = 0;
      }
    }
  }
}


String getMapPath(String mapName) {
  return "../source/src/main/resources/" + mapName + ".schneidermap";
}

void saveMap(Tile[][] matrix, String mapName) {
  String filePath = getMapPath(mapName);
  PrintWriter output = createWriter(filePath);
  int rows = matrix[0].length;
  int cols = matrix.length;
  output.println(cols);
  output.println(rows);
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      if (j == matrix.length-1) {
        output.print(matrix[j][i].getTileType());
      } else output.print(matrix[j][i].getTileType() + ",");
    }
    output.println();
  }
  output.flush();
  output.close();
}

void printMap(Tile[][] matrix) {
  for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix.length; j++) {
      if (j == matrix.length-1) {
        print(matrix[j][i].getTileType());
      } else print(matrix[j][i].getTileType() + ", ");
    }
    println();
  }
}

public Grid loadMap(String mapName) {
  try {
    String filePath = getMapPath(mapName);
    BufferedReader input = createReader(filePath);

    int width = int(input.readLine());
    int height = int(input.readLine());

    grid = new Grid(height, width);

    int y = 0;
    String line;
    while ((line = input.readLine()) != null) {
      int x = 0;
      for (char c : line.toCharArray()) {
        if (c >= '0' && c <= '9') {
          grid.gridMatrix[x][y] = tileFromNumber(c - '0');
          x++;
        }
      }
      y++;
    }

    return grid;
  }
  catch (IOException e) {
    e.printStackTrace();
    return new Grid(0, 0);
  }
}

Tile tileFromNumber(int number) {
  final Tile tile = new Tile(0, 0, grid.tileSize, grid.tileSize);
  tile.tileType = number;
  return tile;
}
