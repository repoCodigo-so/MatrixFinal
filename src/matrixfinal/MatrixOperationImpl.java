/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matrixfinal;

/**
 *
 * @author User
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
// Implementación de la interfaz remota
public class MatrixOperationImpl extends UnicastRemoteObject implements MatrixOperation {
    private static final int BLOCK_SIZE = 16; // Tamaño de bloque más pequeño para la tarea
    private static final int CLIENT_COUNT = 2;
    private int matrixSize;

    protected MatrixOperationImpl() throws RemoteException {
        super();
        this.matrixSize = 3; // Tamaño predeterminado
    }

    @Override
    public int[][] multiplyMatricesSequential(int[][] matrixA, int[][] matrixB) throws RemoteException {
        // Implementación de la multiplicación de matrices de forma secuencial
        int rowA = matrixA.length;
        int colA = matrixA[0].length;
        int colB = matrixB[0].length;
        int[][] result = new int[rowA][colB];
        for (int i = 0; i < rowA; i++) {
            for (int j = 0; j < colB; j++) {
                for (int k = 0; k < colA; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    @Override
    public int[][] multiplyMatricesConcurrent(int[][] matrixA, int[][] matrixB) throws RemoteException {
        // Implementación de la multiplicación de matrices de forma concurrente
        // (Debes proporcionar la implementación específica según tu estrategia de concurrencia)
        return null;
    }

    @Override
    public int[][] multiplyMatricesParallel(int[][] matrixA, int[][] matrixB) throws RemoteException {
        // Implementación de la multiplicación de matrices de forma paralela
        // (Debes proporcionar la implementación específica según tu estrategia de paralelismo)
        return null;
    }
    
    @Override
    public void setMatrixSize(int size) throws RemoteException {
        this.matrixSize = size;
    }
@Override
    public int[][] divideAndMultiplyMatricesSequential(int[][] matrixA, int[][] matrixB, int startRow, int endRow) throws RemoteException {
        int colA = matrixA[0].length;
        int colB = matrixB[0].length;
        int[][] result = new int[endRow - startRow][colB];
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < colB; j++) {
                for (int k = 0; k < colA; k++) {
                    result[i - startRow][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    @Override
    public int[][] divideAndMultiplyMatricesParallel(int[][] matrixA, int[][] matrixB, int startRow, int endRow) throws RemoteException {
        int colA = matrixA[0].length;
        int colB = matrixB[0].length;
        int[][] result = new int[endRow - startRow][colB];
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new MatrixMultiplicationTask(result, matrixA, matrixB, startRow, 0, 0, 0, 0, 0, endRow, colB, colA));
        return result;
    }

    class MatrixMultiplicationTask extends RecursiveTask<Void> {
        private int[][] result;
        private int[][] matrixA;
        private int[][] matrixB;
        private int rowAStart, colAStart, rowBStart, colBStart, rowCStart, colCStart, rowAEnd, colBEnd, colAEnd;

        MatrixMultiplicationTask(int[][] result, int[][] matrixA, int[][] matrixB, int rowAStart, int colAStart, int rowBStart, int colBStart, int rowCStart, int colCStart, int rowAEnd, int colBEnd, int colAEnd) {
            this.result = result;
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.rowAStart = rowAStart;
            this.colAStart = colAStart;
            this.rowBStart = rowBStart;
            this.colBStart = colBStart;
            this.rowCStart = rowCStart;
            this.colCStart = colCStart;
            this.rowAEnd = rowAEnd;
            this.colBEnd = colBEnd;
            this.colAEnd = colAEnd;
        }

        @Override
        protected Void compute() {
            if ((rowAEnd - rowAStart <= BLOCK_SIZE) || (colBEnd - colCStart <= BLOCK_SIZE) || (colAEnd - colAStart <= BLOCK_SIZE)) {
                // Si el tamaño de la tarea es menor o igual al tamaño de bloque, realiza la multiplicación secuencial
                for (int i = rowAStart; i < rowAEnd; i++) {
                    for (int j = colCStart; j < colBEnd; j++) {
                        for (int k = colAStart; k < colAEnd; k++) {
                            result[i - rowAStart][j] += matrixA[i][k] * matrixB[k][j];
                        }
                    }
                }
            } else {
                // Divide la tarea en sub-tareas y las ejecuta de manera concurrente
                int midRowA = (rowAStart + rowAEnd) / 2;
                int midColB = (colCStart + colBEnd) / 2;
                int midColA = (colAStart + colAEnd) / 2;

                invokeAll(
                    new MatrixMultiplicationTask(result, matrixA, matrixB, rowAStart, colAStart, rowBStart, colBStart, rowCStart, colCStart, midRowA, midColB, midColA),
                    new MatrixMultiplicationTask(result, matrixA, matrixB, rowAStart, midColA, rowBStart, midColB, rowCStart, colCStart, midRowA, colBEnd, colAEnd)
                );
            }
            return null;
        }
    }
    
     @Override
    public ClientResult divideAndMultiplyMatricesParallel(int[][] matrixA, int[][] matrixB, int startRow, int endRow, int clientId) throws RemoteException {
        int colA = matrixA[0].length;
        int colB = matrixB[0].length;
        int[][] result = new int[endRow - startRow][colB];

        ForkJoinPool pool = new ForkJoinPool();

        // Divide el trabajo entre los clientes
        int rowsPerClient = (endRow - startRow) / CLIENT_COUNT;
        int clientStartRow = startRow + clientId * rowsPerClient;
        int clientEndRow = clientStartRow + rowsPerClient;

        MatrixMultiplicationTask task = new MatrixMultiplicationTask(result, matrixA, matrixB, clientStartRow, 0, 0, 0, 0, 0, clientEndRow, colB, colA);

        // Invoca la tarea paralela
        ForkJoinTask<Void> taskResult = pool.submit(task);
        taskResult.join(); // Espera a que la tarea paralela haya terminado

        return new ClientResult(result, clientStartRow, clientEndRow);
    }
}