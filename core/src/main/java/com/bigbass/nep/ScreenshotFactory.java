package com.bigbass.nep;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * @see <a href="https://github.com/libgdx/libgdx/wiki/Take-a-Screenshot">https://github.com/libgdx/libgdx/wiki/Take-a-Screenshot</a>
 */
public class ScreenshotFactory {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM.dd.yyyy-HH.mm.ss.S");
	
	public static void saveScreen(){
		try{
			String d = ".", format = ".png";
			
			Date date = new Date();
			
			String filename = "screenshots/pic" + d + DATE_FORMAT.format(date) + format;
			
            FileHandle fh = new FileHandle(filename);
            
            int w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
            
            Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, w, h);
            
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
        } catch (Exception e){     
        	e.printStackTrace();
        	return;
        }
	}
}
