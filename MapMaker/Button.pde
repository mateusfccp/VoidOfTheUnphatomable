public class Button {
  int x, y, w, h; // x-pos, y-pos, width, height

  private int colourID = 0;
  private boolean fill = false;
  private int strokeWeight = 0;

  Button(int x, int y, int w, int h, int colourID) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.colourID = colourID;
  }

  Button(int x, int y, int w, int h, boolean fill) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.fill = fill;
  }



  public void drawColourButton() {
    switch(colourID) {
     case 0: // Void
      fill(255);
      break;

    case 1: // Wall
      fill(0, 0, 255);
      break;

    case 2: // Floor
      fill(190);
      break;

    case 3: // Remove vision
      fill(255, 0, 255);
      break;

    case 4: // Breakable/Special Wall
      fill(123, 73, 23);
      break;

    case 5: // Player Entity
      fill(255, 255, 0);
      break;

    case 6: // Enemy Entity
      fill(2, 166, 37);
      break;

    case 7: // Item Entity
      fill(250, 100, 152);
      break;

    case 8: // Key Entity
      fill(255, 0, 0);
      break;

    case 9: // Merchant
      fill(235, 146, 77);
      break;
    }

    rectMode(CENTER);
    stroke(0);
    strokeWeight(strokeWeight);
    rect(x, y, w, h);
  }


  public void drawFillButton() {
    rectMode(CENTER);
    stroke(0);
    fill(50);
    strokeWeight(strokeWeight);
    rect(x, y, w, h);

    fill(255);
    textSize(10);
    textAlign(CENTER, CENTER);

    if (fill) {
      text("Fill", x, y);
    } else {
      text("Brush", x, y);
    }
  }



  void setStrokeWeight(int number) {
    this.strokeWeight = number;
  }

  void setFillBool(boolean bool) {
    this.fill = bool;
  }

  boolean getFillBool() {
    return fill;
  }

  int getColourID() {
    return colourID;
  }



  public boolean buttonLogic() {
    if (mousePressed && mouseButton == LEFT && mouseX > x-w/2 && mouseX < x+w/2 && mouseY > y-h/2 && mouseY < y+h/2) {
      return true;
    } else return false;
  }
}
