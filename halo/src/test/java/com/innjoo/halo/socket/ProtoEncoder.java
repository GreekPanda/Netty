package com.innjoo.halo.socket;

import com.innjoo.halo.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtoEncoder extends MessageToByteEncoder<Proto> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Proto msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		if (msg == null) {
			throw new Exception("The encode message is null");
		}
		System.out.print("Encode msg:  " + msg.toString());;
		
		out.writeBytes(msg.getHeaderId());
		
		//Get the package length, it has to conver from a byte array to short
		short package_len = msg.getPackage_len();
		byte[] tmplen = new byte[2];
		tmplen[0] = (byte)(package_len & 0xFF);
        // int 倒数第二个字节
		tmplen[1] = (byte)((package_len & 0xFF00) >> 8 );
		out.writeShort(Utils.byte2Short(tmplen));
		
		
		short control_code = msg.getOpType();
		byte[] tmpctrlcode = new byte[2];
		tmpctrlcode[0] = (byte)(control_code & 0xFF);
        // int 倒数第二个字节
		tmpctrlcode[1] = (byte)((control_code & 0xFF00) >> 8 );
		
		out.writeShort(Utils.byte2Short(tmpctrlcode));
		out.writeShort(msg.getRes());
		out.writeBytes(msg.getData());
		
//		byte[] haloId = new byte[8];
//		byte[] data = new byte[msg.getPackage_len()];
		
//		System.out.println(String.format("haloid = %s, package_len = %d, opType=%d, res = %d, data=%s", out.getBytes(0, haloId)  
//				,out.getByte(8) 
//		,out.getByte(10) ,
//		out.getByte(12) 
//		,out.getBytes(14, data)));
		
	}
}
