package cn.fundview.app.tool.file;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.tool.Installation;

/**
 * app 版本处理类
 *
 */
public class VersionUtil {

    /**
     *
     * @param context
     * @param file
     * @return 1 有新的版本
     */
    public static int handleVersionXmlData(Context context, File file) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Map<String, String> versionData = parseVersionXml(inputStream);
            if (versionData != null) {

                String version = versionData.get("version");
                if (version != null) {

                    int versionCode = Integer.parseInt(version);
                    if (versionCode > Installation.getVersionCode(context)) {

                        return 1;//有新的版本
                    } else {

                        return 4;//当前意识最新版本
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (inputStream != null) {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    private static Map<String, String> parseVersionXml(InputStream inputStream)
            throws Exception {

        Map<String, String> map = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "utf-8");
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {
                case XmlPullParser.START_DOCUMENT:

                    break;
                case XmlPullParser.START_TAG:
                    if ("data".equals(parser.getName())) {
                        map = new HashMap<>();
                    } else if ("version".equals(parser.getName())) {

                        String version = parser.nextText();
                        map.put("version", version);
                    } else if ("down".equals(parser.getName())) {

                        String downPath = parser.nextText();
                        map.put("downPath", downPath);
                    }
                    break;
            }
            event = parser.next();
        }

        inputStream.close();
        return map;
    }
}  