import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main extends JFrame {
    private final DefaultListModel<String> tasksModel = new DefaultListModel<>();
    private final JTextField taskInput = new JTextField();
    private final JList<String> taskDisplay = new JList<>(tasksModel);
    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private final JSpinner timeSpinner = new JSpinner(new SpinnerListModel(getHourList()));
    private final TaskManager taskManager = new TaskManager(tasksModel);

    public Main() {
    	System.out.print("Iniciando proyecto de todolist");
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
        inputPanel.add(createButton("Editar", this::editTask)); // Botón para editar tarea

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
            taskManager.saveTasks();
        } else {
            JOptionPane.showMessageDialog(this, "Favor de ingresar la tarea", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editTask(ActionEvent e) {
        int selectedIndex = taskDisplay.getSelectedIndex();
        if (selectedIndex >= 0) {
            String currentTask = tasksModel.getElementAt(selectedIndex);
            String[] parts = currentTask.split(" - ");
            String currentTaskText = parts[0];
            String currentDateTime = parts[1];

            taskInput.setText(currentTaskText);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            try {
                Date currentDate = dateFormat.parse(currentDateTime.substring(0, 10));
                Date currentTime = timeFormat.parse(currentDateTime.substring(11));
                dateSpinner.setValue(currentDate);
                timeSpinner.setValue(new SimpleDateFormat("HH:mm").format(currentTime));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            JPanel editPanel = new JPanel(new GridLayout(0, 2));
            editPanel.add(new JLabel("Tarea:"));
            editPanel.add(taskInput);
            editPanel.add(new JLabel("Fecha (dd/MM/yyyy):"));
            editPanel.add(dateSpinner);
            editPanel.add(new JLabel("Hora (HH:mm):"));
            editPanel.add(timeSpinner);

            int result = JOptionPane.showConfirmDialog(this, editPanel, "Editar Tarea", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String editedTask = taskInput.getText();
                String editedDate = new SimpleDateFormat("dd/MM/yyyy").format(dateSpinner.getValue());
                String editedTime = (String) timeSpinner.getValue();

                if (!editedTask.isEmpty() && !editedDate.isEmpty() && !editedTime.isEmpty()) {
                    String editedDateTime = editedDate + " " + editedTime;
                    tasksModel.set(selectedIndex, editedTask + " - " + editedDateTime);
                    taskManager.saveTasks();
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private void deleteTask(ActionEvent e) {
        int selectedIndex = taskDisplay.getSelectedIndex();
        if (selectedIndex >= 0) {
            tasksModel.remove(selectedIndex);
            taskManager.saveTasks();
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
            new Main().setVisible(true);
        });
    }
}
