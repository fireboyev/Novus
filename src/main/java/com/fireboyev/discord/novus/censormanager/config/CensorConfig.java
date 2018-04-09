package com.fireboyev.discord.novus.censormanager.config;

import java.util.ArrayList;
import java.util.List;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class CensorConfig {
	@Setting("censoredWords")
	public List<String> censoredWords = new ArrayList<String>();
		
	
	@Setting("adminBypass")
	public boolean adminBypass = false;
	@Setting("enabled")
	public boolean isEnabled = false;
	
	@Setting("warn")
	public boolean shouldWarn = false;
	
	@Setting("warnMessage")
	public String warnMessage = "%usermention, Language!";
}
