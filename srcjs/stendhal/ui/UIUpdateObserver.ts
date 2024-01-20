/***************************************************************************
 *                    Copyright © 2024 - Faiumoni e. V.                    *
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License as        *
 *   published by the Free Software Foundation; either version 3 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 ***************************************************************************/

import { ui } from "./UI";


/**
 * Listens for updates to user interface.
 */
export class UIUpdateObserver {

	private startupObserver?: MutationObserver;
	private viewPortObserver?: MutationObserver;

	private static instance: UIUpdateObserver;


	/**
	 * Retrieves the singleton instance.
	 */
	public static get(): UIUpdateObserver {
		if (!UIUpdateObserver.instance) {
			UIUpdateObserver.instance = new UIUpdateObserver();
		}
		return UIUpdateObserver.instance;
	}

	/**
	 * Hidden singleton constructor.
	 */
	private constructor() {
		// do nothing
	}

	/**
	 * Initializes observers for display state & changes to viewport attributes.
	 */
	public init() {
		if (this.viewPortObserver) {
			console.warn("cannot re-initialize UIUpdateObserver");
			return;
		}

		const initialDisplay = this.getClientDisplay();
		if (initialDisplay === "none") {
			this.startupObserver = new MutationObserver((mutations: MutationRecord[]) => {
				// we're listening for only 1 style so list should only contain 1 element
				const mutation = mutations[0];
				if (mutation.attributeName !== "style") {
					console.warn("observer detected wrong attribute: " + mutation.attributeName);
					return;
				}
				if (this.isClientDisplayChanged(initialDisplay)) {
					this.onClientDisplayUpdate();
				}
			});
			this.startupObserver.observe(document.getElementById("client")!,
					{attributes: true, attributeFilter: ["style"]});
		}

		this.viewPortObserver = new MutationObserver((mutations: MutationRecord[]) => {
			this.onViewPortUpdate();
		});
		this.viewPortObserver.observe(document.getElementById("gamewindow")!,
				{attributes: true, attributeFilter: ["left", "right", "top", "bottom", "width", "height"]});
	}

	/**
	 * Retrieves the display style of the client.
	 *
	 * @return
	 *   String representing the client's display state.
	 */
	private getClientDisplay(): string {
		return document.getElementById("client")!.style["display"] || "none";
	}

	/**
	 * Checks if the client's display has changed from its original state.
	 *
	 * @param oldDisplay
	 *   The original display state of the client.
	 * @return
	 *   `true` if new state is not "none".
	 */
	private isClientDisplayChanged(oldDisplay: string): boolean {
		return this.getClientDisplay() !== oldDisplay;
	}

	/**
	 * Called the first time the client's display state is changed (should be from "none" to "block").
	 *
	 * @param oldDisplay
	 *   The display type when the client was first created.
	 * @return
	 *   `true` if the display has changed from "none".
	 */
	private onClientDisplayUpdate() {
		// we shouldn't need to observe any further
		if (this.startupObserver) {
			this.startupObserver.disconnect();
			this.startupObserver = undefined;
		}
		ui.onDisplayReady();
	}

	/**
	 * Called when an attribute of the viewport has changed.
	 */
	private onViewPortUpdate() {
		ui.onDisplayUpdate();
	}
}