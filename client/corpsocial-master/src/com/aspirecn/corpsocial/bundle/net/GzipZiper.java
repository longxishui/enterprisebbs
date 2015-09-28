package com.aspirecn.corpsocial.bundle.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipZiper {
	public final static String PREFERRED_ENCODING = "UTF-8";
	public final static String PREFERRED_ENCODING_GBK = "GBK";
	private static GzipZiper gzipZiper;
	public static GzipZiper getInstance(){
		if(gzipZiper==null){
			gzipZiper = new GzipZiper();
		}
		return gzipZiper;
	}
	public byte[] zipBytes(byte[] bytes) {
		ByteArrayOutputStream baos = null;
		GZIPOutputStream gzos = null;
		try {
			baos = new ByteArrayOutputStream();
			gzos = new GZIPOutputStream(baos);
			gzos.write(bytes, 0, bytes.length);
			gzos.close();
		} catch (IOException e) {
			return null;
		} finally {
			try {
				gzos.close();
			} catch (Exception e) {
			}

			try {
				baos.close();
			} catch (Exception e) {
			}
		}

		return baos.toByteArray();
	}

	public byte[] unZipBytes(byte[] bytes) {
		if (bytes != null && bytes.length >= 4) {

			int head = ((int) bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);
			if (GZIPInputStream.GZIP_MAGIC == head) {
				ByteArrayInputStream bais = null;
				GZIPInputStream gzis = null;
				ByteArrayOutputStream baos = null;
				byte[] buffer = new byte[2048];
				int length = 0;

				try {
					baos = new ByteArrayOutputStream();
					bais = new ByteArrayInputStream(bytes);
					gzis = new GZIPInputStream(bais);

					while ((length = gzis.read(buffer)) >= 0) {
						baos.write(buffer, 0, length);
					} // end while: reading input

					// No error? Get new bytes.
					bytes = baos.toByteArray();

				} // end try
				catch (IOException e) {
					// Just return originally-decoded bytes
				} // end catch
				finally {
					try {
						baos.close();
					} catch (Exception e) {
					}
					try {
						gzis.close();
					} catch (Exception e) {
					}
					try {
						bais.close();
					} catch (Exception e) {
					}
				} // end finally

			} // end if: gzipped
		} // end if: bytes.length >= 2

		return bytes;
	}

	public String unZipBytes(byte[] bytes, String encoding) {
		String rst = null;
		bytes = this.unZipBytes(bytes);
		encoding = (encoding == null || "".equals(encoding)) ? "UTF-8"
				: encoding;
		try {
			rst = new String(bytes, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rst;
	}

}
