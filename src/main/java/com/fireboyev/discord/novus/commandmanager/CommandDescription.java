/*
 *     Copyright (C) <2017>  <Evan Penner / fireboyev>
 *
 *  Novus is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Novus is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Novus.  If not, see <http://www.gnu.org/licenses/>.
 */package com.fireboyev.discord.novus.commandmanager;

public class CommandDescription {
	public String name;
	public String discription;
	public Boolean isVisible;
	public String[] aliases;
	public String usage;

	public static CommandDescription getBlank() {
		return new CommandDescription("", "", false, "", "");
	}

	public CommandDescription(String name, String discription, Boolean isVisible, String usage, String... aliases) {
		this.name = name;
		this.discription = discription;
		this.isVisible = isVisible;
		this.aliases = aliases;
		this.usage = usage;
	}

	public CommandDescription(String name, String discription, String[] aliases, String usage) {
		this.name = name;
		this.discription = discription;
		this.isVisible = true;
		this.aliases = aliases;
		this.usage = usage;
	}

	public CommandDescription(String name, String discription, String usage) {
		this.name = name;
		this.discription = discription;
		this.isVisible = true;
		this.aliases = new String[] {};
		this.usage = usage;
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public String getDescription() {
		return discription;
	}

	public Boolean isVisible() {
		return isVisible;
	}

	public String getUsage() {
		return usage;
	}

	@Override
	public String toString() {
		return "CommandDescription:{" + name + ", " + discription + ", " + isVisible + ", " + usage + ", " + aliases
				+ "}";
	}
}
