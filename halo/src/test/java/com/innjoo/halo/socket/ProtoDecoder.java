package com.innjoo.halo.socket;

import java.util.List;

import com.innjoo.halo.utils.Encryption;
import com.innjoo.halo.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ProtoDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		
		int packageTotalLen = in.readableBytes();
		if(packageTotalLen >= 14) {
			
			//System.out.println("1-->Decode is running, in length " + in.readableBytes());
			byte[] haloId = new byte[8];
			short package_len = 0;
			in.getBytes(0, haloId);
			package_len = in.getByte(8);
			int data_len = package_len - 14;
			//System.out.println("Decode is running, package len " + in.readableBytes());
			short opType = in.getByte(10);
			short res = in.getByte(12);
			byte[] data = new byte[data_len];
			in.getBytes(14, data);
			
//			byte[] byteCrc = new byte[packageTotalLen - 2];
//			in.getBytes(0, byteCrc, 0, packageTotalLen - 2);
//			System.out.println("Crc====>: " + Encryption.crc16(Utils.getChars(byteCrc), (short)(packageTotalLen - 2)));
//			
			System.out.println(String.format("Decode is running, headerId=%s, package_len=%d, opType=%d, data=%s\n",
					Utils.bytesToHexString(haloId), package_len, opType, Utils.bytesToHexString(data)));
			
			Proto p = new Proto(haloId, package_len, opType, data);
			System.out.println(p.toString());
			out.add(p);   
			
		} else {
			in.resetReaderIndex();
		}
	}
}
