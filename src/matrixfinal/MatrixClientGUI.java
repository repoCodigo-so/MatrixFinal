/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matrixfinal;

/**
 *
 * @author User
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class MatrixClientGUI extends JFrame {
    private MatrixOperation matrixOperation;
    private JTextField resultField;

    public MatrixClientGUI(MatrixOperation matrixOperation) {
        this.matrixOperation = matrixOperation;

        setTitle("Matrix Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        initUI();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JButton btnGenerateMatrix = new JButton("Generar Matrices Aleatorias");
        JButton btnParallel = new JButton("Multiplicar en Paralelo");
        JButton btnExit = new JButton("Salir");

        resultField = new JTextField();
        resultField.setEditable(false);

        btnGenerateMatrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRandomMatrices();
            }
        });

        btnParallel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performMatrixMultiplication();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(btnGenerateMatrix);
        panel.add(btnParallel);
        panel.add(resultField);
        panel.add(btnExit);

        add(panel);
    }

    private void generateRandomMatrices() {
        try {
            // Solicitar al usuario el tamaño de la matriz
            int size = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el tamaño de la matriz"));

            // Generar matrices aleatorias
            int[][] matrixA = generateRandomMatrix(size, size);
            int[][] matrixB = generateRandomMatrix(size, size);

            // Mostrar las matrices generadas (opcional)
            System.out.println("Matriz A:");
            printMatrix(matrixA);
            System.out.println("Matriz B:");
            printMatrix(matrixB);

            // Mostrar mensaje (opcional)
            JOptionPane.showMessageDialog(this, "Matrices generadas aleatoriamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para el tamaño de la matriz.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int[][] generateRandomMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (int) (Math.random() * 10); // Valores aleatorios entre 0 y 9
            }
        }
        return matrix;
    }

    private void performMatrixMultiplication() {
        try {
            // Obtener el tamaño de la matriz del usuario (puedes adaptarlo según tus necesidades)
            int size = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el tamaño de la matriz"));

            // Generar matrices aleatorias
            int[][] matrixA = generateRandomMatrix(size, size);
            int[][] matrixB = generateRandomMatrix(size, size);

            // Cada cliente genera un identificador único (puede ser su dirección IP, nombre, etc.)
            String clientId = "Client1"; // Cambiar por el identificador único del cliente

            // Enviar la solicitud al servidor para realizar la multiplicación en paralelo
            ClientResult result = matrixOperation.divideAndMultiplyMatricesParallel(matrixA, matrixB, 0, matrixA.length, 1);

            // Mostrar el resultado parcial (opcional)
            displayPartialResult(result.getResult());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para el tamaño de la matriz.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayPartialResult(int[][] result) {
        // Lógica para mostrar el resultado parcial, puedes adaptar según tus necesidades
        System.out.println("Resultado parcial:");
        printMatrix(result);
    }

    private void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        MatrixOperation matrixOperation = obtenerReferenciaRemotaDelServidor();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MatrixClientGUI(matrixOperation);
            }
        });
    }

    private static MatrixOperation obtenerReferenciaRemotaDelServidor() {
        try {
            return (MatrixOperation) Naming.lookup("rmi://localhost:1099/MatrixOperation");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
