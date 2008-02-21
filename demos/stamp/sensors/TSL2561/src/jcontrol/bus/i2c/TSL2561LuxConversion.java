package jcontrol.bus.i2c;

/**
 * This class converts the raw sensor data into a lux value.  
 * 
 * ****************************************************************************
 * 
 * 	 Copyright E 2004âˆ’2005 TAOS, Inc.
 * 
 * 	 THIS CODE AND INFORMATION IS PROVIDED â€�AS ISâ€� WITHOUT WARRANTY OF ANY
 * 	 KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * 	 IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR
 * 	 PURPOSE.
 *	     Module Name:
 *	            lux.cpp
 *	****************************************************************************
 * 
 * @author adopted by roebbenack
 */
public class TSL2561LuxConversion {

		
		private static final char LUX_SCALE			= 14;     // scale by 2^14
		private static final char RATIO_SCALE 		= 9;       // scale ratio by 2^9

		//	âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
		//	 Integration time scaling factors
		//	âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
		private static final char CH_SCALE            = 10;     // scale channel values by 2^10
		private static final char CHSCALE_TINT0       = 0x7517; // 322/11 * 2^CH_SCALE
		private static final char CHSCALE_TINT1       = 0x0fe7; // 322/81 * 2^CH_SCALE

		//	âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
		//	 T Package coefficients
		//	âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
		//	 For Ch1/Ch0=0.00 to 0.50
		//	            Lux/Ch0=0.0304âˆ’0.062*((Ch1/Ch0)^1.4)
		//	            piecewise approximation
		//	                   For Ch1/Ch0=0.00 to 0.125:
		//	                          Lux/Ch0=0.0304âˆ’0.0272*(Ch1/Ch0)
		//
		//	                   For Ch1/Ch0=0.125 to 0.250:
		//	                          Lux/Ch0=0.0325âˆ’0.0440*(Ch1/Ch0)
		//
		//	                   For Ch1/Ch0=0.250 to 0.375:
		//	                          Lux/Ch0=0.0351âˆ’0.0544*(Ch1/Ch0)
		//
		//	                   For Ch1/Ch0=0.375 to 0.50:
		//	                          Lux/Ch0=0.0381âˆ’0.0624*(Ch1/Ch0)
		//
		//	 For Ch1/Ch0=0.50 to 0.61:
		//	            Lux/Ch0=0.0224âˆ’0.031*(Ch1/Ch0)
		//
		//	 For Ch1/Ch0=0.61 to 0.80:
		//	            Lux/Ch0=0.0128âˆ’0.0153*(Ch1/Ch0)
		//
		//	 For Ch1/Ch0=0.80 to 1.30:
		//	            Lux/Ch0=0.00146âˆ’0.00112*(Ch1/Ch0)
		//
		//	 For Ch1/Ch0>1.3:
		//	            Lux/Ch0=0
		//	âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
		private static final char K1T = 0x0040;          // 0.125 * 2^RATIO_SCALE
		private static final char B1T = 0x01f2;          // 0.0304 * 2^LUX_SCALE
		private static final char M1T = 0x01be;          // 0.0272 * 2^LUX_SCALE
		private static final char K2T = 0x0080;        // 0.250 *

		private static final char B2T = 0x0214;        // 0.0325 * 2^LUX_SCALE
		private static final char M2T = 0x02d1;        // 0.0440 * 2^LUX_SCALE
		private static final char K3T = 0x00c0;       // 0.375 * 2^RATIO_SCALE
		private static final char B3T = 0x023f;        // 0.0351 * 2^LUX_SCALE
		private static final char M3T = 0x037b;        // 0.0544 * 2^LUX_SCALE
		private static final char K4T = 0x0100;        // 0.50 * 2^RATIO_SCALE
		private static final char B4T = 0x0270;        // 0.0381 * 2^LUX_SCALE
		private static final char M4T = 0x03fe;        // 0.0624 * 2^LUX_SCALE
		private static final char K5T = 0x0138;        // 0.61 * 2^RATIO_SCALE
		private static final char B5T = 0x016f;        // 0.0224 * 2^LUX_SCALE
		private static final char M5T = 0x01fc;       // 0.0310 * 2^LUX_SCALE
		private static final char K6T = 0x019a;        // 0.80 * 2^RATIO_SCALE
		private static final char B6T = 0x00d2;        // 0.0128 * 2^LUX_SCALE
		private static final char M6T = 0x00fb;        // 0.0153 * 2^LUX_SCALE
		private static final char K7T = 0x029a;        // 1.3 * 2^RATIO_SCALE
		private static final char B7T = 0x0018;        // 0.00146 * 2^LUX_SCALE
		private static final char M7T = 0x0012;        // 0.00112 * 2^LUX_SCALE
		private static final char K8T = 0x029a;        // 1.3 * 2^RATIO_SCALE
		private static final char B8T = 0x0000;        // 0.000 * 2^LUX_SCALE
		private static final char M8T = 0x0000;        // 0.000 * 2^LUX_SCALE
		
