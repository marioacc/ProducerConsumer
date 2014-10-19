import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by mario_000 on 10/18/2014.
 */


public class ProducerConsumer implements Runnable {
    List<String> lista = new ArrayList<String>();//La lista de donde los tacos(el producto) se va a ir tomando
    Semaphore n;
    Semaphore s;
    int produced=0;
    int consumed=0;

    /***tuve que crear un hilo para poder ejecutar los otros dos, sigase esto como un dogma de Fe
     ya que no podia llamar clases NO estaticas desde el main***/
    public static void main (String [] args){

        new Thread( new ProducerConsumer()).start();


    }

    /***Este metodo inicializa los semaphores y corre los hilos***/
    @Override
    public void run() {
        n=new Semaphore(0,true);
        s=new Semaphore(1,true);
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    /***El dios de los tacos empieza a crear tacos para los maistros***/
    class Producer implements Runnable {

        @Override
        public void run() {
            while(true) {
                String nuevo_producto = produce();
                try {
                    s.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                append(nuevo_producto);
                n.release();
                s.release();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

        }

        /***Produce los tacos para los maestros***/
        public String produce(){
            produced++;
            String producto="taco"+Math.random()*100;
            //System.out.println("Tacos producidos: "+produced);

            return producto;
        }

        /***Agrega los tacos a la barra de tacos para que sean tomados por el maistro(consumidor)***/
        public void append(String nuevo_producto){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lista.add(nuevo_producto);
            System.out.println("Tacos en la barra: "+produced);
        }
    }

    class Consumer implements Runnable {

        @Override

        public void run() {
            while(true) {
                try {
                    n.acquire();
                    s.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String producto = take();
                s.release();
                consume(producto);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        /***Este metodo toma los tacos***/
        public String take(){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String producto=lista.remove(0);
            //System.out.println("Se ha tomado un: "+producto);
            return producto;
        }

        /***Este metodo hace que el maistro se nutra se los deliciosos tacos y los quita de la barra de tacos***/
        public void consume(String producto){
            consumed++;
            System.out.println("Tacos consumidos: "+consumed);
        }
    }
}
