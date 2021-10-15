package com.metoo.modul.app.game.tree.tools;

import java.util.Map;

/**
 * <p>
 * 	Title: GameTaskBase.java
 * </p>
 * 
 * <p>
 * 	Description: 发放水滴
 * </p>
 * @author 46075
 *
 */

public interface GameTaskBase {

	boolean grant(Long id);// GameTask  id
	
	String grant1(Map<String, Object> params);// GameTask  id
}
