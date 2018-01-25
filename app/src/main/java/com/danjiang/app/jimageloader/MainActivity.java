package com.danjiang.app.jimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 *
 *  http://blog.csdn.net/u012416955/article/details/51969828
 */


public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getPic(View v){
        String path = "http://img4.duitang.com/uploads/item/201502/01/20150201163209_NNruz.jpeg";
        //创建一个线程对象
        GetPicThread gpt = new GetPicThread(path, handler);
        Thread t = new Thread(gpt);
        t.start();
    }


    class GetPicThread implements Runnable{
        private String path = null;

        private Handler handler = null;

        public GetPicThread(String path, Handler handler) {
            this.path = path;
            this.handler = handler;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
//            InputStreamReader inputStreamReader = null;
//            BufferedReader bufferedReader = null;
            try {
                //1.定义一个url对象
                URL url = new URL(path);
                //2. 通过一个url获取一个HttpURLConnection对象
                connection = (HttpURLConnection) url.openConnection();
                //3. 设置请求方式
                connection.setRequestMethod("GET");
                //4. 设置请求超时时间
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                //5. 判断响应码 200
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //6.获取到网络的输入的字符流
                    inputStream = connection.getInputStream();
                    //7.将字节流种获取图片数据获取出来
                    // 借助位图工厂类的方法，将流转化为图
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    Message message = handler.obtainMessage();
                    message.what = 0x0001;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
                // 9.释放资源
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(inputStream!= null){
                        inputStream.close();
                    }
                    if(connection!= null){
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
