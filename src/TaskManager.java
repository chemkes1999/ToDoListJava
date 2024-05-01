import javax.swing.*;
import java.io.*;

public class TaskManager {
    private final DefaultListModel<String> tasksModel;
    private final String fileName = "tasks.txt";

    public TaskManager(DefaultListModel<String> tasksModel) {
        this.tasksModel = tasksModel;
        loadTasks();
    }

    public void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < tasksModel.getSize(); i++) {
                writer.println(tasksModel.getElementAt(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasksModel.addElement(line);
            }
        } catch (IOException e) {
            // El archivo no existe o no se puede leer
        }
    }
}
