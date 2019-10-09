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
 */package com.fireboyev.discord.novus.util;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class ChatBot {
	private AIConfiguration configuration;
	private AIDataService dataService;

	public ChatBot(String token) {
		configuration = new AIConfiguration(token);
		dataService = new AIDataService(configuration);
	}

	public String Chat(String message) {
		boolean enabled = false;
		if (!enabled)
			return "It seems that there was an error while I was thinking.";
		AIRequest request = new AIRequest(message);
		try {
			AIResponse response = dataService.request(request);
			if (response.getStatus().getCode() == 200) {
				return response.getResult().getFulfillment().getSpeech();
			} else {
				System.err.println(response.getStatus().getErrorDetails());
			}
		} catch (AIServiceException e) {
			e.printStackTrace();
			return "It seems that there was an error while I was thinking.";
		}
		return "It seems that there was an error while I was thinking.";
	}
}
