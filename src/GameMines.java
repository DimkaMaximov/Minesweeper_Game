import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.concurrent.BlockingDeque;

public class GameMines extends JFrame {
    public static void main(String[] args) {
        new GameMines();
    }

    final String FLAG = "f";
    final int BLOCK_SIZE = 30;
    final int FIELD_SIZE = 9;
    final int MOUSE_BUTTON_LEFT = 1;
    final int MOUSE_BUTTON_RIGHT = 3;
    final int NUMBER_OF_MINES =10;
    final int [] COLORS = {0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x0};
    Cell [][] field = new Cell [FIELD_SIZE][FIELD_SIZE];
    Random random = new Random();
    int countOpenedCells;
    boolean youWon, mineBang;
    int bangX, bangY;

    GameMines (){
        setTitle("Сапер");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(200,200);
        setSize(FIELD_SIZE* BLOCK_SIZE+6, FIELD_SIZE* BLOCK_SIZE+28); // 6 и 28 это рамка окна
        setResizable(false);
        Panel panel = new Panel ();
        panel.setBackground(Color.white);
        panel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int x = e.getX()/BLOCK_SIZE; // убрать?
                int y = e.getY()/BLOCK_SIZE;
                if (e.getButton() == MOUSE_BUTTON_LEFT && !youWon && !mineBang){ // левая кнопка мыши
                    if (field[y][x].isNotOpen()){
                        //openCells(x, y);
                        field[y][x].open();
                        youWon = countOpenedCells == FIELD_SIZE*FIELD_SIZE - NUMBER_OF_MINES; // выигрыш
                        if(mineBang){
                            bangX = x;
                            bangY = y;
                        }
                    }
                }
                if(e.getButton() == MOUSE_BUTTON_RIGHT) field[y][x].inverseFlag(); // правая кнопка мыши
                panel.repaint();
            }
        });
        add(BorderLayout.CENTER, panel);
        setVisible(true);
        initField();
    }

    class Cell{
        void open(){

        }
        boolean isNotOpen(){
            return false;
        }
        void inverseFlag() {

        }
        boolean isMined(){
            return false;
        }
        void mine(){

        }
        void setCountBomb(int coint){

        }
        void paint(Graphics g, int x, int y){

        }
    }
    void initField (){
        int x, y, countMines = 0;
        for (x=0; x< FIELD_SIZE; x++)
            for(y = 0; y < FIELD_SIZE; y++)
                field[y][x] = new Cell();
        while (countMines< NUMBER_OF_MINES){
            do {
                x = random.nextInt(FIELD_SIZE);
                y = random.nextInt(FIELD_SIZE);
            } while (field[y][x].isMined());
            field[y][x].mine();
            countMines++;
        }
        for (x = 0; x< FIELD_SIZE; x++){
            for (y = 0; y< FIELD_SIZE; y++){
                if(!field[y][x].isMined()){
                    int count = 0;
                    for (int dx = -1; dx<2; dx++){
                        for (int dy = -1; dy <2; dy ++) {
                            int nX = x + dx;
                            int nY = y + dy;
                            if (nX < 0 || nY < 0 || nX > FIELD_SIZE - 1 || nY > FIELD_SIZE - 1) {
                                nX = x;
                                nY = y;
                            }
                            count += (field[nY][nX].isMined()) ? 1 : 0;
                        }
                    }
                    field[y][x].setCountBomb(count);
                }
            }
        }
    };

    class Panel extends JPanel {
        @Override
        public void paint (Graphics g){
            super.paint(g);
            for (int x = 0; x < FIELD_SIZE; x++){
                for (int y = 0; y < FIELD_SIZE; y++)
                    field[y][x].paint(g, x, y);
            }
        }
    }

}

