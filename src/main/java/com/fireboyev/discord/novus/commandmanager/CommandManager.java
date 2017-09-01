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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommandManager {
	List<Command> commands;
	public int executed;

	public CommandManager() {
		commands = new ArrayList<Command>();
		executed = 0;
	}

	public void registerCommand(String name, CommandDescription desc, DefaultExecutor command) {
		commands.add(new Command(name, command, desc));
	}

	public List<Command> getCommands() {
		return commands;
	}

	public Command getMostPopularCommand() {
		Comparator<Command> comp = new Comparator<Command>() {
			@Override
			public int compare(Command o1, Command o2) {
				if (o1.executed > o2.executed)
					return 1; // highest value first
				if (o1.executed == o2.executed)
					return 0;
				return -1;
			}
		};
		return Collections.max(commands, comp);
	}
}
