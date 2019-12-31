import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class UI extends PApplet {

class UIManager {

  int timeBegin;
  int timeEnd;
  int time;
  int current;
  int number;
  Animal[] inspace = {new Animal("sheep", new PVector(screenW/3, screenH/2)), new Animal("sheep", new PVector(2*screenW/3, screenH/2))};
  Screen start;
  Screen spacemode;
  Screen gameplay;
  Screen gameOver;
  Screen[] screens;
  PImage[] bgs = new PImage[2];
  Animal[] animals;

  
  UIManager(int n) {
    number = n;
    screens = new Screen[number];

    // Screen 0 - Start Screen
    Button[] titlebuttons = {new Button(2,screenW/3.1f, screenH-250, "Level 1"), new Button(3,screenW/3.1f+screenW, screenH-250, "Level 2"), new Button(4,screenW/3.1f+2*screenW, screenH-250, "Level 3"), new Button(1,screenW/3.1f+3*screenW, screenH-250,"Space Mode")};
    Button[] spacebuttons = {new Button(0, 100, 50, "Main Menu")};
    start = new Screen(0, new Animal[0], new Food[0], new Text[0], titlebuttons, 0, 0,-screenW*3,0,0,0);
    screens[0] = start;
    // Screen 1 - Space
    spacemode = new Screen(1, inspace, new Food[0], new Text[0], spacebuttons, 0, 1,PApplet.parseInt(-.5f*screenW),PApplet.parseInt(-.5f*screenH),PApplet.parseInt(.5f*screenW),PApplet.parseInt(.5f*screenH));
    screens[1] = spacemode;
    // Backgrounds
    bgs[0] = bg1;
    bgs[1] = spacebg;

    // Game Levels
    restart();
    
    // Game over Screen
    Text[] compText = {new Text("Game Over", screenW/2, screenH/2.5f), new Text("", screenW/2, screenH/2)};
    Button[] compTextPlayAgain = {new Button(0, screenW/2, screenH/2, "Main Menu")};
    gameOver = new Screen(0, new Animal[0], new Food[0], compText, compTextPlayAgain, 3, 5,0,0,0,0);
    screens[5] = gameOver;
    // Another game over Screen
    Text[] gameOverText = {new Text("Game Over", screenW/2, screenH/2.5f), new Text("", screenW/2, screenH/2)};
    Button[] playAgain = {new Button(0, screenW/2, screenH/2, "Main Menu")};
    gameOver = new Screen(0, new Animal[0], new Food[0], gameOverText, playAgain, 2, 6,0,0,0,0);
    screens[6] = gameOver;
    
    current = 0;
  }


  public void restart() {
    // Restart timer 
    timeBegin = millis();
    // Recreate all levels
    screens[2] = createLevel1();
    screens[3] = createLevel2();
    screens[4] = createLevel3();
    // Update the current level
    current = 2;
  }
  
  public void run() {
    // Update function
    current = screens[current].go();
  }


  public void pressCheck() {
    if (current != 0 && current != number-1  && current != number-2 && current != 1) {
      screens[current].Animals[0].pressCheck();
    } else if (current == 1);
      for (int i = 0; i < screens[current].Animals.length; i++) {
        screens[current].Animals[i].pressCheck();
      }
  }


  public void button(float x, float y) {
    if (current == 0 || current == 1 || current == number-1  || current == number-2) {
      current = screens[current].button(mouseX, mouseY);
    }
  }
}
class Animal {
  PImage image, worriedImage, dizzyImage, angryImage;
  PVector pos, speed, acc;
  int index;
  float diameter;
  float mass;
  float gravityConstant = 0.1f;
  float bouncingConstant = 0.4f;
  float angle = 0;
  float angSpeed;
  float angAcc = 0;
  boolean isDead = false;
  boolean isWorried = false;
  boolean isWorried2 = false;
  boolean isAngry = false;
  String type;
  float rotatingCons = (2*3.14159265359f)/360;
  float inertia = 10000;
  PVector J;
  PVector w;
  PVector collisionPos;

  Animal(String t, PVector p) {
    type = t;
    pos = p;
    if (type.equals("blackhole")) {
      diameter = PApplet.parseInt(blackholeW);
      mass = 300000;
      angSpeed = 0.06f;
      image = blackhole;
      worriedImage = blackhole;
      dizzyImage = blackhole;
    }

    if (type.equals("sheep")) {
      diameter = PApplet.parseInt(sheepW);
      mass = diameter*diameter;
      angSpeed = 0.1f/random(-20, 20);
      image = sheep;
      worriedImage = worriedSheep;
      dizzyImage = dizzySheep;
    }
    
    if (type.equals("rabbit")) {
      diameter = PApplet.parseInt(rabbitW);
      mass = diameter*diameter;
      angSpeed = 0.1f/random(-20, 20);
      image = rabbit;
      worriedImage = rabbit;
      dizzyImage = rabbit;
    }

    if (type.equals("chicken")) {
      diameter = PApplet.parseInt(chickenW);
      mass = diameter*diameter;
      angSpeed = 0.1f/random(-20, 20);
      image = chicken;
      worriedImage = worriedChicken;
      dizzyImage = dizzyChicken;
    }
    if (type.equals("wolf")) {
      diameter = PApplet.parseInt(wolfW);
      mass = diameter*diameter;
      angSpeed = 0.1f/random(-20, 20);
      image = wolf;
      worriedImage = worriedWolf;
      dizzyImage = wolf;
      angryImage = angryWolf;
    }   

    acc = new PVector(0, 0);
    speed = new PVector(0, 0);
    J = new PVector(0, 0);
    w = new PVector(0, 0);
    collisionPos = new PVector(0, 0);
  }

  public void update(Animal[] Animals) {
    speed.add(acc.copy().mult(timeStep));
    pos.add(speed.copy().mult(timeStep));
    acc.setMag(0);
    angle = angle + angSpeed * timeStep;
    angSpeed = angSpeed + angAcc;
    angAcc = 0;
    isWorried2 = false;
    isAngry = false;
    for (Animal i : Animals) {

      float r = dist(i.pos.x, i.pos.y, pos.x, pos.y);

      if ((r < (1.5f*diameter)) && r != 0 && type.equals("wolf") && (i.type.equals("sheep") || i.type.equals("chicken") )) {
        isAngry = true;
      } 
      if ((r < (1.5f*diameter)) && r != 0 && i.type.equals("wolf")  && (type.equals("sheep") || type.equals("chicken") )) {
        isWorried2 = true;
      } else if ((r < (1.5f*diameter)) && r != 0 && i.type.equals("blackhole")) {
        isWorried2 = true;
      } 

      if ((r < (i.diameter / 2 + diameter / 2)) && r != 0) {
        if (i.type.equals("wolf")) {
          if (r < i.diameter/2) {
            isDead = true;
          }
        } else if (i.type.equals("blackhole")) {
          if (r < diameter/2) {
            isDead = true;
          }
        } else if (type.equals("blackhole")) {
          if (r < i.diameter/2) {
            i.isDead = true;
          }
        } else if (type.equals("wolf")) {
          if (r < diameter/2) {
            i.isDead = true;
          }
        }
        if (((i.type.equals("rabbit")==true || i.type.equals("sheep")==true || i.type.equals("chicken")==true)  && (type.equals("rabbit")==true || type.equals("sheep")==true || type.equals("chicken")==true)) || (i.type.equals("wolf")==true  && type.equals("wolf")==true)) {           
          PVector collision =  new PVector(pos.x-i.pos.x, pos.y-i.pos.y);
          collision.div(dist(pos.x, pos.y, i.pos.x, i.pos.y));
          collisionPos = i.pos.copy().add(collision.copy().mult(diameter/2));
          i.collisionPos = collisionPos;
          pos = i.pos.copy().add(collision.copy().mult(i.diameter/2 + diameter/2) );

          // Old one
          //float dot1 = speed.copy().dot(collision);
          //float dot2 = i.speed.copy().dot(collision);
          //float v1 = ((dot1 * (mass - i.mass) + 2 * i.mass*dot2)/(mass + i.mass)) * bouncingConstant;
          //float v2 = ((dot2 * (i.mass - mass) + 2 * mass*dot1)/(mass + i.mass)) * bouncingConstant;
          //PVector speedf = speed.copy().add(collision.copy().mult(v1 - dot1));
          //PVector speedf2 = i.speed.copy().add(collision.copy().mult(v2 - dot2));
          //J.set(-mass * (speedf.x + speed.x), -mass * (speedf.y + speed.y));
          //i.J.set(-i.mass * (speedf2.x + i.speed.x), -i.mass * (speedf2.y + i.speed.y));
          //w.set((w.x - ((diameter/2) * (J.x - J.y))) / (0.5 * mass * sq(diameter/2)), (w.y - ((diameter/2) * (J.x - J.y))) / (0.5 * mass * sq(diameter/2)));
          //i.w.set((i.w.x - ((i.diameter/2) * (i.J.x - i.J.y))) / (0.5 * i.mass * sq(i.diameter/2)), (i.w.y - ((i.diameter/2) * (i.J.x - i.J.y))) / (0.5 * i.mass * sq(i.diameter/2)));
          //angSpeed = w.mag()/(diameter/2);
          //i.angSpeed = i.w.mag()/(i.diameter/2);
          //if ((collisionPos.y < pos.y && w.x < 0) || (collisionPos.y > pos.y && w.x > 0)) {
          //  angSpeed *= -1;
          //}
          //if ((i.collisionPos.y < i.pos.y && i.w.x < 0) || (i.collisionPos.y > i.pos.y && i.w.x > 0)) {
          //  i.angSpeed *= -1;
          //}
          //speed.set(speedf);
          //i.speed.set(speedf2);
                 
          // Tried the one in http://www.euclideanspace.com/physics/dynamics/collision/twod/index.htm. Did not work well :(
          float e = 1;
          float ma = mass;
          float mb = i.mass;
          PVector ra = collision.copy().mult(diameter/2);
          PVector rb = collision.copy().mult(- i.diameter/2);
          PVector vai = speed; 
          PVector vbi = i.speed;
          float wai = angSpeed;
          float wbi = i.angSpeed;
          PVector vaf;
          PVector vbf;
          float waf;
          float wbf;
          float Ia = inertia;
          float Ib = inertia;
          float k = 1/(ma*ma)+ 2/(ma*mb) +1/(mb*mb) - ra.x*ra.x/(ma*Ia) - rb.x*rb.x/(ma*Ib)  - ra.y*ra.y/(ma*Ia) - ra.y*ra.y/(mb*Ib) - ra.x*ra.x/(mb*Ia) - rb.x*rb.x/(mb*Ib) - rb.y*rb.y/(ma*Ib) - rb.y*rb.y/(mb*Ib) + ra.y*ra.y*rb.x*rb.x/(Ia*Ib) + ra.x*ra.x*rb.y*rb.y/(Ia*Ib) - 2*ra.x*ra.y*rb.x*rb.y/(Ia*Ib);
          float Jx =  (e+1)/k * (vai.x - vbi.x) * ( 1/ma - ra.x*ra.x/Ia + 1/mb - rb.x*rb.x/Ib) - (e+1)/k * (vai.y - vbi.y) * (ra.x*ra.y/Ia + rb.x*rb.y/Ib );
          float Jy =  (e+1)/k * (vai.y - vbi.y) * ( 1/ma - ra.y*ra.y/Ia + 1/mb - rb.y*rb.y/Ib) + (e+1)/k * (vai.x - vbi.x) * (ra.x*ra.y/Ia + rb.x*rb.y /Ib );
          vaf = new PVector(vai.x - Jx/ma, vai.y - Jy/ma);
          vbf = new PVector(vbi.x + Jx/mb, vbi.y + Jy/mb);
          waf = wai - (Jx * ra.y - Jy * ra.x) /Ia;
          wbf = wbi + (Jx * rb.y - Jy * rb.x) /Ib;
          angSpeed = waf;
          i.angSpeed = wbf;
          speed.set(vaf);
          i.speed.set(vbf);
         

        }
      } else if (r != 0 ) {
        float force = (gravityConstant * mass * i.mass) / (sq(r));
        PVector f = new PVector(i.pos.x - pos.x, i.pos.y - pos.y);
        acc.add(f.copy().mult(force / mass));
      }
    }

    if (manager.screens[manager.current].index >1) {
      if (pos.x < 1.5f * diameter || pos.x > screenW - 1.5f * diameter || pos.y < 1.5f * diameter || pos.y > screenH - 1.5f * diameter) {
        isWorried = true;
      } else {
        isWorried = false;
      }

      if (pos.x > screenW - diameter / 2 || pos.x < diameter / 2 || pos.y > screenH - diameter / 2 || pos.y < diameter / 2) {
        isDead = true;
      }
    } else if (manager.screens[manager.current].index == 1) {
      if (pos.x <-screenW/2+.5f*diameter) {
        pos.x = -screenW/2+.5f*diameter;
        speed.x = -speed.x;
      }
      if (pos.x >1.5f * screenW - diameter / 2) {
        pos.x = 1.5f * screenW - diameter / 2;
        speed.x = -speed.x;
      }
      if (pos.y <-screenH/2+.5f*diameter) {
        pos.y = -screenH/2+.5f*diameter;
        speed.y = -speed.y;
      }
      if (pos.y >1.5f * screenH - diameter / 2) {
        pos.y = 1.5f * screenH - diameter / 2;
        speed.y = -speed.y;
      }
    }
    if (drag == 1 && (type=="blackhole"||manager.current==1)) {
      press = false;
      pos.x = mouseX;
      pos.y = mouseY;
      speed.set(new PVector(mouseX-pmouseX, mouseY-pmouseY));
    }
  }
  public void pressCheck() {
    if (press == true && (type == "blackhole" || manager.current==1)) {
      if (dist(pos.x, pos.y, mouseX, mouseY) < diameter/2) {
        drag = 1;
      }
    }
  }

  public void display() {
    PImage draw;
    if (abs(angSpeed) > 0.2f && type.equals("blackhole")==false) {
      draw = dizzyImage;
    } else if (isWorried == true || isWorried2 == true) {
      draw = worriedImage;
    } else if (isAngry == true) {
      draw = angryImage;
    } else {
      draw = image;
    }

    fill(colors[index]);
    translate(pos.x, pos.y);
    rotate(angle*rotatingCons);
    if(type.equals("rabbit")) {
      translate(-0.2f*rabbitW,-.45f*rabbitW);
      rotate(20*angle);
      image(ears,0,-.25f*rabbitW);
      rotate(-20*angle);
      translate(0.4f*rabbitW,0);
      translate(-.1f*rabbitW,0);
      rotate(-20*angle);
      image(ears,0,-.25f*rabbitW);
      rotate(20*angle);
      translate(.1f*rabbitW,0);

      translate(-0.2f*rabbitW,.45f*rabbitW);
    }
    image(draw, 0, 0);
    rotate(-angle*rotatingCons);
    translate(-pos.x, -pos.y);
  }
}
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
  public int press(int currentLevel) {
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

  public void update() {
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
class Food {
  PImage image;
  PVector pos;
  int index;
  float diameter;
  String type;
  boolean isCollected = false;




  Food(String t, PVector p) {
    type = t;
    pos = p;
    if (type.equals("hayBale")) {
      diameter = PApplet.parseInt(blackholeW);
      image = hayBale;
    }

    if (type.equals("feed")) {
      diameter = PApplet.parseInt(sheepW);
      image = feed;
    }
  }


  public void update(Animal[] Animals) {


    for (Animal i : Animals) {
      if (dist(i.pos.x, i.pos.y, pos.x, pos.y) < 50 && !i.type.equals("blackhole")) {
        isCollected = true;
      }
    }
  }

  public void display() {
    if (isCollected == false) {
      translate(pos.x, pos.y);
      image(image, 0, 0);
      translate(-pos.x, -pos.y);
    }
    if (isCollected == true) {
      image(image, 0, 0);
    }
  }
}
// It is a very cool game implemented by Rebecca and Kursat.

//Images
PImage wolf, angryWolf, worriedWolf;
PImage chicken, worriedChicken, dizzyChicken;
PImage sheep, worriedSheep, dizzySheep;
PImage rabbit, ears;
PImage hayBale, feed;
PImage blackhole;
PImage bg1, spacebg;
PImage lvl1,lvl2,lvl3,spaceMode;

//Sizing
int screenW = 1050, screenH = 750;
float spikeW = (screenW * screenH) / 50000, spikeH = 1.2f * spikeW;
float sheepW = screenH/10;
float chickenW = screenH/15;
float rabbitW = screenH/15;
float wolfW = screenH/10;
float blackholeW = screenH/15;
float hayBaleW = screenH/20;
float feedW = screenH/20;
float buttonW = screenH/5, buttonH = screenH/10;

int score = 0;
int rotation = 0;
int drag;
int numberOfScreens = 7;
float timeStep = 0.01f;
float mxi, myi;
float dragx, dragy = 0;
float screenx, screeny = 0;

boolean press = false;
boolean isGameOver = false;
int[] colors = new int[numberOfScreens];

UIManager manager;

public void settings() {
  screenW = displayWidth;
  screenH = displayHeight;
  fullScreen();
}

public void setup() {  

  wolf = loadImage("wolf.png");
  angryWolf = loadImage("angrywolf.png");
  worriedWolf = loadImage("worriedwolf.png");
  chicken = loadImage("chicken.png");
  worriedChicken = loadImage("worriedchicken.png");
  dizzyChicken = loadImage("dizzychicken.png");
  sheep = loadImage("sheep.png");
  worriedSheep = loadImage("worriedsheep.png");
  dizzySheep = loadImage("dizzysheep.png");
  rabbit = loadImage("rabbit.png");
  ears = loadImage("ear.png");
  hayBale = loadImage("haybale.png");
  feed = loadImage("feed.png");
  blackhole = loadImage("blackhole.png");
  bg1 = loadImage("level1bg.png");
  spacebg = loadImage("space.png");
  
  lvl1 = loadImage("lvl1.png");
  lvl2 = loadImage("lvl2.png");
  lvl3 = loadImage("lvl3.png");
  spaceMode = loadImage("spacemode.png");


  
  spacebg.resize(2*screenW, 2*screenH);
  bg1.resize(screenW, screenH);
  wolf.resize(PApplet.parseInt(wolfW),PApplet.parseInt(wolfW));
  angryWolf.resize(PApplet.parseInt(wolfW),PApplet.parseInt(wolfW));
  worriedWolf.resize(PApplet.parseInt(wolfW), PApplet.parseInt(wolfW));
  sheep.resize(PApplet.parseInt(sheepW), PApplet.parseInt(sheepW));
  worriedSheep.resize(PApplet.parseInt(sheepW), PApplet.parseInt(sheepW));
  dizzySheep.resize(PApplet.parseInt(sheepW), PApplet.parseInt(sheepW));
  rabbit.resize(PApplet.parseInt(rabbitW),PApplet.parseInt(rabbitW));
  ears.resize(PApplet.parseInt(rabbitW/3),PApplet.parseInt(.75f*rabbitW));
  chicken.resize(PApplet.parseInt(chickenW), PApplet.parseInt(chickenW));
  worriedChicken.resize(PApplet.parseInt(chickenW), PApplet.parseInt(chickenW));
  dizzyChicken.resize(PApplet.parseInt(chickenW),PApplet.parseInt(chickenW));
  hayBale.resize(PApplet.parseInt(hayBaleW), PApplet.parseInt(hayBaleW));
  feed.resize(PApplet.parseInt(feedW), PApplet.parseInt(feedW));
  
  lvl1.resize(screenW-300,screenH-100);
  lvl2.resize(screenW-300,screenH-100);
  lvl3.resize(screenW-300,screenH-100);
  spaceMode.resize(screenW-300,screenH-100);

  
  
  blackhole.resize(PApplet.parseInt(blackholeW), PApplet.parseInt(blackholeW));
  imageMode(CENTER);
  shapeMode(CENTER);
  manager = new UIManager(numberOfScreens);
}

public void draw() {
  rotation++;
  background(0);
  manager.run();
}


public void mousePressed() {
  press = true;
  manager.pressCheck();

  mxi = mouseX - dragx;
  myi = mouseY - dragy;
}

public void mouseDragged() {
  if (drag == 0) {
      dragx = mouseX-mxi;
      dragy = mouseY-myi;
   }
}

public void mouseReleased() {
  press = false;
  drag = 0;
  manager.button(mouseX, mouseY);
}
public Screen createLevel1() {
  int numberAnimal = 5;
  int numberFood = 4;
  Animal []animals = new Animal[numberAnimal];
  Food []foods = new Food[numberFood];
  foods = new Food[numberFood];
  int ballDiameter = PApplet.parseInt(chickenW);
  PVector rand = new PVector(500, 300);

  animals[0] = new Animal("blackhole", new PVector(screenW/2, screenH/2));
  drag = 0;
  //for (int i=1; i<numberAnimal; i++) {
  //  rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  do {
  //    rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  } while ((rand.x <550 & rand.x >450 ) | (rand.y <350 & rand.y >250 ));
  //  animals[i] = new Animal("chicken", rand);
  //}

  animals[1] = new Animal("rabbit", new PVector(screenW/4, screenH/4));
  animals[2] = new Animal("rabbit", new PVector(3*screenW/4, screenH/4));
  animals[3] = new Animal("rabbit", new PVector(screenW/4, 3*screenH/4));
  animals[4] = new Animal("rabbit", new PVector(3*screenW/4, 3*screenH/4));
  foods[0] = new Food("feed", new PVector(screenW/6, screenH/6));
  foods[1] = new Food("feed", new PVector(5*screenW/6, screenH/6));
  foods[2] = new Food("feed", new PVector(screenW/6, 5*screenH/6));
  foods[3] = new Food("feed", new PVector(5*screenW/6, 5*screenH/6));


  Screen screen = new Screen(0, animals, foods, new Text[0], new Button[0], 1, 2,0,0,0,0);
  return screen;
}


public Screen createLevel2() {
  int numberAnimal = 5;
  int numberFood = 4;
  Animal []animals = new Animal[numberAnimal];
  Food []foods = new Food[numberFood];
  animals = new Animal[numberAnimal];
  int ballDiameter = PApplet.parseInt(sheepW);
  PVector rand = new PVector(500, 300);

  animals[0] = new Animal("blackhole", new PVector(screenW/2, screenH/2));
  drag = 0;
  //for (int i=1; i<numberAnimal; i++) {
  //  rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  do {
  //    rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  } while ((rand.x <550 & rand.x >450 ) | (rand.y <350 & rand.y >250 ));
  //  animals[i] = new Animal("sheep", rand);
  //}

  animals[1] = new Animal("sheep", new PVector(screenW/4, screenH/4));
  animals[2] = new Animal("sheep", new PVector(3*screenW/4, screenH/4));
  animals[3] = new Animal("sheep", new PVector(screenW/4, 3*screenH/4));
  animals[4] = new Animal("sheep", new PVector(3*screenW/4, 3*screenH/4));
  foods[0] = new Food("hayBale", new PVector(screenW/6, screenH/6));
  foods[1] = new Food("hayBale", new PVector(5*screenW/6, screenH/6));
  foods[2] = new Food("hayBale", new PVector(screenW/6, 5*screenH/6));
  foods[3] = new Food("hayBale", new PVector(5*screenW/6, 5*screenH/6));
  Screen screen  = new Screen(0, animals, foods, new Text[0], new Button[0], 1, 3,0,0,0,0);
  return screen;
}


public Screen createLevel3() {
  int numberSheep = 4;
  int numberWolf = 1;    
  int numberFood = 4;
  Food []foods = new Food[numberFood];
  Animal []animals = new Animal[numberSheep + numberWolf + 1];
  int ballDiameter = PApplet.parseInt(sheepW);
  PVector rand = new PVector(500, 300);

  animals[0] = new Animal("blackhole", new PVector(screenW/2, screenH/2));
  drag = 0;
  //for (int i=1; i<numberSheep + 1; i++) {
  //  rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  do {
  //    rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  } while ((rand.x <550 & rand.x >450 ) | (rand.y <350 & rand.y >250 ));

  //  animals[i] = new Animal("sheep", rand);
  //}

  //for (int i=numberSheep + 1; i<numberSheep + numberWolf + 1; i++) {
  //  rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  do {
  //    rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  } while ((rand.x <550 & rand.x >450 ) | (rand.y <350 & rand.y >250 ));

  //  animals[i] = new Animal("wolf", rand);
  //}
  animals[1] = new Animal("sheep", new PVector(screenW/4, screenH/4));
  animals[2] = new Animal("sheep", new PVector(3*screenW/4, screenH/4));
  animals[3] = new Animal("sheep", new PVector(screenW/4, 3*screenH/4));
  animals[4] = new Animal("sheep", new PVector(3*screenW/4, 3*screenH/4));
  animals[5] = new Animal("wolf", new PVector(2*screenW/4, 3*screenH/4));
  foods[0] = new Food("hayBale", new PVector(screenW/6, screenH/6));
  foods[1] = new Food("hayBale", new PVector(5*screenW/6, screenH/6));
  foods[2] = new Food("hayBale", new PVector(screenW/6, 5*screenH/6));
  foods[3] = new Food("hayBale", new PVector(5*screenW/6, 5*screenH/6));
  Screen screen  = new Screen(0, animals, foods, new Text[0], new Button[0], 1, 4,0,0,0,0);
  return screen;
}
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

  public int go() {
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
      if (index == 0 && press == false && ((dragx < -25 && dragx > -screenW/2)||(dragx < -screenW-25 && dragx > -1.5f*screenW)||(dragx < -2*screenW-25 && dragx > -2.5f*screenW))){
        acc = 20;
      } else if (index == 0 && press == false && ((dragx < -screenW/2 && dragx > -screenW+25)||(dragx < -1.5f*screenW && dragx > -2*screenW+25)||(dragx < -2.5f*screenW && dragx > -3*screenW+25))) {
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

      text[1] = new Text("Your score is " + score, screenW/2, screenH/2.25f);
    }
    if (type == 3) {
      
      text[1] = new Text("Your score is " + score, screenW/2, screenH/2.25f);
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

  public int button(float x, float y) {
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
class Text {
  String text;
  float x;
  float y;

  Text(String t, float xcoord, float ycoord) {
    text = t;
    x = xcoord;
    y = ycoord;
  }

  public void update() {
    fill(0);
    text(text, x, y);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "UI" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
