import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;

class CalculatorPanel extends JPanel {
    private JLabel labelDisplay;
    private JPanel panel;
    private BigDecimal result;
    private String lastCommand;
    private boolean start;
    // добавляем кнопки и label в качестве экрана
    public CalculatorPanel() {
        setLayout(new BorderLayout());
        // объявляем значения по умолчанию при старте
        result = BigDecimal.ZERO;
        lastCommand = "=";
        start = true;
        // добавляем дисплей, выравнивание, рамку черного цвета
        labelDisplay = new JLabel("0", SwingConstants.RIGHT);
        labelDisplay.setEnabled(false);
        labelDisplay.setFont(labelDisplay.getFont().deriveFont(60f));
        labelDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
        add(labelDisplay, BorderLayout.NORTH); // прикрепляем к верху
        // добавляем слушателей событий для нажатий кнопок
        ActionListener insert = new InsertAction();
        ActionListener command = new CommandAction();
        // создаем и выводим 4 столбика кнопок и 5 строк
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4));
        addButton("C", command);
        //неактивные кнопки помечаем как не активные
        Button buttonCE = new Button();
            buttonCE.setLabel("CE");
            buttonCE.setEnabled(false);
        panel.add(buttonCE,command);
        addButton("√",command);
        addButton("x^2",command);
        addButton("7", insert);
        addButton("8", insert);
        addButton("9", insert);
        addButton("/", command);
        addButton("4", insert);
        addButton("5", insert);
        addButton("6", insert);
        addButton("*", command);
        addButton("1", insert);
        addButton("2", insert);
        addButton("3", insert);
        addButton("-", command);
        addButton("0", insert);
        addButton(".", insert);
        addButton("=", command);
        addButton("+", command);
        add(panel, BorderLayout.CENTER);
    }

    private void addButton(String label, ActionListener listener) {
        // метод добавления кнопок
        JButton button = new JButton(label);
        button.setFont(button.getFont().deriveFont(20f));
        button.addActionListener(listener);
        panel.add(button);
    }

    private class InsertAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            //метод нажатия кнопок калькулятора
            String input = event.getActionCommand();
            if (start) {
                labelDisplay.setText("");
                start = false;
            }
            labelDisplay.setText(labelDisplay.getText() + input);
        }
    }

    private class CommandAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            // сброс параметров
            if (command.equals("C") == true) {
                labelDisplay.setText("0");
                result = BigDecimal.valueOf(0);
                return;
            }
            // здесь перехватываем команду подсчета корня и Х в степени2
            if ((command.equals("√") == true) || (command.equals("x^2") == true)){
                // так как после нажатия на знак корень нам не надо жать =, присваиваем в lastcommand знак корня
                // для проверки в следующем методе
                lastCommand = command;
                calculate(new BigDecimal(labelDisplay.getText()));
                lastCommand = "=";
                start = true;
            }

            if (!start) {
                // считаем пример
                calculate(new BigDecimal(labelDisplay.getText()));
                lastCommand = command;
                start = true;
            }
        }
    }

    public void calculate(BigDecimal x) {
        // блок вычислений
        if (lastCommand.equals("+")) result = result.add(x);
        if (lastCommand.equals("-")) result = result.subtract(x);
        if (lastCommand.equals("*")) result = result.multiply(x);
        if (lastCommand.equals("/")) result = result.divide(x);
        if (lastCommand.equals("√")) {
            BigDecimal x1 = new BigDecimal(Math.sqrt(x.doubleValue()));
            result = x1;
        }
        if (lastCommand.equals("x^2")) {
            BigDecimal b1 = new BigDecimal(String.valueOf(x));
            BigDecimal x2 = b1.pow(2);
            result = x2;
        }
        if (lastCommand.equals("=")) result = x;
        if (result.compareTo(BigDecimal.ZERO) == 0) {
            result = BigDecimal.ZERO;
        }
        labelDisplay.setText(result.toString());
    }

}
