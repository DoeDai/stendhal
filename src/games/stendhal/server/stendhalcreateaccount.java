/* $Id$ */
/***************************************************************************
 *                      (C) Copyright 2003 - Marauroa                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server;

import marauroa.common.game.*;
import marauroa.server.game.*;
import games.stendhal.server.entity.*;
import marauroa.common.Log4J;

/** The stendhalcreateaccount extends the createaccount class of marauroa package
 *  so that it defines the specific behaviour for an account of stendhal */
public class stendhalcreateaccount extends marauroa.server.createaccount
  {
  public static void main (String[] args)
    {
    Log4J.init("games/stendhal/stendhalcreateaccount.properties");
    Entity.generateRPClass();
    RPEntity.generateRPClass();
    Player.generateRPClass();

    stendhalcreateaccount instance=new stendhalcreateaccount();
    System.exit(instance.run(args));
    }
  
  public stendhalcreateaccount()
    {
    super();
    }
    
  public RPObject populatePlayerRPObject(IPlayerDatabase playerDatabase) throws Exception
    {
    RPObject object=new RPObject(RPObject.INVALID_ID);
    object.put("type","player");
    object.put("name",get("character"));
    object.put("outfit",0);
    object.put("base_hp",100);
    object.put("hp",100);
    object.put("atk",2);
    object.put("atk_xp",100);
    object.put("def",2);
    object.put("def_xp",100);
    object.put("xp",0);
    
    return object;
    }
  }

