import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Calendar;
import java.util.Random;

public class Cliente {

    public static void main(String[] args) throws Exception {

        //join no grupo
        Calendar c = Calendar.getInstance();

        int porta = 8080;
        MulticastSocket mSocket = new MulticastSocket(porta);
        InetAddress endereco = InetAddress.getByName("224.0.0.1");
        mSocket.joinGroup(endereco);
        System.out.println("Usuario conectado");
        long horarioCord, diff, horarioAtualizado;

        DatagramSocket uniSocket = new DatagramSocket();
        Random aleatorio = new Random();
        long clientTime = (System.currentTimeMillis()-aleatorio.nextInt(1000000));
        c.setTimeInMillis(clientTime);
        System.out.println("Horário inicial do cliente: "+ c.getTime());

        //e fica esperando alguma mensagem do coordenador
        while(true) {
            DatagramPacket pctRecebido, pctEnviado, pctFinal;
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

            //recebe diferença para correcao
            pctFinal = new DatagramPacket(new byte[1024], 1024);
            uniSocket.receive(pctFinal);
            diff = Long.parseLong(new String(pctFinal.getData(),pctFinal.getOffset(),pctFinal.getLength()));
            System.out.println("diferenca: " + diff);
            horarioAtualizado = clientTime - (-1*diff);
            c.setTimeInMillis(horarioAtualizado);
            System.out.println("Horário atualizado do cliente: " + c.getTime());

            break;

        }

        //long horario = System.currentTimeMillis();
        //Calendar c = Calendar.getInstance();
        //c.setTimeInMilis(horario);
        //sout(c.getTime());


    }

}
