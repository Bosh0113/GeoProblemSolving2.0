package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.ogr;

public class shapefileUtil {
    public static DataSource getDataSource(String filePath){
        DataSource dSource = null;
        ogr.RegisterAll();
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        gdal.SetConfigOption("SHAPE_ENCODING", "CP936");
        Driver driver = ogr.GetDriverByName("ESRI shapefile");
        if (driver == null) {
            return dSource;
        }
        dSource = driver.Open(filePath, 1);
        if (dSource == null) {
            return dSource;
        }
        return dSource;
    }
//    public static Layer getLayer(DataSource dSource){
//        Layer layer = null;
//        layer = dSource.GetLayerByIndex(0);
//        if (layer == null) {
//            return layer;
//        }
//        return layer;
//    }
}
