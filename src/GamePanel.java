import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

  /* Configuração CORE da aplicação. */
  static final int SCREEN_WIDTH = 600;
  static final int SCREEN_HEIGHT = 600;
  static final int UNIT_SIZE = 25;
  static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
  static final int DELAY = 75;

  /* Armazenamento das coordenadas do corpo da cobra. */
  final int x[] = new int[GAME_UNITS];
  final int y[] = new int[GAME_UNITS];
  int bodyParts = 6;

  /* Posição das maças na tela e quantidade de maças comidas. */
  int appleEaten;
  int appleX;
  int appleY;

  /* Definição da movimentação da cobra */
  char direction = 'R';
  boolean running = false;

  /* Configuração do tempo de jogo. */
  Timer timer;
  Random random;

  GamePanel() {
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());

    startGame();
  }

  public void startGame() {
    newApple();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {

    if (running) {

      // Definição das linhas visíveis do jogo.
      for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
        g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
      }

      // Cor da maça
      g.setColor(Color.red);
      g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

      for (int i = 0; i < bodyParts; i++) {
        if (i == 0) {
          // Cabeça da cobra.
          g.setColor(Color.green);
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        } else {
          // Corpo da cobra.
          g.setColor(new Color(45, 180, 0));
          // g.setColor(new Color(random.nextInt(255), random.nextInt(255),
          // random.nextInt(255)));
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
      }

      g.setColor(Color.white);
      g.setFont(new Font("Fira Code", Font.BOLD, 40));
      FontMetrics metrics = getFontMetrics(g.getFont());
      g.drawString("Pontuação: " + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Pontuação: " + appleEaten)) / 2,
          g.getFont().getSize());

    } else {
      gameOver(g);
    }

  }

  public void newApple() {
    appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
    appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
  }

  public void move() {
    for (int i = bodyParts; i > 0; i--) {
      x[i] = x[i - 1];
      y[i] = y[i - 1];
    }

    switch (direction) {
      case 'U':
        y[0] = y[0] - UNIT_SIZE; // y[0] = Cabeça da cobra.
        break;

      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;

      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
    }
  }

  public void checkApple() {
    if ((x[0] == appleX) && (y[0] == appleY)) {
      bodyParts++;
      appleEaten++;
      newApple();
    }
  }

  public void checkCollisions() {
    // Analisa se a cabeça bateu no corpo.
    for (int i = bodyParts; i > 0; i--) {
      if ((x[0] == x[i]) && (y[0] == y[i])) {
        running = false;

      }
    }

    // Analisa se a cabeça bateu na borda esquerda.
    if (x[0] < 0) {
      running = false;
    }

    // Analisa se a cabeça bateu na borda direita.
    if (x[0] > SCREEN_WIDTH) {
      running = false;
    }

    // Analisa se a cabeça bateu na parte superior da borda.
    if (y[0] < 0) {
      running = false;
    }

    // Analisa se a cabeça bateu na parte inferior da borda.
    if (y[0] > SCREEN_HEIGHT) {
      running = false;
    }

    if (!running) {
      timer.stop();
    }
  }

  public void gameOver(Graphics g) {
    // Pontuação
    g.setColor(Color.white);
    g.setFont(new Font("Fira Code", Font.BOLD, 40));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Pontuação: " + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("Pontuação: " + appleEaten)) / 2,
        g.getFont().getSize());
    // Texto de GameOver.
    g.setColor(Color.red);
    g.setFont(new Font("Fira Code", Font.BOLD, 75));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("GameOver", (SCREEN_WIDTH - metrics2.stringWidth("GameOver")) / 2, SCREEN_HEIGHT / 2);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      checkApple();
      checkCollisions();
    }

    repaint();
  }

  public class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          if (direction != 'R') {
            direction = 'L';
          }
          break;
        case KeyEvent.VK_RIGHT:
          if (direction != 'L') {
            direction = 'R';
          }
          break;
        case KeyEvent.VK_UP:
          if (direction != 'D') {
            direction = 'U';
          }
          break;
        case KeyEvent.VK_DOWN:
          if (direction != 'U') {
            direction = 'D';
          }
      }
    }
  }
}
