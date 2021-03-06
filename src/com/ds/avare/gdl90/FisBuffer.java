/*
Copyright (c) 2012, Zubair Khan (governer@gmail.com) 
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
    *
    *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.ds.avare.gdl90;

import java.util.LinkedList;

/**
 * 
 * @author zkhan
 *
 */
public class FisBuffer {

    private int mSize;
    private byte mBuffer[];
    private LinkedList<Product> mProducts;
    
    /**
     * 
     * @param buffer
     * @param offset
     * @param slotId
     * @param fisbId
     * @param pvalid
     * @param lat
     * @param lon
     */
    public FisBuffer(byte buffer[], int offset, int slotId, int fisbId, boolean pvalid, float lat, float lon) {
        mSize = 424;
        mBuffer = buffer;
        mProducts = new LinkedList<Product>();
        mBuffer = new byte[mSize];
        System.arraycopy(buffer, offset, mBuffer, 0, mSize);
    }
    
    /**
     * Parse products out of the Fis
     */
    public void makeProducts() {
        int i = 0;
        while(i < mSize) {
            
            int iFrameLength = (((int)mBuffer[i]) & 0xFF) << 1;
            iFrameLength += (((int)mBuffer[i + 1]) & 0x80) >> 7;
            
            if(0 == iFrameLength) {
                break;
            }
            
            int frameType = (((int)mBuffer[i + 1]) & 0x0F);
            
            Fis f = new Fis(frameType, mBuffer, i + 2, iFrameLength);
            
            Product p = ProductFactory.buildProduct(f.mBuffer);
            mProducts.add(p);
            
            i += iFrameLength + 2;
        }
    }
    
    /**
     * 
     * @return
     */
    public LinkedList<Product> getProducts() {
        return mProducts;
    }
}