		//	âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
		//	 CS package coefficients
		//	âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
		//	 For 0 <= Ch1/Ch0 <= 0.52
		//	            Lux/Ch0 = 0.0315âˆ’0.0593*((Ch1/Ch0)^1.4)
		//	            piecewise approximation
		//	                   For 0 <= Ch1/Ch0 <= 0.13
		//	                          Lux/Ch0 = 0.0315âˆ’0.0262*(Ch1/Ch0)
		//	                   For 0.13 <= Ch1/Ch0 <= 0.26
		//	                          Lux/Ch0 = 0.0337âˆ’0.0430*(Ch1/Ch0)
		//	                   For 0.26 <= Ch1/Ch0 <= 0.39
		//	                          Lux/Ch0 = 0.0363âˆ’0.0529*(Ch1/Ch0)
		//	                   For 0.39 <= Ch1/Ch0 <= 0.52
		//	                          Lux/Ch0 = 0.0392âˆ’0.0605*(Ch1/Ch0)
		//	 For 0.52 < Ch1/Ch0 <= 0.65
		//	     Lux/Ch0 = 0.0229âˆ’0.0291*(Ch1/Ch0)
		//	 For 0.65 < Ch1/Ch0 <= 0.80
		//	     Lux/Ch0 = 0.00157âˆ’0.00180*(Ch1/Ch0)
		//	 For 0.80 < Ch1/Ch0 <= 1.30
		//	     Lux/Ch0 = 0.00338âˆ’0.00260*(Ch1/Ch0)
		//	 For Ch1/Ch0 > 1.30
		//	     Lux = 0
		//	âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
		private static final char K1C = 0x0043; // 0.130 * 2^RATIO_SCALE
		private static final char B1C = 0x0204; // 0.0315 * 2^LUX_SCALE
		private static final char M1C = 0x01ad; // 0.0262 * 2^LUX_SCALE
		private static final char K2C = 0x0085; // 0.260 * 2^RATIO_SCALE
		private static final char B2C = 0x0228; // 0.0337 * 2^LUX_SCALE
		private static final char M2C = 0x02c1; // 0.0430 * 2^LUX_SCALE
		private static final char K3C = 0x00c8; // 0.390 * 2^RATIO_SCALE
		private static final char B3C = 0x0253; // 0.0363 * 2^LUX_SCALE
		private static final char M3C = 0x0363; // 0.0529 *

		private static final char K4C = 0x010a; // 0.520 * 2^RATIO_SCALE
		private static final char B4C = 0x0282; // 0.0392 * 2^LUX_SCALE
		private static final char M4C = 0x03df; // 0.0605 * 2^LUX_SCALE
		private static final char K5C = 0x014d; // 0.65 * 2^RATIO_SCALE
		private static final char B5C = 0x0177; // 0.0229 * 2^LUX_SCALE
		private static final char M5C = 0x01dd; // 0.0291 * 2^LUX_SCALE
		private static final char K6C = 0x019a; // 0.80 * 2^RATIO_SCALE
		private static final char B6C = 0x0101; // 0.0157 * 2^LUX_SCALE
		private static final char M6C = 0x0127; // 0.0180 * 2^LUX_SCALE
		private static final char K7C = 0x029a; // 1.3 * 2^RATIO_SCALE
		private static final char B7C = 0x0037; // 0.00338 * 2^LUX_SCALE
		private static final char M7C = 0x002b; // 0.00260 * 2^LUX_SCALE
		private static final char K8C = 0x029a; // 1.3 * 2^RATIO_SCALE
		private static final char B8C = 0x0000; // 0.000 * 2^LUX_SCALE
		private static final char M8C = 0x0000; // 0.000 * 2^LUX_SCALE
		
