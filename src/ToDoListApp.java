import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ToDoListApp extends JFrame {
    private final DefaultListModel<String> tasksModel = new DefaultListModel<>();
    private final JTextField taskInput = new JTextField();
    private final JList<String> taskDisplay = new JList<>(tasksModel);
    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private final JSpinner timeSpinner = new JSpinner(new SpinnerListModel(getHourList()));

    public ToDoListApp() {
        setTitle("To-Do List App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(1, 4));
        inputPanel.setBackground(new Color(219, 235, 255, 171)); // Amarillo suave
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(taskInput);
        inputPanel.add(dateSpinner);
        inputPanel.add(timeSpinner);
        inputPanel.add(createButton("Agregar", this::addTask));

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskDisplay), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(219, 235, 255, 171)); // Amarillo suave
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(createButton("Eliminar tarea", this::deleteTask));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setBackground(new Color(219, 235, 255, 171)); // Naranja brillante
        button.setForeground(Color.BLACK); // Texto blanco
        button.setFocusPainted(false); // Eliminar el efecto de foco
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Espacio interior
        return button;
    }

    private void addTask(ActionEvent e) {
        String task = taskInput.getText().trim();
        if (!task.isEmpty()) {
            String dateTime = new SimpleDateFormat("dd/MM/yyyy").format(dateSpinner.getValue()) + " " + timeSpinner.getValue();
            tasksModel.addElement(task + " - " + dateTime);
            taskInput.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Favor de ingresar la tarea", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask(ActionEvent e) {
        int selectedIndex = taskDisplay.getSelectedIndex();
        if (selectedIndex >= 0) {
            tasksModel.remove(selectedIndex);
        }
    }

    private ArrayList<String> getHourList() {
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d:00", i));
        }
        return hours;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Look and feel del sistema operativo
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ToDoListApp().setVisible(true);
        });
    }
}
