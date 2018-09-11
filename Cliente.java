import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;

public class Cliente {

    public static void main(String[] args) throws Exception {

        //join no grupo

        int porta = 8080;
        MulticastSocket mSocket = new MulticastSocket(porta);
        InetAddress endereco = InetAddress.getByName("224.0.0.1");
        mSocket.joinGroup(endereco);
        System.out.println("usuario conectado");
        long horarioCord;

        DatagramSocket uniSocket = new DatagramSocket();
        Random aleatorio = new Random();
        long clientTime = (System.currentTimeMillis()-aleatorio.nextInt(1000000));


        //e fica esperando alguma mensagem do coordenador
        while(true) {
            DatagramPacket pctRecebido, pctEnviado;
            pctRecebido = new DatagramPacket(new byte[1024],1024);
            mSocket.receive(pctRecebido);
            String resposta = new String(pctRecebido.getData(),pctRecebido.getOffset(),pctRecebido.getLength());
            horarioCord  = Long.parseLong(resposta);

            //horario coordenador - horario do cliente
            long diferenca = horarioCord -  clientTime;
            String msg = Long.toString(diferenca);

            //envia a diferença para o coordenador
            pctEnviado = new DatagramPacket(msg.getBytes(), msg.length(), pctRecebido.getAddress(),pctRecebido.getPort());
            uniSocket.send(pctEnviado);



        }
        //long horario = System.currentTimeMillis();
        //Calendar c = Calendar.getInstance();
        //c.setTimeInMilis(horario);
        //sout(c.getTime());


    }

}