		//	 lux equation approximation without floating point calculations
		////////////////////////////////////////////////////////////////////////////	//
		//	     Routine:      unsigned int CalculateLux(unsigned int ch0, unsigned int ch0, int iType)
		//
		//	     Description: Calculate the approximate illuminance (lux) given the raw
		//	                   channel values of the TSL2560. The equation if implemented
		//	                   as a pieceâˆ’wise linear approximation.
		//
		//	     Arguments:    boolean isGain16x âˆ’ gain, where false:1X, true:16X
		//	                   unsigned int tInt âˆ’ integration time, where 0:13.7mS, 1:100mS, 2:402mS, 3:Manual
		//	                   unsigned int ch0 âˆ’ raw channel value from channel 0 of TSL2560
		//	                   unsigned int ch1 âˆ’ raw channel value from channel 1 of TSL2560
		//	                   boolean isTType âˆ’ package type, where true:T, false:CS
		//
		//	     Return:       unsigned int âˆ’ the approximate illuminance (lux)
		//
		////////////////////////////////////////////////////////////////////////////	//
		
		public static short calculateLux(boolean isGain16x, short tInt, short ch0, short ch1, boolean isTType) {
	       //âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
	       // first, scale the channel values depending on the gain and integration time
	       // 16X, 402mS is nominal.
	       // scale if integration time is NOT 402 msec
	       char chScale /* ,channel1, channel0*/;
	       Int32 int32Channel0 = new Int32((char)0,(char)0); //Int32.shortToInt32( (short)channel0 );
	       Int32 int32Channel1 = new Int32((char)0,(char)0); //Int32.shortToInt32( (short)channel1 );
	       
	       switch (tInt) {
	           case 0:    // 13.7 msec
	                  chScale = CHSCALE_TINT0;
	                  break;
	           case 1:    // 101 msec
	                  chScale = CHSCALE_TINT1;
	                  break;
	           default:   // assume no scaling
	                  chScale = (1 << CH_SCALE);
	           break;
	       }
	       // scale if gain is NOT 16X
	       if (!isGain16x) chScale = (char)(chScale << 4);   // scale 1X to 16X
	       Int32 int32Ch0 = Int32.shortToInt32((short)ch0);
	       Int32 int32Ch1 = Int32.shortToInt32((short)ch1);
	       Int32 int32ChScale = Int32.shortToInt32((short)chScale);
	       Int32 int32Mul0 = Int32.mul16( int32Ch0, int32ChScale);
	       Int32 int32Mul1 = Int32.mul16( int32Ch1, int32ChScale); 
	       int32Channel0 = Int32.shiftRight(int32Mul0, (short)CH_SCALE);
	       int32Channel1 = Int32.shiftRight(int32Mul1, (short)CH_SCALE);

	       // âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’âˆ’
	       // find the ratio of the channel values (Channel1/Channel0)
	       // protect against divide by zero
	       char ratio1 = 0;
	       if (!int32Channel0.isZero()) ratio1 = Int32.div16( Int32.shiftLeft(int32Channel1,(short)(RATIO_SCALE+1)), int32Channel0).low;
	       //if (!int32Channel0.isNull()) ratio1 = (channel1 << (RATIO_SCALE+1)) / channel0;

	       // round the ratio value
	       char ratio = (char)((ratio1 + 1) >>> 1);
	       // is ratio <= eachBreak ?
	       Int32 int32B = new Int32((char)0,(char)0);
	       Int32 int32M = new Int32((char)0,(char)0);
	       
	       if ( isTType ) { // T package
	          if ((ratio >= 0) && (ratio <= K1T)) {int32B.low=B1T; int32M.low=M1T;}
	          else if (ratio <= K2T) {int32B.low=B2T; int32M.low=M2T;}
	          else if (ratio <= K3T) {int32B.low=B3T; int32M.low=M3T;}
	          else if (ratio <= K4T) {int32B.low=B4T; int32M.low=M4T;}
	          else if (ratio <= K5T) {int32B.low=B5T; int32M.low=M5T;}
	          else if (ratio <= K6T) {int32B.low=B6T; int32M.low=M6T;}
	          else if (ratio <= K7T) {int32B.low=B7T; int32M.low=M7T;}
	          else if (ratio > K8T) {int32B.low=B8T; int32M.low=M8T;}
	       } else { // CS package
	          if ((ratio >= 0) && (ratio <= K1C)) {int32B.low=B1C; int32M.low=M1C;}
	          else if (ratio <= K2C) {int32B.low=B2C; int32M.low=M2C;}
	          else if (ratio <= K3C) {int32B.low=B3C; int32M.low=M3C;}
	          else if (ratio <= K4C) {int32B.low=B4C; int32M.low=M4C;}
	          else if (ratio <= K5C) {int32B.low=B5C; int32M.low=M5C;}
	          else if (ratio <= K6C) {int32B.low=B6C; int32M.low=M6C;}
	          else if (ratio <= K7C) {int32B.low=B7C; int32M.low=M7C;}
	          else if (ratio > K8C) {int32B.low=B8C; int32M.low=M8C;}
	       }

			Int32 int32Temp = Int32.sub(Int32.mul16(int32Channel0, int32B), Int32.mul16(int32Channel1, int32M));
			
			// do not allow negative lux value
			if ( int32Temp.isNegative() ) int32Temp.setZero();
			
			// round lsb (2^(LUX_SCALEâˆ’1))
			int32Temp = Int32.add(int32Temp, Int32.shortToInt32( (short) (1 << (LUX_SCALE-1)) ));
			
			// strip off fractional portion
			Int32 int32Lux = Int32.shiftRight(int32Temp, (short)LUX_SCALE);
			
			// all done
			return Int32.int32ToShort(int32Lux);
		}
}

