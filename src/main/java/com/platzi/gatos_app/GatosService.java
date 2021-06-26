/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.platzi.gatos_app;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author diegobarrera
 */
public class GatosService {
    
    public static void verGatos() throws IOException{
        
        // 1. Vamos a traer los datos de la API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("https://api.thecatapi.com/v1/images/search")
            .method("GET", null)
            .build();
        
        Response response = client.newCall(request).execute();
        
        String elJson = response.body().string();
        
        // Cortar los square brackets del JSON
        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length() -1);
        
        // Crear un objeto de la clase Gson
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJson, Gatos.class);
        
        // Ajuste de dimension de imagen
        Image image = null;
        try {
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);
            
            ImageIcon fondoGato = new ImageIcon(image);
            
            if(fondoGato.getIconWidth() > 800) {
                // Rediemnsionando
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }
        } catch(IOException e) {
            System.out.println(e);
        }
    
    }
}
