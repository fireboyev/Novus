package com.fireboyev.discord.selfbot;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EvalCommand extends ListenerAdapter {
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().getId().equals("223230587157217280"))
			return;
		final String raw = event.getMessage().getRawContent();
		final String[] parts = raw.split("\\s+", 2);
		if (parts.length < 1 || !parts[0].equals(">eval"))
			return;

		final String input = parts.length > 1 ? parts[1] : "";
		ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
		engine.put("api", event.getJDA());
		engine.put("out", System.out);
		engine.put("event", event);
		engine.put("channel", event.getChannel());
		engine.put("message", event.getMessage());
		engine.put("author", event.getAuthor());
		engine.put("guild", event.getGuild());
		engine.put("textchannel", event.getTextChannel());
		Object o;
		try {
			o = String.valueOf(engine.eval(input));
		} catch (Throwable e) {
			if (e instanceof ScriptException)
				o = e.getCause();
			else
				o = e;
			if (o instanceof Error)
				throw (Error) o;
		}
		if (!o.toString().equals("null"))
			event.getChannel().sendMessage(o.toString()).queue();

	}
}
