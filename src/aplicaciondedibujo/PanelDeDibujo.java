/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplicaciondedibujo;

import figuras.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

import figuras.Rectangulo;
import figuras.Triangulo;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author josearielpereyra
 */
public class PanelDeDibujo extends JPanel implements ActionListener{

    Figura figuraActual;
    ArrayList<Figura> figuras = new ArrayList<>();
    JPanel barraDeHerramientas;
    File archivo;
    
    public void guardar() {
        try {
            if(archivo != null) {
                BufferedImage imagen = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D grafico = imagen.createGraphics();
                this.paint(grafico);
                grafico.dispose();
                
                String ruta = archivo.getPath();
                String extension = ruta.substring(ruta.lastIndexOf(".") + 1);
                ImageIO.write(imagen, extension, archivo);
            }
            else {
                guardarComo();
            }
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void guardarComo() {
        BufferedImage imagen = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D grafico = imagen.createGraphics();
        this.paint(grafico);
        grafico.dispose();

        JFileChooser seleccion = new JFileChooser();
        seleccion.removeChoosableFileFilter(seleccion.getChoosableFileFilters()[0]);
        seleccion.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
        seleccion.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));

        int opcion = seleccion.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            try {
                String ruta = seleccion.getSelectedFile().getPath();
                String extensionSeleccion = seleccion.getFileFilter().getDescription();
                int indice = ruta.lastIndexOf(".");
                String extension = indice < 0 ? "." + extensionSeleccion : ruta.substring(indice);

                if (!extension.equalsIgnoreCase(".jpg") && !extension.equalsIgnoreCase(".png")) {
                    extension = "." + extensionSeleccion;
                    ruta = ruta.substring(0, indice);
                }

                
                archivo = new File(ruta + extension);
                ImageIO.write(imagen, extension.replace(".", ""), archivo);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public PanelDeDibujo() {
        
        barraDeHerramientas = new JPanel();
        barraDeHerramientas.setLayout(new FlowLayout( FlowLayout.LEFT));
        
        JToggleButton botonLinea = new JToggleButton("Linea");
        JToggleButton botonRectangulo = new JToggleButton("Rectangulo");
        JToggleButton botonPoligono = new JToggleButton("Poligono");
        JToggleButton botonPentagono = new JToggleButton("Pentagono");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonGuardarComo = new JButton("Guardar como");
        JButton botonRehacer = new JButton("Rehacer");
        JButton botonDeshacer = new JButton("Deshacer");
        
        barraDeHerramientas.add(botonLinea);
        barraDeHerramientas.add(botonRectangulo);
        barraDeHerramientas.add(botonPoligono);
        barraDeHerramientas.add(botonPentagono);
        barraDeHerramientas.add(botonGuardar);
        barraDeHerramientas.add(botonGuardarComo);
        barraDeHerramientas.add(botonRehacer);
        barraDeHerramientas.add(botonDeshacer);
        
        ButtonGroup grupoBotones = new ButtonGroup();
        grupoBotones.add(botonLinea);
        grupoBotones.add(botonRectangulo);
        grupoBotones.add(botonPoligono);
        grupoBotones.add(botonPentagono);
        
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
                
                repaint();
            }
        });
        
        botonGuardarComo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarComo();
                
                repaint();
            }
        });
        
        addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point puntoActual = e.getPoint();
                
                //decidir la figura que se va a dibujar
                if( botonLinea.isSelected() ) {
                    figuraActual = new Linea( puntoActual );
                }
                else if( botonRectangulo.isSelected() ) {
                    figuraActual = new Rectangulo( puntoActual );
                }
                else if( botonPoligono.isSelected() ) {
                    figuraActual = new Poligono( puntoActual );
                }
                else if( botonPentagono.isSelected() ) {
                    figuraActual = new Pentagono( puntoActual );
                }
                
                
                figuras.add(figuraActual);

                repaint(); 
            }
        });
        
        addMouseMotionListener( new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point puntoActual = e.getPoint();
                figuraActual.actualizar( puntoActual );
                
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Figura figura : figuras) {
            if(figura instanceof FiguraRellenable) {
                ((FiguraRellenable) figura).setFiguraActual(figura == figuraActual);
            }
            
            figura.dibujar(g);
        }
    }
    
    public JPanel getBaraDeHerramientas() {
        return barraDeHerramientas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void addActionLIstener(ActionListener actionListener) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
    
