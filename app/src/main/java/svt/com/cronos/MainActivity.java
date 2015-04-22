package svt.com.cronos;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;
    TextView entrada, lblHoras,lblMinutos,lblSegundos;
    Thread hilo,countDown;
    Button fijar;
    Date fechaEntrada;
    int hora = 9;
    int minuto = 0;
    int segundo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        fijar = (Button)findViewById(R.id.fijarBtn);
        fijar.setOnClickListener(this);
        entrada = (TextView)findViewById(R.id.entradaTxt);
        lblHoras = (TextView)findViewById(R.id.hora);
        lblMinutos = (TextView)findViewById(R.id.minuto);
        lblSegundos = (TextView)findViewById(R.id.segundos);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        entrada.setText(sdf.format(new Date()));
        Runnable myRunnableThread = new CountDownRunner();
        hilo= new Thread(myRunnableThread);
        myRunnableThread = new CountDown();
        countDown = new Thread(myRunnableThread);
        hilo.start();

    }


    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = hours + ":" + minutes + ":" + seconds;
                    entrada.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }
    public void count() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    segundo--;
                    if(segundo<0) {
                        minuto--;
                        segundo = 59;
                    }
                    if(minuto<0) {
                        minuto = 59;
                        hora--;
                    }
                    if(hora<0){
                        soundAlarm();
                    }
                    lblHoras.setText(""+hora);
                    lblMinutos.setText(""+minuto);
                    lblSegundos.setText(""+segundo);

                }catch (Exception e) {}
            }
        });
    }

    public void soundAlarm(){
        System.out.println("se acabo el timer.. mostrar notificacion");
        countDown.interrupt();
    }
//3333 4667 6667
    @Override
    public void onClick(View v) {
        if(v.getId()==fijar.getId()){
            fechaEntrada = new Date();
            lblHoras.setText(""+hora);
            lblMinutos.setText(""+minuto);
            lblSegundos.setText(""+segundo);
            hilo.interrupt();
            countDown.start();
        }
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
    class CountDown implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    count();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Hey you just hit the " + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.navigate) {
            startActivity(new Intent(this, SecondActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }




}
