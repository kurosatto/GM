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
    Button[] titlebuttons = {new Button(2,screenW/3.1, screenH-250, "Level 1"), new Button(3,screenW/3.1+screenW, screenH-250, "Level 2"), new Button(4,screenW/3.1+2*screenW, screenH-250, "Level 3"), new Button(1,screenW/3.1+3*screenW, screenH-250,"Space Mode")};
    Button[] spacebuttons = {new Button(0, 100, 50, "Main Menu")};
    start = new Screen(0, new Animal[0], new Food[0], new Text[0], titlebuttons, 0, 0,-screenW*3,0,0,0);
    screens[0] = start;
    // Screen 1 - Space
    spacemode = new Screen(1, inspace, new Food[0], new Text[0], spacebuttons, 0, 1,int(-.5*screenW),int(-.5*screenH),int(.5*screenW),int(.5*screenH));
    screens[1] = spacemode;
    // Backgrounds
    bgs[0] = bg1;
    bgs[1] = spacebg;

    // Game Levels
    restart();
    
    // Game over Screen
    Text[] compText = {new Text("Game Over", screenW/2, screenH/2.5), new Text("", screenW/2, screenH/2)};
    Button[] compTextPlayAgain = {new Button(0, screenW/2, screenH/2, "Main Menu")};
    gameOver = new Screen(0, new Animal[0], new Food[0], compText, compTextPlayAgain, 3, 5,0,0,0,0);
    screens[5] = gameOver;
    // Another game over Screen
    Text[] gameOverText = {new Text("Game Over", screenW/2, screenH/2.5), new Text("", screenW/2, screenH/2)};
    Button[] playAgain = {new Button(0, screenW/2, screenH/2, "Main Menu")};
    gameOver = new Screen(0, new Animal[0], new Food[0], gameOverText, playAgain, 2, 6,0,0,0,0);
    screens[6] = gameOver;
    
    current = 0;
  }


  void restart() {
    // Restart timer 
    timeBegin = millis();
    // Recreate all levels
    screens[2] = createLevel1();
    screens[3] = createLevel2();
    screens[4] = createLevel3();
    // Update the current level
    current = 2;
  }
  
  void run() {
    // Update function
    current = screens[current].go();
  }


  void pressCheck() {
    if (current != 0 && current != number-1  && current != number-2 && current != 1) {
      screens[current].Animals[0].pressCheck();
    } else if (current == 1);
      for (int i = 0; i < screens[current].Animals.length; i++) {
        screens[current].Animals[i].pressCheck();
      }
  }


  void button(float x, float y) {
    if (current == 0 || current == 1 || current == number-1  || current == number-2) {
      current = screens[current].button(mouseX, mouseY);
    }
  }
}
