/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matrixfinal;

/**
 *
 * @author User
 */
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

public class MatrixServer {
    private List<ClientResult> clientResults;
    private int totalClients;

    public MatrixServer(int totalClients) {
        this.totalClients = totalClients;
        this.clientResults = new ArrayList<>();

        try {
            MatrixOperation matrixOperation = new MatrixOperationImpl();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/MatrixOperation", matrixOperation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addClientResult(ClientResult result) {
        clientResults.add(result);

        if (clientResults.size() == totalClients) {
            // Todos los clientes han enviado sus resultados, combinar y mostrar el resultado final
            int[][] finalResult = combineResults();
            displayFinalResult(finalResult);

            // Reiniciar la lista de resultados para la próxima multiplicación
            clientResults.clear();
        }
    }

    private int[][] combineResults() {
        // Combinar los resultados de los clientes según la lógica deseada
        // En este ejemplo, simplemente sumamos los resultados, pero puedes implementar tu lógica.
        int[][] combinedResult = new int[0][0];
        for (ClientResult result : clientResults) {
            combinedResult = sumMatrices(combinedResult, result.getResult());
        }
        return combinedResult;
    }

    private void displayFinalResult(int[][] result) {
        // Lógica para mostrar el resultado final, puedes adaptar según tus necesidades
        System.out.println("Resultado final:");
        printMatrix(result);
    }

    private int[][] sumMatrices(int[][] matrixA, int[][] matrixB) {
        // Lógica para sumar dos matrices, puedes adaptar según tus necesidades
        int rows = matrixA.length;
        int cols = matrixA[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }

        return result;
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
        int totalClients = 2; // Definir el número total de clientes
        MatrixServer matrixServer = new MatrixServer(totalClients);
    }
}
