import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Coordenador {

    public static void main(String[] args) throws Exception {

        DatagramSocket uSocket = new DatagramSocket();
        int porta = 8080;
        InetAddress endereco = InetAddress.getByName("224.0.0.1");
        HashMap<Integer, Long> clientes = new HashMap<>();
        long coordenadorTime = System.currentTimeMillis();
        long correcao, sum = 0;

        String msg = Long.toString(coordenadorTime);
        DatagramPacket pct = new DatagramPacket(msg.getBytes(),msg.length(),endereco, porta);
        uSocket.send(pct);
        uSocket.setSoTimeout(5000);
        try{
            while(true){                                  

                DatagramPacket pctRecebido = new DatagramPacket(new byte[1024],1024);
                uSocket.receive(pctRecebido);
                //recebe a diferença entre o horario da maquina e o horario do cliente; adiciona na hash com a porta e a diferença
                String resposta = new String(pctRecebido.getData(),pctRecebido.getOffset(),pctRecebido.getLength());
                clientes.put(pctRecebido.getPort(), Long.parseLong(resposta));


                //uSocket.setSoTimeout(5000 + uSocket.getSoTimeout());
            }
        }catch(Exception e){
            System.out.println("Nao tem ngm conectado");
        }

        for (HashMap.Entry<Integer,Long> pair : clientes.entrySet()) {
            System.out.println(pair.getKey());
            System.out.println(pair.getValue());
             sum =+ pair.getValue();
        }
        correcao = sum/4;



    }
}