/**
 * calculate 32 bit values by 16 bit integers. the current state of functionality is alpha-ware and 
 * should completed soon.
 * 
 * @author roebbenack
 */
class Int32 {
	
	/**
	 * 2 x 16 bit values of a 32 bit integer  
	 */
	public char high, low;
	
	/**
	 * creates a 32 bit integer
	 * 
	 * @param high
	 * @param low
	 */
	public Int32(char high, char low) {
		this.high = high;
		this.low = low;
	}
	
	/**
	 * multiplication: 16 Bit * 16 Bit = 32 Bit 
	 * 
	 * constraints: at current time - this function only accepts integers with 16 bit value
	 * (high-value of int32 must be 0)
	 * 
	 * @param x
	 * @param y
	 * @return a 32 bit integer
	 * @throws IllegalArgumentException - if the overgiven value of any argument is bigger than 0xffff
	 */
	public static Int32 mul16(Int32 x, Int32 y) throws IllegalArgumentException {
		
		if ( 0 != x.high || 0 != y.high ) { throw new IllegalArgumentException(); }
		
		// get: high- and low-byte of x AND high- and low-byte of y
		char 	hx = (char)(x.low >>> 8), 
		 		lx = (char)(x.low & 0xff), 
         		hy = (char)(y.low >>> 8), 
         		ly = (char)(y.low & 0xff);  
		
		char dummy1, dummy2, hm, lm;  
		
		hm = (char)(hx*hy);
		lm = (char)(lx*ly);
		dummy1 = (char)(hx*ly);
		dummy2 = (char)(hy*lx);
		hm   += (char)(dummy1>>>8)+(char)(dummy2>>>8); // no overflow-check!
		char dummy3 = (char)((dummy1&0xff)<<8);
		if ( isAddOverflow(dummy3, lm) ) { hm++; } // check for overflow!
		lm   += dummy3;
		dummy3 = (char)((dummy2&0xff)<<8);
		if ( isAddOverflow(dummy3, lm) ) { hm++; } // check for overflow! 
		lm   += dummy3;
		
		// build 32-bit integer
		return new Int32(hm, lm);
	}
	
	/**
	 * division: 32 Bit / 16 Bit = 16 Bit
	 * 
	 * @param x
	 * @param divisor
	 * @return a 16 bit wide result of division
	 * @throws IllegalArgumentException - if the overgiven value of divisor is bigger than 0xffff
	 */
	public static Int32 div16(Int32 x, Int32 divisor) throws IllegalArgumentException {
		
		if ( 0 != divisor.high ) { throw new IllegalArgumentException(); }
		
		char remainder = 0, counter = 32, quotient = 0, hm = x.high, lm = x.low;
		while ( counter > 0 ) {
			if ((hm & 0x8000) != 0) {
				remainder = (char) ((remainder << 1) | 0x0001);
			} else {
				remainder = (char) (remainder << 1);
			}
			if ((lm & 0x8000) != 0) {
				hm = (char) ((hm << 1) | 0x0001);
			} else {
				hm = (char) (hm << 1);
			}
			lm = (char) (lm << 1);
			if (remainder >= divisor.low) {
				remainder -= divisor.low;
				quotient = (char) ((quotient << 1) | 0x0001);
			} else {
				quotient = (char) (quotient << 1);
			}
			counter--;
		}
      
		// done!
		return Int32.shortToInt32( (short)quotient );
		
	}

