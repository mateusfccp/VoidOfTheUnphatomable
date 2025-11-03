public class ColourPicker {
  int x, y, w, h; // x-pos, y-pos, width, height

  ArrayList<Button> colours = new ArrayList<>();
  ArrayList<Button> txt = new ArrayList<>();
  int colourID = 0;
  boolean fill = false;

  public ColourPicker() {
    for (int i = 0; i < 4; i++) {
      colours.add(new Button(640*(i+1)/5-i*30-70, 700-30, 30, 30, i));
    }

    txt.add(new Button(640/2+190, 700-30, 40, 20, false));
    txt.add(new Button(640/2+270, 700-30, 40, 20, true));
  }

  public void drawColourPicker() {
    rectMode(CENTER);
    fill(155);
    noStroke();
    rect(width/2, height-30, width, 60);
    fill(0);
    rect(width/2+120, height-30, 10, 60);

    for (Button button : colours) {
      if (button.buttonLogic()) {
        colourID =  button.getColourID();
      }
      if (button.getColourID() == colourID) {
        button.setStrokeWeight(3);
      } else {
        button.setStrokeWeight(0);
      }
      button.drawColourButton();
    }


    for (Button button : txt) {
      if (button.buttonLogic()) {
        fill = button.getFillBool();
      }
      if (button.getFillBool() == fill) {
        button.setStrokeWeight(3);
      } else {
        button.setStrokeWeight(0);
      }
      button.drawFillButton();
    }
  }


  int getColourID() {
    return colourID;
  }
  
  
    boolean getFill() {
    return fill;
  }
}
