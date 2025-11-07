public class Tile {
  int x, y; // x-pos, y-pos
  int w, h; // width, height
  int tileStrokeWeight = 1; // Tracks border thickness for this tile

  int tileType = 0;

  public Tile(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  public void drawTile() {
    switch(tileType) {
    case 0: // Void
      fill(255);
      break;

    case 1: // Wall
      fill(0, 0, 255);
      break;

    case 2: // Floor
      fill(255, 0, 0);
      break;

    case 3: // Button that does button things
      fill(255, 0, 255);
      break;
    }

    rectMode(CORNER);
    stroke(0);
    // Use the tile-specific stroke weight
    strokeWeight(tileStrokeWeight);
    rect(x, y, w, h);
  }


  int getTileType() {
    return tileType;
  }


  void setTileType(int number) {
    this.tileType = number;
  }



  public boolean matrixPosVerifier(int x, int y) {
    tileStrokeWeight = 1;

    boolean hover = mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h
      && !(mouseX > width/2-width/2 && mouseX < width/2+width/2 && mouseY > height-30-60/2 && mouseY < height-30+60/2);

    if (hover) {
      tileStrokeWeight = 3;
      return true;
    }
    return false;
  }


  void clickSetTileType(int number) {
    if (leftClick && mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h
      && !(mouseX > width/2-width/2 && mouseX < width/2+width/2 && mouseY > height-30-60/2 && mouseY < height-30+60/2)) {
      this.tileType = number;
    }
  }
}


public class Coords {
  private int x = 0;
  private int y = 0;

  Coords(int x, int y) {
    this.x = x;
    this.y = y;
  }

  int getX() {
    return x;
  }

  int getY() {
    return y;
  }

  void setX(int number) {
    this.x = number;
  }

  void setY(int number) {
    this.y = number;
  }
}
