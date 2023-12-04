/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matrixfinal;

/**
 *
 * @author User
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatrixOperation extends Remote {
    int[][] multiplyMatricesSequential(int[][] matrixA, int[][] matrixB) throws RemoteException;
    int[][] multiplyMatricesConcurrent(int[][] matrixA, int[][] matrixB) throws RemoteException;
    int[][] multiplyMatricesParallel(int[][] matrixA, int[][] matrixB) throws RemoteException;
    void setMatrixSize(int size) throws RemoteException;
    int[][] divideAndMultiplyMatricesSequential(int[][] matrixA, int[][] matrixB, int startRow, int endRow) throws RemoteException;
    int[][] divideAndMultiplyMatricesParallel(int[][] matrixA, int[][] matrixB, int startRow, int endRow) throws RemoteException;
    ClientResult divideAndMultiplyMatricesParallel(int[][] matrixA, int[][] matrixB, int startRow, int endRow, int clientId) throws RemoteException;

}
