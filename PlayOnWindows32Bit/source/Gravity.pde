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
float spikeW = (screenW * screenH) / 50000, spikeH = 1.2 * spikeW;
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
float timeStep = 0.01;
float mxi, myi;
float dragx, dragy = 0;
float screenx, screeny = 0;

boolean press = false;
boolean isGameOver = false;
color[] colors = new color[numberOfScreens];

UIManager manager;

void settings() {
  screenW = displayWidth;
  screenH = displayHeight;
  fullScreen();
}

void setup() {  

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
  wolf.resize(int(wolfW),int(wolfW));
  angryWolf.resize(int(wolfW),int(wolfW));
  worriedWolf.resize(int(wolfW), int(wolfW));
  sheep.resize(int(sheepW), int(sheepW));
  worriedSheep.resize(int(sheepW), int(sheepW));
  dizzySheep.resize(int(sheepW), int(sheepW));
  rabbit.resize(int(rabbitW),int(rabbitW));
  ears.resize(int(rabbitW/3),int(.75*rabbitW));
  chicken.resize(int(chickenW), int(chickenW));
  worriedChicken.resize(int(chickenW), int(chickenW));
  dizzyChicken.resize(int(chickenW),int(chickenW));
  hayBale.resize(int(hayBaleW), int(hayBaleW));
  feed.resize(int(feedW), int(feedW));
  
  lvl1.resize(screenW-300,screenH-100);
  lvl2.resize(screenW-300,screenH-100);
  lvl3.resize(screenW-300,screenH-100);
  spaceMode.resize(screenW-300,screenH-100);

  
  
  blackhole.resize(int(blackholeW), int(blackholeW));
  imageMode(CENTER);
  shapeMode(CENTER);
  manager = new UIManager(numberOfScreens);
}

void draw() {
  rotation++;
  background(0);
  manager.run();
}


void mousePressed() {
  press = true;
  manager.pressCheck();

  mxi = mouseX - dragx;
  myi = mouseY - dragy;
}

void mouseDragged() {
  if (drag == 0) {
      dragx = mouseX-mxi;
      dragy = mouseY-myi;
   }
}

void mouseReleased() {
  press = false;
  drag = 0;
  manager.button(mouseX, mouseY);
}
