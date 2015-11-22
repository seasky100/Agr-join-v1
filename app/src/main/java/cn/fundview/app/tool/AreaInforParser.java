package cn.fundview.app.tool;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Context;
import android.util.Xml;

import cn.fundview.app.model.Area;

public class AreaInforParser {

    public static List<Area> getAreas(Context context, String filename) throws Exception {

        InputStream stream = null;

//		int id = context.getResources().getIdentifier(filename, "raw", context.getPackageName());
        stream = context.getAssets().open("data/global/" + filename + ".xml");

        return loadAreaConfig(stream);

    }

    private static List<Area> loadAreaConfig(InputStream stream) throws Exception {

        List<Area> list = null;
        Area area = null;

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(stream, "utf-8");
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    list = new ArrayList<Area>();
                    break;

                case XmlPullParser.START_TAG:
                    if ("RECORD".equals(parser.getName())) {
                        area = new Area();
                    }
                    if (area != null) {
                        if ("id".equals(parser.getName())) {
                            String id = parser.nextText();
                            if (!id.equals(null) && !id.equals(""))
                                area.setId(Integer.valueOf(id.trim()));
                        }
                        if ("name".equals(parser.getName())) {
                            String name = parser.nextText();
                            if (!name.equals(null) && !name.equals(""))
                                area.setName(name.trim());
                        }

                        if ("parent_id".equals(parser.getName())) {
                            String parentId = parser.nextText();
                            if (!parentId.equals(null) && !parentId.equals(""))
                                area.setParentId(Integer.valueOf(parentId.trim()));
                        }

                        if ("levels".equals(parser.getName())) {
                            String levels = parser.nextText();
                            if (!levels.equals(null) && !levels.equals(""))
                                area.setLevels(Integer.parseInt(levels.trim()));
                        }

                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("RECORD".equals(parser.getName())) {
                        list.add(area);
                        area = null;
                    }
                    break;

            }
            event = parser.next();
        }
        stream.close();
        return list;

    }
}
