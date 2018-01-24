/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.net.xinyi.xmjt.utils;


import android.content.Context;
import android.widget.ImageView;

import java.util.HashMap;

import cn.net.xinyi.xmjt.R;

public class FileIconHelper {

    private static final String LOG_TAG = "FileIconHelper";

    private static HashMap<ImageView, ImageView> imageFrames = new HashMap<ImageView, ImageView>();

    private static HashMap<String, Integer> fileExtToIcons = new HashMap<String, Integer>();


    static {
        addItem(new String[] {
            "mp3","wma","wav","mid"
        }, R.drawable.documents_icon_music);
        addItem(new String[] {
                "mp4", "wmv", "mpeg", "m4v", "3gp", "3gpp", "3g2", "3gpp2", "asf", "avi", "rmvb"
        }, R.drawable.documents_icon_videonormal);
        addItem(new String[] {
                "jpg", "jpeg", "gif", "png", "bmp", "wbmp"
        }, R.drawable.documents_icon_picturenormal);
        addItem(new String[] {
                "txt", "log", "xml", "ini", "lrc"
        }, R.drawable.documents_icon_text);
        addItem(new String[] {
                "doc", "docx", 
        }, R.drawable.documents_icon_doc);
        addItem(new String[] {
                "ppt", "pptx", 
        }, R.drawable.documents_icon_ppt);
        addItem(new String[] {
                "xls", "xlsx",
        }, R.drawable.documents_icon_xls);
        addItem(new String[] {
            "pdf"
        }, R.drawable.documents_icon_pdf);
        addItem(new String[] {
            "zip"
        }, R.drawable.documents_icon_zip);
        addItem(new String[] {
            "rar"
        }, R.drawable.documents_icon_zip);
    }

    public FileIconHelper(Context context) {
    }

    private static void addItem(String[] exts, int resId) {
        if (exts != null) {
            for (String ext : exts) {
                fileExtToIcons.put(ext.toLowerCase(), resId);
            }
        }
    }

    public static int getFileIcon(String ext) {
        Integer i = fileExtToIcons.get(ext.toLowerCase());
        if (i != null) {
            return i.intValue();
        } else {
            return R.drawable.documents_icon_other;
        }

    }

}