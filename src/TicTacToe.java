import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class TicTacToe extends JFrame {
    private final JButton[][] buttons;
    private final int size = 3;// двумерный массив кнопок
    private final int[][] cells = new int[size][size];

    // размерность двумерного массива


    //конструктор с параметрами
    public TicTacToe(String winTitle, String path, int w, int h) {
        super(winTitle); // конструктор суперкласса
        try {
            // добавляем иконку, если ее файл найден по заданному пути
            ImageIcon iconFile = new ImageIcon(Objects.requireNonNull(TicTacToe.class.getResource(path)));
            setIconImage(iconFile.getImage());
        } catch (NullPointerException e) {
            System.out.println("Problem is: " + e);
        }

        setSize(w, h); // размеры окна

        // кнопочная панель
        JPanel buttonsPanel = new JPanel() {
            // Переопределяем прорисовку кнопок созданной панели
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);  //вызов конструктора базового класса(необязательно)
                // помтрочный перебор кнопок
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        if (cells[row][col] == 1) {  // если значение cells[row][col] еденица
                            buttons[row][col].setText("X"); // то на кнопке будет "X"
                        } else if (cells[row][col] == 2) {  // если значение cells[row][col] двойка
                            buttons[row][col].setText("O"); // то на кнопке будет "X"
                        } else {
                            buttons[row][col].setText(""); // иначе надписи нет
                        }
                    }
                }
            }
        };

        // массив кнопок 3*3
        buttons = new JButton[size][size];
        Font font = new Font("Arial", Font.BOLD, 80); // шрифт для надписи для кнопки

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                //Создаем кнопку и добавляем параметры
                buttons[row][col] = new JButton();
                buttons[row][col].putClientProperty("row", row);// параметр row - строка
                buttons[row][col].putClientProperty("col", col); // параметр col - столбец
                buttons[row][col].setFont(font);// задаем шрифт
                buttons[row][col].setForeground(Color.BLACK); // и цвет

                buttons[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton sourceButton = (JButton) e.getSource(); // получаем ссылку на нажатую кнопку
                        // получаем данные о нажатой кнопке, используя  заданные ранее свойства методом putClientProperty
                        int row = (int) sourceButton.getClientProperty("row"); // значение row
                        int col = (int) sourceButton.getClientProperty("col"); // значение col
                        gameTurns(row, col); // ход игрока и ход компьютера
                        repaint(); //пока никто не выиграл и ходы еще есть, перерисовываем поле
                    }
                });
                buttonsPanel.add(buttons[row][col]); //добавляем кнопку на панель
            }
        }
        //Размещение кнопок на пенели в виде сетки
        buttonsPanel.setLayout(new GridLayout(size, size));
        //Добавляем панель на окно
        getContentPane().add(BorderLayout.CENTER, buttonsPanel);
    }

    // проверка победителя
    boolean checkWin(int winValue) {
        for (int i = 0; i < size; i++) {
            // строки или столбцы
            if ((cells[i][0] == winValue && cells[i][1] == winValue && cells[i][2] == winValue) ||
                    (cells[0][i] == winValue && cells[1][i] == winValue && cells[2][i] == winValue)) {
                return true;
            }
        }
        return (cells[0][0] == winValue && cells[1][1] == winValue && cells[2][2] == winValue) ||
                (cells[0][2] == winValue && cells[1][1] == winValue && cells[2][0] == winValue);
    }

    // проверка на пустоту клетки
    boolean isCellValid(int row, int col) {
        //клетка должна быть пустой
        return (cells[row][col] == 0);
    }

    // проверка на ничью
    boolean isTableFull() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (cells[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // ход компьютера
    void turnAi() {
        // попробуем поставить нолик в центр
        if (isCellValid(1, 1)) {
            cells[1][1] = 2;
        } else {

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if (checkFutureWinAi(row, col)) {
                        cells[row][col] = 2;
                        return;
                    }
                }
            }

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if (checkFutureWin(row, col)) {
                        cells[row][col] = 2;
                        return;
                    }
                }
            }


            // если не удалось, ставим в любую пустую клетку
            int row, col;
            do {
                row = ((int) (Math.random() * size));
                col = ((int) (Math.random() * size));
            } while (!isCellValid(row, col));
            cells[row][col] = 2;
        }
    }



public boolean checkFutureWin(int row, int col) {
    if (cells[row][col] != 0) {
        return false;
    }
    cells[row][col] = 1;
    boolean isWin = checkWin(1);
    cells[row][col] = 0;
    return isWin;
}

    public boolean checkFutureWinAi(int row, int col) {
        if (cells[row][col] != 0) {
            return false;
        }
        cells[row][col] = 2;
        boolean isWin = checkWin(2);
        cells[row][col] = 0;
        return isWin;
    }



void restartGame() {
    for (int row = 0; row < size; row++) {
        for (int col = 0; col < size; col++) {
            cells[row][col] = 0;
        }
    }
    repaint();
}

void gameTurns(int row, int col) {
    // ходить начинает игрок
    if (cells[row][col] < 1) {
        cells[row][col] = 1; // если клетка пуста, то добавится крестик
        // после каждого хода игрока проверяем не победил ли он
        if (checkWin(1)) {
            repaint(); // перерисовка вызовет PaintComponent
            //вывод информации о победителе
            JOptionPane.showMessageDialog(TicTacToe.super.getContentPane(), "Вы выиграли");
            restartGame();
        }
        //если после хода все клетки заполнены
        if (isTableFull()) { //  а робедитель не был выяылен, то ничья
            repaint();
            JOptionPane.showMessageDialog(TicTacToe.super.getContentPane(), "Ничья");
            restartGame();
        }
        turnAi(); // ход компьютера
        // аналогичная проверка не победил ли он
        if (checkWin(2)) {
            repaint();
            JOptionPane.showMessageDialog(TicTacToe.super.getContentPane(), "Компьютер выиграл");
            restartGame();
        }
        if (isTableFull()) {
            repaint();
            JOptionPane.showMessageDialog(TicTacToe.super.getContentPane(), "Ничья");
            restartGame();
        }
    }
}
}
