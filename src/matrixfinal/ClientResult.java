/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matrixfinal;

/**
 *
 * @author User
 */
import java.io.Serializable;

public class ClientResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String clientId;
    private int[][] result;
    private int startRow;
    private int endRow;

    public ClientResult(int[][] result, int startRow, int endRow) {
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    public String getClientId() {
        return clientId;
    }

    public int[][] getResult() {
        return result;
    }
}
