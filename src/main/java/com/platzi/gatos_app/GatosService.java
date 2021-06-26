/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.platzi.gatos_app;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

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
            
            String menu = "Opciones: \n"
                    + " 1. Ver otra imagen \n"
                    + " 2. Marcar como favorito \n"
                    + " 3. Volver al menu \n";
            
            String [] botones = {"Ver otra imagen", "Favorito", "Volver"};
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(
                    null, 
                    menu, 
                    id_gato, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    fondoGato, 
                    botones, 
                    botones[0]
            );
            
            int seleccion = -1;
            // Validamos que opcion selecciona el usuario
            for(int i = 0; i < botones.length; i++) {
                if(opcion.equals(botones[i])) {
                    seleccion = i;
                }
            }
            
            switch(seleccion) {
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }
            
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public static void favoritoGato(Gatos gato) {
        try {
            
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"image_id\": \"" + gato.getId() + "\"\n}");
            Request request = new Request.Builder()
            .url("https://api.thecatapi.com/v1/favourites")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", gato.getApikey())
            .build();
            Response response = client.newCall(request).execute();
            
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public static void verFavorito(String apikey) throws IOException {
        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
        .url("https://api.thecatapi.com/v1/favourites")
        .method("GET", null)
        .addHeader("x-api-key", apikey)
        .build();
        
        Response response = client.newCall(request).execute();
        
        // Guardando el String con la respuesta
        String elJson = response.body().string();
        
        // Creando el objeto gson
        Gson gson = new Gson();
        
        GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class);
        
        if(gatosArray.length > 0) {
            int min = 1;
            int max = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max - min) + 1)) + min;
            int indice = aleatorio - 1;
            
            GatosFav gatofav = gatosArray[indice];
            
            Image image = null;
            try {
                URL url = new URL(gatofav.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon fondoGato = new ImageIcon(image);

                if(fondoGato.getIconWidth() > 800) {
                    // Rediemnsionando
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }

                String menu = "Opciones: \n"
                        + " 1. Ver otra imagen \n"
                        + " 2. Eliminar favorito \n"
                        + " 3. Volver al menu \n";

                String [] botones = {"Ver otra imagen", "Eliminar favorito", "Volver"};
                String id_gato = gatofav.getId();
                String opcion = (String) JOptionPane.showInputDialog(
                        null, 
                        menu, 
                        id_gato, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        fondoGato, 
                        botones, 
                        botones[0]
                );

                int seleccion = -1;
                // Validamos que opcion selecciona el usuario
                for(int i = 0; i < botones.length; i++) {
                    if(opcion.equals(botones[i])) {
                        seleccion = i;
                    }
                }

                switch(seleccion) {
                    case 0:
                        verFavorito(apikey);
                        break;
                    case 1:
                        borrarFavorito(gatofav);
                        break;
                    default:
                        break;
                }

            } catch(IOException e) {
                System.out.println(e);
            }
        }
    }
    
    public static void borrarFavorito(GatosFav gatofav) {
    
    }
}
