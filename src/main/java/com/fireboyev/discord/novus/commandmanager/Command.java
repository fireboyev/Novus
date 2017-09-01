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

public class Command {
	String name;
	DefaultExecutor command;
	CommandDescription desc;
	public int executed;

	public Command(String name, DefaultExecutor command, CommandDescription desc) {
		this.name = name;
		this.command = command;
		this.desc = desc;
		executed = 0;
	}

	public String getName() {
		return name;
	}

	public DefaultExecutor getExecutor() {
		return command;
	}

	public CommandDescription getDescription() {
		return desc;
	}

	@Override
	public String toString() {
		return "Command: {" + name + ", " + command.toString() + ", " + desc.toString() + "}";
	}
}
