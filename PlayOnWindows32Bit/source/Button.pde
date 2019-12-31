class Button {  
  int dest;
  float x;
  float y;
  String name;
  int col = 255;
  int tcol = 0;
  Button(int d, float xcoord, float ycoord, String n) {
    dest = d;
    x = xcoord;
    y = ycoord;
    name = n;
  }
  int press(int currentLevel) {
    int nextLevel = currentLevel;
    if (mouseX>(x+dragx-buttonW/2) && mouseX<(x+dragx+buttonW/2) && mouseY<(y+dragy+buttonH/2) && mouseY>(y+dragy-buttonH/2)) {
      dragx = 0;
      dragy = 0;
      nextLevel = dest;
      if(dest > 1) {
        manager.restart();
      }
    }
    return nextLevel;
  }

  void update() {
    fill(col);
    if (press == true && mouseX-dragx>(x-buttonW/2) && mouseX-dragx<(x+buttonW/2) && mouseY-dragy<(y+buttonH/2) && mouseY-dragy>(y-buttonH/2)) {
      fill(100);
    }
    rect(x-buttonW/2, y-buttonH/2, buttonW, buttonH);
    fill(tcol);
    if (press == true && mouseX-dragx>(x-buttonW/2) && mouseX-dragx<(x+buttonW/2) && mouseY-dragy<(y+buttonH/2) && mouseY-dragy>(y-buttonH/2)) {
      fill(255);
    }
    textAlign(CENTER);
    text(name, x, y);
  }
}
