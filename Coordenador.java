import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Coordenador {

    public static void main(String[] args) throws Exception {

        Calendar c = Calendar.getInstance();

        DatagramSocket uSocket = new DatagramSocket();
        int porta = 8080;
        InetAddress endereco = InetAddress.getByName("224.0.0.1");
        HashMap<Integer, Long> clientes = new HashMap<>();
        long coordenadorTime = System.currentTimeMillis();
        long correcao, sum = 0, corrigido;

        c.setTimeInMillis(coordenadorTime);
        System.out.println("Horario inicial coordenador: " +c.getTime());
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
            System.out.println("Nao tem mais ngm conectado");
        }

        for (HashMap.Entry<Integer,Long> pair : clientes.entrySet()) {
//            System.out.println("porta: " +pair.getKey());
//            System.out.println("valor: " + pair.getValue());
             sum =+ pair.getValue();
        }
//        System.out.println(sum);
        correcao = sum/4; //rodar 3 cli
//        System.out.println("correcao " + correcao);
        corrigido = coordenadorTime - (-1* correcao);

        for (HashMap.Entry<Integer,Long> pair : clientes.entrySet()) {
            pair.setValue(pair.getValue()-(correcao * -1));
//            System.out.println("valor enviado: "+ pair.getValue());
            String aux = Long.toString(pair.getValue());
            DatagramPacket diff = new DatagramPacket(aux.getBytes(),aux.length(),endereco, pair.getKey());
            uSocket.send(diff);
        }

        c.setTimeInMillis(corrigido);

        System.out.println("Novo horario do coordenador é: "+ c.getTime());


    }
}
