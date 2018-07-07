package org.geotools.tutorial;

import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;

import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;

import com.sun.media.jai.imageioimpl.ImageReadWriteSpi;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        initJAI();
        //System.setProperty(GeoTiffReader.OVERRIDE_CRS_SWITCH, "False");
        File tiffFile = new File("D:\\PugetSound\\ned19_n47x00_w122x00_wa_mounttrainier_2008\\ned19_n47x00_w122x00_wa_mounttrainier_2008.tif");
        try {
			GeoTiffReader reader = new GeoTiffReader(tiffFile);
			GridCoverage2D grid = reader.read(null);
			RenderedImage ri = grid.getRenderedImage();
			Raster r = ri.getData();
			ColorModel cm = ri.getColorModel();
			SampleModel sm = ri.getSampleModel();
			double[] dArray = new double[1];
			double[] ret = r.getPixel(4000, 4000, dArray);
			ret = null;
		} catch (DataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    protected static void initJAI() {
        // See [URL]http://docs.oracle.com/cd/E17802_01/products/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/OperationRegistry.html[/URL]
        OperationRegistry registry = JAI.getDefaultInstance().getOperationRegistry();
        if( registry == null) {
            //log.warning("Error with JAI initialization (needed for GeoTools).");
        } else {
            try {
                new ImageReadWriteSpi().updateRegistry(registry);
            } catch(IllegalArgumentException e) {
                // Probably indicates it was already registered.
            }
        }
    }
}
