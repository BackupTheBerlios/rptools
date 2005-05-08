/*
 * $Id: ZoneRendererFactory.java,v 1.3 2005/05/08 18:57:43 tcroft Exp $
 *
 * Copyright (C) 2005, Digital Motorworks LP, a wholly owned subsidiary of ADP.
 * The contents of this file are protected under the copyright laws of the
 * United States of America with all rights reserved. This document is
 * confidential and contains proprietary information. Any unauthorized use or
 * disclosure is expressly prohibited.
 */
package net.rptools.maptool.client;

import java.util.HashMap;
import java.util.Map;

import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Zone;

public class ZoneRendererFactory {

	public static ZoneRenderer newRenderer(Zone zone) {
        
        ZoneRenderer renderer = null;
        switch (zone.getType()) {
        case Zone.Type.INFINITE: {
            renderer = new IndefiniteZoneRenderer(zone);
            break;
        }
        case Zone.Type.MAP:
        default: {
            renderer = new MapZoneRenderer(zone);
        }
        }

        return renderer;
    }
}
