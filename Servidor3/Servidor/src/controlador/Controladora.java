/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dominio.Archivo;
import dominio.Servidor;
import dominio.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import persistencia.ArchivoXml;
import persistencia.ServidorXml;
import persistencia.UsuarioXml;
import servidor.HiloC;
/**
 *
 * @author alber
 */
public class Controladora {
    ArchivoXml datosarchivos = new ArchivoXml();
    ServidorXml datosservidor = new ServidorXml();
    
    
    public int consultarUsuario(UsuarioXml datos, String username, String password){
        Usuario users = datos.buscarUsuario(username, password);
            if (users == null) {
                return 0;
            }
            else{
               return 1;
            }
    }
    
    public int crearArchivo(String username, String file, int idServer){
        String directorio = username;
        File folder = new File("../Servidor/almacenamiento/"+directorio);
        folder.mkdir();
        int ultid;
        try{
            java.util.Date fecha = new Date();
            DateFormat fechaconvertida = new SimpleDateFormat("dd-MM-yyy HH;mm;ss");
            String fechastring = fechaconvertida.format(fecha);
            ultid = datosarchivos.buscarId();
            Archivo archivo = new Archivo(ultid+1,idServer, file, "../Servidor/almacenamiento/"+directorio+"/"+fechastring+"_"+file, fechastring, username);
            boolean resultado = datosarchivos.agregarArchivo(archivo);
            Files.move(Paths.get("../Servidor/"+file), Paths.get("../Servidor/almacenamiento/"+directorio+"/"+fechastring+"_"+file), StandardCopyOption.REPLACE_EXISTING);
            return 1;
        } catch (IOException ex) {
            Logger.getLogger(HiloC.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }    
    }
    
    public int buscarArchivo(){
        int ultid;
        try{
            ultid = datosarchivos.buscarId();
            return ultid;
        } catch (Exception e){
            System.out.println("Error buscarArchivo " + e);
            return 0;
        }
     }
    
    public String buscarPathArchivo(int id){
        String path;
        try{
        path = datosarchivos.buscarArchivoId(id);
            //System.out.println(path);
        } catch (Exception e){
            System.out.println("Error en buscarPathArchivo "+ e);
            return "";
        }
         
        return path;
    }
    
    public ArrayList<Archivo> buscarTodosArchivos(String user){
        ArrayList<Archivo> archivo;
        try{
            archivo = datosarchivos.todosLosArchivos(user);
            return archivo;
        } catch (Exception e){
            System.out.println("Error buscarArchivo " + e);
            return null;
        }    
    }
    
    public ArrayList<Archivo> buscarUltimosArchivos(String user){
        ArrayList<Archivo> archivo;
        try{
            archivo = datosarchivos.ultimosArchivos(user);
            return archivo;
        } catch (Exception e){
            System.out.println("Error buscarArchivo " + e);
            return null;
        }    
    }
    
    public String buscarArchivoSeleccionado(String user, String name, String date){
        String path;
        try{
            path = datosarchivos.buscarArchivoSeleccionado(user, name, date);
            return path;
        } catch (Exception e){
            System.out.println("Error buscarArchivo " + e);
            return "";
        }
    }
    
    public boolean setearEstado1(int id){
        Servidor servidor;
        try{
            servidor = datosservidor.buscarServidor(id);
            Servidor aux = new Servidor(servidor.getId(), servidor.getPort(), 1, servidor.getRol(), servidor.getIp());
            datosservidor.actualizarServidor(aux, id);
            return true;
        } catch (Exception e){
            System.out.println("Error al setear estado 1. "+ e);
            return false;
        }
    }
    
    public boolean setearEstado0(int id){
        Servidor servidor;
        try{
            servidor = datosservidor.buscarServidor(id);
            Servidor aux = new Servidor(servidor.getId(), servidor.getPort(), 0, servidor.getRol(), servidor.getIp());
            datosservidor.actualizarServidor(aux, id);
            return true;
        } catch (Exception e){
            System.out.println("Error al setear estado 0. "+ e);
            return false;
        }
    }
    
    public int setearRol(){
        int rol = 0;
        ArrayList<Servidor> servidores;
        try{
            servidores = datosservidor.todosLosServidores();
            for (Servidor servidor : servidores) {
                if(servidor.getState()==1){
                    rol = servidor.getRol();
                }
            }
        } catch (Exception e){
            System.out.println("Error al setear rol. "+ e);
        }
        return rol;
    }
    
    public int nuevoRol(int idactual, int id){
        int rol = 0;
        /*int idmenor = 0;
        int idant = 0;
        int idprox = 0;
        int primera = 0;
        Servidor servidor;
        ArrayList<Servidor> servidores = datosservidor.todosLosServidores();
        for(Servidor servidor1 : servidores){
            if(servidor1.getState()==1 && primera==0){
                idmenor = servidor1.getId();
                idant = servidor1.getId();
                primera = 1;
            }
            else if(servidor1.getState()==1 && primera==1){
                idprox = servidor1.getId();
                if(idprox<idant){
                    idmenor = idprox;
                    idant = idprox;
                }
            }
        }
        rol = idmenor;
        System.out.println("Este es el rol nuevo: "+rol);
        servidor = datosservidor.buscarServidor(idmenor);
        Servidor aux = new Servidor(servidor.getId(), servidor.getPort(), servidor.getState(), rol, servidor.getIp());
        datosservidor.actualizarServidor(aux, id);*/
        idactual = idactual + 1;
        rol = idactual;
        Servidor servidor;
        if(idactual==4){
            idactual = 0;
            rol = idactual;
        }
        servidor = datosservidor.buscarServidor(idactual);
        Servidor aux = new Servidor(servidor.getId(), servidor.getPort(), servidor.getState(), rol, servidor.getIp());
        datosservidor.actualizarServidor(aux, idactual);
        return rol;
    }
    
    public ArrayList<Servidor> buscarTodosServidores(){
        ArrayList<Servidor> servidor;
        try{
            servidor = datosservidor.todosLosServidores();
            return servidor;
        } catch (Exception e){
            System.out.println("Error buscarServidor " + e);
            return null;
        }    
    }
    
    public boolean setearEstado(ArrayList<Servidor> servidores){
        Servidor servidor;
        try{
            for (Servidor servidor1 : servidores) {
            servidor = datosservidor.buscarServidor(servidor1.getId());
            //System.out.println("Este es el servidor: "+servidor);
            Servidor aux = new Servidor(servidor.getId(), servidor.getPort(), servidor1.getState(), servidor.getRol(), servidor.getIp());
            datosservidor.actualizarServidor(aux, servidor.getId());
            }
            return true;
        } catch (Exception e){
            System.out.println("Error al setear estado. "+ e);
            return false;
        }
    }
}
