import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ProyectoPong extends JPanel implements KeyListener, Runnable {
   //Variables para el juego
   private static final int Ancho = 640;
   private static final int Alto = 480;
   private static final int Paleta_Ancho = 10;
   private static final int Paleta_Alto = 50;
   private static final int Bola_Tamano = 10;
   private static final int Paleta_Mover = 5;
   private static final int Bola_Mover = 2;
   private static final int Velocidad = 10;
   private boolean juegoCorre;
   private Thread pongHilo;
   private int paleta1Y, paleta2Y, BolaX, BolaY, BolaDX, BolaDY;
   private boolean arribaP1, abajoP1, arribaP2, abajoP2;

   //Declara las variables de la ventana del juego, y permite el uso del teclado en el juego usando KeyListener
   public ProyectoPong() {
      setPreferredSize(new Dimension(Ancho, Alto));
      setFocusable(true);
      addKeyListener(this);
      init();
   }
   //Configuraciones del juego al iniciar (Posicion de paletas y bola)
   private void init() {
      paleta1Y = (Alto - Paleta_Alto) / 2;
      paleta2Y = paleta1Y;
      BolaX = (Ancho - Bola_Tamano) / 2;
      BolaY = (Alto - Bola_Tamano) / 2;
      BolaDX = Bola_Mover;
      BolaDY = Bola_Mover;
      juegoCorre = true;
      pongHilo = new Thread(this);
      pongHilo.start();
   }

   private void update() {
      // Mover paletas en caso de que el check de tecla presionado sea positivo en el metodo de "keyPressed"
      if (arribaP1 && paleta1Y > 0) {
         paleta1Y -= Paleta_Mover;
      }
      if (abajoP1 && paleta1Y < Alto - Paleta_Alto) {
         paleta1Y += Paleta_Mover;
      }
      if (arribaP2 && paleta2Y > 0) {
         paleta2Y -= Paleta_Mover;
      }
      if (abajoP2 && paleta2Y < Alto - Paleta_Alto) {
         paleta2Y += Paleta_Mover;
      }

      // Mover la bola
      BolaX += BolaDX;
      BolaY += BolaDY;

      // Check de colision de bola contra paredes
      if (BolaY < 0 || BolaY > Alto - Bola_Tamano) {
         BolaDY = -BolaDY;
      }
      if (BolaX < 0) {
         BolaX = (Ancho - Bola_Tamano) / 2;
         BolaY = (Alto - Bola_Tamano) / 2;
         BolaDX = Bola_Mover;
         BolaDY = Bola_Mover;
      }
      if (BolaX > Ancho - Bola_Tamano) {
         BolaX = (Ancho - Bola_Tamano) / 2;
         BolaY = (Alto - Bola_Tamano) / 2;
         BolaDX = -Bola_Mover;
         BolaDY = -Bola_Mover;
      }

      // Check de colision de paletas con bola
      if (BolaX < Paleta_Ancho && BolaY + Bola_Tamano > paleta1Y && BolaY < paleta1Y + Paleta_Alto) {
         BolaDX = Bola_Mover;
      }
      if (BolaX + Bola_Tamano > Ancho - Paleta_Ancho && BolaY + Bola_Tamano > paleta2Y && BolaY < paleta2Y + Paleta_Alto) {
         BolaDX = -Bola_Mover;
      }
   }

   private void draw(Graphics g) {
      // Crear pantalla negra
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, Ancho, Alto);

      // Crear las paletas
      g.setColor(Color.WHITE);
      g.fillRect(0, paleta1Y, Paleta_Ancho, Paleta_Alto);
      g.fillRect(Ancho - Paleta_Ancho, paleta2Y, Paleta_Ancho, Paleta_Alto);

      // Crear la bola
      g.setColor(Color.WHITE);
      g.fillOval(BolaX, BolaY, Bola_Tamano, Bola_Tamano);
   }
   //Dibuja el fondo y la bola y paletas en la pantalla del juego
   @Override
   public void paintComponent(Graphics g) {
   super.paintComponent(g);
   draw(g);
   }
   //Check de tecla presionada para mover paleta
   @Override
   public void keyPressed(KeyEvent e) {
   int keyCode = e.getKeyCode();
   if (keyCode == KeyEvent.VK_UP) {
   arribaP2 = true;
   }
   if (keyCode == KeyEvent.VK_DOWN) {
   abajoP2 = true;
   }
   if (keyCode == KeyEvent.VK_W) {
   arribaP1 = true;
   }
   if (keyCode == KeyEvent.VK_S) {
   abajoP1 = true;
   }
   }
   //Check de tecla sin presionar para no mover paleta
   @Override
   public void keyReleased(KeyEvent e) {
   int keyCode = e.getKeyCode();
   if (keyCode == KeyEvent.VK_UP) {
   arribaP2 = false;
   }
   if (keyCode == KeyEvent.VK_DOWN) {
   abajoP2 = false;
   }
   if (keyCode == KeyEvent.VK_W) {
   arribaP1 = false;
   }
   if (keyCode == KeyEvent.VK_S) {
   abajoP1 = false;
   }
   }
   // Este método no hace nada, pero se necesita para la interfaz KeyListener.
   @Override
   public void keyTyped(KeyEvent e) {
   }
   //Corre el juego una vez que juegoCorre es "true" hasta que se cierra la ventana del juego. Usando "update();" el codigo hace un update de la posicion de las paletas y la bola
   @Override
   public void run() {
   while (juegoCorre) {
   update();
   repaint();
   try {
   Thread.sleep(Velocidad);
   } catch (InterruptedException e) {
   e.printStackTrace();
   }
   }
   }
   //Abre la ventana del juego
   public static void main(String[] args) {
   JFrame frame = new JFrame("Juego Pong");
   ProyectoPong juego = new ProyectoPong();
   frame.add(juego);
   frame.pack();
   frame.setResizable(false);
   frame.setLocationRelativeTo(null);
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   frame.setVisible(true);
   }
   }
