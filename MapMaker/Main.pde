
int scroll = 0;
boolean leftClick = false, rightClick = false;


boolean reset = true;
boolean s, p, f;

Grid grid;
ArrayList<Integer> GridSize = new ArrayList();

int state = 1;

public void setup() {
  size(640, 700);
  background(0);
  frameRate(60);
}



public void draw() {
  if (state == 0) {
    background(0);
    scroll = constrain(scroll, -100, 12);
    grid.drawGrid();
    grid.drawColourPicker();

    if (s) {
      saveMap(grid.getGrid());
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

    textSize(30);
    textAlign(LEFT, CENTER);
    text("Insert grid size:", width/2-250, height/2-70);

    textSize(50);
    textAlign(LEFT, CENTER);
    text("-------------------------------------------------", 0, height/2-20);
    textSize(30);
    textAlign(LEFT, CENTER);
    text("Insert grid size:", width/2-250, height/2-70);
    long resultLong = 0;
    for (int element : GridSize) {
      String elementString = String.valueOf(element);
      int elementLength = elementString.length();
      resultLong *= Math.pow(10, elementLength);
      resultLong += element;
    }
    textSize(30);
    text(String.valueOf(resultLong), width/2-50, height/2-70);


    textSize(50);
    textAlign(LEFT, CENTER);
    text("-------------------------------------------------", 0, height/2-120);


    textSize(50);
    textAlign(CENTER, CENTER);
    text("HOTKEYS", width/2, height/2+40);
    textSize(30);
    textAlign(LEFT, CENTER);
    text("R - Reset Pan", width/2-250, height/2+110);
    text("S - Save and Export Map", width/2-250, height/2+150);
    text("P - Print Map in Console", width/2-250, height/2+190);
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
      GridSize.add(key - '0');
    } else if (key == BACKSPACE && GridSize.size() > 0) {
      GridSize.remove(GridSize.size() - 1);
    } else if (key == ENTER || key == RETURN) {
      long finalSize = 0;
      for (int element : GridSize) {
        String elementString = String.valueOf(element);
        int elementLength = elementString.length();
        finalSize *= Math.pow(10, elementLength);
        finalSize += element;
      }
      if (finalSize > 0) {
        grid = new Grid((int)finalSize);
        state = 0;
      }
    }
  }
}



void saveMap(Tile[][] matrix) {
  PrintWriter output = createWriter("Map.txt");
  for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix.length; j++) {
      if (j == matrix.length-1) {
        output.print(matrix[j][i].getTileType());
      } else output.print(matrix[j][i].getTileType() + ", ");
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
