/*     */ package com.common.gis;
/*     */ 

/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;

import ucar.unidata.geoloc.Bearing;

/*     */ 
/*     */ public class Coordinate
/*     */   implements Serializable
/*     */ {

			public static float distFrom(double lat1, double lon1, double lat, double lon) 
			{
			    double earthRadius = 6371000; //meters
			    double dLat = Math.toRadians(lat-lat1);
			    double dLng = Math.toRadians(lon-lon1);
			    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat)) *
			               Math.sin(dLng/2) * Math.sin(dLng/2);
			    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
			    float dist = (float) (earthRadius * c);
			
			    return dist;
			    }

 		}
