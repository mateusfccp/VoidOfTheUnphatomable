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
    case 0:
      fill(255);
      break;

    case 1:
      fill(0, 0, 255);
      break;

    case 2:
      fill(255, 0, 0);
      break;

    case 3:
      fill(255, 0, 255);
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