	/**
	 * substraction: x (32 Bit) - y (32 Bit) = return-value (32 Bit)
	 * 
	 * @param x
	 * @param y
	 * @return a 32 bit wide result of substraction
	 */
	public static Int32 sub(Int32 x, Int32 y) {
		Int32 s = new Int32( (char)(x.high-y.high), (char)(x.low-y.low) );
		boolean underflow = y.low > x.low;
		if ( underflow ) { s.high -= 1; }
		return s;
	}

	/**
	 * helper: checks if x + y is bigger than 16 bit 
	 * 
	 * @param x
	 * @param y
	 * @return true - if x + y is bigger than 16 bit
	 */
	private static boolean isAddOverflow(int x, int y) {
		
		boolean overflow = false;
		
		if ( (x & 0x8000) == 0 ) { // upper bit of x is 0!
			if ( (y&0x8000)==0x8000 ) { // upper bit of y is 1!
				if ( (0x8000 & ((y&0x7fff)+x)) == 0x8000 ) { overflow = true; }
			}
		} else { // upper bit of y is 1
			if ( (y&0x8000)==0x8000 ) { // upper bit of y is 1!
				overflow = true;
			} else { // upper bit of x is 0!
				if ( (0x8000 & ((x&0x7fff)+y)) == 0x8000 ) { overflow = true; }
			}
		}
		
		return overflow;
	}
	
	/**
	 * addition: x (32 Bit) + y (32 Bit) = return-value (32 Bit)
	 * 
	 * @param x
	 * @param y
	 * @return  a 32 bit wide result of addition
	 */
	public static Int32 add(Int32 x, Int32 y) {
		Int32 s = new Int32( (char)(x.high+y.high), (char)(x.low+y.low) );
		boolean overflow = isAddOverflow(y.low, x.low);
		if ( overflow ) { s.high += 1; }
		return s;
	}
	
	/**
	 * checks if the overgiven value is < 0
	 * 
	 * @param x
	 * @return true if the overgiven value is negative
	 */
	public static boolean isNegative(Int32 x) {
		if ( 0 != (x.high & 0x8000) ) return true;
		return false;
	}
	
	/**
	 * converts an overgiven short into a Int32
	 * 
	 * @param x
	 * @return the converted value
	 */
	public static Int32 shortToInt32(short x) {
		return new Int32( (char)0, (char)x );
	}
	
	/**
	 * converts an overgiven Int32 into a short-value
	 * 
	 * @param x
	 * @return
	 * @throws IllegalArgumentException - if the overgiven value is > 0xffff
	 */
	public static short int32ToShort(Int32 x) throws IllegalArgumentException {
		if ( 0 != x.high ) { throw new IllegalArgumentException(); }
		return (short)x.low;
	}

	/**
	 * shifts the overgiven value 'numBits' to the left
	 * 
	 * @param x
	 * @param numBits
	 * @return the shifted value
	 */
	public static Int32 shiftLeft(Int32 x, short numBits) {
		Int32 result = new Int32((char)(x.high << numBits), (char)0 );
		result.high |= x.low>>>(16-numBits);
		result.low  = (char)(x.low << numBits);
		return result;
	}
	
	/**
	 * shifts the overgiven value 'numBits' to the right
	 * 
	 * @param x
	 * @param numBits
	 * @return the shifted value
	 */
	public static Int32 shiftRight(Int32 x, short numBits) {
		Int32 result = new Int32( (char)0, (char)(x.low >>> numBits) );
		result.low |= (char)( x.high << (16-numBits) );
		result.high = (char)( x.high >>> numBits );
		return result;
	}
	
	/**
	 * sets the current value to 0
	 * 
	 * @return this
	 */
	public Int32 setZero() {
		high = 0;
		low = 0;
		return this;
	}
	
	/**
	 * checks the current value is 0
	 * 
	 * @return true - if the value is 0
	 */
	public boolean isZero() {
		return (low==0 && high==0);
	}
	
	/**
	 * checks the current value is negative
	 * 
	 * @return true if negative
	 */
	public boolean isNegative() {
		return isNegative(this);
	}

	/**
	 * dumps the content of the Int32 value
	 * 
	 * @return the value as string in format: 0x<<high>>.<<low>>
	 */
	public String toString() {
		return "0x".concat(Integer.toString(high)).concat(".").concat(Integer.toHexString(low));
	}
}

