package com.fireboyev.discord.novus.filestorage.config.guild;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class PlaylistOptions {
	@Setting("play")
	private boolean play = true;
	@Setting("edit")
	private boolean edit = true;
	@Setting("view")
	private boolean view = true;

	public boolean canPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

	public boolean canEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean canView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}
}
