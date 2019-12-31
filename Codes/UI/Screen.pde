class Screen {
  int bg;
  Animal[] Animals;
  Food[] Foods;
  Text[] text;
  Button[] buttons;
  int type;
  int index;
  int levelTimer = 0;
  int levelSeconds = 20;
  int lboundx,lboundy,uboundx,uboundy;
  int acc = 0;
  int speed = 0;
  
  
  Screen (int c, Animal[] a, Food[] f, Text[] t, Button[] b, int ty, int i, int lx, int ly, int ux, int uy) {
    lboundx = lx;
    lboundy = ly;
    uboundx = ux;
    uboundy = uy;
    bg = c;
    Animals = a;
    Foods = f;
    text = t;
    buttons = b;
    type = ty;
    index = i;
  }

  int go() {
    int nextLevel = index;
      if (dragx < manager.screens[index].lboundx) {
        dragx = manager.screens[index].lboundx;
      }
      if (dragy < manager.screens[index].lboundy) {
        dragy = manager.screens[index].lboundy;
      }
      if (dragx > manager.screens[index].uboundx) {
        dragx = manager.screens[index].uboundx;
      }
      if (dragy > manager.screens[index].uboundy) {
        dragy = manager.screens[index].uboundy;
      }
      if (index == 0 && press == false && ((dragx < -25 && dragx > -screenW/2)||(dragx < -screenW-25 && dragx > -1.5*screenW)||(dragx < -2*screenW-25 && dragx > -2.5*screenW))){
        acc = 20;
      } else if (index == 0 && press == false && ((dragx < -screenW/2 && dragx > -screenW+25)||(dragx < -1.5*screenW && dragx > -2*screenW+25)||(dragx < -2.5*screenW && dragx > -3*screenW+25))) {
        acc = -20;
      } else {
        acc = 0;
      }
      speed += acc;
      dragx += acc;
      
      
    translate(dragx,dragy);
    image(manager.bgs[bg],screenW/2,screenH/2);
    
    if (index==0) {
      translate(-dragx,-dragy);
      image(manager.bgs[bg],screenW/2,screenH/2);
      translate(dragx,dragy);
      
      image(lvl1,screenW/2,screenH/2);
      image(lvl2,3*screenW/2,screenH/2);
      image(lvl3,5*screenW/2,screenH/2);
      image(spaceMode,7*screenW/2,screenH/2);
    }
    
    for (Animal i : Animals) {
      i.update(Animals);
      i.display();
    }
    for (Food i : Foods) {
      i.update(Animals);
      i.display();
    }
    for (Text i : text) {
      i.update();
    }
    for (Button i : buttons) {
      i.update();
    }
    translate(-dragx,-dragy);
    
    
    if (index > 1) {
      for (int i = 0; i < screenW/spikeW-1; i++) {
        fill(90);
        triangle(i * spikeW, 0, (i * spikeW) + spikeW / 2, spikeH, i * spikeW + spikeW, 0);
        triangle(i * spikeW, screenH, (i * spikeW) + spikeW / 2, screenH - spikeH, i * spikeW + spikeW, screenH);
      }
      for (int i = 0; i < screenH / spikeW; i++) {
        fill(90);
        triangle(0, i * spikeW, spikeH, ( i * spikeW) + spikeW / 2, 0, i * spikeW + spikeW);
        triangle(screenW, i * spikeW, screenW - spikeH, (i * spikeW) + spikeW / 2, screenW, i * spikeW + spikeW);
      }
    }

    if (type == 2) {

      text[1] = new Text("Your score is " + score, screenW/2, screenH/2.25);
    }
    if (type == 3) {
      
      text[1] = new Text("Your score is " + score, screenW/2, screenH/2.25);
    }
    if (type  == 1) {
      levelTimer = (millis() - manager.timeBegin) /1000;
      score = (millis() - manager.timeBegin)/1000;
      for (Animal i : Animals) {
        if (i.isDead == true) {
          score = (millis() - manager.timeBegin)/1000;
          nextLevel = 6;
          manager.restart();
          
        }
      }
      if (levelTimer == (index-1) * levelSeconds){
        nextLevel = nextLevel + 1;
      }
    }

    return nextLevel;
  }

  int button(float x, float y) {
    int nextLevel = index;
    for (Button i : buttons) {
      nextLevel = i.press( index);
      if (nextLevel != index) {
        break;
      }
    }
    return nextLevel;
  }
}
